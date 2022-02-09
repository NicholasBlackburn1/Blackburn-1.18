package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;

public class ServerboundUseItemPacket implements Packet<ServerGamePacketListener>
{
    private final InteractionHand hand;

    public ServerboundUseItemPacket(InteractionHand pBuffer)
    {
        this.hand = pBuffer;
    }

    public ServerboundUseItemPacket(FriendlyByteBuf pBuffer)
    {
        this.hand = pBuffer.readEnum(InteractionHand.class);
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeEnum(this.hand);
    }

    public void handle(ServerGamePacketListener pHandler)
    {
        pHandler.handleUseItem(this);
    }

    public InteractionHand getHand()
    {
        return this.hand;
    }
}
