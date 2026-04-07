package de.drremote.dsp408.raw;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

final class RawFrameCodec {
    private RawFrameCodec() {
    }

    static byte[] buildLoginPayload(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN muss genau 4 Ziffern haben, z. B. 1234");
        }

        byte[] ascii = pin.getBytes(StandardCharsets.US_ASCII);
        byte[] payload = new byte[5 + ascii.length];
        payload[0] = 0x00;
        payload[1] = 0x01;
        payload[2] = 0x06;
        payload[3] = 0x2D;
        payload[4] = 0x00;
        System.arraycopy(ascii, 0, payload, 5, ascii.length);
        return payload;
    }

    static byte[] hex(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Hex text is null");
        }

        String cleaned = text
                .replace("0x", "")
                .replace("0X", "")
                .replaceAll("[^0-9A-Fa-f]", "");

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Hex text is empty");
        }
        if ((cleaned.length() & 1) != 0) {
            throw new IllegalArgumentException("Hex text has odd length: " + cleaned);
        }

        byte[] out = new byte[cleaned.length() / 2];
        for (int i = 0; i < out.length; i++) {
            int hi = Character.digit(cleaned.charAt(i * 2), 16);
            int lo = Character.digit(cleaned.charAt(i * 2 + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException("Invalid hex text: " + text);
            }
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    static String toHex(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(data.length * 3);
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", data[i] & 0xFF));
        }
        return sb.toString();
    }

    static String toAsciiPreview(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(data.length);
        for (byte b : data) {
            int v = b & 0xFF;
            if (v >= 32 && v <= 126) {
                sb.append((char) v);
            } else {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    static byte[] buildFrame(byte[] payload) {
        byte[] frame = new byte[payload.length + 5];
        frame[0] = 0x10;
        frame[1] = 0x02;
        System.arraycopy(payload, 0, frame, 2, payload.length);
        frame[2 + payload.length] = 0x10;
        frame[3 + payload.length] = 0x03;
        frame[4 + payload.length] = xor(frame, 0, 4 + payload.length);
        return frame;
    }

    static boolean isValidFrame(byte[] frame) {
        if (frame == null || frame.length < 5) {
            return false;
        }
        if ((frame[0] & 0xFF) != 0x10 || (frame[1] & 0xFF) != 0x02) {
            return false;
        }
        if ((frame[frame.length - 3] & 0xFF) != 0x10 || (frame[frame.length - 2] & 0xFF) != 0x03) {
            return false;
        }
        byte expected = xor(frame, 0, frame.length - 1);
        return expected == frame[frame.length - 1];
    }

    static byte[] extractPayload(byte[] frame) {
        if (frame == null || frame.length < 5) {
            return new byte[0];
        }
        if ((frame[0] & 0xFF) != 0x10 || (frame[1] & 0xFF) != 0x02) {
            return new byte[0];
        }
        if ((frame[frame.length - 3] & 0xFF) != 0x10 || (frame[frame.length - 2] & 0xFF) != 0x03) {
            return new byte[0];
        }
        return Arrays.copyOfRange(frame, 2, frame.length - 3);
    }

    private static byte xor(byte[] data, int startInclusive, int endExclusive) {
        byte x = 0;
        for (int i = startInclusive; i < endExclusive; i++) {
            x ^= data[i];
        }
        return x;
    }
}
