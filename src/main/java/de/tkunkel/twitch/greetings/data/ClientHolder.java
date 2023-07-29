package de.tkunkel.twitch.greetings.data;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class ClientHolder {
    private final ConfigHolder configHolder;
    private final TwitchClient twitchClient;

    public ClientHolder(ConfigHolder configHolder) {
        this.configHolder = configHolder;

        // chat credential
        String accessToken = configHolder.getConfig().accessToken;
        OAuth2Credential credential = new OAuth2Credential("twitch", accessToken);

        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();


    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
