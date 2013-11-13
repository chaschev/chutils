package chaschev.lang;

import chaschev.lang.reflect.MethodDesc;
import chaschev.util.Exceptions;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Predicates2 {

    public static Predicate<String> contains(final CharSequence cs){
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains(cs);
            }
        };
    }

    public static <T> Predicate<T> functionAppliedEquals(final Function<T, ?> fun, final Object to){
        return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T input) {
                Object r = fun.apply(input);

                return objectsEqual(r, to);
            }
        };
    }

    public static <T> Predicate<T> functionAppliedNotEqual(final Function<T, ?> fun, final Object to){
        return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T input) {
                Object r = fun.apply(input);

                return !objectsEqual(r, to);
            }
        };
    }

    public static <T> Predicate<T> methodEquals(final String method, final Object to){
        return methodEquals(to, method);
    }

    public static <T> Predicate<T> methodEquals(final Object to, final String method, final Object... params){
        final MethodDesc[] desc = new MethodDesc[1];

        return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T input) {
                if(input == null) return false;

                if(desc[0] == null){
                    desc[0] = OpenBean.getClassDesc(input.getClass()).getMethodDesc(method, false, params);
                    if(desc[0] == null){
                        throw new IllegalArgumentException("no such method: " + method);
                    }
                }

                Object r = desc[0].invoke(input, params);

                return objectsEqual(r, to);
            }
        };
    }

    public static <T> Predicate<T> fieldEquals(final String field, final Object to){
        final Field[] desc = new Field[1];

        return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T input) {
                if(input == null) return false;

                if(desc[0] == null){
                    desc[0] = OpenBean.getClassDesc(input.getClass()).getField(field);
                    if(desc[0] == null){
                        throw new IllegalArgumentException("no such field: " + field);
                    }
                }

                Object r = null;
                try {
                    r = desc[0].get(input);
                } catch (IllegalAccessException e) {
                    throw Exceptions.runtime(e);
                }

                return objectsEqual(r, to);
            }
        };
    }

    private static boolean objectsEqual(Object a, Object b) {
        if(a == null && b == null) return true;
        if(a == null) return false;

        return a.equals(b);
    }
}
