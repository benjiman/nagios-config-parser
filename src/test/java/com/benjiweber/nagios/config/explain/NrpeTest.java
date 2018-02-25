package com.benjiweber.nagios.config.explain;


import com.benjiweber.nagios.config.model.*;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static com.benjiweber.nagios.config.model.DefineType.host;
import static com.benjiweber.nagios.config.model.DefineType.hostgroup;
import static com.benjiweber.nagios.config.model.DefineType.service;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class NrpeTest  {

    @Test public void ignores_non_nrpe_commands() {
        UnknownValue command = new UnknownValue("some_other_command with arguments");
        Define serviceDefinition = new Define(service, emptyList());
        NagiosConfig config = new NagiosConfig(emptyList());


        Optional<String> result = new Nrpe().describe(command, serviceDefinition, config);

        assertEquals(Optional.empty(), result);
    }

    @Test public void gives_us_an_example_remote_command() {
        UnknownValue command = new UnknownValue("$USER1$/check_nrpe -t 120 -H $HOSTADDRESS$ -c $ARG1$ -a 2 1");
        Define serviceDefinition = new Define(service, asList(new KeyValue("check_command", "check_nrpe!check_something_or_other")));
        NagiosConfig config = new NagiosConfig(emptyList());


        Optional<String> result = new Nrpe().describe(command, serviceDefinition, config);

        assertEquals(Optional.of("This command ``check_something_or_other'' is executed remotely\n" +
                "\n" +
                "Try invoking it manually with \n" +
                "\n" +
                "$ check_nrpe -t 120 -H unknownhost.example.com -c check_something_or_other -a 2 1\n" +
                "\n" +
                "Or finding out what is executed on the remote host with: \n" +
                "\n" +
                "$ ssh unknownhost.example.com fgrep check_something_or_other /etc/nagios/nrpe.cfg"), result);
    }


    @Test public void looks_up_host() {
        UnknownValue command = new UnknownValue("$USER1$/check_nrpe -t 120 -H $HOSTADDRESS$ -c $ARG1$ -a 2 1");
        Define serviceDefinition = new Define(service, asList(
            new KeyValue("check_command", "check_nrpe!check_something_or_other"),
            new KeyValue("host_name", "servicehost")
        ));
        NagiosConfig config = new NagiosConfig(asList(new Define(host,
                asList(
                        new KeyValue("host_name", "servicehost"),
                        new KeyValue("address", "servicehost.example.com")
                )
        )));


        Optional<String> result = new Nrpe().describe(command, serviceDefinition, config);

        assertEquals(Optional.of("This command ``check_something_or_other'' is executed remotely\n" +
                "\n" +
                "Try invoking it manually with \n" +
                "\n" +
                "$ check_nrpe -t 120 -H servicehost.example.com -c check_something_or_other -a 2 1\n" +
                "\n" +
                "Or finding out what is executed on the remote host with: \n" +
                "\n" +
                "$ ssh servicehost.example.com fgrep check_something_or_other /etc/nagios/nrpe.cfg"), result);
    }


    @Test public void looks_up_hostgroup() {
        UnknownValue command = new UnknownValue("$USER1$/check_nrpe -t 120 -H $HOSTADDRESS$ -c $ARG1$ -a 2 1");

        Define serviceDefinition = new Define(service, asList(
                new KeyValue("check_command", "check_nrpe!check_something_or_other"),
                new KeyValue("hostgroup_name", "somehostgroup")
        ));

        NagiosConfig config = new NagiosConfig(
            asList(
                new Define(hostgroup,
                    asList(
                        new KeyValue("hostgroup_name", "anotherhostgroup"),
                        new KeyValue("members", "abc,def")
                    )
                ),
                new Define(hostgroup,
                    asList(
                        new KeyValue("hostgroup_name", "somehostgroup"),
                        new KeyValue("members", "groupedhost,anotherhost")
                    )
                ),new Define(host,
                    asList(
                        new KeyValue("host_name", "groupedhost"),
                        new KeyValue("address", "groupedhost.example.com")
                    )
                )
            ));


        Optional<String> result = new Nrpe().describe(command, serviceDefinition, config);

        assertEquals(Optional.of("This command ``check_something_or_other'' is executed remotely\n" +
                "\n" +
                "Try invoking it manually with \n" +
                "\n" +
                "$ check_nrpe -t 120 -H groupedhost.example.com -c check_something_or_other -a 2 1\n" +
                "\n" +
                "Or finding out what is executed on the remote host with: \n" +
                "\n" +
                "$ ssh groupedhost.example.com fgrep check_something_or_other /etc/nagios/nrpe.cfg"), result);
    }
}