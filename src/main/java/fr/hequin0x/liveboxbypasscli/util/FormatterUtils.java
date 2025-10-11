package fr.hequin0x.liveboxbypasscli.util;

import java.util.HexFormat;

public final class FormatterUtils {

    public static String addSeparators(final String data) {
        return data.replaceAll("(.{2})(?=.)", "$1:");
    }

    public static String to1ByteHex(final int data) {
        return String.format("%02x", data);
    }

    public static String to1ByteHexLength(final String data) {
        return to1ByteHex((data.length() / 2) + 2);
    }

    public static String to2BytesHex(final int data) {
        return String.format("%04x", data);
    }

    public static String parseHex(final String data) {
        byte[] bytes = HexFormat.of().parseHex(data);
        return new String(bytes);
    }

    public static String toHex(final String data) {
        return toHex(data.getBytes());
    }

    public static String toHex(final byte[] data) {
        return HexFormat.of().formatHex(data);
    }
}
