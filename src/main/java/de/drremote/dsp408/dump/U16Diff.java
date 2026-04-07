package de.drremote.dsp408.dump;

public record U16Diff(int offset, int before, int after) {
    public String offsetHex() {
        return String.format("0x%02X", offset);
    }

    public String beforeHex() {
        return String.format("0x%04X", before & 0xFFFF);
    }

    public String afterHex() {
        return String.format("0x%04X", after & 0xFFFF);
    }

    @Override
    public String toString() {
        return "u16le@" + offsetHex() + ": " + before + " -> " + after
                + " (" + beforeHex() + " -> " + afterHex() + ")";
    }
}