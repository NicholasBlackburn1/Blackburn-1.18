package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public class BaseAshSmokeParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected BaseAshSmokeParticle(ClientLevel pLevel, double pX, double p_171906_, double pY, float p_171908_, float pZ, float p_171910_, double pXSeedMultiplier, double pYSpeedMultiplier, double pZSpeedMultiplier, float pXSpeed, SpriteSet p_171915_, float pYSpeed, int p_171917_, float pZSpeed, boolean p_171919_)
    {
        super(pLevel, pX, p_171906_, pY, 0.0D, 0.0D, 0.0D);
        this.friction = 0.96F;
        this.gravity = pZSpeed;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = p_171915_;
        this.xd *= (double)p_171908_;
        this.yd *= (double)pZ;
        this.zd *= (double)p_171910_;
        this.xd += pXSeedMultiplier;
        this.yd += pYSpeedMultiplier;
        this.zd += pZSpeedMultiplier;
        float f = pLevel.random.nextFloat() * pYSpeed;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize *= 0.75F * pXSpeed;
        this.lifetime = (int)((double)p_171917_ / ((double)pLevel.random.nextFloat() * 0.8D + 0.2D));
        this.lifetime = (int)((float)this.lifetime * pXSpeed);
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(p_171915_);
        this.hasPhysics = p_171919_;
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getQuadSize(float pScaleFactor)
    {
        return this.quadSize * Mth.clamp(((float)this.age + pScaleFactor) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick()
    {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }
}
