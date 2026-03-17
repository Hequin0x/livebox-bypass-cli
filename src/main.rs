use anyhow::Result;
use clap::Parser;

use livebox_bypass_cli::cli::{Cli, Commands};
use livebox_bypass_cli::commands::generate::run_generate;
use livebox_bypass_cli::config::Config;

fn main() -> Result<()> {
    let cli = Cli::parse();

    let config = Config {
        livebox_api_url: cli.livebox_api_url.clone(),
    };

    match cli.command {
        Commands::Generate { command } => run_generate(command, &config)?,
    }

    Ok(())
}
