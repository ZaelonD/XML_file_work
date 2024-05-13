package errors;

public class InputDataException extends RuntimeException {
    public InputDataException(String errorMessage) {
        super(errorMessage);
    }
}
