/***
 * this registers commands in a nice little location for me 
 */
package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import space.nickyblackburn.command.BlackburnCommand.CommandSelection;

public class CommandRegister {


    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        VersionCommand.register(dispatcher);
    }
    
}
