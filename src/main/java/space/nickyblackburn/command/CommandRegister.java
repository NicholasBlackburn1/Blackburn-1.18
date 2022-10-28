/***
 * this registers commands in a nice little location for me 
 */
package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import space.nickyblackburn.command.BlackburnCommand.CommandSelection;
import space.nickyblackburn.features.PlayerCrit;
import space.nickyblackburn.utils.Consts;
import java.util.List;

public class CommandRegister {
    
    HelpCommand help = new HelpCommand();
    VersionCommand version = new VersionCommand();
    PlayerCritCommand crit = new PlayerCritCommand();
    NoFallCommand nofall = new NoFallCommand();


    public void registerCommands(Minecraft mc){


        List<String> command = mc.gui.getChat().getRecentChat();

        help.register(command, mc);
        version.register(command, mc);
        crit.register(command, mc);
        nofall.register(command, mc);





        
    }

    public void hacks(Minecraft mc ){
        PlayerCrit crit = new PlayerCrit();
        crit.enableCrit(mc, Minecraft.getInstance().player,Consts.crit);
                
    }

    // adds commands 
    public void addToCommandList(){
        Consts.commands.add(0,help.getName());
        Consts.commands.add(1,version.getName());
        Consts.commands.add(2,crit.getName());
        Consts.commands.add(3,nofall.getName());
        
    }


    public void addToCommandDescList(){
        Consts.commanddesc.add(0,help.getDesc());
        Consts.commanddesc.add(1,version.getDesc());
        Consts.commanddesc.add(2,crit.getDesc());
        
    }

    
}
