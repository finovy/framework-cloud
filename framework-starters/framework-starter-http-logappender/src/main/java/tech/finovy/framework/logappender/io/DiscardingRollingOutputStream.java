package tech.finovy.framework.logappender.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class DiscardingRollingOutputStream extends OutputStream {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final ReentrantLock currentBucketLock = new ReentrantLock();
    private final BlockingDeque<ByteArrayOutputStream> filledBuckets;
    private final ConcurrentLinkedQueue<ByteArrayOutputStream> recycledBucketPool;
    private final long maxBucketSizeInBytes;
    private final AtomicInteger discardedBucketCount = new AtomicInteger();
    private ByteArrayOutputStream currentBucket;

    /**
     * @param maxBucketSizeInBytes maximum byte size of each bucket
     * @param maxBucketCount       maximum number of buckets
     */
    public DiscardingRollingOutputStream(int maxBucketSizeInBytes, int maxBucketCount) {
        if (maxBucketCount < 2) {
            throw new IllegalArgumentException("'maxBucketCount' must be >1");
        }

        this.maxBucketSizeInBytes = maxBucketSizeInBytes;
        this.filledBuckets = new LinkedBlockingDeque<>(maxBucketCount);

        this.recycledBucketPool = new ConcurrentLinkedQueue<>();
        this.currentBucket = newBucket();
    }


    @Override
    public void write(int b) {
        currentBucketLock.lock();
        try {
            currentBucket.write(b);
            rollCurrentBucketIfNeeded();
        } finally {
            currentBucketLock.unlock();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        currentBucketLock.lock();
        try {
            currentBucket.write(b);
            rollCurrentBucketIfNeeded();
        } finally {
            currentBucketLock.unlock();
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        currentBucketLock.lock();
        try {
            currentBucket.write(b, off, len);
            rollCurrentBucketIfNeeded();
        } finally {
            currentBucketLock.unlock();
        }
    }

    @Override
    public void flush() throws IOException {
        currentBucketLock.lock();
        try {
            currentBucket.flush();
        } finally {
            currentBucketLock.unlock();
        }
    }

    /**
     * Close all the underlying buckets (current bucket, filled buckets and buckets from the recycled buckets pool).
     */
    @Override
    public void close() {
        // no-op as ByteArrayOutputStream#close() is no op
    }

    /**
     * Roll current bucket if size threshold has been reached.
     */
    private void rollCurrentBucketIfNeeded() {
        if (currentBucket.size() < maxBucketSizeInBytes) {
            return;
        }
        rollCurrentBucket();
    }

    /**
     * Roll current bucket if size threshold has been reached.
     */
    public void rollCurrentBucketIfNotEmpty() {
        if (currentBucket.size() == 0) {
            return;
        }
        rollCurrentBucket();
    }

    /**
     * Moves the current active bucket to the list of filled buckets and defines a new one.
     * <p>
     * The new active bucket is reused from the {@link #recycledBucketPool} pool if one is available or recreated.
     */
    public void rollCurrentBucket() {
        currentBucketLock.lock();
        try {
            boolean offered = filledBuckets.offer(currentBucket);
            if (offered) {
                onBucketRoll(currentBucket);
            } else {
                onBucketDiscard(currentBucket);
                discardedBucketCount.incrementAndGet();
            }
            currentBucket = newBucket();
        } finally {
            currentBucketLock.unlock();
        }
    }

    /**
     * Designed for extension.
     *
     * @param discardedBucket the discarded bucket
     */
    protected void onBucketDiscard(ByteArrayOutputStream discardedBucket) {

    }

    /**
     * The rolled bucket. Designed for extension.
     *
     * @param rolledBucket the discarded bucket
     */
    protected void onBucketRoll(ByteArrayOutputStream rolledBucket) {

    }

    /**
     * Get a new bucket from the {@link #recycledBucketPool} or instantiate a new one if none available
     * in the free bucket pool.
     *
     * @return the bucket ready to use
     */
    protected ByteArrayOutputStream newBucket() {
        ByteArrayOutputStream bucket = recycledBucketPool.poll();
        if (bucket == null) {
            bucket = new ByteArrayOutputStream();
        }
        return bucket;
    }

    /**
     * Returns the given bucket to the pool of free buckets.
     *
     * @param bucket the bucket to recycle
     */
    public void recycleBucket(ByteArrayOutputStream bucket) {
        bucket.reset();
        recycledBucketPool.offer(bucket);
    }

    /**
     * Return the filled buckets
     */
    public BlockingDeque<ByteArrayOutputStream> getFilledBuckets() {
        return filledBuckets;
    }

    /**
     * @return Number of discarded buckets. Monitoring oriented metric.
     */
    public int getDiscardedBucketCount() {
        return discardedBucketCount.get();
    }

    public long getCurrentOutputStreamSize() {
        long sizeInBytes = 0;
        for (ByteArrayOutputStream bucket : filledBuckets) {
            sizeInBytes += bucket.size();
        }
        sizeInBytes += currentBucket.size();
        return sizeInBytes;
    }

    @Override
    public String toString() {
        return "DiscardingRollingOutputStream{" +
                "currentBucket.bytesWritten=" + currentBucket.size() +
                ", filledBuckets.size=" + filledBuckets.size() +
                ", discardedBucketCount=" + discardedBucketCount +
                ", recycledBucketPool.size=" + recycledBucketPool.size() +
                '}';
    }
}
