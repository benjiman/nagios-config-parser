package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ConfigParseTest {

    @Test public void parse_simple_example() throws IOException {
        assertParse(
            "define ab {\n" +
                "        he lo\n" +
                "\two rld\n" +
            "}\n" +
            "\n" +
            "define ab {\n" +
            "   he lo\n" +
            "\two rld\n" +
            "}",

            "(config (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld) \\n }) \\n\\n (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld) \\n }))"
        );
    }

    private void assertParse(String input, String expected) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(new ByteArrayInputStream((input).getBytes()));

        ConfigLexer lexer = new ConfigLexer(inputStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ConfigParser parser = new ConfigParser(tokens);

        ParseTree tree = parser.config();
        assertEquals(expected, tree.toStringTree(parser));
    }

}