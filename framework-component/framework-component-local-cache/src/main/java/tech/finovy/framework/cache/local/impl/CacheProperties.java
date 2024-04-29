package tech.finovy.framework.cache.local.impl;

import lombok.Data;

@Data
public class CacheProperties {

    public static final String PREFIX = "cache.local-cache";

    private int initialCapacity = 1000;
    private int maximumSize = 5000;
    private int maximumWeight = 0;
    private int weigher = 0;
    private long expireAfterAccess = 60;
    private long expireAfterWrite = 60;
    private boolean weakKeys;
    private boolean weakValues = true;
    private boolean softValues;
    private boolean recordStats;
}
