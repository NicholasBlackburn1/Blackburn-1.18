package net.minecraft.client.renderer.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class RenderRegionCache
{
    private final Long2ObjectMap<RenderRegionCache.ChunkInfo> chunkInfoCache = new Long2ObjectOpenHashMap<>();

    @Nullable
    public RenderChunkRegion createRegion(Level p_200466_, BlockPos p_200467_, BlockPos p_200468_, int p_200469_)
    {
        return this.createRegion(p_200466_, p_200467_, p_200468_, p_200469_, true);
    }

    public RenderChunkRegion createRegion(Level worldIn, BlockPos from, BlockPos to, int padding, boolean checkEmpty)
    {
        int i = SectionPos.blockToSectionCoord(from.getX() - padding);
        int j = SectionPos.blockToSectionCoord(from.getZ() - padding);
        int k = SectionPos.blockToSectionCoord(to.getX() + padding);
        int l = SectionPos.blockToSectionCoord(to.getZ() + padding);
        RenderRegionCache.ChunkInfo[][] arenderregioncache$chunkinfo = new RenderRegionCache.ChunkInfo[k - i + 1][l - j + 1];

        for (int i1 = i; i1 <= k; ++i1)
        {
            for (int j1 = j; j1 <= l; ++j1)
            {
                arenderregioncache$chunkinfo[i1 - i][j1 - j] = this.chunkInfoCache.computeIfAbsent(ChunkPos.asLong(i1, j1), (p_200462_1_) ->
                {
                    return new RenderRegionCache.ChunkInfo(worldIn.getChunk(ChunkPos.getX(p_200462_1_), ChunkPos.getZ(p_200462_1_)));
                });
            }
        }

        if (checkEmpty && a(from, to, i, j, arenderregioncache$chunkinfo))
        {
            return null;
        }
        else
        {
            RenderChunk[][] arenderchunk = new RenderChunk[k - i + 1][l - j + 1];

            for (int l1 = i; l1 <= k; ++l1)
            {
                for (int k1 = j; k1 <= l; ++k1)
                {
                    arenderchunk[l1 - i][k1 - j] = arenderregioncache$chunkinfo[l1 - i][k1 - j].renderChunk();
                }
            }

            return new RenderChunkRegion(worldIn, i, j, arenderchunk);
        }
    }

    private static boolean a(BlockPos p_200471_, BlockPos p_200472_, int p_200473_, int p_200474_, RenderRegionCache.ChunkInfo[][] p_200475_)
    {
        int i = SectionPos.blockToSectionCoord(p_200471_.getX());
        int j = SectionPos.blockToSectionCoord(p_200471_.getZ());
        int k = SectionPos.blockToSectionCoord(p_200472_.getX());
        int l = SectionPos.blockToSectionCoord(p_200472_.getZ());

        for (int i1 = i; i1 <= k; ++i1)
        {
            for (int j1 = j; j1 <= l; ++j1)
            {
                LevelChunk levelchunk = p_200475_[i1 - p_200473_][j1 - p_200474_].chunk();

                if (!levelchunk.isYSpaceEmpty(p_200471_.getY(), p_200472_.getY()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    static final class ChunkInfo
    {
        private final LevelChunk chunk;
        @Nullable
        private RenderChunk renderChunk;

        ChunkInfo(LevelChunk p_200479_)
        {
            this.chunk = p_200479_;
        }

        public LevelChunk chunk()
        {
            return this.chunk;
        }

        public RenderChunk renderChunk()
        {
            if (this.renderChunk == null)
            {
                this.renderChunk = new RenderChunk(this.chunk);
            }

            return this.renderChunk;
        }
    }
}
