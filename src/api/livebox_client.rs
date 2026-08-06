use std::time::Duration;

use anyhow::{Context, Result, anyhow};
use reqwest::blocking::Client;
use reqwest::header::{AUTHORIZATION, CONTENT_TYPE, COOKIE, HeaderValue};
use url::Url;

use crate::api::requests::{ApiRequest, ApiRequestSpec, Login};

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
        let request = ApiRequest::from(Login::new(password.to_owned()));

        let response = self
            .http
            .post(self.base_url.as_str())
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header(AUTHORIZATION, HeaderValue::from_static("X-Sah-Login"))
            .json(&request)
            .send()?
            .error_for_status()
            .context("login request failed")?;

        let cookie = response
            .headers()
            .get("set-cookie")
            .ok_or_else(|| anyhow!("missing Set-Cookie header"))?
            .to_str()?
            .to_owned();

        let login_response = response
            .json::<<Login as ApiRequestSpec>::Response>()
            .context("invalid login response JSON")?;

        Ok(AuthSession {
            context_id: login_response.data.context_id,
            cookie,
        })
    }

    pub fn call<T>(&self, session: &AuthSession, spec: T) -> Result<T::Response>
    where
        T: ApiRequestSpec,
    {
        let request = ApiRequest::from(spec);

        self.http
            .post(self.base_url.as_str())
            .header(CONTENT_TYPE, SAH_CONTENT_TYPE)
            .header("X-Context", &session.context_id)
            .header(COOKIE, &session.cookie)
            .json(&request)
            .send()?
            .error_for_status()
            .with_context(|| format!("{}/{} request failed", T::SERVICE, T::METHOD))?
            .json()
            .with_context(|| format!("invalid {}/{} response JSON", T::SERVICE, T::METHOD))
    }
}
