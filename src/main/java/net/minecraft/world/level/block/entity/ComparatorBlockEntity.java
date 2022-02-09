package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class ComparatorBlockEntity extends BlockEntity
{
    private int output;

    public ComparatorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(BlockEntityType.COMPARATOR, pWorldPosition, pBlockState);
    }

    protected void saveAdditional(CompoundTag p_187493_)
    {
        super.saveAdditional(p_187493_);
        p_187493_.putInt("OutputSignal", this.output);
    }

    public void load(CompoundTag pTag)
    {
        super.load(pTag);
        this.output = pTag.getInt("OutputSignal");
    }

    public int getOutputSignal()
    {
        return this.output;
    }

    public void setOutputSignal(int pOutput)
    {
        this.output = pOutput;
    }
}
