package space.nickyblackburn.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.features.PlayerSpeed;
import space.nickyblackburn.utils.Consts;

public class PlayerSpeedCommand implements ICommandRegister{


    PlayerSpeed speed = new PlayerSpeed();

    @Override
    public void register(List<String> command, Minecraft mc) {
        if(!command.isEmpty()){

            if (command.contains(".speed")){
                
                command.clear();
              
                TextComponent lightlevelenable = new TextComponent(" §5"+I18n.get("blackburn.command.speed.useage"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".speed enable")){
                command.clear();
              
                speed.setPlayerSpeed(Minecraft.getInstance().player,true);
                
                TextComponent lightlevelenable = new TextComponent("Speed"+" "+I18n.get("blackburn.command.enabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".speed disable")){
                command.clear();
            
                
                speed.setPlayerSpeed(Minecraft.getInstance().player,false);
                
                TextComponent lightlevelenable = new TextComponent("Speed"+" "+I18n.get("blackburn.command.disabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }
          
        }
        
    
               }
            
        

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "blackburn.command.speed.pre";
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return "blackburn.command.speed.desc";
    }
    
}
