use anyhow::{bail, Result};
use clap::Parser;
use dialoguer::Password;

use livebox_bypass_cli::api::livebox_client::LiveboxClient;
use livebox_bypass_cli::cli::{Cli, Commands, GenerateCommands};
use livebox_bypass_cli::config::Config;
use livebox_bypass_cli::generators::authentication::AuthenticationGenerator;
use livebox_bypass_cli::renderers::{render_authentication, render_dhcp, render_gpon};


fn main() -> Result<()> {
    let cli = Cli::parse();
    let config = Config::load();

    match cli.command {
        Commands::Generate { command } => run_generate(command, &config)?,
    }

    Ok(())
}

fn run_generate(command: GenerateCommands, config: &Config) -> Result<()> {
    match command {
        GenerateCommands::Dhcp { password } => {
            let password = resolve_password(password, "Livebox admin password")?;
            let client = LiveboxClient::new(&config.livebox_api_url)?;
            let session = client.login(&password)?;
            let mibs = client.get_mibs(&session)?;
            print!("{}", render_dhcp(&mibs)?);
        }
        GenerateCommands::Gpon { password } => {
            let password = resolve_password(password, "Livebox admin password")?;
            let client = LiveboxClient::new(&config.livebox_api_url)?;
            let session = client.login(&password)?;
            let mibs = client.get_mibs(&session)?;
            print!("{}", render_gpon(&mibs));
        }
        GenerateCommands::Authentication { login, password } => {
            let password = resolve_password(password, "Orange password")?;
            let generator = AuthenticationGenerator::new(&config.auth_prefix);
            let authentication = generator.generate_authentication(&login, &password)?;
            print!("{}", render_authentication(&authentication));
        }
    }

    Ok(())
}

fn resolve_password(password: Option<String>, prompt: &str) -> Result<String> {
    match password.as_deref() {
        Some("") => Ok(Password::new().with_prompt(prompt).interact()?),
        Some(value) => Ok(value.to_string()),
        None => bail!("password is required"),
    }
}
