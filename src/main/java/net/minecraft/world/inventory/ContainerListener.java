package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;

public interface ContainerListener
{
    void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack);

    void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue);
}
