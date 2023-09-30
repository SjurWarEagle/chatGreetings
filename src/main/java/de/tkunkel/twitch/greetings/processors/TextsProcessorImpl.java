package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import de.tkunkel.twitch.greetings.types.config.ConfigTexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TextsProcessorImpl extends AbstractProcessor {
    private final Logger logger = LoggerFactory.getLogger(TextsProcessorImpl.class.getName());

    public TextsProcessorImpl(ConfigHolder configHolder, ClientHolder clientHolder, RuntimeInfoHolder runtimeInfoHolder) {
        super(configHolder, clientHolder, runtimeInfoHolder);
    }

    @Override
    public void process(String channelName, String user, String message, ChannelMessageEvent event) {
        for (ConfigChannel channel : configHolder.getConfig().channels) {
            if (channel.name.equalsIgnoreCase(channelName)) {
                for (ConfigTexts text : channel.texts) {
                    if (text.user.equalsIgnoreCase(user)
                            && message.equals(text.triggerWord)
                    ) {
                        Runnable runTextTelling = () -> {
                            for (String line : text.lines) {
                                try {
                                    clientHolder.getTwitchClient().getChat().sendMessage(channelName, line);
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        new Thread(runTextTelling).start();
                        logger.info("Processed text " + user + " " + message + " in " + channelName);
                    }
                }
            }
        }
    }

    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }


}
