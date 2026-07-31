use anyhow::{Result, bail};
use rand::{TryRng, rngs::SysRng};

use crate::utils::hex::{HexExt, NumHexExt};

pub const AUTH_PREFIX: &str = "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:";

pub fn generate_authentication(login: &str, password: &str, salt: Option<&str>) -> Result<String> {
    if login.trim().is_empty() || password.trim().is_empty() {
        bail!("Login and password must be provided");
    }

    let salt = match salt {
        Some(value) => value.to_owned(),
        None => generate_salt()?,
    };

    let id = &salt[0..1];

    let salt_hex = salt.as_bytes().to_hex();
    let id_hex = id.as_bytes().to_hex();

    let digest_input = format!("{id}{password}{salt}");
    let digest = compute_digest(digest_input.as_bytes());

    let auth_chain = build_authentication_chain(&salt_hex, &id_hex, &digest);

    let login_hex = login.as_bytes().to_hex();
    let login_payload = format!("{}{}", login_hex.to_1_byte_hex_length(), login_hex);

    let payload = format!("{login_payload}{auth_chain}");

    Ok(format!(
        "{}{}",
        AUTH_PREFIX,
        payload.colon_separated().to_uppercase()
    ))
}

fn generate_salt() -> Result<String> {
    let mut bytes = [0u8; 1024];
    SysRng.try_fill_bytes(&mut bytes)?;
    Ok(compute_digest(&bytes)[0..16].to_string())
}

fn compute_digest(data: &[u8]) -> String {
    format!("{:x}", md5::compute(data))
}

fn build_authentication_chain(salt_hex: &str, id_hex: &str, digest: &str) -> String {
    let salt_hex_length = salt_hex.to_1_byte_hex_length();
    let id_hex_length = id_hex.to_1_byte_hex_length();
    let chain_length = ((salt_hex.len() + digest.len()) - 4).to_1_byte_hex();
    let digest_with_id_length = format!("{digest}{id_hex_length}").to_1_byte_hex_length();

    format!(
        "{chain_length}{salt_hex_length}{salt_hex}{id_hex_length}{digest_with_id_length}{id_hex}{digest}"
    )
}
