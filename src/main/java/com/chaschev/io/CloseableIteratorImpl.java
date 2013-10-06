package com.chaschev.io;

import java.io.IOException;
import java.util.Iterator;

/**
 * User: chaschev
 * Date: 01/07/12
 */
public class CloseableIteratorImpl<T> implements CloseableIterator<T> {
    final Iterator<T> iterator;

    public CloseableIteratorImpl(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
