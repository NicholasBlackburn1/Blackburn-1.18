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
import space.nickyblackburn.command.CommandRegister;
import space.nickyblackburn.utils.Consts;

public class ClientStartup {





    // allows me to send start up messages
    private void messages(){
        
        
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.a("blackburn.message.startup")));
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.a("blackburn.message.cutie")));
        Consts.minecraft.gui.getChat().addMessage(new TextComponent(I18n.a("blackburn.message.howto")));

    }

     //allows me to send start up messages
    public void sendStartupMessages(Minecraft mine){
        
        CommandRegister register = new CommandRegister();

        if(mine.level != null){
            if (!mine.pause && Consts.showStart) {
                register.addToCommandDescList();
                register.addToCommandList();
                messages();
                Consts.showStart = false;
                

                
            }
            if (!mine.pause){
                
                register.registerCommands(mine);
                register.hacks(mine);
            }
        }
    }


}
