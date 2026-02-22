package backend;

public class EmptyLabException extends RuntimeException {

    public EmptyLabException() {
        super();
    }

    public EmptyLabException(String message) {
        super(message);
    }
}
