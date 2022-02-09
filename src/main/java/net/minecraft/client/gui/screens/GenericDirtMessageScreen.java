package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

public class GenericDirtMessageScreen extends Screen
{
    public GenericDirtMessageScreen(Component p_96061_)
    {
        super(p_96061_);
    }

    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderDirtBackground(0);
        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, 70, 16777215);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
