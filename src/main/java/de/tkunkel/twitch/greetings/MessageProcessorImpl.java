package de.tkunkel.twitch.greetings;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.processors.IProcessor;
import de.tkunkel.twitch.greetings.types.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class MessageProcessorImpl implements IMessageProcessor {
    private Config config;
    private TwitchClient twitchClient;

    @Autowired
    private List<? extends IProcessor> list;

    @PostConstruct
    public void setUpProcessors() {
        list.forEach((bean) -> bean.setClient(twitchClient));
    }

    @Override
    public void setConfig(Config config) {
        list.forEach((bean) -> bean.setConfig(config));
    }

    @Override
    public void process(String channelName, String user, String message) {
        list.forEach((bean) -> bean.process(channelName, user, message));
    }

    @Override
    public void setClient(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }
}
