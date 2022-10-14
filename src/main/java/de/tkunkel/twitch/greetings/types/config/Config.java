import java.util.List;

public class Config {

    public String accessToken;
    public List<ConfigChannel> channels;

    @Override
    public String toString() {
        return "Config{" +
                "accessToken='" + accessToken + '\'' +
                ", channels=" + channels +
                '}';
    }
}
