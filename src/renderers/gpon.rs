use anyhow::Result;

use crate::api::models::MibsResponse;
use crate::formatters::output_formatter::{format_output, Row, Section};

pub fn render_gpon(mibs: &MibsResponse) -> Result<String> {
    let veip0 = &mibs.status.gpon.veip0;

    let sections = vec![Section {
        title: "GPON Configuration",
        rows: vec![
            Row {
                key: "Serial Number",
                value: veip0.serial_number.clone(),
            },
            Row {
                key: "Hardware Version",
                value: veip0.hardware_version.clone(),
            },
            Row {
                key: "Vendor ID",
                value: veip0.vendor_id.clone(),
            },
            Row {
                key: "Software Version 0",
                value: veip0.ont_software_version0.clone(),
            },
            Row {
                key: "Software Version 1",
                value: veip0.ont_software_version1.clone(),
            },
        ],
    }];

    Ok(format_output(&sections))
}
