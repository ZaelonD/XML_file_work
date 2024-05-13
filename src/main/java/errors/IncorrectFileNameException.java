package errors;

public class IncorrectFileNameException extends RuntimeException {
    public IncorrectFileNameException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public IncorrectFileNameException(String errorMessage) {
        super(errorMessage);
    }
}
