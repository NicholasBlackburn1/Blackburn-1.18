package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class GlowParticle extends TextureSheetParticle
{
    static final Random RANDOM = new Random();
    private final SpriteSet sprites;

    GlowParticle(ClientLevel pLevel, double pX, double p_172138_, double pY, double p_172140_, double pZ, double p_172142_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_172138_, pY, p_172140_, pZ, p_172142_);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = pXSpeed;
        this.quadSize *= 0.75F;
        this.hasPhysics = false;
        this.setSpriteFromAge(pXSpeed);
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float pPartialTick)
    {
        float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(pPartialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    public void tick()
    {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class ElectricSparkProvider implements ParticleProvider<SimpleParticleType>
    {
        private final double SPEED_FACTOR = 0.25D;
        private final SpriteSet sprite;

        public ElectricSparkProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172165_, double pY, double p_172167_, double pZ, double p_172169_)
        {
            GlowParticle glowparticle = new GlowParticle(pLevel, pX, p_172165_, pY, 0.0D, 0.0D, 0.0D, this.sprite);
            glowparticle.setColor(1.0F, 0.9F, 1.0F);
            glowparticle.setParticleSpeed(p_172167_ * 0.25D, pZ * 0.25D, p_172169_ * 0.25D);
            int i = 2;
            int j = 4;
            glowparticle.setLifetime(pLevel.random.nextInt(2) + 2);
            return glowparticle;
        }
    }

    public static class GlowSquidProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public GlowSquidProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172186_, double pY, double p_172188_, double pZ, double p_172190_)
        {
            GlowParticle glowparticle = new GlowParticle(pLevel, pX, p_172186_, pY, 0.5D - GlowParticle.RANDOM.nextDouble(), pZ, 0.5D - GlowParticle.RANDOM.nextDouble(), this.sprite);

            if (pLevel.random.nextBoolean())
            {
                glowparticle.setColor(0.6F, 1.0F, 0.8F);
            }
            else
            {
                glowparticle.setColor(0.08F, 0.4F, 0.4F);
            }

            glowparticle.yd *= (double)0.2F;

            if (p_172188_ == 0.0D && p_172190_ == 0.0D)
            {
                glowparticle.xd *= (double)0.1F;
                glowparticle.zd *= (double)0.1F;
            }

            glowparticle.setLifetime((int)(8.0D / (pLevel.random.nextDouble() * 0.8D + 0.2D)));
            return glowparticle;
        }
    }

    public static class ScrapeProvider implements ParticleProvider<SimpleParticleType>
    {
        private final double SPEED_FACTOR = 0.01D;
        private final SpriteSet sprite;

        public ScrapeProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172208_, double pY, double p_172210_, double pZ, double p_172212_)
        {
            GlowParticle glowparticle = new GlowParticle(pLevel, pX, p_172208_, pY, 0.0D, 0.0D, 0.0D, this.sprite);

            if (pLevel.random.nextBoolean())
            {
                glowparticle.setColor(0.29F, 0.58F, 0.51F);
            }
            else
            {
                glowparticle.setColor(0.43F, 0.77F, 0.62F);
            }

            glowparticle.setParticleSpeed(p_172210_ * 0.01D, pZ * 0.01D, p_172212_ * 0.01D);
            int i = 10;
            int j = 40;
            glowparticle.setLifetime(pLevel.random.nextInt(30) + 10);
            return glowparticle;
        }
    }

    public static class WaxOffProvider implements ParticleProvider<SimpleParticleType>
    {
        private final double SPEED_FACTOR = 0.01D;
        private final SpriteSet sprite;

        public WaxOffProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172230_, double pY, double p_172232_, double pZ, double p_172234_)
        {
            GlowParticle glowparticle = new GlowParticle(pLevel, pX, p_172230_, pY, 0.0D, 0.0D, 0.0D, this.sprite);
            glowparticle.setColor(1.0F, 0.9F, 1.0F);
            glowparticle.setParticleSpeed(p_172232_ * 0.01D / 2.0D, pZ * 0.01D, p_172234_ * 0.01D / 2.0D);
            int i = 10;
            int j = 40;
            glowparticle.setLifetime(pLevel.random.nextInt(30) + 10);
            return glowparticle;
        }
    }

    public static class WaxOnProvider implements ParticleProvider<SimpleParticleType>
    {
        private final double SPEED_FACTOR = 0.01D;
        private final SpriteSet sprite;

        public WaxOnProvider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172252_, double pY, double p_172254_, double pZ, double p_172256_)
        {
            GlowParticle glowparticle = new GlowParticle(pLevel, pX, p_172252_, pY, 0.0D, 0.0D, 0.0D, this.sprite);
            glowparticle.setColor(0.91F, 0.55F, 0.08F);
            glowparticle.setParticleSpeed(p_172254_ * 0.01D / 2.0D, pZ * 0.01D, p_172256_ * 0.01D / 2.0D);
            int i = 10;
            int j = 40;
            glowparticle.setLifetime(pLevel.random.nextInt(30) + 10);
            return glowparticle;
        }
    }
}
