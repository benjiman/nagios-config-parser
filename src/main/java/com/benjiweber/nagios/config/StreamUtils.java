package com.benjiweber.nagios.config;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface StreamUtils {
    interface ExceptionalFunction<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    public static <T, R, E extends Exception> Function<T,R> unchecked(ExceptionalFunction<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T,R> Function<T,Stream<R>> stream(Function<T, Optional<R>> optional) {
        return t -> optional.apply(t).map(Stream::of).orElseGet(Stream::empty);
    }

}
