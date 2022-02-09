package space.nickyblackburn.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class PlayerExperance {
    

    public void allExperance(Minecraft mc, Boolean enable){

        if(enable){
            mc.player.setExperienceValues(90.0f, 1000, 1000);
            mc.gui.getChat().addMessage(new TextComponent(I18n.a("blackburn.commmand.xp")));
        }
    }
}
