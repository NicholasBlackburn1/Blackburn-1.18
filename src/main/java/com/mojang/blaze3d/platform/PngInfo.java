package com.mojang.blaze3d.platform;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import org.lwjgl.stb.STBIEOFCallback;
import org.lwjgl.stb.STBIIOCallbacks;
import org.lwjgl.stb.STBIReadCallback;
import org.lwjgl.stb.STBISkipCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class PngInfo
{
    public final int width;
    public final int height;
    private static final Object STATIC_MONITOR = new Object();

    public PngInfo(String pFileName, InputStream pTextureStream) throws IOException
    {
        synchronized (STATIC_MONITOR)
        {
            MemoryStack memorystack = MemoryStack.stackPush();

            try
            {
                PngInfo.StbReader pnginfo$stbreader = createCallbacks(pTextureStream);

                try
                {
                    STBIReadCallback stbireadcallback = STBIReadCallback.create(pnginfo$stbreader::read);

                    try
                    {
                        STBISkipCallback stbiskipcallback = STBISkipCallback.create(pnginfo$stbreader::skip);

                        try
                        {
                            STBIEOFCallback stbieofcallback = STBIEOFCallback.create(pnginfo$stbreader::eof);

                            try
                            {
                                STBIIOCallbacks stbiiocallbacks = STBIIOCallbacks.mallocStack(memorystack);
                                stbiiocallbacks.read(stbireadcallback);
                                stbiiocallbacks.skip(stbiskipcallback);
                                stbiiocallbacks.eof(stbieofcallback);
                                IntBuffer intbuffer = memorystack.mallocInt(1);
                                IntBuffer intbuffer1 = memorystack.mallocInt(1);
                                IntBuffer intbuffer2 = memorystack.mallocInt(1);

                                if (!STBImage.stbi_info_from_callbacks(stbiiocallbacks, 0L, intbuffer, intbuffer1, intbuffer2))
                                {
                                    throw new IOException("Could not read info from the PNG file " + pFileName + " " + STBImage.stbi_failure_reason());
                                }

                                this.width = intbuffer.get(0);
                                this.height = intbuffer1.get(0);
                            }
                            catch (Throwable throwable5)
                            {
                                if (stbieofcallback != null)
                                {
                                    try
                                    {
                                        stbieofcallback.close();
                                    }
                                    catch (Throwable throwable41)
                                    {
                                        throwable5.addSuppressed(throwable41);
                                    }
                                }

                                throw throwable5;
                            }

                            if (stbieofcallback != null)
                            {
                                stbieofcallback.close();
                            }
                        }
                        catch (Throwable throwable6)
                        {
                            if (stbiskipcallback != null)
                            {
                                try
                                {
                                    stbiskipcallback.close();
                                }
                                catch (Throwable throwable31)
                                {
                                    throwable6.addSuppressed(throwable31);
                                }
                            }

                            throw throwable6;
                        }

                        if (stbiskipcallback != null)
                        {
                            stbiskipcallback.close();
                        }
                    }
                    catch (Throwable throwable7)
                    {
                        if (stbireadcallback != null)
                        {
                            try
                            {
                                stbireadcallback.close();
                            }
                            catch (Throwable throwable21)
                            {
                                throwable7.addSuppressed(throwable21);
                            }
                        }

                        throw throwable7;
                    }

                    if (stbireadcallback != null)
                    {
                        stbireadcallback.close();
                    }
                }
                catch (Throwable throwable8)
                {
                    if (pnginfo$stbreader != null)
                    {
                        try
                        {
                            pnginfo$stbreader.close();
                        }
                        catch (Throwable throwable11)
                        {
                            throwable8.addSuppressed(throwable11);
                        }
                    }

                    throw throwable8;
                }

                if (pnginfo$stbreader != null)
                {
                    pnginfo$stbreader.close();
                }
            }
            catch (Throwable throwable91)
            {
                if (memorystack != null)
                {
                    try
                    {
                        memorystack.close();
                    }
                    catch (Throwable throwable1)
                    {
                        throwable91.addSuppressed(throwable1);
                    }
                }

                throw throwable91;
            }

            if (memorystack != null)
            {
                memorystack.close();
            }
        }
    }

    public String toString()
    {
        return this.width + " x " + this.height;
    }

    private static PngInfo.StbReader createCallbacks(InputStream pTextureStream)
    {
        return (PngInfo.StbReader)(pTextureStream instanceof FileInputStream ? new PngInfo.StbReaderSeekableByteChannel(((FileInputStream)pTextureStream).getChannel()) : new PngInfo.StbReaderBufferedChannel(Channels.newChannel(pTextureStream)));
    }

    abstract static class StbReader implements AutoCloseable
    {
        protected boolean closed;

        int read(long pFunctionPointer, long p_85225_, int pAdress)
        {
            try
            {
                return this.read(p_85225_, pAdress);
            }
            catch (IOException ioexception)
            {
                this.closed = true;
                return 0;
            }
        }

        void skip(long pFunctionPointer, int p_85222_)
        {
            try
            {
                this.skip(p_85222_);
            }
            catch (IOException ioexception)
            {
                this.closed = true;
            }
        }

        int eof(long pFunctionPointer)
        {
            return this.closed ? 1 : 0;
        }

        protected abstract int read(long pAddress, int p_85228_) throws IOException;

        protected abstract void skip(int pOffset) throws IOException;

        public abstract void close() throws IOException;
    }

    static class StbReaderBufferedChannel extends PngInfo.StbReader
    {
        private static final int START_BUFFER_SIZE = 128;
        private final ReadableByteChannel channel;
        private long readBufferAddress = MemoryUtil.nmemAlloc(128L);
        private int bufferSize = 128;
        private int read;
        private int consumed;

        StbReaderBufferedChannel(ReadableByteChannel pChannel)
        {
            this.channel = pChannel;
        }

        private void fillReadBuffer(int pLength) throws IOException
        {
            ByteBuffer bytebuffer = MemoryUtil.memByteBuffer(this.readBufferAddress, this.bufferSize);

            if (pLength + this.consumed > this.bufferSize)
            {
                this.bufferSize = pLength + this.consumed;
                bytebuffer = MemoryUtil.memRealloc(bytebuffer, this.bufferSize);
                this.readBufferAddress = MemoryUtil.memAddress(bytebuffer);
            }

            bytebuffer.position(this.read);

            while (pLength + this.consumed > this.read)
            {
                try
                {
                    int i = this.channel.read(bytebuffer);

                    if (i == -1)
                    {
                        break;
                    }
                }
                finally
                {
                    this.read = bytebuffer.position();
                }
            }
        }

        public int read(long pAddress, int p_85246_) throws IOException
        {
            this.fillReadBuffer(p_85246_);

            if (p_85246_ + this.consumed > this.read)
            {
                p_85246_ = this.read - this.consumed;
            }

            MemoryUtil.memCopy(this.readBufferAddress + (long)this.consumed, pAddress, (long)p_85246_);
            this.consumed += p_85246_;
            return p_85246_;
        }

        public void skip(int pOffset) throws IOException
        {
            if (pOffset > 0)
            {
                this.fillReadBuffer(pOffset);

                if (pOffset + this.consumed > this.read)
                {
                    throw new EOFException("Can't skip past the EOF.");
                }
            }

            if (this.consumed + pOffset < 0)
            {
                throw new IOException("Can't seek before the beginning: " + (this.consumed + pOffset));
            }
            else
            {
                this.consumed += pOffset;
            }
        }

        public void close() throws IOException
        {
            MemoryUtil.nmemFree(this.readBufferAddress);
            this.channel.close();
        }
    }

    static class StbReaderSeekableByteChannel extends PngInfo.StbReader
    {
        private final SeekableByteChannel channel;

        StbReaderSeekableByteChannel(SeekableByteChannel pChannel)
        {
            this.channel = pChannel;
        }

        public int read(long pAddress, int p_85260_) throws IOException
        {
            ByteBuffer bytebuffer = MemoryUtil.memByteBuffer(pAddress, p_85260_);
            return this.channel.read(bytebuffer);
        }

        public void skip(int pOffset) throws IOException
        {
            this.channel.position(this.channel.position() + (long)pOffset);
        }

        public int eof(long pFunctionPointer)
        {
            return super.eof(pFunctionPointer) != 0 && this.channel.isOpen() ? 1 : 0;
        }

        public void close() throws IOException
        {
            this.channel.close();
        }
    }
}
