package space.nickyblackburn.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import space.nickyblackburn.utils.Consts;

public class DeathScreenOverlay {
    
    // this renders custom death screen
    public void renderDeathScreen(PoseStack p_95920_, Screen screen, Component title, Font font, Component causeOfDeath, Component sufix, int width, int height){

        Consts.rich.CustomPresenceWithImage("U died....", "sad owo noises", "screem");
        
        screen.fillGradient(p_95920_, 0, 0, width, height, 1615855616, -1602211792);
        p_95920_.pushPose();
        p_95920_.scale(2.0F, 2.0F, 2.0F);
            
        screen.drawCenteredString(p_95920_, font, title, width / 2 / 2, 30, 16777215);
  
        p_95920_.popPose();
            
        if (causeOfDeath != null) {
            screen.drawCenteredString(p_95920_,font, sufix, width / 2, 85, 16777215);
            screen.drawCenteredString(p_95920_,font, causeOfDeath, width / 2, 100, 16777215);
  
        }

        
  
        //screen.drawCenteredString(p_95920_, font,  "§e§b"+Splash, width / 2, 100, 16777215);
  
    }
    
    }
