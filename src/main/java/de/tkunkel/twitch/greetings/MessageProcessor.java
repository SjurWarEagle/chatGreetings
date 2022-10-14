import de.tkunkel.twitch.monitor.types.Chats;
import de.tkunkel.twitch.monitor.types.EmotesPerIntervalTrigger;
import de.tkunkel.twitch.monitor.types.config.Config;
import de.tkunkel.twitch.monitor.types.config.ConfigChannel;
import de.tkunkel.twitch.monitor.types.config.ConfigEmote;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MessageProcessor implements IMessageProcessor{
    private final Chats chats = new Chats();
    private Config config;
    private final EmoteDetector emoteDetector = new EmoteDetector();

    @Override
    public void setConfig(Config config) {
        this.config = config;
        for (ConfigChannel channel : config.channels) {
            List<EmotesPerIntervalTrigger> triggers = new ArrayList<>();
            for (ConfigEmote emote : channel.emotes) {
                EmotesPerIntervalTrigger emotesPerIntervalTrigger = new EmotesPerIntervalTrigger(emote.triggerCnt, emote.intervalInSeconds, TimeUnit.SECONDS, emote.coolDownInSeconds, TimeUnit.SECONDS);
                triggers.add(emotesPerIntervalTrigger);
                //fixme this needs to be a list, not only a single emote
                chats.chat2emotesPerInterval.put(channel.name, emotesPerIntervalTrigger);
            }
        }
    }

    @Override
    public void process(String channelName, String message) {
        for (ConfigChannel channel : config.channels) {
            if (channel.name.equalsIgnoreCase(channelName)) {
                for (ConfigEmote emote : channel.emotes) {
                    checkEmote(channelName, emote, message);
                }
            }
        }

    }

    @Override
    public boolean getTriggerAndSetToRead(String chat, String emote) {
        if (chats.chat2emotesPerInterval.containsKey(chat)) {
            EmotesPerIntervalTrigger emotesPerIntervalTrigger = chats.chat2emotesPerInterval.get(chat);
            return emotesPerIntervalTrigger.readEventFiredMarkerAndSetToReceived(emote);
        }
        return false;
    }

    @Override
    public boolean isInCoolDown(String chat, String emote) {
        if (chats.chat2emotesPerInterval.containsKey(chat)) {
            EmotesPerIntervalTrigger emotesPerIntervalTrigger = chats.chat2emotesPerInterval.get(chat);
            return emotesPerIntervalTrigger.isInCoolDown(emote);
        }
        return false;
    }

    @Override
    public int getEventsOfInterval(String chat, String emote) {
        if (chats.chat2emotesPerInterval.containsKey(chat)) {
            EmotesPerIntervalTrigger emotesPerIntervalTrigger = chats.chat2emotesPerInterval.get(chat);
            return emotesPerIntervalTrigger.emotesPerInterval.getOrDefault(emote,new ArrayList<>()).size();
        }
        return 0;
    }

    private void checkEmote(String chat, ConfigEmote emote, String message) {
//        logger.info("emoteDetector.containsEmote("+emote.name+","+ message+")");
        if (emoteDetector.containsEmote(emote.name, message)) {
//            System.out.println(message);

//            logger.info("+ " + message);
            chats.rememberEmote(chat, emote.name);
        }
    }
}
