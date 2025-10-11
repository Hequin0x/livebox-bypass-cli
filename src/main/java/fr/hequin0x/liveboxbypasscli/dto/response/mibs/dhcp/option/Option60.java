package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.to2BytesHex;
import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.parseHex;

public record Option60(@JsonProperty("Value") String dhcpv6Value) {
    private static final String IANA_ENTERPRISE_NUMBER_HEX = to2BytesHex(0);
    private static final String SAGEM_ENTERPRISE_NUMBER_HEX = to2BytesHex(1038);

    public String dhcpv4Value() {
        return parseHex(this.dhcpv6Value);
    }

    public String dhcpv6Value() {
        String valueLengthHex = to2BytesHex(this.dhcpv6Value.length() / 2);
        return (IANA_ENTERPRISE_NUMBER_HEX + SAGEM_ENTERPRISE_NUMBER_HEX + valueLengthHex + this.dhcpv6Value).toUpperCase();
    }
}
