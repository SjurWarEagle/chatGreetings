package de.tkunkel.twitch.greetings.types;

import java.util.HashMap;
import java.util.Map;

public class Chats {

    public Map<String, Long> chat2wasGreeted = new HashMap<>();

    public void markAsGreeted(String chat, String user) {
        //fixme implement
    }
}
