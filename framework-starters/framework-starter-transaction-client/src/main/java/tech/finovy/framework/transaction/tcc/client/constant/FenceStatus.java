package tech.finovy.framework.transaction.tcc.client.constant;

/**
 * @Author: Ryan Luo
 * @Date: 2023/3/3 18:36
 */
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
