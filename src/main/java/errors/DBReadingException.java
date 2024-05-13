package errors;

public class DBReadingException extends RuntimeException {

    public DBReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
