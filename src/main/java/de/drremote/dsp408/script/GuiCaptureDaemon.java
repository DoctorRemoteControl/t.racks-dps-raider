package de.drremote.dsp408.script;

import de.drremote.dsp408.model.GuiCaptureResult;
import de.drremote.dsp408.model.SniffedFrame;
import de.drremote.dsp408.proxy.GuiSnifferClient;
import de.drremote.dsp408.proxy.ProxyConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class GuiCaptureDaemon {
    public static final int DEFAULT_PORT = 19091;

    private final GuiSnifferClient sniffer;
    private final ServerSocket serverSocket;
    private volatile boolean running = true;

    private GuiCaptureDaemon(String streamHost, int streamPort, int controlPort, boolean verbose) throws Exception {
        this.sniffer = new GuiSnifferClient(streamHost, streamPort, verbose);
        this.sniffer.connect();
        this.serverSocket = new ServerSocket(controlPort, 50, InetAddress.getByName("127.0.0.1"));
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
            new GuiCaptureDaemon(options.streamHost, options.streamPort, options.port, options.verbose).run();
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private void run() throws Exception {
        try (serverSocket; sniffer) {
            System.out.println("GUI capture daemon listening on 127.0.0.1:" + serverSocket.getLocalPort());
            System.out.flush();

            while (running) {
                try (Socket socket = serverSocket.accept()) {
                    handle(socket);
                }
            }
        }
    }

    private void handle(Socket socket) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        String line = reader.readLine();
        if (line == null || line.isBlank()) {
            writer.write("ERROR empty command\n");
            writer.flush();
            return;
        }

        String[] parts = line.trim().split("\\s+");
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "arm" -> {
                sniffer.beginCapture();
                writer.write("OK armed\n");
            }
            case "finish" -> {
                long quietMs = parts.length >= 2 ? Long.parseLong(parts[1]) : 1800L;
                long maxWaitMs = parts.length >= 3 ? Long.parseLong(parts[2]) : 12000L;

                GuiCaptureResult capture = sniffer.finishCapture(quietMs, maxWaitMs);
                SniffedFrame interesting = capture.lastWriteExcluding(0x40);
                List<SniffedFrame> recentInteresting = capture.recentWritesExcluding(20, 0x40);

                writer.write("OK capture\n");
                writer.write(ScriptFunctions.displayValue(capture));
                writer.write("\n");

                if (capture.lastWrite() != null) {
                    writer.write("LAST_WRITE " + capture.lastWrite().payloadHex() + "\n");
                    writer.write("LAST_CMD " + capture.lastWrite().commandHex() + "\n");
                } else {
                    writer.write("LAST_WRITE null\n");
                    writer.write("LAST_CMD null\n");
                }

                if (interesting != null) {
                    writer.write("LAST_INTERESTING_WRITE " + interesting.payloadHex() + "\n");
                    writer.write("LAST_INTERESTING_CMD " + interesting.commandHex() + "\n");
                } else {
                    writer.write("LAST_INTERESTING_WRITE null\n");
                    writer.write("LAST_INTERESTING_CMD null\n");
                }

                writer.write("RECENT_INTERESTING_WRITES " + recentInteresting.size() + "\n");
                for (int i = 0; i < recentInteresting.size(); i++) {
                    SniffedFrame frame = recentInteresting.get(i);
                    writer.write("RECENT_" + i + "_CMD " + frame.commandHex() + "\n");
                    writer.write("RECENT_" + i + "_WRITE " + frame.payloadHex() + "\n");
                }
            }
            case "stop" -> {
                running = false;
                writer.write("OK stopping\n");
                serverSocket.close();
            }
            default -> writer.write("ERROR unknown command\n");
        }

        writer.flush();
    }

    private static void printUsage() {
        System.out.println("GUI Capture Daemon");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -cp tracks-dsp-raider-0.0.1-SNAPSHOT-all.jar de.drremote.dsp408.script.GuiCaptureDaemon");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --stream-host <host>       Stream host (default: 127.0.0.1)");
        System.out.println("  --stream-port <port>       Stream port (default: 19081)");
        System.out.println("  --port <port>              Control port for the daemon (default: 19091)");
        System.out.println("  --verbose                  More logs");
        System.out.println("  --help                     Help");
    }

    private static final class Options {
        private String streamHost = ProxyConstants.DEFAULT_STREAM_HOST;
        private int streamPort = ProxyConstants.DEFAULT_STREAM_PORT;
        private int port = DEFAULT_PORT;
        private boolean verbose;
        private boolean help;

        private static Options parse(String[] args) {
            Options options = new Options();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "--stream-host" -> options.streamHost = requireValue(args, ++i, "--stream-host");
                    case "--stream-port" -> options.streamPort = Integer.parseInt(requireValue(args, ++i, "--stream-port"));
                    case "--port" -> options.port = Integer.parseInt(requireValue(args, ++i, "--port"));
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
