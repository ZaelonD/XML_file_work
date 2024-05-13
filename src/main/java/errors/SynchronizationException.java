package errors;

public class SynchronizationException extends RuntimeException{
    public SynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
