pub mod login;
pub mod mibs;

use serde::Serialize;
use serde::de::DeserializeOwned;

pub use login::Login;
pub use mibs::GetMibs;

pub trait ApiRequestSpec {
    const SERVICE: &'static str;
    const METHOD: &'static str;

    type Parameters: Serialize;
    type Response: DeserializeOwned;

    fn parameters(&self) -> Self::Parameters;
}

#[derive(Debug, Serialize)]
pub struct ApiRequest<T: ApiRequestSpec> {
    pub service: &'static str,
    pub method: &'static str,
    pub parameters: T::Parameters,
}

impl<T: ApiRequestSpec> From<T> for ApiRequest<T> {
    fn from(spec: T) -> Self {
        Self {
            service: T::SERVICE,
            method: T::METHOD,
            parameters: spec.parameters(),
        }
    }
}

#[derive(Debug, Default, Serialize)]
pub struct EmptyParameters {}
