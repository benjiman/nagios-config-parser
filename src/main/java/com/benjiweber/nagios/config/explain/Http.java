package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.util.Optional;

public class Http implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        if (!commandLine.text().contains("check_http")) {
            return Optional.empty();
        }

        String host = commandLine.text().replaceFirst(".*?-H ","").replaceFirst(" .*","");
        String url = host + commandLine.text().replaceFirst(".*-u ","").replaceFirst(" .*","");
        String match = commandLine.text().replaceFirst(".*--regex ","");

        return Optional.of(
            "This check is looking for a HTTP response from http://" + url + " containing ``" + match + "''.\n\n" +
            "Perhaps check with a curl for yourself:\n\n" +
            "$ curl 'http://"+ url + "'"
        );

    }
}
