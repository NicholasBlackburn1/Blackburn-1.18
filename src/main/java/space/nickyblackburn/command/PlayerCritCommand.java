package space.nickyblackburn.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import space.nickyblackburn.features.PlayerCrit;
;
import space.nickyblackburn.utils.Consts;

public class PlayerCritCommand implements ICommandRegister{


    PlayerCrit crit = new PlayerCrit();

    @Override
    public void register(List<String> command, Minecraft mc) {
        if(!command.isEmpty()){

            if (command.contains(".crit")){
                
                command.clear();
              
                TextComponent lightlevelenable = new TextComponent(" §5"+I18n.get("blackburn.command.crit.useage"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".crit enable")){
                command.clear();
              
                crit.enableCrit(mc, Minecraft.getInstance().player, true);
                
                TextComponent lightlevelenable = new TextComponent("crit"+" "+I18n.get("blackburn.command.enabled"));
                mc.gui.getChat().addMessage(lightlevelenable);
            
                
                command.clear();
            
            
                
            }

            if (command.contains(".crit disable")){
                command.clear();
            
                
                crit.enableCrit(mc, Minecraft.getInstance().player, false);

                TextComponent lightlevelenable = new TextComponent("crit"+" "+I18n.get("blackburn.command.disabled"));
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
