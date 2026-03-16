use crate::api::models::MibsResponse;
use crate::formatters::output_formatter::{format_output, Section};

pub fn render_gpon(mibs: &MibsResponse) -> String {
    let veip0 = &mibs.status.gpon.veip0;

    let sections: Vec<Section> = vec![(
        "GPON Configuration",
        vec![
            ("Serial Number", veip0.serial_number.clone()),
            ("Hardware Version", veip0.hardware_version.clone()),
            ("Vendor ID", veip0.vendor_id.clone()),
            ("Software Version 0", veip0.ont_software_version0.clone()),
            ("Software Version 1", veip0.ont_software_version1.clone()),
        ],
    )];

    format_output(&sections)
}
