package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.util.Mth;

public class DustParticleBase<T extends DustParticleOptionsBase> extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected DustParticleBase(ClientLevel pLevel, double pX, double p_172096_, double pY, double p_172098_, double pZ, double p_172100_, T pXSpeed, SpriteSet p_172102_)
    {
        super(pLevel, pX, p_172096_, pY, p_172098_, pZ, p_172100_);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = p_172102_;
        this.xd *= (double)0.1F;
        this.yd *= (double)0.1F;
        this.zd *= (double)0.1F;
        float f = this.random.nextFloat() * 0.4F + 0.6F;
        this.rCol = this.randomizeColor(pXSpeed.getColor().x(), f);
        this.gCol = this.randomizeColor(pXSpeed.getColor().y(), f);
        this.bCol = this.randomizeColor(pXSpeed.getColor().z(), f);
        this.quadSize *= 0.75F * pXSpeed.getScale();
        int i = (int)(8.0D / (this.random.nextDouble() * 0.8D + 0.2D));
        this.lifetime = (int)Math.max((float)i * pXSpeed.getScale(), 1.0F);
        this.setSpriteFromAge(p_172102_);
    }

    protected float randomizeColor(float pCoordMultiplier, float pMultiplier)
    {
        return (this.random.nextFloat() * 0.2F + 0.8F) * pCoordMultiplier * pMultiplier;
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
