package space.nickyblackburn.utils;

/**
 * this is a class to store most of my gui stuff buttons and etc functiosn 
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

    Consts.dbg("Setting up "+new TranslatableComponent(buttonText).toString()+ "......");
    titlescreen.addRenderableWidget(new Button(ButtonWidth / 2 - ButtonLocationX, j+72 - ButtonLocationY, ButtonLenght, 20, new TranslatableComponent(buttonText), (p_96781_) -> {
       minecraft.setScreen(onClickedScreen);
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

}
