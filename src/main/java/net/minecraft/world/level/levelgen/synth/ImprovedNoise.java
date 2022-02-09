package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.RandomSource;

public final class ImprovedNoise
{
    private static final float SHIFT_UP_EPSILON = 1.0E-7F;
    private final byte[] p;
    public final double xo;
    public final double yo;
    public final double zo;

    public ImprovedNoise(RandomSource pRandomSource)
    {
        this.xo = pRandomSource.nextDouble() * 256.0D;
        this.yo = pRandomSource.nextDouble() * 256.0D;
        this.zo = pRandomSource.nextDouble() * 256.0D;
        this.p = new byte[256];

        for (int i = 0; i < 256; ++i)
        {
            this.p[i] = (byte)i;
        }

        for (int k = 0; k < 256; ++k)
        {
            int j = pRandomSource.nextInt(256 - k);
            byte b0 = this.p[k];
            this.p[k] = this.p[k + j];
            this.p[k + j] = b0;
        }
    }

    public double noise(double pX, double p_164310_, double pY)
    {
        return this.noise(pX, p_164310_, pY, 0.0D, 0.0D);
    }

    @Deprecated
    public double noise(double pX, double p_75329_, double pY, double p_75331_, double pZ)
    {
        double d0 = pX + this.xo;
        double d1 = p_75329_ + this.yo;
        double d2 = pY + this.zo;
        int i = Mth.floor(d0);
        int j = Mth.floor(d1);
        int k = Mth.floor(d2);
        double d3 = d0 - (double)i;
        double d4 = d1 - (double)j;
        double d5 = d2 - (double)k;
        double d6;

        if (p_75331_ != 0.0D)
        {
            double d7;

            if (pZ >= 0.0D && pZ < d4)
            {
                d7 = pZ;
            }
            else
            {
                d7 = d4;
            }

            d6 = (double)Mth.floor(d7 / p_75331_ + (double)1.0E-7F) * p_75331_;
        }
        else
        {
            d6 = 0.0D;
        }

        return this.sampleAndLerp(i, j, k, d3, d4 - d6, d5, d4);
    }

    public double a(double p_164313_, double p_164314_, double p_164315_, double[] p_164316_)
    {
        double d0 = p_164313_ + this.xo;
        double d1 = p_164314_ + this.yo;
        double d2 = p_164315_ + this.zo;
        int i = Mth.floor(d0);
        int j = Mth.floor(d1);
        int k = Mth.floor(d2);
        double d3 = d0 - (double)i;
        double d4 = d1 - (double)j;
        double d5 = d2 - (double)k;
        return this.a(i, j, k, d3, d4, d5, p_164316_);
    }

    private static double gradDot(int pGradIndex, double pXFactor, double p_75338_, double pYFactor)
    {
        return SimplexNoise.a(SimplexNoise.GRADIENT[pGradIndex & 15], pXFactor, p_75338_, pYFactor);
    }

    private int p(int pIndex)
    {
        return this.p[pIndex & 255] & 255;
    }

    private double sampleAndLerp(int pGridX, int pGridY, int pGridZ, double pDeltaX, double p_164322_, double pWeirdDeltaY, double p_164324_)
    {
        int i = this.p(pGridX);
        int j = this.p(pGridX + 1);
        int k = this.p(i + pGridY);
        int l = this.p(i + pGridY + 1);
        int i1 = this.p(j + pGridY);
        int j1 = this.p(j + pGridY + 1);
        double d0 = gradDot(this.p(k + pGridZ), pDeltaX, p_164322_, pWeirdDeltaY);
        double d1 = gradDot(this.p(i1 + pGridZ), pDeltaX - 1.0D, p_164322_, pWeirdDeltaY);
        double d2 = gradDot(this.p(l + pGridZ), pDeltaX, p_164322_ - 1.0D, pWeirdDeltaY);
        double d3 = gradDot(this.p(j1 + pGridZ), pDeltaX - 1.0D, p_164322_ - 1.0D, pWeirdDeltaY);
        double d4 = gradDot(this.p(k + pGridZ + 1), pDeltaX, p_164322_, pWeirdDeltaY - 1.0D);
        double d5 = gradDot(this.p(i1 + pGridZ + 1), pDeltaX - 1.0D, p_164322_, pWeirdDeltaY - 1.0D);
        double d6 = gradDot(this.p(l + pGridZ + 1), pDeltaX, p_164322_ - 1.0D, pWeirdDeltaY - 1.0D);
        double d7 = gradDot(this.p(j1 + pGridZ + 1), pDeltaX - 1.0D, p_164322_ - 1.0D, pWeirdDeltaY - 1.0D);
        double d8 = Mth.smoothstep(pDeltaX);
        double d9 = Mth.smoothstep(p_164324_);
        double d10 = Mth.smoothstep(pWeirdDeltaY);
        return Mth.lerp3(d8, d9, d10, d0, d1, d2, d3, d4, d5, d6, d7);
    }

    private double a(int p_164326_, int p_164327_, int p_164328_, double p_164329_, double p_164330_, double p_164331_, double[] p_164332_)
    {
        int i = this.p(p_164326_);
        int j = this.p(p_164326_ + 1);
        int k = this.p(i + p_164327_);
        int l = this.p(i + p_164327_ + 1);
        int i1 = this.p(j + p_164327_);
        int j1 = this.p(j + p_164327_ + 1);
        int k1 = this.p(k + p_164328_);
        int l1 = this.p(i1 + p_164328_);
        int i2 = this.p(l + p_164328_);
        int j2 = this.p(j1 + p_164328_);
        int k2 = this.p(k + p_164328_ + 1);
        int l2 = this.p(i1 + p_164328_ + 1);
        int i3 = this.p(l + p_164328_ + 1);
        int j3 = this.p(j1 + p_164328_ + 1);
        int[] aint = SimplexNoise.GRADIENT[k1 & 15];
        int[] aint1 = SimplexNoise.GRADIENT[l1 & 15];
        int[] aint2 = SimplexNoise.GRADIENT[i2 & 15];
        int[] aint3 = SimplexNoise.GRADIENT[j2 & 15];
        int[] aint4 = SimplexNoise.GRADIENT[k2 & 15];
        int[] aint5 = SimplexNoise.GRADIENT[l2 & 15];
        int[] aint6 = SimplexNoise.GRADIENT[i3 & 15];
        int[] aint7 = SimplexNoise.GRADIENT[j3 & 15];
        double d0 = SimplexNoise.a(aint, p_164329_, p_164330_, p_164331_);
        double d1 = SimplexNoise.a(aint1, p_164329_ - 1.0D, p_164330_, p_164331_);
        double d2 = SimplexNoise.a(aint2, p_164329_, p_164330_ - 1.0D, p_164331_);
        double d3 = SimplexNoise.a(aint3, p_164329_ - 1.0D, p_164330_ - 1.0D, p_164331_);
        double d4 = SimplexNoise.a(aint4, p_164329_, p_164330_, p_164331_ - 1.0D);
        double d5 = SimplexNoise.a(aint5, p_164329_ - 1.0D, p_164330_, p_164331_ - 1.0D);
        double d6 = SimplexNoise.a(aint6, p_164329_, p_164330_ - 1.0D, p_164331_ - 1.0D);
        double d7 = SimplexNoise.a(aint7, p_164329_ - 1.0D, p_164330_ - 1.0D, p_164331_ - 1.0D);
        double d8 = Mth.smoothstep(p_164329_);
        double d9 = Mth.smoothstep(p_164330_);
        double d10 = Mth.smoothstep(p_164331_);
        double d11 = Mth.lerp3(d8, d9, d10, (double)aint[0], (double)aint1[0], (double)aint2[0], (double)aint3[0], (double)aint4[0], (double)aint5[0], (double)aint6[0], (double)aint7[0]);
        double d12 = Mth.lerp3(d8, d9, d10, (double)aint[1], (double)aint1[1], (double)aint2[1], (double)aint3[1], (double)aint4[1], (double)aint5[1], (double)aint6[1], (double)aint7[1]);
        double d13 = Mth.lerp3(d8, d9, d10, (double)aint[2], (double)aint1[2], (double)aint2[2], (double)aint3[2], (double)aint4[2], (double)aint5[2], (double)aint6[2], (double)aint7[2]);
        double d14 = Mth.lerp2(d9, d10, d1 - d0, d3 - d2, d5 - d4, d7 - d6);
        double d15 = Mth.lerp2(d10, d8, d2 - d0, d6 - d4, d3 - d1, d7 - d5);
        double d16 = Mth.lerp2(d8, d9, d4 - d0, d5 - d1, d6 - d2, d7 - d3);
        double d17 = Mth.smoothstepDerivative(p_164329_);
        double d18 = Mth.smoothstepDerivative(p_164330_);
        double d19 = Mth.smoothstepDerivative(p_164331_);
        double d20 = d11 + d17 * d14;
        double d21 = d12 + d18 * d15;
        double d22 = d13 + d19 * d16;
        p_164332_[0] += d20;
        p_164332_[1] += d21;
        p_164332_[2] += d22;
        return Mth.lerp3(d8, d9, d10, d0, d1, d2, d3, d4, d5, d6, d7);
    }

    @VisibleForTesting
    public void parityConfigString(StringBuilder p_192824_)
    {
        NoiseUtils.a(p_192824_, this.xo, this.yo, this.zo, this.p);
    }
}
