package tech.finovy.framework.transaction.tcc.client.fence;

import java.util.Date;

/**
 * TCC Fence Domain
 *
 * @author kaka2code
 */
public class TCCFenceDO {

    /**
     * the global transaction id
     */
    private String xid;

    /**
     * the branch transaction id
     */
    private Long branchId;

    /**
     * the action name
     */
    private String actionName;

    /**
     * the tcc fence status
     * tried: 1; committed: 2; rollbacked: 3; suspended: 4
     */
    private Integer status;

    /**
     * create time
     */
    private Date gmtCreate;

    /**
     * update time
     */
    private Date gmtModified;

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}
