use anyhow::Result;
use dialoguer::Password;

use crate::api::livebox_client::LiveboxClient;
use crate::api::models::MibsResponse;
use crate::cli::GenerateCommands;
use crate::config::Config;
use crate::generators::authentication_generator::AuthenticationGenerator;
use crate::renderers::{render_authentication, render_dhcp, render_gpon};

pub fn run_generate(command: GenerateCommands, config: &Config) -> Result<()> {
    match command {
        GenerateCommands::Dhcp { password } => {
            let password = resolve_password(password, "Livebox admin password")?;
            let mibs = fetch_mibs(&password, config)?;
            print!("{}", render_dhcp(&mibs)?);
        }
        GenerateCommands::Gpon { password } => {
            let password = resolve_password(password, "Livebox admin password")?;
            let mibs = fetch_mibs(&password, config)?;
            print!("{}", render_gpon(&mibs));
        }
        GenerateCommands::Authentication { login, password } => {
            let password = resolve_password(password, "Orange password")?;
            let generator = AuthenticationGenerator::new();
            let authentication = generator.generate_authentication(&login, &password)?;
            print!("{}", render_authentication(&authentication));
        }
    }

    Ok(())
}

fn fetch_mibs(password: &str, config: &Config) -> Result<MibsResponse> {
    let client = LiveboxClient::new(config.livebox_api_url.clone())?;
    let session = client.login(password)?;
    client.get_mibs(&session)
}

fn resolve_password(password: Option<String>, prompt: &str) -> Result<String> {
    match password {
        Some(value) if !value.is_empty() => Ok(value),
        Some(_) | None => Ok(Password::new().with_prompt(prompt).interact()?),
    }
}
