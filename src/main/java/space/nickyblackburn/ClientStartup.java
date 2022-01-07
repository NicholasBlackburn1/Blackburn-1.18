/**
 * this is for handling printing messages as the client logs in the first time 
*/

package space.nickyblackburn;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.utils.Consts;

public class ClientStartup {




     //allows me to send start up messages
    public void sendStartupMessages(Minecraft mine){
        int i = 0; 
        
        if(mine.level != null){
            if (!mine.pause) {
                    if (i <= 0){
                    i++;
                    Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get("blackburn.message.Startup")));
                    
                    Consts.log("inc is "+ " "+ i);
                 
                }
                
            }
        }
    }
}
