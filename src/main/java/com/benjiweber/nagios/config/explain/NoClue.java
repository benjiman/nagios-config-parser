package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.util.Optional;

public class NoClue implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, NagiosConfig config) {
        return Optional.of("I have no clue what " + commandLine.text() + " means :(");
    }
}
