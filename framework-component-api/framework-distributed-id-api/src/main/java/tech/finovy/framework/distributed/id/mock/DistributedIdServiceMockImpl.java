package tech.finovy.framework.distributed.id.mock;

import tech.finovy.framework.distributed.id.api.DistributedIdService;

public class DistributedIdServiceMockImpl implements DistributedIdService {
    @Override
    public long incrementAndGet(String key) {
        return 0;
    }

    @Override
    public void set(String key, long id) {

    }
}
