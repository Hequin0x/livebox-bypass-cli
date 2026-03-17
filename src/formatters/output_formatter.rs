use std::io::{self, IsTerminal};

use owo_colors::OwoColorize;

#[derive(Debug, Clone)]
pub struct Row<'a> {
    pub key: &'a str,
    pub value: String,
}

#[derive(Debug, Clone)]
pub struct Section<'a> {
    pub title: &'a str,
    pub rows: Vec<Row<'a>>,
}

pub fn format_output(sections: &[Section<'_>]) -> String {
    let use_color = io::stdout().is_terminal();
    let mut out = String::new();

    for (section_index, section) in sections.iter().enumerate() {
        if section_index > 0 {
            out.push('\n');
        }

        let title = if use_color {
            format!("{}", section.title.blue().bold())
        } else {
            section.title.to_string()
        };

        out.push_str(&format!("[{title}]\n"));

        for row in &section.rows {
            let key = if use_color {
                format!("{}", row.key.green().bold())
            } else {
                row.key.to_string()
            };

            out.push_str(&format!("{key} -> {}\n", row.value));
        }
    }

    out
}