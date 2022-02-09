package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ClientboundExplodePacket implements Packet<ClientGamePacketListener>
{
    private final double x;
    private final double y;
    private final double z;
    private final float power;
    private final List<BlockPos> toBlow;
    private final float knockbackX;
    private final float knockbackY;
    private final float knockbackZ;

    public ClientboundExplodePacket(double pX, double p_132116_, double pY, float p_132118_, List<BlockPos> pZ, @Nullable Vec3 p_132120_)
    {
        this.x = pX;
        this.y = p_132116_;
        this.z = pY;
        this.power = p_132118_;
        this.toBlow = Lists.newArrayList(pZ);

        if (p_132120_ != null)
        {
            this.knockbackX = (float)p_132120_.x;
            this.knockbackY = (float)p_132120_.y;
            this.knockbackZ = (float)p_132120_.z;
        }
        else
        {
            this.knockbackX = 0.0F;
            this.knockbackY = 0.0F;
            this.knockbackZ = 0.0F;
        }
    }

    public ClientboundExplodePacket(FriendlyByteBuf pBuffer)
    {
        this.x = (double)pBuffer.readFloat();
        this.y = (double)pBuffer.readFloat();
        this.z = (double)pBuffer.readFloat();
        this.power = pBuffer.readFloat();
        int i = Mth.floor(this.x);
        int j = Mth.floor(this.y);
        int k = Mth.floor(this.z);
        this.toBlow = pBuffer.readList((p_178850_) ->
        {
            int l = p_178850_.readByte() + i;
            int i1 = p_178850_.readByte() + j;
            int j1 = p_178850_.readByte() + k;
            return new BlockPos(l, i1, j1);
        });
        this.knockbackX = pBuffer.readFloat();
        this.knockbackY = pBuffer.readFloat();
        this.knockbackZ = pBuffer.readFloat();
    }

    public void write(FriendlyByteBuf pBuffer)
    {
        pBuffer.writeFloat((float)this.x);
        pBuffer.writeFloat((float)this.y);
        pBuffer.writeFloat((float)this.z);
        pBuffer.writeFloat(this.power);
        int i = Mth.floor(this.x);
        int j = Mth.floor(this.y);
        int k = Mth.floor(this.z);
        pBuffer.writeCollection(this.toBlow, (p_178855_, p_178856_) ->
        {
            int l = p_178856_.getX() - i;
            int i1 = p_178856_.getY() - j;
            int j1 = p_178856_.getZ() - k;
            p_178855_.writeByte(l);
            p_178855_.writeByte(i1);
            p_178855_.writeByte(j1);
        });
        pBuffer.writeFloat(this.knockbackX);
        pBuffer.writeFloat(this.knockbackY);
        pBuffer.writeFloat(this.knockbackZ);
    }

    public void handle(ClientGamePacketListener pHandler)
    {
        pHandler.handleExplosion(this);
    }

    public float getKnockbackX()
    {
        return this.knockbackX;
    }

    public float getKnockbackY()
    {
        return this.knockbackY;
    }

    public float getKnockbackZ()
    {
        return this.knockbackZ;
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

    public float getPower()
    {
        return this.power;
    }

    public List<BlockPos> getToBlow()
    {
        return this.toBlow;
    }
}
