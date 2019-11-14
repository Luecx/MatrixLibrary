package core.exceptions;

public class NotEnoughSlotsException extends RuntimeException {


    public NotEnoughSlotsException(int actual, int required) {
        super(required + " required but only "+actual + "given");
    }
}
