package com.chaschev.lang.reflect;

import com.chaschev.util.Exceptions;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class ConstructorDesc<T> {
    protected final Constructor<T> constructor;
    protected final Class<?>[] params;

    public ConstructorDesc(Constructor constructor) {
        this.constructor = constructor;
        params = constructor.getParameterTypes();
    }

    public boolean matchesStrictly(Class... parameters) {
        return Arrays.equals(params, parameters);
    }

    public boolean matches(Object... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            if(!params[i].isAssignableFrom(parameters[i].getClass())){
                return false;
            }
        }

        return true;
    }

    public boolean matchesStrictly(Object... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            if(params[i] != parameters[i].getClass()){
                return false;
            }
        }

        return true;
    }

    public boolean matches(Class... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            if(!params[i].isAssignableFrom(parameters[i])){
                return false;
            }
        }

        return true;
    }

    private boolean checkLength(Object[] parameters) {
        return params.length == parameters.length;
    }

    public T newInstance(Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }
}
