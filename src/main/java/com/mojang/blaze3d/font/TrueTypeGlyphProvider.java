package com.mojang.blaze3d.font;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class TrueTypeGlyphProvider implements GlyphProvider
{
    private final ByteBuffer fontMemory;
    final STBTTFontinfo font;
    final float oversample;
    private final IntSet skip = new IntArraySet();
    final float shiftX;
    final float shiftY;
    final float pointScale;
    final float ascent;

    public TrueTypeGlyphProvider(ByteBuffer pFontMemory, STBTTFontinfo pFont, float pHeight, float pOversample, float pShiftX, float pShiftY, String pSkip)
    {
        this.fontMemory = pFontMemory;
        this.font = pFont;
        this.oversample = pOversample;
        pSkip.codePoints().forEach(this.skip::add);
        this.shiftX = pShiftX * pOversample;
        this.shiftY = pShiftY * pOversample;
        this.pointScale = STBTruetype.stbtt_ScaleForPixelHeight(pFont, pHeight * pOversample);
        MemoryStack memorystack = MemoryStack.stackPush();

        try
        {
            IntBuffer intbuffer = memorystack.mallocInt(1);
            IntBuffer intbuffer1 = memorystack.mallocInt(1);
            IntBuffer intbuffer2 = memorystack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(pFont, intbuffer, intbuffer1, intbuffer2);
            this.ascent = (float)intbuffer.get(0) * this.pointScale;
        }
        catch (Throwable throwable1)
        {
            if (memorystack != null)
            {
                try
                {
                    memorystack.close();
                }
                catch (Throwable throwable)
                {
                    throwable1.addSuppressed(throwable);
                }
            }

            throw throwable1;
        }

        if (memorystack != null)
        {
            memorystack.close();
        }
    }

    @Nullable
    public TrueTypeGlyphProvider.Glyph getGlyph(int pCharacter)
    {
        if (this.skip.contains(pCharacter))
        {
            return null;
        }
        else
        {
            MemoryStack memorystack = MemoryStack.stackPush();
            Object object1;
            label61:
            {
                TrueTypeGlyphProvider.Glyph truetypeglyphprovider$glyph;
                label62:
                {
                    Object $$9;

                    try
                    {
                        IntBuffer intbuffer = memorystack.mallocInt(1);
                        IntBuffer intbuffer1 = memorystack.mallocInt(1);
                        IntBuffer intbuffer2 = memorystack.mallocInt(1);
                        IntBuffer intbuffer3 = memorystack.mallocInt(1);
                        int i = STBTruetype.stbtt_FindGlyphIndex(this.font, pCharacter);

                        if (i == 0)
                        {
                            object1 = null;
                            break label61;
                        }

                        STBTruetype.stbtt_GetGlyphBitmapBoxSubpixel(this.font, i, this.pointScale, this.pointScale, this.shiftX, this.shiftY, intbuffer, intbuffer1, intbuffer2, intbuffer3);
                        int j = intbuffer2.get(0) - intbuffer.get(0);
                        int k = intbuffer3.get(0) - intbuffer1.get(0);

                        if (j > 0 && k > 0)
                        {
                            IntBuffer intbuffer5 = memorystack.mallocInt(1);
                            IntBuffer intbuffer4 = memorystack.mallocInt(1);
                            STBTruetype.stbtt_GetGlyphHMetrics(this.font, i, intbuffer5, intbuffer4);
                            truetypeglyphprovider$glyph = new TrueTypeGlyphProvider.Glyph(intbuffer.get(0), intbuffer2.get(0), -intbuffer1.get(0), -intbuffer3.get(0), (float)intbuffer5.get(0) * this.pointScale, (float)intbuffer4.get(0) * this.pointScale, i);
                            break label62;
                        }

                        $$9 = null;
                    }
                    catch (Throwable throwable1)
                    {
                        if (memorystack != null)
                        {
                            try
                            {
                                memorystack.close();
                            }
                            catch (Throwable throwable)
                            {
                                throwable1.addSuppressed(throwable);
                            }
                        }

                        throw throwable1;
                    }

                    if (memorystack != null)
                    {
                        memorystack.close();
                    }

                    return (TrueTypeGlyphProvider.Glyph)$$9;
                }

                if (memorystack != null)
                {
                    memorystack.close();
                }

                return truetypeglyphprovider$glyph;
            }

            if (memorystack != null)
            {
                memorystack.close();
            }

            return (TrueTypeGlyphProvider.Glyph)object1;
        }
    }

    public void close()
    {
        this.font.free();
        MemoryUtil.memFree(this.fontMemory);
    }

    public IntSet getSupportedGlyphs()
    {
        return IntStream.range(0, 65535).filter((p_83863_) ->
        {
            return !this.skip.contains(p_83863_);
        }).collect(IntOpenHashSet::new, IntCollection::add, IntCollection::addAll);
    }

    class Glyph implements RawGlyph
    {
        private final int width;
        private final int height;
        private final float bearingX;
        private final float bearingY;
        private final float advance;
        private final int index;

        Glyph(int p_83882_, int p_83883_, int p_83884_, int p_83885_, float p_83886_, float p_83887_, int p_83888_)
        {
            this.width = p_83883_ - p_83882_;
            this.height = p_83884_ - p_83885_;
            this.advance = p_83886_ / TrueTypeGlyphProvider.this.oversample;
            this.bearingX = (p_83887_ + (float)p_83882_ + TrueTypeGlyphProvider.this.shiftX) / TrueTypeGlyphProvider.this.oversample;
            this.bearingY = (TrueTypeGlyphProvider.this.ascent - (float)p_83884_ + TrueTypeGlyphProvider.this.shiftY) / TrueTypeGlyphProvider.this.oversample;
            this.index = p_83888_;
        }

        public int getPixelWidth()
        {
            return this.width;
        }

        public int getPixelHeight()
        {
            return this.height;
        }

        public float getOversample()
        {
            return TrueTypeGlyphProvider.this.oversample;
        }

        public float getAdvance()
        {
            return this.advance;
        }

        public float getBearingX()
        {
            return this.bearingX;
        }

        public float getBearingY()
        {
            return this.bearingY;
        }

        public void upload(int pXOffset, int pYOffset)
        {
            NativeImage nativeimage = new NativeImage(NativeImage.Format.LUMINANCE, this.width, this.height, false);
            nativeimage.copyFromFont(TrueTypeGlyphProvider.this.font, this.index, this.width, this.height, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.shiftX, TrueTypeGlyphProvider.this.shiftY, 0, 0);
            nativeimage.upload(0, pXOffset, pYOffset, 0, 0, this.width, this.height, false, true);
        }

        public boolean isColored()
        {
            return false;
        }
    }
}
