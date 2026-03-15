package fr.hequin0x.liveboxbypasscli.util;

import picocli.CommandLine.Help.Ansi;

import java.util.Map;
import java.util.stream.Collectors;

public final class OutputFormatter {

    public static String formatOutput(final Map<String, Map<String, String>> data) {
        return data.entrySet().stream()
                .map(entry -> {
                    String coloredSectionName = Ansi.AUTO.string("@|bold,blue " + entry.getKey() + "|@");
                    String formattedRows = entry.getValue().entrySet().stream()
                            .map(row -> {
                                String coloredKey = Ansi.AUTO.string("@|bold,green " + row.getKey() + "|@");
                                return coloredKey + " -> " + row.getValue();
                            })
                            .collect(Collectors.joining("\n"));
                    return "[" + coloredSectionName + "]\n" + formattedRows;
                })
                .collect(Collectors.joining("\n\n", "\n", ""));
    }
}
