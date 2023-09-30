package de.tkunkel.twitch.greetings.types.config;

import java.util.List;

public class ConfigChannel {

    public String name;
    public String fileForLinks;

    public List<ConfigCommand> commands;

    public List<ConfigGreeting> greetings;
    public List<ConfigTexts> texts;
    public List<String> shoutouts;

}
