package space.nickyblackburn.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.utils.Consts;

public class PlayerSpeedCommand implements ICommandRegister{

    @Override
    public void register(List<String> command, Minecraft mc) {
        if(!command.isEmpty()){

            if (command.contains(".lightlevel")){
                
                command.clear();
              
                TextComponent lightlevelenable = new TextComponent(" §5"+I18n.get("blackburn.command.speed.useage"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".lightlevel enable")){
                command.clear();
                Consts.speed = true;
                
                TextComponent lightlevelenable = new TextComponent("Speed"+" "+I18n.get("blackburn.command.enabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".lightlevel disable")){
                command.clear();
            
                
                Consts.speed = false;
                TextComponent lightlevelenable = new TextComponent("Speed"+" "+I18n.get("blackburn.command.disabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }
          
        }
        
    
               }
            
        

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
