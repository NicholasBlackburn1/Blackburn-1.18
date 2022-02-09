package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundInitializeBorderPacket implements Packet<ClientGamePacketListener>
{
    private final double newCenterX;
    private final double newCenterZ;
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;
    private final int newAbsoluteMaxSize;
    private final int warningBlocks;
    private final int warningTime;

    public ClientboundInitializeBorderPacket(FriendlyByteBuf pBuffer)
    {
        this.newCenterX = pBuffer.readDouble();
        this.newCenterZ = pBuffer.readDouble();
        this.oldSize = pBuffer.readDouble();
        this.newSize = pBuffer.readDouble();
        this.lerpTime = pBuffer.readVarLong();
        this.newAbsoluteMaxSize = pBuffer.readVarInt();
        this.warningBlocks = pBuffer.readVarInt();
        this.warningTime = pBuffer.readVarInt();
    }

    public ClientboundInitializeBorderPacket(WorldBorder pBuffer)
    {
        this.newCenterX = pBuffer.getCenterX();
        this.newCenterZ = pBuffer.getCenterZ();
        this.oldSize = pBuffer.getSize();
        this.newSize = pBuffer.getLerpTarget();
        this.lerpTime = pBuffer.getLerpRemainingTime();
        this.newAbsoluteMaxSize = pBuffer.getAbsoluteMaxSize();
        this.warningBlocks = pBuffer.getWarningBlocks();
        this.warningTime = pBuffer.getWarningTime();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeDouble(this.newCenterX);
        pBuffer.writeDouble(this.newCenterZ);
        pBuffer.writeDouble(this.oldSize);
        pBuffer.writeDouble(this.newSize);
        pBuffer.writeVarLong(this.lerpTime);
        pBuffer.writeVarInt(this.newAbsoluteMaxSize);
        pBuffer.writeVarInt(this.warningBlocks);
        pBuffer.writeVarInt(this.warningTime);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleInitializeBorder(this);
    }

    public double getNewCenterX()
    {
        return this.newCenterX;
    }

    public double getNewCenterZ()
    {
        return this.newCenterZ;
    }

    public double getNewSize()
    {
        return this.newSize;
    }

    public double getOldSize()
    {
        return this.oldSize;
    }

    public long getLerpTime()
    {
        return this.lerpTime;
    }

    public int getNewAbsoluteMaxSize()
    {
        return this.newAbsoluteMaxSize;
    }

    public int getWarningTime()
    {
        return this.warningTime;
    }

    public int getWarningBlocks()
    {
        return this.warningBlocks;
    }
}
