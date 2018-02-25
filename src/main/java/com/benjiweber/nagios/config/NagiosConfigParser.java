package com.benjiweber.nagios.config;

import com.benjiweber.nagios.config.model.NagiosConfig;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.*;
import src.main.antlr4.com.benjiweber.nagios.config.Config;
import src.main.antlr4.com.benjiweber.nagios.lexer.ConfigTokens;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class NagiosConfigParser {
    
    public static void main(String... args) throws Exception {

        System.out.println(
            parse(new File(args[0]))
        );

    }

    public static NagiosConfig parse(File file)  {
        return file.isDirectory()
            ? new NagiosConfig(Stream.of(
                file.listFiles((File dir, String name) -> name.endsWith(".cfg") || file.isDirectory()))
                    .flatMap(ignoringUnparsableFiles((File f) -> parse(f).stream()))
                    .collect(toList()))
            : new NagiosConfig(ignoringUnparsableFiles(__ -> parseFile(file).stream()).apply(file).collect(toList()));

    }

    interface ExceptionalFunction<T,R,E extends Exception> {
        R apply(T t) throws E;
    }

    public static <T,R,E extends Exception> Function<T,Stream<R>> ignoringUnparsableFiles(ExceptionalFunction<T, Stream<R>, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                return Stream.empty();
            }
        };
    }


    public static NagiosConfig parseFile(File file) throws InvalidNagiosConfigException {
        try {
            ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(file));
            return parse(input);
        } catch (IOException e) {
            throw new InvalidNagiosConfigException(e);
        }

    }

    public static NagiosConfig parse(ANTLRInputStream input) throws InvalidNagiosConfigException {
        ConfigTokens lexer = new ConfigTokens(input);
        lexer.removeErrorListeners();

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Config parser = new Config(tokens);
        parser.removeErrorListeners();

        ParseTree tree = parser.config();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new InvalidNagiosConfigException(parser.getNumberOfSyntaxErrors() + " syntax errors.");
        }

        return new NagiosConfig(tree.accept(new BuildJavaConfig()).collect(toList()));
    }

    public static NagiosConfig parse(String content) throws InvalidNagiosConfigException {
        try {
            return parse(new ANTLRInputStream(new ByteArrayInputStream((content.getBytes()))));
        } catch (IOException e) {
            throw new InvalidNagiosConfigException(e);
        }
    }

    static class InvalidNagiosConfigException extends Exception {
        public InvalidNagiosConfigException() {
        }

        public InvalidNagiosConfigException(String message) {
            super(message);
        }

        public InvalidNagiosConfigException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidNagiosConfigException(Throwable cause) {
            super(cause);
        }

        public InvalidNagiosConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
