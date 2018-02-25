package com.benjiweber.nagios.config;

import com.benjiweber.nagios.config.model.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.benjiweber.nagios.config.model.DefineType.service;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Explain {

    public static void main(String... args) {
        String serviceDescription = args[0];
        String configLocation = args[1];

        NagiosConfig config = NagiosConfigParser.parse(new File(configLocation));

        List<Define> matches = config
                .ofType(service)
                .stream()
                .filter(define -> searchMatch(define.get("service_description"), serviceDescription))
                .collect(toList());

        if (matches.size() > 1) {
            System.out.println(
                green("Did you mean one of: \n") +
                matches.stream()
                    .flatMap(match -> match.config().stream().filter(description))
                    .map(keyvalue -> keyvalue.value().text())
                    .map(s -> "\t" + s)
                    .collect(joining("\n")));
        } else {
            System.out.println(describe(matches.get(0), config));
        }

    }

    private static boolean searchMatch(Optional<Value> candidateValue, String b) {
        return candidateValue.map(a -> a.text().toLowerCase().contains(b.toLowerCase())).orElse(false);
    }

    private static String describe(Define define, NagiosConfig   entireConfig) {
        String serviceDescription = define
                .get("service_description")
                .map(value -> value.text())
                .orElse("no description");

        Optional<CheckCommand> command = define
            .get("check_command")
            .flatMap(kv -> kv.tryCast(CheckCommand.class));

        Optional<Define> commandDefinition = command.flatMap(
            cmd -> entireConfig
                .ofType(DefineType.command)
                .stream()
                .filter(def -> searchMatch(def.get("command_name"), cmd.command()))
                .findAny()
        );

        Optional<Value> commandLine = commandDefinition
                .flatMap(c -> c.get("command_line"));

        Optional<String> fullCommand = command.flatMap(cmd -> commandLine.map(line -> cmd.interpolate(line.text())));

        Optional<String> commandContents = commandLine
                .map(Value::text)
                .map(cmd -> cmd.split(" ")[0])
                .map(unchecked(filename -> Files.lines(new File(filename).toPath())
                .collect(joining("\n"))));

        return fullCommand.map(c -> green("Service:") + "\n" + serviceDescription + "\n\n" + green("Executes:") + "\n" + c + commandContents.map(contents -> green("\n\n====== Start Contents of " + scriptName(commandLine) + " ======\n") + contents + green("\n====== End Contents of "+scriptName(commandLine)+" ======") ).orElse("")).orElse("Unable to find a command definition :(");
    }

    private static String green(String s) {
        return "\033[32;1m"+s+"\033[0m";
    }

    private static String scriptName(Optional<Value> commandLine) {
        return commandLine.map(cml -> cml.text().replaceAll(" .*","")).orElse("Script");
    }

    interface ExceptionalFunction<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    private static <T, R, E extends Exception> Function<T,R> unchecked(ExceptionalFunction<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static <T,R> Function<T,Stream<R>> stream(Function<T, Optional<R>> optional) {
        return t -> optional.apply(t).map(Stream::of).orElseGet(Stream::empty);
    }

    private static Predicate<KeyValue> description = forKey("service_description");

    private static Predicate<KeyValue> forKey(String key) {
         return keyvalue -> Objects.equals(key, keyvalue.key());
    }

    private static Predicate<KeyValue> valueEquals(String s) {
        return keyvalue -> keyvalue.value().text().equals(s);
    }
    
}
