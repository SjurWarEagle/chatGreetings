package de.tkunkel.twitch.greetings;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public interface IMessageProcessor {

    void process(String channelName, String user, String message, ChannelMessageEvent event);

}
