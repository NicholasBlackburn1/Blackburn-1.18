package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.optifine.Config;
import net.optifine.render.MultiTextureData;
import net.optifine.render.MultiTextureRenderer;
import net.optifine.render.VboRange;
import net.optifine.render.VboRegion;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;

public class VertexBuffer implements AutoCloseable
{
    private int vertextBufferId;
    private int indexBufferId;
    private VertexFormat.IndexType indexType;
    private int arrayObjectId;
    private int indexCount;
    private VertexFormat.Mode mode;
    private boolean sequentialIndices;
    private VertexFormat format;
    private VboRegion vboRegion;
    private VboRange vboRange;
    private MultiTextureData multiTextureData;

    public VertexBuffer()
    {
        RenderSystem.glGenBuffers((idIn) ->
        {
            this.vertextBufferId = idIn;
        });
        RenderSystem.glGenVertexArrays((idIn) ->
        {
            this.arrayObjectId = idIn;
        });
        RenderSystem.glGenBuffers((idIn) ->
        {
            this.indexBufferId = idIn;
        });
    }

    public void bind()
    {
        GlStateManager._glBindBuffer(34962, this.vertextBufferId);

        if (this.sequentialIndices)
        {
            RenderSystem.AutoStorageIndexBuffer rendersystem$autostorageindexbuffer = RenderSystem.getSequentialBuffer(this.mode, this.indexCount);
            this.indexType = rendersystem$autostorageindexbuffer.type();
            GlStateManager._glBindBuffer(34963, rendersystem$autostorageindexbuffer.name());
        }
        else
        {
            GlStateManager._glBindBuffer(34963, this.indexBufferId);
        }
    }

    public void upload(BufferBuilder pBuilder)
    {
        if (!RenderSystem.isOnRenderThread())
        {
            RenderSystem.recordRenderCall(() ->
            {
                this.upload_(pBuilder);
            });
        }
        else
        {
            this.upload_(pBuilder);
        }
    }

    public CompletableFuture<Void> uploadLater(BufferBuilder pBuilder)
    {
        if (!RenderSystem.isOnRenderThread())
        {
            return CompletableFuture.runAsync(() ->
            {
                this.upload_(pBuilder);
            }, (runnableIn) ->
            {
                RenderSystem.recordRenderCall(runnableIn::run);
            });
        }
        else
        {
            this.upload_(pBuilder);
            return CompletableFuture.completedFuture((Void)null);
        }
    }

    private void upload_(BufferBuilder pBuilder)
    {
        Pair<BufferBuilder.DrawState, ByteBuffer> pair = pBuilder.popNextBuffer();
        BufferBuilder.DrawState bufferbuilder$drawstate = pair.getFirst();

        if (this.vboRegion != null)
        {
            ByteBuffer bytebuffer1 = pair.getSecond();
            bytebuffer1.position(0);
            bytebuffer1.limit(bufferbuilder$drawstate.vertexBufferSize());
            this.vboRegion.bufferData(bytebuffer1, this.vboRange);
            bytebuffer1.position(0);
            bytebuffer1.limit(bufferbuilder$drawstate.bufferSize());
        }
        else
        {
            this.multiTextureData = bufferbuilder$drawstate.getMultiTextureData();

            if (this.vertextBufferId != 0)
            {
                BufferUploader.reset();
                BufferBuilder.DrawState bufferbuilder$drawstate1 = pair.getFirst();
                ByteBuffer bytebuffer = pair.getSecond();
                int i = bufferbuilder$drawstate1.vertexBufferSize();
                this.indexCount = bufferbuilder$drawstate1.indexCount();
                this.indexType = bufferbuilder$drawstate1.indexType();
                this.format = bufferbuilder$drawstate1.format();
                this.mode = bufferbuilder$drawstate1.mode();
                this.sequentialIndices = bufferbuilder$drawstate1.sequentialIndex();
                this.bindVertexArray();
                this.bind();

                if (!bufferbuilder$drawstate1.indexOnly())
                {
                    bytebuffer.limit(i);
                    RenderSystem.glBufferData(34962, bytebuffer, 35044);
                    bytebuffer.position(i);
                }

                if (!this.sequentialIndices)
                {
                    bytebuffer.limit(bufferbuilder$drawstate1.bufferSize());
                    RenderSystem.glBufferData(34963, bytebuffer, 35044);
                    bytebuffer.position(0);
                }
                else
                {
                    bytebuffer.limit(bufferbuilder$drawstate1.bufferSize());
                    bytebuffer.position(0);
                }

                unbind();
                unbindVertexArray();
            }
        }
    }

    private void bindVertexArray()
    {
        GlStateManager._glBindVertexArray(this.arrayObjectId);
    }

    public static void unbindVertexArray()
    {
        GlStateManager._glBindVertexArray(0);
    }

    public void draw()
    {
        if (this.vboRegion != null)
        {
            this.vboRegion.drawArrays(VertexFormat.Mode.QUADS, this.vboRange);
        }
        else if (this.multiTextureData != null)
        {
            MultiTextureRenderer.draw(this.mode, this.indexType.asGLType, this.multiTextureData);
        }
        else if (this.indexCount != 0)
        {
            RenderSystem.drawElements(this.mode.asGLMode, this.indexCount, this.indexType.asGLType);
        }
    }

    public void drawWithShader(Matrix4f pModelViewMatrix, Matrix4f pProjectionMatrix, ShaderInstance pShaderInstance)
    {
        if (!RenderSystem.isOnRenderThread())
        {
            RenderSystem.recordRenderCall(() ->
            {
                this._drawWithShader(pModelViewMatrix.copy(), pProjectionMatrix.copy(), pShaderInstance);
            });
        }
        else
        {
            this._drawWithShader(pModelViewMatrix, pProjectionMatrix, pShaderInstance);
        }
    }

    public void _drawWithShader(Matrix4f pModelViewMatrix, Matrix4f pProjectionMatrix, ShaderInstance pShaderInstance)
    {
        if (this.indexCount != 0)
        {
            RenderSystem.assertOnRenderThread();
            BufferUploader.reset();

            for (int i = 0; i < 12; ++i)
            {
                int j = RenderSystem.getShaderTexture(i);
                pShaderInstance.setSampler(i, j);
            }

            if (pShaderInstance.MODEL_VIEW_MATRIX != null)
            {
                pShaderInstance.MODEL_VIEW_MATRIX.set(pModelViewMatrix);
            }

            if (pShaderInstance.PROJECTION_MATRIX != null)
            {
                pShaderInstance.PROJECTION_MATRIX.set(pProjectionMatrix);
            }

            if (pShaderInstance.INVERSE_VIEW_ROTATION_MATRIX != null)
            {
                pShaderInstance.INVERSE_VIEW_ROTATION_MATRIX.set(RenderSystem.getInverseViewRotationMatrix());
            }

            if (pShaderInstance.COLOR_MODULATOR != null)
            {
                pShaderInstance.COLOR_MODULATOR.a(RenderSystem.getShaderColor());
            }

            if (pShaderInstance.FOG_START != null)
            {
                pShaderInstance.FOG_START.set(RenderSystem.getShaderFogStart());
            }

            if (pShaderInstance.FOG_END != null)
            {
                pShaderInstance.FOG_END.set(RenderSystem.getShaderFogEnd());
            }

            if (pShaderInstance.FOG_COLOR != null)
            {
                pShaderInstance.FOG_COLOR.a(RenderSystem.getShaderFogColor());
            }

            if (pShaderInstance.TEXTURE_MATRIX != null)
            {
                pShaderInstance.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
            }

            if (pShaderInstance.GAME_TIME != null)
            {
                pShaderInstance.GAME_TIME.set(RenderSystem.getShaderGameTime());
            }

            if (pShaderInstance.SCREEN_SIZE != null)
            {
                Window window = Minecraft.getInstance().getWindow();
                pShaderInstance.SCREEN_SIZE.set((float)window.getWidth(), (float)window.getHeight());
            }

            if (pShaderInstance.LINE_WIDTH != null && (this.mode == VertexFormat.Mode.LINES || this.mode == VertexFormat.Mode.LINE_STRIP))
            {
                pShaderInstance.LINE_WIDTH.set(RenderSystem.getShaderLineWidth());
            }

            if (Config.isShaders())
            {
                Shaders.setModelViewMatrix(pModelViewMatrix);
                Shaders.setProjectionMatrix(pProjectionMatrix);
                Shaders.setTextureMatrix(RenderSystem.getTextureMatrix());
                Shaders.setColorModulator(RenderSystem.getShaderColor());
            }

            RenderSystem.setupShaderLights(pShaderInstance);
            this.bindVertexArray();
            this.bind();
            this.getFormat().setupBufferState();
            pShaderInstance.apply();

            if (Config.isShaders())
            {
                ShadersRender.setupArrayPointersVbo();
            }

            this.draw();
            pShaderInstance.clear();
            this.getFormat().clearBufferState();
            unbind();
            unbindVertexArray();
        }
    }

    public void drawChunkLayer()
    {
        if (this.indexCount != 0)
        {
            RenderSystem.assertOnRenderThread();
            this.bindVertexArray();
            this.bind();
            this.format.setupBufferState();

            if (Config.isShaders())
            {
                ShadersRender.setupArrayPointersVbo();
            }

            this.draw();
        }
    }

    public static void unbind()
    {
        GlStateManager._glBindBuffer(34962, 0);
        GlStateManager._glBindBuffer(34963, 0);
    }

    public void close()
    {
        if (this.indexBufferId >= 0)
        {
            RenderSystem.glDeleteBuffers(this.indexBufferId);
            this.indexBufferId = -1;
        }

        if (this.vertextBufferId > 0)
        {
            RenderSystem.glDeleteBuffers(this.vertextBufferId);
            this.vertextBufferId = 0;
        }

        if (this.arrayObjectId > 0)
        {
            RenderSystem.glDeleteVertexArrays(this.arrayObjectId);
            this.arrayObjectId = 0;
        }
    }

    public VertexFormat getFormat()
    {
        return this.format;
    }

    public void setVboRegion(VboRegion vboRegion)
    {
        if (vboRegion != null)
        {
            this.close();
            this.vboRegion = vboRegion;
            this.vboRange = new VboRange();
        }
    }

    public VboRegion getVboRegion()
    {
        return this.vboRegion;
    }
}
