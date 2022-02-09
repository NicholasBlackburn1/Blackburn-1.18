package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ScatteredOreFeature extends Feature<OreConfiguration>
{
    private static final int MAX_DIST_FROM_ORIGIN = 7;

    ScatteredOreFeature(Codec<OreConfiguration> p_160304_)
    {
        super(p_160304_);
    }

    public boolean place(FeaturePlaceContext<OreConfiguration> pContext)
    {
        WorldGenLevel worldgenlevel = pContext.level();
        Random random = pContext.random();
        OreConfiguration oreconfiguration = pContext.config();
        BlockPos blockpos = pContext.origin();
        int i = random.nextInt(oreconfiguration.size + 1);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < i; ++j)
        {
            this.offsetTargetPos(blockpos$mutableblockpos, random, blockpos, Math.min(j, 7));
            BlockState blockstate = worldgenlevel.getBlockState(blockpos$mutableblockpos);

            for (OreConfiguration.TargetBlockState oreconfiguration$targetblockstate : oreconfiguration.targetStates)
            {
                if (OreFeature.canPlaceOre(blockstate, worldgenlevel::getBlockState, random, oreconfiguration, oreconfiguration$targetblockstate, blockpos$mutableblockpos))
                {
                    worldgenlevel.setBlock(blockpos$mutableblockpos, oreconfiguration$targetblockstate.state, 2);
                    break;
                }
            }
        }

        return true;
    }

    private void offsetTargetPos(BlockPos.MutableBlockPos pMutablePos, Random pRandom, BlockPos pPos, int pMagnitude)
    {
        int i = this.getRandomPlacementInOneAxisRelativeToOrigin(pRandom, pMagnitude);
        int j = this.getRandomPlacementInOneAxisRelativeToOrigin(pRandom, pMagnitude);
        int k = this.getRandomPlacementInOneAxisRelativeToOrigin(pRandom, pMagnitude);
        pMutablePos.setWithOffset(pPos, i, j, k);
    }

    private int getRandomPlacementInOneAxisRelativeToOrigin(Random pRandom, int pMagnitude)
    {
        return Math.round((pRandom.nextFloat() - pRandom.nextFloat()) * (float)pMagnitude);
    }
}
