package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.Config;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.UnknownValue;
import org.junit.Test;

import java.util.Optional;

import static com.benjiweber.nagios.config.model.DefineType.service;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class HttpTest {
    Define serviceDefinition = new Define(service, emptyList());
    NagiosConfig config = new NagiosConfig(emptyList());

    @Test
    public void ignores_non_graphite_commands() {
        UnknownValue command = new UnknownValue("some_other_command with arguments");

        Optional<String> result = new Http().describe(command, serviceDefinition, config);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void gives_example_curl() {
        UnknownValue command = new UnknownValue("check_http -H foo.example.com -u /some/path --regex hello");

        Optional<String> result = new Http().describe(command, serviceDefinition, config);

        assertEquals(Optional.of("This check is looking for a HTTP response from http://foo.example.com/some/path containing ``hello''.\n\n" +
                "Perhaps check with a curl for yourself:\n\n" +
                "$ curl 'http://foo.example.com/some/path" + "'"), result);


    }

}