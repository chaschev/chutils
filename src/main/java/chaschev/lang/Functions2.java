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
