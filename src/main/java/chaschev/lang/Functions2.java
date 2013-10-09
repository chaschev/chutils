package chaschev.lang;

import chaschev.lang.reflect.MethodDesc;
import chaschev.util.Exceptions;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Functions2 {
    @SuppressWarnings("unchecked")
    public static <K> Function<K, Object> field(Class<K> elementClass, String fieldName){
        final Field field = OpenBean.getClassDesc(elementClass).getField(fieldName);

        Preconditions.checkNotNull(field, "no such field: " + fieldName);

        return new Function<K, Object>() {
            public Object apply(@Nullable K input) {
                if(input == null){
                    return null;
                }else{
                    try {
                        return field.get(input);
                    } catch (IllegalAccessException e) {
                        throw Exceptions.runtime(e);
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <K> Function<K, Object> method(Class<K> elementClass, String methodName){
        MethodDesc methodDesc = OpenBean.getClassDesc(elementClass).getMethodDesc(methodName, true);

        Preconditions.checkNotNull(methodDesc, "no such method: " + methodName);

        final Method method = methodDesc.getMethod();

        return new Function<K, Object>() {
            public Object apply(@Nullable K input) {
                if(input == null){
                    return null;
                }else{
                    try {
                        return method.invoke(input);
                    } catch (Exception e) {
                        throw Exceptions.runtime(e);
                    }
                }
            }
        };
    }
}
