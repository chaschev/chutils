package chaschev.lang;

import chaschev.lang.reflect.MethodDesc;
import chaschev.util.Exceptions;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Predicates2 {
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

                if(r == null && to == null) return true;
                if(r == null) return false;

                return r.equals(to);
            }
        };
    }

    public static <T> Predicate<T> fieldEquals(final Object to, final String field){
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

                if(r == null && to == null) return true;
                if(r == null) return false;

                return r.equals(to);
            }
        };
    }
}
