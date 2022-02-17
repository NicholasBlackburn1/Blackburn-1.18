package space.nickyblackburn.screens;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;

/***
 * this class is for displaying my custom Version screen and info about the client 
 */
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.utils.Consts;

public class CopyRightScreen extends Screen{

    

    public CopyRightScreen(Component p_96550_) {
        super(new TextComponent("CopyRight by nicky blackburn"));
    }

    public void render(PoseStack p_96295_, int p_96296_, int p_96297_, float p_96298){
        this.renderBackground(p_96295_);

        Consts.rich.CustomPresenceWithImage("Who Made this UwU~", "By Nicky Blackburn", "projeto_14_6", "OwO licks ur Bulgy");
        
        drawCenteredString(p_96295_, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
        
        this.drawCenteredString(p_96295_, this.font,"§6Blackburn Software Stats UwU",this.width/2 ,140, 16777215);
        this.drawCenteredString(p_96295_, this.font,"§6Version:"+"§r "+Consts.VERSION+Consts.RELEASE,this.width/2 ,150, 16777215);
        this.drawCenteredString(p_96295_, this.font,"§6Build:"+"§r "+Consts.RELEASE,this.width/2 ,160, 16777215);
        try {
			File jarFile = new File
			(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

            this.drawCenteredString(p_96295_, this.font,"§6BuildDate:"+"§r "+new Date(jarFile.lastModified()),this.width/2 ,170, 16777215);
            
		} catch (URISyntaxException e1) {
            Consts.error(e1.getMessage());
		}
      
    }
}
