package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.TickContainerAccess;

public class ImposterProtoChunk extends ProtoChunk
{
    private final LevelChunk wrapped;
    private final boolean allowWrites;

    public ImposterProtoChunk(LevelChunk p_187920_, boolean p_187921_)
    {
        super(p_187920_.getPos(), UpgradeData.EMPTY, p_187920_.levelHeightAccessor, p_187920_.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), p_187920_.getBlendingData());
        this.wrapped = p_187920_;
        this.allowWrites = p_187921_;
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pPos)
    {
        return this.wrapped.getBlockEntity(pPos);
    }

    public BlockState getBlockState(BlockPos pPos)
    {
        return this.wrapped.getBlockState(pPos);
    }

    public FluidState getFluidState(BlockPos pPos)
    {
        return this.wrapped.getFluidState(pPos);
    }

    public int getMaxLightLevel()
    {
        return this.wrapped.getMaxLightLevel();
    }

    public LevelChunkSection getSection(int p_187932_)
    {
        return this.allowWrites ? this.wrapped.getSection(p_187932_) : super.getSection(p_187932_);
    }

    @Nullable
    public BlockState setBlockState(BlockPos pPos, BlockState pState, boolean pIsMoving)
    {
        return this.allowWrites ? this.wrapped.setBlockState(pPos, pState, pIsMoving) : null;
    }

    public void setBlockEntity(BlockEntity pBlockEntity)
    {
        if (this.allowWrites)
        {
            this.wrapped.setBlockEntity(pBlockEntity);
        }
    }

    public void addEntity(Entity pEntity)
    {
        if (this.allowWrites)
        {
            this.wrapped.addEntity(pEntity);
        }
    }

    public void setStatus(ChunkStatus pStatus)
    {
        if (this.allowWrites)
        {
            super.setStatus(pStatus);
        }
    }

    public LevelChunkSection[] getSections()
    {
        return this.wrapped.getSections();
    }

    public void a(Heightmap.Types p_62706_, long[] p_62707_)
    {
    }

    private Heightmap.Types fixType(Heightmap.Types pType)
    {
        if (pType == Heightmap.Types.WORLD_SURFACE_WG)
        {
            return Heightmap.Types.WORLD_SURFACE;
        }
        else
        {
            return pType == Heightmap.Types.OCEAN_FLOOR_WG ? Heightmap.Types.OCEAN_FLOOR : pType;
        }
    }

    public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types pType)
    {
        return this.wrapped.getOrCreateHeightmapUnprimed(pType);
    }

    public int getHeight(Heightmap.Types pType, int pX, int pZ)
    {
        return this.wrapped.getHeight(this.fixType(pType), pX, pZ);
    }

    public Biome getNoiseBiome(int pX, int pY, int pZ)
    {
        return this.wrapped.getNoiseBiome(pX, pY, pZ);
    }

    public ChunkPos getPos()
    {
        return this.wrapped.getPos();
    }

    @Nullable
    public StructureStart<?> getStartForFeature(StructureFeature<?> pStructure)
    {
        return this.wrapped.getStartForFeature(pStructure);
    }

    public void setStartForFeature(StructureFeature<?> pStructure, StructureStart<?> pStart)
    {
    }

    public Map < StructureFeature<?>, StructureStart<? >> getAllStarts()
    {
        return this.wrapped.getAllStarts();
    }

    public void setAllStarts(Map < StructureFeature<?>, StructureStart<? >> pStructureStarts)
    {
    }

    public LongSet getReferencesForFeature(StructureFeature<?> pStructure)
    {
        return this.wrapped.getReferencesForFeature(pStructure);
    }

    public void addReferenceForFeature(StructureFeature<?> pStructure, long pReference)
    {
    }

    public Map < StructureFeature<?>, LongSet > getAllReferences()
    {
        return this.wrapped.getAllReferences();
    }

    public void setAllReferences(Map < StructureFeature<?>, LongSet > pStructureReferences)
    {
    }

    public void setUnsaved(boolean pUnsaved)
    {
    }

    public boolean isUnsaved()
    {
        return false;
    }

    public ChunkStatus getStatus()
    {
        return this.wrapped.getStatus();
    }

    public void removeBlockEntity(BlockPos pPos)
    {
    }

    public void markPosForPostprocessing(BlockPos pPos)
    {
    }

    public void setBlockEntityNbt(CompoundTag pTag)
    {
    }

    @Nullable
    public CompoundTag getBlockEntityNbt(BlockPos pPos)
    {
        return this.wrapped.getBlockEntityNbt(pPos);
    }

    @Nullable
    public CompoundTag getBlockEntityNbtForSaving(BlockPos pPos)
    {
        return this.wrapped.getBlockEntityNbtForSaving(pPos);
    }

    public Stream<BlockPos> getLights()
    {
        return this.wrapped.getLights();
    }

    public TickContainerAccess<Block> getBlockTicks()
    {
        return this.allowWrites ? this.wrapped.getBlockTicks() : BlackholeTickAccess.emptyContainer();
    }

    public TickContainerAccess<Fluid> getFluidTicks()
    {
        return this.allowWrites ? this.wrapped.getFluidTicks() : BlackholeTickAccess.emptyContainer();
    }

    public ChunkAccess.TicksToSave getTicksForSerialization()
    {
        return this.wrapped.getTicksForSerialization();
    }

    @Nullable
    public BlendingData getBlendingData()
    {
        return this.wrapped.getBlendingData();
    }

    public void setBlendingData(BlendingData p_187930_)
    {
        this.wrapped.setBlendingData(p_187930_);
    }

    public CarvingMask getCarvingMask(GenerationStep.Carving p_187926_)
    {
        if (this.allowWrites)
        {
            return super.getCarvingMask(p_187926_);
        }
        else
        {
            throw(UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
        }
    }

    public CarvingMask getOrCreateCarvingMask(GenerationStep.Carving p_187934_)
    {
        if (this.allowWrites)
        {
            return super.getOrCreateCarvingMask(p_187934_);
        }
        else
        {
            throw(UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
        }
    }

    public LevelChunk getWrapped()
    {
        return this.wrapped;
    }

    public boolean isLightCorrect()
    {
        return this.wrapped.isLightCorrect();
    }

    public void setLightCorrect(boolean pLightCorrect)
    {
        this.wrapped.setLightCorrect(pLightCorrect);
    }

    public void fillBiomesFromNoise(BiomeResolver p_187923_, Climate.Sampler p_187924_)
    {
        if (this.allowWrites)
        {
            this.wrapped.fillBiomesFromNoise(p_187923_, p_187924_);
        }
    }
}
