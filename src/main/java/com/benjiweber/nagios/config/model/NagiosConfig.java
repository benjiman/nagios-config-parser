package com.benjiweber.nagios.config.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class NagiosConfig {
    private final Map<DefineType, List<Define>> byType;
    private final List<Define> defines;

    private NagiosConfig(List<Define> defines, Map<DefineType, List<Define>> byType) {
        this.defines = defines;
        this.byType = byType;
    }

    public NagiosConfig(List<Define> defines) {
        this.defines = defines;
        this.byType = defines.stream().collect(groupingBy(Define::type));
    }

    public Stream<Define> stream() {
        return defines.stream();
    }

    public static NagiosConfig of(Define... defines) {
        return new NagiosConfig(Arrays.asList(defines));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NagiosConfig)) return false;

        NagiosConfig defines1 = (NagiosConfig) o;

        return defines != null ? defines.equals(defines1.defines) : defines1.defines == null;
    }

    @Override
    public int hashCode() {
        return defines != null ? defines.hashCode() : 0;
    }

    @Override
    public String toString() {
        return defines.toString();
    }

    public NagiosConfig ofType(DefineType type) {
        return new NagiosConfig(byType.get(type), mapOf(type, byType.get(type)));
    }

    private static <K,V> Map<K,V> mapOf(K key, V value) {
        return new HashMap<K,V>(){{ put (key, value); }};
    }
}
