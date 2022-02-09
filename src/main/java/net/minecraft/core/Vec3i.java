package net.minecraft.core;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import net.minecraft.Util;
import net.minecraft.util.Mth;

@Immutable
public class Vec3i implements Comparable<Vec3i>
{
    public static final Codec<Vec3i> CODEC = Codec.INT_STREAM.comapFlatMap((p_123318_) ->
    {
        return Util.fixedSize(p_123318_, 3).map((p_175586_) -> {
            return new Vec3i(p_175586_[0], p_175586_[1], p_175586_[2]);
        });
    }, (p_123313_) ->
    {
        return IntStream.of(p_123313_.getX(), p_123313_.getY(), p_123313_.getZ());
    });
    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    private int x;
    private int y;
    private int z;

    private static Function<Vec3i, DataResult<Vec3i>> checkOffsetAxes(int p_194646_)
    {
        return (p_194649_) ->
        {
            return Math.abs(p_194649_.getX()) < p_194646_ && Math.abs(p_194649_.getY()) < p_194646_ && Math.abs(p_194649_.getZ()) < p_194646_ ? DataResult.success(p_194649_) : DataResult.error("Position out of range, expected at most " + p_194646_ + ": " + p_194649_);
        };
    }

    public static Codec<Vec3i> offsetCodec(int p_194651_)
    {
        return CODEC.flatXmap(checkOffsetAxes(p_194651_), checkOffsetAxes(p_194651_));
    }

    public Vec3i(int pX, int p_123297_, int pY)
    {
        this.x = pX;
        this.y = p_123297_;
        this.z = pY;
    }

    public Vec3i(double pX, double p_123293_, double pY)
    {
        this(Mth.floor(pX), Mth.floor(p_123293_), Mth.floor(pY));
    }

    public boolean equals(Object pOther)
    {
        if (this == pOther)
        {
            return true;
        }
        else if (!(pOther instanceof Vec3i))
        {
            return false;
        }
        else
        {
            Vec3i vec3i = (Vec3i)pOther;

            if (this.getX() != vec3i.getX())
            {
                return false;
            }
            else if (this.getY() != vec3i.getY())
            {
                return false;
            }
            else
            {
                return this.getZ() == vec3i.getZ();
            }
        }
    }

    public int hashCode()
    {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3i p_123330_)
    {
        if (this.getY() == p_123330_.getY())
        {
            return this.getZ() == p_123330_.getZ() ? this.getX() - p_123330_.getX() : this.getZ() - p_123330_.getZ();
        }
        else
        {
            return this.getY() - p_123330_.getY();
        }
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    protected Vec3i setX(int pX)
    {
        this.x = pX;
        return this;
    }

    protected Vec3i setY(int pY)
    {
        this.y = pY;
        return this;
    }

    protected Vec3i setZ(int pZ)
    {
        this.z = pZ;
        return this;
    }

    public Vec3i offset(double pDx, double p_175588_, double pDy)
    {
        return pDx == 0.0D && p_175588_ == 0.0D && pDy == 0.0D ? this : new Vec3i((double)this.getX() + pDx, (double)this.getY() + p_175588_, (double)this.getZ() + pDy);
    }

    public Vec3i offset(int pDx, int p_175594_, int pDy)
    {
        return pDx == 0 && p_175594_ == 0 && pDy == 0 ? this : new Vec3i(this.getX() + pDx, this.getY() + p_175594_, this.getZ() + pDy);
    }

    public Vec3i offset(Vec3i pVector)
    {
        return this.offset(pVector.getX(), pVector.getY(), pVector.getZ());
    }

    public Vec3i subtract(Vec3i pVec)
    {
        return this.offset(-pVec.getX(), -pVec.getY(), -pVec.getZ());
    }

    public Vec3i multiply(int pScalar)
    {
        if (pScalar == 1)
        {
            return this;
        }
        else
        {
            return pScalar == 0 ? ZERO : new Vec3i(this.getX() * pScalar, this.getY() * pScalar, this.getZ() * pScalar);
        }
    }

    public Vec3i above()
    {
        return this.above(1);
    }

    public Vec3i above(int pDistance)
    {
        return this.relative(Direction.UP, pDistance);
    }

    public Vec3i below()
    {
        return this.below(1);
    }

    public Vec3i below(int pDistance)
    {
        return this.relative(Direction.DOWN, pDistance);
    }

    public Vec3i north()
    {
        return this.north(1);
    }

    public Vec3i north(int pDistance)
    {
        return this.relative(Direction.NORTH, pDistance);
    }

    public Vec3i south()
    {
        return this.south(1);
    }

    public Vec3i south(int pDistance)
    {
        return this.relative(Direction.SOUTH, pDistance);
    }

    public Vec3i west()
    {
        return this.west(1);
    }

    public Vec3i west(int pDistance)
    {
        return this.relative(Direction.WEST, pDistance);
    }

    public Vec3i east()
    {
        return this.east(1);
    }

    public Vec3i east(int pDistance)
    {
        return this.relative(Direction.EAST, pDistance);
    }

    public Vec3i relative(Direction pDirection)
    {
        return this.relative(pDirection, 1);
    }

    public Vec3i relative(Direction pAxis, int pDistance)
    {
        return pDistance == 0 ? this : new Vec3i(this.getX() + pAxis.getStepX() * pDistance, this.getY() + pAxis.getStepY() * pDistance, this.getZ() + pAxis.getStepZ() * pDistance);
    }

    public Vec3i relative(Direction.Axis pAxis, int pDistance)
    {
        if (pDistance == 0)
        {
            return this;
        }
        else
        {
            int i = pAxis == Direction.Axis.X ? pDistance : 0;
            int j = pAxis == Direction.Axis.Y ? pDistance : 0;
            int k = pAxis == Direction.Axis.Z ? pDistance : 0;
            return new Vec3i(this.getX() + i, this.getY() + j, this.getZ() + k);
        }
    }

    public Vec3i cross(Vec3i pVector)
    {
        return new Vec3i(this.getY() * pVector.getZ() - this.getZ() * pVector.getY(), this.getZ() * pVector.getX() - this.getX() * pVector.getZ(), this.getX() * pVector.getY() - this.getY() * pVector.getX());
    }

    public boolean closerThan(Vec3i pPosition, double pDistance)
    {
        return this.distSqr((double)pPosition.getX(), (double)pPosition.getY(), (double)pPosition.getZ(), false) < pDistance * pDistance;
    }

    public boolean closerThan(Position pPosition, double pDistance)
    {
        return this.distSqr(pPosition.x(), pPosition.y(), pPosition.z(), true) < pDistance * pDistance;
    }

    public double distSqr(Vec3i pVector)
    {
        return this.distSqr((double)pVector.getX(), (double)pVector.getY(), (double)pVector.getZ(), true);
    }

    public double distSqr(Position pPosition, boolean pUseCenter)
    {
        return this.distSqr(pPosition.x(), pPosition.y(), pPosition.z(), pUseCenter);
    }

    public double distSqr(Vec3i pPosition, boolean pUseCenter)
    {
        return this.distSqr((double)pPosition.x, (double)pPosition.y, (double)pPosition.z, pUseCenter);
    }

    public double distSqr(double pX, double p_123301_, double pY, boolean p_123303_)
    {
        double d0 = p_123303_ ? 0.5D : 0.0D;
        double d1 = (double)this.getX() + d0 - pX;
        double d2 = (double)this.getY() + d0 - p_123301_;
        double d3 = (double)this.getZ() + d0 - pY;
        return d1 * d1 + d2 * d2 + d3 * d3;
    }

    public int distManhattan(Vec3i pVector)
    {
        float f = (float)Math.abs(pVector.getX() - this.getX());
        float f1 = (float)Math.abs(pVector.getY() - this.getY());
        float f2 = (float)Math.abs(pVector.getZ() - this.getZ());
        return (int)(f + f1 + f2);
    }

    public int get(Direction.Axis pAxis)
    {
        return pAxis.choose(this.x, this.y, this.z);
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    public String toShortString()
    {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }
}
