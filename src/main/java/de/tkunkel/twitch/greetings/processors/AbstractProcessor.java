package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;

abstract public class AbstractProcessor implements IProcessor {
    protected final ClientHolder clientHolder;
    protected final ConfigHolder configHolder;
    protected final RuntimeInfoHolder runtimeInfoHolder;

    public AbstractProcessor(ConfigHolder configHolder, ClientHolder clientHolder, RuntimeInfoHolder runtimeInfoHolder) {
        this.configHolder = configHolder;
        this.clientHolder = clientHolder;
        this.runtimeInfoHolder = runtimeInfoHolder;
    }

    public abstract void process(String channelName, String user, String message, ChannelMessageEvent event);

}
