package tech.finovy.framework.logappender.push.http;

public class ClientConnectionStatus {
    final String mIpAddress;
    long mCreateTimeNano;
    long mLastUsedTimeNano;
    long mSendDataSize;
    long mPullDataSize;
    boolean mIsValid;

    public ClientConnectionStatus(String ip) {
        mIpAddress = ip;
        mCreateTimeNano = System.nanoTime();
        mLastUsedTimeNano = 0;
        mSendDataSize = 0;
        mPullDataSize = 0;
        mIsValid = true;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void addSendDataSize(long dataSize) {
        mSendDataSize += dataSize;
    }

    public long getSendDataSize() {
        return mSendDataSize;
    }

    public void addPullDataSize(long dataSize) {
        mPullDataSize += dataSize;
    }

    public long getPullDataSize() {
        return mPullDataSize;
    }

    public long getCreateTime() {
        return mCreateTimeNano;
    }

    public boolean isValidConnection() {
        return mIsValid && mIpAddress != null && !mIpAddress.isEmpty();
    }

    public void disableConnection() {
        mIsValid = false;
    }

    public void updateLastUsedTime(long curTime) {
        mLastUsedTimeNano = curTime;
    }

    public long getLastUsedTime() {
        return mLastUsedTimeNano;
    }
}
