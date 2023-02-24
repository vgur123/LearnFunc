package scenario.properties;

import lombok.Data;

@Data
public class ExternalServiceProperties implements Properties{
    private String path;
    private long timeout;
    private int bufferLimit;
    private String contentType;
    private String error5xxServer;
    private int responseStatusTimeout;
    private String externalRequest;
    private String ext;
    private String folder;
}
