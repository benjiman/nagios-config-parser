package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;
import src.main.antlr4.com.benjiweber.nagios.config.Config;
import src.main.antlr4.com.benjiweber.nagios.config.ConfigListener;
import src.main.antlr4.com.benjiweber.nagios.config.ConfigVisitor;

public class BuildJavaConfig<T> implements ConfigVisitor<T> {

    @Override
    public T visitConfig(Config.ConfigContext ctx) {
        return null;
    }

    @Override
    public T visitKeyvalue(Config.KeyvalueContext ctx) {
        return null;
    }

    @Override
    public T visitDefine(Config.DefineContext ctx) {
        return null;
    }

    @Override
    public T visitType(Config.TypeContext ctx) {
        return null;
    }

    @Override
    public T visitKey(Config.KeyContext ctx) {
        return null;
    }

    @Override
    public T visitValue(Config.ValueContext ctx) {
        return null;
    }

    @Override
    public T visit(ParseTree parseTree) {
        return null;
    }

    @Override
    public T visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public T visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public T visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
