package tech.finovy.framework.transaction.tcc.client.constant;

public enum FenceStatus {

    TRIED,
    COMMITTING,
    COMMITTED,
    ROLLBACKING,
    ROLLBACKED,

    SUSPENDED;

    public static FenceStatus get(String status) {
        for (FenceStatus value : FenceStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
