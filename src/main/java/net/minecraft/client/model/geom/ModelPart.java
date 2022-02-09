package net.minecraft.client.model.geom;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.optifine.Config;
import net.optifine.IRandomEntity;
import net.optifine.RandomEntities;
import net.optifine.entity.model.anim.ModelUpdater;
import net.optifine.model.ModelSprite;
import net.optifine.render.BoxVertexPositions;
import net.optifine.render.VertexPosition;
import net.optifine.shaders.Shaders;

public final class ModelPart
{
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean visible = true;
    public final List<ModelPart.Cube> cubes;
    public final Map<String, ModelPart> children;
    public List<ModelPart> childModelsList;
    public List<ModelSprite> spriteList = new ArrayList<>();
    public boolean mirrorV = false;
    public float scaleX = 1.0F;
    public float scaleY = 1.0F;
    public float scaleZ = 1.0F;
    private ResourceLocation textureLocation = null;
    private String id = null;
    private ModelUpdater modelUpdater;
    private LevelRenderer renderGlobal = Config.getRenderGlobal();
    public float textureWidth = 64.0F;
    public float textureHeight = 32.0F;
    private int textureOffsetX;
    private int textureOffsetY;
    public boolean mirror;

    public ModelPart setTextureOffset(int x, int y)
    {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public ModelPart setTextureSize(int textureWidthIn, int textureHeightIn)
    {
        this.textureWidth = (float)textureWidthIn;
        this.textureHeight = (float)textureHeightIn;
        return this;
    }

    public ModelPart(List<ModelPart.Cube> pCubes, Map<String, ModelPart> pChildren)
    {
        if (pCubes instanceof ImmutableList)
        {
            pCubes = new ArrayList<>(pCubes);
        }

        this.cubes = pCubes;
        this.children = pChildren;
        this.childModelsList = new ArrayList<>(this.children.values());
    }

    public PartPose storePose()
    {
        return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
    }

    public void loadPose(PartPose pPartPose)
    {
        this.x = pPartPose.x;
        this.y = pPartPose.y;
        this.z = pPartPose.z;
        this.xRot = pPartPose.xRot;
        this.yRot = pPartPose.yRot;
        this.zRot = pPartPose.zRot;
    }

    public void copyFrom(ModelPart pModelPart)
    {
        this.xRot = pModelPart.xRot;
        this.yRot = pModelPart.yRot;
        this.zRot = pModelPart.zRot;
        this.x = pModelPart.x;
        this.y = pModelPart.y;
        this.z = pModelPart.z;
    }

    public ModelPart getChilds(String pName)
    {
        ModelPart modelpart = this.children.get(pName);

        if (modelpart == null)
        {
            throw new NoSuchElementException("Can't find part " + pName);
        }
        else
        {
            return modelpart;
        }
    }

    public void setPos(float pX, float pY, float pZ)
    {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    public void setRotation(float pXRot, float pYRot, float pZRot)
    {
        this.xRot = pXRot;
        this.yRot = pYRot;
        this.zRot = pZRot;
    }

    public void render(PoseStack pPoseStack, VertexConsumer pVertexConsumer, int pPackedLight, int pPackedOverlay)
    {
        this.render(pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack pPoseStack, VertexConsumer pVertexConsumer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        if (this.visible && (!this.cubes.isEmpty() || !this.children.isEmpty() || !this.spriteList.isEmpty()))
        {
            RenderType rendertype = null;
            MultiBufferSource.BufferSource multibuffersource$buffersource = null;

            if (this.textureLocation != null)
            {
                if (this.renderGlobal.renderOverlayEyes)
                {
                    return;
                }

                multibuffersource$buffersource = pVertexConsumer.getRenderTypeBuffer();

                if (multibuffersource$buffersource != null)
                {
                    VertexConsumer vertexconsumer = pVertexConsumer.getSecondaryBuilder();
                    rendertype = multibuffersource$buffersource.getLastRenderType();
                    pVertexConsumer = multibuffersource$buffersource.getBuffer(this.textureLocation, pVertexConsumer);

                    if (vertexconsumer != null)
                    {
                        pVertexConsumer = VertexMultiConsumer.create(vertexconsumer, pVertexConsumer);
                    }
                }
            }

            if (this.modelUpdater != null)
            {
                this.modelUpdater.update();
            }

            pPoseStack.pushPose();
            this.translateAndRotate(pPoseStack);
            this.compile(pPoseStack.last(), pVertexConsumer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            int j = this.childModelsList.size();

            for (int i = 0; i < j; ++i)
            {
                ModelPart modelpart = this.childModelsList.get(i);
                modelpart.render(pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            }

            int k = this.spriteList.size();

            for (int l = 0; l < k; ++l)
            {
                ModelSprite modelsprite = this.spriteList.get(l);
                modelsprite.render(pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            }

            pPoseStack.popPose();

            if (rendertype != null)
            {
                multibuffersource$buffersource.getBuffer(rendertype);
            }
        }
    }

    public void visit(PoseStack pPoseStack, ModelPart.Visitor pVisitor)
    {
        this.visit(pPoseStack, pVisitor, "");
    }

    private void visit(PoseStack p_171313_, ModelPart.Visitor p_171314_, String p_171315_)
    {
        if (!this.cubes.isEmpty() || !this.children.isEmpty())
        {
            p_171313_.pushPose();
            this.translateAndRotate(p_171313_);
            PoseStack.Pose posestack$pose = p_171313_.last();

            for (int i = 0; i < this.cubes.size(); ++i)
            {
                p_171314_.visit(posestack$pose, p_171315_, i, this.cubes.get(i));
            }

            String s = p_171315_ + "/";
            this.children.forEach((p_171316_3_, p_171316_4_) ->
            {
                p_171316_4_.visit(p_171313_, p_171314_, s + p_171316_3_);
            });
            p_171313_.popPose();
        }
    }

    public void translateAndRotate(PoseStack pPoseStack)
    {
        pPoseStack.translate((double)(this.x / 16.0F), (double)(this.y / 16.0F), (double)(this.z / 16.0F));

        if (this.zRot != 0.0F)
        {
            pPoseStack.mulPose(Vector3f.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F)
        {
            pPoseStack.mulPose(Vector3f.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F)
        {
            pPoseStack.mulPose(Vector3f.XP.rotation(this.xRot));
        }
    }

    private void compile(PoseStack.Pose pPose, VertexConsumer pVertexConsumer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        boolean flag = Config.isShaders() && Shaders.useVelocityAttrib && Config.isMinecraftThread();
        int i = this.cubes.size();

        for (int j = 0; j < i; ++j)
        {
            ModelPart.Cube modelpart$cube = this.cubes.get(j);
            VertexPosition[][] avertexposition = null;

            if (flag)
            {
                IRandomEntity irandomentity = RandomEntities.getRandomEntityRendered();

                if (irandomentity != null)
                {
                    avertexposition = modelpart$cube.getBoxVertexPositions(irandomentity.getId());
                }
            }

            modelpart$cube.compile(pPose, pVertexConsumer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha, avertexposition);
        }
    }

    public ModelPart.Cube getRandomCube(Random pRandom)
    {
        return this.cubes.get(pRandom.nextInt(this.cubes.size()));
    }

    public boolean isEmpty()
    {
        return this.cubes.isEmpty();
    }

    public Stream<ModelPart> getAllParts()
    {
        return Stream.concat(Stream.of(this), this.children.values().stream().flatMap(ModelPart::getAllParts));
    }

    public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd)
    {
        this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
    }

    public ResourceLocation getTextureLocation()
    {
        return this.textureLocation;
    }

    public void setTextureLocation(ResourceLocation textureLocation)
    {
        this.textureLocation = textureLocation;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta)
    {
        this.cubes.add(new ModelPart.Cube(faceUvs, x, y, z, dx, dy, dz, delta, delta, delta, this.mirror, this.textureWidth, this.textureHeight));
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta)
    {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
    }

    private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirror, boolean dummyIn)
    {
        this.cubes.add(new ModelPart.Cube(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirror, this.textureWidth, this.textureHeight));
    }

    public ModelPart getChildModelDeep(String name)
    {
        if (name == null)
        {
            return null;
        }
        else if (this.children.containsKey(name))
        {
            return this.getChilds(name);
        }
        else
        {
            if (this.children != null)
            {
                for (String s : this.children.keySet())
                {
                    ModelPart modelpart = this.children.get(s);
                    ModelPart modelpart1 = modelpart.getChildModelDeep(name);

                    if (modelpart1 != null)
                    {
                        return modelpart1;
                    }
                }
            }

            return null;
        }
    }

    public ModelPart getChild(String id)
    {
        if (id == null)
        {
            return null;
        }
        else
        {
            if (this.children != null)
            {
                for (String s : this.children.keySet())
                {
                    ModelPart modelpart = this.children.get(s);

                    if (id.equals(modelpart.getId()))
                    {
                        return modelpart;
                    }
                }
            }

            return null;
        }
    }

    public ModelPart getChildDeep(String id)
    {
        if (id == null)
        {
            return null;
        }
        else
        {
            ModelPart modelpart = this.getChild(id);

            if (modelpart != null)
            {
                return modelpart;
            }
            else
            {
                if (this.children != null)
                {
                    for (String s : this.children.keySet())
                    {
                        ModelPart modelpart1 = this.children.get(s);
                        ModelPart modelpart2 = modelpart1.getChildDeep(id);

                        if (modelpart2 != null)
                        {
                            return modelpart2;
                        }
                    }
                }

                return null;
            }
        }
    }

    public void setModelUpdater(ModelUpdater modelUpdater)
    {
        this.modelUpdater = modelUpdater;
    }

    public void addChildModel(String name, ModelPart part)
    {
        if (part != null)
        {
            this.children.put(name, part);
            this.childModelsList = new ArrayList<>(this.children.values());
        }
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("id: " + this.id + ", boxes: " + (this.cubes != null ? this.cubes.size() : null) + ", submodels: " + (this.children != null ? this.children.size() : null));
        return stringbuffer.toString();
    }

    public static class Cube
    {
        private final ModelPart.Polygon[] polygons;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;
        private BoxVertexPositions boxVertexPositions;

        public Cube(int pTexCoordU, int pTexCoordV, float pMinX, float pMinY, float pMinZ, float pDimensionX, float pDimensionY, float pDimensionZ, float pGrowX, float pGrowY, float pGrowZ, boolean pMirror, float pTexWidthScaled, float pTexHeightScaled)
        {
            this.minX = pMinX;
            this.minY = pMinY;
            this.minZ = pMinZ;
            this.maxX = pMinX + pDimensionX;
            this.maxY = pMinY + pDimensionY;
            this.maxZ = pMinZ + pDimensionZ;
            this.polygons = new ModelPart.Polygon[6];
            float f = pMinX + pDimensionX;
            float f1 = pMinY + pDimensionY;
            float f2 = pMinZ + pDimensionZ;
            pMinX -= pGrowX;
            pMinY -= pGrowY;
            pMinZ -= pGrowZ;
            f += pGrowX;
            f1 += pGrowY;
            f2 += pGrowZ;

            if (pMirror)
            {
                float f3 = f;
                f = pMinX;
                pMinX = f3;
            }

            ModelPart.Vertex modelpart$vertex7 = new ModelPart.Vertex(pMinX, pMinY, pMinZ, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex = new ModelPart.Vertex(f, pMinY, pMinZ, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex1 = new ModelPart.Vertex(f, f1, pMinZ, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex2 = new ModelPart.Vertex(pMinX, f1, pMinZ, 8.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex3 = new ModelPart.Vertex(pMinX, pMinY, f2, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex4 = new ModelPart.Vertex(f, pMinY, f2, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex5 = new ModelPart.Vertex(f, f1, f2, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex6 = new ModelPart.Vertex(pMinX, f1, f2, 8.0F, 0.0F);
            float f4 = (float)pTexCoordU;
            float f5 = (float)pTexCoordU + pDimensionZ;
            float f6 = (float)pTexCoordU + pDimensionZ + pDimensionX;
            float f7 = (float)pTexCoordU + pDimensionZ + pDimensionX + pDimensionX;
            float f8 = (float)pTexCoordU + pDimensionZ + pDimensionX + pDimensionZ;
            float f9 = (float)pTexCoordU + pDimensionZ + pDimensionX + pDimensionZ + pDimensionX;
            float f10 = (float)pTexCoordV;
            float f11 = (float)pTexCoordV + pDimensionZ;
            float f12 = (float)pTexCoordV + pDimensionZ + pDimensionY;
            this.polygons[2] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex}, f5, f10, f6, f11, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.DOWN);
            this.polygons[3] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex1, modelpart$vertex2, modelpart$vertex6, modelpart$vertex5}, f6, f11, f7, f10, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.UP);
            this.polygons[1] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2}, f4, f11, f5, f12, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.WEST);
            this.polygons[4] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1}, f5, f11, f6, f12, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.NORTH);
            this.polygons[0] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5}, f6, f11, f8, f12, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.EAST);
            this.polygons[5] = new ModelPart.Polygon(new ModelPart.Vertex[] {modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6}, f8, f11, f9, f12, pTexWidthScaled, pTexHeightScaled, pMirror, Direction.SOUTH);
        }

        public Cube(int[][] faceUvs, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight)
        {
            this.minX = x;
            this.minY = y;
            this.minZ = z;
            this.maxX = x + width;
            this.maxY = y + height;
            this.maxZ = z + depth;
            this.polygons = new ModelPart.Polygon[6];
            float f = x + width;
            float f1 = y + height;
            float f2 = z + depth;
            x -= deltaX;
            y -= deltaY;
            z -= deltaZ;
            f += deltaX;
            f1 += deltaY;
            f2 += deltaZ;

            if (mirorIn)
            {
                float f3 = f;
                f = x;
                x = f3;
            }

            ModelPart.Vertex modelpart$vertex7 = new ModelPart.Vertex(x, y, z, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex = new ModelPart.Vertex(f, y, z, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex1 = new ModelPart.Vertex(f, f1, z, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex2 = new ModelPart.Vertex(x, f1, z, 8.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex3 = new ModelPart.Vertex(x, y, f2, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex4 = new ModelPart.Vertex(f, y, f2, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex5 = new ModelPart.Vertex(f, f1, f2, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex6 = new ModelPart.Vertex(x, f1, f2, 8.0F, 0.0F);
            this.polygons[2] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex}, faceUvs[1], true, texWidth, texHeight, mirorIn, Direction.DOWN);
            this.polygons[3] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex1, modelpart$vertex2, modelpart$vertex6, modelpart$vertex5}, faceUvs[0], true, texWidth, texHeight, mirorIn, Direction.UP);
            this.polygons[1] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2}, faceUvs[5], false, texWidth, texHeight, mirorIn, Direction.WEST);
            this.polygons[4] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1}, faceUvs[2], false, texWidth, texHeight, mirorIn, Direction.NORTH);
            this.polygons[0] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5}, faceUvs[4], false, texWidth, texHeight, mirorIn, Direction.EAST);
            this.polygons[5] = this.makeTexturedQuad(new ModelPart.Vertex[] {modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6}, faceUvs[3], false, texWidth, texHeight, mirorIn, Direction.SOUTH);
        }

        private ModelPart.Polygon makeTexturedQuad(ModelPart.Vertex[] positionTextureVertexs, int[] faceUvs, boolean reverseUV, float textureWidth, float textureHeight, boolean mirrorIn, Direction directionIn)
        {
            if (faceUvs == null)
            {
                return null;
            }
            else
            {
                return reverseUV ? new ModelPart.Polygon(positionTextureVertexs, (float)faceUvs[2], (float)faceUvs[3], (float)faceUvs[0], (float)faceUvs[1], textureWidth, textureHeight, mirrorIn, directionIn) : new ModelPart.Polygon(positionTextureVertexs, (float)faceUvs[0], (float)faceUvs[1], (float)faceUvs[2], (float)faceUvs[3], textureWidth, textureHeight, mirrorIn, directionIn);
            }
        }

        public VertexPosition[][] getBoxVertexPositions(int key)
        {
            if (this.boxVertexPositions == null)
            {
                this.boxVertexPositions = new BoxVertexPositions();
            }

            return this.boxVertexPositions.get(key);
        }

        public void compile(PoseStack.Pose pPose, VertexConsumer pVertexConsumer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
        {
            this.compile(pPose, pVertexConsumer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha, (VertexPosition[][])null);
        }

        public void compile(PoseStack.Pose matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, VertexPosition[][] boxPos)
        {
            Matrix4f matrix4f = matrixEntryIn.pose();
            Matrix3f matrix3f = matrixEntryIn.normal();
            int i = this.polygons.length;

            for (int j = 0; j < i; ++j)
            {
                ModelPart.Polygon modelpart$polygon = this.polygons[j];

                if (modelpart$polygon != null)
                {
                    if (boxPos != null)
                    {
                        bufferIn.setQuadVertexPositions(boxPos[j]);
                    }

                    Vector3f vector3f = bufferIn.getTempVec3f(modelpart$polygon.normal);
                    vector3f.transform(matrix3f);
                    float f = vector3f.x();
                    float f1 = vector3f.y();
                    float f2 = vector3f.z();

                    for (ModelPart.Vertex modelpart$vertex : modelpart$polygon.vertices)
                    {
                        float f3 = modelpart$vertex.pos.x() / 16.0F;
                        float f4 = modelpart$vertex.pos.y() / 16.0F;
                        float f5 = modelpart$vertex.pos.z() / 16.0F;
                        float f6 = matrix4f.getTransformX(f3, f4, f5, 1.0F);
                        float f7 = matrix4f.getTransformY(f3, f4, f5, 1.0F);
                        float f8 = matrix4f.getTransformZ(f3, f4, f5, 1.0F);
                        bufferIn.vertex(f6, f7, f8, red, green, blue, alpha, modelpart$vertex.u, modelpart$vertex.v, packedOverlayIn, packedLightIn, f, f1, f2);
                    }
                }
            }
        }
    }

    static class Polygon
    {
        public final ModelPart.Vertex[] vertices;
        public final Vector3f normal;

        public Polygon(ModelPart.Vertex[] p_104362_, float p_104363_, float p_104364_, float p_104365_, float p_104366_, float p_104367_, float p_104368_, boolean p_104369_, Direction p_104370_)
        {
            this.vertices = p_104362_;
            float f = 0.0F / p_104367_;
            float f1 = 0.0F / p_104368_;

            if (Config.isAntialiasing())
            {
                f = 0.05F / p_104367_;
                f1 = 0.05F / p_104368_;

                if (p_104365_ < p_104363_)
                {
                    f = -f;
                }

                if (p_104366_ < p_104364_)
                {
                    f1 = -f1;
                }
            }

            p_104362_[0] = p_104362_[0].remap(p_104365_ / p_104367_ - f, p_104364_ / p_104368_ + f1);
            p_104362_[1] = p_104362_[1].remap(p_104363_ / p_104367_ + f, p_104364_ / p_104368_ + f1);
            p_104362_[2] = p_104362_[2].remap(p_104363_ / p_104367_ + f, p_104366_ / p_104368_ - f1);
            p_104362_[3] = p_104362_[3].remap(p_104365_ / p_104367_ - f, p_104366_ / p_104368_ - f1);

            if (p_104369_)
            {
                int i = p_104362_.length;

                for (int j = 0; j < i / 2; ++j)
                {
                    ModelPart.Vertex modelpart$vertex = p_104362_[j];
                    p_104362_[j] = p_104362_[i - 1 - j];
                    p_104362_[i - 1 - j] = modelpart$vertex;
                }
            }

            this.normal = p_104370_.step();

            if (p_104369_)
            {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }
        }
    }

    static class Vertex
    {
        public final Vector3f pos;
        public final float u;
        public final float v;

        public Vertex(float pX, float pY, float pZ, float pU, float pV)
        {
            this(new Vector3f(pX, pY, pZ), pU, pV);
        }

        public ModelPart.Vertex remap(float pU, float pV)
        {
            return new ModelPart.Vertex(this.pos, pU, pV);
        }

        public Vertex(Vector3f pPos, float pU, float pV)
        {
            this.pos = pPos;
            this.u = pU;
            this.v = pV;
        }
    }

    @FunctionalInterface
    public interface Visitor
    {
        void visit(PoseStack.Pose p_171342_, String p_171343_, int p_171344_, ModelPart.Cube p_171345_);
    }
}
