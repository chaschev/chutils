package chaschev.lang.reflect;

import chaschev.util.Exceptions;

import java.lang.reflect.Constructor;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class ConstructorDesc<T> extends HavingMethodSignature {
    protected final Constructor<T> constructor;

    public ConstructorDesc(Constructor<T> constructor) {
        super(constructor.getParameterTypes());
        this.constructor = constructor;
        constructor.setAccessible(true);
    }

    public T newInstance(Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }
}
