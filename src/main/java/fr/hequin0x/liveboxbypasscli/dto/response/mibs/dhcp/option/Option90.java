package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.addSeparators;

public record Option90(@JsonProperty("Value") String dhcpv6Value) {

    public String dhcpv4Value() {
        return addSeparators(this.dhcpv6Value());
    }

    public String dhcpv6Value() {
        return this.dhcpv6Value.toUpperCase();
    }
}
