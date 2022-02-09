package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class LargeSmokeParticle extends SmokeParticle
{
    protected LargeSmokeParticle(ClientLevel pLevel, double pX, double p_107046_, double pY, double p_107048_, double pZ, double p_107050_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_107046_, pY, p_107048_, pZ, p_107050_, 2.5F, pXSpeed);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_107068_, double pY, double p_107070_, double pZ, double p_107072_)
        {
            return new LargeSmokeParticle(pLevel, pX, p_107068_, pY, p_107070_, pZ, p_107072_, this.sprites);
        }
    }
}
