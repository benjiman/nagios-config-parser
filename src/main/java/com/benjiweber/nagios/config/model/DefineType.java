package com.benjiweber.nagios.config.model;

public enum DefineType {
    service,
    servicegroup,
    servicedependency,
    serviceescalation,
    serviceextinfo,
    host,
    hostgroup,
    hostdependency,
    hostescalation,
    hostextinfo,
    command,
    contact,
    contactgroup,
    timeperiod,
    unknown;

    public static DefineType parse(String defineType) {
        try {
            return valueOf(defineType);
        } catch (IllegalArgumentException e) {
            return unknown;
        }
    }
}
