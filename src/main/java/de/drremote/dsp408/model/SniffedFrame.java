package de.drremote.dsp408.model;

import de.drremote.dsp408.util.DspProtocol;
import de.drremote.dsp408.util.HexUtil;

import java.util.Arrays;

public record SniffedFrame(
        String direction,
        byte[] frame,
        byte[] payload,
        Integer command,
        int payloadLen,
        boolean checksumOk
) {
    public SniffedFrame {
        frame = frame == null ? new byte[0] : Arrays.copyOf(frame, frame.length);
        payload = payload == null ? new byte[0] : Arrays.copyOf(payload, payload.length);

        if (payloadLen != payload.length) {
            payloadLen = payload.length;
        }

        if (command == null) {
            command = DspProtocol.command(payload);
        }
    }

    @Override
    public byte[] frame() {
        return Arrays.copyOf(frame, frame.length);
    }

    @Override
    public byte[] payload() {
        return Arrays.copyOf(payload, payload.length);
    }

    public String frameHex() {
        return HexUtil.toHex(frame);
    }

    public String payloadHex() {
        return HexUtil.toHex(payload);
    }

    public String payloadAscii() {
        return HexUtil.payloadAscii(payload);
    }

    public String commandHex() {
        return DspProtocol.commandHex(command);
    }

    public Integer readBlockIndex() {
        return DspProtocol.readBlockIndex(payload);
    }

    public boolean isWrite() {
        return "PC_TO_DSP".equalsIgnoreCase(direction);
    }

    public boolean isResponse() {
        return "DSP_TO_PC".equalsIgnoreCase(direction);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sniff{dir=").append(direction);
        sb.append(", cmd=").append(commandHex());
        sb.append(", len=").append(payloadLen);
        sb.append(", hex=").append(payloadHex());
        if (readBlockIndex() != null) {
            sb.append(", block=").append(String.format("0x%02X", readBlockIndex()));
        }
        if (!checksumOk) {
            sb.append(", checksum_ok=false");
        }
        sb.append("}");
        return sb.toString();
    }
}