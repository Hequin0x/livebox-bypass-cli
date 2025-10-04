package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HexFormat;

public record Option77(@JsonProperty("Value") String value) {

    public String value() {
        byte[] bytes = HexFormat.of().parseHex(this.value);
        return new String(bytes).substring(1);
    }
}
