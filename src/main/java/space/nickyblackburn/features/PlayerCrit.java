package space.nickyblackburn.features;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import space.nickyblackburn.utils.Consts;

public class PlayerCrit{


    public void enableCrit(Minecraft mc , Player player,Boolean enabled){
        if(enabled){
            Consts.log("enabled crit");
            
            //TODO: ENABLE CRIT STUFF
           
        }else{
            Consts.log("disbaled crit");
        }

    }
    
}
