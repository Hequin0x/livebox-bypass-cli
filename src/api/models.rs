use crate::utils::hex::{HexExt, NumHexExt};
use anyhow::Result;
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize)]
pub struct LoginRequest {
    pub service: &'static str,
    pub method: &'static str,
    pub parameters: LoginParameters,
}

#[derive(Debug, Serialize)]
pub struct LoginParameters {
    #[serde(rename = "applicationName")]
    pub application_name: &'static str,
    pub username: &'static str,
    pub password: String,
}

impl LoginRequest {
    #[must_use]
    pub const fn new(password: String) -> Self {
        Self {
            service: "sah.Device.Information",
            method: "createContext",
            parameters: LoginParameters {
                application_name: "webui",
                username: "admin",
                password,
            },
        }
    }
}

#[derive(Debug, Serialize)]
pub struct MibsRequest {
    pub service: &'static str,
    pub method: &'static str,
    pub parameters: EmptyParameters,
}

#[derive(Debug, Default, Serialize)]
pub struct EmptyParameters {}

impl Default for MibsRequest {
    fn default() -> Self {
        Self {
            service: "NeMo.Intf.data",
            method: "getMIBs",
            parameters: EmptyParameters::default(),
        }
    }
}

#[derive(Debug, Deserialize)]
pub struct LoginResponse {
    pub data: LoginData,
}

#[derive(Debug, Deserialize)]
pub struct LoginData {
    #[serde(rename = "contextID")]
    pub context_id: String,
}

#[derive(Debug, Deserialize)]
pub struct MibsResponse {
    pub status: MibsStatus,
}

#[derive(Debug, Deserialize)]
pub struct MibsStatus {
    pub dhcp: Dhcp,
    pub gpon: Gpon,
    pub vlan: Vlan,
}

#[derive(Debug, Deserialize)]
pub struct Dhcp {
    #[serde(rename = "dhcp_data")]
    pub dhcp_data: DhcpData,
}

#[derive(Debug, Deserialize)]
pub struct DhcpData {
    #[serde(rename = "PriorityMark")]
    pub priority_mark: u16,
    #[serde(rename = "SentOption")]
    pub sent_option: SentOption,
}

#[derive(Debug, Deserialize)]
pub struct SentOption {
    #[serde(rename = "60")]
    pub vendor_class: VendorClass,
    #[serde(rename = "61")]
    pub client_identifier: ClientIdentifier,
    #[serde(rename = "77")]
    pub user_class: UserClass,
    #[serde(rename = "90")]
    pub authentication: Authentication,
}

#[derive(Debug, Deserialize)]
pub struct VendorClass {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct ClientIdentifier {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct UserClass {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct Authentication {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct Gpon {
    pub veip0: Veip0,
}

#[derive(Debug, Deserialize)]
pub struct Veip0 {
    #[serde(rename = "SerialNumber")]
    pub serial_number: String,
    #[serde(rename = "HardwareVersion")]
    pub hardware_version: String,
    #[serde(rename = "VendorId")]
    pub vendor_id: String,
    #[serde(rename = "ONTSoftwareVersion0")]
    pub ont_software_version0: String,
    #[serde(rename = "ONTSoftwareVersion1")]
    pub ont_software_version1: String,
}

#[derive(Debug, Deserialize)]
pub struct Vlan {
    #[serde(rename = "gvlan_multi")]
    pub gvlan_multi: GvlanMulti,
}

#[derive(Debug, Deserialize)]
pub struct GvlanMulti {
    #[serde(rename = "VLANID")]
    pub vlan_id: u16,
}

impl VendorClass {
    /// Decodes the vendor class identifier from DHCP Option 60 (`DHCPv4`).
    ///
    /// # Errors
    /// Returns an error if the hexadecimal value cannot be parsed.
    pub fn dhcpv4_value(&self) -> Result<String> {
        self.value.decode_hex()
    }

    /// Formats the vendor class identifier for DHCP Option 60 (`DHCPv6`).
    ///
    /// Prepends the IANA enterprise number (0), Sagem enterprise number (1038),
    /// and value length header to the original value, then converts to uppercase.
    #[must_use]
    pub fn dhcpv6_value(&self) -> String {
        let iana_enterprise_number_hex = 0usize.to_2_bytes_hex();
        let sagem_enterprise_number_hex = 1038usize.to_2_bytes_hex();
        let value_length_hex = (self.value.len() / 2).to_2_bytes_hex();

        format!(
            "{}{}{}{}",
            iana_enterprise_number_hex, sagem_enterprise_number_hex, value_length_hex, self.value
        )
        .to_uppercase()
    }
}

impl ClientIdentifier {
    /// Formats the client identifier for DHCP Option 61 (`DHCPv4`).
    ///
    /// Adds separators to the hexadecimal value (excluding the first two characters) and converts to uppercase.
    #[must_use]
    pub fn dhcpv4_value(&self) -> String {
        self.value[2..].colon_separated().to_uppercase()
    }

    /// Formats the DHCP Unique Identifier (DUID) for DHCP Option 61 (`DHCPv6`).
    ///
    /// Constructs a DUID with type 3 (DUID-LL) and Ethernet hardware type (1),
    /// then adds separators and converts to uppercase.
    #[must_use]
    pub fn dhcpv6_value(&self) -> String {
        let duid_type_hex = 3usize.to_2_bytes_hex();
        let hardware_type_ethernet_hex = 1usize.to_2_bytes_hex();
        let value = &self.value[2..];

        format!("{duid_type_hex}{hardware_type_ethernet_hex}{value}")
            .colon_separated()
            .to_uppercase()
    }
}

impl UserClass {
    /// Decodes and extracts the user class data from DHCP Option 77.
    ///
    /// Parses the hexadecimal value and returns the decoded string with the first character removed.
    ///
    /// # Errors
    /// Returns an error if the hexadecimal value cannot be parsed.
    pub fn dhcp_value(&self) -> Result<String> {
        let decoded = self.value.decode_hex()?;
        Ok(decoded.chars().skip(1).collect())
    }
}

impl Authentication {
    /// Formats the authentication information for DHCP Option 90 (`DHCPv4`).
    ///
    /// Adds separators to the `DHCPv6` formatted value.
    #[must_use]
    pub fn dhcpv4_value(&self) -> String {
        self.dhcpv6_value().colon_separated()
    }

    /// Formats the authentication information for DHCP Option 90 (`DHCPv6`).
    ///
    /// Converts the value to uppercase hexadecimal.
    #[must_use]
    pub fn dhcpv6_value(&self) -> String {
        self.value.to_uppercase()
    }
}
