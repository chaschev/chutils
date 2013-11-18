package chaschev.lang;

import chaschev.lang.reflect.ClassDesc;
import chaschev.lang.reflect.ConstructorDesc;
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

    public static <T> ObjectMethod<T> constructor(final Class<T> aClass, final Class... params){
        return new ObjectMethod<T>() {
            private final ConstructorDesc<T> constructor = OpenBean.getClassDesc(aClass).getConstructorDesc(false, params);

            @Override
            public T create(Object... params) {
                return constructor.newInstance(params);
            }
        };
    }

    public static <T> ObjectMethod<T> method(final Object obj, final String method, final Class... params){
        return method(obj, method, false, params);
    }

    public static <T> ObjectMethod<T> method(final Object obj, final String method, final boolean strictly, final Class... params){
        return new ObjectMethod<T>() {
            private final MethodDesc methodDesc = OpenBean.getClassDesc(obj.getClass()).getMethodDesc(method, strictly, params);

            @Override
            public T create(Object... params) {
                return (T) methodDesc.invoke(obj, params);
            }
        };
    }

    public static <T> ObjectMethod<T> dynamicConstructor(final Class<T> aClass){
        return new ObjectMethod<T>() {
            private final ClassDesc<T> classDesc = OpenBean.getClassDesc(aClass);

            @Override
            public T create(Object... params) {
                return classDesc.getConstructorDesc(false, params).newInstance(params);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <OBJECT, FIELD> Function<OBJECT, FIELD> field(final String fieldName){
        final Field[] field = new Field[1];

        return new Function<OBJECT, FIELD>() {
            public FIELD apply(@Nullable OBJECT input) {
                if(input == null){
                    return null;
                }else{
                    Field _field = field[0];

                    if(_field == null){
                        _field = OpenBean.getClassDesc(input.getClass()).getField(fieldName);

                        Preconditions.checkNotNull(_field, "no such field: " + fieldName);

                        field[0] = _field;
                    }

                    try {
                        return (FIELD) _field.get(input);
                    } catch (Exception e) {
                        throw Exceptions.runtime(e);
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <OBJECT, RETURN> Function<OBJECT, RETURN> method(final String methodName){
        final Method[] method = new Method[1]; 

        return new Function<OBJECT, RETURN>() {
            public RETURN apply(@Nullable OBJECT input) {
                if(input == null){
                    return null;
                }else{
                    Method _method = method[0];

                    if(_method == null){
                        MethodDesc methodDesc = OpenBean.getClassDesc(input.getClass()).getMethodDesc(methodName, true);

                        Preconditions.checkNotNull(methodDesc, "no such method: " + methodName);

                        _method = method[0] = methodDesc.getMethod();
                    }
                    
                    try {
                        return (RETURN) _method.invoke(input);
                    } catch (Exception e) {
                        throw Exceptions.runtime(e);
                    }
                }
            }
        };
    }
}
