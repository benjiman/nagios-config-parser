package com.benjiweber.nagios.config.model;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface Value {
    static Value parse(String key, String value) {
        return
            Objects.equals("check_command", key)
                ? new CheckCommand(value)
                : new UnknownValue(value);
    }

    String text();

    default <T extends Value, R> Optional<R> when(Class<T> cls, Function<T,R> f) {
        return getClass().isAssignableFrom(cls)
            ? Optional.of(f.apply((T)this))
            : Optional.empty();
    }
}
