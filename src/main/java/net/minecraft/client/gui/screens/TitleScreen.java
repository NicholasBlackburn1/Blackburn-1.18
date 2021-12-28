package net.minecraft.client.gui.screens;

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
import net.minecraft.client.Minecraft;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import space.nickyblackburn.screens.TitleScreenOverlay;
import space.nickyblackburn.utils.Consts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class TitleScreen extends Screen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String DEMO_LEVEL_ID = "Demo_World";
   public static final String COPYRIGHT_TEXT = "Copyright Mojang AB. Do not distribute!";
   public static final CubeMap CUBE_MAP = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
   private static  ResourceLocation PANORAMA_OVERLAY = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
   private static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
   private final boolean minceraftEasterEgg;
   @Nullable
   private String splash;
   private Button resetDemoButton;
   private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
   private Screen realmsNotificationsScreen;
   private int copyrightWidth;
   private int copyrightX;
   private final PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
   private final boolean fading;
   private long fadeInStart;
   private int i = 0;
   public TitleScreen() {
      this(false);
   }

   public TitleScreen(boolean p_96733_) {
      super(new TranslatableComponent("narrator.screen.title"));
      this.fading = p_96733_;
      this.minceraftEasterEgg = (double)(new Random()).nextFloat() < 1.0E-4D;
   }

   private boolean realmsNotificationsEnabled() {
      return this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen != null;
   }

   public void tick() {
      if (this.realmsNotificationsEnabled()) {
         this.realmsNotificationsScreen.tick();
      }

   }

   public static CompletableFuture<Void> preloadResources(TextureManager p_96755_, Executor p_96756_) {
      return CompletableFuture.allOf(p_96755_.preload(MINECRAFT_LOGO, p_96756_), p_96755_.preload(MINECRAFT_EDITION, p_96756_), p_96755_.preload(PANORAMA_OVERLAY, p_96756_), CUBE_MAP.preload(p_96755_, p_96756_));
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   // Chnaged the copy right text out side of the main minecarft version
   protected void init() {
      i++;

      TitleScreenOverlay overlay = new TitleScreenOverlay();

      if (splash == null) {
         splash = minecraft.getSplashManager().getSplash();
      }

      // runs only on 2nd startup of main menu
      if(i == 1){
         overlay.BlackburnTitleInit();
      }

      this.PANORAMA_OVERLAY = new ResourceLocation(overlay.setBackgroundScreen());

      copyrightWidth = font.width(Consts.copyright);
      this.copyrightX = width - copyrightWidth - 2;
      
      int i = 24;
      int j = this.height / 4 + 48;
      
      // Creates my custom 
      if (this.minecraft.isDemo()) {

         overlay.setUpCustomMainMenu(minecraft, this, width, height, j, realmsNotificationsScreen);

      } else {

         if(Consts.background.size() == 0){
            Consts.warn("Cannot Register new Main menu  because list is 0");

         } else{
            Consts.log("Registering main menu");
            overlay.LoadCustomMainMenu(minecraft,this,width, j);
            //overlay.setUpCustomMainMenu(minecraft, this, width,height, j, realmsNotificationsScreen);
            Consts.log("Registered main menu");
         }
      }
      

   }

   private void realmsButtonClicked() {
      this.minecraft.setScreen(new RealmsMainScreen(this));
   }

   public void render(PoseStack p_96739_, int p_96740_, int p_96741_, float p_96742_) {
      TitleScreenOverlay overlay = new TitleScreenOverlay();


      if (this.fadeInStart == 0L && this.fading) {
         this.fadeInStart = Util.getMillis();
      }

      float f = this.fading ? (float)(Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
      this.panorama.render(p_96742_, Mth.clamp(f, 0.0F, 1.0F));
      int i = 274;
      int j = this.width / 2 - 137;
      int k = 30;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.fading ? (float)Mth.ceil(Mth.clamp(f, 0.0F, 1.0F)) : 1.0F);
      blit(p_96739_, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
      float f1 = this.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
      int l = Mth.ceil(f1 * 255.0F) << 24;
      if ((l & -67108864) != 0) {
         RenderSystem.setShader(GameRenderer::getPositionTexShader);

         // Renders adition
         RenderSystem.setShaderTexture(0, MINECRAFT_EDITION);
         overlay.renderEdition(this,splash, p_96739_, font, this.width / 2 - 130, 100, -120, j, l);

         // draws version string at the bottom
         overlay.setDrawVersionName(this.minecraft,this,p_96739_,this.font,this.height,l);

         // this sets the copyright text
         overlay.drawCopyRightString(this, p_96739_, this.font, Consts.copyright, this.height, this.width, this.copyrightX, this.copyrightWidth, p_96740_, p_96741_,f1);

         for(GuiEventListener guieventlistener : this.children()) {
            if (guieventlistener instanceof AbstractWidget) {
               ((AbstractWidget)guieventlistener).setAlpha(f1);
            }
         }

         super.render(p_96739_, p_96740_, p_96741_, p_96742_);
         if (this.realmsNotificationsEnabled() && f1 >= 1.0F) {
            this.realmsNotificationsScreen.render(p_96739_, p_96740_, p_96741_, p_96742_);
         }

      }
   }

   public boolean mouseClicked(double p_96735_, double p_96736_, int p_96737_) {
      if (super.mouseClicked(p_96735_, p_96736_, p_96737_)) {
         return true;
      } else if (this.realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(p_96735_, p_96736_, p_96737_)) {
         return true;
      } else {
         if (p_96735_ > (double)this.copyrightX && p_96735_ < (double)(this.copyrightX + this.copyrightWidth) && p_96736_ > (double)(this.height - 10) && p_96736_ < (double)this.height) {
            this.minecraft.setScreen(new WinScreen(false, Runnables.doNothing()));
         }

         return false;
      }
   }

   public void removed() {
      if (this.realmsNotificationsScreen != null) {
         this.realmsNotificationsScreen.removed();
      }

   }

   private void confirmDemo(boolean p_96778_) {
      if (p_96778_) {
         try {
            LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = this.minecraft.getLevelSource().createAccess("Demo_World");

            try {
               levelstoragesource$levelstorageaccess.deleteLevel();
            } catch (Throwable throwable1) {
               if (levelstoragesource$levelstorageaccess != null) {
                  try {
                     levelstoragesource$levelstorageaccess.close();
                  } catch (Throwable throwable) {
                     throwable1.addSuppressed(throwable);
                  }
               }

               throw throwable1;
            }

            if (levelstoragesource$levelstorageaccess != null) {
               levelstoragesource$levelstorageaccess.close();
            }
         } catch (IOException ioexception) {
            SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
            LOGGER.warn("Failed to delete demo world", (Throwable)ioexception);
         }
      }

      this.minecraft.setScreen(this);
   }
}