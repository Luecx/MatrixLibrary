package core.exceptions;

public class NotMatchingSlotsException extends RuntimeException{

    public NotMatchingSlotsException(int s1, int s2) {
        super("this size: " + s1 +  "   other size: " + s2);
    }

}
