package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@Slf4j
@Path("/")
public class Calculator {

    @RegisterRestClient(baseUri = "http://localhost:8080")
    public interface Api {
        @GET @Path("/{n}plus{m}")
        int add(@PathParam("n") int n, @PathParam("m") int m);
    }

    @Inject
    Parallel parallel;

    @Inject
    @RestClient
    Api api;

    @GET @Path("/{n}plus{m}")
    public int addOne(@PathParam("n") int n, @PathParam("m") int m) throws InterruptedException {
        Thread.sleep(500L + n * 100L);
        var result = n + m;
        log.info("{} plus {} is {}", n, m, result);
        return result;
    }


    @GET @Path("/sum")
    public Integer sum() {
        var start = Instant.now();
        log("start", start);
        var sum = parallel.submit(() -> Stream.of(1, 2)
            .parallel()
            .peek(number -> log("call " + number, start))
            .map(number -> api.add(number, 1))
            .peek(result -> log("got " + result, start))
            .mapToInt(Integer::intValue)
            .sum());
        log("sum " + sum, start);
        return sum;
    }

    private static void log(String message, Instant start) {
        log.info(message + " [" + Duration.between(start, Instant.now()) + "]");
    }
}
