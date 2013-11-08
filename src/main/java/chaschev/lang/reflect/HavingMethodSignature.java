package chaschev.lang.reflect;

import com.google.common.primitives.Primitives;

import java.util.Arrays;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public abstract class HavingMethodSignature {
    protected final Class<?>[] params;

    public HavingMethodSignature(Class<?>[] params) {
        this.params = params;
    }

    public boolean matchesStrictly(Class... parameters) {
        return Arrays.equals(params, parameters);
    }

    public boolean matches(Object... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            final boolean matches = _matches(parameters[i].getClass(), params[i]);

            if(!matches) {
                return false;
            }
        }

        return true;
    }

    public boolean matchesStrictly(Object... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            if (params[i] != parameters[i].getClass()) {
                return false;
            }
        }

        return true;
    }

    public boolean matches(Class... parameters) {
        if (!checkLength(parameters)) return false;

        for (int i = 0, length = parameters.length; i < length; i++) {
            final boolean matches = _matches(parameters[i], params[i]);

            if(!matches) {
                return false;
            }
        }

        return true;
    }

    private static boolean _matches(Class inputParam, Class<?> declaredParam) {
        boolean matches = true;
        if (!declaredParam.isAssignableFrom(inputParam)) {
            if(
                (inputParam.isPrimitive() && Primitives.wrap(inputParam) == declaredParam) ||
                (declaredParam.isPrimitive() && Primitives.wrap(declaredParam) == inputParam)
                ){
            }else{
                matches = false;
            }
        }
        return matches;
    }

    private boolean checkLength(Object[] parameters) {
        return params.length == parameters.length;
    }

    public abstract String getName();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128)
            .append(getName()).append("(");
        int l = params.length;

        for (int i = 0; i < l; i++) {
            Class<?> param = params[i];
            sb.append(param.getSimpleName());
            if (i != l - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        boolean isMethod = this instanceof MethodDesc;

        if (isMethod) sb.append(":");

        sb.append(isMethod ? (((MethodDesc) this).method.getReturnType()
            .getSimpleName()) : "");

        return sb.toString();
    }
}
