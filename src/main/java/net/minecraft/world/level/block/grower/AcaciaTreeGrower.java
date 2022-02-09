package net.minecraft.world.level.block.grower;

import java.util.Random;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AcaciaTreeGrower extends AbstractTreeGrower
{
    protected ConfiguredFeature <? , ? > getConfiguredFeature(Random pRandom, boolean pLargeHive)
    {
        return TreeFeatures.ACACIA;
    }
}
