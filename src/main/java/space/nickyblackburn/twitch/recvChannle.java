package space.nickyblackburn.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import space.nickyblackburn.utils.Consts;

public class recvChannle {



    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     * @return 
     */
    public recvChannle(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
    }

    /**
     * Subscribe to the ChannelMessage Event and write the output to the console
     */
    public void onChannelMessage(ChannelMessageEvent event) {
        Consts.TwitchCurrentMessage = event.getMessage();
        Consts.TwitchCurrentSent =  event.getUser().getName();
    }
    
}
