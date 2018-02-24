package com.benjiweber.nagios.config.model;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class KeyValue {
    private final String key;
    private final String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String value() { return value; }
    public String key() { return key; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyValue)) return false;

        KeyValue keyValue = (KeyValue) o;

        if (key != null ? !key.equals(keyValue.key) : keyValue.key != null) return false;
        return value != null ? value.equals(keyValue.value) : keyValue.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    private static final int PADDING_CHARS = 40;
    @Override
    public String toString() {
        return key +
            IntStream.range(0,  PADDING_CHARS - key.length()).mapToObj(i -> " ").collect(joining())
        + value;
    }
}
