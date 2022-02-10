/**
 * This is the settings screen and simple button creation for the menu and the options screen
 * 
 */
package space.nickyblackburn.screens;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.optifine.Lang;
import net.optifine.gui.GuiScreenButtonOF;
import net.optifine.gui.GuiScreenOF;
import net.optifine.util.GuiRect;

public class TwitchOptionsScreen extends GuiScreenOF{

    private String Email;
    private String password;
    private List <String> options;

   
    public TwitchOptionsScreen(Component component){
        super(new TextComponent("Twitch settings screen"));
        init();
    }

    // intis the screen
    public void init(){
        options = new ArrayList();

        options.add("exit");
    }

    
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);
        
        String s2 = "Minecraft 1.18.1";
        int i = this.minecraft.font.width(s2);
        // draws the title and the stuff
        drawCenteredString(pPoseStack, this.minecraft.font, this.title, this.width / 2, 15, 16777215);
        drawString(pPoseStack, this.minecraft.font, s2, this.width - i - 2, this.height - 10, 8421504);

        buttonLayout(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
      
    }

    // allows me to set up custom buttons in the menu
    private void buttonLayout(PoseStack pPoseStack){
        int l = this.height / 6 + 21 * 5 / 2 - 12;
        
        // this is the entry box for the componet email 
        /**
        this.drawString(pPoseStack, this.minecraft.font, I18n.a("blackburn.twitch.email"),this.width / 2 - 100, l+ 15, 16777215);
        this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 100, l+ 05 , 200, 20, new TranslatableComponent("blackburn.twitch.email")));
        */

        // entry box for password
        this.drawString(pPoseStack, this.minecraft.font, I18n.a("blackburn.twitch.password"),this.width / 2 - 208, l+ 20, 16777215);
        this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 208, l+ 30, 200, 20, new TranslatableComponent("blackburn.twitch.password")));

        // cbuttons
        this.addRenderableWidget(new GuiScreenButtonOF(304, 0 , l+ 60, Lang.get("blackburn.twitch.connect")));
        this.addRenderableWidget(new GuiScreenButtonOF(305, 0, l+90, Lang.get("blackburn.twitch.exit")));
       
    }   



    
}
