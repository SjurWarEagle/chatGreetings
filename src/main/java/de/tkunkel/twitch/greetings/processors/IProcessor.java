package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public interface IProcessor {
    void process(String channelName, String user, String message, ChannelMessageEvent event);

    String getUsageInfo();
}
