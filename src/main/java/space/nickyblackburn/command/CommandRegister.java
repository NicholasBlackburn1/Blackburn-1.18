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

        Consts.commands.add(0,VersionCommand.getName());
        Consts.commanddesc.add(0,VersionCommand.getName());
    }
    
}
