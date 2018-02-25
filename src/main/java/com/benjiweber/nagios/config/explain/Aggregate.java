package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.Config;
import com.benjiweber.nagios.config.model.CheckCommand;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.net.URLEncoder;
import java.util.Optional;

public class Aggregate implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        if (!commandLine.text().contains("check_aggregate")) {
            return Optional.empty();
        }

        String interpolated = serviceDefinition
            .get("check_command")
            .flatMap(cmd -> cmd.tryCast(CheckCommand.class))
            .map(cmd -> cmd.interpolate(commandLine.text()))
            .orElse(commandLine.text());

        String aggregateTerm = interpolated.replaceFirst(".*? .*? \"","").replaceFirst("\".*","");
        
        return Optional.of(
        "This check is aggregating the results of any other checks that match the term ``"+aggregateTerm+"''. If any of these individual checks fail, then so does the aggregate\n\n" +
                "Try searching for the individual checks in Thruk: " + Config.THRUK_PREFIX +  "#cgi-bin/status.cgi?hidesearch=0" +
                "&hidetop=&dfl_columns=&dfl_s0_hoststatustypes=15&dfl_s0_servicestatustypes=31&dfl_s0_hostprops=0" +
                "&dfl_s0_serviceprops=0&style=detail&dfl_s0_type=service&dfl_s0_val_pre=&dfl_s0_op==" +
                "&dfl_s0_value=" + URLEncoder.encode(aggregateTerm) +
                "&dfl_s0_value_sel=5&hoststatustypes=15&servicestatustypes=28"
        );

    }
}
