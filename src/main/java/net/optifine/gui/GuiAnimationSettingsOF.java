package net.optifine.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.optifine.Lang;

public class GuiAnimationSettingsOF extends GuiScreenOF
{
    private Screen prevScreen;
    private Options settings;
    private static Option[] enumOptions = new Option[] {Option.ANIMATED_WATER, Option.ANIMATED_LAVA, Option.ANIMATED_FIRE, Option.ANIMATED_PORTAL, Option.ANIMATED_REDSTONE, Option.ANIMATED_EXPLOSION, Option.ANIMATED_FLAME, Option.ANIMATED_SMOKE, Option.VOID_PARTICLES, Option.WATER_PARTICLES, Option.RAIN_SPLASH, Option.PORTAL_PARTICLES, Option.POTION_PARTICLES, Option.DRIPPING_WATER_LAVA, Option.ANIMATED_TERRAIN, Option.ANIMATED_TEXTURES, Option.FIREWORK_PARTICLES, Option.PARTICLES};

    public GuiAnimationSettingsOF(Screen guiscreen, Options gamesettings)
    {
        super(new TextComponent(I18n.a("of.options.animationsTitle")));
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public void init()
    {
        this.clearWidgets();

        for (int i = 0; i < enumOptions.length; ++i)
        {
            Option option = enumOptions[i];
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 + 21 * (i / 2) - 12;
            AbstractWidget abstractwidget = this.addRenderableWidget(option.createButton(this.minecraft.options, j, k, 150));
        }

        this.addRenderableWidget(new GuiButtonOF(210, this.width / 2 - 155, this.height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOn")));
        this.addRenderableWidget(new GuiButtonOF(211, this.width / 2 - 155 + 80, this.height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOff")));
        this.addRenderableWidget(new GuiScreenButtonOF(200, this.width / 2 + 5, this.height / 6 + 168 + 11, I18n.a("gui.done")));
    }

    protected void actionPerformed(AbstractWidget guiElement)
    {
        if (guiElement instanceof GuiButtonOF)
        {
            GuiButtonOF guibuttonof = (GuiButtonOF)guiElement;

            if (guibuttonof.active)
            {
                if (guibuttonof.id == 200)
                {
                    this.minecraft.options.save();
                    this.minecraft.setScreen(this.prevScreen);
                }

                if (guibuttonof.id == 210)
                {
                    this.minecraft.options.setAllAnimations(true);
                }

                if (guibuttonof.id == 211)
                {
                    this.minecraft.options.setAllAnimations(false);
                }

                this.minecraft.resizeDisplay();
            }
        }
    }

    public void removed()
    {
        this.minecraft.options.save();
        super.removed();
    }

    public void render(PoseStack matrixStackIn, int x, int y, float partialTicks)
    {
        this.renderBackground(matrixStackIn);
        drawCenteredString(matrixStackIn, this.minecraft.font, this.title, this.width / 2, 15, 16777215);
        super.render(matrixStackIn, x, y, partialTicks);
    }
}
