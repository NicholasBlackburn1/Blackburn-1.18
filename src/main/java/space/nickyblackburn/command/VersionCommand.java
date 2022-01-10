package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;

public class VersionCommand{

    // This is where the command gets registered to run
    public static void register(CommandDispatcher<CommandSourceStack> p_138786_) {
        
        p_138786_.register(Commands.literal(".version").requires((p_138790_) -> {
            Consts.log("OwO .Version command run");
           return p_138790_.hasPermission(1);
        }).executes((p_138788_) -> {
           p_138788_.getSource().sendSuccess(new TranslatableComponent("blackburn.commands.version").append(" "+Consts.VERSION), true);
          
           return 1;
        }));
    }
}
