package de.drremote.dsp408.tool;

import de.drremote.dsp408.dump.DSP408ProxyDump;
import de.drremote.dsp408.dump.DumpResult;
import de.drremote.dsp408.proxy.ProxyConfig;

public final class ReadTool {

    private ReadTool() {
    }

    public static String dumpAllJson(
            String streamHost,
            int streamPort,
            String controlHost,
            int controlPort,
            String pin,
            boolean verbose
    ) throws Exception {
        ProxyConfig config = ProxyConfig.of(
                streamHost,
                streamPort,
                controlHost,
                controlPort,
                pin,
                verbose
        );

        DumpResult result = DSP408ProxyDump.dumpAll(config);
        return result.toJson();
    }
}
