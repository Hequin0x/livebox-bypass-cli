use anyhow::Result;

use crate::api::livebox_client::LiveboxClient;
use crate::api::requests::GetMibs;
use crate::api::responses::MibsResponse;
use crate::cli::GenerateCommands;
use crate::config::Config;
use crate::generators::authentication_generator::generate_authentication;
use crate::renderers::{render_authentication, render_dhcp, render_gpon};
use crate::utils::password::resolve_password;

pub fn run_generate(command: GenerateCommands, config: &Config) -> Result<()> {
    match command {
        GenerateCommands::Dhcp { password } => {
            fetch_mibs(password, config, render_dhcp)?;
        }
        GenerateCommands::Gpon { password } => {
            fetch_mibs(password, config, render_gpon)?;
        }
        GenerateCommands::Authentication {
            login,
            password,
            salt,
        } => {
            let password = resolve_password(password, "Orange password")?;
            let authentication = generate_authentication(&login, &password, salt.as_deref())?;
            print!("{}", render_authentication(&authentication)?);
        }
    }

    Ok(())
}

fn fetch_mibs<F>(password: Option<String>, config: &Config, render: F) -> Result<()>
where
    F: FnOnce(&MibsResponse) -> Result<String>,
{
    let password = resolve_password(password, "Livebox admin password")?;

    let client = LiveboxClient::new(config.livebox_api_url.clone())?;
    let session = client.login(&password)?;
    let mibs = client.call(&session, GetMibs)?;

    print!("{}", render(&mibs)?);
    Ok(())
}
