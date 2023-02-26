package scenario.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="application.availability", ignoreInvalidFields = true)
public class ApplicationAvailabilityProperties implements Properties{
    private int maxRequestActiveReadiness;
    private int maxDbQueue;
}
