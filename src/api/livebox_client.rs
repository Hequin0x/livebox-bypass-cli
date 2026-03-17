use std::time::Duration;

use anyhow::{anyhow, bail, Context, Result};
use reqwest::blocking::Client;
use reqwest::header::{HeaderValue, AUTHORIZATION, CONTENT_TYPE, COOKIE};
use url::Url;

use crate::api::models::{LoginRequest, LoginResponse, MibsRequest, MibsResponse};

const SAH_CONTENT_TYPE: &str = "application/x-sah-ws-4-call+json";

#[derive(Debug, Clone)]
pub struct AuthSession {
    pub context_id: String,
    pub cookie: String,
}

#[derive(Debug, Clone)]
pub struct LiveboxClient {
    http: Client,
    base_url: Url,
}

impl LiveboxClient {
    pub fn new(base_url: Url) -> Result<Self> {
        let http = Client::builder()
            .connect_timeout(Duration::from_secs(5))
            .timeout(Duration::from_secs(10))
            .build()
            .context("failed to build HTTP client")?;

        Ok(Self { http, base_url })
    }

    pub fn login(&self, password: &str) -> Result<AuthSession> {
        let response = self
            .http
            .post(self.base_url.clone())
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header(AUTHORIZATION, HeaderValue::from_static("X-Sah-Login"))
            .json(&LoginRequest::new(password.to_string()))
            .send()
            .context("failed to send login request")?;

        if !response.status().is_success() {
            bail!("login failed with status {}", response.status());
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
            .post(self.base_url.clone())
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header("X-Context", &session.context_id)
            .header(COOKIE, &session.cookie)
            .json(&MibsRequest::default())
            .send()
            .context("failed to send MIBs request")?;

        if !response.status().is_success() {
            bail!("getMIBs failed with status {}", response.status());
        }

        response.json().context("invalid MIBs response JSON")
    }
}
