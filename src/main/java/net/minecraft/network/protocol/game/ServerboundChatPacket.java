package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundChatPacket implements Packet<ServerGamePacketListener>
{
    private static final int MAX_MESSAGE_LENGTH = 256;
    private final String message;

    public ServerboundChatPacket(String pMessage)
    {
        if (pMessage.length() > 256)
        {
            pMessage = pMessage.substring(0, 256);
        }

        this.message = pMessage;
    }

    public ServerboundChatPacket(FriendlyByteBuf pMessage)
    {
        this.message = pMessage.readUtf(256);
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeUtf(this.message);
    }

    public void handle(ServerGamePacketListener pHandler)
    {
        pHandler.handleChat(this);
    }

    public String getMessage()
    {
        return this.message;
    }
}
