package scenario.context;

import scenario.logger.Logger;

import java.util.Map;

public interface Context<RQ, RS> {
    long getStartTime();
    Logger getLogger();
    long getTime();
    Map<String, String> getMapMdc();
    RQ getRg();
    RS getRs();
    String getServiceName();

    void setLogger();
    void setTime();
    void setMapMdc( Map<String, String> mapMdc);
    void setRg(RQ rq);
    void setRs(RS rs);
    void setServiceName(String serviceName);
}
