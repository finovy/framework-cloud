package tech.finovy.framework.transaction.tcc.client.fence;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TCCFenceStore {

    /**
     * Query tcc fence do.
     *
     * @param conn     the connection
     * @param xid      the global transaction id
     * @param branchId the branch transaction id
     * @return the tcc fence do
     */
    TCCFenceDO queryTCCFenceDO(Connection conn, String xid, Long branchId);

    /**
     * Query xid.
     *
     * @param conn     the connection
     * @param datetime the datetime
     * @param limit    the limit size
     * @return the tcc fence do
     */
    Set<String> queryEndStatusXidsByDate(Connection conn, Date datetime, int limit);

    /**
     * Insert tcc fence do boolean.
     *
     * @param conn       the connection
     * @param tccFenceDO the tcc fence do
     * @return the boolean
     */
    boolean insertTCCFenceDO(Connection conn, TCCFenceDO tccFenceDO);

    /**
     * Update tcc fence do boolean.
     *
     * @param conn      the connection
     * @param xid       the global transaction id
     * @param branchId  the branch transaction id
     * @param newStatus the new status
     * @param oldStatus the old status
     * @return the boolean
     */
    boolean updateTCCFenceDO(Connection conn, String xid, Long branchId, int newStatus, int oldStatus);

    /**
     * Delete tcc fence do boolean.
     *
     * @param conn     the connection
     * @param xid      the global transaction id
     * @param branchId the branch transaction id
     * @return the boolean
     */
    boolean deleteTCCFenceDO(Connection conn, String xid, Long branchId);

    /**
     * Delete tcc fence do boolean.
     *
     * @param conn the connection
     * @param xids the global transaction ids
     * @return the boolean
     */
    int deleteTCCFenceDO(Connection conn, List<String> xids);

    /**
     * Delete tcc fence by datetime.
     *
     * @param conn     the connection
     * @param datetime datetime
     * @return the deleted row count
     */
    int deleteTCCFenceDOByDate(Connection conn, Date datetime);


    /**
     * Set LogTable Name
     *
     * @param logTableName logTableName
     */
    void setLogTableName(String logTableName);

}
