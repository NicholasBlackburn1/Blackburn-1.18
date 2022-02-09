package net.minecraft.world.entity.ai.memory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.phys.Vec3;

public class WalkTarget
{
    private final PositionTracker target;
    private final float speedModifier;
    private final int closeEnoughDist;

    public WalkTarget(BlockPos pPos, float pSpeedModifier, int pCloseEnoughDist)
    {
        this(new BlockPosTracker(pPos), pSpeedModifier, pCloseEnoughDist);
    }

    public WalkTarget(Vec3 pPos, float pSpeedModifier, int pCloseEnoughDist)
    {
        this(new BlockPosTracker(new BlockPos(pPos)), pSpeedModifier, pCloseEnoughDist);
    }

    public WalkTarget(Entity pPos, float pSpeedModifier, int pCloseEnoughDist)
    {
        this(new EntityTracker(pPos, false), pSpeedModifier, pCloseEnoughDist);
    }

    public WalkTarget(PositionTracker pPos, float pSpeedModifier, int pCloseEnoughDist)
    {
        this.target = pPos;
        this.speedModifier = pSpeedModifier;
        this.closeEnoughDist = pCloseEnoughDist;
    }

    public PositionTracker getTarget()
    {
        return this.target;
    }

    public float getSpeedModifier()
    {
        return this.speedModifier;
    }

    public int getCloseEnoughDist()
    {
        return this.closeEnoughDist;
    }
}
