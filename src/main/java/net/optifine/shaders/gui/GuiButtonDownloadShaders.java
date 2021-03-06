package net.optifine.shaders.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.optifine.gui.GuiButtonOF;

public class GuiButtonDownloadShaders extends GuiButtonOF
{
    public GuiButtonDownloadShaders(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 22, 20, "");
    }

    public void render(PoseStack matrixStackIn, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            super.render(matrixStackIn, mouseX, mouseY, partialTicks);
            ResourceLocation resourcelocation = new ResourceLocation("optifine/textures/icons.png");
            RenderSystem.setShaderTexture(0, resourcelocation);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.blit(matrixStackIn, this.x + 3, this.y + 2, 0, 0, 16, 16);
        }
    }
}
