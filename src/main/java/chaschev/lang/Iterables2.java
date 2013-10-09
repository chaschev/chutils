package chaschev.lang;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.List;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Iterables2 {
    public static <F, T> Iterable<T> projectField(Iterable<F> fromIterable, Class<F> elClass, Class<T> fieldClass, String name) {
        Function<F, T> field = (Function<F, T>) Functions2.field(elClass, name);
        return Iterables.transform(fromIterable, field);
    }

    public static <F, T> Iterable<T> projectField(Iterable<F> fromIterable, String name) {
        List<T> r = null;

        F nonNull = Iterables.find(fromIterable, Predicates.notNull(), null);

        if(nonNull == null) {
            if (Iterables.isEmpty(fromIterable)) {
                r = Collections.emptyList();
            } else {
                r = Collections.singletonList(null);
            }
        }

        if(r != null) return r;

        Class aClass = nonNull.getClass();

        return projectField(fromIterable, aClass, null, name);
    }

    public static <F, T> Iterable<T> projectMethod(Iterable<F> fromIterable, Class<F> elClass, Class<T> methodReturnClass, String name) {
        Function<F, T> field = (Function<F, T>) Functions2.method(elClass, name);
        return Iterables.transform(fromIterable, field);
    }

    public static <F, T> Iterable<T> projectMethod(Iterable<F> fromIterable, String name) {
        List<T> r = null;

        F nonNull = Iterables.find(fromIterable, Predicates.notNull(), null);

        if(nonNull == null) {
            if (Iterables.isEmpty(fromIterable)) {
                r = Collections.emptyList();
            } else {
                r = Collections.nCopies(Iterables.size(fromIterable), null);
            }
        }

        if(r != null) return r;

        Class aClass = nonNull.getClass();

        return projectMethod(fromIterable, aClass, null, name);
    }
}
