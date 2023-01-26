package exception;

public class DuplicateIdException extends Exception{
    public DuplicateIdException(String userId) {
        super(String.format("%s is a duplicate Id.", userId));
    }
}
