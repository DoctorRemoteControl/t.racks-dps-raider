package de.drremote.dsp408.util;

import java.io.IOException;

public final class HexUtil {
    private HexUtil() {
    }

    public static String payloadAscii(byte[] payload) {
        StringBuilder sb = new StringBuilder(payload.length);
        for (byte b : payload) {
            int v = b & 0xFF;
            if (v >= 32 && v <= 126) {
                sb.append((char) v);
            } else {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 3);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", data[i] & 0xFF));
        }
        return sb.toString();
    }

    public static String toHexPreview(byte[] data, int maxBytes) {
        if (data == null || data.length == 0) {
            return "";
        }

        int len = Math.min(data.length, maxBytes);
        StringBuilder sb = new StringBuilder(len * 3 + 4);

        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", data[i] & 0xFF));
        }

        if (data.length > maxBytes) {
            sb.append(" ...");
        }

        return sb.toString();
    }

    public static String toHexCompact(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(String.format("%02X", b & 0xFF));
        }
        return sb.toString();
    }

    public static String compactHex(String hex) {
        return hex == null ? null : hex.replaceAll("\\s+", "").toUpperCase();
    }

    public static byte[] hexToBytes(String hex) throws IOException {
        String normalized = compactHex(hex);
        if (normalized == null) {
            return new byte[0];
        }
        if ((normalized.length() & 1) != 0) {
            throw new IOException("Odd hex length: " + normalized.length());
        }

        byte[] out = new byte[normalized.length() / 2];
        for (int i = 0; i < normalized.length(); i += 2) {
            int hi = Character.digit(normalized.charAt(i), 16);
            int lo = Character.digit(normalized.charAt(i + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IOException("Invalid hex at position " + i + ": " + normalized);
            }
            out[i / 2] = (byte) ((hi << 4) | lo);
        }
        return out;
    }
}
