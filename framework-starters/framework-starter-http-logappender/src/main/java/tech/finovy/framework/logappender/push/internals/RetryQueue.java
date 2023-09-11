package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.entry.PushBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryQueue.class);
    private final DelayQueue<PushBatch> retryBatches = new DelayQueue<>();
    private final AtomicInteger putsInProgress;
    private volatile boolean closed;

    public RetryQueue() {
        this.putsInProgress = new AtomicInteger(0);
        this.closed = false;
    }

    public void put(PushBatch batch) {
        putsInProgress.incrementAndGet();
        try {
            if (closed) {
                throw new IllegalStateException("cannot put after the retry queue was closed");
            }
            retryBatches.put(batch);
        } finally {
            putsInProgress.decrementAndGet();
        }
    }

    public List<PushBatch> expiredBatches(long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        List<PushBatch> expiredBatches = new ArrayList<>();
        retryBatches.drainTo(expiredBatches);
        if (!expiredBatches.isEmpty()) {
            return expiredBatches;
        }
        while (true) {
            if (timeoutMs < 0) {
                break;
            }
            PushBatch batch;
            try {
                batch = retryBatches.poll(timeoutMs, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted when poll batch from the retry batches");
                Thread.currentThread().interrupt();
                break;
            }
            if (batch == null) {
                break;
            }
            expiredBatches.add(batch);
            retryBatches.drainTo(expiredBatches);
            if (!expiredBatches.isEmpty()) {
                break;
            }
            timeoutMs = deadline - System.currentTimeMillis();
        }
        return expiredBatches;
    }

    public List<PushBatch> remainingBatches() {
        if (!closed) {
            throw new IllegalStateException("cannot get the remaining batches before the retry queue closed");
        }
        while (true) {
            if (!putsInProgress()) {
                break;
            }
        }
        List<PushBatch> remainingBatches = new ArrayList<>(retryBatches);
        retryBatches.clear();
        return remainingBatches;
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        this.closed = true;
    }

    private boolean putsInProgress() {
        return putsInProgress.get() > 0;
    }
}
