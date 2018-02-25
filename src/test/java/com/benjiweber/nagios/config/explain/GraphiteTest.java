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

public class GraphiteTest {
    Define serviceDefinition = new Define(service, emptyList());
    NagiosConfig config = new NagiosConfig(emptyList());

    @Test
    public void ignores_non_graphite_commands() {
        UnknownValue command = new UnknownValue("some_other_command with arguments");

        Optional<String> result = new Graphite().describe(command, serviceDefinition, config);

        assertEquals(Optional.empty(), result);
    }


    @Test
    public void gives_link_to_graphite() {
        UnknownValue command = new UnknownValue("/usr/lib/nagios/custom/check_graphite --metric \"collectd.some_domain.metric.submetric.value\" --critical 45 --warn 30 --status_on_error 3");

        Optional<String> result = new Graphite().describe(command, serviceDefinition, config);

        assertEquals(Optional.of(
            "This check is comparing the current value of a graphite query against some thresholds\n\n" +
            "Take a look at the graph at " + Config.GRAPHITE_PREFIX +"render/?width=588&height=310&_salt=1519563694.074&target=collectd.some_domain.metric.submetric.value"
        ), result);
    }

}