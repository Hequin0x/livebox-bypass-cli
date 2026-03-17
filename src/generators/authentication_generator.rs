use anyhow::{bail, Result};
use rand::TryRng;

use crate::formatters::hex_formatter::{add_separators, to_1_byte_hex, to_1_byte_hex_length, to_hex};

pub static AUTH_PREFIX: &str =
    "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:";

#[derive(Debug, Clone, Default)]
pub struct AuthenticationGenerator;

impl AuthenticationGenerator {
    pub fn generate_authentication(&self, login: &str, password: &str) -> Result<String> {
        self.generate_authentication_with_random(login, password, &self.generate_random()?)
    }

    pub fn generate_authentication_with_random(
        &self,
        login: &str,
        password: &str,
        random: &str,
    ) -> Result<String> {
        if login.trim().is_empty() || password.trim().is_empty() {
            bail!("Login and password must be provided");
        }

        let id = &random[0..1];

        let random_hex = to_hex(random.as_bytes());
        let id_hex = to_hex(id.as_bytes());

        let digest_input = format!("{id}{password}{random}");
        let digest = self.compute_digest(digest_input.as_bytes());

        let auth_chain = self.build_authentication_chain(&random_hex, &id_hex, &digest);

        let login_hex = to_hex(login.as_bytes());
        let login_payload = format!("{}{}", to_1_byte_hex_length(&login_hex), login_hex);

        let payload = format!("{login_payload}{auth_chain}");

        Ok(format!(
            "{}{}",
            AUTH_PREFIX,
            add_separators(&payload).to_uppercase()
        ))
    }

    pub fn generate_random(&self) -> Result<String> {
        let mut bytes = [0u8; 1024];
        rand::rngs::SysRng.try_fill_bytes(&mut bytes)?;
        Ok(self.compute_digest(&bytes)[0..16].to_string())
    }

    pub fn compute_digest(&self, data: &[u8]) -> String {
        format!("{:x}", md5::compute(data))
    }

    fn build_authentication_chain(&self, random_hex: &str, id_hex: &str, digest: &str) -> String {
        let random_hex_length = to_1_byte_hex_length(random_hex);
        let id_hex_length = to_1_byte_hex_length(id_hex);
        let chain_length = to_1_byte_hex((random_hex.len() + digest.len()) - 4);
        let digest_with_id_length = to_1_byte_hex_length(&format!("{digest}{id_hex_length}"));

        format!(
            "{chain_length}{random_hex_length}{random_hex}{id_hex_length}{digest_with_id_length}{id_hex}{digest}"
        )
    }
}