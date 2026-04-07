package de.drremote.dsp408.model;

import java.util.Arrays;

public final class Frame {
    private final byte[] raw;
    private final byte[] payload;
    private final boolean checksumOk;

    public Frame(byte[] raw, byte[] payload, boolean checksumOk) {
        this.raw = raw == null ? new byte[0] : Arrays.copyOf(raw, raw.length);
        this.payload = payload == null ? new byte[0] : Arrays.copyOf(payload, payload.length);
        this.checksumOk = checksumOk;
    }

    public byte[] raw() {
        return Arrays.copyOf(raw, raw.length);
    }

    public byte[] payload() {
        return Arrays.copyOf(payload, payload.length);
    }

    public boolean checksumOk() {
        return checksumOk;
    }

    public int command() {
        return payload.length > 3 ? (payload[3] & 0xFF) : -1;
    }
}
