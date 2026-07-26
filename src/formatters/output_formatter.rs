use anyhow::Result;
use owo_colors::OwoColorize;
use std::fmt::Write;
use std::io::{IsTerminal, stdout};

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

pub fn format_output(sections: &[Section<'_>]) -> Result<String> {
    let use_color = stdout().is_terminal();
    let mut out = String::new();

    for (section_index, section) in sections.iter().enumerate() {
        if section_index > 0 {
            writeln!(out)?;
        }

        let title = if use_color {
            format!("{}", section.title.blue().bold())
        } else {
            section.title.to_owned()
        };

        writeln!(out, "[{title}]")?;

        for row in &section.rows {
            let key = if use_color {
                format!("{}", row.key.green().bold())
            } else {
                row.key.to_owned()
            };

            writeln!(out, "{key} -> {}", row.value)?;
        }
    }

    Ok(out)
}
