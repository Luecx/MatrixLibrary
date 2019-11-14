package core.exceptions;

public class NotSupportedOperation extends RuntimeException {

    public NotSupportedOperation() {
        super("This method is not supported!");
    }
}
