package de.tkunkel.twitch.greetings;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.google.gson.Gson;
import de.tkunkel.twitch.greetings.types.config.Config;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = Starter.class)
@EntityScan(basePackageClasses = Starter.class)
public class Starter {

    @Autowired
    private MessageProcessor messageProcessor;

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @PostConstruct
    private void startMonitor() throws IOException {
        Logger logger = LoggerFactory.getLogger(Starter.class.getName());

        Config config = readConfig();
//        logger.info(config.toString());

        // chat credential
        String accessToken = config.accessToken;
        OAuth2Credential credential = new OAuth2Credential("twitch", accessToken);

        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();

        EventManager eventManager = twitchClient.getEventManager();

        connectToChannels(logger, config, twitchClient);

        messageProcessor.setConfig(config);
        messageProcessor.setClient(twitchClient);

        eventManager.onEvent(ChannelMessageEvent.class, event -> {
            messageProcessor.process(event.getChannel().getName()
                    , event.getUser().getName()
                    , event.getMessage()
            );
//            twitchClient.getChat().sendPrivateMessage("sjurwareagle", "Test2");
        });

    }

    private static void connectToChannels(Logger logger, Config config, TwitchClient twitchClient) {
        for (ConfigChannel channel : config.channels) {
            logger.info("Connecting to channel '" + channel.name + "'");
            twitchClient.getChat().joinChannel(channel.name);
        }
    }

    private static Config readConfig() throws IOException {
        Gson gson = new Gson();

        String configFile = System.getenv("CONFIG_FILE");
        if (Objects.isNull(configFile)) {
            throw new RuntimeException("Environment variable CONFIG_FILE not set");
        }

        return gson.fromJson(Files.readString(Paths.get(configFile)), Config.class);
    }
}
