package de.tkunkel.twitch.monitor.types.config;

import java.util.List;

public class ConfigChannel {

    public String name;
    public List<ConfigEmote> emotes;

    @Override
    public String toString() {
        return "ConfigChannel{" +
                "name='" + name + '\'' +
                ", emotes=" + emotes +
                '}';
    }
}
