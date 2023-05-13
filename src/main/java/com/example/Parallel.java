package com.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    @Override public String toString() {
        var string = forkJoinPool.toString(); // not all info in here is publicly available
        return string.substring(string.indexOf('[')); // we're not interested in the class name & id
    }

    public static <T> T get(CompletionStage<T> completionStage) {
        try {
            return completionStage.toCompletableFuture().get(10, SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
