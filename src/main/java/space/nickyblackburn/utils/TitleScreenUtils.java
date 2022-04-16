package space.nickyblackburn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import space.nickyblackburn.screens.TitleScreenOverlay;

public class TitleScreenUtils {


/**
 * 
 * @param font the font 
 * @param splash the spash text 
 * @param minecraft main instance 
 * @param PANORAMA_OVERLAY the pictures for the backgoround 
 * @param i inex
 * @param copyrightX copyrigt x
 * @param copyrightWidth copythihg  y 
 * @param height hight  fpr screen
 * @param width withd 
 */
    public void titlescrenInit(Font font, String splash, Minecraft minecraft,ResourceLocation PANORAMA_OVERLAY, Screen screen, int i, int copyrightX, int copyrightWidth,int height, int width) {
        i++;
        Consts.showStart = true;
        
        TitleScreenOverlay overlay = new TitleScreenOverlay();
  
        if (splash == null) {
           splash = minecraft.getSplashManager().getSplash();
        }
  
        // runs only on 2nd startup of main menu
        if(i == 1){
           overlay.BlackburnTitleInit();
        }
      
        PANORAMA_OVERLAY = new ResourceLocation(overlay.setBackgroundScreen());
  
        copyrightWidth = font.width(Consts.copyright);
        copyrightX = width - copyrightWidth - 2;
  
         i = 24;
        int j = height / 4 + 48;
  
        // Creates my custom 
        if (minecraft.isDemo()) {
  
           if(Consts.background.size() == 0){
              Consts.warn("Cannot Register new Main menu  because list is 0");
  
           } else{
              Consts.log("Registering main menu");
              overlay.LoadCustomMainMenu(minecraft,screen, width, j);
  
  
              //overlay.setUpCustomMainMenu(minecraft, this, width,height, j, realmsNotificationsScreen);
              Consts.log("Registered main menu");
           }
  
        } else {
  
           if(Consts.background.size() == 0){
              Consts.warn("Cannot Register new Main menu  because list is 0");
  
           } else{
              Consts.log("Registering main menu");
              overlay.LoadCustomMainMenu(minecraft,screen,width, j);
  
  
              //overlay.setUpCustomMainMenu(minecraft, this, width,height, j, realmsNotificationsScreen);
              Consts.log("Registered main menu");

              
           }
        }

      }
   }
  
  
   
