package de.drremote.dsp408.tool;

import de.drremote.dsp408.proxy.ProxyConstants;

import java.nio.file.Files;
import java.nio.file.Path;

public final class DspReadCli {

    private DspReadCli() {
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
            String json = ReadTool.dumpAllJson(
                    options.streamHost,
                    options.streamPort,
                    options.controlHost,
                    options.controlPort,
                    options.pin,
                    options.verbose
            );

            if (options.outFile != null) {
                Path parent = options.outFile.toAbsolutePath().getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.writeString(options.outFile, json);
                System.out.println("Dump saved to " + options.outFile.toAbsolutePath());
            } else {
                System.out.println(json);
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("DSP408 Reader CLI");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -jar tracks-dsp-reader-all.jar");
        System.out.println("  java -jar tracks-dsp-reader-all.jar --pin 1234 --out out\\\\dump.json");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --stream-host <host>       Stream host (default: 127.0.0.1)");
        System.out.println("  --stream-port <port>       Stream port (default: 19081)");
        System.out.println("  --control-host <host>      Control host (default: 127.0.0.1)");
        System.out.println("  --control-port <port>      Control port (default: 19082)");
        System.out.println("  --pin <1234>               Optional 4-digit login PIN");
        System.out.println("  --out <path>               Save JSON to file instead of stdout");
        System.out.println("  --verbose                  More logs");
        System.out.println("  --help                     Help");
    }

    private static final class Options {
        private String streamHost = ProxyConstants.DEFAULT_STREAM_HOST;
        private int streamPort = ProxyConstants.DEFAULT_STREAM_PORT;
        private String controlHost = ProxyConstants.DEFAULT_CONTROL_HOST;
        private int controlPort = ProxyConstants.DEFAULT_CONTROL_PORT;
        private String pin;
        private Path outFile;
        private boolean verbose;
        private boolean help;

        private static Options parse(String[] args) {
            Options options = new Options();

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "--stream-host" -> options.streamHost = requireValue(args, ++i, "--stream-host");
                    case "--stream-port" -> options.streamPort = Integer.parseInt(requireValue(args, ++i, "--stream-port"));
                    case "--control-host" -> options.controlHost = requireValue(args, ++i, "--control-host");
                    case "--control-port" -> options.controlPort = Integer.parseInt(requireValue(args, ++i, "--control-port"));
                    case "--pin" -> options.pin = requireValue(args, ++i, "--pin");
                    case "--out" -> options.outFile = Path.of(requireValue(args, ++i, "--out"));
                    case "--verbose" -> options.verbose = true;
                    case "--help", "-h" -> options.help = true;
                    default -> throw new IllegalArgumentException("Unknown option: " + arg);
                }
            }

            return options;
        }

        private static String requireValue(String[] args, int index, String option) {
            if (index >= args.length) {
                throw new IllegalArgumentException("Missing value for " + option);
            }
            return args[index];
        }
    }
}
