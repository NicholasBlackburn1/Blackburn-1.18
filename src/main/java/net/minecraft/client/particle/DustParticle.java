package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;

public class DustParticle extends DustParticleBase<DustParticleOptions>
{
    protected DustParticle(ClientLevel pLevel, double pX, double p_106417_, double pY, double p_106419_, double pZ, double p_106421_, DustParticleOptions pXSpeed, SpriteSet p_106423_)
    {
        super(pLevel, pX, p_106417_, pY, p_106419_, pZ, p_106421_, pXSpeed, p_106423_);
    }

    public static class Provider implements ParticleProvider<DustParticleOptions>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(DustParticleOptions pType, ClientLevel pLevel, double pX, double p_106446_, double pY, double p_106448_, double pZ, double p_106450_)
        {
            return new DustParticle(pLevel, pX, p_106446_, pY, p_106448_, pZ, p_106450_, pType, this.sprites);
        }
    }
}
