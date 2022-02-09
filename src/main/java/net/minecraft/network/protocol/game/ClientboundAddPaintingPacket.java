package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;

public class ClientboundAddPaintingPacket implements Packet<ClientGamePacketListener>
{
    private final int id;
    private final UUID uuid;
    private final BlockPos pos;
    private final Direction direction;
    private final int motive;

    public ClientboundAddPaintingPacket(Painting pBuffer)
    {
        this.id = pBuffer.getId();
        this.uuid = pBuffer.getUUID();
        this.pos = pBuffer.getPos();
        this.direction = pBuffer.getDirection();
        this.motive = Registry.MOTIVE.getId(pBuffer.motive);
    }

    public ClientboundAddPaintingPacket(FriendlyByteBuf pBuffer)
    {
        this.id = pBuffer.readVarInt();
        this.uuid = pBuffer.readUUID();
        this.motive = pBuffer.readVarInt();
        this.pos = pBuffer.readBlockPos();
        this.direction = Direction.from2DDataValue(pBuffer.readUnsignedByte());
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.id);
        pBuffer.writeUUID(this.uuid);
        pBuffer.writeVarInt(this.motive);
        pBuffer.writeBlockPos(this.pos);
        pBuffer.writeByte(this.direction.get2DDataValue());
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleAddPainting(this);
    }

    public int getId()
    {
        return this.id;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public Direction getDirection()
    {
        return this.direction;
    }

    public Motive getMotive()
    {
        return Registry.MOTIVE.byId(this.motive);
    }
}
