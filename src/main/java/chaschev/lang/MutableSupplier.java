package chaschev.lang;

import com.google.common.base.Supplier;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class MutableSupplier<T> implements Supplier<T> {
    T instance;

    boolean finalized;

    public MutableSupplier(T instance) {
        this.instance = instance;
    }

    public MutableSupplier() {
    }

    @Override
    public T get() {
        return instance;
    }

    public MutableSupplier<T> setInstance(T instance) {
        if(!finalized){
            this.instance = instance;
        }else{
            throw new IllegalStateException("can't change, already finalized");
        }
        return this;
    }

    public MutableSupplier<T> makeFinal(){
        finalized = true;
        return this;
    }
}
