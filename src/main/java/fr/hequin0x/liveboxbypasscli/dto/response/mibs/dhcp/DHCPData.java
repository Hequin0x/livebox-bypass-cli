package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DHCPData(
        @JsonProperty("PriorityMark") Integer priorityMark,
        @JsonProperty("SentOption") SentOption sentOption
) { }
