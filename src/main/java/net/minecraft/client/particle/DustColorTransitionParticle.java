package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;

public class DustColorTransitionParticle extends DustParticleBase<DustColorTransitionOptions>
{
    private final Vector3f fromColor;
    private final Vector3f toColor;

    protected DustColorTransitionParticle(ClientLevel pLevel, double pX, double p_172055_, double pY, double p_172057_, double pZ, double p_172059_, DustColorTransitionOptions pXSpeed, SpriteSet p_172061_)
    {
        super(pLevel, pX, p_172055_, pY, p_172057_, pZ, p_172059_, pXSpeed, p_172061_);
        float f = this.random.nextFloat() * 0.4F + 0.6F;
        this.fromColor = this.randomizeColor(pXSpeed.getFromColor(), f);
        this.toColor = this.randomizeColor(pXSpeed.getToColor(), f);
    }

    private Vector3f randomizeColor(Vector3f pVector, float pMultiplier)
    {
        return new Vector3f(this.randomizeColor(pVector.x(), pMultiplier), this.randomizeColor(pVector.y(), pMultiplier), this.randomizeColor(pVector.z(), pMultiplier));
    }

    private void lerpColors(float p_172070_)
    {
        float f = ((float)this.age + p_172070_) / ((float)this.lifetime + 1.0F);
        Vector3f vector3f = this.fromColor.copy();
        vector3f.lerp(this.toColor, f);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();
    }

    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks)
    {
        this.lerpColors(pPartialTicks);
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    public static class Provider implements ParticleProvider<DustColorTransitionOptions>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites)
        {
            this.sprites = pSprites;
        }

        public Particle createParticle(DustColorTransitionOptions pType, ClientLevel pLevel, double pX, double p_172078_, double pY, double p_172080_, double pZ, double p_172082_)
        {
            return new DustColorTransitionParticle(pLevel, pX, p_172078_, pY, p_172080_, pZ, p_172082_, pType, this.sprites);
        }
    }
}
