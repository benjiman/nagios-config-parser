package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.Config;
import com.benjiweber.nagios.config.model.CheckCommand;
import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.util.Optional;

public class Graphite implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        if (!commandLine.text().contains("check_graphite")) {
            return Optional.empty();
        }

        String withArgs = serviceDefinition
                .get("check_command")
                .flatMap(cmd -> cmd.tryCast(CheckCommand.class))
                .map(cmd -> cmd.interpolate(commandLine.text()))
                .orElse(commandLine.text());

        String metricName = withArgs.replaceAll(".*--metric \"(.*?)\" .*","$1");

        return Optional.of(
            "This check is comparing the current value of a graphite query against some thresholds\n\n" +
            "Take a look at the graph at http://"+ Config.GRAPHITE_DOMAIN +"/render/?width=588&height=310&_salt=1519563694.074&target=" + metricName
        );
    }
}
