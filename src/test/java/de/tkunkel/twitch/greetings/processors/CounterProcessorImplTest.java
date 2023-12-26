package de.tkunkel.twitch.greetings.processors;

import de.tkunkel.twitch.greetings.data.ClientHolder;
import de.tkunkel.twitch.greetings.data.ConfigHolder;
import de.tkunkel.twitch.greetings.data.RuntimeInfoHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class CounterProcessorImplTest {
    @Test
    public void extractCnt() throws IOException {

        ConfigHolder configHolder = new ConfigHolder();
        CounterProcessorImpl counterProcessor = new CounterProcessorImpl(configHolder, new ClientHolder(configHolder), new RuntimeInfoHolder());
        String message = "💀+5";
        int cnt = counterProcessor.extractCnt(message);
        Assertions.assertEquals(5, cnt);

        message = "💀+1";
        cnt = counterProcessor.extractCnt(message);
        Assertions.assertEquals(1, cnt);

        message = "💀-50";
        cnt = counterProcessor.extractCnt(message);
        Assertions.assertEquals(-50, cnt);

        message = "💀-99";
        cnt = counterProcessor.extractCnt(message);
        Assertions.assertEquals(-99, cnt);

        message = "💀=";
        cnt = counterProcessor.extractCnt(message);
        Assertions.assertEquals(0, cnt);

    }

}
