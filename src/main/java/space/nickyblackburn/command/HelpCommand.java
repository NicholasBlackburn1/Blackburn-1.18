package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;

public class HelpCommand {
    
// This is where the command gets registered to run
public static void register(CommandDispatcher<CommandSourceStack> p_138786_) {
        
    p_138786_.register(BlackburnCommand.literal(".help").requires((p_138790_) -> {
        Consts.log("OwO .help command run");
       return p_138790_.hasPermission(1);

    }).executes((p_138788_) -> {
       p_138788_.getSource().sendSuccess(new TranslatableComponent("blackburn.commands.help"), true);
        
       // this should run and show me the  the commands in it 
       for (int i = 0;  i < Consts.commands.size()-1; i++)
       {   
            Consts.log("data ib the commands list are "+Consts.commands.get(i).toString());
            p_138788_.getSource().sendSuccess(new TranslatableComponent(Consts.commands.get(i).toString()).append(new TranslatableComponent(Consts.commanddesc.get(i).toString())), true);

       }    
      
      
       return 1;
    }));
}
public static String getName(){
    return "blackburn.commands.help.pre";
}

public static String getDecs(){
    return "blackburn.commands.help.desc";
}


}
