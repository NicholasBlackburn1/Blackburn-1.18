package space.nickyblackburn.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class CopyRightScreen extends Screen{

    

    public CopyRightScreen(Component p_96550_) {
        super(new TextComponent("CopyRight by nicky blackburn"));
    }

    public void render(PoseStack p_96295_, int p_96296_, int p_96297_, float p_96298){
        this.renderBackground(p_96295_);
        drawCenteredString(p_96295_, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
        drawString(p_96295_, this.font, "OwO Daddy.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
    }
}
