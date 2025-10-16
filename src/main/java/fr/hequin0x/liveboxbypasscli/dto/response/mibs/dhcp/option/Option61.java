package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.addSeparators;
import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.to2BytesHex;

public record Option61(@JsonProperty("Value") String dhcpv6Value) {
    private static final String DUID_TYPE_HEX = to2BytesHex(3);
    private static final String HARDWARE_TYPE_ETHERNET_HEX = to2BytesHex(1);

    public String dhcpv4Value() {
        String value = this.dhcpv6Value.substring(2);
        return addSeparators(value).toUpperCase();
    }

    public String dhcpv6Value() {
        String value = this.dhcpv6Value.substring(2);
        return addSeparators(DUID_TYPE_HEX + HARDWARE_TYPE_ETHERNET_HEX + value).toUpperCase();
    }
}
