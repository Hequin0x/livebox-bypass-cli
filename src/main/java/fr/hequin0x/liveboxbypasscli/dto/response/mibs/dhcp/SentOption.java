package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option.Option60;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option.Option61;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option.Option77;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.option.Option90;

public record SentOption(
        @JsonProperty("60") Option60 option60,
        @JsonProperty("61") Option61 option61,
        @JsonProperty("77") Option77 option77,
        @JsonProperty("90") Option90 option90
) { }
