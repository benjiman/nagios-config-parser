package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.util.Optional;
import java.util.stream.Stream;

public interface CommandExplanation {
    CommandExplanation[] explainers = {
        new ScriptExecutedDirectly(),
        new NoClue()
    };

    static Optional<String> describeToMe(Value commandLine, NagiosConfig config) {
        return Stream.of(explainers)
        .map(explainer -> explainer.describe(commandLine, config))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();

    }

    Optional<String> describe(Value commandLine, NagiosConfig config) ;

}
