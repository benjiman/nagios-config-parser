package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.Define;
import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class ScriptExecutedDirectly implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        try {
            File file = new File(commandLine.text().split(" ")[0]);

            if (!file.exists()) {
                return Optional.empty();
            }

            return Optional.of(Files.lines(file.toPath())
                .collect(joining("\n")));
        } catch (IOException e) {
            return Optional.empty();
        }

    }
}
