package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HexFormat;

public record Option60(@JsonProperty("Value") String dhcpv6Value) {
    private static final int IANA_ENTERPRISE_NUMBER = 0;
    private static final int SAGEM_ENTERPRISE_NUMBER = 1038;

    public String dhcpv4Value() {
        byte[] bytes = HexFormat.of().parseHex(this.dhcpv6Value);
        return new String(bytes);
    }

    public String dhcpv6Value() {
        return String.format("%04x%04x%04x%s",
                IANA_ENTERPRISE_NUMBER,
                SAGEM_ENTERPRISE_NUMBER,
                this.dhcpv4Value().length(),
                this.dhcpv6Value
        ).toUpperCase();
    }
}
