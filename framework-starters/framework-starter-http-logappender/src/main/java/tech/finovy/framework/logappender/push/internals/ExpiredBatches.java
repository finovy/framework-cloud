package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.entry.PushBatch;

import java.util.ArrayList;
import java.util.List;

public class ExpiredBatches {

    private final List<PushBatch> batches = new ArrayList<>();

    private long remainingMs;

    public List<PushBatch> getBatches() {
        return batches;
    }

    public void add(PushBatch pushBatch) {
        if (!batches.add(pushBatch)) {
            throw new IllegalStateException("failed to add producer batch to expired batches");
        }
    }

    public long getRemainingMs() {
        return remainingMs;
    }

    public void setRemainingMs(long remainingMs) {
        this.remainingMs = remainingMs;
    }
}
