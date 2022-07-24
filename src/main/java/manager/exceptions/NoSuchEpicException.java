package manager.exceptions;

public class NoSuchEpicException extends RuntimeException {
    public NoSuchEpicException(String msg) {
        super(msg);
    }
}
