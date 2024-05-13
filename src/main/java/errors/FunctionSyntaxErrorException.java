package errors;

public class FunctionSyntaxErrorException extends RuntimeException {
    public FunctionSyntaxErrorException(String message) {
        super(message);
    }
}
