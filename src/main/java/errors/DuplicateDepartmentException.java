package errors;

public class DuplicateDepartmentException extends RuntimeException{
    public DuplicateDepartmentException(String message) {
        super(message);
    }
}
