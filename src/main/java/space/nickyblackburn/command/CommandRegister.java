/***
 * this registers commands in a nice little location for me 
 */
package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import space.nickyblackburn.command.BlackburnCommand.CommandSelection;
import space.nickyblackburn.utils.Consts;
import java.util.List;

public class CommandRegister {
    
    HelpCommand help = new HelpCommand();
    VersionCommand version = new VersionCommand();
    PlayerSpeedCommand speed = new PlayerSpeedCommand();


    public void registerCommands(Minecraft mc){


        List<String> command = mc.gui.getChat().getRecentChat();

        help.register(command, mc);
        version.register(command, mc);
        speed.register(command, mc);

        
    }

    public void addToCommandList(){
        Consts.commands.add(0,help.getName());
        Consts.commands.add(1,version.getName());
        Consts.commands.add(2,speed.getName());
        
    }


    public void addToCommandDescList(){
        Consts.commanddesc.add(0,help.getDesc());
        Consts.commanddesc.add(1,version.getDesc());
        Consts.commanddesc.add(2,speed.getDesc());
        
    }

    
}
