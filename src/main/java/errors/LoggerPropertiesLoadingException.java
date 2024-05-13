package errors;

public class LoggerPropertiesLoadingException extends RuntimeException {
    public LoggerPropertiesLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}

