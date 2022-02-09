package net.minecraft.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;

public interface Widget
{
    void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick);
}
