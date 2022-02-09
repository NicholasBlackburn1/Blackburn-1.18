package net.minecraft.client.gui.components;

import java.util.List;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.ProgressOption;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.optifine.config.FloatOptions;
import net.optifine.gui.IOptionControl;

public class SliderButton extends AbstractOptionSliderButton implements TooltipAccessor, IOptionControl
{
    private final ProgressOption option;
    private final List<FormattedCharSequence> tooltip;
    private boolean supportAdjusting;
    private boolean adjusting;

    public SliderButton(Options pOptions, int pX, int pY, int pWidth, int pHeight, ProgressOption pProgressOption, List<FormattedCharSequence> pTooltip)
    {
        super(pOptions, pX, pY, pWidth, pHeight, (double)((float)pProgressOption.toPct(pProgressOption.get(pOptions))));
        this.option = pProgressOption;
        this.tooltip = pTooltip;
        this.updateMessage();
        this.supportAdjusting = FloatOptions.supportAdjusting(this.option);
        this.adjusting = false;
    }

    protected void applyValue()
    {
        if (!this.adjusting)
        {
            double d0 = this.option.get(this.options);
            double d1 = this.option.toValue(this.value);

            if (d1 != d0)
            {
                this.option.set(this.options, this.option.toValue(this.value));
                this.options.save();
            }
        }
    }

    protected void updateMessage()
    {
        if (this.adjusting)
        {
            double d0 = this.option.toValue(this.value);
            Component component = FloatOptions.getTextComponent(this.option, d0);

            if (component != null)
            {
                this.setMessage(component);
            }
        }
        else
        {
            this.setMessage(this.option.getMessage(this.options));
        }
    }

    public List<FormattedCharSequence> getTooltip()
    {
        return this.tooltip;
    }

    public void onClick(double mouseX, double mouseY)
    {
        if (this.supportAdjusting)
        {
            this.adjusting = true;
        }

        super.onClick(mouseX, mouseY);
    }

    protected void onDrag(double mouseX, double mouseY, double mouseDX, double mouseDY)
    {
        if (this.supportAdjusting)
        {
            this.adjusting = true;
        }

        super.onDrag(mouseX, mouseY, mouseDX, mouseDY);
    }

    public void onRelease(double mouseX, double mouseY)
    {
        if (this.adjusting)
        {
            this.adjusting = false;
            this.applyValue();
            this.updateMessage();
        }

        super.onRelease(mouseX, mouseY);
    }

    public static int getWidth(AbstractWidget btn)
    {
        return btn.width;
    }

    public static int getHeight(AbstractWidget btn)
    {
        return btn.height;
    }

    public Option getControlOption()
    {
        return this.option;
    }
}
