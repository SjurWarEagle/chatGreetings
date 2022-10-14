package de.tkunkel.twitch.monitor.types.config;

public class ConfigEmote {
    public String name;
    public int triggerCnt;
    public int intervalInSeconds;
    public int coolDownInSeconds;

    @Override
    public String toString() {
        return "ConfigEmote{" +
                "name='" + name + '\'' +
                ", triggerCnt=" + triggerCnt +
                ", intervalInSeconds=" + intervalInSeconds +
                ", coolDownInSeconds=" + coolDownInSeconds +
                '}';
    }
}
