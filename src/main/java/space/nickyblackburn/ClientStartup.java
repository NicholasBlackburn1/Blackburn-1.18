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
import space.nickyblackburn.discord.loading;
import space.nickyblackburn.screens.TwitchChatOverlay;
import space.nickyblackburn.twitch.TwitchEnabler;
import space.nickyblackburn.twitch.TwitchIRC;
import space.nickyblackburn.twitch.recvChannle;
import space.nickyblackburn.utils.Consts;

public class ClientStartup {

    TwitchIRC irc = new TwitchIRC();
    recvChannle chan;

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
               
                loading load  =new loading();
                load.getOS();
                
            }
            if (!mine.pause){

                TwitchEnabler enable = new TwitchEnabler();
                
             
                register.registerCommands(mine);
                register.hacks(mine);
                
            if(mine.player.isAlive()){
                
                try {
                    Consts.rich.CustomPresenceWithImage("Yaaa Playing Minecrafty~", "UwU~ Love all you Furries uwu~", "gamer", "OwO Gamer foxy~");                    

                    if(mine.getCurrentServer().isLan()){
                }
                else{

                        // this aallows me to send rpc when some one is alive
                        Consts.rich.ImageWithoutDescPersantes("In a Server Named "+mine.getCurrentServer().name, "gamer", mine.getCurrentServer().playerList.size(), 0);
                    }


                } catch (Exception e) {
                    //TODO: handle exception
                }
            
        }
            }
        }
    }
}

