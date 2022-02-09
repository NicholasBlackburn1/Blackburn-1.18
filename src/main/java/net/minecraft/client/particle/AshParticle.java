package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class AshParticle extends BaseAshSmokeParticle
{
    protected AshParticle(ClientLevel pLevel, double pX, double p_105516_, double pY, double p_105518_, double pZ, double p_105520_, float pXSpeed, SpriteSet p_105522_)
    {
        super(pLevel, pX, p_105516_, pY, 0.1F, -0.1F, 0.1F, p_105518_, pZ, p_105520_, pXSpeed, p_105522_, 0.5F, 20, 0.1F, false);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_105539_, double pY, double p_105541_, double pZ, double p_105543_)
        {
            return new AshParticle(pLevel, pX, p_105539_, pY, 0.0D, 0.0D, 0.0D, 1.0F, this.sprites);
        }
    }
}
