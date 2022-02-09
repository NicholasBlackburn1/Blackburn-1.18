package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SnowflakeParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected SnowflakeParticle(ClientLevel pLevel, double pX, double p_172294_, double pY, double p_172296_, double pZ, double p_172298_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_172294_, pY);
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.sprites = pXSpeed;
        this.xd = p_172296_ + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
        this.yd = pZ + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
        this.zd = p_172298_ + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.lifetime = (int)(16.0D / ((double)this.random.nextFloat() * 0.8D + 0.2D)) + 2;
        this.setSpriteFromAge(pXSpeed);
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick()
    {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.xd *= (double)0.95F;
        this.yd *= (double)0.9F;
        this.zd *= (double)0.95F;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172318_, double pY, double p_172320_, double pZ, double p_172322_)
        {
            SnowflakeParticle snowflakeparticle = new SnowflakeParticle(pLevel, pX, p_172318_, pY, p_172320_, pZ, p_172322_, this.sprites);
            snowflakeparticle.setColor(0.923F, 0.964F, 0.999F);
            return snowflakeparticle;
        }
    }
}
