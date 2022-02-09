package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class SquidInkParticle extends SimpleAnimatedParticle
{
    SquidInkParticle(ClientLevel pLevel, double pX, double p_172327_, double pY, double p_172329_, double pZ, double p_172331_, int pXSpeed, SpriteSet p_172333_)
    {
        super(pLevel, pX, p_172327_, pY, p_172333_, 0.0F);
        this.friction = 0.92F;
        this.quadSize = 0.5F;
        this.setAlpha(1.0F);
        this.setColor((float)FastColor.ARGB32.red(pXSpeed), (float)FastColor.ARGB32.green(pXSpeed), (float)FastColor.ARGB32.blue(pXSpeed));
        this.lifetime = (int)((double)(this.quadSize * 12.0F) / (Math.random() * (double)0.8F + (double)0.2F));
        this.setSpriteFromAge(p_172333_);
        this.hasPhysics = false;
        this.xd = p_172329_;
        this.yd = pZ;
        this.zd = p_172331_;
    }

    public void tick()
    {
        super.tick();

        if (!this.removed)
        {
            this.setSpriteFromAge(this.sprites);

            if (this.age > this.lifetime / 2)
            {
                this.setAlpha(1.0F - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
            }

            if (this.level.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir())
            {
                this.yd -= (double)0.0074F;
            }
        }
    }

    public static class GlowInkProvider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public GlowInkProvider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_172350_, double pY, double p_172352_, double pZ, double p_172354_)
        {
            return new SquidInkParticle(pLevel, pX, p_172350_, pY, p_172352_, pZ, p_172354_, FastColor.ARGB32.color(255, 204, 31, 102), this.sprites);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_108005_, double pY, double p_108007_, double pZ, double p_108009_)
        {
            return new SquidInkParticle(pLevel, pX, p_108005_, pY, p_108007_, pZ, p_108009_, FastColor.ARGB32.color(255, 255, 255, 255), this.sprites);
        }
    }
}
