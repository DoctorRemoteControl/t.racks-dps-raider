package de.drremote.dsp408.tool;

import de.drremote.dsp408.model.ProxyResponse;
import de.drremote.dsp408.proxy.ProxyConstants;
import de.drremote.dsp408.util.HexUtil;

public final class DspWriteCli {

    private DspWriteCli() {
    }

    public static void main(String[] args) {
        Options options;
        try {
            options = Options.parse(args);
        } catch (IllegalArgumentException ex) {
            System.err.println("Error: " + ex.getMessage());
            System.err.println();
            printUsage();
            System.exit(2);
            return;
        }

        if (options.help) {
            printUsage();
            return;
        }

        try {
            ProxyResponse response = WriteTool.sendPayloadOnce(
                    options.streamHost,
                    options.streamPort,
                    options.controlHost,
                    options.controlPort,
                    options.pin,
                    options.verbose,
                    HexUtil.hexToBytes(options.payloadHex),
                    options.expectedCommand,
                    options.strictResponse,
                    options.label
            );

            if (response == null) {
                System.out.println("No response received.");
                return;
            }

            System.out.println("cmd=" + response.commandHex());
            System.out.println("len=" + response.payloadLen());
            System.out.println("payload_hex=" + response.payloadHex());
            System.out.println("raw_hex=" + response.rawHex());
            System.out.println("checksum_ok=" + response.checksumOk());
            if (response.readBlockIndex() != null) {
                System.out.println("read_block_index=" + String.format("0x%02X", response.readBlockIndex()));
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("DSP408 Writer CLI");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar tracks-dsp-writer-all.jar --payload \"00 01 03 35 04 01\"");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --payload <hex>            Raw DSP payload to inject");
        System.out.println("  --stream-host <host>       Stream host (default: 127.0.0.1)");
        System.out.println("  --stream-port <port>       Stream port (default: 19081)");
        System.out.println("  --control-host <host>      Control host (default: 127.0.0.1)");
        System.out.println("  --control-port <port>      Control port (default: 19082)");
        System.out.println("  --pin <1234>               Optional 4-digit login PIN");
        System.out.println("  --expected-command <cmd>   Expected response command, e.g. 0x24 or 36");
        System.out.println("  --label <text>             Transaction label for logs");
        System.out.println("  --non-strict               Allow missing/unexpected response");
        System.out.println("  --verbose                  More logs");
        System.out.println("  --help                     Help");
    }

    private static final class Options {
        private String streamHost = ProxyConstants.DEFAULT_STREAM_HOST;
        private int streamPort = ProxyConstants.DEFAULT_STREAM_PORT;
        private String controlHost = ProxyConstants.DEFAULT_CONTROL_HOST;
        private int controlPort = ProxyConstants.DEFAULT_CONTROL_PORT;
        private String pin;
        private String payloadHex;
        private Integer expectedCommand;
        private String label = "write_payload_once";
        private boolean strictResponse = true;
        private boolean verbose;
        private boolean help;

        private static Options parse(String[] args) {
            Options options = new Options();

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "--payload" -> options.payloadHex = requireValue(args, ++i, "--payload");
                    case "--stream-host" -> options.streamHost = requireValue(args, ++i, "--stream-host");
                    case "--stream-port" -> options.streamPort = Integer.parseInt(requireValue(args, ++i, "--stream-port"));
                    case "--control-host" -> options.controlHost = requireValue(args, ++i, "--control-host");
                    case "--control-port" -> options.controlPort = Integer.parseInt(requireValue(args, ++i, "--control-port"));
                    case "--pin" -> options.pin = requireValue(args, ++i, "--pin");
                    case "--expected-command" -> options.expectedCommand = parseFlexibleInt(requireValue(args, ++i, "--expected-command"));
                    case "--label" -> options.label = requireValue(args, ++i, "--label");
                    case "--non-strict" -> options.strictResponse = false;
                    case "--verbose" -> options.verbose = true;
                    case "--help", "-h" -> options.help = true;
                    default -> throw new IllegalArgumentException("Unknown option: " + arg);
                }
            }

            if (!options.help && (options.payloadHex == null || options.payloadHex.isBlank())) {
                throw new IllegalArgumentException("--payload is missing");
            }

            return options;
        }

        private static int parseFlexibleInt(String text) {
            String value = text.trim();
            if (value.startsWith("0x") || value.startsWith("0X")) {
                return Integer.parseInt(value.substring(2), 16);
            }
            return Integer.parseInt(value);
        }

        private static String requireValue(String[] args, int index, String option) {
            if (index >= args.length) {
                throw new IllegalArgumentException("Missing value for " + option);
            }
            return args[index];
        }
    }
}
