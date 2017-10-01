package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;

public class Config {
    public static void main(String... args) throws Exception {

        String filename = args[0];
        
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(filename));

        ConfigLexer lexer = new ConfigLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        ConfigParser parser = new ConfigParser(tokens);

        ParseTree tree = parser.config();
        
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Failed to parse with " + parser.getNumberOfSyntaxErrors() + " errors");
            System.exit(parser.getNumberOfSyntaxErrors());
        }

        System.out.println(tree.toStringTree(parser)); 
    }

}
