package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class HugeExplosionParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    HugeExplosionParticle(ClientLevel pLevel, double pX, double p_106907_, double pY, double p_106909_, SpriteSet pZ)
    {
        super(pLevel, pX, p_106907_, pY, 0.0D, 0.0D, 0.0D);
        this.lifetime = 6 + this.random.nextInt(4);
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize = 2.0F * (1.0F - (float)p_106909_ * 0.5F);
        this.sprites = pZ;
        this.setSpriteFromAge(pZ);
    }

    public int getLightColor(float pPartialTick)
    {
        return 15728880;
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
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_106925_)
        {
            this.sprites = p_106925_;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_106939_, double pY, double p_106941_, double pZ, double p_106943_)
        {
            return new HugeExplosionParticle(pLevel, pX, p_106939_, pY, p_106941_, this.sprites);
        }
    }
}
