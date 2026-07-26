use anyhow::Result;

use crate::api::models::{Option60, Option61, Option77, Option90};
use crate::formatters::hex_formatter::{add_separators, parse_hex, to_2_bytes_hex};

/// Decodes the vendor class identifier from DHCP Option 60 (`DHCPv4`).
///
/// # Errors
/// Returns an error if the hexadecimal value cannot be parsed.
pub fn option60_dhcpv4_value(option: &Option60) -> Result<String> {
    parse_hex(&option.value)
}

/// Formats the vendor class identifier for DHCP Option 60 (`DHCPv6`).
///
/// Prepends the IANA enterprise number (0), Sagem enterprise number (1038),
/// and value length header to the original value, then converts to uppercase.
#[must_use]
pub fn option60_dhcpv6_value(option: &Option60) -> String {
    let iana_enterprise_number_hex = to_2_bytes_hex(0);
    let sagem_enterprise_number_hex = to_2_bytes_hex(1038);
    let value_length_hex = to_2_bytes_hex(option.value.len() / 2);

    format!(
        "{}{}{}{}",
        iana_enterprise_number_hex, sagem_enterprise_number_hex, value_length_hex, option.value
    )
    .to_uppercase()
}

/// Formats the client identifier for DHCP Option 61 (`DHCPv4`).
///
/// Adds separators to the hexadecimal value (excluding the first two characters) and converts to uppercase.
#[must_use]
pub fn option61_dhcpv4_value(option: &Option61) -> String {
    add_separators(&option.value[2..]).to_uppercase()
}

/// Formats the DHCP Unique Identifier (DUID) for DHCP Option 61 (`DHCPv6`).
///
/// Constructs a DUID with type 3 (DUID-LL) and Ethernet hardware type (1),
/// then adds separators and converts to uppercase.
#[must_use]
pub fn option61_dhcpv6_value(option: &Option61) -> String {
    let duid_type_hex = to_2_bytes_hex(3);
    let hardware_type_ethernet_hex = to_2_bytes_hex(1);
    let value = &option.value[2..];

    add_separators(&format!(
        "{duid_type_hex}{hardware_type_ethernet_hex}{value}"
    ))
    .to_uppercase()
}

/// Decodes and extracts the user class data from DHCP Option 77.
///
/// Parses the hexadecimal value and returns the decoded string with the first character removed.
///
/// # Errors
/// Returns an error if the hexadecimal value cannot be parsed.
pub fn option77_value(option: &Option77) -> Result<String> {
    let decoded = parse_hex(&option.value)?;
    Ok(decoded.chars().skip(1).collect())
}

/// Formats the authentication information for DHCP Option 90 (`DHCPv4`).
///
/// Adds separators to the `DHCPv6` formatted value.
#[must_use]
pub fn option90_dhcpv4_value(option: &Option90) -> String {
    add_separators(&option90_dhcpv6_value(option))
}

/// Formats the authentication information for DHCP Option 90 (`DHCPv6`).
///
/// Converts the value to uppercase hexadecimal.
#[must_use]
pub fn option90_dhcpv6_value(option: &Option90) -> String {
    option.value.to_uppercase()
}
