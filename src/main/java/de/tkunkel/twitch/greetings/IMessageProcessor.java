package de.tkunkel.twitch.greetings;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.types.config.Config;

public interface IMessageProcessor {
    void setConfig(Config config);

    void process(String channelName, String user, String message);

    void setClient(TwitchClient twitchClient);
}
