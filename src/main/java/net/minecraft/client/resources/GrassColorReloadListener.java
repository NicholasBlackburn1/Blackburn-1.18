package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GrassColor;

public class GrassColorReloadListener extends SimplePreparableReloadListener<int[]>
{
    private static final ResourceLocation LOCATION = new ResourceLocation("textures/colormap/grass.png");

    protected int[] prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler)
    {
        try
        {
            return LegacyStuffWrapper.getPixels(pResourceManager, LOCATION);
        }
        catch (IOException ioexception)
        {
            throw new IllegalStateException("Failed to load grass color texture", ioexception);
        }
    }

    protected void apply(int[] p_118684_, ResourceManager p_118685_, ProfilerFiller p_118686_)
    {
        GrassColor.a(p_118684_);
    }
}
