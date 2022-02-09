package net.minecraft.world.level.block.grower;

import java.util.Random;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class JungleTreeGrower extends AbstractMegaTreeGrower
{
    protected ConfiguredFeature <? , ? > getConfiguredFeature(Random pRandom, boolean pLargeHive)
    {
        return TreeFeatures.JUNGLE_TREE_NO_VINE;
    }

    protected ConfiguredFeature <? , ? > getConfiguredMegaFeature(Random pRand)
    {
        return TreeFeatures.MEGA_JUNGLE_TREE;
    }
}
