package com.benjiweber.nagios.config.explain;

import com.benjiweber.nagios.config.model.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Nrpe implements CommandExplanation {
    @Override
    public Optional<String> describe(Value commandLine, Define serviceDefinition, NagiosConfig config) {
        if (!commandLine.text().contains("check_nrpe")) {
            return Optional.empty();
        }

        String withArgs = serviceDefinition
            .get("check_command")
            .flatMap(cmd -> cmd.tryCast(CheckCommand.class))
            .map(cmd -> cmd.interpolate(commandLine.text()))
            .orElse(commandLine.text());

        String commandName = withArgs.replaceAll(".*-c (.*?) .*","$1");

        String host =
            serviceDefinition
                .getAsText("host_name")
                .flatMap(h -> addressFromHostname(h, config))
                .orElseGet(() -> {
                    String hostgroup = serviceDefinition.getAsText("hostgroup_name").orElse("unknown_host");
                    return anyHostFromHostgroup(hostgroup, config);
                });

        return Optional.of(
            "This command ``"+commandName+"'' is executed remotely\n\n" +
            "Try invoking it manually with \n\n$ " + withArgs.replaceAll("\\$USER1\\$/","").replaceAll("\\$HOSTADDRESS\\$", host)  + "\n\n" +
            "Or finding out what is executed on the remote host with: \n\n" +
            "$ ssh " + host +" fgrep " + commandName + " /etc/nagios/nrpe.cfg"


        );
    }

    private String anyHostFromHostgroup(String hostgroup, NagiosConfig config) {
        return config.ofType(DefineType.hostgroup)
                .stream()
                .filter(hg -> Objects.equals(hostgroup, hg.getAsText("hostgroup_name").orElse("unknown_name")))
                .map(hg -> hg.get("members"))
                .flatMap(optMembers -> optMembers.map(members -> Arrays.stream(members.text().split(","))).orElse(Stream.empty()))
                .findFirst()
                .flatMap(hostname -> addressFromHostname(hostname, config))
                .orElse("unknownhost.example.com");
    }

    private Optional<String> addressFromHostname(String hostname, NagiosConfig config) {
        return config
                .ofType(DefineType.host)
                .stream()
                .filter(h -> Objects.equals(hostname, h.get("host_name").map(Value::text).orElse("unknown_host")))
                .map(h -> h.get("address").map(Value::text).orElse("unknown_host"))
                .findFirst();
    }
}
