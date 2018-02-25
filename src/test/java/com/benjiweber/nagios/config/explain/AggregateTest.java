package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.Config;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.KeyValue;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.UnknownValue;
import org.junit.Test;

import java.util.Optional;

import static com.benjiweber.nagios.config.model.DefineType.service;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class AggregateTest {


    @Test
    public void ignores_non_aggregate_commands() {
        Define serviceDefinition = new Define(service, emptyList());
        NagiosConfig config = new NagiosConfig(emptyList());
        UnknownValue command = new UnknownValue("some_other_command with arguments");

        Optional<String> result = new Aggregate().describe(command, serviceDefinition, config);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void gives_link_to_thruk_search_for_sub_checks() {
        Define serviceDefinition = new Define(service, asList(new KeyValue("check_command", "check_aggregate!Security updates")));
        NagiosConfig config = new NagiosConfig(emptyList());
        UnknownValue command = new UnknownValue("/usr/lib/nagios/custom/check_aggregate_for_team.sh ateam \"$ARG1$\" 1 \"http://example.com/12345\"");

        Optional<String> result = new Aggregate().describe(command, serviceDefinition, config);

        assertEquals(Optional.of(
            "This check is aggregating the results of any other checks that match the term ``Security updates''. If any of these individual checks fail, then so does the aggregate\n\n" +
            "Try searching for the individual checks in Thruk: " + Config.THRUK_PREFIX +  "#cgi-bin/status.cgi?hidesearch=0&hidetop=&dfl_columns=&dfl_s0_hoststatustypes=15&dfl_s0_servicestatustypes=31&dfl_s0_hostprops=0&dfl_s0_serviceprops=0&style=detail&dfl_s0_type=service&dfl_s0_val_pre=&dfl_s0_op==&dfl_s0_value=Security+updates&dfl_s0_value_sel=5&hoststatustypes=15&servicestatustypes=28"
        ), result);
    }

}