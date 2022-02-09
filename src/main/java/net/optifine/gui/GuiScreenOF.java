package net.optifine.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.optifine.util.GuiUtils;

public class GuiScreenOF extends Screen
{
    protected Font fontRenderer = Minecraft.getInstance().font;
    protected boolean mousePressed = false;

    public GuiScreenOF(Component title)
    {
        super(title);
    }

    public List<AbstractWidget> getButtonList()
    {
        List<AbstractWidget> list = new ArrayList<>();

        for (GuiEventListener guieventlistener : this.children())
        {
            if (guieventlistener instanceof AbstractWidget)
            {
                list.add((AbstractWidget)guieventlistener);
            }
        }

        return list;
    }

    protected void actionPerformed(AbstractWidget button)
    {
    }

    protected void actionPerformedRightClick(AbstractWidget button)
    {
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);
        this.mousePressed = true;
        AbstractWidget abstractwidget = getSelectedButton((int)mouseX, (int)mouseY, this.getButtonList());

        if (abstractwidget != null && abstractwidget.active)
        {
            if (mouseButton == 1 && abstractwidget instanceof IOptionControl)
            {
                IOptionControl ioptioncontrol = (IOptionControl)abstractwidget;

                if (ioptioncontrol.getControlOption() == Option.GUI_SCALE)
                {
                    abstractwidget.playDownSound(super.minecraft.getSoundManager());
                }
            }

            if (mouseButton == 0)
            {
                this.actionPerformed(abstractwidget);
            }
            else if (mouseButton == 1)
            {
                this.actionPerformedRightClick(abstractwidget);
            }

            return true;
        }
        else
        {
            return flag;
        }
    }

    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_)
    {
        if (!this.mousePressed)
        {
            return false;
        }
        else
        {
            this.mousePressed = false;
            this.setDragging(false);
            return this.getFocused() != null && this.getFocused().mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_) ? true : super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        }
    }

    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_)
    {
        return !this.mousePressed ? false : super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    public static AbstractWidget getSelectedButton(int x, int y, List<AbstractWidget> listButtons)
    {
        for (int i = 0; i < listButtons.size(); ++i)
        {
            AbstractWidget abstractwidget = listButtons.get(i);

            if (abstractwidget.visible)
            {
                int j = GuiUtils.getWidth(abstractwidget);
                int k = GuiUtils.getHeight(abstractwidget);

                if (x >= abstractwidget.x && y >= abstractwidget.y && x < abstractwidget.x + j && y < abstractwidget.y + k)
                {
                    return abstractwidget;
                }
            }
        }

        return null;
    }

    public static void drawCenteredString(PoseStack matrixStackIn, Font fontRendererIn, FormattedCharSequence textIn, int xIn, int yIn, int colorIn)
    {
        fontRendererIn.drawShadow(matrixStackIn, textIn, (float)(xIn - fontRendererIn.width(textIn) / 2), (float)yIn, colorIn);
    }
}
