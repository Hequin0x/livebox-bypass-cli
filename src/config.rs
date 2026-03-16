pub const DEFAULT_LIVEBOX_API_URL: &str = "http://192.168.1.1/ws";
pub const DEFAULT_AUTH_PREFIX: &str =
    "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:";

#[derive(Debug, Clone)]
pub struct Config {
    pub livebox_api_url: String,
    pub auth_prefix: String,
}

impl Config {
    pub fn load() -> Self {
        Self {
            livebox_api_url: std::env::var("LIVEBOX_API_URL")
                .unwrap_or_else(|_| DEFAULT_LIVEBOX_API_URL.to_string()),
            auth_prefix: std::env::var("LIVEBOX_AUTH_PREFIX")
                .unwrap_or_else(|_| DEFAULT_AUTH_PREFIX.to_string()),
        }
    }
}
