package net.minecraft.world.level.levelgen.structure;

import java.util.Random;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class NetherFossilPieces
{
    private static final ResourceLocation[] FOSSILS = new ResourceLocation[] {new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14")};

    public static void addPieces(StructureManager pStructureManager, StructurePieceAccessor pPieces, Random pRandom, BlockPos pPos)
    {
        Rotation rotation = Rotation.getRandom(pRandom);
        pPieces.addPiece(new NetherFossilPieces.NetherFossilPiece(pStructureManager, Util.a(FOSSILS, pRandom), pPos, rotation));
    }

    public static class NetherFossilPiece extends TemplateStructurePiece
    {
        public NetherFossilPiece(StructureManager pStructureManager, ResourceLocation pLocation, BlockPos pPos, Rotation pRotation)
        {
            super(StructurePieceType.NETHER_FOSSIL, 0, pStructureManager, pLocation, pLocation.toString(), makeSettings(pRotation), pPos);
        }

        public NetherFossilPiece(StructureManager p_192242_, CompoundTag p_192243_)
        {
            super(StructurePieceType.NETHER_FOSSIL, p_192243_, p_192242_, (p_162980_) ->
            {
                return makeSettings(Rotation.valueOf(p_192243_.getString("Rot")));
            });
        }

        private static StructurePlaceSettings makeSettings(Rotation pRotation)
        {
            return (new StructurePlaceSettings()).setRotation(pRotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext p_192253_, CompoundTag p_192254_)
        {
            super.addAdditionalSaveData(p_192253_, p_192254_);
            p_192254_.putString("Rot", this.placeSettings.getRotation().name());
        }

        protected void handleDataMarker(String pMarker, BlockPos pPos, ServerLevelAccessor pLevel, Random pRandom, BoundingBox pBox)
        {
        }

        public void postProcess(WorldGenLevel p_192245_, StructureFeatureManager p_192246_, ChunkGenerator p_192247_, Random p_192248_, BoundingBox p_192249_, ChunkPos p_192250_, BlockPos p_192251_)
        {
            p_192249_.encapsulate(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            super.postProcess(p_192245_, p_192246_, p_192247_, p_192248_, p_192249_, p_192250_, p_192251_);
        }
    }
}
