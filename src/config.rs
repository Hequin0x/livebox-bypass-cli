use url::Url;

pub const DEFAULT_LIVEBOX_API_URL: &str = "http://192.168.1.1/ws";

#[derive(Debug, Clone)]
pub struct Config {
    pub livebox_api_url: Url,
}
