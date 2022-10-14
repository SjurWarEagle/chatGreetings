import de.tkunkel.twitch.monitor.types.config.Config;

public interface IMessageProcessor {
    void setConfig(Config config);

    void process(String channelName, String message);

    boolean getTriggerAndSetToRead(String chat, String emote);

    boolean isInCoolDown(String chat, String emote);

    int getEventsOfInterval(String chat, String emote);
}
