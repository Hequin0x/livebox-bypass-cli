use url::Url;

pub const DEFAULT_LIVEBOX_API_URL: &str = "http://192.168.1.1/ws";
pub const DEFAULT_AUTH_PREFIX: &str =
    "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:";

#[derive(Debug, Clone)]
pub struct Config {
    pub livebox_api_url: Url,
    pub auth_prefix: String,
}
