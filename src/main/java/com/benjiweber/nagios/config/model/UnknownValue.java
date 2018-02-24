package com.benjiweber.nagios.config.model;

public class UnknownValue implements Value {
    private String value;

    public UnknownValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnknownValue)) return false;

        UnknownValue that = (UnknownValue) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String text() {
        return value;
    }
}
