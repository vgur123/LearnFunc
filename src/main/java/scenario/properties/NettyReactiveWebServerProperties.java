package scenario.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="netty", ignoreInvalidFields = true)
public class NettyReactiveWebServerProperties implements Properties{
    private int serverWorkerCount;
    private int serverSelectCount;
    private int serverMaxConnection;
    private int serverPendingAcquireMaxCount;
    private int clientWorkerCount;
    private int clientSelectCount;
    private int clientMaxConnection;
    private int clientPendingAcquireMaxCount;
}
