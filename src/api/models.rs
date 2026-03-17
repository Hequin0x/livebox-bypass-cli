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
    pub fn new(password: String) -> Self {
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
    pub option60: Option60,
    #[serde(rename = "61")]
    pub option61: Option61,
    #[serde(rename = "77")]
    pub option77: Option77,
    #[serde(rename = "90")]
    pub option90: Option90,
}

#[derive(Debug, Deserialize)]
pub struct Option60 {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct Option61 {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct Option77 {
    #[serde(rename = "Value")]
    pub value: String,
}

#[derive(Debug, Deserialize)]
pub struct Option90 {
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
