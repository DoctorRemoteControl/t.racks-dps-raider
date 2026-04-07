package de.drremote.dsp408.dump;

import java.util.ArrayList;
import java.util.List;

public final class DumpDiffUtil {
    private DumpDiffUtil() {
    }

    public static List<ByteDiff> diffBytes(byte[] before, byte[] after) {
        requireBytes(before, after);

        List<ByteDiff> out = new ArrayList<>();
        int len = Math.min(before.length, after.length);

        for (int i = 0; i < len; i++) {
            int bv = Byte.toUnsignedInt(before[i]);
            int av = Byte.toUnsignedInt(after[i]);
            if (bv != av) {
                out.add(new ByteDiff(i, bv, av));
            }
        }

        return List.copyOf(out);
    }

    public static List<U16Diff> diffU16le(byte[] before, byte[] after) {
        requireBytes(before, after);

        List<U16Diff> out = new ArrayList<>();
        int len = Math.min(before.length, after.length);

        for (int offset = 0; offset + 1 < len; offset++) {
            int b0 = Byte.toUnsignedInt(before[offset]);
            int b1 = Byte.toUnsignedInt(before[offset + 1]);
            int a0 = Byte.toUnsignedInt(after[offset]);
            int a1 = Byte.toUnsignedInt(after[offset + 1]);

            if (b0 == a0 && b1 == a1) {
                continue;
            }

            int beforeValue = DumpByteReaders.u16le(before, offset);
            int afterValue = DumpByteReaders.u16le(after, offset);

            if (beforeValue != afterValue) {
                out.add(new U16Diff(offset, beforeValue, afterValue));
            }
        }

        return List.copyOf(out);
    }

    public static String formatByteDiffs(byte[] before, byte[] after) {
        List<ByteDiff> diffs = diffBytes(before, after);
        if (diffs.isEmpty()) {
            return "No byte changes.";
        }

        StringBuilder sb = new StringBuilder();
        for (ByteDiff diff : diffs) {
            sb.append(diff.offsetHex())
                    .append(": ")
                    .append(diff.beforeHex())
                    .append(" -> ")
                    .append(diff.afterHex())
                    .append("\n");
        }
        return sb.toString().trim();
    }

    public static String formatU16Diffs(byte[] before, byte[] after) {
        List<U16Diff> diffs = diffU16le(before, after);
        if (diffs.isEmpty()) {
            return "No u16le changes.";
        }

        StringBuilder sb = new StringBuilder();
        for (U16Diff diff : diffs) {
            sb.append(diff).append("\n");
        }
        return sb.toString().trim();
    }

    public static String formatDiffReport(byte[] before, byte[] after) {
        requireBytes(before, after);

        StringBuilder sb = new StringBuilder();
        sb.append("before_len=").append(before.length)
                .append(", after_len=").append(after.length)
                .append("\n\n");

        sb.append("[BYTE DIFFS]\n");
        sb.append(formatByteDiffs(before, after));

        sb.append("\n\n[U16LE CANDIDATES]\n");
        sb.append(formatU16Diffs(before, after));

        return sb.toString();
    }

    private static void requireBytes(byte[] before, byte[] after) {
        if (before == null) {
            throw new IllegalArgumentException("before is null");
        }
        if (after == null) {
            throw new IllegalArgumentException("after is null");
        }
    }
}