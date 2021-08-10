package com.bitcom.openepark.queue;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;


public class FailCache {
    private RedisTemplate<String, Object> redis = null;


    public FailCache(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }


    public int increaseAndGet(String taskId) {
        String key = getCacheKey(taskId);
        return this.redis.opsForValue().increment(key, 1L).intValue();
    }


    public void invalidate(String taskId) {
        String key = getCacheKey(taskId);
        this.redis.expire(key, 1000L, TimeUnit.MILLISECONDS);
    }


    private String getCacheKey(String taskId) {
        return "String:taskfailCount:" + taskId;
    }
}



