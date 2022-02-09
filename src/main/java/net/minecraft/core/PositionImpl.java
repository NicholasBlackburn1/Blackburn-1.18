package net.minecraft.core;

public class PositionImpl implements Position
{
    protected final double x;
    protected final double y;
    protected final double z;

    public PositionImpl(double pX, double p_122803_, double pY)
    {
        this.x = pX;
        this.y = p_122803_;
        this.z = pY;
    }

    public double x()
    {
        return this.x;
    }

    public double y()
    {
        return this.y;
    }

    public double z()
    {
        return this.z;
    }
}
