package chaschev.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatchyRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CatchyRunnable.class);

    Runnable runnable;
    boolean log = true;
    boolean propagate = true;

    public CatchyRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public CatchyRunnable(Runnable runnable, boolean log, boolean propagate) {
        this.runnable = runnable;
        this.log = log;
        this.propagate = propagate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            if (log) {
                logger.warn("exception during run", e);
            }

            if(propagate){
                throw Exceptions.runtime(e);
            }
        }
    }

}