use std::str::from_utf8;

use anyhow::{Result, anyhow};

/// Extension trait for hex encoding/decoding operations on byte sequences.
pub trait HexExt {
    /// Returns a colon-separated hex string representation of the bytes.
    fn colon_separated(&self) -> String;

    /// Returns a single-byte hex representation of the length of the byte sequence,
    /// calculated as (length / 2) + 2.
    fn to_1_byte_hex_length(&self) -> String;

    /// Encodes the bytes as a hexadecimal string.
    fn to_hex(&self) -> String;

    /// Decodes a hexadecimal string into a UTF-8 string.
    ///
    /// # Returns
    /// A `Result` containing the decoded string.
    ///
    /// # Errors
    /// Returns an error if the input is not a valid hexadecimal string.
    fn decode_hex(&self) -> Result<String>;
}

/// Extension trait for converting numeric types to hex strings.
pub trait NumHexExt {
    /// Converts the number to a single-byte (2-digit) hexadecimal string.
    fn to_1_byte_hex(&self) -> String;

    /// Converts the number to a two-byte (4-digit) hexadecimal string.
    fn to_2_bytes_hex(&self) -> String;
}

impl<T: AsRef<[u8]> + ?Sized> HexExt for T {
    fn colon_separated(&self) -> String {
        self.as_ref()
            .chunks(2)
            .map(|chunk| from_utf8(chunk).unwrap_or_default())
            .collect::<Vec<_>>()
            .join(":")
    }

    fn to_1_byte_hex_length(&self) -> String {
        ((self.as_ref().len() / 2) + 2).to_1_byte_hex()
    }

    fn to_hex(&self) -> String {
        hex::encode(self.as_ref())
    }

    fn decode_hex(&self) -> Result<String> {
        let bytes = hex::decode(self.as_ref()).map_err(|e| anyhow!("invalid hex: {e}"))?;
        Ok(String::from_utf8_lossy(&bytes).into_owned())
    }
}

impl NumHexExt for usize {
    fn to_1_byte_hex(&self) -> String {
        format!("{self:02x}")
    }

    fn to_2_bytes_hex(&self) -> String {
        format!("{self:04x}")
    }
}
