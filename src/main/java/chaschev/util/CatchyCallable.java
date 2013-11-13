package chaschev.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class CatchyCallable<V> implements Callable<V> {
    private static final Logger logger = LoggerFactory.getLogger(CatchyCallable.class);

    Callable<V> callable;
    boolean log = true;

    public CatchyCallable(Callable<V> callable) {
        this.callable = callable;
    }

    public CatchyCallable(Callable<V> callable, boolean log) {
        this.callable = callable;
        this.log = log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V call() throws Exception{
        try {
            return callable.call();
        } catch (Exception e) {
            if (log) {
                logger.warn("exception during run", e);
            }

            throw e;
        }
    }

}