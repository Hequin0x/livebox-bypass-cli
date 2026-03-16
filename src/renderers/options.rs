use anyhow::Result;

use crate::api::models::{Option60, Option61, Option77, Option90};
use crate::formatters::hex_formatter::{add_separators, parse_hex, to_2_bytes_hex};

pub fn option60_dhcpv4_value(option: &Option60) -> Result<String> {
    parse_hex(&option.dhcpv6_value)
}

pub fn option60_dhcpv6_value(option: &Option60) -> String {
    let iana_enterprise_number_hex = to_2_bytes_hex(0);
    let sagem_enterprise_number_hex = to_2_bytes_hex(1038);
    let value_length_hex = to_2_bytes_hex(option.dhcpv6_value.len() / 2);

    format!(
        "{}{}{}{}",
        iana_enterprise_number_hex,
        sagem_enterprise_number_hex,
        value_length_hex,
        option.dhcpv6_value
    )
        .to_uppercase()
}

pub fn option61_dhcpv4_value(option: &Option61) -> String {
    add_separators(&option.dhcpv6_value[2..]).to_uppercase()
}

pub fn option61_dhcpv6_value(option: &Option61) -> String {
    let duid_type_hex = to_2_bytes_hex(3);
    let hardware_type_ethernet_hex = to_2_bytes_hex(1);
    let value = &option.dhcpv6_value[2..];

    add_separators(&format!("{duid_type_hex}{hardware_type_ethernet_hex}{value}")).to_uppercase()
}

pub fn option77_value(option: &Option77) -> Result<String> {
    let decoded = parse_hex(&option.value)?;
    Ok(decoded.chars().skip(1).collect())
}

pub fn option90_dhcpv4_value(option: &Option90) -> String {
    add_separators(&option90_dhcpv6_value(option))
}

pub fn option90_dhcpv6_value(option: &Option90) -> String {
    option.dhcpv6_value.to_uppercase()
}
