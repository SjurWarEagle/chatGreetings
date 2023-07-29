package de.tkunkel.twitch.greetings.processors;

public interface IProcessor {
    void process(String channelName, String user, String message);

    String getUsageInfo();
}
