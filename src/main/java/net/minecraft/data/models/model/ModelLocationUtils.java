package net.minecraft.data.models.model;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModelLocationUtils
{
    @Deprecated
    public static ResourceLocation decorateBlockModelLocation(String pBlockModelLocation)
    {
        return new ResourceLocation("minecraft", "block/" + pBlockModelLocation);
    }

    public static ResourceLocation decorateItemModelLocation(String pItemModelLocation)
    {
        return new ResourceLocation("minecraft", "item/" + pItemModelLocation);
    }

    public static ResourceLocation getModelLocation(Block pItem, String pModelLocationSuffix)
    {
        ResourceLocation resourcelocation = Registry.BLOCK.getKey(pItem);
        return new ResourceLocation(resourcelocation.getNamespace(), "block/" + resourcelocation.getPath() + pModelLocationSuffix);
    }

    public static ResourceLocation getModelLocation(Block pItem)
    {
        ResourceLocation resourcelocation = Registry.BLOCK.getKey(pItem);
        return new ResourceLocation(resourcelocation.getNamespace(), "block/" + resourcelocation.getPath());
    }

    public static ResourceLocation getModelLocation(Item pItem)
    {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(pItem);
        return new ResourceLocation(resourcelocation.getNamespace(), "item/" + resourcelocation.getPath());
    }

    public static ResourceLocation getModelLocation(Item pItem, String pModelLocationSuffix)
    {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(pItem);
        return new ResourceLocation(resourcelocation.getNamespace(), "item/" + resourcelocation.getPath() + pModelLocationSuffix);
    }
}
