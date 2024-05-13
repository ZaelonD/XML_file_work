package errors;

public class ConnectionErrorException extends RuntimeException {
    public ConnectionErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
