package net.minecraft.server.level;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.core.SectionPos;
import net.minecraft.util.SortedArraySet;
import net.minecraft.util.thread.ProcessorHandle;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.optifine.reflect.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DistanceManager
{
    static final Logger LOGGER = LogManager.getLogger();
    private static final int ENTITY_TICKING_RANGE = 2;
    static final int PLAYER_TICKET_LEVEL = 33 + ChunkStatus.getDistance(ChunkStatus.FULL) - 2;
    private static final int INITIAL_TICKET_LIST_CAPACITY = 4;
    private static final int ENTITY_TICKING_LEVEL_THRESHOLD = 32;
    private static final int BLOCK_TICKING_LEVEL_THRESHOLD = 33;
    final Long2ObjectMap<ObjectSet<ServerPlayer>> playersPerChunk = new Long2ObjectOpenHashMap<>();
    final Long2ObjectOpenHashMap < SortedArraySet < Ticket<? >>> tickets = new Long2ObjectOpenHashMap<>();
    private final DistanceManager.ChunkTicketTracker ticketTracker = new DistanceManager.ChunkTicketTracker();
    private final DistanceManager.FixedPlayerDistanceChunkTracker naturalSpawnChunkCounter = new DistanceManager.FixedPlayerDistanceChunkTracker(8);
    private final TickingTracker tickingTicketsTracker = new TickingTracker();
    private final DistanceManager.PlayerTicketTracker playerTicketManager = new DistanceManager.PlayerTicketTracker(65);
    final Set<ChunkHolder> chunksToUpdateFutures = Sets.newHashSet();
    final ChunkTaskPriorityQueueSorter ticketThrottler;
    final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> ticketThrottlerInput;
    final ProcessorHandle<ChunkTaskPriorityQueueSorter.Release> ticketThrottlerReleaser;
    final LongSet ticketsToRelease = new LongOpenHashSet();
    final Executor mainThreadExecutor;
    private long ticketTickCounter;
    private int simulationDistance = 10;
    private final Long2ObjectOpenHashMap < SortedArraySet < Ticket<? >>> forcedTickets = new Long2ObjectOpenHashMap<>();

    protected DistanceManager(Executor p_140774_, Executor p_140775_)
    {
        ProcessorHandle<Runnable> processorhandle = ProcessorHandle.of("player ticket throttler", p_140775_::execute);
        ChunkTaskPriorityQueueSorter chunktaskpriorityqueuesorter = new ChunkTaskPriorityQueueSorter(ImmutableList.of(processorhandle), p_140774_, 4);
        this.ticketThrottler = chunktaskpriorityqueuesorter;
        this.ticketThrottlerInput = chunktaskpriorityqueuesorter.getProcessor(processorhandle, true);
        this.ticketThrottlerReleaser = chunktaskpriorityqueuesorter.getReleaseProcessor(processorhandle);
        this.mainThreadExecutor = p_140775_;
    }

    protected void purgeStaleTickets()
    {
        ++this.ticketTickCounter;
        ObjectIterator < Entry < SortedArraySet < Ticket<? >>> > objectiterator = this.tickets.long2ObjectEntrySet().fastIterator();

        while (objectiterator.hasNext())
        {
            Entry < SortedArraySet < Ticket<? >>> entry = objectiterator.next();
            Iterator < Ticket<? >> iterator = entry.getValue().iterator();
            boolean flag = false;

            while (iterator.hasNext())
            {
                Ticket<?> ticket = iterator.next();

                if (ticket.timedOut(this.ticketTickCounter))
                {
                    iterator.remove();
                    flag = true;
                    this.tickingTicketsTracker.removeTicket(entry.getLongKey(), ticket);
                }
            }

            if (flag)
            {
                this.ticketTracker.update(entry.getLongKey(), getTicketLevelAt(entry.getValue()), false);
            }

            if (entry.getValue().isEmpty())
            {
                objectiterator.remove();
            }
        }
    }

    private static int getTicketLevelAt(SortedArraySet < Ticket<? >> p_140798_)
    {
        return !p_140798_.isEmpty() ? p_140798_.first().getTicketLevel() : ChunkMap.MAX_CHUNK_DISTANCE + 1;
    }

    protected abstract boolean isChunkToRemove(long p_140779_);

    @Nullable
    protected abstract ChunkHolder getChunk(long pChunkPos);

    @Nullable
    protected abstract ChunkHolder updateChunkScheduling(long pChunkPos, int p_140781_, @Nullable ChunkHolder pNewLevel, int pHolder);

    public boolean runAllUpdates(ChunkMap pChunkManager)
    {
        this.naturalSpawnChunkCounter.runAllUpdates();
        this.tickingTicketsTracker.runAllUpdates();
        this.playerTicketManager.runAllUpdates();
        int i = Integer.MAX_VALUE - this.ticketTracker.runDistanceUpdates(Integer.MAX_VALUE);
        boolean flag = i != 0;

        if (flag)
        {
        }

        if (!this.chunksToUpdateFutures.isEmpty())
        {
            this.chunksToUpdateFutures.forEach((p_183906_2_) ->
            {
                p_183906_2_.updateFutures(pChunkManager, this.mainThreadExecutor);
            });
            this.chunksToUpdateFutures.clear();
            return true;
        }
        else
        {
            if (!this.ticketsToRelease.isEmpty())
            {
                LongIterator longiterator = this.ticketsToRelease.iterator();

                while (longiterator.hasNext())
                {
                    long j = longiterator.nextLong();

                    if (this.getTickets(j).stream().anyMatch((p_183909_0_) ->
                {
                    return p_183909_0_.getType() == TicketType.PLAYER;
                    }))
                    {
                        ChunkHolder chunkholder = pChunkManager.getUpdatingChunkIfPresent(j);

                        if (chunkholder == null)
                        {
                            throw new IllegalStateException();
                        }

                        CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> completablefuture = chunkholder.getEntityTickingChunkFuture();
                        completablefuture.thenAccept((p_183903_3_) ->
                        {
                            this.mainThreadExecutor.execute(() -> {
                                this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {
                                }, j, false));
                            });
                        });
                    }
                }

                this.ticketsToRelease.clear();
            }

            return flag;
        }
    }

    void addTicket(long pChunkPos, Ticket<?> p_140786_)
    {
        SortedArraySet < Ticket<? >> sortedarrayset = this.getTickets(pChunkPos);
        int i = getTicketLevelAt(sortedarrayset);
        Ticket<?> ticket = sortedarrayset.addOrGet(p_140786_);
        ticket.setCreatedTick(this.ticketTickCounter);

        if (p_140786_.getTicketLevel() < i)
        {
            this.ticketTracker.update(pChunkPos, p_140786_.getTicketLevel(), true);
        }

        if (Reflector.callBoolean(p_140786_, Reflector.ForgeTicket_isForceTicks))
        {
            SortedArraySet < Ticket<? >> sortedarrayset1 = this.forcedTickets.computeIfAbsent(pChunkPos, (e) ->
            {
                return SortedArraySet.create(4);
            });
            sortedarrayset1.addOrGet(ticket);
        }
    }

    void removeTicket(long pChunkPos, Ticket<?> p_140820_)
    {
        SortedArraySet < Ticket<? >> sortedarrayset = this.getTickets(pChunkPos);

        if (sortedarrayset.remove(p_140820_))
        {
        }

        if (sortedarrayset.isEmpty())
        {
            this.tickets.remove(pChunkPos);
        }

        this.ticketTracker.update(pChunkPos, getTicketLevelAt(sortedarrayset), false);

        if (Reflector.callBoolean(p_140820_, Reflector.ForgeTicket_isForceTicks))
        {
            SortedArraySet < Ticket<? >> sortedarrayset1 = this.forcedTickets.get(pChunkPos);

            if (sortedarrayset1 != null)
            {
                sortedarrayset1.remove(p_140820_);
            }
        }
    }

    public <T> void addTicket(TicketType<T> pType, ChunkPos pPos, int pLevel, T pValue)
    {
        this.addTicket(pPos.toLong(), new Ticket<>(pType, pLevel, pValue));
    }

    public <T> void removeTicket(TicketType<T> pType, ChunkPos pPos, int pLevel, T pValue)
    {
        Ticket<T> ticket = new Ticket<>(pType, pLevel, pValue);
        this.removeTicket(pPos.toLong(), ticket);
    }

    public <T> void addRegionTicket(TicketType<T> pType, ChunkPos pPos, int pDistance, T pValue)
    {
        Ticket<T> ticket = new Ticket<>(pType, 33 - pDistance, pValue);
        long i = pPos.toLong();
        this.addTicket(i, ticket);
        this.tickingTicketsTracker.addTicket(i, ticket);
    }

    public <T> void removeRegionTicket(TicketType<T> pType, ChunkPos pPos, int pDistance, T pValue)
    {
        Ticket<T> ticket = new Ticket<>(pType, 33 - pDistance, pValue);
        long i = pPos.toLong();
        this.removeTicket(i, ticket);
        this.tickingTicketsTracker.removeTicket(i, ticket);
    }

    private SortedArraySet < Ticket<? >> getTickets(long p_140858_)
    {
        return this.tickets.computeIfAbsent(p_140858_, (p_183922_0_) ->
        {
            return SortedArraySet.create(4);
        });
    }

    protected void updateChunkForced(ChunkPos pPos, boolean pAdd)
    {
        Ticket<ChunkPos> ticket = new Ticket<>(TicketType.FORCED, 31, pPos);
        long i = pPos.toLong();

        if (pAdd)
        {
            this.addTicket(i, ticket);
            this.tickingTicketsTracker.addTicket(i, ticket);
        }
        else
        {
            this.removeTicket(i, ticket);
            this.tickingTicketsTracker.removeTicket(i, ticket);
        }
    }

    public void addPlayer(SectionPos pSectionPos, ServerPlayer pPlayer)
    {
        ChunkPos chunkpos = pSectionPos.chunk();
        long i = chunkpos.toLong();
        this.playersPerChunk.computeIfAbsent(i, (p_183920_0_) ->
        {
            return new ObjectOpenHashSet();
        }).add(pPlayer);
        this.naturalSpawnChunkCounter.update(i, 0, true);
        this.playerTicketManager.update(i, 0, true);
        this.tickingTicketsTracker.addTicket(TicketType.PLAYER, chunkpos, this.getPlayerTicketLevel(), chunkpos);
    }

    public void removePlayer(SectionPos pSectionPos, ServerPlayer pPlayer)
    {
        ChunkPos chunkpos = pSectionPos.chunk();
        long i = chunkpos.toLong();
        ObjectSet<ServerPlayer> objectset = this.playersPerChunk.get(i);
        objectset.remove(pPlayer);

        if (objectset.isEmpty())
        {
            this.playersPerChunk.remove(i);
            this.naturalSpawnChunkCounter.update(i, Integer.MAX_VALUE, false);
            this.playerTicketManager.update(i, Integer.MAX_VALUE, false);
            this.tickingTicketsTracker.removeTicket(TicketType.PLAYER, chunkpos, this.getPlayerTicketLevel(), chunkpos);
        }
    }

    private int getPlayerTicketLevel()
    {
        return Math.max(0, 31 - this.simulationDistance);
    }

    public boolean inEntityTickingRange(long p_183914_)
    {
        return this.tickingTicketsTracker.getLevel(p_183914_) < 32;
    }

    public boolean inBlockTickingRange(long p_183917_)
    {
        return this.tickingTicketsTracker.getLevel(p_183917_) < 33;
    }

    protected String getTicketDebugString(long p_140839_)
    {
        SortedArraySet < Ticket<? >> sortedarrayset = this.tickets.get(p_140839_);
        return sortedarrayset != null && !sortedarrayset.isEmpty() ? sortedarrayset.first().toString() : "no_ticket";
    }

    protected void updatePlayerTickets(int pViewDistance)
    {
        this.playerTicketManager.updateViewDistance(pViewDistance);
    }

    public void updateSimulationDistance(int p_183912_)
    {
        if (p_183912_ != this.simulationDistance)
        {
            this.simulationDistance = p_183912_;
            this.tickingTicketsTracker.replacePlayerTicketsLevel(this.getPlayerTicketLevel());
        }
    }

    public int getNaturalSpawnChunkCount()
    {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.size();
    }

    public boolean hasPlayersNearby(long pChunkPos)
    {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.containsKey(pChunkPos);
    }

    public String getDebugStatus()
    {
        return this.ticketThrottler.getDebugStatus();
    }

    private void dumpTickets(String p_143208_)
    {
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(new File(p_143208_));

            try
            {
                for (Entry < SortedArraySet < Ticket<? >>> entry : this.tickets.long2ObjectEntrySet())
                {
                    ChunkPos chunkpos = new ChunkPos(entry.getLongKey());

                    for (Ticket<?> ticket : entry.getValue())
                    {
                        fileoutputstream.write((chunkpos.x + "\t" + chunkpos.z + "\t" + ticket.getType() + "\t" + ticket.getTicketLevel() + "\t\n").getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
            catch (Throwable throwable1)
            {
                try
                {
                    fileoutputstream.close();
                }
                catch (Throwable throwable2)
                {
                    throwable1.addSuppressed(throwable2);
                }

                throw throwable1;
            }

            fileoutputstream.close();
        }
        catch (IOException ioexception1)
        {
            LOGGER.error(ioexception1);
        }
    }

    @VisibleForTesting
    TickingTracker tickingTracker()
    {
        return this.tickingTicketsTracker;
    }

    public <T> void registerTicking(TicketType<T> type, ChunkPos pos, int distance, T value)
    {
        Ticket ticket = (Ticket)Reflector.ForgeTicket_Constructor.newInstance(type, 33 - distance, value, true);
        this.addTicket(pos.toLong(), ticket);
    }

    public <T> void releaseTicking(TicketType<T> type, ChunkPos pos, int distance, T value)
    {
        Ticket ticket = (Ticket)Reflector.ForgeTicket_Constructor.newInstance(type, 33 - distance, value, true);
        this.removeTicket(pos.toLong(), ticket);
    }

    public boolean shouldForceTicks(long chunkPos)
    {
        SortedArraySet < Ticket<? >> sortedarrayset = this.forcedTickets.get(chunkPos);
        return sortedarrayset != null && !sortedarrayset.isEmpty();
    }

    class ChunkTicketTracker extends ChunkTracker
    {
        public ChunkTicketTracker()
        {
            super(ChunkMap.MAX_CHUNK_DISTANCE + 2, 256, 256);
        }

        protected int getLevelFromSource(long pPos)
        {
            SortedArraySet < Ticket<? >> sortedarrayset = DistanceManager.this.tickets.get(pPos);

            if (sortedarrayset == null)
            {
                return Integer.MAX_VALUE;
            }
            else
            {
                return sortedarrayset.isEmpty() ? Integer.MAX_VALUE : sortedarrayset.first().getTicketLevel();
            }
        }

        protected int getLevel(long pSectionPos)
        {
            if (!DistanceManager.this.isChunkToRemove(pSectionPos))
            {
                ChunkHolder chunkholder = DistanceManager.this.getChunk(pSectionPos);

                if (chunkholder != null)
                {
                    return chunkholder.getTicketLevel();
                }
            }

            return ChunkMap.MAX_CHUNK_DISTANCE + 1;
        }

        protected void setLevel(long pSectionPos, int p_140881_)
        {
            ChunkHolder chunkholder = DistanceManager.this.getChunk(pSectionPos);
            int i = chunkholder == null ? ChunkMap.MAX_CHUNK_DISTANCE + 1 : chunkholder.getTicketLevel();

            if (i != p_140881_)
            {
                chunkholder = DistanceManager.this.updateChunkScheduling(pSectionPos, p_140881_, chunkholder, i);

                if (chunkholder != null)
                {
                    DistanceManager.this.chunksToUpdateFutures.add(chunkholder);
                }
            }
        }

        public int runDistanceUpdates(int p_140878_)
        {
            return this.runUpdates(p_140878_);
        }
    }

    class FixedPlayerDistanceChunkTracker extends ChunkTracker
    {
        protected final Long2ByteMap chunks = new Long2ByteOpenHashMap();
        protected final int maxDistance;

        protected FixedPlayerDistanceChunkTracker(int p_140891_)
        {
            super(p_140891_ + 2, 2048, 2048);
            this.maxDistance = p_140891_;
            this.chunks.defaultReturnValue((byte)(p_140891_ + 2));
        }

        protected int getLevel(long pSectionPos)
        {
            return this.chunks.get(pSectionPos);
        }

        protected void setLevel(long pSectionPos, int p_140894_)
        {
            byte b0;

            if (p_140894_ > this.maxDistance)
            {
                b0 = this.chunks.remove(pSectionPos);
            }
            else
            {
                b0 = this.chunks.put(pSectionPos, (byte)p_140894_);
            }

            this.onLevelChange(pSectionPos, b0, p_140894_);
        }

        protected void onLevelChange(long pChunkPos, int p_140896_, int pOldLevel)
        {
        }

        protected int getLevelFromSource(long pPos)
        {
            return this.havePlayer(pPos) ? 0 : Integer.MAX_VALUE;
        }

        private boolean havePlayer(long pChunkPos)
        {
            ObjectSet<ServerPlayer> objectset = DistanceManager.this.playersPerChunk.get(pChunkPos);
            return objectset != null && !objectset.isEmpty();
        }

        public void runAllUpdates()
        {
            this.runUpdates(Integer.MAX_VALUE);
        }

        private void dumpChunks(String p_143213_)
        {
            try
            {
                FileOutputStream fileoutputstream = new FileOutputStream(new File(p_143213_));

                try
                {
                    for (it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry entry : this.chunks.long2ByteEntrySet())
                    {
                        ChunkPos chunkpos = new ChunkPos(entry.getLongKey());
                        String s = Byte.toString(entry.getByteValue());
                        fileoutputstream.write((chunkpos.x + "\t" + chunkpos.z + "\t" + s + "\n").getBytes(StandardCharsets.UTF_8));
                    }
                }
                catch (Throwable throwable1x)
                {
                    try
                    {
                        fileoutputstream.close();
                    }
                    catch (Throwable throwable1)
                    {
                        throwable1x.addSuppressed(throwable1);
                    }

                    throw throwable1x;
                }

                fileoutputstream.close();
            }
            catch (IOException ioexception1)
            {
                DistanceManager.LOGGER.error(ioexception1);
            }
        }
    }

    class PlayerTicketTracker extends DistanceManager.FixedPlayerDistanceChunkTracker
    {
        private int viewDistance;
        private final Long2IntMap queueLevels = Long2IntMaps.synchronize(new Long2IntOpenHashMap());
        private final LongSet toUpdate = new LongOpenHashSet();

        protected PlayerTicketTracker(int p_140910_)
        {
            super(p_140910_);
            this.viewDistance = 0;
            this.queueLevels.defaultReturnValue(p_140910_ + 2);
        }

        protected void onLevelChange(long pChunkPos, int p_140916_, int pOldLevel)
        {
            this.toUpdate.add(pChunkPos);
        }

        public void updateViewDistance(int pViewDistance)
        {
            for (it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry entry : this.chunks.long2ByteEntrySet())
            {
                byte b0 = entry.getByteValue();
                long i = entry.getLongKey();
                this.onLevelChange(i, b0, this.haveTicketFor(b0), b0 <= pViewDistance - 2);
            }

            this.viewDistance = pViewDistance;
        }

        private void onLevelChange(long p_140919_, int p_140920_, boolean p_140921_, boolean p_140922_)
        {
            if (p_140921_ != p_140922_)
            {
                Ticket<?> ticket = new Ticket<>(TicketType.PLAYER, DistanceManager.PLAYER_TICKET_LEVEL, new ChunkPos(p_140919_));

                if (p_140922_)
                {
                    DistanceManager.this.ticketThrottlerInput.tell(ChunkTaskPriorityQueueSorter.message(() ->
                    {
                        DistanceManager.this.mainThreadExecutor.execute(() -> {
                            if (this.haveTicketFor(this.getLevel(p_140919_)))
                            {
                                DistanceManager.this.addTicket(p_140919_, ticket);
                                DistanceManager.this.ticketsToRelease.add(p_140919_);
                            }
                            else {
                                DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {
                                }, p_140919_, false));
                            }
                        });
                    }, p_140919_, () ->
                    {
                        return p_140920_;
                    }));
                }
                else
                {
                    DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() ->
                    {
                        DistanceManager.this.mainThreadExecutor.execute(() -> {
                            DistanceManager.this.removeTicket(p_140919_, ticket);
                        });
                    }, p_140919_, true));
                }
            }
        }

        public void runAllUpdates()
        {
            super.runAllUpdates();

            if (!this.toUpdate.isEmpty())
            {
                LongIterator longiterator = this.toUpdate.iterator();

                while (longiterator.hasNext())
                {
                    long i = longiterator.nextLong();
                    int j = this.queueLevels.get(i);
                    int k = this.getLevel(i);

                    if (j != k)
                    {
                        DistanceManager.this.ticketThrottler.onLevelChange(new ChunkPos(i), () ->
                        {
                            return this.queueLevels.get(i);
                        }, k, (p_140926_3_) ->
                        {
                            if (p_140926_3_ >= this.queueLevels.defaultReturnValue())
                            {
                                this.queueLevels.remove(i);
                            }
                            else {
                                this.queueLevels.put(i, p_140926_3_);
                            }
                        });
                        this.onLevelChange(i, k, this.haveTicketFor(j), this.haveTicketFor(k));
                    }
                }

                this.toUpdate.clear();
            }
        }

        private boolean haveTicketFor(int p_140933_)
        {
            return p_140933_ <= this.viewDistance - 2;
        }
    }
}
