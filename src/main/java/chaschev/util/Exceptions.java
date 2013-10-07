package chaschev.util;

/**
 * User: chaschev
 * Date: 31/03/12
 */
public class Exceptions {

    public static RuntimeException runtime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException)e;
        }

        return new RuntimeException(e);
    }
    public static Error runtime(Error e) {
        return e;
    }
    public static RuntimeException runtime(Exception e) {
        if (RuntimeException.class.isAssignableFrom(e.getClass())) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    public static RuntimeException runtime(String message, Exception cause) {
        return new RuntimeException(message, cause);
    }
}
