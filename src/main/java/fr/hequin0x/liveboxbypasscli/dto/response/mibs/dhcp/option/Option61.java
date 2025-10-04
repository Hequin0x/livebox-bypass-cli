package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Option61(@JsonProperty("Value") String dhcpv6Value) {
    private static final int DUID_TYPE = 3;
    private static final int HARDWARE_TYPE_ETHERNET = 1;

    public String dhcpv4Value() {
        return this.dhcpv6Value.substring(2).replaceAll("(.{2})(?=.)", "$1:");
    }

    public String dhcpv6Value() {
        return String.format("%04x%04x%s",
                DUID_TYPE,
                HARDWARE_TYPE_ETHERNET,
                this.dhcpv6Value.substring(2)
        ).replaceAll("(.{2})(?=.)", "$1:").toUpperCase();
    }
}
