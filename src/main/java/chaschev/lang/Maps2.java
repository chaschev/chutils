package chaschev.lang;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Maps2 {
    public static <K,V> Map<K,V> newHashMap(Object... keyValues) {
        return newHashMap(Maps.newHashMapWithExpectedSize(keyValues.length / 2), keyValues);
    }

    public static <K,V> Map<K,V> newLinkedHashMap(Object... keyValues) {
        return newHashMap(Maps.newLinkedHashMap(), keyValues);
    }

    static <K,V> Map<K,V> newHashMap(HashMap<Object, Object> mapImpl, Object... keyValues){
        Preconditions.checkArgument(keyValues.length % 2 == 0, "parameters is a flat list of pairs");

        HashMap<Object,Object> map = mapImpl;

        for (int i = 0; i < keyValues.length; i+=2) {
            Object key = keyValues[i];
            Object value = keyValues[i + 1];

            map.put(key, value);
        }

        return (Map<K, V>) map;
    }
}
