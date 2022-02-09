package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainParticle extends TextureSheetParticle
{
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public TerrainParticle(ClientLevel pLevel, double pX, double p_108284_, double pY, double p_108286_, double pZ, double p_108288_, BlockState pXSpeed)
    {
        this(pLevel, pX, p_108284_, pY, p_108286_, pZ, p_108288_, pXSpeed, new BlockPos(pX, p_108284_, pY));
    }

    public TerrainParticle(ClientLevel pLevel, double pX, double p_172453_, double pY, double p_172455_, double pZ, double p_172457_, BlockState pXSpeed, BlockPos p_172459_)
    {
        super(pLevel, pX, p_172453_, pY, p_172455_, pZ, p_172457_);
        this.pos = p_172459_;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(pXSpeed));
        this.gravity = 1.0F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;

        if (!pXSpeed.is(Blocks.GRASS_BLOCK))
        {
            int i = Minecraft.getInstance().getBlockColors().getColor(pXSpeed, pLevel, p_172459_, 0);
            this.rCol *= (float)(i >> 16 & 255) / 255.0F;
            this.gCol *= (float)(i >> 8 & 255) / 255.0F;
            this.bCol *= (float)(i & 255) / 255.0F;
        }

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected float getU0()
    {
        return this.sprite.getU((double)((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1()
    {
        return this.sprite.getU((double)(this.uo / 4.0F * 16.0F));
    }

    protected float getV0()
    {
        return this.sprite.getV((double)(this.vo / 4.0F * 16.0F));
    }

    protected float getV1()
    {
        return this.sprite.getV((double)((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    public int getLightColor(float pPartialTick)
    {
        int i = super.getLightColor(pPartialTick);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }

    public static class Provider implements ParticleProvider<BlockParticleOption>
    {
        public Particle createParticle(BlockParticleOption pType, ClientLevel pLevel, double pX, double p_108307_, double pY, double p_108309_, double pZ, double p_108311_)
        {
            BlockState blockstate = pType.getState();
            return !blockstate.isAir() && !blockstate.is(Blocks.MOVING_PISTON) ? new TerrainParticle(pLevel, pX, p_108307_, pY, p_108309_, pZ, p_108311_, blockstate) : null;
        }
    }
}
