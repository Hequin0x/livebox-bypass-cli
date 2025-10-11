package fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DHCP(@JsonProperty("dhcp_data") DHCPData dhcpData) { }
