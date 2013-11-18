package chaschev.lang;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public abstract class ObjectMethod<T> implements Function<Object[], T>, Supplier<T> {
    public abstract T create(Object... params);

    @Override
    public final T apply(Object... input) {
        return create(input);
    }

    @Override
    public T get() {
        return create();
    }
}
