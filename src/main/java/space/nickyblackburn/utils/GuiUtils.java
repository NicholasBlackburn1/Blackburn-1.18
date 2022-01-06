package space.nickyblackburn.utils;

/**
 * this is a class to store most of my gui stuff buttons and etc functiosn 
 * JSON STRING FOR MENU {'id':id,'image':str(basedir)+str(id)+str(fileextention),'sp_posX': addToFile(id,"sp_posX"), 'sp_posY':addToFile(id,"sp_posY"),'mp_posX':addToFile(id,"mp_posX"),'mp_posY':addToFile(id,"mp_posY"),'settings_posX':addToFile(id,"settings_posX"),
        'settings_posY':addToFile(id,"settings_posY"), 'quit_posX':addToFile(id,"quit_posX"), 'quit_posY':addToFile(id,"quit_posY"),'splash_posX':addToFile(id,"splash_posX"), 
        'splash_posY':addToFile(id,"splash_posY"),'edition_posX':addToFile(id,"edition_posX"),'edition_posY':addToFile(id,"edition_posY"),
        'lang_posX':addToFile(id,"lang_posX"), 'lang_posY':addToFile(id,"lang_posY")}
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import space.nickyblackburn.utils.*;

import com.google.common.util.concurrent.Runnables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.kinds.Const;
import com.mojang.math.Vector3f;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
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

public class GuiUtils {
    

 /**
  * No image button
  * @param minecraft main minecraft instance
  * @param titlescreen this main title screen
  * @param onClickedScreen the screen the button will open 
  * @param ButtonWidth the width of the button
  * @param j some magic number mojan made 
  * @param ButtonLocationY Is the y axis of where the button sits on the screen 
  * @param ButtonLocationX is the x axis of where the button sitis on the screen
  * @param ButtonLength the length of the button  set to 100 for short buttons 
  * @param buttonText the Translatable text from a lang.json file
  * @param texturehight the place where if you have to hight it shows other mapped textures to the buttons set to 20 unless crazy wide button
  */
   public void CreatebuttonwithoutImage(Minecraft minecraft,Screen titlescreen, Screen onClickedScreen, int ButtonWidth, int j, int ButtonLocationY, int ButtonLocationX, int ButtonLenght,  String buttonText){
      int width = 0;
      int y = 0;

    Consts.dbg("Setting up "+buttonText+ "......");
    
    Consts.dbg("abs of ButtonLocationx" + Integer.toString(Math.abs(ButtonLocationX)));
    Consts.dbg("abs of ButtonLocationy" + Integer.toString(Math.abs(ButtonLocationY)));
    
   
   
    if(ButtonLocationX < 0 ){
     width =  ButtonWidth / 2 -  Math.abs(ButtonLocationX);
    }else{
     width = ButtonWidth / 2 + ButtonLocationX;
    }

    if(ButtonLocationY < 0){
       y = j+72 - Math.abs(ButtonLocationY);
    } else{
       y = j+72 + ButtonLocationY;
    }


    Consts.warn("the pos of the  button "+(buttonText)+" X:"+ " "+Integer.toString(width)+" "+ " Y:"+Integer.toString(y));
    titlescreen.addRenderableWidget(new Button(width, y, 100, 20, new TranslatableComponent(buttonText), (p_96781_) -> {
       minecraft.setScreen(onClickedScreen);
    }));
    Consts.dbg("Set up "+new TranslatableComponent(buttonText).toString()+"Sucessfully");
 }

 public void CreatebuttonwithoutImageQuit(Minecraft minecraft,Screen titlescreen, int ButtonWidth, int j, int ButtonLocationY, int ButtonLocationX, int ButtonLenght,  String buttonText){


   int width = 0;
   int y = 0;

 Consts.dbg("Setting up "+buttonText+ "......");
 
 Consts.dbg("abs of ButtonLocationx" + Integer.toString(Math.abs(ButtonLocationX)));
 Consts.dbg("abs of ButtonLocationy" + Integer.toString(Math.abs(ButtonLocationY)));
 
// convirts negive numbers into actual lfull numebrs just uses negitive as detector 
 if(ButtonLocationX < 0 ){
  width =  ButtonWidth / 2 -  Math.abs(ButtonLocationX);
 }else{
  width = ButtonWidth / 2 + ButtonLocationX;
 }

 if(ButtonLocationY < 0){
    y = j+72 - Math.abs(ButtonLocationY);
 } else{
    y = j+72 + ButtonLocationY;
 }


 Consts.warn("the pos of the  button "+(buttonText)+" X:"+ " "+Integer.toString(width)+" "+ " Y:"+Integer.toString(y));
   Consts.dbg("Setting up "+new TranslatableComponent(buttonText).toString()+ "......");
   titlescreen.addRenderableWidget(new Button(width, y, 100, 20, new TranslatableComponent(buttonText), (p_96781_) -> {
      minecraft.stop();
   }));
   Consts.dbg("Set up "+new TranslatableComponent(buttonText).toString()+"Sucessfully");
}




/**
* Image button
* @param minecraft main minecraft instance
* @param titlescreen this main title screen
* @param onClickedScreen the screen the button will open 
* @param ButtonWidth the width of the button
* @param j some magic number mojan made 
* @param ButtonLocationY Is the y axis of where the button sits on the screen 
* @param ButtonLocationX is the x axis of where the button sitis on the screen
* @param ButtonLength the length of the button  set to 100 for short buttons 
* @param buttonText the Translatable text from a lang.json file
* @param texturehight the place where if you have to hight it shows other mapped textures to the buttons set to 20 unless crazy wide button
*/
 public void createButtonWithImage(Minecraft minecraft,Screen titlescreen, Screen onClickedScreen, int ButtonWidth, int j, int ButtonLocationY, int ButtonLocationX, int ButtonLenght,String buttonText,ResourceLocation buttonImage){
    Consts.dbg("Registering Menu item"+new TranslatableComponent(buttonText).toString()+ "....");

    titlescreen.addRenderableWidget(new ImageButton(ButtonWidth / 2 - ButtonLocationX,j+ 72 + ButtonLocationY, ButtonLenght, 20, 0, 106, 20, buttonImage, 256, 256, (p_96791_) -> {
       minecraft.setScreen(onClickedScreen);
    }, new TranslatableComponent(buttonText)))
    ;
    Consts.dbg("Regestered Menu item"+new TranslatableComponent(buttonText).toString()+ " ");
 }


 /***
  * This Allows me to easly check to see if program is in dev mode
  * @param random random java 
  * @param background background int 
  * @param backgroundCount the size of the background list 
  * @return
  */
 public static int getBackgroundnum(Random random,int background, int backgroundCount){
    int output = 0;
   if(Consts.devMode){
      output = background;
   }
   else{
      output = random.nextInt(backgroundCount);
   }
   return output;
 }


 // loads data from json fiels 
 public static void loadFromJson(InputStream p_128109_) {
   Gson json = new Gson();
   Random random = new Random();

   JsonArray jsonobject = json.fromJson(new InputStreamReader(p_128109_, StandardCharsets.UTF_8), JsonArray.class);
   int i  = 0;

   for(Entry<String, JsonElement> entry : jsonobject.get(getBackgroundnum(random,0,26)).getAsJsonObject().entrySet()) {
      Consts.debug(entry.getKey().toString());
      Consts.keys.add(entry.getKey().toString());
      Consts.background.add(entry.getValue());

      Consts.log("Linked list for keys"+ " "+ entry.getKey().toString()+ " "+ entry.getValue().toString());
      Consts.log("Linked List for data"+ " "+ Consts.background.toString());
      
   }

}

 // Dumps json so i cam then hope fully get daya from it 
 public void dumpLayoutJson(){
   try{
   InputStream inputstream = GuiUtils.class.getResourceAsStream("/assets/minecraft/blackburn/backgrounds.json");

   try {
      loadFromJson(inputstream);

   } catch (Throwable throwable1) {

      if (inputstream != null) {
         try {
            inputstream.close();
         } catch (Throwable throwable) {
            throwable1.addSuppressed(throwable);
         }
      }

      throw throwable1;
   }

   if (inputstream != null) {
      inputstream.close();
   }
} catch (JsonParseException | IOException ioexception) {
   Consts.error("Couldn't read strings from {}"+"/assets/blackburn/backgrounds.json" +" "+ioexception.toString());
}
 }
}
