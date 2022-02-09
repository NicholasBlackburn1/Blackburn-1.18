package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.advancements.CriteriaTriggers;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import space.nickyblackburn.utils.Consts;

public class HelpCommand implements ICommandRegister{

 
    @Override
    public void register(List<String> command,Minecraft mc) {
        TextComponent startup ;
        TextComponent commands;
                
            if(!command.isEmpty()){

                if (command.contains(".help")){

                    startup = new TextComponent(I18n.a("blackburn.commands.help"));
                        startup.setStyle(Style.EMPTY);
                        mc.gui.getChat().addMessage(startup);

                    for (int i = 0;  i < Consts.commands.size(); i++)
                    {   
                        
                            Consts.log("data ib the commands list are "+Consts.commands.get(i).toString());
                    
                            mc.gui.getChat().addMessage(new TextComponent(I18n.a(Consts.commands.get(i).toString())+" "+I18n.a(Consts.commanddesc.get(i).toString())));
                            
                            
                    }   
                    command.clear();
               


            }}
        }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "blackburn.commands.help.pre";
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return "blackburn.commands.help.desc";
    }
}
