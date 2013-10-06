package com.chaschev.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: chaschev
 * Date: 11/8/11
 */
public class BoundedExecutorService implements ExecutorService {
    private final ExecutorService executor;
    private final Semaphore semaphore;

    public BoundedExecutorService(ExecutorService executor, int bound) {
        this.executor = executor;
        this.semaphore = new Semaphore(bound);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public Future<?> submit(final Runnable command)
        throws RejectedExecutionException {
        try {
            semaphore.acquire();
            return executor.submit(new Runnable() {
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        } catch (InterruptedException ignore) {
        }
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        try {
            semaphore.acquire();
            return executor.submit(new Callable<T>() {
                public T call() throws Exception {
                    try {
                        return task.call();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        } catch (InterruptedException ignore) {
        }
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public void execute(Runnable command) {
        submit(command);
    }

    public ThreadFactory getThreadFactory(){
        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
            return threadPoolExecutor.getThreadFactory();
        }else {
            throw new UnsupportedOperationException("todo implemented reflective call");
        }
    }
}