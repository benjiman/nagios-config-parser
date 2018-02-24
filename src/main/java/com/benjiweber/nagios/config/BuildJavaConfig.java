package com.benjiweber.nagios.config;

import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.DefineType;
import com.benjiweber.nagios.config.model.KeyValue;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;
import src.main.antlr4.com.benjiweber.nagios.config.Config;
import src.main.antlr4.com.benjiweber.nagios.config.ConfigBaseVisitor;
import src.main.antlr4.com.benjiweber.nagios.config.ConfigListener;
import src.main.antlr4.com.benjiweber.nagios.config.ConfigVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class BuildJavaConfig extends ConfigBaseVisitor<Stream<Define>> {

    @Override
    public Stream<Define> visitDefine(Config.DefineContext ctx) {
        return Stream.of(
            new Define(
                    DefineType.parse(ctx.type().getText()),
                    ctx.keyvalue().stream().map(this::toKeyValue).collect(toList())
            )
        );
    }

    private KeyValue toKeyValue(Config.KeyvalueContext ctx) {
        return new KeyValue(ctx.key().getText(), ctx.value().getText());
    }

    @Override
    protected Stream<Define> defaultResult() {
        return Stream.empty();
    }

    @Override
    protected Stream<Define> aggregateResult(Stream<Define> aggregate, Stream<Define> nextResult) {
        return concat(aggregate, nextResult);
    }


}
