package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;
import java.util.List;


public class VersionCommand implements ICommandRegister{

    @Override
    // This is where the command gets registered to run
    public void register(List<String> command,Minecraft mc) {
        
        TextComponent startup ;
        TextComponent commands;
        
        if(!command.isEmpty()){

            if (command.contains(".version")){

                   
                
                    mc.gui.getChat().addMessage(new TextComponent(I18n.get("blackburn.commands.version")).append(" "+Consts.VERSION));

                    command.clear();
                
        
            }   


        }
    }
          

    @Override
    public String getName(){
        return "blackburn.commands.version.pre";
    }
    @Override
    public  String getDesc(){
        return "blackburn.commands.version.desc";
    }
}



