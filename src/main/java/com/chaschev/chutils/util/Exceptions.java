package com.chaschev.chutils.util;

/**
 * User: chaschev
 * Date: 31/03/12
 */
public class Exceptions {

    public static RuntimeException runtime(Error e) {
        return new RuntimeException(e);
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
