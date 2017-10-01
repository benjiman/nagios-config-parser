package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ConfigParseApprovalTest {

    @Test public void simple_example() throws IOException {
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

    @Test public void special_characters_in_values() throws IOException {
        assertParse(
                "    define service {\n" +
                        "        use                             some-base-definition\n" +
                        "        service_description             Blah Blah blah blah - http://example.com/blah\n" +
                        "        host_name                       a-host-name\n" +
                        "        service_groups                  a-service-group\n" +
                        "        check_command                   checkCommandName!\"blah(blah(blah(a.b.c-d*_example.com.{p99,m1_rate},2,'multiplySeries')),sumSeries(a.b.abc-eu*_example_com.com.example.a.b.a-b-time.m1_rate))\"!200!250!3\n" +
                        "    }\n",

                "(config (define define (type service) { \\n (key use) (value some-base-definition) \\n (key service_description) (value Blah Blah blah blah - http://example.com/blah) \\n (key host_name) (value a-host-name) \\n (key service_groups) (value a-service-group) \\n (key check_command) (value checkCommandName!\"blah(blah(blah(a.b.c-d*_example.com.{p99,m1_rate},2,'multiplySeries')),sumSeries(a.b.abc-eu*_example_com.com.example.a.b.a-b-time.m1_rate))\"!200!250!3) \\n }) \\n)"
        );
    }

    @Test public void skips_comments() throws IOException {
        assertParse(
                "define ab {\n" +
                        "        he lo\n" +
                        "#\two rld\n" +
                        "}\n" +
                        "\n" +
                        "define ab {\n" +
                        "   he lo\n" +
                        "\two rld\n" +
                        "}",

                "(config (define define (type ab) { \\n (key he) (value lo) \\n }) \\n\\n (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld) \\n }))"
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