package com.benjiweber.nagios.config.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Define {
    private final DefineType type;
    private final List<KeyValue> config;
    private final Map<String, Value> keyed;

    public DefineType type() {
        return type;
    }

    public List<KeyValue> config() {
        return config;
    }

    public Define(DefineType type, List<KeyValue> config) {
        this.type = type;
        this.config = config;
        this.keyed = config.stream().collect(toMap(KeyValue::key, KeyValue::value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Define)) return false;

        Define define = (Define) o;

        if (type != define.type) return false;
        return config != null ? config.equals(define.config) : define.config == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (config != null ? config.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return type + " {\n" +
                config.stream().map(Object::toString).map(s -> "\t" + s).collect(joining("\n")) + "\n" +
            '}';
    }

    public Optional<Value> get(String key) {
        return Optional.ofNullable(keyed.get(key));
    }
}
