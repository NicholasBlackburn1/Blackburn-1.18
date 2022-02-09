package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.optifine.Config;
import net.optifine.CustomColors;
import net.optifine.reflect.Reflector;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.shaders.Shaders;

public class LiquidBlockRenderer
{
    private static final float MAX_FLUID_HEIGHT = 0.8888889F;
    private final TextureAtlasSprite[] lavaIcons = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] waterIcons = new TextureAtlasSprite[2];
    private TextureAtlasSprite waterOverlay;

    protected void setupSprites()
    {
        this.lavaIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.LAVA.defaultBlockState()).getParticleIcon();
        this.lavaIcons[1] = ModelBakery.LAVA_FLOW.sprite();
        this.waterIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.WATER.defaultBlockState()).getParticleIcon();
        this.waterIcons[1] = ModelBakery.WATER_FLOW.sprite();
        this.waterOverlay = ModelBakery.WATER_OVERLAY.sprite();
    }

    private static boolean isNeighborSameFluid(BlockGetter pLevel, BlockPos pPos, Direction pSide, FluidState pState)
    {
        BlockPos blockpos = pPos.relative(pSide);
        FluidState fluidstate = pLevel.getFluidState(blockpos);
        return fluidstate.getType().isSame(pState.getType());
    }

    private static boolean isFaceOccludedByState(BlockGetter pLevel, Direction pFace, float pHeight, BlockPos pPos, BlockState pState)
    {
        if (pState.canOcclude())
        {
            VoxelShape voxelshape = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, (double)pHeight, 1.0D);
            VoxelShape voxelshape1 = pState.getOcclusionShape(pLevel, pPos);
            return Shapes.blockOccudes(voxelshape, voxelshape1, pFace);
        }
        else
        {
            return false;
        }
    }

    private static boolean isFaceOccludedByNeighbor(BlockGetter pLevel, BlockPos pPos, Direction pFace, float pHeight)
    {
        BlockPos blockpos = pPos.relative(pFace);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        return isFaceOccludedByState(pLevel, pFace, pHeight, blockpos, blockstate);
    }

    private static boolean isFaceOccludedBySelf(BlockGetter pLevel, BlockPos pPos, BlockState pState, Direction pFace)
    {
        return isFaceOccludedByState(pLevel, pFace.getOpposite(), 1.0F, pPos, pState);
    }

    public static boolean shouldRenderFace(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState, BlockState pBlockState, Direction pFace)
    {
        return !isFaceOccludedBySelf(pLevel, pPos, pBlockState, pFace) && !isNeighborSameFluid(pLevel, pPos, pFace, pFluidState);
    }

    public boolean tesselate(BlockAndTintGetter pLevel, BlockPos pPos, VertexConsumer pConsumer, FluidState pFluidState)
    {
        BlockState blockstate = pFluidState.createLegacyBlock();
        boolean flg, flag7 = false;

        try
        {
            if (Config.isShaders())
            {
                SVertexBuilder.pushEntity(blockstate, pConsumer);
            }

            boolean flag = pFluidState.is(FluidTags.LAVA);
            TextureAtlasSprite[] atextureatlassprite = flag ? this.lavaIcons : this.waterIcons;
            BlockState blockstate1 = pLevel.getBlockState(pPos);

            if (Reflector.ForgeHooksClient_getFluidSprites.exists())
            {
                TextureAtlasSprite[] atextureatlassprite1 = (TextureAtlasSprite[])Reflector.call(Reflector.ForgeHooksClient_getFluidSprites, pLevel, pPos, pFluidState);

                if (atextureatlassprite1 != null)
                {
                    atextureatlassprite = atextureatlassprite1;
                }
            }

            RenderEnv renderenv = pConsumer.getRenderEnv(blockstate, pPos);
            boolean flag1 = !flag && Minecraft.useAmbientOcclusion();
            int i = -1;
            float f = 1.0F;

            if (Reflector.IForgeFluid_getAttributes.exists())
            {
                Object object = Reflector.call(pFluidState.getType(), Reflector.IForgeFluid_getAttributes);

                if (object != null && Reflector.FluidAttributes_getColor.exists())
                {
                    i = Reflector.callInt(object, Reflector.FluidAttributes_getColor, pLevel, pPos);
                    f = (float)(i >> 24 & 255) / 255.0F;
                }
            }

            boolean flag9 = !isNeighborSameFluid(pLevel, pPos, Direction.UP, pFluidState);
            boolean flag2 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate1, Direction.DOWN) && !isFaceOccludedByNeighbor(pLevel, pPos, Direction.DOWN, 0.8888889F);
            boolean flag3 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate1, Direction.NORTH);
            boolean flag4 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate1, Direction.SOUTH);
            boolean flag5 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate1, Direction.WEST);
            boolean flag6 = shouldRenderFace(pLevel, pPos, pFluidState, blockstate1, Direction.EAST);

            if (flag9 || flag2 || flag6 || flag5 || flag3 || flag4)
            {
                if (i < 0)
                {
                    i = CustomColors.getFluidColor(pLevel, blockstate, pPos, renderenv);
                }

                float f18 = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                boolean flag8 = false;
                float f3 = pLevel.getShade(Direction.DOWN, true);
                float f4 = pLevel.getShade(Direction.UP, true);
                float f5 = pLevel.getShade(Direction.NORTH, true);
                float f6 = pLevel.getShade(Direction.WEST, true);
                float f7 = this.getWaterHeight(pLevel, pPos, pFluidState.getType());
                float f8 = this.getWaterHeight(pLevel, pPos.south(), pFluidState.getType());
                float f9 = this.getWaterHeight(pLevel, pPos.east().south(), pFluidState.getType());
                float f10 = this.getWaterHeight(pLevel, pPos.east(), pFluidState.getType());
                double d0 = (double)(pPos.getX() & 15);
                double d1 = (double)(pPos.getY() & 15);
                double d2 = (double)(pPos.getZ() & 15);

                if (Config.isRenderRegions())
                {
                    int j = pPos.getX() >> 4 << 4;
                    int k = pPos.getY() >> 4 << 4;
                    int l = pPos.getZ() >> 4 << 4;
                    int i1 = 8;
                    int j1 = j >> i1 << i1;
                    int k1 = l >> i1 << i1;
                    int l1 = j - j1;
                    int i2 = l - k1;
                    d0 += (double)l1;
                    d1 += (double)k;
                    d2 += (double)i2;
                }

                if (Config.isShaders() && Shaders.useMidBlockAttrib)
                {
                    pConsumer.setMidBlock((float)(d0 + 0.5D), (float)(d1 + 0.5D), (float)(d2 + 0.5D));
                }

                float f19 = 0.001F;
                float f20 = flag2 ? 0.001F : 0.0F;

                if (flag9 && !isFaceOccludedByNeighbor(pLevel, pPos, Direction.UP, Math.min(Math.min(f7, f8), Math.min(f9, f10))))
                {
                    flag8 = true;
                    f7 -= 0.001F;
                    f8 -= 0.001F;
                    f9 -= 0.001F;
                    f10 -= 0.001F;
                    Vec3 vec3 = pFluidState.getFlow(pLevel, pPos);
                    float f11;
                    float f12;
                    float f13;
                    float f22;
                    float f24;
                    float f27;
                    float f30;
                    float f32;

                    if (vec3.x == 0.0D && vec3.z == 0.0D)
                    {
                        TextureAtlasSprite textureatlassprite2 = atextureatlassprite[0];
                        pConsumer.setSprite(textureatlassprite2);
                        f22 = textureatlassprite2.getU(0.0D);
                        f11 = textureatlassprite2.getV(0.0D);
                        f24 = f22;
                        f32 = textureatlassprite2.getV(16.0D);
                        f27 = textureatlassprite2.getU(16.0D);
                        f12 = f32;
                        f30 = f27;
                        f13 = f11;
                    }
                    else
                    {
                        TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
                        pConsumer.setSprite(textureatlassprite);
                        float f14 = (float)Mth.atan2(vec3.z, vec3.x) - ((float)Math.PI / 2F);
                        float f15 = Mth.sin(f14) * 0.25F;
                        float f16 = Mth.cos(f14) * 0.25F;
                        float f17 = 8.0F;
                        f22 = textureatlassprite.getU((double)(8.0F + (-f16 - f15) * 16.0F));
                        f11 = textureatlassprite.getV((double)(8.0F + (-f16 + f15) * 16.0F));
                        f24 = textureatlassprite.getU((double)(8.0F + (-f16 + f15) * 16.0F));
                        f32 = textureatlassprite.getV((double)(8.0F + (f16 + f15) * 16.0F));
                        f27 = textureatlassprite.getU((double)(8.0F + (f16 + f15) * 16.0F));
                        f12 = textureatlassprite.getV((double)(8.0F + (f16 - f15) * 16.0F));
                        f30 = textureatlassprite.getU((double)(8.0F + (f16 - f15) * 16.0F));
                        f13 = textureatlassprite.getV((double)(8.0F + (-f16 - f15) * 16.0F));
                    }

                    float f36 = (f22 + f24 + f27 + f30) / 4.0F;
                    float f37 = (f11 + f32 + f12 + f13) / 4.0F;
                    float f38 = (float)atextureatlassprite[0].getWidth() / (atextureatlassprite[0].getU1() - atextureatlassprite[0].getU0());
                    float f39 = (float)atextureatlassprite[0].getHeight() / (atextureatlassprite[0].getV1() - atextureatlassprite[0].getV0());
                    float f40 = 4.0F / Math.max(f39, f38);
                    f22 = Mth.lerp(f40, f22, f36);
                    f24 = Mth.lerp(f40, f24, f36);
                    f27 = Mth.lerp(f40, f27, f36);
                    f30 = Mth.lerp(f40, f30, f36);
                    f11 = Mth.lerp(f40, f11, f37);
                    f32 = Mth.lerp(f40, f32, f37);
                    f12 = Mth.lerp(f40, f12, f37);
                    f13 = Mth.lerp(f40, f13, f37);
                    int j2 = this.getLightColor(pLevel, pPos);
                    int k2 = j2;
                    int l2 = j2;
                    int i3 = j2;
                    int j3 = j2;

                    if (flag1)
                    {
                        BlockPos blockpos = pPos.north();
                        BlockPos blockpos1 = pPos.south();
                        BlockPos blockpos2 = pPos.east();
                        BlockPos blockpos3 = pPos.west();
                        int k3 = this.getLightColor(pLevel, blockpos);
                        int l3 = this.getLightColor(pLevel, blockpos1);
                        int i4 = this.getLightColor(pLevel, blockpos2);
                        int j4 = this.getLightColor(pLevel, blockpos3);
                        int k4 = this.getLightColor(pLevel, blockpos.west());
                        int l4 = this.getLightColor(pLevel, blockpos1.west());
                        int i5 = this.getLightColor(pLevel, blockpos1.east());
                        int j5 = this.getLightColor(pLevel, blockpos.east());
                        k2 = ModelBlockRenderer.AmbientOcclusionFace.blend(k3, k4, j4, j2);
                        l2 = ModelBlockRenderer.AmbientOcclusionFace.blend(l3, l4, j4, j2);
                        i3 = ModelBlockRenderer.AmbientOcclusionFace.blend(l3, i5, i4, j2);
                        j3 = ModelBlockRenderer.AmbientOcclusionFace.blend(k3, j5, i4, j2);
                    }

                    float f44 = f4 * f18;
                    float f46 = f4 * f1;
                    float f48 = f4 * f2;
                    this.vertexVanilla(pConsumer, d0 + 0.0D, d1 + (double)f7, d2 + 0.0D, f44, f46, f48, f, f22, f11, k2);
                    this.vertexVanilla(pConsumer, d0 + 0.0D, d1 + (double)f8, d2 + 1.0D, f44, f46, f48, f, f24, f32, l2);
                    this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f9, d2 + 1.0D, f44, f46, f48, f, f27, f12, i3);
                    this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f10, d2 + 0.0D, f44, f46, f48, f, f30, f13, j3);

                    if (pFluidState.shouldRenderBackwardUpFace(pLevel, pPos.above()))
                    {
                        this.vertexVanilla(pConsumer, d0 + 0.0D, d1 + (double)f7, d2 + 0.0D, f44, f46, f48, f, f22, f11, k2);
                        this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f10, d2 + 0.0D, f44, f46, f48, f, f30, f13, j3);
                        this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f9, d2 + 1.0D, f44, f46, f48, f, f27, f12, i3);
                        this.vertexVanilla(pConsumer, d0 + 0.0D, d1 + (double)f8, d2 + 1.0D, f44, f46, f48, f, f24, f32, l2);
                    }
                }

                if (flag2)
                {
                    pConsumer.setSprite(atextureatlassprite[0]);
                    float f21 = atextureatlassprite[0].getU0();
                    float f23 = atextureatlassprite[0].getU1();
                    float f25 = atextureatlassprite[0].getV0();
                    float f28 = atextureatlassprite[0].getV1();
                    int i6 = this.getLightColor(pLevel, pPos.below());
                    float f31 = pLevel.getShade(Direction.DOWN, true);
                    float f33 = f31 * f18;
                    float f34 = f31 * f1;
                    float f35 = f31 * f2;
                    this.vertexVanilla(pConsumer, d0, d1 + (double)f20, d2 + 1.0D, f33, f34, f35, f, f21, f28, i6);
                    this.vertexVanilla(pConsumer, d0, d1 + (double)f20, d2, f33, f34, f35, f, f21, f25, i6);
                    this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f20, d2, f33, f34, f35, f, f23, f25, i6);
                    this.vertexVanilla(pConsumer, d0 + 1.0D, d1 + (double)f20, d2 + 1.0D, f33, f34, f35, f, f23, f28, i6);
                    flag8 = true;
                }

                int k5 = this.getLightColor(pLevel, pPos);

                for (int l5 = 0; l5 < 4; ++l5)
                {
                    float f26;
                    float f29;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    Direction direction;
                    boolean flag10;

                    if (l5 == 0)
                    {
                        f26 = f7;
                        f29 = f10;
                        d3 = d0;
                        d5 = d0 + 1.0D;
                        d4 = d2 + (double)0.001F;
                        d6 = d2 + (double)0.001F;
                        direction = Direction.NORTH;
                        flag10 = flag3;
                    }
                    else if (l5 == 1)
                    {
                        f26 = f9;
                        f29 = f8;
                        d3 = d0 + 1.0D;
                        d5 = d0;
                        d4 = d2 + 1.0D - (double)0.001F;
                        d6 = d2 + 1.0D - (double)0.001F;
                        direction = Direction.SOUTH;
                        flag10 = flag4;
                    }
                    else if (l5 == 2)
                    {
                        f26 = f8;
                        f29 = f7;
                        d3 = d0 + (double)0.001F;
                        d5 = d0 + (double)0.001F;
                        d4 = d2 + 1.0D;
                        d6 = d2;
                        direction = Direction.WEST;
                        flag10 = flag5;
                    }
                    else
                    {
                        f26 = f10;
                        f29 = f9;
                        d3 = d0 + 1.0D - (double)0.001F;
                        d5 = d0 + 1.0D - (double)0.001F;
                        d4 = d2;
                        d6 = d2 + 1.0D;
                        direction = Direction.EAST;
                        flag10 = flag6;
                    }

                    if (flag10 && !isFaceOccludedByNeighbor(pLevel, pPos, direction, Math.max(f26, f29)))
                    {
                        flag8 = true;
                        BlockPos blockpos4 = pPos.relative(direction);
                        TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];
                        float f41 = 0.0F;
                        float f42 = 0.0F;
                        boolean flag11 = !flag;

                        if (Reflector.IForgeBlockState_shouldDisplayFluidOverlay.exists())
                        {
                            flag11 = atextureatlassprite[2] != null;
                        }

                        if (flag11)
                        {
                            BlockState blockstate2 = pLevel.getBlockState(blockpos4);
                            Block block = blockstate2.getBlock();
                            boolean flag12 = false;

                            if (Reflector.IForgeBlockState_shouldDisplayFluidOverlay.exists())
                            {
                                flag12 = Reflector.callBoolean(blockstate2, Reflector.IForgeBlockState_shouldDisplayFluidOverlay, pLevel, blockpos4, pFluidState);
                            }

                            if (flag12 || block instanceof HalfTransparentBlock || block instanceof LeavesBlock || block == Blocks.BEACON)
                            {
                                textureatlassprite1 = this.waterOverlay;
                            }

                            if (block == Blocks.FARMLAND || block == Blocks.DIRT_PATH)
                            {
                                f41 = 0.9375F;
                                f42 = 0.9375F;
                            }

                            if (block instanceof SlabBlock)
                            {
                                SlabBlock slabblock = (SlabBlock)block;

                                if (blockstate2.getValue(SlabBlock.TYPE) == SlabType.BOTTOM)
                                {
                                    f41 = 0.5F;
                                    f42 = 0.5F;
                                }
                            }
                        }

                        pConsumer.setSprite(textureatlassprite1);

                        if (!(f26 <= f41) || !(f29 <= f42))
                        {
                            f41 = Math.min(f41, f26);
                            f42 = Math.min(f42, f29);

                            if (f41 > f19)
                            {
                                f41 -= f19;
                            }

                            if (f42 > f19)
                            {
                                f42 -= f19;
                            }

                            float f43 = textureatlassprite1.getV((double)((1.0F - f41) * 16.0F * 0.5F));
                            float f45 = textureatlassprite1.getV((double)((1.0F - f42) * 16.0F * 0.5F));
                            float f47 = textureatlassprite1.getU(0.0D);
                            float f49 = textureatlassprite1.getU(8.0D);
                            float f50 = textureatlassprite1.getV((double)((1.0F - f26) * 16.0F * 0.5F));
                            float f51 = textureatlassprite1.getV((double)((1.0F - f29) * 16.0F * 0.5F));
                            float f52 = textureatlassprite1.getV(8.0D);
                            float f53 = l5 < 2 ? pLevel.getShade(Direction.NORTH, true) : pLevel.getShade(Direction.WEST, true);
                            float f54 = f4 * f53 * f18;
                            float f55 = f4 * f53 * f1;
                            float f56 = f4 * f53 * f2;
                            this.vertexVanilla(pConsumer, d3, d1 + (double)f26, d4, f54, f55, f56, f, f47, f50, k5);
                            this.vertexVanilla(pConsumer, d5, d1 + (double)f29, d6, f54, f55, f56, f, f49, f51, k5);
                            this.vertexVanilla(pConsumer, d5, d1 + (double)f20, d6, f54, f55, f56, f, f49, f45, k5);
                            this.vertexVanilla(pConsumer, d3, d1 + (double)f20, d4, f54, f55, f56, f, f47, f43, k5);

                            if (textureatlassprite1 != this.waterOverlay)
                            {
                                this.vertexVanilla(pConsumer, d3, d1 + (double)f20, d4, f54, f55, f56, f, f47, f43, k5);
                                this.vertexVanilla(pConsumer, d5, d1 + (double)f20, d6, f54, f55, f56, f, f49, f45, k5);
                                this.vertexVanilla(pConsumer, d5, d1 + (double)f29, d6, f54, f55, f56, f, f49, f51, k5);
                                this.vertexVanilla(pConsumer, d3, d1 + (double)f26, d4, f54, f55, f56, f, f47, f50, k5);
                            }
                        }
                    }
                }

                pConsumer.setSprite((TextureAtlasSprite)null);
                return flag8;
            }

            flg = flag7;
        }
        finally
        {
            if (Config.isShaders())
            {
                SVertexBuilder.popEntity(pConsumer);
            }
        }

        return flg;
    }

    private void vertex(VertexConsumer pConsumer, double pX, double p_110987_, double pY, float p_110989_, float pZ, float p_110991_, float pRed, float pGreen, int pBlue)
    {
        pConsumer.vertex(pX, p_110987_, pY).color(p_110989_, pZ, p_110991_, 1.0F).uv(pRed, pGreen).uv2(pBlue).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private void vertexVanilla(VertexConsumer buffer, double x, double y, double z, float red, float green, float blue, float alpha, float u, float v, int combinedLight)
    {
        buffer.vertex(x, y, z).color(red, green, blue, alpha).uv(u, v).uv2(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private int getLightColor(BlockAndTintGetter pLevel, BlockPos pPos)
    {
        int i = LevelRenderer.getLightColor(pLevel, pPos);
        int j = LevelRenderer.getLightColor(pLevel, pPos.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }

    private float getWaterHeight(BlockGetter pLevel, BlockPos pPos, Fluid pFluid)
    {
        int i = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; ++j)
        {
            BlockPos blockpos = pPos.offset(-(j & 1), 0, -(j >> 1 & 1));

            if (pLevel.getFluidState(blockpos.above()).getType().isSame(pFluid))
            {
                return 1.0F;
            }

            FluidState fluidstate = pLevel.getFluidState(blockpos);

            if (fluidstate.getType().isSame(pFluid))
            {
                float f1 = fluidstate.getHeight(pLevel, blockpos);

                if (f1 >= 0.8F)
                {
                    f += f1 * 10.0F;
                    i += 10;
                }
                else
                {
                    f += f1;
                    ++i;
                }
            }
            else if (!pLevel.getBlockState(blockpos).getMaterial().isSolid())
            {
                ++i;
            }
        }

        return f / (float)i;
    }
}
