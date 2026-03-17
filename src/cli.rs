use clap::{Parser, Subcommand};
use url::Url;

use crate::config::{DEFAULT_AUTH_PREFIX, DEFAULT_LIVEBOX_API_URL};

#[derive(Debug, Parser)]
#[command(name = "livebox-bypass-cli", version, about = "Generate DHCP and GPON configuration for bypassing Orange Livebox")]
pub struct Cli {
    #[arg(long, env = "LIVEBOX_API_URL", default_value = DEFAULT_LIVEBOX_API_URL)]
    pub livebox_api_url: Url,

    #[arg(long, env = "LIVEBOX_AUTH_PREFIX", default_value = DEFAULT_AUTH_PREFIX)]
    pub auth_prefix: String,

    #[command(subcommand)]
    pub command: Commands,
}

#[derive(Debug, Subcommand)]
pub enum Commands {
    Generate {
        #[command(subcommand)]
        command: GenerateCommands,
    },
}

#[derive(Debug, Subcommand)]
pub enum GenerateCommands {
    Dhcp {
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
    Gpon {
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
    Authentication {
        #[arg(short = 'l', long = "login", required = true)]
        login: String,
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
}
