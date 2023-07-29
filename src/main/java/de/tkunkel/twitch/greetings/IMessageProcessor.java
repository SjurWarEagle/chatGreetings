package de.tkunkel.twitch.greetings;

public interface IMessageProcessor {

    void process(String channelName, String user, String message);

}
