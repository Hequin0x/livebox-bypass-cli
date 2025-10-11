package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option;

import com.fasterxml.jackson.annotation.JsonProperty;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.parseHex;

public record Option77(@JsonProperty("Value") String value) {

    public String value() {
        return parseHex(this.value).substring(1);
    }
}
