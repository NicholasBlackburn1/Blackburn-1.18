package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class EndRodParticle extends SimpleAnimatedParticle
{
    EndRodParticle(ClientLevel pLevel, double pX, double p_106533_, double pY, double p_106535_, double pZ, double p_106537_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_106533_, pY, pXSpeed, 0.0125F);
        this.xd = p_106535_;
        this.yd = pZ;
        this.zd = p_106537_;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setFadeColor(15916745);
        this.setSpriteFromAge(pXSpeed);
    }

    public void move(double pX, double p_106551_, double pY)
    {
        this.setBoundingBox(this.getBoundingBox().move(pX, p_106551_, pY));
        this.setLocationFromBoundingbox();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_106569_, double pY, double p_106571_, double pZ, double p_106573_)
        {
            return new EndRodParticle(pLevel, pX, p_106569_, pY, p_106571_, pZ, p_106573_, this.sprites);
        }
    }
}
