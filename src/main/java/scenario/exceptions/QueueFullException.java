package scenario.exceptions;

import lombok.Getter;

public class QueueFullException extends Throwable {
    @Getter
    private String nameThread;

    public QueueFullException(Throwable cause, String nameThread) {
        super(cause);
        this.nameThread=nameThread;
    }
}
