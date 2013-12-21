package chaschev.lang.reflect;

import chaschev.util.Exceptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class MethodDesc<T> extends HavingMethodSignature {
    protected final Method method;

    public MethodDesc(Method method) {
        super(method.getParameterTypes());
        this.method = method;
        method.setAccessible(true);
    }

    public T invoke(Object object, Object... params) {
        try {
            return (T) method.invoke(object, params);
        }
        catch (InvocationTargetException e){
            throw Exceptions.runtime(e.getCause());
        }
        catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public final String getName() {
        return method.getName();
    }

    public Method getMethod() {
        return method;
    }
}
