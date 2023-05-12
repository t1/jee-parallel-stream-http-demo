package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class AddOne {
    @Inject
    @RestClient
    Api api;

    @RegisterRestClient(baseUri = "http://localhost:8080")
    public interface Api {
        @GET @Path("/{n}plus{m}")
        int plus(@PathParam("n") int n, @PathParam("m") int m);
    }

    public Integer to(Integer number) {
        return api.plus(number, 1);
    }
}
