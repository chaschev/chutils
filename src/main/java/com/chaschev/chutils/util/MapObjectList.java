package com.chaschev.chutils.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: chaschev
 * Date: 6/9/13
 */

public interface MapObjectList extends Iterable<MapObjectBase> {
    Iterator<MapObjectBase> iterator();
    MapObjectBase get(int n);

    MapObjectMap having(
        String field1, Object value1);

    MapObjectMap having(
        String field1, Object value1,
        String field2, Object value2);

    int size();

    MapObjectBase dup();

    Collection<Object> asCollection();
}
