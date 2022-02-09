package net.minecraft.world.level.levelgen.feature;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;

public class DripstoneUtils
{
    protected static double getDripstoneHeight(double pRadius, double p_159625_, double pMaxRadius, double p_159627_)
    {
        if (pRadius < p_159627_)
        {
            pRadius = p_159627_;
        }

        double d0 = 0.384D;
        double d1 = pRadius / p_159625_ * 0.384D;
        double d2 = 0.75D * Math.pow(d1, 1.3333333333333333D);
        double d3 = Math.pow(d1, 0.6666666666666666D);
        double d4 = 0.3333333333333333D * Math.log(d1);
        double d5 = pMaxRadius * (d2 - d3 - d4);
        d5 = Math.max(d5, 0.0D);
        return d5 / 0.384D * p_159625_;
    }

    protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel pLevel, BlockPos pPos, int pRadius)
    {
        if (isEmptyOrWaterOrLava(pLevel, pPos))
        {
            return false;
        }
        else
        {
            float f = 6.0F;
            float f1 = 6.0F / (float)pRadius;

            for (float f2 = 0.0F; f2 < ((float)Math.PI * 2F); f2 += f1)
            {
                int i = (int)(Mth.cos(f2) * (float)pRadius);
                int j = (int)(Mth.sin(f2) * (float)pRadius);

                if (isEmptyOrWaterOrLava(pLevel, pPos.offset(i, 0, j)))
                {
                    return false;
                }
            }

            return true;
        }
    }

    protected static boolean isEmptyOrWater(LevelAccessor pLevel, BlockPos pPos)
    {
        return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWater);
    }

    protected static boolean isEmptyOrWaterOrLava(LevelAccessor pLevel, BlockPos pPos)
    {
        return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWaterOrLava);
    }

    protected static void buildBaseToTipColumn(Direction pDirection, int pHeight, boolean pMergeTip, Consumer<BlockState> pBlockSetter)
    {
        if (pHeight >= 3)
        {
            pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.BASE));

            for (int i = 0; i < pHeight - 3; ++i)
            {
                pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.MIDDLE));
            }
        }

        if (pHeight >= 2)
        {
            pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.FRUSTUM));
        }

        if (pHeight >= 1)
        {
            pBlockSetter.accept(createPointedDripstone(pDirection, pMergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }
    }

    protected static void growPointedDripstone(LevelAccessor p_190848_, BlockPos p_190849_, Direction p_190850_, int p_190851_, boolean p_190852_)
    {
        if (isDripstoneBase(p_190848_.getBlockState(p_190849_.relative(p_190850_.getOpposite()))))
        {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = p_190849_.mutable();
            buildBaseToTipColumn(p_190850_, p_190851_, p_190852_, (p_190846_) ->
            {
                if (p_190846_.is(Blocks.POINTED_DRIPSTONE))
                {
                    p_190846_ = p_190846_.setValue(PointedDripstoneBlock.WATERLOGGED, Boolean.valueOf(p_190848_.isWaterAt(blockpos$mutableblockpos)));
                }

                p_190848_.setBlock(blockpos$mutableblockpos, p_190846_, 2);
                blockpos$mutableblockpos.move(p_190850_);
            });
        }
    }

    protected static boolean placeDripstoneBlockIfPossible(LevelAccessor p_190854_, BlockPos p_190855_)
    {
        BlockState blockstate = p_190854_.getBlockState(p_190855_);

        if (blockstate.is(BlockTags.DRIPSTONE_REPLACEABLE))
        {
            p_190854_.setBlock(p_190855_, Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 2);
            return true;
        }
        else
        {
            return false;
        }
    }

    private static BlockState createPointedDripstone(Direction pDirection, DripstoneThickness pDripstoneThickness)
    {
        return Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, pDirection).setValue(PointedDripstoneBlock.THICKNESS, pDripstoneThickness);
    }

    public static boolean isDripstoneBaseOrLava(BlockState pState)
    {
        return isDripstoneBase(pState) || pState.is(Blocks.LAVA);
    }

    public static boolean isDripstoneBase(BlockState pState)
    {
        return pState.is(Blocks.DRIPSTONE_BLOCK) || pState.is(BlockTags.DRIPSTONE_REPLACEABLE);
    }

    public static boolean isEmptyOrWater(BlockState p_159665_)
    {
        return p_159665_.isAir() || p_159665_.is(Blocks.WATER);
    }

    public static boolean isEmptyOrWaterOrLava(BlockState p_159667_)
    {
        return p_159667_.isAir() || p_159667_.is(Blocks.WATER) || p_159667_.is(Blocks.LAVA);
    }
}
