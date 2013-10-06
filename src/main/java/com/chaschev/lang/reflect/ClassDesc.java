package com.chaschev.lang.reflect;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
* @author Andrey Chaschev chaschev@gmail.com
*/
public class ClassDesc<T> {
    private static final Map<Class, ClassDesc> CLASS_DESC_MAP = new HashMap<Class, ClassDesc>();
    Class<T> aClass;

    public static <T> ClassDesc<T> getClassDesc(Class<T> aClass) {
        ClassDesc result = CLASS_DESC_MAP.get(aClass);

        if (result == null) {
            result = new ClassDesc(aClass);
            synchronized (CLASS_DESC_MAP) {
                CLASS_DESC_MAP.put(aClass, result);
            }
        }

        return result;
    }

    //this is 1.5-2x faster than List
    public final Field[] fields;
    public final Field[] staticFields;
    public final Method[] methods;
    public final ConstructorDesc[] constructors;

    ClassDesc(Class aClass) {
        this.aClass = aClass;

        List<Field> tempFields = new ArrayList<Field>();
        List<Field> tempStaticFields = new ArrayList<Field>();
        List<Method> tempMethods = new ArrayList<Method>();

        Constructor[] constructors = aClass.getConstructors();
        this.constructors = new ConstructorDesc[constructors.length];

        for (int i = 0; i < constructors.length; i++) {
            Constructor constructor = constructors[i];
            this.constructors[i] = new ConstructorDesc(constructor);
        }

        while (aClass != Object.class) {
            Field[] fields = aClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    tempStaticFields.add(field);
                } else {
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

    public ConstructorDesc<T> getConstructorDesc(final boolean strictly, Class... parameters) {
        if (strictly) {
            for (ConstructorDesc constructor : constructors) {
                if (constructor.matchesStrictly(parameters)) {
                    return constructor;
                }
            }
        } else {
            for (ConstructorDesc constructor : constructors) {
                if (constructor.matches(parameters)) {
                    return constructor;
                }
            }
        }

        return null;
    }

    public ConstructorDesc<T> getConstructorDesc(final boolean strictly, Object... parameters) {
        if (strictly) {
            for (ConstructorDesc constructor : constructors) {
                if (constructor.matchesStrictly(parameters)) {
                    return constructor;
                }
            }
        } else {
            for (ConstructorDesc constructor : constructors) {
                if (constructor.matches(parameters)) {
                    return constructor;
                }
            }
        }

        return null;
    }

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
}
