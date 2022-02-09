package net.minecraft.client.renderer.culling;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.world.phys.AABB;
import net.optifine.render.ICamera;

public class Frustum implements ICamera
{
    public static final int OFFSET_STEP = 4;
    protected final Vector4f[] frustumData = new Vector4f[6];
    protected Vector4f viewVector;
    private double camX;
    private double camY;
    private double camZ;
    public boolean disabled = false;

    public Frustum(Matrix4f p_113000_, Matrix4f p_113001_)
    {
        this.calculateFrustum(p_113000_, p_113001_);
    }

    public Frustum(Frustum p_194440_)
    {
        System.arraycopy(p_194440_.frustumData, 0, this.frustumData, 0, p_194440_.frustumData.length);
        this.camX = p_194440_.camX;
        this.camY = p_194440_.camY;
        this.camZ = p_194440_.camZ;
        this.viewVector = p_194440_.viewVector;
    }

    public Frustum offsetToFullyIncludeCameraCube(int p_194442_)
    {
        double d0 = Math.floor(this.camX / (double)p_194442_) * (double)p_194442_;
        double d1 = Math.floor(this.camY / (double)p_194442_) * (double)p_194442_;
        double d2 = Math.floor(this.camZ / (double)p_194442_) * (double)p_194442_;
        double d3 = Math.ceil(this.camX / (double)p_194442_) * (double)p_194442_;
        double d4 = Math.ceil(this.camY / (double)p_194442_) * (double)p_194442_;
        int i = 0;

        for (double d5 = Math.ceil(this.camZ / (double)p_194442_) * (double)p_194442_; !this.cubeCompletelyInFrustum((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)); this.camZ -= (double)(this.viewVector.z() * 4.0F))
        {
            this.camX -= (double)(this.viewVector.x() * 4.0F);
            this.camY -= (double)(this.viewVector.y() * 4.0F);

            if (i++ > 10)
            {
                break;
            }
        }

        return this;
    }

    public void prepare(double pCamX, double p_113004_, double pCamY)
    {
        this.camX = pCamX;
        this.camY = p_113004_;
        this.camZ = pCamY;
    }

    private void calculateFrustum(Matrix4f pProjection, Matrix4f pFrustrumMatrix)
    {
        Matrix4f matrix4f = pFrustrumMatrix.copy();
        matrix4f.multiply(pProjection);
        matrix4f.transpose();
        this.viewVector = new Vector4f(0.0F, 0.0F, 1.0F, 0.0F);
        this.viewVector.transform(matrix4f);
        this.getPlane(matrix4f, -1, 0, 0, 0);
        this.getPlane(matrix4f, 1, 0, 0, 1);
        this.getPlane(matrix4f, 0, -1, 0, 2);
        this.getPlane(matrix4f, 0, 1, 0, 3);
        this.getPlane(matrix4f, 0, 0, -1, 4);
        this.getPlane(matrix4f, 0, 0, 1, 5);
    }

    private void getPlane(Matrix4f pFrustrumMatrix, int pX, int pY, int pZ, int pId)
    {
        Vector4f vector4f = new Vector4f((float)pX, (float)pY, (float)pZ, 1.0F);
        vector4f.transform(pFrustrumMatrix);
        vector4f.normalize();
        this.frustumData[pId] = vector4f;
    }

    public boolean isVisible(AABB pAabb)
    {
        return this.cubeInFrustum(pAabb.minX, pAabb.minY, pAabb.minZ, pAabb.maxX, pAabb.maxY, pAabb.maxZ);
    }

    private boolean cubeInFrustum(double pMinX, double p_113008_, double pMinY, double p_113010_, double pMinZ, double p_113012_)
    {
        if (this.disabled)
        {
            return true;
        }
        else
        {
            float f = (float)(pMinX - this.camX);
            float f1 = (float)(p_113008_ - this.camY);
            float f2 = (float)(pMinY - this.camZ);
            float f3 = (float)(p_113010_ - this.camX);
            float f4 = (float)(pMinZ - this.camY);
            float f5 = (float)(p_113012_ - this.camZ);
            return this.cubeInFrustum(f, f1, f2, f3, f4, f5);
        }
    }

    private boolean cubeInFrustum(float pMinX, float p_113015_, float pMinY, float p_113017_, float pMinZ, float p_113019_)
    {
        for (int i = 0; i < 6; ++i)
        {
            Vector4f vector4f = this.frustumData[i];
            float f = vector4f.x();
            float f1 = vector4f.y();
            float f2 = vector4f.z();
            float f3 = vector4f.w();

            if (f * pMinX + f1 * p_113015_ + f2 * pMinY + f3 <= 0.0F && f * p_113017_ + f1 * p_113015_ + f2 * pMinY + f3 <= 0.0F && f * pMinX + f1 * pMinZ + f2 * pMinY + f3 <= 0.0F && f * p_113017_ + f1 * pMinZ + f2 * pMinY + f3 <= 0.0F && f * pMinX + f1 * p_113015_ + f2 * p_113019_ + f3 <= 0.0F && f * p_113017_ + f1 * p_113015_ + f2 * p_113019_ + f3 <= 0.0F && f * pMinX + f1 * pMinZ + f2 * p_113019_ + f3 <= 0.0F && f * p_113017_ + f1 * pMinZ + f2 * p_113019_ + f3 <= 0.0F)
            {
                return false;
            }
        }

        return true;
    }

    private boolean cubeCompletelyInFrustum(float p_194444_, float p_194445_, float p_194446_, float p_194447_, float p_194448_, float p_194449_)
    {
        for (int i = 0; i < 6; ++i)
        {
            Vector4f vector4f = this.frustumData[i];

            if (vector4f.dot(new Vector4f(p_194444_, p_194445_, p_194446_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194447_, p_194445_, p_194446_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194444_, p_194448_, p_194446_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194447_, p_194448_, p_194446_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194444_, p_194445_, p_194449_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194447_, p_194445_, p_194449_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194444_, p_194448_, p_194449_, 1.0F)) <= 0.0F)
            {
                return false;
            }

            if (vector4f.dot(new Vector4f(p_194447_, p_194448_, p_194449_, 1.0F)) <= 0.0F)
            {
                return false;
            }
        }

        return true;
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        if (this.disabled)
        {
            return true;
        }
        else
        {
            float f = (float)minX;
            float f1 = (float)minY;
            float f2 = (float)minZ;
            float f3 = (float)maxX;
            float f4 = (float)maxY;
            float f5 = (float)maxZ;

            for (int i = 0; i < 6; ++i)
            {
                Vector4f vector4f = this.frustumData[i];
                float f6 = vector4f.x();
                float f7 = vector4f.y();
                float f8 = vector4f.z();
                float f9 = vector4f.w();

                if (i < 4)
                {
                    if (f6 * f + f7 * f1 + f8 * f2 + f9 <= 0.0F || f6 * f3 + f7 * f1 + f8 * f2 + f9 <= 0.0F || f6 * f + f7 * f4 + f8 * f2 + f9 <= 0.0F || f6 * f3 + f7 * f4 + f8 * f2 + f9 <= 0.0F || f6 * f + f7 * f1 + f8 * f5 + f9 <= 0.0F || f6 * f3 + f7 * f1 + f8 * f5 + f9 <= 0.0F || f6 * f + f7 * f4 + f8 * f5 + f9 <= 0.0F || f6 * f3 + f7 * f4 + f8 * f5 + f9 <= 0.0F)
                    {
                        return false;
                    }
                }
                else if (f6 * f + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f2 + f9 <= 0.0F && f6 * f + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f1 + f8 * f5 + f9 <= 0.0F && f6 * f + f7 * f4 + f8 * f5 + f9 <= 0.0F && f6 * f3 + f7 * f4 + f8 * f5 + f9 <= 0.0F)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public double getCameraX()
    {
        return this.camX;
    }

    public double getCameraY()
    {
        return this.camY;
    }

    public double getCameraZ()
    {
        return this.camZ;
    }

	@Override
	public void setCameraPosition(double var1, double var3, double var5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBoundingBoxInFrustum(AABB var1) {
		// TODO Auto-generated method stub
		return false;
	}
}
