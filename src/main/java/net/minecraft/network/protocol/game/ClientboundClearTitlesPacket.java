package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundClearTitlesPacket implements Packet<ClientGamePacketListener>
{
    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(boolean pBuffer)
    {
        this.resetTimes = pBuffer;
    }

    public ClientboundClearTitlesPacket(FriendlyByteBuf pBuffer)
    {
        this.resetTimes = pBuffer.readBoolean();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeBoolean(this.resetTimes);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleTitlesClear(this);
    }

    public boolean shouldResetTimes()
    {
        return this.resetTimes;
    }
}
