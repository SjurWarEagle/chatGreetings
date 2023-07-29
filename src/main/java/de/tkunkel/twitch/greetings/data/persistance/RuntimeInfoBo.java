package de.tkunkel.twitch.greetings.data.persistance;

import java.util.Date;
import java.util.HashMap;

public class RuntimeInfoBo {
    public Date startedAt;
    /**
     * <channel, <User, Date>>
     */
    public HashMap<String, HashMap<String, Date>> lastGreeting = new HashMap<>();
    /**
     * <channel, <User, Date>>
     */
    private HashMap<String, HashMap<String, Date>> lastShoutout = new HashMap<>();

    public HashMap<String, HashMap<String, Date>> getLastShoutout() {
        return lastShoutout;
    }

    public void setLastShoutout(HashMap<String, HashMap<String, Date>> lastShoutout) {
        this.lastShoutout = lastShoutout;
    }
}
