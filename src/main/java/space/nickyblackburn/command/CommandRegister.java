/***
 * this registers commands in a nice little location for me 
 */
package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import space.nickyblackburn.command.BlackburnCommand.CommandSelection;
import space.nickyblackburn.utils.Consts;

public class CommandRegister {
    

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){


        VersionCommand.register(dispatcher);
        HelpCommand.register(dispatcher);
        GetBlockStandingCommand.register(dispatcher);
        GetBlocksArroundMeCommand.register(dispatcher);
    

        Consts.commands.add(0,HelpCommand.getName());
        Consts.commanddesc.add(0,HelpCommand.getDecs());
        Consts.commands.add(1,VersionCommand.getName());
        Consts.commanddesc.add(1,VersionCommand.getDecs());
        Consts.commands.add(2,GetBlockStandingCommand.getName());
        Consts.commanddesc.add(2,GetBlockStandingCommand.getDecs());
        Consts.commands.add(3,GetBlocksArroundMeCommand.getName());
        Consts.commanddesc.add(3,GetBlocksArroundMeCommand.getDecs());
        
    }
    
}
