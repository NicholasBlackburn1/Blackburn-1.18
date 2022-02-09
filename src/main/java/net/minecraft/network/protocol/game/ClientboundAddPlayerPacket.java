package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;

public class ClientboundAddPlayerPacket implements Packet<ClientGamePacketListener>
{
    private final int entityId;
    private final UUID playerId;
    private final double x;
    private final double y;
    private final double z;
    private final byte yRot;
    private final byte xRot;

    public ClientboundAddPlayerPacket(Player pBuffer)
    {
        this.entityId = pBuffer.getId();
        this.playerId = pBuffer.getGameProfile().getId();
        this.x = pBuffer.getX();
        this.y = pBuffer.getY();
        this.z = pBuffer.getZ();
        this.yRot = (byte)((int)(pBuffer.getYRot() * 256.0F / 360.0F));
        this.xRot = (byte)((int)(pBuffer.getXRot() * 256.0F / 360.0F));
    }

    public ClientboundAddPlayerPacket(FriendlyByteBuf pBuffer)
    {
        this.entityId = pBuffer.readVarInt();
        this.playerId = pBuffer.readUUID();
        this.x = pBuffer.readDouble();
        this.y = pBuffer.readDouble();
        this.z = pBuffer.readDouble();
        this.yRot = pBuffer.readByte();
        this.xRot = pBuffer.readByte();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.entityId);
        pBuffer.writeUUID(this.playerId);
        pBuffer.writeDouble(this.x);
        pBuffer.writeDouble(this.y);
        pBuffer.writeDouble(this.z);
        pBuffer.writeByte(this.yRot);
        pBuffer.writeByte(this.xRot);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleAddPlayer(this);
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public UUID getPlayerId()
    {
        return this.playerId;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public byte getyRot()
    {
        return this.yRot;
    }

    public byte getxRot()
    {
        return this.xRot;
    }
}
