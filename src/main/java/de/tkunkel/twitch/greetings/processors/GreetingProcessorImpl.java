package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.TwitchClient;
import de.tkunkel.twitch.greetings.types.Chat2wasGreeted;
import de.tkunkel.twitch.greetings.types.config.Config;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import de.tkunkel.twitch.greetings.types.config.ConfigGreeting;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Component
public class GreetingProcessorImpl implements IProcessor {
    private final Logger logger = LoggerFactory.getLogger(GreetingProcessorImpl.class.getName());

    private final Chat2wasGreeted chat2wasGreeted = new Chat2wasGreeted();
    private Config config;
    private TwitchClient twitchClient;

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void process(String channelName, String user, String message) {
        for (ConfigChannel channel : config.channels) {
            if (channel.name.equalsIgnoreCase(channelName)) {
                storeLinkIfExists(channel, user, message);
                for (ConfigGreeting greeting : channel.greetings) {
                    if (greeting.user.equalsIgnoreCase(user)
                            && !chat2wasGreeted.wasGreetedToday(channelName, user)
                    ) {
                        int rnd = (int) Math.floor(Math.random() * greeting.greetingOptions.size());
                        String greetingMessage = greeting.greetingOptions.get(rnd);
                        //System.out.println("Greeting  " + user + ": " + greetingMessage);
                        twitchClient.getChat().sendMessage(channelName, greetingMessage);
                        chat2wasGreeted.markAsGreeted(channelName, user);
                        logger.info("Greeted " + user + " in " + channelName);
                    }
                }
            }
        }
    }

    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }

    private void storeLinkIfExists(ConfigChannel channel, String user, String message) {
//        logger.info("storeLinkIfExists "+channel.name+ " "+user+ " "+message);
//        logger.info(""+Strings.isEmpty(channel.fileForLinks));

        if (Strings.isEmpty(channel.fileForLinks)) {
            return;
        }
        for (String word : message.toLowerCase().split("\\s+")) {
            if (word.startsWith("http")) {
//                logger.info("storeLinkIfExists " + word);
                try {
                    logger.info("Writing link " + word+" from "+user);
                    Writer output = new BufferedWriter(new FileWriter(channel.fileForLinks, true));
                    output.write(user + ": " + word + "\n");
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void setClient(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }

}
