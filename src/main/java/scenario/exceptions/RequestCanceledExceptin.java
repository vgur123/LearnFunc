package scenario.exceptions;

public class RequestCanceledExceptin extends RuntimeException{
    public static final RequestCanceledExceptin instance = new RequestCanceledExceptin();

    public RequestCanceledExceptin(){}
}
