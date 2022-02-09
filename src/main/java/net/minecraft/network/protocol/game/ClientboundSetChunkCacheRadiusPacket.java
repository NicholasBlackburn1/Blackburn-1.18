package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetChunkCacheRadiusPacket implements Packet<ClientGamePacketListener>
{
    private final int radius;

    public ClientboundSetChunkCacheRadiusPacket(int pRadius)
    {
        this.radius = pRadius;
    }

    public ClientboundSetChunkCacheRadiusPacket(FriendlyByteBuf pRadius)
    {
        this.radius = pRadius.readVarInt();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.radius);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleSetChunkCacheRadius(this);
    }

    public int getRadius()
    {
        return this.radius;
    }
}
