package tech.finovy.framework.transaction.tcc.client.fence;


import tech.finovy.framework.common.core.exception.DataAccessException;
import tech.finovy.framework.common.core.exception.FrameworkErrorCode;
import tech.finovy.framework.common.core.exception.StoreException;
import tech.finovy.framework.common.core.util.IOUtil;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;
import tech.finovy.framework.transaction.tcc.client.exception.TCCFenceException;

import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type TCC Fence store data base dao
 *
 * @author kaka2code
 */
public class TCCFenceStoreDataBaseDAO implements TCCFenceStore {

    private static volatile TCCFenceStoreDataBaseDAO instance = null;
    /**
     * TCC fence log table name
     */
    private String logTableName = Constants.DEFAULT_TCC_FENCE_LOG_TABLE_NAME;

    private TCCFenceStoreDataBaseDAO() {
    }

    public static TCCFenceStore getInstance() {
        if (instance == null) {
            synchronized (TCCFenceStore.class) {
                if (instance == null) {
                    instance = new TCCFenceStoreDataBaseDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public TCCFenceDO queryTCCFenceDO(Connection conn, String xid, Long branchId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = TCCFenceStoreSqls.getQuerySQLByBranchIdAndXid(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setString(1, xid);
            ps.setLong(2, branchId);
            rs = ps.executeQuery();
            if (rs.next()) {
                TCCFenceDO tccFenceDO = new TCCFenceDO();
                tccFenceDO.setXid(rs.getString("xid"));
                tccFenceDO.setBranchId(rs.getLong("branch_id"));
                tccFenceDO.setStatus(rs.getInt("status"));
                return tccFenceDO;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            IOUtil.close(rs, ps);
        }
    }

    @Override
    public Set<String> queryEndStatusXidsByDate(Connection conn, Date datetime, int limit) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = TCCFenceStoreSqls.getQueryEndStatusSQLByDate(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(datetime.getTime()));
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            Set<String> xids = new HashSet<>(limit);
            while (rs.next()) {
                xids.add(rs.getString("xid"));
            }
            return xids;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            IOUtil.close(rs, ps);
        }
    }

    @Override
    public boolean insertTCCFenceDO(Connection conn, TCCFenceDO tccFenceDO) {
        PreparedStatement ps = null;
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String sql = TCCFenceStoreSqls.getInsertLocalTCCLogSQL(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setString(1, tccFenceDO.getXid());
            ps.setLong(2, tccFenceDO.getBranchId());
            ps.setString(3, tccFenceDO.getActionName());
            ps.setInt(4, tccFenceDO.getStatus());
            ps.setTimestamp(5, now);
            ps.setTimestamp(6, now);
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new TCCFenceException(String.format("Insert tcc fence record duplicate key exception. xid= %s, branchId= %s", tccFenceDO.getXid(), tccFenceDO.getBranchId()),
                    FrameworkErrorCode.DuplicateKeyException);
        } catch (SQLException e) {
            throw new StoreException(e);
        } finally {
            IOUtil.close(ps);
        }
    }

    @Override
    public boolean updateTCCFenceDO(Connection conn, String xid, Long branchId, int newStatus, int oldStatus) {
        PreparedStatement ps = null;
        try {
            String sql = TCCFenceStoreSqls.getUpdateStatusSQLByBranchIdAndXid(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStatus);
            // gmt_modified
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, xid);
            ps.setLong(4, branchId);
            ps.setInt(5, oldStatus);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new StoreException(e);
        } finally {
            IOUtil.close(ps);
        }
    }

    @Override
    public boolean deleteTCCFenceDO(Connection conn, String xid, Long branchId) {
        PreparedStatement ps = null;
        try {
            String sql = TCCFenceStoreSqls.getDeleteSQLByBranchIdAndXid(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setString(1, xid);
            ps.setLong(2, branchId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new StoreException(e);
        } finally {
            IOUtil.close(ps);
        }
    }

    @Override
    public int deleteTCCFenceDO(Connection conn, List<String> xids) {
        PreparedStatement ps = null;
        try {
            String paramsPlaceHolder = org.apache.commons.lang.StringUtils.repeat("?", ",", xids.size());
            String sql = TCCFenceStoreSqls.getDeleteSQLByXids(logTableName, paramsPlaceHolder);
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < xids.size(); i++) {
                ps.setString(i + 1, xids.get(i));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException(e);
        } finally {
            IOUtil.close(ps);
        }
    }

    @Override
    public int deleteTCCFenceDOByDate(Connection conn, Date datetime) {
        PreparedStatement ps = null;
        try {
            String sql = TCCFenceStoreSqls.getDeleteSQLByDateAndStatus(logTableName);
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(datetime.getTime()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException(e);
        } finally {
            IOUtil.close(ps);
        }
    }

    @Override
    public void setLogTableName(String logTableName) {
        this.logTableName = logTableName;
    }
}
