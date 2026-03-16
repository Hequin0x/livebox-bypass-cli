use crate::api::models::{LoginRequest, LoginResponse, MibsRequest, MibsResponse};
use anyhow::{anyhow, bail, Context, Result};
use reqwest::blocking::Client;
use reqwest::header::{HeaderValue, AUTHORIZATION, CONTENT_TYPE, COOKIE};

const SAH_CONTENT_TYPE: &str = "application/x-sah-ws-4-call+json";

#[derive(Debug, Clone)]
pub struct AuthSession {
    pub context_id: String,
    pub cookie: String,
}

#[derive(Debug, Clone)]
pub struct LiveboxClient {
    http: Client,
    base_url: String,
}

impl LiveboxClient {
    pub fn new(base_url: impl Into<String>) -> Result<Self> {
        Ok(Self {
            http: Client::builder().build()?,
            base_url: base_url.into(),
        })
    }

    pub fn login(&self, password: &str) -> Result<AuthSession> {
        let response = self
            .http
            .post(&self.base_url)
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header(AUTHORIZATION, HeaderValue::from_static("X-Sah-Login"))
            .json(&LoginRequest::new(password.to_string()))
            .send()
            .context("failed to send login request")?;

        if !response.status().is_success() {
            bail!("login failed with status {}", response.text().unwrap_or_else(|_| "<no response body>".to_string()));
        }

        let cookie = response
            .headers()
            .get("set-cookie")
            .ok_or_else(|| anyhow!("missing Set-Cookie header"))?
            .to_str()?
            .to_string();

        let login_response: LoginResponse = response.json().context("invalid login response JSON")?;

        Ok(AuthSession {
            context_id: login_response.data.context_id,
            cookie,
        })
    }

    pub fn get_mibs(&self, session: &AuthSession) -> Result<MibsResponse> {
        let response = self
            .http
            .post(&self.base_url)
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header("X-Context", &session.context_id)
            .header(COOKIE, &session.cookie)
            .json(&MibsRequest::new())
            .send()
            .context("failed to send MIBs request")?;

        if !response.status().is_success() {
            bail!("getMIBs failed with status {}", response.status());
        }

        response.json().context("invalid MIBs response JSON")
    }
}
