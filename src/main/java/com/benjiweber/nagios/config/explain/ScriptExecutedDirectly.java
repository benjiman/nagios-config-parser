package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.NagiosConfig;
import com.benjiweber.nagios.config.model.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static com.benjiweber.nagios.config.StreamUtils.unchecked;
import static java.util.stream.Collectors.joining;

public class ScriptExecutedDirectly implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, NagiosConfig config) {
        try {
            String filename = commandLine.text().split(" ")[0];
            return Optional.of(Files.lines(new File(filename).toPath())
                .collect(joining("\n")));
        } catch (IOException e) {
            return Optional.empty();
        }

    }
}
