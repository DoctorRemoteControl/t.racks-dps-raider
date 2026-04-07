package de.drremote.dsp408.raw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class RawSocketClient implements AutoCloseable {
    private final String host;
    private final int port;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final boolean verbose;

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    RawSocketClient(String host, int port, int connectTimeoutMs, int readTimeoutMs, boolean verbose) {
        this.host = host;
        this.port = port;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.verbose = verbose;
    }

    void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), connectTimeoutMs);
        socket.setSoTimeout(readTimeoutMs);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        System.out.println("Verbunden mit " + host + ":" + port);
    }

    List<byte[]> sendPayloadAndRead(String label, byte[] payload, int responses) throws IOException {
        byte[] frame = RawFrameCodec.buildFrame(payload);

        System.out.println();
        System.out.println("TX " + label + " payload: " + RawFrameCodec.toHex(payload));
        System.out.println("TX " + label + " frame:   " + RawFrameCodec.toHex(frame));

        out.write(frame);
        out.flush();

        return readAndPrintResponses(label, responses);
    }

    List<byte[]> sendFrameAndRead(String label, byte[] frame, int responses) throws IOException {
        System.out.println();
        System.out.println("TX " + label + " frame:   " + RawFrameCodec.toHex(frame));

        out.write(frame);
        out.flush();

        return readAndPrintResponses(label, responses);
    }

    private List<byte[]> readAndPrintResponses(String label, int maxFrames) throws IOException {
        if (maxFrames <= 0) {
            return List.of();
        }

        List<byte[]> frames = readFrames(maxFrames);

        if (frames.isEmpty()) {
            System.out.println("RX " + label + ": <no response within timeout>");
            return frames;
        }

        for (int i = 0; i < frames.size(); i++) {
            byte[] frame = frames.get(i);
            byte[] payload = RawFrameCodec.extractPayload(frame);
            boolean ok = RawFrameCodec.isValidFrame(frame);

            System.out.println("RX " + label + " frame[" + i + "]:    " + RawFrameCodec.toHex(frame));
            System.out.println("RX " + label + " payload[" + i + "]:  " + RawFrameCodec.toHex(payload));
            System.out.println("RX " + label + " ascii[" + i + "]:    " + RawFrameCodec.toAsciiPreview(payload));
            System.out.println("RX " + label + " checksum[" + i + "]: " + (ok ? "OK" : "INVALID"));
        }

        return frames;
    }

    private List<byte[]> readFrames(int maxFrames) throws IOException {
        List<byte[]> frames = new ArrayList<>();
        ByteArrayOutputStream pending = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];

        while (frames.size() < maxFrames) {
            try {
                int n = in.read(buf);
                if (n < 0) {
                    break;
                }
                if (n == 0) {
                    continue;
                }

                pending.write(buf, 0, n);

                if (verbose) {
                    byte[] last = Arrays.copyOf(buf, n);
                    System.out.println("RX raw chunk: " + RawFrameCodec.toHex(last));
                }

                extractFramesFromPending(pending, frames, maxFrames);

            } catch (SocketTimeoutException timeout) {
                break;
            }
        }

        return frames;
    }

    private static void extractFramesFromPending(ByteArrayOutputStream pending, List<byte[]> outFrames, int maxFrames) {
        byte[] data = pending.toByteArray();
        int pos = 0;

        while (outFrames.size() < maxFrames) {
            int start = findSequence(data, pos, (byte) 0x10, (byte) 0x02);
            if (start < 0) {
                pos = data.length;
                break;
            }

            int endMarker = findSequence(data, start + 2, (byte) 0x10, (byte) 0x03);
            if (endMarker < 0) {
                pos = start;
                break;
            }

            int checksumIndex = endMarker + 2;
            if (checksumIndex >= data.length) {
                pos = start;
                break;
            }

            byte[] frame = Arrays.copyOfRange(data, start, checksumIndex + 1);
            outFrames.add(frame);
            pos = checksumIndex + 1;
        }

        pending.reset();
        if (pos < data.length) {
            pending.write(data, pos, data.length - pos);
        }
    }

    private static int findSequence(byte[] data, int from, byte b1, byte b2) {
        for (int i = Math.max(0, from); i < data.length - 1; i++) {
            if (data[i] == b1 && data[i + 1] == b2) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void close() {
        closeQuietly(in);
        closeQuietly(out);
        closeQuietly(socket);
    }

    private static void closeQuietly(AutoCloseable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Exception ignored) {
        }
    }
}
