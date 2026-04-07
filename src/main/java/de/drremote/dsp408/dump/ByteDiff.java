package de.drremote.dsp408.dump;

public record ByteDiff(int offset, int before, int after) {
    public String offsetHex() {
        return String.format("0x%02X", offset);
    }

    public String beforeHex() {
        return String.format("%02X", before & 0xFF);
    }

    public String afterHex() {
        return String.format("%02X", after & 0xFF);
    }

    @Override
    public String toString() {
        return offsetHex() + ": " + beforeHex() + " -> " + afterHex();
    }
}