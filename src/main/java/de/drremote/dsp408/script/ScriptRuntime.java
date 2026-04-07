package de.drremote.dsp408.script;

import de.drremote.dsp408.model.GuiCaptureResult;
import de.drremote.dsp408.proxy.GuiSnifferClient;
import de.drremote.dsp408.proxy.ProxyClient;
import de.drremote.dsp408.proxy.ProxyConfig;

import java.util.LinkedHashMap;
import java.util.Map;

final class ScriptRuntime {
    final String defaultStreamHost;
    final int defaultStreamPort;
    final String defaultControlHost;
    final int defaultControlPort;
    final boolean verbose;

    final Map<String, Object> variables = new LinkedHashMap<>();

    private ProxyClient client;
    private GuiSnifferClient guiSniffer;

    ScriptRuntime(
            String defaultStreamHost,
            int defaultStreamPort,
            String defaultControlHost,
            int defaultControlPort,
            boolean verbose
    ) {
        this.defaultStreamHost = defaultStreamHost;
        this.defaultStreamPort = defaultStreamPort;
        this.defaultControlHost = defaultControlHost;
        this.defaultControlPort = defaultControlPort;
        this.verbose = verbose;
    }

    void connect(String streamHost, int streamPort, String controlHost, int controlPort) throws Exception {
        closeClient();
        ProxyConfig config = ProxyConfig.of(streamHost, streamPort, controlHost, controlPort, null, verbose);
        client = new ProxyClient(config);
        client.connect();
    }

    void connectGuiSniffer(String streamHost, int streamPort) throws Exception {
        closeGuiSniffer();
        guiSniffer = new GuiSnifferClient(streamHost, streamPort, verbose);
        guiSniffer.connect();
    }

    ProxyClient requireClient() {
        if (client == null) {
            throw new IllegalStateException("No proxy connection active. The script must run 'connect' first.");
        }
        return client;
    }

    GuiSnifferClient requireGuiSniffer() throws Exception {
        if (guiSniffer == null) {
            connectGuiSniffer(defaultStreamHost, defaultStreamPort);
        }
        return guiSniffer;
    }

    void beginGuiCapture() throws Exception {
        if (client != null) {
            client.beginCapture();
            return;
        }
        requireGuiSniffer().beginCapture();
    }

    GuiCaptureResult finishGuiCapture(long quietMs, long maxWaitMs) throws Exception {
        if (client != null) {
            return client.finishCapture(quietMs, maxWaitMs);
        }
        return requireGuiSniffer().finishCapture(quietMs, maxWaitMs);
    }

    GuiCaptureResult captureGuiAction(long actionWindowMs,
                                      long quietMs,
                                      long maxWaitMs,
                                      Integer... ignoredCommands) throws Exception {
        if (client != null) {
            return client.captureUntilAction(actionWindowMs, quietMs, maxWaitMs, ignoredCommands);
        }
        return requireGuiSniffer().captureUntilAction(actionWindowMs, quietMs, maxWaitMs, ignoredCommands);
    }

    void closeClient() throws Exception {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    void closeGuiSniffer() throws Exception {
        if (guiSniffer != null) {
            guiSniffer.close();
            guiSniffer = null;
        }
    }

    void closeAll() throws Exception {
        Exception first = null;

        try {
            closeClient();
        } catch (Exception e) {
            first = e;
        }

        try {
            closeGuiSniffer();
        } catch (Exception e) {
            if (first == null) {
                first = e;
            } else {
                first.addSuppressed(e);
            }
        }

        if (first != null) {
            throw first;
        }
    }
}
