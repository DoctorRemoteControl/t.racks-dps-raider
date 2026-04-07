package de.drremote.dsp408.raw;

public final class DspRawWriteTool {

    public static void main(String[] args) {
        CliOptions options;
        try {
            options = CliOptions.parse(args);
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

        if (options.payloadHex == null && options.frameHex == null) {
            System.err.println("Error: Either --payload or --frame must be specified.");
            System.err.println();
            printUsage();
            System.exit(2);
            return;
        }

        try (RawSocketClient client = new RawSocketClient(
                options.host,
                options.port,
                options.connectTimeoutMs,
                options.readTimeoutMs,
                options.verbose)) {

            client.connect();

            if (options.handshake) {
                client.sendPayloadAndRead("handshake_init", RawFrameCodec.hex("00 01 01 10"), 1);
                client.sendPayloadAndRead("device_info", RawFrameCodec.hex("00 01 01 13"), 1);
                client.sendPayloadAndRead("system_info", RawFrameCodec.hex("00 01 01 2C"), 1);
            }

            if (options.loginPin != null) {
                client.sendPayloadAndRead("login", RawFrameCodec.buildLoginPayload(options.loginPin), 1);
            }

            if (options.payloadHex != null) {
                client.sendPayloadAndRead("custom_payload", RawFrameCodec.hex(options.payloadHex), options.responses);
            } else {
                client.sendFrameAndRead("custom_frame", RawFrameCodec.hex(options.frameHex), options.responses);
            }

        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("DSP408 Raw Write Tool");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java de.drremote.dsp408.raw.DspRawWriteTool --host 192.168.0.166 --port 9761 --payload \"00 01 03 35 04 01\"");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --host <ip>                 Target IP (default: 192.168.0.166)");
        System.out.println("  --port <port>               Target port (default: 9761)");
        System.out.println("  --payload <hex>             Raw payload without frame");
        System.out.println("  --frame <hex>               Full frame including DLE/STX/ETX/XOR");
        System.out.println("  --handshake                 send handshake_init + device_info + system_info");
        System.out.println("  --login <1234>              Login with 4-digit PIN");
        System.out.println("  --responses <n>             Number of response frames to read (default: 1)");
        System.out.println("  --connect-timeout-ms <ms>   Connect timeout (default: 2000)");
        System.out.println("  --read-timeout-ms <ms>      Read timeout (default: 1200)");
        System.out.println("  --verbose                   Additional logs");
        System.out.println("  --help                      Help");
        System.out.println();
        System.out.println("Notes:");
        System.out.println("  - With --payload, the payload must already be complete:");
        System.out.println("      <dir> <fixed?> <len> <cmd> <args...>");
        System.out.println("  - The tool then builds automatically:");
        System.out.println("      10 02 + payload + 10 03 + xor");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  1) Mute Out1:");
        System.out.println("     java de.drremote.dsp408.raw.DspRawWriteTool --handshake --login 1234 --payload \"00 01 03 35 04 01\"");
        System.out.println();
        System.out.println("  2) Out1 unmute:");
        System.out.println("     java de.drremote.dsp408.raw.DspRawWriteTool --handshake --login 1234 --payload \"00 01 03 35 04 00\"");
        System.out.println();
        System.out.println("  3) Set Out1 phase to 180:");
        System.out.println("     java de.drremote.dsp408.raw.DspRawWriteTool --handshake --login 1234 --payload \"00 01 03 36 04 01\"");
        System.out.println();
        System.out.println("  4) Set Out1 gain to 0.0 dB (raw 280 = 0x0118 LE):");
        System.out.println("     java de.drremote.dsp408.raw.DspRawWriteTool --handshake --login 1234 --payload \"00 01 04 34 04 18 01\"");
        System.out.println();
        System.out.println("  5) Set Out1 delay to 20.0 ms (raw 1920 = 0x0780 LE):");
        System.out.println("     java de.drremote.dsp408.raw.DspRawWriteTool --handshake --login 1234 --payload \"00 01 04 38 04 80 07\"");
        System.out.println();
        System.out.println("Channel indices (likely):");
        System.out.println("  InA=0 InB=1 InC=2 InD=3 Out1=4 Out2=5 Out3=6 Out4=7 Out5=8 Out6=9 Out7=10 Out8=11");
    }

    private static final class CliOptions {
        String host = "192.168.0.166";
        int port = 9761;
        String payloadHex;
        String frameHex;
        boolean handshake;
        String loginPin;
        int responses = 1;
        int connectTimeoutMs = 2000;
        int readTimeoutMs = 1200;
        boolean verbose;
        boolean help;

        static CliOptions parse(String[] args) {
            CliOptions o = new CliOptions();

            for (int i = 0; i < args.length; i++) {
                String a = args[i];
                switch (a) {
                    case "--host":
                        o.host = requireValue(args, ++i, "--host");
                        break;
                    case "--port":
                        o.port = Integer.parseInt(requireValue(args, ++i, "--port"));
                        break;
                    case "--payload":
                        o.payloadHex = requireValue(args, ++i, "--payload");
                        break;
                    case "--frame":
                        o.frameHex = requireValue(args, ++i, "--frame");
                        break;
                    case "--handshake":
                        o.handshake = true;
                        break;
                    case "--login":
                        o.loginPin = requireValue(args, ++i, "--login");
                        break;
                    case "--responses":
                        o.responses = Integer.parseInt(requireValue(args, ++i, "--responses"));
                        break;
                    case "--connect-timeout-ms":
                        o.connectTimeoutMs = Integer.parseInt(requireValue(args, ++i, "--connect-timeout-ms"));
                        break;
                    case "--read-timeout-ms":
                        o.readTimeoutMs = Integer.parseInt(requireValue(args, ++i, "--read-timeout-ms"));
                        break;
                    case "--verbose":
                        o.verbose = true;
                        break;
                    case "--help":
                    case "-h":
                        o.help = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown option: " + a);
                }
            }

            if (o.payloadHex != null && o.frameHex != null) {
                throw new IllegalArgumentException("--payload and --frame are mutually exclusive");
            }
            if (o.responses < 0) {
                throw new IllegalArgumentException("--responses must be >= 0");
            }

            return o;
        }

        private static String requireValue(String[] args, int index, String opt) {
            if (index >= args.length) {
                throw new IllegalArgumentException("Missing value for " + opt);
            }
            return args[index];
        }
    }
}
