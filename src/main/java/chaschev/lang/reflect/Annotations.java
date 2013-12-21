package chaschev.lang.reflect;

import chaschev.util.Exceptions;

import java.lang.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Annotations {
    public static Object defaultValue(Annotation a, String methodName){
        try {
            Method method = a.annotationType().getMethod(methodName);
            return method.getDefaultValue();
        } catch (java.lang.NoSuchMethodException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static String defaultString(Annotation a, String methodName){
        return (String) defaultValue(a, methodName);
    }

    public static boolean defaultBoolean(Annotation a, String methodName){
        return (Boolean) defaultValue(a, methodName);
    }

    public static int defaultInt(Annotation a, String methodName){
        return (Integer) defaultValue(a, methodName);
    }
}
