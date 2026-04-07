package de.drremote.dsp408.dump;

public final class DumpFailedException extends Exception {
    private static final long serialVersionUID = 1L;

    private final DumpResult result;

    public DumpFailedException(DumpResult result, Throwable cause) {
        super(cause);
        this.result = result;
    }

    public DumpResult getResult() {
        return result;
    }
}