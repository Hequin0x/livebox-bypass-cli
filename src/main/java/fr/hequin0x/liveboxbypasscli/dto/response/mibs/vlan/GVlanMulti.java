package fr.hequin0x.liveboxbypasscli.dto.response.mibs.vlan;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GVlanMulti(@JsonProperty("VLANID") Integer vlanID) { }
