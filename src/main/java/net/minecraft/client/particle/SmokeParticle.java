package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SmokeParticle extends BaseAshSmokeParticle
{
    protected SmokeParticle(ClientLevel pLevel, double pX, double p_107687_, double pY, double p_107689_, double pZ, double p_107691_, float pXSpeed, SpriteSet p_107693_)
    {
        super(pLevel, pX, p_107687_, pY, 0.1F, 0.1F, 0.1F, p_107689_, pZ, p_107691_, pXSpeed, p_107693_, 0.3F, 8, -0.1F, true);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_107710_, double pY, double p_107712_, double pZ, double p_107714_)
        {
            return new SmokeParticle(pLevel, pX, p_107710_, pY, p_107712_, pZ, p_107714_, 1.0F, this.sprites);
        }
    }
}
