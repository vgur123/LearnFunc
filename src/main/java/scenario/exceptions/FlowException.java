package scenario.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public abstract class FlowException extends RuntimeException{
    public FlowException(Throwable cause){
        super(cause);
    }
}
