package space.nickyblackburn.features;

import net.minecraft.world.entity.player.Player;
import space.nickyblackburn.utils.Consts;

public class PlayerSpeed{


    public void setPlayerSpeed(Player player,Boolean speed){
        
        if(speed){
            Consts.warn("Starting speedy player~");
            player.setWalk(100.0f);
        } else{
            Consts.warn("stopping speedy player~");
            player.setSpeed(1.0f);
        }

    }
    
}
