package de.drremote.dsp408.proxy;

import de.drremote.dsp408.model.ProxyStatus;
import de.drremote.dsp408.util.HexUtil;
import de.drremote.dsp408.util.JsonUtil;
import de.drremote.dsp408.util.WaitUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

final class ControlChannel implements AutoCloseable {
    private static final int LOG_JSON_MAX = 180;
    private static final int LOG_HEX_MAX_BYTES = 12;

    private final String host;
    private final int port;
    private final boolean verbose;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    ControlChannel(String host, int port, boolean verbose) {
        this.host = host;
        this.port = port;
        this.verbose = verbose;
    }

    void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), ProxyConstants.CONTROL_CONNECT_TIMEOUT_MS);
        socket.setSoTimeout(ProxyConstants.CONTROL_READ_TIMEOUT_MS);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        log("Connected to control " + host + ":" + port);
    }

    ProxyStatus status() throws IOException {
        String response = sendJsonLine("{\"type\":\"status\"}");
        Boolean ok = JsonUtil.jsonBoolean(response, "ok");
        Boolean sessionActive = JsonUtil.jsonBoolean(response, "sessionActive");
        Boolean injectReady = JsonUtil.jsonBoolean(response, "injectReady");

        if (!Boolean.TRUE.equals(ok)) {
            throw new IOException("Control status failed: " + extractError(response));
        }

        return new ProxyStatus(
                Boolean.TRUE.equals(sessionActive),
                Boolean.TRUE.equals(injectReady),
                response
        );
    }

    void resetSession() throws IOException {
        requireOk("{\"type\":\"reset_session\"}", "reset_session");
    }

    void ensureSession() throws IOException {
        requireOk("{\"type\":\"ensure_session\"}", "ensure_session");
    }

    void beginTransaction(long timeoutMs) throws IOException {
        requireOk("{\"type\":\"begin_control_transaction\",\"timeoutMs\":" + timeoutMs + "}",
                "begin_control_transaction");
    }

    void endTransaction() throws IOException {
        requireOk("{\"type\":\"end_control_transaction\"}", "end_control_transaction");
    }

    void sendPayload(byte[] payload) throws IOException {
        String compactHex = HexUtil.toHexCompact(payload);
        String request = "{\"type\":\"send_payload\",\"payloadHex\":\"" + compactHex + "\"}";
        String preview = HexUtil.toHexPreview(payload, LOG_HEX_MAX_BYTES);

        IOException lastError = null;

        for (int attempt = 1; attempt <= ProxyConstants.SEND_RETRY_ATTEMPTS; attempt++) {
            String response = sendJsonLine(request);

            Boolean ok = JsonUtil.jsonBoolean(response, "ok");
            if (Boolean.TRUE.equals(ok)) {
                log("CONTROL TX payload=" + preview);
                return;
            }

            Boolean sessionActive = JsonUtil.jsonBoolean(response, "sessionActive");
            Boolean injectReady = JsonUtil.jsonBoolean(response, "injectReady");
            String msg = JsonUtil.jsonString(response, "message");

            boolean transientNotReady =
                    "Session still starting / DSP stream not ready".equals(msg)
                            || (Boolean.TRUE.equals(sessionActive) && !Boolean.TRUE.equals(injectReady));

            if (transientNotReady && attempt < ProxyConstants.SEND_RETRY_ATTEMPTS) {
                log("CONTROL retry " + attempt + "/" + ProxyConstants.SEND_RETRY_ATTEMPTS + " inject not ready");
                WaitUtil.sleepMs(ProxyConstants.RETRY_DELAY_MS);
                continue;
            }

            lastError = new IOException("Control send_payload failed: " + extractError(response));
            break;
        }

        if (lastError != null) {
            throw lastError;
        }

        throw new IOException("Control send_payload failed");
    }

    private void requireOk(String request, String action) throws IOException {
        String response = sendJsonLine(request);
        Boolean ok = JsonUtil.jsonBoolean(response, "ok");

        if (Boolean.TRUE.equals(ok)) {
            return;
        }

        throw new IOException("Control " + action + " failed: " + extractError(response));
    }

    private String sendJsonLine(String json) throws IOException {
        writer.write(json);
        writer.write('\n');
        writer.flush();

        String response = reader.readLine();
        if (response == null) {
            throw new IOException("Control connection closed");
        }

        log("CONTROL RX " + abbreviate(response, LOG_JSON_MAX));
        return response;
    }

    private static String extractError(String response) {
        String err = JsonUtil.jsonString(response, "error");
        if (err != null && !err.isBlank()) {
            return abbreviate(err, 180);
        }

        String msg = JsonUtil.jsonString(response, "message");
        if (msg != null && !msg.isBlank()) {
            return abbreviate(msg, 180);
        }

        return abbreviate(response, 180);
    }

    private static String abbreviate(String text, int maxChars) {
        if (text == null) {
            return "null";
        }
        if (text.length() <= maxChars) {
            return text;
        }
        return text.substring(0, maxChars) + " ...";
    }

    private void log(String msg) {
        if (verbose) {
            System.err.println(msg);
        }
    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
