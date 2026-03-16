use anyhow::{anyhow, Result};

pub fn add_separators(data: &str) -> String {
    data.as_bytes()
        .chunks(2)
        .map(|chunk| std::str::from_utf8(chunk).unwrap_or_default())
        .collect::<Vec<_>>()
        .join(":")
}

pub fn to_1_byte_hex(data: usize) -> String {
    format!("{data:02x}")
}

pub fn to_1_byte_hex_length(data: &str) -> String {
    to_1_byte_hex((data.len() / 2) + 2)
}

pub fn to_2_bytes_hex(data: usize) -> String {
    format!("{data:04x}")
}

pub fn parse_hex(data: &str) -> Result<String> {
    let bytes = hex::decode(data).map_err(|e| anyhow!("invalid hex: {e}"))?;
    Ok(String::from_utf8_lossy(&bytes).to_string())
}

pub fn to_hex(data: &[u8]) -> String {
    hex::encode(data)
}
