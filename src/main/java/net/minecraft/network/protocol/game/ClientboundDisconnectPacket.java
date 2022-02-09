package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundDisconnectPacket implements Packet<ClientGamePacketListener>
{
    private final Component reason;

    public ClientboundDisconnectPacket(Component pBuffer)
    {
        this.reason = pBuffer;
    }

    public ClientboundDisconnectPacket(FriendlyByteBuf pBuffer)
    {
        this.reason = pBuffer.readComponent();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeComponent(this.reason);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleDisconnect(this);
    }

    public Component getReason()
    {
        return this.reason;
    }
}
