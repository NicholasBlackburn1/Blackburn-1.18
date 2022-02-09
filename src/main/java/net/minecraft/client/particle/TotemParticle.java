package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class TotemParticle extends SimpleAnimatedParticle
{
    TotemParticle(ClientLevel pLevel, double pX, double p_108348_, double pY, double p_108350_, double pZ, double p_108352_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_108348_, pY, pXSpeed, 1.25F);
        this.friction = 0.6F;
        this.xd = p_108350_;
        this.yd = pZ;
        this.zd = p_108352_;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setSpriteFromAge(pXSpeed);

        if (this.random.nextInt(4) == 0)
        {
            this.setColor(0.6F + this.random.nextFloat() * 0.2F, 0.6F + this.random.nextFloat() * 0.3F, this.random.nextFloat() * 0.2F);
        }
        else
        {
            this.setColor(0.1F + this.random.nextFloat() * 0.2F, 0.4F + this.random.nextFloat() * 0.3F, this.random.nextFloat() * 0.2F);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_108380_, double pY, double p_108382_, double pZ, double p_108384_)
        {
            return new TotemParticle(pLevel, pX, p_108380_, pY, p_108382_, pZ, p_108384_, this.sprites);
        }
    }
}
