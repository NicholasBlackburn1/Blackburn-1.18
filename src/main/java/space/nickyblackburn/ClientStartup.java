/**
 * this is for handling printing messages as the client logs in the first time 
*/

package space.nickyblackburn;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import space.nickyblackburn.utils.Consts;

public class ClientStartup {


    private void messages(){
        
        
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get("blackburn.message.startup")));
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get("blackburn.message.cutie")));
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get("")));
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.get("blackburn.message.commands.pre")+ " §b"+ Consts.commands.size()+ " "+I18n.get("blackburn.message.commands.end")));
       
    }


     //allows me to send start up messages
    public void sendStartupMessages(Minecraft mine){
     
        if(mine.level != null){
            if (!mine.pause && Consts.showStart) {
                messages();

                
            }
        }
    }


}
