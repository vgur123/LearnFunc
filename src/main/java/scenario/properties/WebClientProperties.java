package scenario.properties;

import java.time.Duration;

public interface WebClientProperties extends Properties{
    String getPath();
    Duration getTimeout();
    Integer getBufferLimit();
    String getConnectionType();
}
