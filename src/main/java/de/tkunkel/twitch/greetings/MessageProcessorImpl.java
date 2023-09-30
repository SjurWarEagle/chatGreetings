package de.tkunkel.twitch.greetings;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.processors.IProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageProcessorImpl implements IMessageProcessor {
    private final ConfigHolder configHolder;
    private final List<? extends IProcessor> list;

    public MessageProcessorImpl(ConfigHolder configHolder, List<? extends IProcessor> list) {
        this.configHolder = configHolder;
        this.list = list;
    }

    @Override
    public void process(String channelName, String user, String message, ChannelMessageEvent event) {
        list.forEach((bean) -> bean.process(channelName, user, message, event));
    }

}
