package tech.finovy.framework.core.redis.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonCodec;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.config.Config;
import org.redisson.connection.ConnectionManager;
import org.redisson.eviction.EvictionScheduler;

import java.util.concurrent.TimeUnit;

/**
 * @author: Dtype.Huang
 * @date: 2020/6/12 16:24
 */
@Slf4j
public class RedissonExtImpl extends Redisson implements RedissonClientInterface {
    private static final String[] REP = new String[]{"tech.finovy.framework.distributed.event.entity.",
            "tech.finovy.framework.distributed.", "::"};
    private static final String[] EMPTY_STRING = new String[]{"", "", ""};
    private RedissonConfiguration redissonConfiguration;
    private int version;

    protected RedissonExtImpl(Config config) {
        super(config);
    }

    public static RedissonClientInterface create(Config config) {
        return new RedissonExtImpl(config);
    }

    @Override
    public void setRedissonConfiguration(RedissonConfiguration redissonConfiguration, int version) {
        this.redissonConfiguration = redissonConfiguration;
        this.version = version;
    }

    @Override
    public boolean isDebug() {
        return redissonConfiguration.isDebug();
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String createKey(String key, String type, boolean skipPrefix) {
        if (skipPrefix) {
            return key;
        }
        StringBuilder strBul = new StringBuilder(redissonConfiguration.getPrefix());
        if (redissonConfiguration.getVersion() != null) {
            strBul.append(":").append(redissonConfiguration.getVersion());
        }
        if (type != null) {
            type = StringUtils.replaceEach(type, REP, EMPTY_STRING);
        }
        strBul.append(":")
                .append(type)
                .append(":")
                .append(key);
        return strBul.toString();
    }

    @Override
    public String createKey(String key, String type) {
        return createKey(key, type, false);
    }

    @Override
    public String createKey(String key) {
        StringBuilder strBul = new StringBuilder(redissonConfiguration.getPrefix());
        if (redissonConfiguration.getVersion() != null) {
            strBul.append(":").append(redissonConfiguration.getVersion());
        }
        strBul.append(":").append(key);
        String newKey = strBul.toString();
        return newKey;
    }

    @Override
    public String createMapKey(String key) {
        StringBuilder strBul = new StringBuilder(redissonConfiguration.getPrefix());
        if (redissonConfiguration.getVersion() != null) {
            strBul.append(":").append(redissonConfiguration.getVersion());
        }
        strBul.append(":RMap:").append(key);
        String newKey = strBul.toString();
        return newKey;
    }

    @Override
    public String createLocalCacheMapKey(String key) {
        StringBuilder strBul = new StringBuilder(redissonConfiguration.getPrefix());
        if (redissonConfiguration.getVersion() != null) {
            strBul.append(":").append(redissonConfiguration.getVersion());
        }
        strBul.append(":LocalCacheMap:").append(key);
        String newKey = strBul.toString();
        return newKey;
    }

    @Override
    public int calHash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    @Override
    public int calKeyHash(String key) {
        return calHash(key) % redissonConfiguration.getHashModSize();
    }

    @Override
    public EvictionScheduler getEvictionScheduler() {
        return super.getEvictionScheduler();
    }

    @Override
    public CommandAsyncExecutor getCommandExecutor() {
        return super.getCommandExecutor();
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return super.getConnectionManager();
    }

    @Override
    public RedissonRxClient rxJava() {
        return super.rxJava();
    }

    @Override
    public RedissonReactiveClient reactive() {
        return super.reactive();
    }

    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String name) {
        return super.getTimeSeries(name);
    }

    @Override
    public <V, L> RTimeSeries<V, L> getTimeSeries(String name, Codec codec) {
        return super.getTimeSeries(name, codec);
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name) {
        return super.getStream(name);
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name, Codec codec) {
        return super.getStream(name, codec);
    }

    @Override
    public RBinaryStream getBinaryStream(String name) {
        return super.getBinaryStream(name);
    }

    @Override
    public <V> RGeo<V> getGeo(String name) {
        return super.getGeo(name);
    }

    @Override
    public <V> RGeo<V> getGeo(String name, Codec codec) {
        return super.getGeo(name, codec);
    }

    @Override
    public <V> RBucket<V> getBucket(String name) {
        return super.getBucket(name);
    }

    @Override
    public RRateLimiter getRateLimiter(String name) {
        return super.getRateLimiter(name);
    }

    @Override
    public <V> RBucket<V> getBucket(String name, Codec codec) {
        return super.getBucket(name, codec);
    }

    @Override
    public RBuckets getBuckets() {
        return super.getBuckets();
    }

    @Override
    public RBuckets getBuckets(Codec codec) {
        return super.getBuckets(codec);
    }

    @Override
    public <V> RJsonBucket<V> getJsonBucket(String name, JsonCodec<V> codec) {
        return super.getJsonBucket(name, codec);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name) {
        return super.getHyperLogLog(name);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name, Codec codec) {
        return super.getHyperLogLog(name, codec);
    }

    @Override
    public <V> RList<V> getList(String name) {
        return super.getList(name);
    }

    @Override
    public <V> RList<V> getList(String name, Codec codec) {
        return super.getList(name, codec);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name) {
        return super.getListMultimap(name);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name, Codec codec) {
        return super.getListMultimap(name, codec);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, LocalCachedMapOptions<K, V> options) {
        return super.getLocalCachedMap(name, options);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, Codec codec, LocalCachedMapOptions<K, V> options) {
        return super.getLocalCachedMap(name, codec, options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name) {
        return super.getMap(name);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, MapOptions<K, V> options) {
        return super.getMap(name, options);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name) {
        return super.getSetMultimap(name);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name) {
        return super.getSetMultimapCache(name);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name, Codec codec) {
        return super.getSetMultimapCache(name, codec);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name) {
        return super.getListMultimapCache(name);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name, Codec codec) {
        return super.getListMultimapCache(name, codec);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name, Codec codec) {
        return super.getSetMultimap(name, codec);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name) {
        return super.getSetCache(name);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name, Codec codec) {
        return super.getSetCache(name, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name) {
        return super.getMapCache(name);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, MapOptions<K, V> options) {
        return super.getMapCache(name, options);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec) {
        return super.getMapCache(name, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec, MapOptions<K, V> options) {
        return super.getMapCache(name, codec, options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec) {
        return super.getMap(name, codec);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec, MapOptions<K, V> options) {
        return super.getMap(name, codec, options);
    }

    @Override
    public RLock getLock(String name) {
        return super.getLock(name);
    }

    @Override
    public RLock getSpinLock(String name) {
        return super.getSpinLock(name);
    }

    @Override
    public RLock getSpinLock(String name, LockOptions.BackOff backOff) {
        return super.getSpinLock(name, backOff);
    }

    @Override
    public RLock getMultiLock(RLock... locks) {
        return super.getMultiLock(locks);
    }

    @Override
    public RLock getRedLock(RLock... locks) {
        return super.getRedLock(locks);
    }

    @Override
    public RLock getFairLock(String name) {
        return super.getFairLock(name);
    }

    @Override
    public RReadWriteLock getReadWriteLock(String name) {
        return super.getReadWriteLock(name);
    }

    @Override
    public <V> RSet<V> getSet(String name) {
        return super.getSet(name);
    }

    @Override
    public <V> RSet<V> getSet(String name, Codec codec) {
        return super.getSet(name, codec);
    }

    @Override
    public RFunction getFunction() {
        return super.getFunction();
    }

    @Override
    public RFunction getFunction(Codec codec) {
        return super.getFunction(codec);
    }

    @Override
    public RScript getScript() {
        return super.getScript();
    }

    @Override
    public RScript getScript(Codec codec) {
        return super.getScript(codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name) {
        return super.getExecutorService(name);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, ExecutorOptions options) {
        return super.getExecutorService(name, options);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec) {
        return super.getExecutorService(name, codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec, ExecutorOptions options) {
        return super.getExecutorService(name, codec, options);
    }

    @Override
    public RRemoteService getRemoteService() {
        return super.getRemoteService();
    }

    @Override
    public RRemoteService getRemoteService(String name) {
        return super.getRemoteService(name);
    }

    @Override
    public RRemoteService getRemoteService(Codec codec) {
        return super.getRemoteService(codec);
    }

    @Override
    public RRemoteService getRemoteService(String name, Codec codec) {
        return super.getRemoteService(name, codec);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name) {
        return super.getSortedSet(name);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name, Codec codec) {
        return super.getSortedSet(name, codec);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name) {
        return super.getScoredSortedSet(name);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name, Codec codec) {
        return super.getScoredSortedSet(name, codec);
    }

    @Override
    public RLexSortedSet getLexSortedSet(String name) {
        return super.getLexSortedSet(name);
    }

    @Override
    public RShardedTopic getShardedTopic(String name) {
        return super.getShardedTopic(name);
    }

    @Override
    public RShardedTopic getShardedTopic(String name, Codec codec) {
        return super.getShardedTopic(name, codec);
    }

    @Override
    public RTopic getTopic(String name) {
        return super.getTopic(name);
    }

    @Override
    public RTopic getTopic(String name, Codec codec) {
        return super.getTopic(name, codec);
    }

    @Override
    public RReliableTopic getReliableTopic(String name) {
        return super.getReliableTopic(name);
    }

    @Override
    public RReliableTopic getReliableTopic(String name, Codec codec) {
        return super.getReliableTopic(name, codec);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern) {
        return super.getPatternTopic(pattern);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern, Codec codec) {
        return super.getPatternTopic(pattern, codec);
    }

    @Override
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> destinationQueue) {
        return super.getDelayedQueue(destinationQueue);
    }

    @Override
    public <V> RQueue<V> getQueue(String name) {
        return super.getQueue(name);
    }

    @Override
    public <V> RQueue<V> getQueue(String name, Codec codec) {
        return super.getQueue(name, codec);
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String name) {
        return super.getTransferQueue(name);
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String name, Codec codec) {
        return super.getTransferQueue(name, codec);
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name) {
        return super.getRingBuffer(name);
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name, Codec codec) {
        return super.getRingBuffer(name, codec);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name) {
        return super.getBlockingQueue(name);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name, Codec codec) {
        return super.getBlockingQueue(name, codec);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name) {
        return super.getBoundedBlockingQueue(name);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name, Codec codec) {
        return super.getBoundedBlockingQueue(name, codec);
    }

    @Override
    public <V> RDeque<V> getDeque(String name) {
        return super.getDeque(name);
    }

    @Override
    public <V> RDeque<V> getDeque(String name, Codec codec) {
        return super.getDeque(name, codec);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name) {
        return super.getBlockingDeque(name);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name, Codec codec) {
        return super.getBlockingDeque(name, codec);
    }

    @Override
    public RAtomicLong getAtomicLong(String name) {
        return super.getAtomicLong(name);
    }

    @Override
    public RLongAdder getLongAdder(String name) {
        return super.getLongAdder(name);
    }

    @Override
    public RDoubleAdder getDoubleAdder(String name) {
        return super.getDoubleAdder(name);
    }

    @Override
    public RAtomicDouble getAtomicDouble(String name) {
        return super.getAtomicDouble(name);
    }

    @Override
    public RCountDownLatch getCountDownLatch(String name) {
        return super.getCountDownLatch(name);
    }

    @Override
    public RBitSet getBitSet(String name) {
        return super.getBitSet(name);
    }

    @Override
    public RSemaphore getSemaphore(String name) {
        return super.getSemaphore(name);
    }

    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String name) {
        return super.getPermitExpirableSemaphore(name);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name) {
        return super.getBloomFilter(name);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name, Codec codec) {
        return super.getBloomFilter(name, codec);
    }

    @Override
    public RIdGenerator getIdGenerator(String name) {
        return super.getIdGenerator(name);
    }

    @Override
    public RKeys getKeys() {
        return super.getKeys();
    }

    @Override
    public RTransaction createTransaction(TransactionOptions options) {
        return super.createTransaction(options);
    }

    @Override
    public RBatch createBatch(BatchOptions options) {
        return super.createBatch(options);
    }

    @Override
    public RBatch createBatch() {
        return super.createBatch();
    }

    @Override
    public RLiveObjectService getLiveObjectService() {
        return super.getLiveObjectService();
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
        super.shutdown(quietPeriod, timeout, unit);
    }

    @Override
    public Config getConfig() {
        return super.getConfig();
    }

    @Override
    public <T extends BaseRedisNodes> T getRedisNodes(RedisNodes<T> nodes) {
        return super.getRedisNodes(nodes);
    }

    @Override
    public NodesGroup<Node> getNodesGroup() {
        return super.getNodesGroup();
    }

    @Override
    public ClusterNodesGroup getClusterNodesGroup() {
        return super.getClusterNodesGroup();
    }

    @Override
    public boolean isShutdown() {
        return super.isShutdown();
    }

    @Override
    public boolean isShuttingDown() {
        return super.isShuttingDown();
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name) {
        return super.getPriorityQueue(name);
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name, Codec codec) {
        return super.getPriorityQueue(name, codec);
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name) {
        return super.getPriorityBlockingQueue(name);
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name, Codec codec) {
        return super.getPriorityBlockingQueue(name, codec);
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name) {
        return super.getPriorityBlockingDeque(name);
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name, Codec codec) {
        return super.getPriorityBlockingDeque(name, codec);
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name) {
        return super.getPriorityDeque(name);
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name, Codec codec) {
        return super.getPriorityDeque(name, codec);
    }

    @Override
    public String getId() {
        return super.getId();
    }
}
