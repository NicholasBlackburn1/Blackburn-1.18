package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public abstract class ServerboundMovePlayerPacket implements Packet<ServerGamePacketListener>
{
    protected final double x;
    protected final double y;
    protected final double z;
    protected final float yRot;
    protected final float xRot;
    protected final boolean onGround;
    protected final boolean hasPos;
    protected final boolean hasRot;

    protected ServerboundMovePlayerPacket(double pX, double p_179676_, double pY, float p_179678_, float pZ, boolean p_179680_, boolean pYRot, boolean pXRot)
    {
        this.x = pX;
        this.y = p_179676_;
        this.z = pY;
        this.yRot = p_179678_;
        this.xRot = pZ;
        this.onGround = p_179680_;
        this.hasPos = pYRot;
        this.hasRot = pXRot;
    }

    public void handle(ServerGamePacketListener pHandler)
    {
        pHandler.handleMovePlayer(this);
    }

    public double getX(double pDefaultValue)
    {
        return this.hasPos ? this.x : pDefaultValue;
    }

    public double getY(double pDefaultValue)
    {
        return this.hasPos ? this.y : pDefaultValue;
    }

    public double getZ(double pDefaultValue)
    {
        return this.hasPos ? this.z : pDefaultValue;
    }

    public float getYRot(float pDefaultValue)
    {
        return this.hasRot ? this.yRot : pDefaultValue;
    }

    public float getXRot(float pDefaultValue)
    {
        return this.hasRot ? this.xRot : pDefaultValue;
    }

    public boolean isOnGround()
    {
        return this.onGround;
    }

    public boolean hasPosition()
    {
        return this.hasPos;
    }

    public boolean hasRotation()
    {
        return this.hasRot;
    }

    public static class Pos extends ServerboundMovePlayerPacket
    {
        public Pos(double pX, double p_134151_, double pY, boolean p_134153_)
        {
            super(pX, p_134151_, pY, 0.0F, 0.0F, p_134153_, true, false);
        }

        public static ServerboundMovePlayerPacket.Pos read(FriendlyByteBuf pBuffer)
        {
            double d0 = pBuffer.readDouble();
            double d1 = pBuffer.readDouble();
            double d2 = pBuffer.readDouble();
            boolean flag = pBuffer.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.Pos(d0, d1, d2, flag);
        }

        public void write(FriendlyByteBuf pBuffer)
        {
            pBuffer.writeDouble(this.x);
            pBuffer.writeDouble(this.y);
            pBuffer.writeDouble(this.z);
            pBuffer.writeByte(this.onGround ? 1 : 0);
        }
    }

    public static class PosRot extends ServerboundMovePlayerPacket
    {
        public PosRot(double pX, double p_134163_, double pY, float p_134165_, float pZ, boolean p_134167_)
        {
            super(pX, p_134163_, pY, p_134165_, pZ, p_134167_, true, true);
        }

        public static ServerboundMovePlayerPacket.PosRot read(FriendlyByteBuf pBuffer)
        {
            double d0 = pBuffer.readDouble();
            double d1 = pBuffer.readDouble();
            double d2 = pBuffer.readDouble();
            float f = pBuffer.readFloat();
            float f1 = pBuffer.readFloat();
            boolean flag = pBuffer.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.PosRot(d0, d1, d2, f, f1, flag);
        }

        public void write(FriendlyByteBuf pBuffer)
        {
            pBuffer.writeDouble(this.x);
            pBuffer.writeDouble(this.y);
            pBuffer.writeDouble(this.z);
            pBuffer.writeFloat(this.yRot);
            pBuffer.writeFloat(this.xRot);
            pBuffer.writeByte(this.onGround ? 1 : 0);
        }
    }

    public static class Rot extends ServerboundMovePlayerPacket
    {
        public Rot(float pYRot, float pXRot, boolean pOnGround)
        {
            super(0.0D, 0.0D, 0.0D, pYRot, pXRot, pOnGround, false, true);
        }

        public static ServerboundMovePlayerPacket.Rot read(FriendlyByteBuf pBuffer)
        {
            float f = pBuffer.readFloat();
            float f1 = pBuffer.readFloat();
            boolean flag = pBuffer.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.Rot(f, f1, flag);
        }

        public void write(FriendlyByteBuf pBuffer)
        {
            pBuffer.writeFloat(this.yRot);
            pBuffer.writeFloat(this.xRot);
            pBuffer.writeByte(this.onGround ? 1 : 0);
        }
    }

    public static class StatusOnly extends ServerboundMovePlayerPacket
    {
        public StatusOnly(boolean pOnGround)
        {
            super(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, pOnGround, false, false);
        }

        public static ServerboundMovePlayerPacket.StatusOnly read(FriendlyByteBuf pBuffer)
        {
            boolean flag = pBuffer.readUnsignedByte() != 0;
            return new ServerboundMovePlayerPacket.StatusOnly(flag);
        }

        public void write(FriendlyByteBuf pBuffer)
        {
            pBuffer.writeByte(this.onGround ? 1 : 0);
        }
    }
}
