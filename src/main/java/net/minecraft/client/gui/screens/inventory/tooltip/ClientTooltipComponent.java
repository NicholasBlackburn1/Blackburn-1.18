package net.minecraft.client.gui.screens.inventory.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public interface ClientTooltipComponent
{
    static ClientTooltipComponent create(FormattedCharSequence pText)
    {
        return new ClientTextTooltip(pText);
    }

    static ClientTooltipComponent create(TooltipComponent pText)
    {
        if (pText instanceof BundleTooltip)
        {
            return new ClientBundleTooltip((BundleTooltip)pText);
        }
        else
        {
            throw new IllegalArgumentException("Unknown TooltipComponent");
        }
    }

    int getHeight();

    int getWidth(Font pFont);

default void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource)
    {
    }

default void renderImage(Font p_194048_, int p_194049_, int p_194050_, PoseStack p_194051_, ItemRenderer p_194052_, int p_194053_)
    {
    }
}
