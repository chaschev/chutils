package chaschev.util;

import java.util.Iterator;

/**
* User: chaschev
* Date: 6/25/13
*/
public abstract class MapVisitor {
    /**
     * @return false if ok, true if deleted
     */
    public boolean visitBefore(MapObjectMap map, Object parent, Iterator parentIterator){ return false;}
    public boolean visitBefore(MapObjectList list, Object parent, Iterator parentIterator){return false;}

    public void visitAfter(MapObjectMap map, Object parent, Iterator parentIterator){}
    public void visitAfter(MapObjectList list, Object parent, Iterator parentIterator){}
}
