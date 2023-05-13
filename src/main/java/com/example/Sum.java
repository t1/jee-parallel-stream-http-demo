package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.concurrent.CompletionStage;

@Slf4j
public class Sum {
    @Inject
    @RestClient
    Api api;

    @Inject
    RequestId requestId;

    @RegisterRestClient(baseUri = "http://localhost:8080")
    public interface Api {
        @GET @Path("/{n}plus{m}")
        CompletionStage<Integer> futurePlus(@PathParam("n") int n, @PathParam("m") int m);
        @GET @Path("/{n}plus{m}")
        int plus(@PathParam("n") int n, @PathParam("m") int m);
    }

    public CompletionStage<Integer> futureOf(int left, int right) {
        log.info("sum of {} and {} [{}]", left, right, requestId.getValue());
        return api.futurePlus(left, right);
    }

    public int of(int left, int right) {
        log.info("sum of {} and {} [{}]", left, right, requestId.getValue());
        return api.plus(left, right);
    }
}
