package com.chaschev.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapObjectBase extends MapObjectMap implements MapObjectList {
    public Object obj;

    public MapObjectBase() {
    }

    public MapObjectBase(Object obj) {
        reuse(obj);
    }

    public MapObjectBase reuse(Object obj) {
        this.obj = obj;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<MapObjectBase> iterator() {
        if(obj == null){
            return Iterators.emptyIterator();
        }

        final Iterator iterator = ((Iterable) obj).iterator();
        final MapObjectBase nav = new MapObjectBase();
        return Iterators.transform(iterator, new Function<Object, MapObjectBase>() {
            @Override
            public MapObjectBase apply(Object input) {
                return nav.reuse(input);
            }
        });
    }

    public MapObjectBase get(int n){
        obj = ((List)obj).get(n);
        return this;
    }

    @Override
    public MapObjectList newCollection(String path){
        return dup().gotoCollection(path);
    }

    @Override
    public MapObjectList gotoCollection(String path){
        if(obj == null) return this;

        obj = MapObjectUtils.pathToCollection((Map<String, Object>) obj, path);
        return this;
    }

    @Override
    public MapObjectMap gotoMap(String path){
        obj = MapObjectUtils.pathToMap((Map<String, Object>) obj, path);
        return this;
    }

    @Override
    public MapObjectMap having(
        String field1, Object value1) {

        if(obj == null) return this;

        obj = MapObjectUtils.having((Collection)obj, field1, value1);

        return this;
    }

    @Override
    public MapObjectMap having(
        String field1, Object value1,
        String field2, Object value2) {

        if(obj == null) return this;

        obj = MapObjectUtils.having((Collection)obj, field1, value1, field2, value2);

        return this;
    }

    @Override
    public int size() {
        if(obj == null) return 0;
        if (isCollection()) {
            return ((Collection)obj).size();
        }else
        if (isMap()) {
            return ((Map)obj).size();
        }

        return 0;
    }

    public boolean isMap() {
        return obj instanceof Map;
    }

    public final Object value(){
        return obj;
    }

    @Override
    public final Map<String, Object> asMap(){
        return (Map<String, Object>) obj;
    }

    @Override
    public final Collection<Object> asCollection(){
        return (Collection<Object>) obj;
    }

    public final MapObjectMap asNavMap(){
        return this;
    }

    public final MapObjectList asNavCollection(){
        return this;
    }

    @Override
    public Object get(String key){
        if(obj == null) return this;

        return asMap().get(key);
    }

    @Override
    public String getString(String key, String ifNull) {
        Object o = get(key);

        if(o == null || o instanceof MapObjectBase) return ifNull;

        return o.toString();
    }

    @Override
    public MapObjectBase dup() {
        return new MapObjectBase(obj);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MapObjectNav{");
        sb.append("obj=").append(obj);
        sb.append('}');
        return sb.toString();
    }

    public void acceptVisitor(MapVisitor mapVisitor){
        acceptVisitor(mapVisitor, null, null);
    }

    protected void acceptVisitor(MapVisitor mapVisitor, Object parent, Iterator<Object> parentIterator){
        final boolean isCollection = isCollection();
        final boolean wasRemoved;

        if(isCollection){
            wasRemoved = mapVisitor.visitBefore((MapObjectList) this, parent, parentIterator);
        }else{
            wasRemoved = mapVisitor.visitBefore((MapObjectMap) this, parent, parentIterator);
        }

        final MapObjectBase thisMapObj = this;
        final Object thisObj = thisMapObj.obj;

        Iterator<Object> iterator = isCollection ?
            asCollection().iterator() :
            asMap().values().iterator();

        for (; iterator.hasNext(); ) {
            Object o = iterator.next();
            if (o instanceof Collection) {
                thisMapObj.obj = o;
                thisMapObj.acceptVisitor(mapVisitor, thisObj, iterator);
            } else if (o instanceof Map) {
                thisMapObj.obj = o;
                thisMapObj.acceptVisitor(mapVisitor, thisObj, iterator);
            }
        }

        thisMapObj.obj = thisObj;

        if (!wasRemoved) {
            if (isCollection) {
                mapVisitor.visitAfter((MapObjectList) this, parent, parentIterator);
            } else {
                mapVisitor.visitAfter((MapObjectMap) this, parent, parentIterator);
            }
        }
    }

    public boolean isCollection() {
        return obj instanceof Collection;
    }
}
