package de.drremote.dsp408.dump;

import de.drremote.dsp408.util.HexUtil;

import java.util.Arrays;

public final class DumpByteReaders {
    private DumpByteReaders() {
    }

    public static int u8(byte[] bytes, int offset) {
        requireRange(bytes, offset, 1);
        return Byte.toUnsignedInt(bytes[offset]);
    }

    public static int u16le(byte[] bytes, int offset) {
        requireRange(bytes, offset, 2);
        return Byte.toUnsignedInt(bytes[offset])
                | (Byte.toUnsignedInt(bytes[offset + 1]) << 8);
    }

    public static long u32le(byte[] bytes, int offset) {
        requireRange(bytes, offset, 4);
        return (long) Byte.toUnsignedInt(bytes[offset])
                | ((long) Byte.toUnsignedInt(bytes[offset + 1]) << 8)
                | ((long) Byte.toUnsignedInt(bytes[offset + 2]) << 16)
                | ((long) Byte.toUnsignedInt(bytes[offset + 3]) << 24);
    }

    public static byte[] slice(byte[] bytes, int offset, int length) {
        requireRange(bytes, offset, length);
        return Arrays.copyOfRange(bytes, offset, offset + length);
    }

    public static String hex(byte[] bytes, int offset, int length) {
        return HexUtil.toHex(slice(bytes, offset, length));
    }

    public static String ascii(byte[] bytes, int offset, int length, boolean trimZero) {
        byte[] slice = slice(bytes, offset, length);
        StringBuilder sb = new StringBuilder(slice.length);
        for (byte b : slice) {
            int v = b & 0xFF;
            if (trimZero && v == 0) {
                break;
            }
            if (v >= 32 && v <= 126) {
                sb.append((char) v);
            } else if (v == 0 && !trimZero) {
                sb.append('\0');
            } else {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    private static void requireRange(byte[] bytes, int offset, int length) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes sind null.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset < 0: " + offset);
        }
        if (length < 0) {
            throw new IllegalArgumentException("Laenge < 0: " + length);
        }
        if (offset + length > bytes.length) {
            throw new IllegalArgumentException("Range ausserhalb des Byte-Arrays: offset="
                    + offset + ", length=" + length + ", bytes=" + bytes.length);
        }
    }
}
