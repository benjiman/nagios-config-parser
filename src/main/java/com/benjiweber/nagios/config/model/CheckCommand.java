package com.benjiweber.nagios.config.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CheckCommand implements Value {
    private String value;

    public CheckCommand(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckCommand)) return false;

        CheckCommand that = (CheckCommand) o;

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

    public String command() {
        return value.replaceAll("!.*","");
    }

    public List<String> args() {
        return Arrays.asList(value.replaceFirst(".*?!","").split("!"));
    }

    public String interpolate(String line) {
        String newLine = line;
        List<String> args = args();
        for (int i = 0; i < args.size(); i++) {
            newLine = newLine.replace("$ARG" + (i+1) + "$", args.get(i));
        }
        return newLine;
    }
}
