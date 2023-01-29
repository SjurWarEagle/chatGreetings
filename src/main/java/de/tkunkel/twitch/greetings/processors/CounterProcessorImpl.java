package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.types.config.Config;

public class CounterProcessorImpl implements IProcessor {
    private Config config;
    private TwitchClient twitchClient;
    private final String SKULL = "\uD83D\uDC80";
    private final String SKULL_WITH_BONES = "☠";


    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void setClient(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }

    @Override
    public void process(String channelName, String user, String message) {
        if (channelName.equals("SjurWarEagle") && (message.startsWith("") || message.startsWith(""))){
    }
    }

}
