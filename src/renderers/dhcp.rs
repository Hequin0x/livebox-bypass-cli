use anyhow::Result;

use crate::api::models::MibsResponse;
use crate::formatters::output_formatter::{format_output, Row, Section};

use super::options::{
    option60_dhcpv4_value, option60_dhcpv6_value, option61_dhcpv4_value, option61_dhcpv6_value,
    option77_value, option90_dhcpv4_value, option90_dhcpv6_value,
};

pub fn render_dhcp(mibs: &MibsResponse) -> Result<String> {
    let dhcp_cos = mibs.status.dhcp.dhcp_data.priority_mark.to_string();
    let wan_vlan_id = mibs.status.vlan.gvlan_multi.vlan_id.to_string();
    let sent = &mibs.status.dhcp.dhcp_data.sent_option;

    let sections = vec![
        Section {
            title: "DHCPv4/v6 Options",
            rows: vec![Row {
                key: "CoS",
                value: dhcp_cos,
            }],
        },
        Section {
            title: "WAN Options",
            rows: vec![Row {
                key: "VLAN ID",
                value: wan_vlan_id,
            }],
        },
        Section {
            title: "DHCPv4 Options",
            rows: vec![
                Row {
                    key: "60",
                    value: option60_dhcpv4_value(&sent.option60)?,
                },
                Row {
                    key: "61",
                    value: option61_dhcpv4_value(&sent.option61),
                },
                Row {
                    key: "77",
                    value: option77_value(&sent.option77)?,
                },
                Row {
                    key: "90",
                    value: option90_dhcpv4_value(&sent.option90),
                },
            ],
        },
        Section {
            title: "DHCPv6 Options",
            rows: vec![
                Row {
                    key: "16",
                    value: option60_dhcpv6_value(&sent.option60),
                },
                Row {
                    key: "1",
                    value: option61_dhcpv6_value(&sent.option61),
                },
                Row {
                    key: "15",
                    value: option77_value(&sent.option77)?,
                },
                Row {
                    key: "11",
                    value: option90_dhcpv6_value(&sent.option90),
                },
            ],
        },
    ];

    Ok(format_output(&sections))
}