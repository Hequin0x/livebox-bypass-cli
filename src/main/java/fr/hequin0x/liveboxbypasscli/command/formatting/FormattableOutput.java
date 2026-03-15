package fr.hequin0x.liveboxbypasscli.command.formatting;

import com.github.freva.asciitable.AsciiTable;
import picocli.CommandLine.Option;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FormattableOutput {

    @Option(names = {"-f", "--format"}, description = "Output format", defaultValue = "table")
    private String format;

    public String formatOutput(final Map<String[], String[][]> data) {
        return switch (this.format) {
            case "table" -> "\n" + this.getFormattedTable(data);
            case "raw" -> "\n" + this.getFormattedRaw(data);
            default -> throw new IllegalArgumentException("Unsupported format: " + this.format);
        };

    }

    private String getFormattedTable(final Map<String[], String[][]> data) {
        return data.entrySet().stream()
                .map(entry -> AsciiTable.getTable(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }

    private String getFormattedRaw(final Map<String[], String[][]> data) {
        return data.entrySet().stream()
                .map(entry -> {
                    String section = entry.getKey()[0];

                    String rows = Stream.of(entry.getValue())
                            .map(row -> row[0] + " -> " + row[1])
                            .collect(Collectors.joining("\n"));

                    return "[" + section + "]\n" + rows;
                })
                .collect(Collectors.joining("\n\n"));
    }
}
