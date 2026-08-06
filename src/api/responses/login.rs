use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct LoginResponse {
    pub data: LoginData,
}

#[derive(Debug, Deserialize)]
pub struct LoginData {
    #[serde(rename = "contextID")]
    pub context_id: String,
}
