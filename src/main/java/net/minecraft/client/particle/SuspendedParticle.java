package net.minecraft.client.particle;

import java.util.Optional;
import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SuspendedParticle extends TextureSheetParticle
{
    SuspendedParticle(ClientLevel pLevel, SpriteSet pSprites, double pX, double p_172406_, double pY)
    {
        super(pLevel, pX, p_172406_ - 0.125D, pY);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(pSprites);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    SuspendedParticle(ClientLevel pLevel, SpriteSet pSprites, double pX, double p_172412_, double pY, double p_172414_, double pZ, double p_172416_)
    {
        super(pLevel, pX, p_172412_ - 0.125D, pY, p_172414_, pZ, p_172416_);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(pSprites);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
        this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class CrimsonSporeProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public CrimsonSporeProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_108056_, double pY, double p_108058_, double pZ, double p_108060_)
        {
            Random random = pLevel.random;
            double d0 = random.nextGaussian() * (double)1.0E-6F;
            double d1 = random.nextGaussian() * (double)1.0E-4F;
            double d2 = random.nextGaussian() * (double)1.0E-6F;
            SuspendedParticle suspendedparticle = new SuspendedParticle(pLevel, this.sprite, pX, p_108056_, pY, d0, d1, d2);
            suspendedparticle.setColor(0.9F, 0.4F, 0.5F);
            return suspendedparticle;
        }
    }

    public static class SporeBlossomAirProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public SporeBlossomAirProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172433_, double pY, double p_172435_, double pZ, double p_172437_)
        {
            SuspendedParticle suspendedparticle = new SuspendedParticle(pLevel, this.sprite, pX, p_172433_, pY, 0.0D, (double) - 0.8F, 0.0D)
            {
                public Optional<ParticleGroup> getParticleGroup()
                {
                    return Optional.of(ParticleGroup.SPORE_BLOSSOM);
                }
            };
            suspendedparticle.lifetime = Mth.randomBetweenInclusive(pLevel.random, 500, 1000);
            suspendedparticle.gravity = 0.01F;
            suspendedparticle.setColor(0.32F, 0.5F, 0.22F);
            return suspendedparticle;
        }
    }

    public static class UnderwaterProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public UnderwaterProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_108077_, double pY, double p_108079_, double pZ, double p_108081_)
        {
            SuspendedParticle suspendedparticle = new SuspendedParticle(pLevel, this.sprite, pX, p_108077_, pY);
            suspendedparticle.setColor(0.4F, 0.4F, 0.7F);
            return suspendedparticle;
        }
    }

    public static class WarpedSporeProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public WarpedSporeProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_108098_, double pY, double p_108100_, double pZ, double p_108102_)
        {
            double d0 = (double)pLevel.random.nextFloat() * -1.9D * (double)pLevel.random.nextFloat() * 0.1D;
            SuspendedParticle suspendedparticle = new SuspendedParticle(pLevel, this.sprite, pX, p_108098_, pY, 0.0D, d0, 0.0D);
            suspendedparticle.setColor(0.1F, 0.1F, 0.3F);
            suspendedparticle.setSize(0.001F, 0.001F);
            return suspendedparticle;
        }
    }
}
