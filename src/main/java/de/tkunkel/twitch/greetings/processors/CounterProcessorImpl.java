package de.tkunkel.twitch.greetings.processors;

import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;
import org.springframework.stereotype.Component;

@Component
public class CounterProcessorImpl extends AbstractProcessor {
    @SuppressWarnings("FieldCanBeLocal")
    private final String SKULL = "\uD83D\uDC80";
    @SuppressWarnings("FieldCanBeLocal")
    private final String SKULL_WITH_BONES = "☠";

    public CounterProcessorImpl(ConfigHolder configHolder, ClientHolder clientHolder, RuntimeInfoHolder runtimeInfoHolder) {
        super(configHolder, clientHolder, runtimeInfoHolder);
    }

    @Override
    public String getUsageInfo() {
        return this.getClass().getName();
    }

    @Override
    public void process(String channelName, String user, String message) {
        if (channelName.equalsIgnoreCase("SjurWarEagle")
                && (message.startsWith(SKULL) || message.startsWith(SKULL_WITH_BONES))) {
        }
    }

    protected int extractCnt(String message) {
        String cntAsString = message.substring(2);
        int cnt;
        try {
            cnt = Integer.parseInt(cntAsString);
        } catch (Exception e) {
            cnt = 0;
        }
        return cnt;
    }

}
