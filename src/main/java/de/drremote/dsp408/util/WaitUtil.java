package de.drremote.dsp408.util;

import java.io.IOException;

public final class WaitUtil {
    private WaitUtil() {
    }

    public static void sleepMs(long ms) throws IOException {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while waiting", e);
        }
    }
}
