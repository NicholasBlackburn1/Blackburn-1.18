package net.minecraft.client.gui.components;

import net.minecraft.client.Options;
import net.minecraft.network.chat.TextComponent;

public abstract class AbstractOptionSliderButton extends AbstractSliderButton
{
    protected final Options options;

    protected AbstractOptionSliderButton(Options pOptions, int pX, int pY, int pWidth, int pHeight, double pValue)
    {
        super(pX, pY, pWidth, pHeight, TextComponent.EMPTY, pValue);
        this.options = pOptions;
    }
}
