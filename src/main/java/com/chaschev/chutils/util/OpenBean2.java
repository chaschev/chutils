package com.chaschev.chutils.util;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * User: chaschev
 * Date: 5/20/13
 */
public class OpenBean2 {
    private static final Comparator<Method> METHOD_COMPARATOR = new Comparator<Method>() {
        @Override
        public int compare(Method o1, Method o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    private static final Comparator<Field> FIELD_COMPARATOR = new Comparator<Field>() {
        @Override
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static Map<String, Object> putAll(Map<String, Object> dest, Object src) {
        try {
            for (Field field : OpenBean2.getClassDesc(src.getClass()).fields) {
                final Object v = field.get(src);

                dest.put(field.getName(), v);
            }

            return dest;
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Object putAll(Object dest, Map<String, Object> src) {
        try {
            for (Field field : OpenBean2.getClassDesc(dest.getClass()).fields) {
                final String name = field.getName();

                if(src.containsKey(name)){
                    final Object v = src.get(name);
                    field.set(dest, v);
                }
            }

            return dest;
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static class ClassDesc {
        Class aClass;

        //this is 1.5-2x faster than List
        public final Field[] fields;
        public final Field[] staticFields;
        public final Method[] methods;

        private ClassDesc(Class aClass) {
            this.aClass = aClass;

            List<Field> tempFields = new ArrayList<Field>();
            List<Field> tempStaticFields = new ArrayList<Field>();
            List<Method> tempMethods = new ArrayList<Method>();

            while (aClass != Object.class) {
                Field[] fields = aClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    final int modifiers = field.getModifiers();
                    if(Modifier.isStatic(modifiers)){
                        tempStaticFields.add(field);
                    }else{
                        tempFields.add(field);
                    }
                }

                final Method[] methods = aClass.getDeclaredMethods();

                for (Method method : methods) {
                    method.setAccessible(true);
                    tempMethods.add(method);
                }

                aClass = aClass.getSuperclass();
            }

            tempFields.toArray(this.fields = new Field[tempFields.size()]);
            tempStaticFields.toArray(this.staticFields = new Field[tempStaticFields.size()]);
            tempMethods.toArray(this.methods = new Method[tempMethods.size()]);

            Arrays.sort(this.fields, FIELD_COMPARATOR);
            Arrays.sort(this.staticFields, FIELD_COMPARATOR);
            Arrays.sort(this.methods, METHOD_COMPARATOR);

        }

        @SuppressWarnings("ForLoopReplaceableByForEach")
        @Nullable
        public Field getField(String name) {
            return findField(fields, name);
        }

        @SuppressWarnings("ForLoopReplaceableByForEach")
        @Nullable
        public Field getStaticField(String name) {
            return findField(staticFields, name);
        }

        @Nullable
        private static Field findField(Field[] array, String name) {
            final int n = array.length;

            for (int i = 0; i < n; i++) {
                Field field = array[i];
                if (field.getName().equals(name)) {
                    return field;
                }
            }

            return null;
        }

        public Method getMethod(String name) {
            final int n = methods.length;
            for (int i = 0; i < n; i++) {
                Method method = methods[i];
                if (method.getName().equals(name)) {
                    return method;
                }
            }

            return null;
        }
    }

    private static final Map<Class, ClassDesc> CLASS_DESC_MAP = new HashMap<Class, ClassDesc>();

    static final Map<Class, Map<String, Field>> fieldMap = new HashMap<Class, Map<String, Field>>();

    public static ClassDesc getClassDesc(Class aClass) {
        ClassDesc result = CLASS_DESC_MAP.get(aClass);

        if (result == null) {
            result = new ClassDesc(aClass);
            synchronized (CLASS_DESC_MAP) {
                CLASS_DESC_MAP.put(aClass, result);
            }
        }

        return result;
    }

    public static Object getFieldValue(Object object, String fieldName) {
        try {
            return getField(object, fieldName).get(object);
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Object getFieldValue2(Object object, String fieldName) {
        try {
            return getField2(object, fieldName).get(object);
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Object getOrInitCollection(Object object, String fieldName) {
        try {
            final Field collectionField = getField2(object, fieldName);

            final Class<?> aClass = collectionField.getType();

            Object value = collectionField.get(object);

            if (value == null) {
                if (List.class.isAssignableFrom(aClass)) {
                    value = new ArrayList();
                }else
                if (Set.class.isAssignableFrom(aClass)) {
                    value = new HashSet();
                }else
                if (Map.class.isAssignableFrom(aClass)){
                    value = new HashMap();
                }else{
                    throw new IllegalStateException("could not init collection/map for field: " + fieldName + " of class " + object.getClass());
                }

                collectionField.set(object, value);
            }

            return value;
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Field getField2(Object object, String fieldName) {
        return getClassDesc(object.getClass()).getField(fieldName);
    }

    public static void setField(Object object, String fieldName, Object value){
        try {
            getField2(object, fieldName).set(object, value);
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Field getField(Object object, String fieldName) {

        Class aClass = isClass(object) ? (Class) object : object.getClass();
        final Class<?> objectClass = aClass;

        Map<String, Field> map = fieldMap.get(objectClass);

        if (map != null) {
            Field field = map.get(fieldName);
            if (field != null) {
                return field;
            }
        } else {
            map = new HashMap<String, Field>(8);
            fieldMap.put(objectClass, map);
        }

        Field field = null;

        while (aClass != Object.class) {
            try {
                field = aClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                break;
            } catch (SecurityException e) {
                aClass = aClass.getSuperclass();
                //continue searching
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
                //continue searching
            }

        }

        if (field == null) {
            throw new IllegalArgumentException("didn't find field: " + fieldName + " in class " +
                Arrays.asList(aClass));
        }

        synchronized (fieldMap) {
            map.put(fieldName, field);
        }

        return field;
    }

    public static void copyFields(Object dest, Object src) {
        copyFields(dest, src, DEFAULT_HANDLER);
    }

    public static void copyFields(Object dest, Map src) {
        copyFields(dest, src, DEFAULT_HANDLER);
    }

    public static abstract class CustomCopyHandler{
        public boolean handle(Field field1, Field field2, Object dest, Object src, String name) throws Exception{
            return handle(field1, field2.get(dest), src, name);
        }

        public boolean handle(Field destField, Object srcValue, Object dest, String name) throws Exception{
            throw new UnsupportedOperationException("todo");
        }
    }

    private static final CustomCopyHandler DEFAULT_HANDLER = new CustomCopyHandler(){
        @Override
        public final boolean handle(Field field1, Field field2, Object dest, Object src, String name) throws Exception{
            field1.set(dest, field2.get(src));
            return true;
        }

        @Override
        public boolean handle(Field destField, Object dest, Object srcValue, String name) throws Exception {
            final Class<?> destClass = destField.getType();
            if(destClass.isEnum() && srcValue instanceof String){
                srcValue = Enum.valueOf((Class<Enum>)destClass, (String)srcValue);
            }
            try {
                destField.set(dest, srcValue);
            } catch (Exception e) {
                throw Exceptions.runtime(e);
            }
            return true;
        }
    };

    public static void copyFields(Object dest, Map<String,?> src, final CustomCopyHandler handler) {
        final ClassDesc destClassDesc = getClassDesc(dest.getClass());

        try {
            for (Map.Entry entry : src.entrySet()) {
                final String name = (String) entry.getKey();
                final Field destField = destClassDesc.getField(name);

                if(destField == null){
                    continue;
                }

                final Object srcValue = entry.getValue();

                if(!(handler != null && handler.handle(destField, dest, srcValue, name))){
                    DEFAULT_HANDLER.handle(destField, dest, srcValue, name);
                }
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public static void copyFields(Object dest, Object src, final CustomCopyHandler handler) {
        final ClassDesc destClassDesc = getClassDesc(dest.getClass());
        final ClassDesc srcClassDesc = getClassDesc(src.getClass());

        int i1 = 0, i2 = 0;

        final int l1 = destClassDesc.fields.length;
        final int l2 = srcClassDesc.fields.length;

        try {
            while (true) {
                final Field field1 = destClassDesc.fields[i1];
                final Field field2 = srcClassDesc.fields[i2];

                final String name1 = field1.getName();
                final String name2 = field2.getName();

                final int r = name1.compareTo(name2);

                if (r == 0) {
                    if (handler == null || !handler.handle(field1, field2, dest, src, name1)) {
                        DEFAULT_HANDLER.handle(field1, field2, dest, src, name1);
                    }

                    i1++;
                    i2++;

                    if (i1 >= l1 || i2 >= l2) break;
                } else if (r < 0) {
                    i1++;
                    if (i1 >= l1) break;
                } else {
                    i2++;
                    if (i2 >= l2) break;
                }
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    private static boolean isClass(Object object) {
        return object instanceof Class;
    }

    public static Object getStaticFieldValue(Class aClass, String name){
        try {
            final Field field = OpenBean2.getClassDesc(aClass).getStaticField(name);

            if(field == null){
                throw new RuntimeException("no such field '" + name + "' " + " in class " + aClass);
            }

            return field.get(aClass);
        } catch (IllegalAccessException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Object getStaticMethodValue(Class aClass, String name, Object... args){
        try {
            final Method field = OpenBean2.getClassDesc(aClass).getMethod(name);

            if(field == null){
                throw new RuntimeException("no such field '" + name + "' " + " in class " + aClass);
            }

            return field.invoke(aClass, args);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }
}
