package space.nickyblackburn.command;

import java.util.List;


import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.features.PlayerCrit;
import space.nickyblackburn.utils.Consts;


public class NoFallCommand implements ICommandRegister{
    
    @Override
    public void register(List<String> command, Minecraft mc) {
        if(!command.isEmpty()){

            if (command.contains(".nofall")){
                
                command.clear();
              
                TextComponent lightlevelenable = new TextComponent(" §5"+I18n.a("blackburn.command.useage"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".nofall enable")){
                command.clear();
                Consts.nofall = true;

              
                
                TextComponent lightlevelenable = new TextComponent("nofall"+I18n.a("blackburn.command.enabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".nofall disable")){
                command.clear();
            
                
               Consts.nofall = false;

                TextComponent lightlevelenable = new TextComponent("nofall"+I18n.a("blackburn.command.disabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }
          
        }
        
    
               }
            
        

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "blackburn.commands.crit.pre";
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return "blackburn.commands.crit.desc";
    }
}
