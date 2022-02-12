package space.nickyblackburn.twitch;

import net.minecraft.client.Options;
import space.nickyblackburn.screens.TwitchChatOverlay;
import space.nickyblackburn.utils.Consts;
import net.minecraft.client.*;
public class TwitchEnabler {
    
    public void enableTwitch(Options opt){
        
        if(!opt.twitchchatenable.isDown()){
          
            Consts.enableTwitch = true;
        }else{
            
        }
    }

}
