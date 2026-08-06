use dialoguer::Password;

pub fn resolve_password(password: Option<String>, prompt: &str) -> anyhow::Result<String> {
    match password {
        Some(value) if !value.is_empty() => Ok(value),
        Some(_) | None => Ok(Password::new().with_prompt(prompt).interact()?),
    }
}
