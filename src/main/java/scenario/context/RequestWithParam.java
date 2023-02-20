package scenario.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestWithParam<RQ, RQP> {
    private RQ request;
    private RQP requestParam;
}
