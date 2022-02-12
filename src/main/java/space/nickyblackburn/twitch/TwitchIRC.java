package space.nickyblackburn.twitch;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;

import space.nickyblackburn.utils.Consts;

public class TwitchIRC {
    


// chat credential
OAuth2Credential credential;
// twitch client
TwitchClient twitchClient;

// this should set up the bot connection

public void setupBot(){
    
    this.credential = new OAuth2Credential(Consts.TwitchUsername, Consts.TwitchPass);
    this.twitchClient = TwitchClientBuilder.builder()
        
    .withEnableChat(true)
    .withChatAccount(this.credential)
    .build();

    
}


}
