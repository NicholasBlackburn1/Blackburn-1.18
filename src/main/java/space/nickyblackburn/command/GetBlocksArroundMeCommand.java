package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;
import space.nickyblackburn.xray.XrayRenderer;

public class GetBlocksArroundMeCommand {
    
 

     // This is where the command gets registered to run
     public static void register(CommandDispatcher<CommandSourceStack> p_138786_) {
        XrayRenderer xray = new XrayRenderer();
        
        p_138786_.register(Commands.literal(".getblockarroundme").requires((p_138790_) -> {
            Consts.log("OwO .blockpos command run");
           return p_138790_.hasPermission(1);

        }).executes((p_138788_) -> {
            xray.getBlocks(Minecraft.getInstance(),Minecraft.getInstance().player);

           p_138788_.getSource().sendSuccess(new TranslatableComponent("blackburn.commands.getblocks").append(" "+Consts.blocklistseen.size()),true);
            
           return 1;
        }));
    }


    public static String getName(){
        return "blackburn.commands.getblocks.pre";
    }

    public static String getDecs(){
        return "blackburn.commands.getblocks.desc";
    }


}
