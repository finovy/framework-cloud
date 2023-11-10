package tech.finovy.framework.common.core;

import java.util.Date;


public class BranchDO {
    private String xid;
    private Long transactionId;
    private Integer status;
    private Double test;
    private Date gmtCreate;

    public BranchDO() {
    }

    public BranchDO(String xid, Long transactionId, Integer status, Double test,
                    Date gmtCreate) {
        this.xid = xid;
        this.transactionId = transactionId;
        this.status = status;
        this.test = test;
        this.gmtCreate = gmtCreate;
    }

    public String getXid() {
        return xid;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Integer getStatus() {
        return status;
    }

    public Double getTest() {
        return test;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }
}
