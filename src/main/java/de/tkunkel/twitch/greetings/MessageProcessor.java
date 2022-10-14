package de.tkunkel.twitch.greetings;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.types.Chat2wasGreeted;
import de.tkunkel.twitch.greetings.types.config.Config;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import de.tkunkel.twitch.greetings.types.config.ConfigGreeting;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor implements IMessageProcessor {
    private final Chat2wasGreeted chat2wasGreeted = new Chat2wasGreeted();
    private Config config;
    private TwitchClient twitchClient;

    @Override
    public void setConfig(Config config) {
        this.config = config;
//        for (ConfigChannel channel : config.channels) {
//        }
    }

    @Override
    public void process(String channelName, String user, String message) {
        for (ConfigChannel channel : config.channels) {
            if (channel.name.equalsIgnoreCase(channelName)) {
                for (ConfigGreeting greeting : channel.greetings) {
                    if (greeting.user.equalsIgnoreCase(user)
                            && !chat2wasGreeted.wasGreetedToday(channelName, user)
                    ) {
                        int rnd = (int) Math.floor(Math.random() * greeting.greetingOptions.size());
                        String greetingMessage = greeting.greetingOptions.get(rnd);
                        //System.out.println("Greeting  " + user + ": " + greetingMessage);
                        twitchClient.getChat().sendMessage(channelName,greetingMessage);
                        chat2wasGreeted.markAsGreeted(channelName, user);
                    }
                }
            }
        }
    }

    @Override
    public void setClient(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }
}
