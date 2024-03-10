package com.example;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;

import java.util.UUID;

@RequestScoped
@Getter
public class RequestId {
    private final UUID value = UUID.randomUUID();
}
