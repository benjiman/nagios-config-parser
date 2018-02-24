package com.benjiweber.nagios.config;

import com.benjiweber.nagios.config.NagiosConfig.InvalidNagiosConfigException;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.DefineType;
import com.benjiweber.nagios.config.model.KeyValue;
import org.junit.Test;

import java.util.Arrays;

import static com.benjiweber.nagios.config.model.DefineType.service;
import static com.benjiweber.nagios.config.model.DefineType.servicegroup;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ParseToDefineTest {

    @Test
    public void should_parse_to_list_of_defines() throws InvalidNagiosConfigException {
        assertEquals(
                NagiosConfig.parse("define service {\n" +
                        "        he lo\n" +
                        "\two rld\n" +
                        "}\n" +
                        "\n" +
                        "define servicegroup {\n" +
                        "   good bye\n" +
                        "\two rld\n" +
                        "}"),
                asList(
                     new Define(service, asList(new KeyValue("he", "lo"), new KeyValue("wo", "rld"))),
                     new Define(servicegroup, asList(new KeyValue("good", "bye"), new KeyValue("wo", "rld")))
                )
        );
    }
}
