package net.optifine.shaders;

import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

public class ClippingHelperDummy extends Frustum
{
    public ClippingHelperDummy()
    {
        super(new Matrix4f(), new Matrix4f());
    }

    public boolean isVisible(AABB aabbIn)
    {
        return true;
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        return true;
    }
}
