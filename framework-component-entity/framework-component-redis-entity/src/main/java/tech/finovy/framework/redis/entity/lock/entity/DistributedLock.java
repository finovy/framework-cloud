package tech.finovy.framework.redis.entity.lock.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DistributedLock implements java.io.Serializable{

	private static final long serialVersionUID = 6149181919731716721L;
	/**
	 * 锁名称 桶分组
	 */
	private String key;
	private boolean mock;
	/**
	 * 唯一 流水 全局id
	 */
	private long id = 0L;

	private long leaseTime=1000L;
	/**
	 * 剩余
	 */
	private long remainTimeToLive = 1000L;
	/**
	 * 是否存在
	 */
	private boolean exists = false;
	/**
	 *
	 */
	private boolean locked = false;
	/**
	 * 是否解锁完成
	 */
	private boolean finished = false;
	private long finishedTimeToLive = 0;
	private boolean force=false;
	/**
	 * 额外信息
	 */
	private String info;
	private String errMsg;
}
