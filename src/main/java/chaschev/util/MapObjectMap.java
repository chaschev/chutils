package chaschev.util;

import java.util.Map;
import java.util.Set;

/**
 * User: chaschev
 * Date: 6/9/13
 */

public abstract class MapObjectMap {
    public abstract MapObjectList newCollection(String path);
    public abstract MapObjectList gotoCollection(String path);

    public abstract MapObjectMap gotoMap(String path);

    public MapObjectMap newMap(String path){
        return dup().gotoMap(path);
    }

    public abstract Map<String, Object> asMap();

    public abstract Object get(String key);

    public abstract String getString(String key, String ifNull);

    public abstract MapObjectMap dup();

    public Set<String> keySet(){
        return asMap().keySet();
    }

    public abstract int size();

}
