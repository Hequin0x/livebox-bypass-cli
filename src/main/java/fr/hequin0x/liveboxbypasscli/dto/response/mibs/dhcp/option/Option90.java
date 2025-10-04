package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Option90(@JsonProperty("Value") String dhcpv6Value) {

    public String dhcpv4Value() {
        return this.dhcpv6Value.replaceAll("(.{2})(?=.)", "$1:");
    }

    public String dhcpv6Value() {
        return this.dhcpv6Value.toUpperCase();
    }
}
