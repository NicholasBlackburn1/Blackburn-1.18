package net.minecraft.world.phys;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class AABB
{
    private static final double EPSILON = 1.0E-7D;
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AABB(double pX1, double p_82296_, double pY1, double p_82298_, double pZ1, double p_82300_)
    {
        this.minX = Math.min(pX1, p_82298_);
        this.minY = Math.min(p_82296_, pZ1);
        this.minZ = Math.min(pY1, p_82300_);
        this.maxX = Math.max(pX1, p_82298_);
        this.maxY = Math.max(p_82296_, pZ1);
        this.maxZ = Math.max(pY1, p_82300_);
    }

    public AABB(BlockPos pPos)
    {
        this((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), (double)(pPos.getX() + 1), (double)(pPos.getY() + 1), (double)(pPos.getZ() + 1));
    }

    public AABB(BlockPos pStart, BlockPos pEnd)
    {
        this((double)pStart.getX(), (double)pStart.getY(), (double)pStart.getZ(), (double)pEnd.getX(), (double)pEnd.getY(), (double)pEnd.getZ());
    }

    public AABB(Vec3 pStart, Vec3 pEnd)
    {
        this(pStart.x, pStart.y, pStart.z, pEnd.x, pEnd.y, pEnd.z);
    }

    public static AABB of(BoundingBox pMutableBox)
    {
        return new AABB((double)pMutableBox.minX(), (double)pMutableBox.minY(), (double)pMutableBox.minZ(), (double)(pMutableBox.maxX() + 1), (double)(pMutableBox.maxY() + 1), (double)(pMutableBox.maxZ() + 1));
    }

    public static AABB unitCubeFromLowerCorner(Vec3 pVector)
    {
        return new AABB(pVector.x, pVector.y, pVector.z, pVector.x + 1.0D, pVector.y + 1.0D, pVector.z + 1.0D);
    }

    public AABB setMinX(double pMinX)
    {
        return new AABB(pMinX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMinY(double pMinY)
    {
        return new AABB(this.minX, pMinY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMinZ(double pMinZ)
    {
        return new AABB(this.minX, this.minY, pMinZ, this.maxX, this.maxY, this.maxZ);
    }

    public AABB setMaxX(double pMaxX)
    {
        return new AABB(this.minX, this.minY, this.minZ, pMaxX, this.maxY, this.maxZ);
    }

    public AABB setMaxY(double pMaxY)
    {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, pMaxY, this.maxZ);
    }

    public AABB setMaxZ(double pMaxZ)
    {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, pMaxZ);
    }

    public double min(Direction.Axis pAxis)
    {
        return pAxis.choose(this.minX, this.minY, this.minZ);
    }

    public double max(Direction.Axis pAxis)
    {
        return pAxis.choose(this.maxX, this.maxY, this.maxZ);
    }

    public boolean equals(Object p_82398_)
    {
        if (this == p_82398_)
        {
            return true;
        }
        else if (!(p_82398_ instanceof AABB))
        {
            return false;
        }
        else
        {
            AABB aabb = (AABB)p_82398_;

            if (Double.compare(aabb.minX, this.minX) != 0)
            {
                return false;
            }
            else if (Double.compare(aabb.minY, this.minY) != 0)
            {
                return false;
            }
            else if (Double.compare(aabb.minZ, this.minZ) != 0)
            {
                return false;
            }
            else if (Double.compare(aabb.maxX, this.maxX) != 0)
            {
                return false;
            }
            else if (Double.compare(aabb.maxY, this.maxY) != 0)
            {
                return false;
            }
            else
            {
                return Double.compare(aabb.maxZ, this.maxZ) == 0;
            }
        }
    }

    public int hashCode()
    {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        return 31 * j + (int)(i ^ i >>> 32);
    }

    public AABB contract(double pX, double p_82312_, double pY)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (pX < 0.0D)
        {
            d0 -= pX;
        }
        else if (pX > 0.0D)
        {
            d3 -= pX;
        }

        if (p_82312_ < 0.0D)
        {
            d1 -= p_82312_;
        }
        else if (p_82312_ > 0.0D)
        {
            d4 -= p_82312_;
        }

        if (pY < 0.0D)
        {
            d2 -= pY;
        }
        else if (pY > 0.0D)
        {
            d5 -= pY;
        }

        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB expandTowards(Vec3 pVector)
    {
        return this.expandTowards(pVector.x, pVector.y, pVector.z);
    }

    public AABB expandTowards(double pX, double p_82365_, double pY)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (pX < 0.0D)
        {
            d0 += pX;
        }
        else if (pX > 0.0D)
        {
            d3 += pX;
        }

        if (p_82365_ < 0.0D)
        {
            d1 += p_82365_;
        }
        else if (p_82365_ > 0.0D)
        {
            d4 += p_82365_;
        }

        if (pY < 0.0D)
        {
            d2 += pY;
        }
        else if (pY > 0.0D)
        {
            d5 += pY;
        }

        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB inflate(double pX, double p_82379_, double pY)
    {
        double d0 = this.minX - pX;
        double d1 = this.minY - p_82379_;
        double d2 = this.minZ - pY;
        double d3 = this.maxX + pX;
        double d4 = this.maxY + p_82379_;
        double d5 = this.maxZ + pY;
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB inflate(double pValue)
    {
        return this.inflate(pValue, pValue, pValue);
    }

    public AABB intersect(AABB pOther)
    {
        double d0 = Math.max(this.minX, pOther.minX);
        double d1 = Math.max(this.minY, pOther.minY);
        double d2 = Math.max(this.minZ, pOther.minZ);
        double d3 = Math.min(this.maxX, pOther.maxX);
        double d4 = Math.min(this.maxY, pOther.maxY);
        double d5 = Math.min(this.maxZ, pOther.maxZ);
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB minmax(AABB pOther)
    {
        double d0 = Math.min(this.minX, pOther.minX);
        double d1 = Math.min(this.minY, pOther.minY);
        double d2 = Math.min(this.minZ, pOther.minZ);
        double d3 = Math.max(this.maxX, pOther.maxX);
        double d4 = Math.max(this.maxY, pOther.maxY);
        double d5 = Math.max(this.maxZ, pOther.maxZ);
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB move(double pX, double p_82388_, double pY)
    {
        return new AABB(this.minX + pX, this.minY + p_82388_, this.minZ + pY, this.maxX + pX, this.maxY + p_82388_, this.maxZ + pY);
    }

    public AABB move(BlockPos pPos)
    {
        return new AABB(this.minX + (double)pPos.getX(), this.minY + (double)pPos.getY(), this.minZ + (double)pPos.getZ(), this.maxX + (double)pPos.getX(), this.maxY + (double)pPos.getY(), this.maxZ + (double)pPos.getZ());
    }

    public AABB move(Vec3 pPos)
    {
        return this.move(pPos.x, pPos.y, pPos.z);
    }

    public boolean intersects(AABB pOther)
    {
        return this.intersects(pOther.minX, pOther.minY, pOther.minZ, pOther.maxX, pOther.maxY, pOther.maxZ);
    }

    public boolean intersects(double pX1, double p_82316_, double pY1, double p_82318_, double pZ1, double p_82320_)
    {
        return this.minX < p_82318_ && this.maxX > pX1 && this.minY < pZ1 && this.maxY > p_82316_ && this.minZ < p_82320_ && this.maxZ > pY1;
    }

    public boolean intersects(Vec3 pMin, Vec3 pMax)
    {
        return this.intersects(Math.min(pMin.x, pMax.x), Math.min(pMin.y, pMax.y), Math.min(pMin.z, pMax.z), Math.max(pMin.x, pMax.x), Math.max(pMin.y, pMax.y), Math.max(pMin.z, pMax.z));
    }

    public boolean contains(Vec3 pVec)
    {
        return this.contains(pVec.x, pVec.y, pVec.z);
    }

    public boolean contains(double pX, double p_82395_, double pY)
    {
        return pX >= this.minX && pX < this.maxX && p_82395_ >= this.minY && p_82395_ < this.maxY && pY >= this.minZ && pY < this.maxZ;
    }

    public double getSize()
    {
        double d0 = this.getXsize();
        double d1 = this.getYsize();
        double d2 = this.getZsize();
        return (d0 + d1 + d2) / 3.0D;
    }

    public double getXsize()
    {
        return this.maxX - this.minX;
    }

    public double getYsize()
    {
        return this.maxY - this.minY;
    }

    public double getZsize()
    {
        return this.maxZ - this.minZ;
    }

    public AABB deflate(double pX, double p_165899_, double pY)
    {
        return this.inflate(-pX, -p_165899_, -pY);
    }

    public AABB deflate(double pValue)
    {
        return this.inflate(-pValue);
    }

    public Optional<Vec3> clip(Vec3 pFrom, Vec3 pTo)
    {
        double[] adouble = new double[] {1.0D};
        double d0 = pTo.x - pFrom.x;
        double d1 = pTo.y - pFrom.y;
        double d2 = pTo.z - pFrom.z;
        Direction direction = a(this, pFrom, adouble, (Direction)null, d0, d1, d2);

        if (direction == null)
        {
            return Optional.empty();
        }
        else
        {
            double d3 = adouble[0];
            return Optional.of(pFrom.add(d3 * d0, d3 * d1, d3 * d2));
        }
    }

    @Nullable
    public static BlockHitResult clip(Iterable<AABB> pBoxes, Vec3 pStart, Vec3 pEnd, BlockPos pPos)
    {
        double[] adouble = new double[] {1.0D};
        Direction direction = null;
        double d0 = pEnd.x - pStart.x;
        double d1 = pEnd.y - pStart.y;
        double d2 = pEnd.z - pStart.z;

        for (AABB aabb : pBoxes)
        {
            direction = a(aabb.move(pPos), pStart, adouble, direction, d0, d1, d2);
        }

        if (direction == null)
        {
            return null;
        }
        else
        {
            double d3 = adouble[0];
            return new BlockHitResult(pStart.add(d3 * d0, d3 * d1, d3 * d2), direction, pPos, false);
        }
    }

    @Nullable
    private static Direction a(AABB p_82326_, Vec3 p_82327_, double[] p_82328_, @Nullable Direction p_82329_, double p_82330_, double p_82331_, double p_82332_)
    {
        if (p_82330_ > 1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82330_, p_82331_, p_82332_, p_82326_.minX, p_82326_.minY, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, Direction.WEST, p_82327_.x, p_82327_.y, p_82327_.z);
        }
        else if (p_82330_ < -1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82330_, p_82331_, p_82332_, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, Direction.EAST, p_82327_.x, p_82327_.y, p_82327_.z);
        }

        if (p_82331_ > 1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82331_, p_82332_, p_82330_, p_82326_.minY, p_82326_.minZ, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, Direction.DOWN, p_82327_.y, p_82327_.z, p_82327_.x);
        }
        else if (p_82331_ < -1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82331_, p_82332_, p_82330_, p_82326_.maxY, p_82326_.minZ, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, Direction.UP, p_82327_.y, p_82327_.z, p_82327_.x);
        }

        if (p_82332_ > 1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82332_, p_82330_, p_82331_, p_82326_.minZ, p_82326_.minX, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, Direction.NORTH, p_82327_.z, p_82327_.x, p_82327_.y);
        }
        else if (p_82332_ < -1.0E-7D)
        {
            p_82329_ = a(p_82328_, p_82329_, p_82332_, p_82330_, p_82331_, p_82326_.maxZ, p_82326_.minX, p_82326_.maxX, p_82326_.minY, p_82326_.maxY, Direction.SOUTH, p_82327_.z, p_82327_.x, p_82327_.y);
        }

        return p_82329_;
    }

    @Nullable
    private static Direction a(double[] p_82348_, @Nullable Direction p_82349_, double p_82350_, double p_82351_, double p_82352_, double p_82353_, double p_82354_, double p_82355_, double p_82356_, double p_82357_, Direction p_82358_, double p_82359_, double p_82360_, double p_82361_)
    {
        double d0 = (p_82353_ - p_82359_) / p_82350_;
        double d1 = p_82360_ + d0 * p_82351_;
        double d2 = p_82361_ + d0 * p_82352_;

        if (0.0D < d0 && d0 < p_82348_[0] && p_82354_ - 1.0E-7D < d1 && d1 < p_82355_ + 1.0E-7D && p_82356_ - 1.0E-7D < d2 && d2 < p_82357_ + 1.0E-7D)
        {
            p_82348_[0] = d0;
            return p_82358_;
        }
        else
        {
            return p_82349_;
        }
    }

    public String toString()
    {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean hasNaN()
    {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }

    public Vec3 getCenter()
    {
        return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), Mth.lerp(0.5D, this.minY, this.maxY), Mth.lerp(0.5D, this.minZ, this.maxZ));
    }

    public static AABB ofSize(Vec3 pCenter, double pXSize, double p_165885_, double pYSize)
    {
        return new AABB(pCenter.x - pXSize / 2.0D, pCenter.y - p_165885_ / 2.0D, pCenter.z - pYSize / 2.0D, pCenter.x + pXSize / 2.0D, pCenter.y + p_165885_ / 2.0D, pCenter.z + pYSize / 2.0D);
    }
}
