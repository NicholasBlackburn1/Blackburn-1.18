package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPongPacket implements Packet<ServerGamePacketListener>
{
    private final int id;

    public ServerboundPongPacket(int pId)
    {
        this.id = pId;
    }

    public ServerboundPongPacket(FriendlyByteBuf pId)
    {
        this.id = pId.readInt();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeInt(this.id);
    }

    public void handle(ServerGamePacketListener pHandler)
    {
        pHandler.handlePong(this);
    }

    public int getId()
    {
        return this.id;
    }
}
