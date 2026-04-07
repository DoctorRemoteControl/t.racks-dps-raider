package de.drremote.dsp408.dump;

import de.drremote.dsp408.model.ProxyResponse;
import de.drremote.dsp408.proxy.ProxyClient;
import de.drremote.dsp408.proxy.ProxyConfig;
import de.drremote.dsp408.proxy.ProxyConstants;

public final class DSP408ProxyDump {

    private DSP408ProxyDump() {
    }

    public static DumpResult dumpAll(ProxyConfig config) throws DumpFailedException {
        DumpResult result = DumpResult.start();

        try (ProxyClient client = new ProxyClient(config)) {
            client.connect();
            client.resetSession();
            client.ensureSession();

            result.handshakeInit = DumpResult.FrameJson.fromResponse(
                    require(client.handshakeInit(), "handshake_init")
            );

            result.deviceInfo = DumpResult.FrameJson.fromResponse(
                    require(client.deviceInfo(), "device_info")
            );

            result.systemInfo = DumpResult.FrameJson.fromResponse(
                    require(client.systemInfo(), "system_info")
            );

            if (config.pin() != null) {
                result.login = DumpResult.FrameJson.fromResponse(
                        require(client.login(config.pin()), "login")
                );
            }

            for (int idx = ProxyConstants.BLOCK_START; idx <= ProxyConstants.BLOCK_END; idx++) {
                ProxyResponse resp = require(client.readBlock(idx), "read_block");
                result.blocks.add(DumpResult.BlockJson.fromResponse(idx, resp));
            }

            result.ok = true;
            return result;
        } catch (Exception e) {
            result.ok = false;
            result.error = compactMessage(e);
            throw new DumpFailedException(result, e);
        }
    }

    private static ProxyResponse require(ProxyResponse response, String label) {
        if (response == null) {
            throw new IllegalStateException("Missing DSP response for " + label);
        }
        return response;
    }

    private static String compactMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        String msg = root.getMessage();
        if (msg == null || msg.isBlank()) {
            msg = root.getClass().getSimpleName();
        }

        return abbreviate(msg, 180);
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
}
