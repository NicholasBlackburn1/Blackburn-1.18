package net.minecraft.client.multiplayer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagContainer;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;
import net.optifine.Config;
import net.optifine.CustomGuis;
import net.optifine.DynamicLights;
import net.optifine.RandomEntities;
import net.optifine.override.PlayerControllerOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.Shaders;

public class ClientLevel extends Level
{
    private static final double FLUID_PARTICLE_SPAWN_OFFSET = 0.05D;
    private static final int NORMAL_LIGHT_UPDATES_PER_FRAME = 10;
    private static final int LIGHT_UPDATE_QUEUE_SIZE_THRESHOLD = 1000;
    final EntityTickList tickingEntities = new EntityTickList();
    private final TransientEntitySectionManager<Entity> entityStorage = new TransientEntitySectionManager<>(Entity.class, new ClientLevel.EntityCallbacks());
    private final ClientPacketListener connection;
    private final LevelRenderer levelRenderer;
    private final ClientLevel.ClientLevelData clientLevelData;
    private final DimensionSpecialEffects effects;
    private final Minecraft minecraft = Minecraft.getInstance();
    final List<AbstractClientPlayer> players = Lists.newArrayList();
    private Scoreboard scoreboard = new Scoreboard();
    private final Map<String, MapItemSavedData> mapData = Maps.newHashMap();
    private static final long CLOUD_COLOR = 16777215L;
    private int skyFlashTime;
    private final Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches = Util.make(new Object2ObjectArrayMap<>(3), (p_194169_1_) ->
    {
        p_194169_1_.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache((p_194180_1_) -> {
            return this.calculateBlockTint(p_194180_1_, BiomeColors.GRASS_COLOR_RESOLVER);
        }));
        p_194169_1_.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache((p_194176_1_) -> {
            return this.calculateBlockTint(p_194176_1_, BiomeColors.FOLIAGE_COLOR_RESOLVER);
        }));
        p_194169_1_.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache((p_194167_1_) -> {
            return this.calculateBlockTint(p_194167_1_, BiomeColors.WATER_COLOR_RESOLVER);
        }));
    });
    private final ClientChunkCache chunkSource;
    private final Deque<Runnable> lightUpdateQueue = Queues.newArrayDeque();
    private int serverSimulationDistance;
    private static final Set<Item> MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT);
    private boolean playerUpdate = false;

    public ClientLevel(ClientPacketListener p_194129_, ClientLevel.ClientLevelData p_194130_, ResourceKey<Level> p_194131_, DimensionType p_194132_, int p_194133_, int p_194134_, Supplier<ProfilerFiller> p_194135_, LevelRenderer p_194136_, boolean p_194137_, long p_194138_)
    {
        super(p_194130_, p_194131_, p_194132_, p_194135_, true, p_194137_, p_194138_);
        this.connection = p_194129_;
        this.chunkSource = new ClientChunkCache(this, p_194133_);
        this.clientLevelData = p_194130_;
        this.levelRenderer = p_194136_;
        this.effects = DimensionSpecialEffects.forType(p_194132_);
        this.setDefaultSpawnPos(new BlockPos(8, 64, 8), 0.0F);
        this.serverSimulationDistance = p_194134_;
        this.updateSkyBrightness();
        this.prepareWeather();

        if (Reflector.CapabilityProvider_gatherCapabilities.exists())
        {
            Reflector.call(this, Reflector.CapabilityProvider_gatherCapabilities);
        }

        Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, this);

        if (this.minecraft.gameMode != null && this.minecraft.gameMode.getClass() == MultiPlayerGameMode.class)
        {
            this.minecraft.gameMode = new PlayerControllerOF(this.minecraft, this.connection);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.minecraft.gameMode);
        }
    }

    public void queueLightUpdate(Runnable p_194172_)
    {
        this.lightUpdateQueue.add(p_194172_);
    }

    public void pollLightUpdates()
    {
        int i = this.lightUpdateQueue.size();
        int j = i < 1000 ? Math.max(10, i / 10) : i;

        for (int k = 0; k < j; ++k)
        {
            Runnable runnable = this.lightUpdateQueue.poll();

            if (runnable == null)
            {
                break;
            }

            runnable.run();
        }
    }

    public boolean isLightUpdateQueueEmpty()
    {
        return this.lightUpdateQueue.isEmpty();
    }

    public DimensionSpecialEffects effects()
    {
        return this.effects;
    }

    public void tick(BooleanSupplier pHasTimeLeft)
    {
        this.getWorldBorder().tick();
        this.tickTime();
        this.getProfiler().push("blocks");
        this.chunkSource.tick(pHasTimeLeft);
        this.getProfiler().pop();
    }

    private void tickTime()
    {
        this.setGameTime(this.levelData.getGameTime() + 1L);

        if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT))
        {
            this.setDayTime(this.levelData.getDayTime() + 1L);
        }
    }

    public void setGameTime(long pTime)
    {
        this.clientLevelData.setGameTime(pTime);
    }

    public void setDayTime(long pTime)
    {
        if (pTime < 0L)
        {
            pTime = -pTime;
            this.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, (MinecraftServer)null);
        }
        else
        {
            this.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, (MinecraftServer)null);
        }

        this.clientLevelData.setDayTime(pTime);
    }

    public Iterable<Entity> entitiesForRendering()
    {
        return this.getEntities().getAll();
    }

    public void tickEntities()
    {
        ProfilerFiller profilerfiller = this.getProfiler();
        profilerfiller.push("entities");
        this.tickingEntities.forEach((p_194182_1_) ->
        {
            if (!p_194182_1_.isRemoved() && !p_194182_1_.isPassenger())
            {
                this.guardEntityTick(this::tickNonPassenger, p_194182_1_);
            }
        });
        profilerfiller.pop();
        this.tickBlockEntities();
    }

    public boolean shouldTickDeath(Entity p_194185_)
    {
        return p_194185_.chunkPosition().getChessboardDistance(this.minecraft.player.chunkPosition()) <= this.serverSimulationDistance;
    }

    public void tickNonPassenger(Entity p_104640_)
    {
        p_104640_.setOldPosAndRot();
        ++p_104640_.tickCount;
        this.getProfiler().push(() ->
        {
            return Registry.ENTITY_TYPE.getKey(p_104640_.getType()).toString();
        });

        if (ReflectorForge.canUpdate(p_104640_))
        {
            p_104640_.tick();
        }

        if (p_104640_.isRemoved())
        {
            this.onEntityRemoved(p_104640_);
        }

        this.getProfiler().pop();

        for (Entity entity : p_104640_.getPassengers())
        {
            this.tickPassenger(p_104640_, entity);
        }
    }

    private void tickPassenger(Entity pMount, Entity pRider)
    {
        if (!pRider.isRemoved() && pRider.getVehicle() == pMount)
        {
            if (pRider instanceof Player || this.tickingEntities.contains(pRider))
            {
                pRider.setOldPosAndRot();
                ++pRider.tickCount;
                pRider.rideTick();

                for (Entity entity : pRider.getPassengers())
                {
                    this.tickPassenger(pRider, entity);
                }
            }
        }
        else
        {
            pRider.stopRiding();
        }
    }

    public void unload(LevelChunk pChunk)
    {
        pChunk.clearAllBlockEntities();
        this.chunkSource.getLightEngine().enableLightSources(pChunk.getPos(), false);
        this.entityStorage.stopTicking(pChunk.getPos());
    }

    public void onChunkLoaded(ChunkPos pChunkPos)
    {
        this.tintCaches.forEach((p_194152_1_, p_194152_2_) ->
        {
            p_194152_2_.invalidateForChunk(pChunkPos.x, pChunkPos.z);
        });
        this.entityStorage.startTicking(pChunkPos);
    }

    public void clearTintCaches()
    {
        this.tintCaches.forEach((p_194156_0_, p_194156_1_) ->
        {
            p_194156_1_.invalidateAll();
        });
    }

    public boolean hasChunk(int pChunkX, int pChunkZ)
    {
        return true;
    }

    public int getEntityCount()
    {
        return this.entityStorage.count();
    }

    public void addPlayer(int pPlayerId, AbstractClientPlayer pPlayerEntity)
    {
        this.addEntity(pPlayerId, pPlayerEntity);
    }

    public void putNonPlayerEntity(int pEntityId, Entity pEntityToSpawn)
    {
        this.addEntity(pEntityId, pEntityToSpawn);
    }

    private void addEntity(int pEntityId, Entity pEntityToSpawn)
    {
        if (!Reflector.EntityJoinWorldEvent_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.EntityJoinWorldEvent_Constructor, pEntityToSpawn, this))
        {
            this.removeEntity(pEntityId, Entity.RemovalReason.DISCARDED);
            this.entityStorage.addEntity(pEntityToSpawn);

            if (Reflector.IForgeEntity_onAddedToWorld.exists())
            {
                Reflector.call(pEntityToSpawn, Reflector.IForgeEntity_onAddedToWorld);
            }

            this.onEntityAdded(pEntityToSpawn);
        }
    }

    public void removeEntity(int pEntityId, Entity.RemovalReason pReason)
    {
        Entity entity = this.getEntities().get(pEntityId);

        if (entity != null)
        {
            entity.setRemoved(pReason);
            entity.onClientRemoval();
        }
    }

    @Nullable
    public Entity getEntity(int pId)
    {
        return this.getEntities().get(pId);
    }

    public void setKnownState(BlockPos pPos, BlockState pState)
    {
        this.setBlock(pPos, pState, 19);
    }

    public void disconnect()
    {
        this.connection.getConnection().disconnect(new TranslatableComponent("multiplayer.status.quitting"));
    }

    public void animateTick(int pPosX, int pPosY, int pPosZ)
    {
        int i = 32;
        Random random = new Random();
        Block block = this.getMarkerParticleTarget();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < 667; ++j)
        {
            this.doAnimateTick(pPosX, pPosY, pPosZ, 16, random, block, blockpos$mutableblockpos);
            this.doAnimateTick(pPosX, pPosY, pPosZ, 32, random, block, blockpos$mutableblockpos);
        }
    }

    @Nullable
    private Block getMarkerParticleTarget()
    {
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE)
        {
            ItemStack itemstack = this.minecraft.player.getMainHandItem();
            Item item = itemstack.getItem();

            if (MARKER_PARTICLE_ITEMS.contains(item) && item instanceof BlockItem)
            {
                BlockItem blockitem = (BlockItem)item;
                return blockitem.getBlock();
            }
        }

        return null;
    }

    public void doAnimateTick(int p_194143_, int p_194144_, int p_194145_, int p_194146_, Random p_194147_, @Nullable Block p_194148_, BlockPos.MutableBlockPos p_194149_)
    {
        int i = p_194143_ + this.random.nextInt(p_194146_) - this.random.nextInt(p_194146_);
        int j = p_194144_ + this.random.nextInt(p_194146_) - this.random.nextInt(p_194146_);
        int k = p_194145_ + this.random.nextInt(p_194146_) - this.random.nextInt(p_194146_);
        p_194149_.set(i, j, k);
        BlockState blockstate = this.getBlockState(p_194149_);
        blockstate.getBlock().animateTick(blockstate, this, p_194149_, p_194147_);
        FluidState fluidstate = this.getFluidState(p_194149_);

        if (!fluidstate.isEmpty())
        {
            fluidstate.animateTick(this, p_194149_, p_194147_);
            ParticleOptions particleoptions = fluidstate.getDripParticle();

            if (particleoptions != null && this.random.nextInt(10) == 0)
            {
                boolean flag = blockstate.isFaceSturdy(this, p_194149_, Direction.DOWN);
                BlockPos blockpos = p_194149_.below();
                this.trySpawnDripParticles(blockpos, this.getBlockState(blockpos), particleoptions, flag);
            }
        }

        if (p_194148_ == blockstate.getBlock())
        {
            this.addParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, blockstate), (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 0.0D, 0.0D, 0.0D);
        }

        if (!blockstate.isCollisionShapeFullBlock(this, p_194149_))
        {
            this.getBiome(p_194149_).getAmbientParticle().ifPresent((p_194164_2_) ->
            {
                if (p_194164_2_.canSpawn(this.random))
                {
                    this.addParticle(p_194164_2_.getOptions(), (double)p_194149_.getX() + this.random.nextDouble(), (double)p_194149_.getY() + this.random.nextDouble(), (double)p_194149_.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            });
        }
    }

    private void trySpawnDripParticles(BlockPos pBlockPos, BlockState pBlockState, ParticleOptions pParticleData, boolean pShapeDownSolid)
    {
        if (pBlockState.getFluidState().isEmpty())
        {
            VoxelShape voxelshape = pBlockState.getCollisionShape(this, pBlockPos);
            double d0 = voxelshape.max(Direction.Axis.Y);

            if (d0 < 1.0D)
            {
                if (pShapeDownSolid)
                {
                    this.spawnFluidParticle((double)pBlockPos.getX(), (double)(pBlockPos.getX() + 1), (double)pBlockPos.getZ(), (double)(pBlockPos.getZ() + 1), (double)(pBlockPos.getY() + 1) - 0.05D, pParticleData);
                }
            }
            else if (!pBlockState.is(BlockTags.IMPERMEABLE))
            {
                double d1 = voxelshape.min(Direction.Axis.Y);

                if (d1 > 0.0D)
                {
                    this.spawnParticle(pBlockPos, pParticleData, voxelshape, (double)pBlockPos.getY() + d1 - 0.05D);
                }
                else
                {
                    BlockPos blockpos = pBlockPos.below();
                    BlockState blockstate = this.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(this, blockpos);
                    double d2 = voxelshape1.max(Direction.Axis.Y);

                    if (d2 < 1.0D && blockstate.getFluidState().isEmpty())
                    {
                        this.spawnParticle(pBlockPos, pParticleData, voxelshape, (double)pBlockPos.getY() - 0.05D);
                    }
                }
            }
        }
    }

    private void spawnParticle(BlockPos pPos, ParticleOptions pParticleData, VoxelShape pVoxelShape, double pY)
    {
        this.spawnFluidParticle((double)pPos.getX() + pVoxelShape.min(Direction.Axis.X), (double)pPos.getX() + pVoxelShape.max(Direction.Axis.X), (double)pPos.getZ() + pVoxelShape.min(Direction.Axis.Z), (double)pPos.getZ() + pVoxelShape.max(Direction.Axis.Z), pY, pParticleData);
    }

    private void spawnFluidParticle(double pXStart, double p_104594_, double pXEnd, double p_104596_, double pZStart, ParticleOptions p_104598_)
    {
        this.addParticle(p_104598_, Mth.lerp(this.random.nextDouble(), pXStart, p_104594_), pZStart, Mth.lerp(this.random.nextDouble(), pXEnd, p_104596_), 0.0D, 0.0D, 0.0D);
    }

    public CrashReportCategory fillReportDetails(CrashReport pReport)
    {
        CrashReportCategory crashreportcategory = super.fillReportDetails(pReport);
        crashreportcategory.setDetail("Server brand", () ->
        {
            return this.minecraft.player.getServerBrand();
        });
        crashreportcategory.setDetail("Server type", () ->
        {
            return this.minecraft.getSingleplayerServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
        });
        return crashreportcategory;
    }

    public void playSound(@Nullable Player pPlayer, double pX, double p_104647_, double pY, SoundEvent p_104649_, SoundSource pZ, float p_104651_, float pSound)
    {
        if (Reflector.ForgeEventFactory_onPlaySoundAtEntity.exists())
        {
            Object object = Reflector.ForgeEventFactory_onPlaySoundAtEntity.call(pPlayer, p_104649_, pZ, p_104651_, pSound);

            if (Reflector.callBoolean(object, Reflector.Event_isCanceled) || Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getSound) == null)
            {
                return;
            }

            p_104649_ = (SoundEvent)Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getSound);
            pZ = (SoundSource)Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getCategory);
            p_104651_ = Reflector.callFloat(object, Reflector.PlaySoundAtEntityEvent_getVolume);
        }

        if (pPlayer == this.minecraft.player)
        {
            this.playLocalSound(pX, p_104647_, pY, p_104649_, pZ, p_104651_, pSound, false);
        }
    }

    public void playSound(@Nullable Player pPlayer, Entity pEntity, SoundEvent pEvent, SoundSource pCategory, float pVolume, float pPitch)
    {
        if (Reflector.ForgeEventFactory_onPlaySoundAtEntity.exists())
        {
            Object object = Reflector.ForgeEventFactory_onPlaySoundAtEntity.call(pPlayer, pEvent, pCategory, pVolume, pPitch);

            if (Reflector.callBoolean(object, Reflector.Event_isCanceled) || Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getSound) == null)
            {
                return;
            }

            pEvent = (SoundEvent)Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getSound);
            pCategory = (SoundSource)Reflector.call(object, Reflector.PlaySoundAtEntityEvent_getCategory);
            pVolume = Reflector.callFloat(object, Reflector.PlaySoundAtEntityEvent_getVolume);
        }

        if (pPlayer == this.minecraft.player)
        {
            this.minecraft.getSoundManager().play(new EntityBoundSoundInstance(pEvent, pCategory, pVolume, pPitch, pEntity));
        }
    }

    public void playLocalSound(BlockPos pPos, SoundEvent pSound, SoundSource pCategory, float pVolume, float pPitch, boolean pDistanceDelay)
    {
        this.playLocalSound((double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, pSound, pCategory, pVolume, pPitch, pDistanceDelay);
    }

    public void playLocalSound(double pX, double p_104601_, double pY, SoundEvent p_104603_, SoundSource pZ, float p_104605_, float pSound, boolean pCategory)
    {
        double d0 = this.minecraft.gameRenderer.getMainCamera().getPosition().distanceToSqr(pX, p_104601_, pY);
        SimpleSoundInstance simplesoundinstance = new SimpleSoundInstance(p_104603_, pZ, p_104605_, pSound, pX, p_104601_, pY);

        if (pCategory && d0 > 100.0D)
        {
            double d1 = Math.sqrt(d0) / 40.0D;
            this.minecraft.getSoundManager().playDelayed(simplesoundinstance, (int)(d1 * 20.0D));
        }
        else
        {
            this.minecraft.getSoundManager().play(simplesoundinstance);
        }
    }

    public void createFireworks(double pX, double p_104586_, double pY, double p_104588_, double pZ, double p_104590_, @Nullable CompoundTag pMotionX)
    {
        this.minecraft.particleEngine.add(new FireworkParticles.Starter(this, pX, p_104586_, pY, p_104588_, pZ, p_104590_, this.minecraft.particleEngine, pMotionX));
    }

    public void sendPacketToServer(Packet<?> pPacket)
    {
        this.connection.send(pPacket);
    }

    public RecipeManager getRecipeManager()
    {
        return this.connection.getRecipeManager();
    }

    public void setScoreboard(Scoreboard pScoreboard)
    {
        this.scoreboard = pScoreboard;
    }

    public LevelTickAccess<Block> getBlockTicks()
    {
        return BlackholeTickAccess.emptyLevelList();
    }

    public LevelTickAccess<Fluid> getFluidTicks()
    {
        return BlackholeTickAccess.emptyLevelList();
    }

    public ClientChunkCache getChunkSource()
    {
        return this.chunkSource;
    }

    public boolean setBlock(BlockPos pos, BlockState newState, int flags)
    {
        this.playerUpdate = this.isPlayerActing();
        boolean flag = super.setBlock(pos, newState, flags);
        this.playerUpdate = false;
        return flag;
    }

    private boolean isPlayerActing()
    {
        if (this.minecraft.gameMode instanceof PlayerControllerOF)
        {
            PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.minecraft.gameMode;
            return playercontrollerof.isActing();
        }
        else
        {
            return false;
        }
    }

    public boolean isPlayerUpdate()
    {
        return this.playerUpdate;
    }

    public void onEntityAdded(Entity entityIn)
    {
        RandomEntities.entityLoaded(entityIn, this);

        if (Config.isDynamicLights())
        {
            DynamicLights.entityAdded(entityIn, Config.getRenderGlobal());
        }
    }

    public void onEntityRemoved(Entity entityIn)
    {
        RandomEntities.entityUnloaded(entityIn, this);

        if (Config.isDynamicLights())
        {
            DynamicLights.entityRemoved(entityIn, Config.getRenderGlobal());
        }
    }

    @Nullable
    public MapItemSavedData getMapData(String pMapName)
    {
        return this.mapData.get(pMapName);
    }

    public void setMapData(String pMapId, MapItemSavedData pData)
    {
        this.mapData.put(pMapId, pData);
    }

    public int getFreeMapId()
    {
        return 0;
    }

    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    public TagContainer getTagManager()
    {
        return this.connection.getTags();
    }

    public RegistryAccess registryAccess()
    {
        return this.connection.registryAccess();
    }

    public void sendBlockUpdated(BlockPos pPos, BlockState pOldState, BlockState pNewState, int pFlags)
    {
        this.levelRenderer.blockChanged(this, pPos, pOldState, pNewState, pFlags);
    }

    public void setBlocksDirty(BlockPos pBlockPos, BlockState pOldState, BlockState pNewState)
    {
        this.levelRenderer.setBlockDirty(pBlockPos, pOldState, pNewState);
    }

    public void setSectionDirtyWithNeighbors(int pSectionX, int pSectionY, int pSectionZ)
    {
        this.levelRenderer.setSectionDirtyWithNeighbors(pSectionX, pSectionY, pSectionZ);
    }

    public void setLightReady(int p_197406_, int p_197407_)
    {
        LevelChunk levelchunk = this.chunkSource.getChunk(p_197406_, p_197407_, false);

        if (levelchunk != null)
        {
            levelchunk.setClientLightReady(true);
        }
    }

    public void destroyBlockProgress(int pBreakerId, BlockPos pPos, int pProgress)
    {
        this.levelRenderer.destroyBlockProgress(pBreakerId, pPos, pProgress);
    }

    public void globalLevelEvent(int pId, BlockPos pPos, int pData)
    {
        this.levelRenderer.globalLevelEvent(pId, pPos, pData);
    }

    public void levelEvent(@Nullable Player pPlayer, int pType, BlockPos pPos, int pData)
    {
        try
        {
            this.levelRenderer.levelEvent(pPlayer, pType, pPos, pData);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Playing level event");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Level event being played");
            crashreportcategory.setDetail("Block coordinates", CrashReportCategory.formatLocation(this, pPos));
            crashreportcategory.setDetail("Event source", pPlayer);
            crashreportcategory.setDetail("Event type", pType);
            crashreportcategory.setDetail("Event data", pData);
            throw new ReportedException(crashreport);
        }
    }

    public void addParticle(ParticleOptions pParticleData, double pX, double p_104708_, double pY, double p_104710_, double pZ, double p_104712_)
    {
        this.levelRenderer.addParticle(pParticleData, pParticleData.getType().getOverrideLimiter(), pX, p_104708_, pY, p_104710_, pZ, p_104712_);
    }

    public void addParticle(ParticleOptions pParticleData, boolean pForceAlwaysRender, double pX, double p_104717_, double pY, double p_104719_, double pZ, double p_104721_)
    {
        this.levelRenderer.addParticle(pParticleData, pParticleData.getType().getOverrideLimiter() || pForceAlwaysRender, pX, p_104717_, pY, p_104719_, pZ, p_104721_);
    }

    public void addAlwaysVisibleParticle(ParticleOptions pParticleData, double pX, double p_104768_, double pY, double p_104770_, double pZ, double p_104772_)
    {
        this.levelRenderer.addParticle(pParticleData, false, true, pX, p_104768_, pY, p_104770_, pZ, p_104772_);
    }

    public void addAlwaysVisibleParticle(ParticleOptions pParticleData, boolean pIgnoreRange, double pX, double p_104777_, double pY, double p_104779_, double pZ, double p_104781_)
    {
        this.levelRenderer.addParticle(pParticleData, pParticleData.getType().getOverrideLimiter() || pIgnoreRange, true, pX, p_104777_, pY, p_104779_, pZ, p_104781_);
    }

    public List<AbstractClientPlayer> players()
    {
        return this.players;
    }

    public Biome getUncachedNoiseBiome(int pX, int pY, int pZ)
    {
        return this.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOrThrow(Biomes.PLAINS);
    }

    public float getSkyDarken(float pPartialTick)
    {
        float f = this.getTimeOfDay(pPartialTick);
        float f1 = 1.0F - (Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(this.getRainLevel(pPartialTick) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderLevel(pPartialTick) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    public Vec3 getSkyColor(Vec3 pPos, float pPartialTick)
    {
        float f = this.getTimeOfDay(pPartialTick);
        Vec3 vec3 = pPos.subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
        BiomeManager biomemanager = this.getBiomeManager();
        Vec3 vec31 = CubicSampler.gaussianSampleVec3(vec3, (p_194159_1_, p_194159_2_, p_194159_3_) ->
        {
            return Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_194159_1_, p_194159_2_, p_194159_3_).getSkyColor());
        });
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = (float)vec31.x * f1;
        float f3 = (float)vec31.y * f1;
        float f4 = (float)vec31.z * f1;
        float f5 = this.getRainLevel(pPartialTick);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.75F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        float f9 = this.getThunderLevel(pPartialTick);

        if (f9 > 0.0F)
        {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.75F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        if (!this.minecraft.options.hideLightningFlashes && this.skyFlashTime > 0)
        {
            float f11 = (float)this.skyFlashTime - pPartialTick;

            if (f11 > 1.0F)
            {
                f11 = 1.0F;
            }

            f11 *= 0.45F;
            f2 = f2 * (1.0F - f11) + 0.8F * f11;
            f3 = f3 * (1.0F - f11) + 0.8F * f11;
            f4 = f4 * (1.0F - f11) + 1.0F * f11;
        }

        return new Vec3((double)f2, (double)f3, (double)f4);
    }

    public Vec3 getCloudColor(float pPartialTick)
    {
        float f = this.getTimeOfDay(pPartialTick);
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = this.getRainLevel(pPartialTick);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.95F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        f2 *= f1 * 0.9F + 0.1F;
        f3 *= f1 * 0.9F + 0.1F;
        f4 *= f1 * 0.85F + 0.15F;
        float f9 = this.getThunderLevel(pPartialTick);

        if (f9 > 0.0F)
        {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.95F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        return new Vec3((double)f2, (double)f3, (double)f4);
    }

    public float getStarBrightness(float pPartialTick)
    {
        float f = this.getTimeOfDay(pPartialTick);
        float f1 = 1.0F - (Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        return f1 * f1 * 0.5F;
    }

    public int getSkyFlashTime()
    {
        return this.skyFlashTime;
    }

    public void setSkyFlashTime(int pTimeFlash)
    {
        this.skyFlashTime = pTimeFlash;
    }

    public float getShade(Direction pDirection, boolean pShade)
    {
        boolean flag = this.effects().constantAmbientLight();
        boolean flag1 = Config.isShaders();

        if (!pShade)
        {
            return flag ? 0.9F : 1.0F;
        }
        else
        {
            switch (pDirection)
            {
                case DOWN:
                    return flag ? 0.9F : (flag1 ? Shaders.blockLightLevel05 : 0.5F);

                case UP:
                    return flag ? 0.9F : 1.0F;

                case NORTH:
                case SOUTH:
                    if (Config.isShaders())
                    {
                        return Shaders.blockLightLevel08;
                    }

                    return 0.8F;

                case WEST:
                case EAST:
                    if (Config.isShaders())
                    {
                        return Shaders.blockLightLevel06;
                    }

                    return 0.6F;

                default:
                    return 1.0F;
            }
        }
    }

    public int getBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver)
    {
        BlockTintCache blocktintcache = this.tintCaches.get(pColorResolver);
        return blocktintcache.getColor(pBlockPos);
    }

    public int calculateBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver)
    {
        int i = Minecraft.getInstance().options.biomeBlendRadius;

        if (i == 0)
        {
            return pColorResolver.getColor(this.getBiome(pBlockPos), (double)pBlockPos.getX(), (double)pBlockPos.getZ());
        }
        else
        {
            int j = (i * 2 + 1) * (i * 2 + 1);
            int k = 0;
            int l = 0;
            int i1 = 0;
            Cursor3D cursor3d = new Cursor3D(pBlockPos.getX() - i, pBlockPos.getY(), pBlockPos.getZ() - i, pBlockPos.getX() + i, pBlockPos.getY(), pBlockPos.getZ() + i);
            int j1;

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(); cursor3d.advance(); i1 += j1 & 255)
            {
                blockpos$mutableblockpos.set(cursor3d.nextX(), cursor3d.nextY(), cursor3d.nextZ());
                j1 = pColorResolver.getColor(this.getBiome(blockpos$mutableblockpos), (double)blockpos$mutableblockpos.getX(), (double)blockpos$mutableblockpos.getZ());
                k += (j1 & 16711680) >> 16;
                l += (j1 & 65280) >> 8;
            }

            return (k / j & 255) << 16 | (l / j & 255) << 8 | i1 / j & 255;
        }
    }

    public BlockPos getSharedSpawnPos()
    {
        BlockPos blockpos = new BlockPos(this.levelData.getXSpawn(), this.levelData.getYSpawn(), this.levelData.getZSpawn());

        if (!this.getWorldBorder().isWithinBounds(blockpos))
        {
            blockpos = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockpos;
    }

    public float getSharedSpawnAngle()
    {
        return this.levelData.getSpawnAngle();
    }

    public void setDefaultSpawnPos(BlockPos pSpawnPos, float pSpawnAngle)
    {
        this.levelData.setSpawn(pSpawnPos, pSpawnAngle);
    }

    public String toString()
    {
        return "ClientLevel";
    }

    public ClientLevel.ClientLevelData getLevelData()
    {
        return this.clientLevelData;
    }

    public void gameEvent(@Nullable Entity pEntity, GameEvent pEvent, BlockPos pPos)
    {
    }

    protected Map<String, MapItemSavedData> getAllMapData()
    {
        return ImmutableMap.copyOf(this.mapData);
    }

    protected void addMapData(Map<String, MapItemSavedData> pMap)
    {
        this.mapData.putAll(pMap);
    }

    protected LevelEntityGetter<Entity> getEntities()
    {
        return this.entityStorage.getEntityGetter();
    }

    public String gatherChunkSourceStats()
    {
        return "Chunks[C] W: " + this.chunkSource.gatherStats() + " E: " + this.entityStorage.gatherStats();
    }

    public void addDestroyBlockEffect(BlockPos pPos, BlockState pState)
    {
        this.minecraft.particleEngine.destroy(pPos, pState);
    }

    public void setServerSimulationDistance(int p_194175_)
    {
        this.serverSimulationDistance = p_194175_;
    }

    public int getServerSimulationDistance()
    {
        return this.serverSimulationDistance;
    }

    public boolean shouldDelayFallingBlockEntityRemoval(Entity.RemovalReason p_194151_)
    {
        return p_194151_ == Entity.RemovalReason.DISCARDED && this.minecraft.options.prioritizeChunkUpdates != PrioritizeChunkUpdates.NEARBY;
    }

    public TransientEntitySectionManager getEntityStorage()
    {
        return this.entityStorage;
    }

    public EntitySectionStorage getSectionStorage()
    {
        return EntitySection.getSectionStorage(this.entityStorage);
    }

    public static class ClientLevelData implements WritableLevelData
    {
        private final boolean hardcore;
        private final GameRules gameRules;
        private final boolean isFlat;
        private int xSpawn;
        private int ySpawn;
        private int zSpawn;
        private float spawnAngle;
        private long gameTime;
        private long dayTime;
        private boolean raining;
        private Difficulty difficulty;
        private boolean difficultyLocked;

        public ClientLevelData(Difficulty pDifficulty, boolean pHardcore, boolean pIsFlat)
        {
            this.difficulty = pDifficulty;
            this.hardcore = pHardcore;
            this.isFlat = pIsFlat;
            this.gameRules = new GameRules();
        }

        public int getXSpawn()
        {
            return this.xSpawn;
        }

        public int getYSpawn()
        {
            return this.ySpawn;
        }

        public int getZSpawn()
        {
            return this.zSpawn;
        }

        public float getSpawnAngle()
        {
            return this.spawnAngle;
        }

        public long getGameTime()
        {
            return this.gameTime;
        }

        public long getDayTime()
        {
            return this.dayTime;
        }

        public void setXSpawn(int pX)
        {
            this.xSpawn = pX;
        }

        public void setYSpawn(int pY)
        {
            this.ySpawn = pY;
        }

        public void setZSpawn(int pZ)
        {
            this.zSpawn = pZ;
        }

        public void setSpawnAngle(float pAngle)
        {
            this.spawnAngle = pAngle;
        }

        public void setGameTime(long pGameTime)
        {
            this.gameTime = pGameTime;
        }

        public void setDayTime(long pDayTime)
        {
            this.dayTime = pDayTime;
        }

        public void setSpawn(BlockPos pSpawnPoint, float pAngle)
        {
            this.xSpawn = pSpawnPoint.getX();
            this.ySpawn = pSpawnPoint.getY();
            this.zSpawn = pSpawnPoint.getZ();
            this.spawnAngle = pAngle;
        }

        public boolean isThundering()
        {
            return false;
        }

        public boolean isRaining()
        {
            return this.raining;
        }

        public void setRaining(boolean pIsRaining)
        {
            this.raining = pIsRaining;
        }

        public boolean isHardcore()
        {
            return this.hardcore;
        }

        public GameRules getGameRules()
        {
            return this.gameRules;
        }

        public Difficulty getDifficulty()
        {
            return this.difficulty;
        }

        public boolean isDifficultyLocked()
        {
            return this.difficultyLocked;
        }

        public void fillCrashReportCategory(CrashReportCategory pCrashReportCategory, LevelHeightAccessor pLevel)
        {
            WritableLevelData.super.fillCrashReportCategory(pCrashReportCategory, pLevel);
        }

        public void setDifficulty(Difficulty pDifficulty)
        {
            Reflector.ForgeHooks_onDifficultyChange.callVoid(pDifficulty, this.difficulty);
            this.difficulty = pDifficulty;
        }

        public void setDifficultyLocked(boolean pDifficultyLocked)
        {
            this.difficultyLocked = pDifficultyLocked;
        }

        public double getHorizonHeight(LevelHeightAccessor pLevel)
        {
            return this.isFlat ? (double)pLevel.getMinBuildHeight() : 63.0D;
        }

        public double getClearColorScale()
        {
            return this.isFlat ? 1.0D : 0.03125D;
        }
    }

    final class EntityCallbacks implements LevelCallback<Entity>
    {
        public void onCreated(Entity p_171696_)
        {
        }

        public void onDestroyed(Entity p_171700_)
        {
        }

        public void onTickingStart(Entity p_171704_)
        {
            ClientLevel.this.tickingEntities.add(p_171704_);
        }

        public void onTickingEnd(Entity p_171708_)
        {
            ClientLevel.this.tickingEntities.remove(p_171708_);
        }

        public void onTrackingStart(Entity p_171712_)
        {
            if (p_171712_ instanceof AbstractClientPlayer)
            {
                ClientLevel.this.players.add((AbstractClientPlayer)p_171712_);
            }
        }

        public void onTrackingEnd(Entity p_171716_)
        {
            p_171716_.unRide();
            ClientLevel.this.players.remove(p_171716_);

            if (Reflector.IForgeEntity_onRemovedFromWorld.exists())
            {
                Reflector.call(p_171716_, Reflector.IForgeEntity_onRemovedFromWorld);
            }

            if (Reflector.EntityLeaveWorldEvent_Constructor.exists())
            {
                Reflector.postForgeBusEvent(Reflector.EntityLeaveWorldEvent_Constructor, p_171716_, ClientLevel.this);
            }

            ClientLevel.this.onEntityRemoved(p_171716_);
        }
    }
}
