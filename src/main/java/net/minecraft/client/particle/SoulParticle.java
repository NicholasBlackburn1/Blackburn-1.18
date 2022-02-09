package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SoulParticle extends RisingParticle
{
    private final SpriteSet sprites;

    SoulParticle(ClientLevel pLevel, double pX, double p_107719_, double pY, double p_107721_, double pZ, double p_107723_, SpriteSet pXSpeed)
    {
        super(pLevel, pX, p_107719_, pY, p_107721_, pZ, p_107723_);
        this.sprites = pXSpeed;
        this.scale(1.5F);
        this.setSpriteFromAge(pXSpeed);
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick()
    {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprites)
        {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double p_107753_, double pY, double p_107755_, double pZ, double p_107757_)
        {
            SoulParticle soulparticle = new SoulParticle(pLevel, pX, p_107753_, pY, p_107755_, pZ, p_107757_, this.sprite);
            soulparticle.setAlpha(1.0F);
            return soulparticle;
        }
    }
}
