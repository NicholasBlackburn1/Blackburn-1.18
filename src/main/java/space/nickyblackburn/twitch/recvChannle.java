package space.nickyblackburn.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

import space.nickyblackburn.utils.Consts;

public class recvChannle {



    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     * @return 
     */
    public recvChannle(SimpleEventHandler eventHandler) {

        eventHandler.onEvent(ChannelMessageEvent.class, event -> {

            if (event.getMessage() == null){

            }else{
            Consts.chatmessage.add(event.getMessage());
            }
        
        });
        
    }

    
}
