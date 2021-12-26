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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.annotation.Nullable;
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


   public void setCopyRightTexT(String splash, Minecraft minecraft, int copyrightWidth, int copyrightX, int width,Font font,Screen screen){
        if (splash == null) {
            splash = minecraft.getSplashManager().getSplash();
         }
   
         copyrightWidth = font.width(Consts.copyright);
         copyrightX = width - copyrightWidth - 2;
      
    }


   // this allows me to make custom minecraft loading screens  by just calling this function
    public  void setUpCustomMainMenu(Minecraft minecraft, Screen screen,  int width, int hight, int j, Screen realmsNotificationsScreen){
      
      // lang selection
      screen.addRenderableWidget(new ImageButton(width / 2 - 124, j + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (p_96791_) -> {
         minecraft.setScreen(new LanguageSelectScreen(screen, minecraft.options, minecraft.getLanguageManager()));
      }, new TranslatableComponent("narrator.button.language")));

      // Settings screen 
      screen.addRenderableWidget(new Button(width  / 2 - 200, j + 72+ 25, 100, 20, new TranslatableComponent("menu.options"), (p_96788_) -> {
         minecraft.setScreen(new OptionsScreen(screen, minecraft.options));
      }));

      // quit button 
      screen.addRenderableWidget(new Button(width /2 - 200, j + 72+ 25, 100, 20, new TranslatableComponent("menu.quit"), (p_96786_) -> {
         minecraft.stop();
      }));

   
      minecraft.setConnectedToRealms(false);

      if (minecraft.options.realmsNotifications && realmsNotificationsScreen == null) {
         realmsNotificationsScreen = new RealmsNotificationsScreen();
      }

   }
  
   
}
