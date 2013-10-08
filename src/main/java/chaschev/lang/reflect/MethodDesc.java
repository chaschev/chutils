package chaschev.lang.reflect;

import chaschev.util.Exceptions;

import java.lang.reflect.Method;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class MethodDesc extends HavingMethodSignature {
    protected final Method method;

    public MethodDesc(Method method) {
        super(method.getParameterTypes());
        this.method = method;
        method.setAccessible(true);
    }

    public Object invoke(Object[] params) {
        try {
            return method.invoke(params);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }
}
