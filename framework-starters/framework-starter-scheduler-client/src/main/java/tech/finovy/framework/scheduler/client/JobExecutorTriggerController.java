package tech.finovy.framework.scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;
import tech.finovy.framework.scheduler.entity.RemoteJobConstant;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;

import java.util.List;

@Slf4j
@RestController
public class JobExecutorTriggerController<T> {
    private final JobExecutorControllerProperties properties;
    private final SchedulerExecutorService<T> schedulerExecutorService;

    public JobExecutorTriggerController(JobExecutorControllerProperties properties, SchedulerExecutorService<T> schedulerExecutorService) {
        this.properties = properties;
        this.schedulerExecutorService = schedulerExecutorService;
    }

    private final RedisContext context = RedisContextHolder.get();

    @PostMapping(value = {RemoteJobConstant.JOB_TRIGGER, RemoteJobConstant.JOB_ACTION_TRIGGER})
    public RemoteJobExecuteResult trigger(@RequestBody RemoteJobExecuteConfig config) {
        if (properties.isDebug()) {
            log.info("Begin Trigger Job:{} , Name: {}", config.getJobKey(), config.getJobName());
        }
        if (config == null) {
            return RemoteJobExecuteResult.builder().success(false).result("RemoteJobExecuteConfig IS NULL").build();
        }
        RLock lock = null;
        if (!properties.isSkipDistributedLock()) {
            lock = context.getClient().getLock("trigger-" + config.getJobKey() + "-" + config.getShardingItem() + "-" + config.getShardingTotalCount());
        }
        if (lock == null || config.getRateLimiter() < 0.0) {
            return schedulerExecutorService.trigger(config);
        }
        if (lock.tryLock()) {
            try {
                return schedulerExecutorService.trigger(config);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        if (properties.isDebug()) {
            log.info("Lock Trigger Limited Job:{} , Name: {}", config.getJobKey(), config.getJobName());
        }
        return RemoteJobExecuteResult.builder().success(false).jobKey(config.getJobKey()).result("LockLimited").build();
    }

    @PostMapping(value = {RemoteJobConstant.JOB_ACTION_FETCH, RemoteJobConstant.JOB_FETCH})
    public List<T> fetch(@RequestBody RemoteJobExecuteConfig config) {
        if (properties.isDebug()) {
            log.info("Begin Fetch Job:{} , Name: {}", config.getJobKey(), config.getJobName());
        }
        return schedulerExecutorService.fetch(config);
    }

    @PostMapping(value = {RemoteJobConstant.JOB_ACTION_PROCESS})
    public RemoteJobExecuteResult process(@RequestBody RemoteJobProcessConfig<T> config) {
        if (properties.isDebug()) {
            log.info("Begin Process Job:{} , Name: {}", config.getConfig().getJobKey(), config.getConfig().getJobName());
        }
        return schedulerExecutorService.process(config);
    }
}
