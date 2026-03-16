use owo_colors::OwoColorize;

pub type Section = (&'static str, Vec<(&'static str, String)>);

pub fn format_output(sections: &[Section]) -> String {
    let mut out = String::new();

    for (idx, (section, rows)) in sections.iter().enumerate() {
        if idx > 0 {
            out.push('\n');
        }

        out.push_str(&format!("[{}]\n", section.blue().bold()));

        for (key, value) in rows {
            out.push_str(&format!("{} -> {}\n", key.green().bold(), value));
        }
    }

    out
}
