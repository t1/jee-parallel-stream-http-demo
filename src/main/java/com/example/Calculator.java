package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Slf4j
@Path("/")
public class Calculator {

    @Inject
    RequestId requestId;

    @Inject
    Sum sum;

    @Inject
    Parallel parallel;

    @GET @Path("/{n}plus{m}")
    public int plus(@PathParam("n") int n, @PathParam("m") int m) throws InterruptedException {
        Thread.sleep(500L + n * 100L);
        var result = n + m;
        log.info("{} plus {} is {} [{}]", n, m, result, requestId.getValue());
        return result;
    }


    @GET @Path("/sum{n}")
    public Integer sum(@PathParam("n") int n) {
        var start = Instant.now();
        Consumer<String> logger = message -> log.info(message + " [" + Duration.between(start, Instant.now()) + "] " + parallel);
        log.info("start " + n + " ------------------------------------------");
        // ### sequential stream with intermediate collection [this works]
        // %%% parallel stream with unmanaged threads [this fails as expected]
        // $$$ parallel stream with managed threads [this should work but fails]
        var result =
                //parallel.submit( // $$$
                IntStream.rangeClosed(1, n).boxed()
                        // .parallel() // %%%
                        // , stream -> stream // $$$
                        .peek(number -> logger.accept("call " + number))
                        // .map(number -> sum.of(number, 1)) // $$$ %%%
                        .map(number -> sum.futureOf(number, 1)) // ###
                        .toList().stream() // ###
                        .map(Parallel::get) // ###
                        .peek(sum -> logger.accept("got " + sum))
                        .mapToInt(Integer::intValue)
                        .sum()
                //) // $$$
                ;
        logger.accept("sum " + result);
        return result;
    }
}
