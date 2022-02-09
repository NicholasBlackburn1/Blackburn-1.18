package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class PacketDecoder extends ByteToMessageDecoder
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker MARKER = MarkerManager.getMarker("PACKET_RECEIVED", Connection.PACKET_MARKER);
    private final PacketFlow flow;

    public PacketDecoder(PacketFlow pFlow)
    {
        this.flow = pFlow;
    }

    protected void decode(ChannelHandlerContext p_130535_, ByteBuf p_130536_, List<Object> p_130537_) throws Exception
    {
        int i = p_130536_.readableBytes();

        if (i != 0)
        {
            FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(p_130536_);
            int j = friendlybytebuf.readVarInt();
            Packet<?> packet = p_130535_.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get().createPacket(this.flow, j, friendlybytebuf);

            if (packet == null)
            {
                throw new IOException("Bad packet id " + j);
            }
            else
            {
                int k = p_130535_.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get().getId();
                JvmProfiler.INSTANCE.onPacketReceived(k, j, p_130535_.channel().remoteAddress(), i);

                if (friendlybytebuf.readableBytes() > 0)
                {
                    throw new IOException("Packet " + p_130535_.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get().getId() + "/" + j + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + friendlybytebuf.readableBytes() + " bytes extra whilst reading packet " + j);
                }
                else
                {
                    p_130537_.add(packet);

                    if (LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug(MARKER, " IN: [{}:{}] {}", p_130535_.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get(), j, packet.getClass().getName());
                    }
                }
            }
        }
    }
}
