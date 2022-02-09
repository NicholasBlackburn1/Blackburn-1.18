package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class DebugRenderer
{
    public final PathfindingRenderer pathfindingRenderer = new PathfindingRenderer();
    public final DebugRenderer.SimpleDebugRenderer waterDebugRenderer;
    public final DebugRenderer.SimpleDebugRenderer chunkBorderRenderer;
    public final DebugRenderer.SimpleDebugRenderer heightMapRenderer;
    public final DebugRenderer.SimpleDebugRenderer collisionBoxRenderer;
    public final DebugRenderer.SimpleDebugRenderer neighborsUpdateRenderer;
    public final StructureRenderer structureRenderer;
    public final DebugRenderer.SimpleDebugRenderer lightDebugRenderer;
    public final DebugRenderer.SimpleDebugRenderer worldGenAttemptRenderer;
    public final DebugRenderer.SimpleDebugRenderer solidFaceRenderer;
    public final DebugRenderer.SimpleDebugRenderer chunkRenderer;
    public final BrainDebugRenderer brainDebugRenderer;
    public final VillageSectionsDebugRenderer villageSectionsDebugRenderer;
    public final BeeDebugRenderer beeDebugRenderer;
    public final RaidDebugRenderer raidDebugRenderer;
    public final GoalSelectorDebugRenderer goalSelectorRenderer;
    public final GameTestDebugRenderer gameTestDebugRenderer;
    public final GameEventListenerRenderer gameEventListenerRenderer;
    private boolean renderChunkborder;

    public DebugRenderer(Minecraft pMinecraft)
    {
        this.waterDebugRenderer = new WaterDebugRenderer(pMinecraft);
        this.chunkBorderRenderer = new ChunkBorderRenderer(pMinecraft);
        this.heightMapRenderer = new HeightMapRenderer(pMinecraft);
        this.collisionBoxRenderer = new CollisionBoxRenderer(pMinecraft);
        this.neighborsUpdateRenderer = new NeighborsUpdateRenderer(pMinecraft);
        this.structureRenderer = new StructureRenderer(pMinecraft);
        this.lightDebugRenderer = new LightDebugRenderer(pMinecraft);
        this.worldGenAttemptRenderer = new WorldGenAttemptRenderer();
        this.solidFaceRenderer = new SolidFaceRenderer(pMinecraft);
        this.chunkRenderer = new ChunkDebugRenderer(pMinecraft);
        this.brainDebugRenderer = new BrainDebugRenderer(pMinecraft);
        this.villageSectionsDebugRenderer = new VillageSectionsDebugRenderer();
        this.beeDebugRenderer = new BeeDebugRenderer(pMinecraft);
        this.raidDebugRenderer = new RaidDebugRenderer(pMinecraft);
        this.goalSelectorRenderer = new GoalSelectorDebugRenderer(pMinecraft);
        this.gameTestDebugRenderer = new GameTestDebugRenderer();
        this.gameEventListenerRenderer = new GameEventListenerRenderer(pMinecraft);
    }

    public void clear()
    {
        this.pathfindingRenderer.clear();
        this.waterDebugRenderer.clear();
        this.chunkBorderRenderer.clear();
        this.heightMapRenderer.clear();
        this.collisionBoxRenderer.clear();
        this.neighborsUpdateRenderer.clear();
        this.structureRenderer.clear();
        this.lightDebugRenderer.clear();
        this.worldGenAttemptRenderer.clear();
        this.solidFaceRenderer.clear();
        this.chunkRenderer.clear();
        this.brainDebugRenderer.clear();
        this.villageSectionsDebugRenderer.clear();
        this.beeDebugRenderer.clear();
        this.raidDebugRenderer.clear();
        this.goalSelectorRenderer.clear();
        this.gameTestDebugRenderer.clear();
        this.gameEventListenerRenderer.clear();
    }

    public boolean switchRenderChunkborder()
    {
        this.renderChunkborder = !this.renderChunkborder;
        return this.renderChunkborder;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource.BufferSource pBufferSource, double pCamX, double p_113461_, double pCamY)
    {
        if (this.renderChunkborder && !Minecraft.getInstance().showOnlyReducedInfo())
        {
            this.chunkBorderRenderer.render(pPoseStack, pBufferSource, pCamX, p_113461_, pCamY);
        }

        this.gameTestDebugRenderer.render(pPoseStack, pBufferSource, pCamX, p_113461_, pCamY);
    }

    public static Optional<Entity> getTargetedEntity(@Nullable Entity pEntity, int pDistance)
    {
        if (pEntity == null)
        {
            return Optional.empty();
        }
        else
        {
            Vec3 vec3 = pEntity.getEyePosition();
            Vec3 vec31 = pEntity.getViewVector(1.0F).scale((double)pDistance);
            Vec3 vec32 = vec3.add(vec31);
            AABB aabb = pEntity.getBoundingBox().expandTowards(vec31).inflate(1.0D);
            int i = pDistance * pDistance;
            Predicate<Entity> predicate = (p_113447_) ->
            {
                return !p_113447_.isSpectator() && p_113447_.isPickable();
            };
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(pEntity, vec3, vec32, aabb, predicate, (double)i);

            if (entityhitresult == null)
            {
                return Optional.empty();
            }
            else
            {
                return vec3.distanceToSqr(entityhitresult.getLocation()) > (double)i ? Optional.empty() : Optional.of(entityhitresult.getEntity());
            }
        }
    }

    public static void renderFilledBox(BlockPos pPos, BlockPos pSize, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        if (camera.isInitialized())
        {
            Vec3 vec3 = camera.getPosition().reverse();
            AABB aabb = (new AABB(pPos, pSize)).move(vec3);
            renderFilledBox(aabb, pRed, pGreen, pBlue, pAlpha);
        }
    }

    public static void renderFilledBox(BlockPos pPos, float pSize, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        if (camera.isInitialized())
        {
            Vec3 vec3 = camera.getPosition().reverse();
            AABB aabb = (new AABB(pPos)).move(vec3).inflate((double)pSize);
            renderFilledBox(aabb, pRed, pGreen, pBlue, pAlpha);
        }
    }

    public static void renderFilledBox(AABB pBox, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        renderFilledBox(pBox.minX, pBox.minY, pBox.minZ, pBox.maxX, pBox.maxY, pBox.maxZ, pRed, pGreen, pBlue, pAlpha);
    }

    public static void renderFilledBox(double pMinX, double p_113437_, double pMinY, double p_113439_, double pMinZ, double p_113441_, float pMaxX, float p_113443_, float pMaxY, float p_113445_)
    {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.addChainedFilledBoxVertices(bufferbuilder, pMinX, p_113437_, pMinY, p_113439_, pMinZ, p_113441_, pMaxX, p_113443_, pMaxY, p_113445_);
        tesselator.end();
    }

    public static void renderFloatingText(String pText, int pX, int p_113503_, int pY, int p_113505_)
    {
        renderFloatingText(pText, (double)pX + 0.5D, (double)p_113503_ + 0.5D, (double)pY + 0.5D, p_113505_);
    }

    public static void renderFloatingText(String pText, double pX, double p_113480_, double pY, int p_113482_)
    {
        renderFloatingText(pText, pX, p_113480_, pY, p_113482_, 0.02F);
    }

    public static void renderFloatingText(String pText, double pX, double p_113486_, double pY, int p_113488_, float pZ)
    {
        renderFloatingText(pText, pX, p_113486_, pY, p_113488_, pZ, true, 0.0F, false);
    }

    public static void renderFloatingText(String pText, double pX, double p_113493_, double pY, int p_113495_, float pZ, boolean p_113497_, float pColor, boolean pScale)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        if (camera.isInitialized() && minecraft.getEntityRenderDispatcher().options != null)
        {
            Font font = minecraft.font;
            double d0 = camera.getPosition().x;
            double d1 = camera.getPosition().y;
            double d2 = camera.getPosition().z;
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate((double)((float)(pX - d0)), (double)((float)(p_113493_ - d1) + 0.07F), (double)((float)(pY - d2)));
            posestack.mulPoseMatrix(new Matrix4f(camera.rotation()));
            posestack.scale(pZ, -pZ, pZ);
            RenderSystem.enableTexture();

            if (pScale)
            {
                RenderSystem.disableDepthTest();
            }
            else
            {
                RenderSystem.enableDepthTest();
            }

            RenderSystem.depthMask(true);
            posestack.scale(-1.0F, 1.0F, 1.0F);
            RenderSystem.applyModelViewMatrix();
            float f = p_113497_ ? (float)(-font.width(pText)) / 2.0F : 0.0F;
            f -= pColor / pZ;
            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            font.drawInBatch(pText, f, 0.0F, p_113495_, false, Transformation.identity().getMatrix(), multibuffersource$buffersource, pScale, 0, 15728880);
            multibuffersource$buffersource.endBatch();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    public interface SimpleDebugRenderer
    {
        void render(PoseStack pPoseStack, MultiBufferSource pBufferSource, double pCamX, double p_113510_, double pCamY);

    default void clear()
        {
        }
    }
}
