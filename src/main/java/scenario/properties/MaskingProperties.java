package scenario.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="app.logging", ignoreInvalidFields = true)
public class MaskingProperties implements Properties{
    private EnableLog enableLog;

    public enum EnableLog { OFF, FULL, MASK}
}
