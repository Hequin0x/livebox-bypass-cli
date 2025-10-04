package fr.hequin0x.liveboxbypasscli.dto.response.wan;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WanData(@JsonProperty("LinkType") String linkType) {
}
