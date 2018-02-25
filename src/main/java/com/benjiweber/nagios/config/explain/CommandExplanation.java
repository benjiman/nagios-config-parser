package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.util.Optional;
import java.util.stream.Stream;

public interface CommandExplanation {
    CommandExplanation[] explainers = {
        new ScriptExecutedDirectly(),
        new Nrpe(),
        new Graphite(),
        new Aggregate(),
        new Http(),
        new NoClue()
    };

    static Optional<String> describeToMe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        return Stream.of(explainers)
        .map(explainer -> explainer.describe(commandLine, serviceDefinition, config))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();

    }

    Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) ;

}
