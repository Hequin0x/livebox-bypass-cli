use clap::{Parser, Subcommand};
use url::Url;

use crate::config::DEFAULT_LIVEBOX_API_URL;

#[derive(Debug, Parser)]
#[command(name = "livebox-bypass-cli", version, about = "Generate DHCP and GPON configuration for bypassing Orange Livebox")]
pub struct Cli {
    #[arg(long, env = "LIVEBOX_API_URL", default_value = DEFAULT_LIVEBOX_API_URL)]
    pub livebox_api_url: Url,

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
    #[command(about = "Generate DHCP options from Livebox MIBs")]
    Dhcp {
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
    #[command(about = "Generate GPON settings from Livebox MIBs")]
    Gpon {
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
    #[command(about = "Generate authentication string from login + Orange password")]
    Authentication {
        #[arg(short = 'l', long = "login", required = true)]
        login: String,
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1)]
        password: Option<String>,
    },
}
