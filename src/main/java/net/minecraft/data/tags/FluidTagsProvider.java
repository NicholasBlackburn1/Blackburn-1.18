package net.minecraft.data.tags;

import java.nio.file.Path;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTagsProvider extends TagsProvider<Fluid>
{
    public FluidTagsProvider(DataGenerator pGenerator)
    {
        super(pGenerator, Registry.FLUID);
    }

    protected void addTags()
    {
        this.tag(FluidTags.WATER).a(Fluids.WATER, Fluids.FLOWING_WATER);
        this.tag(FluidTags.LAVA).a(Fluids.LAVA, Fluids.FLOWING_LAVA);
    }

    protected Path getPath(ResourceLocation pId)
    {
        return this.generator.getOutputFolder().resolve("data/" + pId.getNamespace() + "/tags/fluids/" + pId.getPath() + ".json");
    }

    public String getName()
    {
        return "Fluid Tags";
    }
}
