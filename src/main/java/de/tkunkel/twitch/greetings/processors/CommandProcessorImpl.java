package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;
import de.tkunkel.twitch.greetings.types.Chat2wasGreeted;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import de.tkunkel.twitch.greetings.types.config.ConfigCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

@Component
public class CommandProcessorImpl extends AbstractProcessor {
    private final Logger logger = LoggerFactory.getLogger(CommandProcessorImpl.class.getName());

    private final Chat2wasGreeted chat2wasGreeted = new Chat2wasGreeted();

    public CommandProcessorImpl(ConfigHolder configHolder, ClientHolder clientHolder, RuntimeInfoHolder runtimeInfoHolder) {
        super(configHolder, clientHolder, runtimeInfoHolder);
    }

    @Override
    public void process(String channelName, String user, String message, ChannelMessageEvent event) {
        for (ConfigChannel channel : configHolder.getConfig().channels) {
            if (channel.name.equalsIgnoreCase(channelName)) {
                storeLinkIfExists(channel, user, message);
                for (ConfigCommand command : channel.commands) {
                    if (Objects.isNull(command)||Objects.isNull(command.command)){
                        continue;
                    }
                    if (!message.toLowerCase().replaceAll(" ","").startsWith(command.command.toLowerCase().replaceAll(" ",""))) {
                        continue;
                    }
                    if (command.meOnly && (!user.equalsIgnoreCase("sjurwareagle"))) {
                        //me only, but it is not me
                        continue;
                    }
                    if (command.modOnly && !event.getPermissions().contains(CommandPermission.MODERATOR)) {
                        //mod only, but it is no mod
                        continue;
                    }

                    String textToSay = command.text;
                    textToSay = textToSay.replaceAll("##USERNAME##", user);
                    clientHolder.getTwitchClient().getChat().sendMessage(channelName, textToSay);
                }
            }
        }
    }

    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }

    private void storeLinkIfExists(ConfigChannel channel, String user, String message) {
        if (channel.fileForLinks == null) {
            return;
        }
        for (String word : message.toLowerCase().split("\\s+")) {
            if (word.startsWith("http") || word.startsWith("www")) {
                try {
                    logger.info("Writing link " + word + " from " + user);
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

}
