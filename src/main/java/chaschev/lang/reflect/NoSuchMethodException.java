package chaschev.lang.reflect;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class NoSuchMethodException extends RuntimeException {
    public NoSuchMethodException() {
    }

    public NoSuchMethodException(String message) {
        super(message);
    }

    public NoSuchMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
