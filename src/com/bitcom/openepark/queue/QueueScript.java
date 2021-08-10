package com.bitcom.openepark.queue;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;


public class QueueScript<T> {
    private RedisTemplate<String, Object> redisTemplate = null;
    private DefaultRedisScript<T> redisScript;

    public QueueScript(RedisTemplate<String, Object> redis, String script, Class<T> resultType) {
        this.redisTemplate = redis;
        this.redisScript = new DefaultRedisScript(script, resultType);
    }


    public T exec(List<String> keys) {
        return (T) this.redisTemplate.execute((RedisScript) this.redisScript, keys, new Object[0]);
    }


    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }


    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public DefaultRedisScript<T> getRedisScript() {
        return this.redisScript;
    }


    public void setRedisScript(DefaultRedisScript<T> redisScript) {
        this.redisScript = redisScript;
    }
}



