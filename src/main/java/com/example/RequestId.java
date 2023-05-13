package com.example;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;

import java.util.UUID;

@RequestScoped
public class RequestId {
    @Getter
    private final UUID value = UUID.randomUUID();
}
