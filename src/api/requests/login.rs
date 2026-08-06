use serde::Serialize;

use super::ApiRequestSpec;
use crate::api::responses::login::LoginResponse;

#[derive(Debug)]
pub struct Login {
    pub password: String,
}

impl Login {
    #[must_use]
    pub const fn new(password: String) -> Self {
        Self { password }
    }
}

#[derive(Debug, Serialize)]
pub struct LoginParameters {
    #[serde(rename = "applicationName")]
    pub application_name: &'static str,
    pub username: &'static str,
    pub password: String,
}

impl ApiRequestSpec for Login {
    const SERVICE: &'static str = "sah.Device.Information";
    const METHOD: &'static str = "createContext";

    type Parameters = LoginParameters;
    type Response = LoginResponse;

    fn parameters(&self) -> Self::Parameters {
        LoginParameters {
            application_name: "webui",
            username: "admin",
            password: self.password.clone(),
        }
    }
}
