package fr.hequin0x.liveboxbypasscli.dto.response.mibs.vlan;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VLAN(@JsonProperty("gvlan_multi") GVlanMulti gvlanMulti) {
}
