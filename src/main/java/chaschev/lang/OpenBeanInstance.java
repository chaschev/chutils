package chaschev.lang;

import chaschev.lang.reflect.ClassDesc;
import chaschev.lang.reflect.ConstructorDesc;
import chaschev.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public final class OpenBeanInstance {
    private static final Logger logger = LoggerFactory.getLogger(OpenBeanInstance.class);

    OpenBean openBean;

    public Map<String, Object> putAll(Map<String, Object> dest, Object src) {
        return OpenBean.putAll(dest, src);
    }

    public Field getField(Object object, String fieldName) {
        return OpenBean.getField(object, fieldName);
    }

    public Object putAll(Object dest, Map<String, Object> src) {
        return OpenBean.putAll(dest, src);
    }

    public void copyFields(Object dest, Map src) {
        OpenBean.copyFields(dest, src);
    }

    public void copyFields(Object dest, Object src) {
        OpenBean.copyFields(dest, src);
    }

    public <T> T newInstance(Class<T> aClass, boolean strictly, Object... params) {
        return OpenBean.newInstance(aClass, strictly, params);
    }

    public void setField(Object object, String fieldName, Object value) {
        OpenBean.setField(object, fieldName, value);
    }

    public <T> T newInstance(Class<T> aClass, Object... params) {
        return OpenBean.newInstance(aClass, params);
    }

    public void copyFields(Object dest, Object src, OpenBean.CustomCopyHandler handler) {
        OpenBean.copyFields(dest, src, handler);
    }

    public <T> ConstructorDesc<T> getConstructorDesc(Class<T> aClass, Object... params) {
        return OpenBean.getConstructorDesc(aClass, params);
    }

    public <T> ConstructorDesc<T> getConstructorDesc(Class<T> aClass, boolean strictly, Object... params) {
        return OpenBean.getConstructorDesc(aClass, strictly, params);
    }

    public Object getOrInitCollection(Object object, String fieldName) {
        return OpenBean.getOrInitCollection(object, fieldName);
    }

    public void copyFields(Object dest, Map<String, ?> src, OpenBean.CustomCopyHandler handler) {
        OpenBean.copyFields(dest, src, handler);
    }

    public <T> ClassDesc<T> getClassDesc(Class<T> aClass) {
        try {
            return OpenBean.getClassDesc(aClass);
        } catch (Exception e) {
            logger.warn("", e);
            throw Exceptions.runtime(e);
        }
    }

    public <T> ConstructorDesc<T> getConstructorDesc(Class<T> aClass, boolean strictly, Class... classes) {
        try {
            return OpenBean.getConstructorDesc(aClass, strictly, classes);
        } catch (Exception e) {
            logger.warn("", e);
            throw Exceptions.runtime(e);
        }
    }

    public <T> ConstructorDesc<T> getConstructorDesc(Class<T> aClass, Class... classes) {
        try {
            return OpenBean.getConstructorDesc(aClass, classes);
        } catch (Exception e) {
            logger.warn("", e);
            throw Exceptions.runtime(e);
        }
    }

    public Object getStaticFieldValue(Class aClass, String name) {
        return OpenBean.getStaticFieldValue(aClass, name);
    }

    public Object getFieldValue(Object object, String fieldName) {
        return OpenBean.getFieldValue(object, fieldName);
    }

    public Object getStaticMethodValue(Class aClass, String name, Object... args) {
        return OpenBean.invoke(aClass, name, args);
    }
}
