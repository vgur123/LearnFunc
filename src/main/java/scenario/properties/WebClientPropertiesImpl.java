package scenario.properties;

import lombok.Data;

import java.time.Duration;

@Data
public class WebClientPropertiesImpl implements WebClientProperties{
    private String path;
    private Duration timeout;
    private Integer bufferLimit;
    private String connectionType;
}
