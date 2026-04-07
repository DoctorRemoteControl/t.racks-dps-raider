package de.drremote.dsp408.tool;

import de.drremote.dsp408.model.ProxyResponse;
import de.drremote.dsp408.proxy.ProxyClient;
import de.drremote.dsp408.proxy.ProxyConfig;

public final class WriteTool {

    private WriteTool() {
    }

    public static ProxyResponse sendPayloadOnce(
            String streamHost,
            int streamPort,
            String controlHost,
            int controlPort,
            String pin,
            boolean verbose,
            byte[] payload,
            Integer expectedCommand,
            boolean strictResponse,
            String label
    ) throws Exception {
        if (payload == null || payload.length == 0) {
            throw new IllegalArgumentException("Payload must not be empty.");
        }

        ProxyConfig config = ProxyConfig.of(
                streamHost,
                streamPort,
                controlHost,
                controlPort,
                pin,
                verbose
        ); // nur zur Validierung

        try (ProxyClient client = new ProxyClient(config)) {

            client.prepareSession(pin);

            return client.sendPayload(
                    payload,
                    expectedCommand,
                    strictResponse,
                    label == null || label.isBlank() ? "write_payload_once" : label
            );
        }
    }
}
