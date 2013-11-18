package chaschev.util;

import chaschev.lang.LangUtils;
import sun.misc.Unsafe;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * User: chaschev
 * Date: 31/03/12
 */
public class Exceptions {
    private static final Unsafe UNSAFE = LangUtils.getUnsafe();

    public static void throwUnchecked(Throwable e) {
        if (e instanceof RuntimeException) {
            throw  (RuntimeException)e;
        }

        UNSAFE.throwException(e);
    }

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

    public static String toString(Throwable e){
        StringWriter sw = new StringWriter(e.getStackTrace().length * 64);
        e.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

    public static Throwable rootCause(Throwable e){
        while(e.getCause() != null){
            e = e.getCause();
        }

        return e;
    }
}
