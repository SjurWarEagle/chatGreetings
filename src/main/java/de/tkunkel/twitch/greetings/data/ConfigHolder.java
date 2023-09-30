package de.tkunkel.twitch.greetings.data;

import com.google.gson.Gson;
import de.tkunkel.twitch.greetings.processors.ShoutoutProcessorImpl;
import de.tkunkel.twitch.greetings.types.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

import static java.nio.file.StandardWatchEventKinds.*;

@Component
public class ConfigHolder {
    private final Logger LOG = LoggerFactory.getLogger(ConfigHolder.class.getName());
    private Config config;

    public ConfigHolder() throws IOException {
        this.config = readConfig();
//        startFileChangedWatcher();
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

    private void startFileChangedWatcher() {
        String configFileName = System.getenv("CONFIG_FILE");

        if (!Objects.isNull(configFileName)) {
            Path configFile = Paths.get(configFileName);
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                Path dir = configFile.getParent();
                WatchKey key = dir.register(watcher, ENTRY_MODIFY);

                for (; ; ) {

                    // wait for key to be signaled
                    WatchKey keyToProcess;
                    try {
                        keyToProcess = watcher.take();
                    } catch (InterruptedException x) {
                        return;
                    }

                    for (WatchEvent<?> event : keyToProcess.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind != ENTRY_MODIFY) {
                            continue;
                        }
                        LOG.info("reloading config file");
                        WatchEvent<Path> ev = (WatchEvent<Path>)event;
                        Path filename = ev.context();
                        if (filename.getFileName().toString().equalsIgnoreCase(configFile.getFileName().toString())){
                            config = readConfig();
                        }
                    }

                    // Reset the key -- this step is critical if you want to
                    // receive further watch events.  If the key is no longer valid,
                    // the directory is inaccessible so exit the loop.
                    boolean valid = keyToProcess.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
