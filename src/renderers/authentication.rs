use crate::formatters::output_formatter::{Row, Section, format_output};

pub fn render_authentication(authentication: &str) -> String {
    let dhcpv6_authentication = authentication.replace(':', "");

    let sections = vec![
        Section {
            title: "DHCPv4 Options",
            rows: vec![Row {
                key: "90",
                value: authentication.to_string(),
            }],
        },
        Section {
            title: "DHCPv6 Options",
            rows: vec![Row {
                key: "11",
                value: dhcpv6_authentication,
            }],
        },
    ];

    format_output(&sections)
}
