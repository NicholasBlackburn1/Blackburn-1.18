
/***
 * this class is for changing the images on the loading screen
 */
package space.nickyblackburn.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;

import java.util.Calendar;
import java.util.Date;

import com.mojang.blaze3d.vertex.PoseStack;

import org.lwjgl.system.Platform;


public class LoadingOverlayImage {
     

    // adding loading image loading screen
    public void setLoadingImage(ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION,Minecraft pMc){
        
        // ALLOWS ME TO Swich the loading image based on day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        switch (calendar.get(Calendar.DAY_OF_WEEK) ) {
            case 1:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("blackburn/title/logo.png");
              
                break;

            case 2:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("blackburn/title/logo2.png");
                break;

            case 3:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("blackburn/title/logo3.png");
                break;

            case 4:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("blackburn/title/logo4.png");
                break;

            case 5:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/blackburn/title/logo5.png");
                break;
                
            case 6:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/blackburn/title/logo6.png");
                break;
            
                       
            case 7:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/blackburn/title/logo7.png");
                break;
        
            
        
            default:
                MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/blackburn/title/logo.png");
                break;
        }
        
        pMc.getTextureManager().register(MOJANG_STUDIOS_LOGO_LOCATION, new LoadingOverlay.LogoTexture());
    
    }

    // sets the default mojang logo local
    public void setLogoDefaultLogoLocal(PoseStack pose, GuiComponent component, int j2, int j1, int k2, int i1, int d1){
         
       component.blit(pose, j2 - j1, k2 - i1, j1, (int)d1, -0.0625F, 0.0F, 120, 60, 120, 120);
       component.blit(pose, j2, k2 - i1, j1, (int)d1, 0.0625F, 60.0F, 120, 60, 120, 120);

    }

    // seting small logo pos
    public void setCustomLogoPosNonFull(LoadingOverlay component,PoseStack pMatrixStack,int px,int py, int UOffset, float VOffest, int pWidth, int pHight, int pTextureWidth, int pTextureHight){


        component.blit(pMatrixStack,px, py, UOffset, VOffest,pWidth, pHight,pTextureWidth,pTextureHight);
      
    }

      // seting logo pos
      public void setCustomLogoPosFull(LoadingOverlay component,PoseStack pMatrixStack,int px,int py, int UOffset, float VOffest, int pWidth, int pHight, int pTextureWidth, int pTextureHight){


        component.blit(pMatrixStack,px, py, UOffset, VOffest,pWidth, pHight,pTextureWidth,pTextureHight);
      
    }
}
