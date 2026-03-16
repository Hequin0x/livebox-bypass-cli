use crate::formatters::output_formatter::{format_output, Section};

pub fn render_authentication(authentication: &str) -> String {
    let dhcpv6_authentication = authentication.replace(':', "");

    let sections: Vec<Section> = vec![
        ("DHCPv4 Options", vec![("90", authentication.to_string())]),
        ("DHCPv6 Options", vec![("11", dhcpv6_authentication)]),
    ];

    format_output(&sections)
}