/***
 * TODO: get the main menu screen to randomly choose a pick out of a list of picturs and the locations of the buttons for that pic
 * and also get it to work correctly on non auto gui layout
 */


package space.nickyblackburn.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import space.nickyblackburn.utils.*;

import com.google.common.util.concurrent.Runnables;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import net.minecraft.SharedConstants;
import net.minecraft.Util;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.api.distmarker.Dist;


import  net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;




public class TitleScreenOverlay {



   private String output = "";
   // Allows me to  pull custom random furry background on my client 
   public String setBackgroundScreen(){
      

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      
      // runs when time is lewdy
      if(calendar.get(Calendar.HOUR_OF_DAY) == 0 || calendar.get(Calendar.HOUR_OF_DAY) < 5){
       Consts.dbg("Showing lewdy Images...");
      }

      // only runs when  time is not lewdy
      if(calendar.get(Calendar.HOUR_OF_DAY) != 0 || calendar.get(Calendar.HOUR_OF_DAY) > 5){
         Consts.dbg("Showing Non Lewdy Images...");
         
              

         Consts.log("Finished registering default main menu here is list entry"+ Consts.background.toString());
         
         // set's baground only when arrray is populated 
         if(Consts.background.size() == 0){
            Consts.log("Array is empty its first run");
         } else{
            output = Consts.background.get(1).toString().replaceAll("^\"+|\"+$", "");
            Consts.log("Set background to"+" "+ output);
         }
         
         
      } else{
         output = "blackburn/background/1.png";
      }
      return output;
      
   }

   // Allows me to init my classes in the tile screen
   public void BlackburnTitleInit(){
      GuiUtils gui = new GuiUtils();
      gui.dumpLayoutJson();
         
   }

   public void setCopyRightTexT(String splash, Minecraft minecraft, int copyrightWidth, int copyrightX, int width,Font font,Screen screen){
       
      

        if (splash == null) {
            splash = minecraft.getSplashManager().getSplash();
         }
   
         copyrightWidth = font.width(Consts.copyright);
         copyrightX = width - copyrightWidth - 2;
         
        
      
    }

    // Draws copy right info 
    public void drawCopyRightString(Screen screen, PoseStack pose,Font font, String copyrightinfo, int height, int width, int copyrightX, int copyrightWidth, int p_96740_, int p_96741_ , float f1){
      int l = Mth.ceil(f1 * 255.0F) << 24;

      screen.drawString(pose, font, copyrightinfo, copyrightX, height - 10, 16777215 | l);

      if (p_96740_ > copyrightX && p_96740_ < copyrightX + copyrightWidth && p_96741_ > height - 10 && p_96741_ < height) {
         screen.fill(pose, copyrightX, height - 1, copyrightX + copyrightWidth, height, 16777215 | l);
      }
    }


    // sets version info on main menu to blackburn + version name 
    public void setDrawVersionName(Minecraft minecraft,Screen screen, PoseStack pose,Font font, int height, int l ){
      Consts.showStart= true;
      
      String s = Consts.ReleaseName + SharedConstants.getCurrentVersion().getName();
      if (minecraft.isDemo()) {
         s = s + " Demo";
      } else {
         s = s + ("release".equalsIgnoreCase(minecraft.getVersionType()) ? "" : "/" + minecraft.getVersionType());
      }

      if (Minecraft.checkModStatus().shouldReportAsModified()) {
         s = s + I18n.a("menu.modded");
      }
      screen.drawString(pose, font, s, 2, height - 10, 16777215 | l);
      
    }

    // Sets the splash screen pos 
    public void setSplashPos(Screen screen ,String Splash, PoseStack p_96739_, int width, int splashX, int splashRot, Font font, int l){
      if (Splash != null) {
         p_96739_.pushPose();
         p_96739_.translate((double)(splashX), 70.0F, 1.0F);
         p_96739_.mulPose(Vector3f.ZP.rotationDegrees(splashRot));
         float f2 = 1.8F - Mth.abs(Mth.sin((float)(Util.getMillis() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
         f2 = f2 * 100.0F / (float)(font.width(Splash) + 32);
         p_96739_.scale(f2, f2, f2);
         screen.drawCenteredString(p_96739_, font, Splash, 0, -8, 16776960 | l);
         p_96739_.popPose();
      }

    }

   // this allows me to make custom minecraft loading screens  by just calling this function
   // int j is hight i think
    public  void setUpCustomMainMenu(Minecraft minecraft, Screen screen,  int width, int hight, int j, Screen realmsNotificationsScreen){

      Consts.dbg("Setting up custom title screen~");

      // Single player selection
      screen.addRenderableWidget(new Button(width / 2  + 100, j+72 -100, 100, 20, new TranslatableComponent("menu.singleplayer"), (p_96781_) -> {
         minecraft.setScreen(new SelectWorldScreen(screen));

      }));
      Consts.warn("the pos of the  button "+"menu.singleplayer"+" X:"+ " "+Integer.toString(width / 2  + 100)+" "+ " Y:"+Integer.toString(j+72 -100));
      Consts.dbg("Regestering single player menu");
      
      // adds Multipayer selection
      screen.addRenderableWidget(new Button(width / 2 + 100, j + 72 - 74 , 100, 20, new TranslatableComponent("menu.multiplayer"), (p_169450_) -> {
      minecraft.setScreen(new JoinMultiplayerScreen(screen));
      }));
      Consts.dbg("Regestering mutli player menu");

      // lang selection
      screen.addRenderableWidget(new ImageButton(width / 2 + 100, j + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (p_96791_) -> {
         minecraft.setScreen(new LanguageSelectScreen(screen, minecraft.options, minecraft.getLanguageManager()));
      }, new TranslatableComponent("narrator.button.language")));
      Consts.dbg("Regestering lang select menu");

      // Settings screen 
      screen.addRenderableWidget(new Button(width  / 2  + 100, j+72 - 46, 100, 20, new TranslatableComponent("menu.options"), (p_96788_) -> {
         minecraft.setScreen(new OptionsScreen(screen, minecraft.options));
      }));
      Consts.dbg("Regestering Settings Menu select menu");

      // quit button 
      screen.addRenderableWidget(new Button(width /2 + 100, j + 72 - 20, 100, 20, new TranslatableComponent("menu.quit"), (p_96786_) -> {
         minecraft.stop();
      }));
      Consts.dbg("Regestering quit");
     
   
      minecraft.setConnectedToRealms(false);

      if (minecraft.options.realmsNotifications && realmsNotificationsScreen == null) {
         realmsNotificationsScreen = new RealmsNotificationsScreen();
      }

   }

   /**
    * This is for redering the splash title and editon ifno
    * @param screen
    * @param splash
    * @param p_96739_
    * @param font
    * @param width
    * @param editionX
    * @param editionY
    * @param splashX
    * @param j
    * @param l
    * @param edtionImageWidth the edition image width 
    * @param editionImage the edtion image
    * TODO: need to add Boolean for full screen or not  to set edition to the right hight 
    */
   public void renderEdition(Screen screen,String splash, PoseStack p_96739_, Font font, int width, int hight, int editionXfull,int editionYfull, int splashX, int j,int l, int editionXSmol, int editionYSmol, int editionImageWidth, int editionImageHight, int editionTextureWidth, int splashrot, boolean isfullscreen){
      int x = 0;
      int y =0;
     
      int spshx = 0;
      int editionhight = 0;

      // sets it so it can detect if its full sdcreen
      if(isfullscreen){
         x = CalculateX(editionXfull,width);
         y = CalculateY(editionYfull, j);
         
         
      } else{
         x = CalculateX(editionXSmol, width);
         y = CalculateY(editionYSmol, j);
   ;
     
      }



     

     if(splashX < 0){
      spshx =  width / 2 -  Math.abs(splashX);
     }else{
      spshx =width / 2 + splashX;
     }
     
     if(editionImageHight < 0){
        editionhight = hight / 2 - Math.abs(editionImageHight);
     } else{
        editionhight = hight / 2 + editionImageHight;
     }

      
      screen.blit(p_96739_,x, y, 0.0F, 0.0F, editionTextureWidth, 20, editionImageWidth,editionImageHight);
      if (splash != null) {
        setSplashPos(screen, splash, p_96739_, width, spshx,splashrot, font, l);
      }

   }

   // This allows me to load and displays all the buttons and the logo ans the splashcreen all from the file 
   public void LoadCustomMainMenu(Minecraft minecraft, Screen titlescreen,int buttonwidth,int j){
      GuiUtils gui = new GuiUtils();   
      JsonPrimitive x,y ;
     

      Consts.log("array for buttons is "+ Consts.background.toString());

      // Suppost to be at X: 313  Y:80
      // not this X: 113  Y:280
      x = (JsonPrimitive) Consts.background.get(4);
      y = ( JsonPrimitive) Consts.background.get(3);
      
      gui.CreatebuttonwithoutImage(minecraft, titlescreen, new SelectWorldScreen(titlescreen), buttonwidth, j, x.getAsInt(), y.getAsInt(), 20, "menu.singleplayer");
     
      x = (JsonPrimitive) Consts.background.get(6);
      y = (JsonPrimitive) Consts.background.get(5);
      gui.CreatebuttonwithoutImage(minecraft, titlescreen, new JoinMultiplayerScreen(titlescreen), buttonwidth, j, x.getAsInt(), y.getAsInt(), 20, "menu.multiplayer");
      
      x = (JsonPrimitive) Consts.background.get(8);
      y = (JsonPrimitive) Consts.background.get(7);
      gui.CreatebuttonwithoutImage(minecraft, titlescreen, new OptionsScreen(titlescreen, minecraft.options), buttonwidth, j, x.getAsInt(), y.getAsInt(), 20, "menu.options");
      
      x = (JsonPrimitive) Consts.background.get(10);
      y = (JsonPrimitive) Consts.background.get(9);
      gui.CreatebuttonwithoutImageQuit(minecraft, titlescreen, buttonwidth, j, x.getAsInt(), y.getAsInt(), 20, "menu.quit");
   }

   // does maths for the x an dy pos 
   private int CalculateX(int input,int width){
      int x = 0 ;
      if(input < 0){
         x =  width / 2 -  Math.abs(input);
        }else{
         x =width / 2 + input;
         
        }
      return x;
   }  

   private int CalculateY(int input, int j){
         int y = 0;
         // allows me to detect when a number in my config is - and turns it from neg to postive and subtracts it 
      if(input < 0){
         y = j - Math.abs(input);
      } else{
         y = j + input;
      }
      return y;
   }
  
   
}