package de.drremote.dsp408.util;

import de.drremote.dsp408.model.ProxyResponse;
import de.drremote.dsp408.model.SniffedFrame;

public final class DspProtocol {
    private DspProtocol() {
    }

    public static Integer command(byte[] payload) {
        return payload != null && payload.length > 3 ? (payload[3] & 0xFF) : null;
    }

    public static String commandHex(Integer command) {
        return command == null ? "null" : String.format("0x%02X", command);
    }

    public static Integer readBlockIndex(byte[] payload) {
        Integer cmd = command(payload);
        if (cmd == null || cmd != 0x24 || payload.length <= 4) {
            return null;
        }
        return payload[4] & 0xFF;
    }

    public static Integer readBlockIndex(ProxyResponse response) {
        return response == null ? null : readBlockIndex(response.payload());
    }

    public static Integer readBlockIndex(SniffedFrame frame) {
        return frame == null ? null : readBlockIndex(frame.payload());
    }

    public static boolean isReadBlockResponse(byte[] payload) {
        return readBlockIndex(payload) != null;
    }

    public static boolean isReadBlockResponse(ProxyResponse response) {
        return readBlockIndex(response) != null;
    }

    public static boolean isReadBlockResponse(SniffedFrame frame) {
        return readBlockIndex(frame) != null;
    }
}