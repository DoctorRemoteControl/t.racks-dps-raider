package de.drremote.dsp408.proxy;

public record ProxyConfig(
        String streamHost,
        int streamPort,
        String controlHost,
        int controlPort,
        String pin,
        boolean verbose
) {
    public static ProxyConfig of(
            String streamHost,
            int streamPort,
            String controlHost,
            int controlPort,
            String pin,
            boolean verbose
    ) {
        if (streamHost == null || streamHost.isBlank()) {
            throw new IllegalArgumentException("streamHost must not be blank");
        }
        if (controlHost == null || controlHost.isBlank()) {
            throw new IllegalArgumentException("controlHost must not be blank");
        }
        if (streamPort <= 0 || streamPort > 65535) {
            throw new IllegalArgumentException("streamPort must be between 1 and 65535");
        }
        if (controlPort <= 0 || controlPort > 65535) {
            throw new IllegalArgumentException("controlPort must be between 1 and 65535");
        }
        if (pin != null && !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }

        return new ProxyConfig(
                streamHost,
                streamPort,
                controlHost,
                controlPort,
                pin,
                verbose
        );
    }
}
