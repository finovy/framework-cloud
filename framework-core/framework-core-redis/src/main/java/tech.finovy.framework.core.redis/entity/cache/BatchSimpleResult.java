package tech.finovy.framework.core.redis.entity.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchSimpleResult {
    private List<String> errKeys;
    private List<String> successKey;
    private boolean success;
}
