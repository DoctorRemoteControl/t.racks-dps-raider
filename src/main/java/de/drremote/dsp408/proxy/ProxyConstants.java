package de.drremote.dsp408.proxy;

public final class ProxyConstants {
    public static final String DEFAULT_STREAM_HOST = "127.0.0.1";
    public static final int DEFAULT_STREAM_PORT = 19081;

    public static final String DEFAULT_CONTROL_HOST = "127.0.0.1";
    public static final int DEFAULT_CONTROL_PORT = 19082;

    public static final int CONTROL_CONNECT_TIMEOUT_MS = 2000;
    public static final int CONTROL_READ_TIMEOUT_MS = 5000;
    public static final int STREAM_CONNECT_TIMEOUT_MS = 120000;
    public static final int READY_WAIT_TIMEOUT_MS = 10000;
    public static final int TRANSACTION_TIMEOUT_MS = 5000;
    public static final int RETRY_DELAY_MS = 150;
    public static final int SEND_RETRY_ATTEMPTS = 20;
    public static final int CONTROL_TX_LEASE_MS = 7000;

    public static final int BLOCK_START = 0x00;
    public static final int BLOCK_END = 0x1C;

    private ProxyConstants() {
    }
}
