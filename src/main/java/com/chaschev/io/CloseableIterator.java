package com.chaschev.io;

import java.io.Closeable;
import java.util.Iterator;

/**
* User: chaschev
* Date: 01/07/12
*/
public interface CloseableIterator<T> extends Iterator<T>, Closeable {


}
