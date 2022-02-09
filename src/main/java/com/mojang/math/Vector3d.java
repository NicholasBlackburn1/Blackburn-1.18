package com.mojang.math;

public class Vector3d
{
    public double x;
    public double y;
    public double z;

    public Vector3d(double pX, double p_86219_, double pY)
    {
        this.x = pX;
        this.y = p_86219_;
        this.z = pY;
    }

    public void set(Vector3d pOther)
    {
        this.x = pOther.x;
        this.y = pOther.y;
        this.z = pOther.z;
    }

    public void set(double pX, double p_176287_, double pY)
    {
        this.x = pX;
        this.y = p_176287_;
        this.z = pY;
    }

    public void scale(double pScale)
    {
        this.x *= pScale;
        this.y *= pScale;
        this.z *= pScale;
    }

    public void add(Vector3d pOther)
    {
        this.x += pOther.x;
        this.y += pOther.y;
        this.z += pOther.z;
    }
}
