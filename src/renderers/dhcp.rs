use anyhow::Result;

use crate::api::models::MibsResponse;
use crate::formatters::output_formatter::{format_output, Section};

use super::options::{
    option60_dhcpv4_value, option60_dhcpv6_value, option61_dhcpv4_value, option61_dhcpv6_value,
    option77_value, option90_dhcpv4_value, option90_dhcpv6_value,
};

pub fn render_dhcp(mibs: &MibsResponse) -> Result<String> {
    let dhcp_cos = mibs.status.dhcp.dhcp_data.priority_mark.to_string();
    let wan_vlan_id = mibs.status.vlan.gvlan_multi.vlan_id.to_string();
    let sent = &mibs.status.dhcp.dhcp_data.sent_option;

    let sections: Vec<Section> = vec![
        ("DHCPv4/v6 Options", vec![("CoS", dhcp_cos)]),
        ("WAN Options", vec![("VLAN ID", wan_vlan_id)]),
        (
            "DHCPv4 Options",
            vec![
                ("60", option60_dhcpv4_value(&sent.option60)?),
                ("61", option61_dhcpv4_value(&sent.option61)),
                ("77", option77_value(&sent.option77)?),
                ("90", option90_dhcpv4_value(&sent.option90)),
            ],
        ),
        (
            "DHCPv6 Options",
            vec![
                ("16", option60_dhcpv6_value(&sent.option60)),
                ("1", option61_dhcpv6_value(&sent.option61)),
                ("15", option77_value(&sent.option77)?),
                ("11", option90_dhcpv6_value(&sent.option90)),
            ],
        ),
    ];

    Ok(format_output(&sections))
}
