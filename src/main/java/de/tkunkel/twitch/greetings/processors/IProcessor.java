package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.types.config.Config;

public interface IProcessor {
    void process(String channelName, String user, String message);

    String getUsageInfo();

    void setConfig(Config config);

    void setClient(TwitchClient twitchClient);
}
