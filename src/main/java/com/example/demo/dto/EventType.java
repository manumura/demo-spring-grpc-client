package com.example.demo.dto;

public enum EventType {

    CREATED("created"),
    UPDATED("updated"),
    DELETED("deleted");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
