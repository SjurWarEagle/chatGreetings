package de.tkunkel.twitch.greetings.data;

import com.google.gson.Gson;
import de.tkunkel.twitch.greetings.data.persistance.RuntimeInfoBo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class RuntimeInfoHolder {
    private final RuntimeInfoBo runtimeInfo;
    private static String configFile;
    private static final Gson gson = new Gson();

    public RuntimeInfoHolder() throws IOException {
        configFile = System.getenv("RUNTIME_INFO_FILE");
        if (Objects.isNull(configFile)) {
            throw new RuntimeException("Environment variable RUNTIME_INFO_FILE not set");
        }
        this.runtimeInfo = readRuntimeInfo();

    }

    private RuntimeInfoBo readRuntimeInfo() throws IOException {
        String json = Files.readString(Paths.get(configFile));
        RuntimeInfoBo runtimeInfoBo = gson.fromJson(json, RuntimeInfoBo.class);
        if (Objects.isNull(runtimeInfoBo)) {
            return new RuntimeInfoBo();
        } else {
            return runtimeInfoBo;
        }
    }

    public RuntimeInfoBo getRuntimeInfo() {
        return runtimeInfo;
    }

    public void storeRuntimeInfo() throws IOException {
        String json = gson.toJson(runtimeInfo);
        Files.write(Paths.get(configFile), json.getBytes());
    }
}

