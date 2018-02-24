package com.benjiweber.nagios.config;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import src.main.antlr4.com.benjiweber.nagios.config.Config;
import src.main.antlr4.com.benjiweber.nagios.lexer.ConfigTokens;

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

    @Test public void comment_char_in_value() throws IOException {
        assertParse(
                "    define service {\n" +
                        "        use                             some-base-definition\n" +
                        "        service_description             Blah Blah blah blah - http://example.com/blah#fragment\n" +
                        "    }\n",

                "(config (define define (type service) { \\n (key use) (value some-base-definition) \\n (key service_description) (value Blah Blah blah blah - http://example.com/blah#fragment) \\n }) \\n)"
        );
    }


    @Test public void skips_block_singleline_comments() throws IOException {
        assertParse(
                "#######\n" +
                "#\n" +
                "# this is a comment\n" +
                "#\n" +
                "#######\n" +
                        "define ab {\n" +
                        "        he lo\n" +
                        "#\two rld\n" +
                        "}\n" +
                        "\n" +
                        "define ab {\n" +
                        "   he lo\n" +
                        "\two rld\n" +
                        "}",

                "(config (define define (type ab) { \\n (key he) (value lo) \\n \\n }) \\n\\n (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld) \\n }))"
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

                "(config (define define (type ab) { \\n (key he) (value lo) \\n \\n }) \\n\\n (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld) \\n }))"
        );
    }

    @Test public void handles_pipe_redirection() throws IOException {
        assertParse(
                "define ab {\n" +
                        "        he lo\n" +
                        "#\two rld\n" +
                        "}\n" +
                        "\n" +
                        "define ab {\n" +
                        "   he lo\n" +
                        "\two rld >> /dev/null\n" +
                        "}",

                "(config (define define (type ab) { \\n (key he) (value lo) \\n \\n }) \\n\\n (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value rld >> /dev/null) \\n }))"
        );
    }

    @Test public void skips_midline_comments() throws IOException {
        assertParse(
                "define ab {\n" +
                        "        he lo\n" +
                        "\two r;ld\n" +
                        "}",

                "(config (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value r) \\n }))"
        );
    }

    @Test public void handles_lack_of_whitespace() throws IOException {
        assertParse(
                "define ab{\n" +
                        "        he lo\n" +
                        "\two r\n" +
                        "}",

                "(config (define define (type ab) { \\n (key he) (value lo) \\n (key wo) (value r) \\n }))"
        );
    }


    private void assertParse(String input, String expected) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(new ByteArrayInputStream((input).getBytes()));

        ConfigTokens lexer = new ConfigTokens(inputStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Config parser = new Config(tokens);

        ParseTree tree = parser.config();
        assertEquals(expected, tree.toStringTree(parser));
    }

}