package chaschev.lang;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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

}
