package tech.finovy.framework.transaction.tcc.client.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TccClientContext {

    public static final String TX_ID = "TX_ID";
    private static final ThreadLocal<TxInfo> CURRENT_TX = new ThreadLocal<>();

    private TccClientContext() {
    }

    public static TxInfo getTxInfo() {
        return CURRENT_TX.get();
    }

    public static String getTxId() {
        final TxInfo tx = CURRENT_TX.get();
        if (tx != null) {
            return tx.txId;
        }
        return null;
    }

    public static Long getBranchId() {
        final TxInfo tx = CURRENT_TX.get();
        if (tx != null) {
            return tx.branchId;
        }
        return null;
    }

    public static String getTxName() {
        final TxInfo tx = CURRENT_TX.get();
        if (tx != null) {
            return tx.txName;
        }
        return null;
    }

    public static void setTx(TxInfo tx) {
        if (tx == null) {
            return;
        }
        CURRENT_TX.set(tx);
    }

    public static void clear() {
        CURRENT_TX.remove();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TxInfo {
        String txId;
        Long branchId;
        String txName;

        public TxInfo(String txId, String txName) {
            this.txId = txId;
            this.txName = txName;
            long i = txName.hashCode();
            this.branchId = i >= 0 ? i : -1 * i;
        }

    }

}
