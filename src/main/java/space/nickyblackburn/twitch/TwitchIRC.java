package space.nickyblackburn.twitch;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import space.nickyblackburn.utils.Consts;

public class TwitchIRC {
    


// chat credential
OAuth2Credential credential;
// twitch client
TwitchClient twitchClient;
recvChannle channle;
SimpleEventHandler eventHandler;

// this should set up the bot connection

public void setupBot(){

    Consts.info("starting to start twitch bot");
  
    // Creates basic client connection 
    this.credential = new OAuth2Credential(Consts.TwitchUsername, Consts.TwitchPass);
    this.eventHandler  = this.twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
 

    this.twitchClient = TwitchClientBuilder.builder()
        
    .withEnableChat(true)
    .withChatAccount(this.credential)
    .build();

    this.channle = new recvChannle(this.eventHandler);


    Consts.info("started twitch bot...");
    start();
}

// starts suwu
public void start(){
    Consts.warn("creating twithc bot info");
        // connects to the chat 
        this.twitchClient.getChat().joinChannel(Consts.TwitchUsername);

}


}
