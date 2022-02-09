package net.minecraft.world.level;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface CollisionGetter extends BlockGetter
{
    WorldBorder getWorldBorder();

    @Nullable
    BlockGetter getChunkForCollisions(int pChunkX, int pChunkZ);

default boolean isUnobstructed(@Nullable Entity pEntity, VoxelShape pShape)
    {
        return true;
    }

default boolean isUnobstructed(BlockState pState, BlockPos pPos, CollisionContext pContext)
    {
        VoxelShape voxelshape = pState.getCollisionShape(this, pPos, pContext);
        return voxelshape.isEmpty() || this.isUnobstructed((Entity)null, voxelshape.move((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ()));
    }

default boolean isUnobstructed(Entity pEntity)
    {
        return this.isUnobstructed(pEntity, Shapes.create(pEntity.getBoundingBox()));
    }

default boolean noCollision(AABB pEntity)
    {
        return this.noCollision((Entity)null, pEntity);
    }

default boolean noCollision(Entity pEntity)
    {
        return this.noCollision(pEntity, pEntity.getBoundingBox());
    }

default boolean noCollision(@Nullable Entity pEntity, AABB pCollisionBox)
    {
        for (VoxelShape voxelshape : this.getBlockCollisions(pEntity, pCollisionBox))
        {
            if (!voxelshape.isEmpty())
            {
                return false;
            }
        }

        if (!this.getEntityCollisions(pEntity, pCollisionBox).isEmpty())
        {
            return false;
        }
        else if (pEntity == null)
        {
            return true;
        }
        else
        {
            VoxelShape voxelshape1 = this.borderCollision(pEntity, pCollisionBox);
            return voxelshape1 == null || !Shapes.joinIsNotEmpty(voxelshape1, Shapes.create(pCollisionBox), BooleanOp.AND);
        }
    }

    List<VoxelShape> getEntityCollisions(@Nullable Entity p_186427_, AABB p_186428_);

default Iterable<VoxelShape> getCollisions(@Nullable Entity p_186432_, AABB p_186433_)
    {
        List<VoxelShape> list = this.getEntityCollisions(p_186432_, p_186433_);
        Iterable<VoxelShape> iterable = this.getBlockCollisions(p_186432_, p_186433_);
        return list.isEmpty() ? iterable : Iterables.concat(list, iterable);
    }

default Iterable<VoxelShape> getBlockCollisions(@Nullable Entity p_186435_, AABB p_186436_)
    {
        return () ->
        {
            return new BlockCollisions(this, p_186435_, p_186436_);
        };
    }

    @Nullable
    private VoxelShape borderCollision(Entity p_186441_, AABB p_186442_)
    {
        WorldBorder worldborder = this.getWorldBorder();
        return worldborder.isInsideCloseToBorder(p_186441_, p_186442_) ? worldborder.getCollisionShape() : null;
    }

default boolean collidesWithSuffocatingBlock(@Nullable Entity p_186438_, AABB p_186439_)
    {
        BlockCollisions blockcollisions = new BlockCollisions(this, p_186438_, p_186439_, true);

        while (blockcollisions.hasNext())
        {
            if (!blockcollisions.next().isEmpty())
            {
                return true;
            }
        }

        return false;
    }

default Optional<Vec3> findFreePosition(@Nullable Entity pEntity, VoxelShape pShape, Vec3 pPos, double pX, double p_151423_, double pY)
    {
        if (pShape.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            AABB aabb = pShape.bounds().inflate(pX, p_151423_, pY);
            VoxelShape voxelshape = StreamSupport.stream(this.getBlockCollisions(pEntity, aabb).spliterator(), false).filter((p_186430_) ->
            {
                return this.getWorldBorder() == null || this.getWorldBorder().isWithinBounds(p_186430_.bounds());
            }).flatMap((p_186426_) ->
            {
                return p_186426_.toAabbs().stream();
            }).map((p_186424_) ->
            {
                return p_186424_.inflate(pX / 2.0D, p_151423_ / 2.0D, pY / 2.0D);
            }).map(Shapes::create).reduce(Shapes.empty(), Shapes::or);
            VoxelShape voxelshape1 = Shapes.join(pShape, voxelshape, BooleanOp.ONLY_FIRST);
            return voxelshape1.closestPointTo(pPos);
        }
    }
}
