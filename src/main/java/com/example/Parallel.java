package com.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Stream;

public class Parallel {
    @Resource(lookup = "java:jboss/ee/concurrency/factory/default")
    private ManagedThreadFactory threadFactory;
    private ForkJoinPool forkJoinPool;

    @PostConstruct
    void init() {
        var parallelism = Runtime.getRuntime().availableProcessors();
        this.forkJoinPool = new ForkJoinPool(parallelism, threadFactory, null, false);
    }

    public <IN, OUT> OUT submit(Stream<IN> stream, Function<Stream<IN>, OUT> task) {
        try {
            return forkJoinPool.submit(() -> task.apply(stream.parallel())).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
