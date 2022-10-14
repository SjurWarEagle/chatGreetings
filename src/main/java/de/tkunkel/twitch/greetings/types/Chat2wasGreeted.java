package de.tkunkel.twitch.greetings.types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Chat2wasGreeted {

    public Map<String, Map<String, Long>> chat2wasGreeted = new HashMap<>();

    public void markAsGreeted(String chat, String user) {
        chat2wasGreeted.putIfAbsent(chat, new HashMap<>());
        Map<String, Long> chatGreetings = chat2wasGreeted.get(chat);
        chatGreetings.put(user, new Date().getTime());
    }

    public boolean wasGreetedToday(String chat, String user) {
        Map<String, Long> chatGreetings = chat2wasGreeted.getOrDefault(chat, new HashMap<>());
        long lastGreeting = chatGreetings.getOrDefault(user, 0L);
        long now = new Date().getTime();
        long yesterday = now - TimeUnit.DAYS.toMillis(1);

        return lastGreeting > yesterday;
    }
}
