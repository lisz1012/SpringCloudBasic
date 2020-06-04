package com.lisz.test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class TraceContextAwareExecutorService extends AbstractExecutorService {
    private ExecutorService underlying;
    private Record record;

    public TraceContextAwareExecutorService(ExecutorService underlying, Record record) {
        this.underlying = underlying;
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    @Override
    public void shutdown() {
        underlying.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return underlying.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return underlying.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return underlying.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(Runnable command) {

    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        // 这里也可以写一些对于record的操作
        return underlying.invokeAll(tasks);
    }

    public Callable<String> getCurrentSegment() {
        return () -> {
            return "abc";
        };
    }
}
