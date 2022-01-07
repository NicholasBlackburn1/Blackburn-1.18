package net.minecraft.client.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import space.nickyblackburn.screens.DeathScreenOverlay;
import space.nickyblackburn.utils.Consts;

@OnlyIn(Dist.CLIENT)
public class DeathScreen extends Screen {
   private int delayTicker;
   private final Component causeOfDeath;
   private final boolean hardcore;
   private Component deathScore;
   private String splash;
   
   private Component sufix;

   private final List<Button> exitButtons = Lists.newArrayList();

   public DeathScreen(@Nullable Component p_95911_, boolean p_95912_) {
      super(new TranslatableComponent(p_95912_ ? "deathScreen.title.hardcore" : "deathScreen.title"));
      this.causeOfDeath = p_95911_;
      this.hardcore = p_95912_;
   }

   protected void init() {
      this.splash = Consts.minecraft.getSplashManager().getSplash();

      this.delayTicker = 0;
      this.exitButtons.clear();
      this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.hardcore ? new TranslatableComponent("deathScreen.spectate") : new TranslatableComponent("deathScreen.respawn"), (p_95930_) -> {
         this.minecraft.player.respawn();
         this.minecraft.setScreen((Screen)null);
      })));
      this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 96, 200, 20, new TranslatableComponent("deathScreen.titleScreen"), (p_95925_) -> {
         if (this.hardcore) {
            this.exitToTitleScreen();
         } else {
            ConfirmScreen confirmscreen = new ConfirmScreen(this::confirmResult, new TranslatableComponent("deathScreen.quit.confirm"), TextComponent.EMPTY, new TranslatableComponent("deathScreen.titleScreen"), new TranslatableComponent("deathScreen.respawn"));
            this.minecraft.setScreen(confirmscreen);
            confirmscreen.setDelay(20);
         }
      })));

      for(Button button : this.exitButtons) {
         button.active = false;
      }

      this.deathScore = (new TranslatableComponent("deathScreen.score")).append(": ").append((new TextComponent(Integer.toString(this.minecraft.player.getScore()))).withStyle(ChatFormatting.YELLOW));
      this.sufix = new TranslatableComponent("blackburn.deathscreen.more");
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   private void confirmResult(boolean p_95932_) {
      if (p_95932_) {
         this.exitToTitleScreen();
      } else {
         this.minecraft.player.respawn();
         this.minecraft.setScreen((Screen)null);
      }

   }

   private void exitToTitleScreen() {
      if (this.minecraft.level != null) {
         this.minecraft.level.disconnect();
      }

      this.minecraft.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
      this.minecraft.setScreen(new TitleScreen());
   }

   
   public void render(PoseStack p_95920_, int p_95921_, int p_95922_, float p_95923_) {
      // Added my Own overlay to the death sccreen
     DeathScreenOverlay overlay = new DeathScreenOverlay();

      overlay.renderDeathScreen(p_95920_, this, title, font, this.causeOfDeath,this.sufix,this.splash,width, height, p_95921_, p_95922_);
      
      if (causeOfDeath != null && p_95922_ > 85 && p_95922_ < 85 + 9) {
         Style style3 = this.getClickedComponentStyleAt(p_95921_);
         this.renderComponentHoverEffect(p_95920_, style3, p_95921_, p_95922_);
         
      }
      super.render(p_95920_, p_95921_, p_95922_, p_95923_);
   }

   @Nullable
   public Style getClickedComponentStyleAt(int p_95918_) {
      if (this.causeOfDeath == null) {
         return null;
      } else {
         int i = this.minecraft.font.width(this.causeOfDeath);
         int j = this.width / 2 - i / 2;
         int k = this.width / 2 + i / 2;
         return p_95918_ >= j && p_95918_ <= k ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, p_95918_ - j) : null;
      }
   }

   public boolean mouseClicked(double p_95914_, double p_95915_, int p_95916_) {
      if (this.causeOfDeath != null && p_95915_ > 85.0D && p_95915_ < (double)(85 + 9)) {
         Style style = this.getClickedComponentStyleAt((int)p_95914_);
         if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
            this.handleComponentClicked(style);
            return false;
         }
      }

      return super.mouseClicked(p_95914_, p_95915_, p_95916_);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void tick() {
      super.tick();
      ++this.delayTicker;
      if (this.delayTicker == 20) {
         for(Button button : this.exitButtons) {
            button.active = true;
         }
      }

   }
}