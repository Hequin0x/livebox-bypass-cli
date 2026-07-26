use anyhow::Result;

use crate::api::models::MibsResponse;
use crate::formatters::output_formatter::{Row, Section, format_output};

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
                    value: sent.vendor_class.dhcpv4_value()?,
                },
                Row {
                    key: "61",
                    value: sent.client_identifier.dhcpv4_value(),
                },
                Row {
                    key: "77",
                    value: sent.user_class.dhcp_value()?,
                },
                Row {
                    key: "90",
                    value: sent.authentication.dhcpv4_value(),
                },
            ],
        },
        Section {
            title: "DHCPv6 Options",
            rows: vec![
                Row {
                    key: "16",
                    value: sent.vendor_class.dhcpv6_value(),
                },
                Row {
                    key: "1",
                    value: sent.client_identifier.dhcpv6_value(),
                },
                Row {
                    key: "15",
                    value: sent.user_class.dhcp_value()?,
                },
                Row {
                    key: "11",
                    value: sent.authentication.dhcpv6_value(),
                },
            ],
        },
    ];

    format_output(&sections)
}
