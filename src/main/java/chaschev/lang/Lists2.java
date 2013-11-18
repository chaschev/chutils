package chaschev.lang;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Lists2 {
    public static <F, T> List<T> projectField(List<F> fromList, Class<F> elClass, Class<T> fieldClass, String name) {
        Function<F, T> field = (Function<F, T>) Functions2.field(name);
        return Lists.transform(fromList, field);
    }

    public static <F, T> List<T> projectMethod(List<F> fromList, Class<F> elClass, Class<T> methodClass, String name) {
        Function<F, T> method = (Function<F, T>) Functions2.method(name);
        return Lists.transform(fromList, method);
    }

    public static <F, T> List<T> projectField(List<F> fromList, String name) {
        F nonNull = Iterables.find(fromList, Predicates.notNull(), null);

        if(nonNull == null){
            if(fromList.isEmpty()){
                return Collections.emptyList();
            }else{
                return Collections.nCopies(fromList.size(), null);
            }
        }

        Class aClass = nonNull.getClass();

        return projectField(fromList, aClass, null, name);
    }

    public static <F, T> List<T> projectMethod(List<F> fromList, String name) {
        F nonNull = Iterables.find(fromList, Predicates.notNull(), null);

        if(nonNull == null){
            if(fromList.isEmpty()){
                return Collections.emptyList();
            }else{
                return Collections.nCopies(fromList.size(), null);
            }
        }

        Class aClass = nonNull.getClass();

        return projectMethod(fromList, aClass, null, name);
    }

    public static <T> List<T> removeDupsInSortedList(List<T> list) {
        int w = 0;
        for (int r = 0, listSize = list.size(); r < listSize; r++) {
            if (r == 0) {
                w++;
                continue;
            }

            if (!list.get(r).equals(list.get(r - 1))) {
                list.set(w, list.get(r));
                w++;
            }
        }

        for (int last = list.size() - 1; last >= w; last--) {
            list.remove(last);
        }

        return list;
    }

    public static <T> List<T> newFilledArrayList(int size, T value) {
        List<T> list = Lists.newArrayListWithExpectedSize(size);

        for (int i = 0; i < size; i++) {
            list.add(value);
        }

        return list;
    }

    public static <T> List<T> nInstances(int n, Supplier<T> supplier){
        List<T> list = Lists.newArrayListWithCapacity(n);

        for(int i = 0; i < n; i++){
            list.add(supplier.get());
        }

        return list;
    }

    public static <T> List<T> computingList(final int n, final Function<Integer, T> function){
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                return function.apply(index);
            }

            @Override
            public int size() {
                return n;
            }
        };
    }
}
