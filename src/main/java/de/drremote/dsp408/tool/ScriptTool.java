package de.drremote.dsp408.tool;

import de.drremote.dsp408.script.DspScriptEngine;

import java.nio.file.Path;

public final class ScriptTool {
    private ScriptTool() {
    }

    public static void runScriptFile(
            String streamHost,
            int streamPort,
            String controlHost,
            int controlPort,
            boolean verbose,
            Path file
    ) throws Exception {
        new DspScriptEngine(
                streamHost,
                streamPort,
                controlHost,
                controlPort,
                verbose
        ).execute(file);
    }

    public static void runScriptText(
            String streamHost,
            int streamPort,
            String controlHost,
            int controlPort,
            boolean verbose,
            String scriptText
    ) throws Exception {
        new DspScriptEngine(
                streamHost,
                streamPort,
                controlHost,
                controlPort,
                verbose
        ).executeText(scriptText);
    }
}
