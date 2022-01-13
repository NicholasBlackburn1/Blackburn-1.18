package space.nickyblackburn.features;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import space.nickyblackburn.utils.Consts;

public class PlayerCrit{


    public void enableCrit(Minecraft mc , Player player,Boolean enabled){
        while(enabled){
            Minecraft MC = Consts.MC;

            Consts.log("enabled crit");
            
            //TODO: ENABLE CRIT STUFF
            try{

                if(mc.crosshairPickEntity != null){
                
                    
                            if( mc.crosshairPickEntity.getType() != null){

                            mc.gui.getChat().addMessage(new TextComponent(mc.crosshairPickEntity.getType().toString()));
                        }
                    }
                    }
                catch(Exception e){
                    Consts.error(e.toString());
                }

            if(!enabled){
                Consts.log("disbaled crit");
                break;
            }
    

    }
    
}}
