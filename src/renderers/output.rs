use anyhow::Result;
use owo_colors::OwoColorize;
use std::fmt::Write;
use std::io::{IsTerminal, stdout};
use std::sync::LazyLock;

static USE_COLOR: LazyLock<bool> = LazyLock::new(|| stdout().is_terminal());

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

impl Row<'_> {
    fn formatted_key(&self) -> String {
        if *USE_COLOR {
            format!("{}", self.key.green().bold())
        } else {
            self.key.to_owned()
        }
    }
}

impl Section<'_> {
    fn formatted_title(&self) -> String {
        if *USE_COLOR {
            format!("{}", self.title.blue().bold())
        } else {
            self.title.to_owned()
        }
    }
}

pub fn format_output(sections: &[Section<'_>]) -> Result<String> {
    let mut out = String::new();

    for (section_index, section) in sections.iter().enumerate() {
        if section_index > 0 {
            writeln!(out)?;
        }

        let title = section.formatted_title();

        writeln!(out, "[{title}]")?;

        for row in &section.rows {
            let key = row.formatted_key();
            writeln!(out, "{key} -> {}", row.value)?;
        }
    }

    Ok(out)
}
