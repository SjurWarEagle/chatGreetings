package de.tkunkel.twitch.greetings.data;

import com.google.gson.Gson;
import de.tkunkel.twitch.greetings.types.config.Config;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class ConfigHolder {
    private final Config config;

    public ConfigHolder() throws IOException {
        this.config = readConfig();
    }

    private static Config readConfig() throws IOException {
        Gson gson = new Gson();

        String configFile = System.getenv("CONFIG_FILE");
        if (Objects.isNull(configFile)) {
            throw new RuntimeException("Environment variable CONFIG_FILE not set");
        }

        return gson.fromJson(Files.readString(Paths.get(configFile)), Config.class);
    }


    public Config getConfig() {
        return config;
    }
}
