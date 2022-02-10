/**
 * This is the settings screen and simple button creation for the menu and the options screen
 * TODO: GET TWITCH IRC TO WORK AND VALIDATE USERNAMES
 */
package space.nickyblackburn.screens;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
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
import space.nickyblackburn.utils.Consts;

public class TwitchOptionsScreen extends GuiScreenOF{

    private String Email;

    private List <String> options;

    private EditBox username;
    private EditBox password;

    private Button connect;
    private Button exit;

    private Screen last;

    private PoseStack poseStack;

   
    public TwitchOptionsScreen(Component component,Screen lastScreen){
        super(new TextComponent("Twitch settings screen"));
        this.last = lastScreen;

        init();
    }
    // intis the screen
    public void init(){
        Consts.twitchlog.add("Twitch irc: Starting... ");

        int l = this.height / 6 + 21 * 5 / 2 - 12;

        this.minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);

         // editboxes for input
         this.username =  new EditBox(this.font, this.width / 2 - 210, l+ -5, 150, 20, new TranslatableComponent("blackburn.twitch.username"));
         this.password = new EditBox(this.font, this.width / 2 - 210, l+ 30, 150, 20, new TranslatableComponent("blackburn.twitch.password"));

         // gui buttons
         this.connect = new Button(this.width / 2 -0 , l+ 60,150, 20, new TranslatableComponent("blakcburn.twitch.connect"), (p_95981_) ->
         {  
             // sets the global var as the password and user to log into twithc 
             Consts.TwitchPass = this.password.getValue();
             Consts.TwitchUsername = this.username.getValue();
         });

         // this returns to the previuse screen
         this.exit = new Button(this.width / 2 -0 , l+ 90,150, 20, new TranslatableComponent("blakcburn.twitch.exit"), (p_95981_) ->
         {
             this.minecraft.setScreen(this.last);
         });
         
        

         // username edit boxes
         this.username.setMaxLength(128);
         this.username.setFocus(true);
         this.username.setValue("please enter username");
         this.username.setResponder((p_95983_) ->
         {
            
         });
 

         // password edit boxes
         this.password.setMaxLength(128);
         this.password.setFocus(true);
         this.password.setValue("please enter password");
         this.password.setResponder((p_95983_) ->
         {
            
         });

        
         
 
 
    }
    int count = 0 ;
    // renders all the gui elements
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {   
        this.poseStack = pPoseStack;

        int l = this.height / 6 + 21 * 5 / 2 - 12;
        this.renderBackground(pPoseStack);
        
        String s2 = "Minecraft 1.18.1";
        int i = this.minecraft.font.width(s2);
        // draws the title and the stuff
        drawCenteredString(pPoseStack, this.minecraft.font, this.title, this.width / 2, 15, 16777215);
        drawString(pPoseStack, this.minecraft.font, s2, this.width - i - 2, this.height - 10, 8421504);
        
       
        buttonLayout(pPoseStack);

        drawCenteredString(pPoseStack, this.minecraft.font, "the username entered is "+Consts.TwitchUsername, this.width / 2 + 20, 15+20, 16777215);
        drawCenteredString(pPoseStack, this.minecraft.font, "the password entered is "+Consts.TwitchPass, this.width / 2 + 20, 15+30, 16777215);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
      
    }

    public void tick()
    {
        this.password.tick();
        this.username.tick();
    }


    // allows me to set up custom buttons in the menu
    private void buttonLayout(PoseStack pPoseStack){
        int l = this.height / 6 + 21 * 5 / 2 - 12;
    

        // this is the entry box for the componet email 
    
        this.drawString(pPoseStack, this.minecraft.font, I18n.a("blackburn.twitch.email"),this.width / 2 - 210, l+ -15, 16777215);
        this.addRenderableWidget(this.username);
        

        // entry box for password
        this.drawString(pPoseStack, this.minecraft.font, I18n.a("blackburn.twitch.password"),this.width / 2 - 210, l+ 20, 16777215);
        this.addRenderableWidget(this.password);


        this.addRenderableWidget(this.connect);
        this.addRenderableWidget(this.exit);
        
       
    }   

    

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
    {
        if (!this.connect.active || this.getFocused() != this.password || pKeyCode != 257 && pKeyCode != 335)
        {
          
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
        else
        {
            this.onSelect();
            return true;
        }
    }
    
    private void onSelect()
    {
        Consts.error("got data from usrrname : "+this.username.getValue()+ " "+ "this is the password "+ this.password.getValue());
        
        count++;
}

    
}
