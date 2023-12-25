package tech.finovy.framework.transaction.tcc.client.api.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecuteResult {

    private String txId;

    private boolean success;

    private long duration;

}
