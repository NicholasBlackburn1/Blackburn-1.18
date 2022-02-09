package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class BubblePopParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    BubblePopParticle(ClientLevel pLevel, double pX, double p_105816_, double pY, double p_105818_, double pZ, double p_105820_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_105816_, pY);
        this.sprites = pXSpeed;
        this.lifetime = 4;
        this.gravity = 0.008F;
        this.xd = p_105818_;
        this.yd = pZ;
        this.zd = p_105820_;
        this.setSpriteFromAge(pXSpeed);
    }

    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }
        else
        {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_105850_, double pY, double p_105852_, double pZ, double p_105854_)
        {
            return new BubblePopParticle(pLevel, pX, p_105850_, pY, p_105852_, pZ, p_105854_, this.sprites);
        }
    }
}
