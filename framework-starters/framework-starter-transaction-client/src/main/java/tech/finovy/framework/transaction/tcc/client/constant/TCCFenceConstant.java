package tech.finovy.framework.transaction.tcc.client.constant;

public class TCCFenceConstant {

    /**
     * PHASE 1: The Commit tried.
     */
    public static final int STATUS_TRIED = 1;
    /**
     * PHASE 2: The Committed.
     */
    public static final int STATUS_COMMITTED = 2;
    /**
     * PHASE 2: The Rollbacked.
     */
    public static final int STATUS_ROLLBACKED = 3;
    /**
     * Suspended status.
     */
    public static final int STATUS_SUSPENDED = 4;

    private TCCFenceConstant() {
    }
}
