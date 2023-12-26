package de.tkunkel.twitch.greetings.processors;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;
import de.tkunkel.twitch.greetings.data.persistance.RuntimeInfoBo;
import de.tkunkel.twitch.greetings.types.config.ConfigChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ShoutoutProcessorImpl extends AbstractProcessor {
    Logger LOG = LoggerFactory.getLogger(ShoutoutProcessorImpl.class.getName());

    public ShoutoutProcessorImpl(ConfigHolder configHolder, ClientHolder clientHolder, RuntimeInfoHolder runtimeInfoHolder) {
        super(configHolder, clientHolder, runtimeInfoHolder);
    }

    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }

    @Override
    public void process(String channelName, String user, String message, ChannelMessageEvent event) {
        RuntimeInfoBo runtimeInfo = runtimeInfoHolder.getRuntimeInfo();
        HashMap<String, HashMap<String, Date>> lastShoutouts = runtimeInfo.getLastShoutout();
        if (Objects.isNull(lastShoutouts)) {
            lastShoutouts = new HashMap<>();
            runtimeInfo.setLastShoutout(lastShoutouts);
        }
        if (!isUserRelevantForShoutout(channelName, user)) {
//            LOG.info(channelName + ": NO shoutout to " + user + ", user not relevant.");
            return;
        }

        Date userShoutout;
        if (!lastShoutouts.containsKey(channelName)) {
            lastShoutouts.put(channelName, new HashMap<>());
        }
        if (lastShoutouts.get(channelName).containsKey(user)) {
            userShoutout = lastShoutouts.get(channelName).get(user);
        } else {
            userShoutout = new Date(0);
            lastShoutouts.get(channelName).put(user, userShoutout);
        }

        long twelfeHours = TimeUnit.HOURS.toMillis(12);
        if ((userShoutout.getTime() + twelfeHours) > new Date().getTime()) {
            //last shoutout is too close, no shoutout
            LOG.info(channelName + " NO shoutout to " + user + ", too early.");
        } else {
            userShoutout = new Date();
            lastShoutouts.get(channelName).put(user, userShoutout);
            LOG.info("-> " + channelName + " shoutout to " + user);
//            clientHolder.getTwitchClient().getChat().sendMessage(channelName, "/shoutout " + user);
            clientHolder.getTwitchClient().getChat().sendMessage(channelName, "!so " + user);
            try {
                runtimeInfoHolder.storeRuntimeInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isUserRelevantForShoutout(String channelName, String user) {
        Optional<ConfigChannel> optionalConfigChannel = configHolder.getConfig().channels.stream().filter(configChannel -> configChannel.name.equalsIgnoreCase(channelName)).findFirst();
        if (optionalConfigChannel.isPresent() && !Objects.isNull(optionalConfigChannel.get().shoutouts)) {
            return optionalConfigChannel.get().shoutouts.contains(user);
        }
        return false;
    }

}
