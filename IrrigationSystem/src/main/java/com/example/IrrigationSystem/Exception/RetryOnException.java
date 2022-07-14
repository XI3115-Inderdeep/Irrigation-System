package com.example.IrrigationSystem.Exception;

public class RetryOnException {
    public static final int DEFAULT_RETRIES = 3;
    public static final long DEFAULT_TIME_TO_WAIT_MS = 10000;

    private int numRetries;
    private long timeToWaitMS;

    public RetryOnException(int _numRetries,
                            long _timeToWaitMS) {
        numRetries = _numRetries;
        timeToWaitMS = _timeToWaitMS;
    }

    public RetryOnException() {
        this(DEFAULT_RETRIES, DEFAULT_TIME_TO_WAIT_MS);
    }

    public boolean shouldRetry() {
        return (numRetries >= 0);
    }

    public void waitUntilNextTry() {
        try {
            Thread.sleep(timeToWaitMS);
        } catch (InterruptedException iex) {
        }
    }

    public void exceptionOccurred() throws Exception {
        numRetries--;
        if (!shouldRetry()) {
            throw new Exception("Retry limit exceeded. ALERTING SYSTEM");
        }
        waitUntilNextTry();
    }
}
