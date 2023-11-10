package tech.finovy.framework.redisson.holder;

import lombok.Getter;
import lombok.Setter;
import tech.finovy.framework.redisson.client.RedissonClientInterface;

@Getter
@Setter
public class RedisContext {

    private RedissonClientInterface client;

}
