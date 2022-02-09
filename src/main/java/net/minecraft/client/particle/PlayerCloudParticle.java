package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PlayerCloudParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    PlayerCloudParticle(ClientLevel pLevel, double pX, double p_107485_, double pY, double p_107487_, double pZ, double p_107489_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_107485_, pY, 0.0D, 0.0D, 0.0D);
        this.friction = 0.96F;
        this.sprites = pXSpeed;
        float f = 2.5F;
        this.xd *= (double)0.1F;
        this.yd *= (double)0.1F;
        this.zd *= (double)0.1F;
        this.xd += p_107487_;
        this.yd += pZ;
        this.zd += p_107489_;
        float f1 = 1.0F - (float)(Math.random() * (double)0.3F);
        this.rCol = f1;
        this.gCol = f1;
        this.bCol = f1;
        this.quadSize *= 1.875F;
        int i = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
        this.lifetime = (int)Math.max((float)i * 2.5F, 1.0F);
        this.hasPhysics = false;
        this.setSpriteFromAge(pXSpeed);
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getQuadSize(float pScaleFactor)
    {
        return this.quadSize * Mth.clamp(((float)this.age + pScaleFactor) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick()
    {
        super.tick();

        if (!this.removed)
        {
            this.setSpriteFromAge(this.sprites);
            Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0D, false);

            if (player != null)
            {
                double d0 = player.getY();

                if (this.y > d0)
                {
                    this.y += (d0 - this.y) * 0.2D;
                    this.yd += (player.getDeltaMovement().y - this.yd) * 0.2D;
                    this.setPos(this.x, this.y, this.z);
                }
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_107521_, double pY, double p_107523_, double pZ, double p_107525_)
        {
            return new PlayerCloudParticle(pLevel, pX, p_107521_, pY, p_107523_, pZ, p_107525_, this.sprites);
        }
    }

    public static class SneezeProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public SneezeProvider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_107542_, double pY, double p_107544_, double pZ, double p_107546_)
        {
            Particle particle = new PlayerCloudParticle(pLevel, pX, p_107542_, pY, p_107544_, pZ, p_107546_, this.sprites);
            particle.setColor(200.0F, 50.0F, 120.0F);
            particle.setAlpha(0.4F);
            return particle;
        }
    }
}
