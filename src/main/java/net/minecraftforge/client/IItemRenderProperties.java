package net.minecraftforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;

public interface IItemRenderProperties
{
    IItemRenderProperties DUMMY = new IItemRenderProperties()
    {
    };

default Font getFont(ItemStack stack)
    {
        return null;
    }

default BlockEntityWithoutLevelRenderer getItemStackRenderer()
    {
        return Minecraft.getInstance().getItemRenderer().getBlockEntityRenderer();
    }
}
