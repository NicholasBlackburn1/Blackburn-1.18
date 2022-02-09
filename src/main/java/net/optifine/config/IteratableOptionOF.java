package net.optifine.config;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.optifine.gui.GuiOptionButtonOF;

public class IteratableOptionOF extends Option
{
    public IteratableOptionOF(String nameIn)
    {
        super(nameIn);
    }

    public AbstractWidget createButton(Options options, int xIn, int yIn, int widthIn)
    {
        return new GuiOptionButtonOF(xIn, yIn, widthIn, 20, this, this.getOptionText(options), (btn) ->
        {
            this.onPress(btn, options);
        });
    }

    public void onPress(Button button, Options options)
    {
        options.setOptionValueOF(this, 1);
        button.setMessage(options.getKeyComponentOF(this));
        options.save();
    }

    public Component getOptionText(Options options)
    {
        return options.getKeyComponentOF(this);
    }
}
