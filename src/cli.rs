use clap::{Parser, Subcommand};

#[derive(Debug, Parser)]
#[command(name = "livebox-bypass-cli", version, about = "Generate DHCP and GPON configuration for bypassing Orange Livebox")]
pub struct Cli {
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
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1, default_missing_value = "")]
        password: Option<String>,
    },
    Gpon {
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1, default_missing_value = "")]
        password: Option<String>,
    },
    Authentication {
        #[arg(short = 'l', long = "login", required = true)]
        login: String,
        #[arg(short = 'p', long = "password", required = true, num_args = 0..=1, default_missing_value = "")]
        password: Option<String>,
    },
}
