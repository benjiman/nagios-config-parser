package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.*;
import src.main.antlr4.com.benjiweber.nagios.config.Config;
import src.main.antlr4.com.benjiweber.nagios.lexer.ConfigTokens;

import java.io.FileInputStream;

public class NagiosConfig {
    public static void main(String... args) throws Exception {

        String filename = args[0];
        
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(filename));

        ConfigTokens lexer = new ConfigTokens(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Config parser = new Config(tokens);

        ParseTree tree = parser.config();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Failed to parse with " + parser.getNumberOfSyntaxErrors() + " errors");
            System.exit(parser.getNumberOfSyntaxErrors());
        }

        System.out.println(tree.toStringTree(parser)); 
    }

}
