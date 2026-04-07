package de.drremote.dsp408.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class GuiCaptureDaemonCli {
    private GuiCaptureDaemonCli() {
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

        try (Socket socket = new Socket(options.host, options.port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            writer.write(options.command);
            writer.write('\n');
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("GUI Capture Daemon CLI");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -cp tracks-dsp-raider-0.0.1-SNAPSHOT-all.jar de.drremote.dsp408.script.GuiCaptureDaemonCli arm");
        System.out.println("  java -cp tracks-dsp-raider-0.0.1-SNAPSHOT-all.jar de.drremote.dsp408.script.GuiCaptureDaemonCli finish 1800 12000");
        System.out.println("  java -cp tracks-dsp-raider-0.0.1-SNAPSHOT-all.jar de.drremote.dsp408.script.GuiCaptureDaemonCli stop");
    }

    private static final class Options {
        private String host = "127.0.0.1";
        private int port = GuiCaptureDaemon.DEFAULT_PORT;
        private String command;
        private boolean help;

        private static Options parse(String[] args) {
            Options options = new Options();
            int i = 0;
            while (i < args.length) {
                String arg = args[i];
                switch (arg) {
                    case "--host" -> options.host = requireValue(args, ++i, "--host");
                    case "--port" -> options.port = Integer.parseInt(requireValue(args, ++i, "--port"));
                    case "--help", "-h" -> options.help = true;
                    default -> {
                        StringBuilder sb = new StringBuilder(arg);
                        for (int j = i + 1; j < args.length; j++) {
                            sb.append(' ').append(args[j]);
                        }
                        options.command = sb.toString();
                        i = args.length;
                        continue;
                    }
                }
                i++;
            }

            if (!options.help && (options.command == null || options.command.isBlank())) {
                throw new IllegalArgumentException("Command is missing");
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
