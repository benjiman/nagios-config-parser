package com.benjiweber.nagios.config;

import com.benjiweber.nagios.config.NagiosConfigParser.InvalidNagiosConfigException;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.KeyValue;
import org.junit.Test;

import static com.benjiweber.nagios.config.NagiosConfigParser.parse;
import static com.benjiweber.nagios.config.model.DefineType.service;
import static com.benjiweber.nagios.config.model.DefineType.servicegroup;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ParseToDefineTest {

    @Test
    public void should_parse_to_listable_config() throws InvalidNagiosConfigException {
        assertEquals(
                parse("define service {\n" +
                        "        he lo\n" +
                        "\two rld\n" +
                        "}\n" +
                        "\n" +
                        "define servicegroup {\n" +
                        "   good bye\n" +
                        "\two rld\n" +
                        "}"),
                NagiosConfig.of(
                     new Define(service, asList(new KeyValue("he", "lo"), new KeyValue("wo", "rld"))),
                     new Define(servicegroup, asList(new KeyValue("good", "bye"), new KeyValue("wo", "rld")))
                )
        );
    }

    @Test
    public void should_be_able_to_ask_for_services() throws InvalidNagiosConfigException {
        NagiosConfig config = parse("define service {\n" +
                "        he lo\n" +
                "\two rld\n" +
                "}\n" +
                "define service {\n" +
                "        ab cd\n" +
                "}\n" +
                "define servicegroup {\n" +
                "   good bye\n" +
                "\two rld\n" +
                "}");

        assertEquals(
            config.ofType(service),
            NagiosConfig.of(
                new Define(service, asList(new KeyValue("he", "lo"), new KeyValue("wo", "rld"))),
                new Define(service, asList(new KeyValue("ab", "cd")))
            )
        );
    }

    @Test
    public void should_be_able_to_get_value_by_key() throws InvalidNagiosConfigException {
        Define define = new Define(service, asList(new KeyValue("he", "lo"), new KeyValue("wo", "rld")));
        assertEquals("lo", define.get("he").get().text());
        assertEquals("rld", define.get("wo").get().text());
    }


    @Test
    public void should_retain_spaces_in_descriptions() throws InvalidNagiosConfigException {
        assertEquals(
                parse("define service {\n" +
                        "        he lo there bar\n" +
                        "\two rld\n" +
                        "}"),
                NagiosConfig.of(
                    new Define(service, asList(new KeyValue("he", "lo there bar"), new KeyValue("wo", "rld")))
                )
        );
    }


}
