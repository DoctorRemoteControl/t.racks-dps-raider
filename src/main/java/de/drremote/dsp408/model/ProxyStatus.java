package de.drremote.dsp408.model;

public final class ProxyStatus {
    private final boolean sessionActive;
    private final boolean injectReady;
    private final String rawResponse;

    public ProxyStatus(boolean sessionActive, boolean injectReady, String rawResponse) {
        this.sessionActive = sessionActive;
        this.injectReady = injectReady;
        this.rawResponse = rawResponse;
    }

    public boolean sessionActive() {
        return sessionActive;
    }

    public boolean injectReady() {
        return injectReady;
    }

    public String rawResponse() {
        return rawResponse;
    }

    @Override
    public String toString() {
        return "status{active=" + sessionActive + ", ready=" + injectReady + "}";
    }
}
