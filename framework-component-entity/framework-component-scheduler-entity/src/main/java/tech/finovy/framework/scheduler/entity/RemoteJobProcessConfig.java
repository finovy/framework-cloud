package tech.finovy.framework.scheduler.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RemoteJobProcessConfig<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6885975872937583284L;
    private RemoteJobExecuteConfig config;
    private List<T> data;

    public RemoteJobProcessConfig() {
    }
}
