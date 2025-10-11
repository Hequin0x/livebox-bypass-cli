package fr.hequin0x.liveboxbypasscli.dto.response.mibs;

import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.DHCP;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.gpon.GPON;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.vlan.VLAN;

public record MIBsStatus(DHCP dhcp, GPON gpon, VLAN vlan) { }
