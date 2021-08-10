package com.bitcom.openepark.queue;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.api.vo.RedisTask;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisQueue {
    public static Logger log = Logger.getLogger(RedisQueue.class);

    public static final String SUFFIX_WAIT = "-wait-queue";
    public static final String SUFFIX_FAIL = "-fail-queue";
    public static final String SUFFIX_PROCESS = "-processing-queue";
    public static final Integer FAIL_TRY_COUNT = Integer.valueOf(2);

    private String waitQueue = null;
    private String processingQueue = null;
    private String faileQueue = null;
    private RedisTemplate<String, Object> redis = null;
    private FailCache failCache = null;


    private boolean stop = false;


    private QueueScript<Boolean> FAIL_QUEUE_SCRIPT;


    public RedisQueue(String queueName, RedisTemplate<String, Object> redisTemplate) {
        this.waitQueue = queueName + "-wait-queue";
        this.faileQueue = queueName + "-fail-queue";
        this.processingQueue = queueName + "-processing-queue";
        this.redis = redisTemplate;
        this.failCache = new FailCache(redisTemplate);
        String script = "if redis.call('LREM',KEYS[1],1,KEYS[2]) > 0 then redis.call('lpush',KEYS[3],KEYS[2]) return true end return false";
        this.FAIL_QUEUE_SCRIPT = new QueueScript<>(redisTemplate, script, Boolean.class);
    }


    public String take() {
        while (!this.stop) {
            String task = null;
            try {
                task = (String) this.redis.opsForList().rightPopAndLeftPush(this.waitQueue, this.processingQueue);
            } catch (Exception e) {
                log.error("[RedisQueue]" + e.getMessage());
                try {
                    Thread.sleep(5000L);
                    continue;
                } catch (InterruptedException interruptedException) {
                    continue;
                }
            }
            if (task != null) {
                return task;
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException interruptedException) {
            }
        }

        return null;
    }


    public void fail(RedisTask task) {
        int tryTimes = this.failCache.increaseAndGet(task.getTaskId());
        String taskStr = JSONObject.toJSONString(task);
        if (tryTimes < FAIL_TRY_COUNT.intValue()) {
            this.FAIL_QUEUE_SCRIPT.exec(Lists.newArrayList(new String[]{this.processingQueue, taskStr, this.waitQueue}));
            log.error("[task] fail.taskId=" + task.getTaskId() + ". retry count=" + tryTimes + ". Will retry later");
        } else {
            this.FAIL_QUEUE_SCRIPT.exec(Lists.newArrayList(new String[]{this.processingQueue, taskStr, this.waitQueue}));
            this.failCache.invalidate(task.getTaskId());
            log.error("[task] fail.taskId=" + task.getTaskId() + ". retry count=" + tryTimes + ". Discard to fail queue");
        }
    }

    public void success(RedisTask task) {
        String taskStr = JSONObject.toJSONString(task);
        this.redis.opsForList().remove(this.processingQueue, 0L, taskStr);
        this.failCache.invalidate(task.getTaskId());
        log.info("[task] success. " + task.getTaskId());
    }

    public void offer(RedisTask task) {
        String taskStr = JSONObject.toJSONString(task);
        this.redis.opsForList().leftPush(this.waitQueue, taskStr);
    }


    public void close() {
        this.stop = true;
    }


    public String getWaitQueue() {
        return this.waitQueue;
    }


    public void setWaitQueue(String waitQueue) {
        this.waitQueue = waitQueue;
    }


    public String getProcessingQueue() {
        return this.processingQueue;
    }


    public void setProcessingQueue(String processingQueue) {
        this.processingQueue = processingQueue;
    }


    public String getFaileQueue() {
        return this.faileQueue;
    }


    public void setFaileQueue(String faileQueue) {
        this.faileQueue = faileQueue;
    }


    public String getFaileQueueName() {
        return this.faileQueue;
    }


    public void setFaileQueueName(String faileQueueName) {
        this.faileQueue = faileQueueName;
    }


    public String getProcessingQueueName() {
        return this.processingQueue;
    }


    public void setProcessingQueueName(String processingQueueName) {
        this.processingQueue = processingQueueName;
    }


    public FailCache getFailCache() {
        return this.failCache;
    }


    public void setFailCache(FailCache failCache) {
        this.failCache = failCache;
    }


    public RedisTemplate<String, Object> getRedis() {
        return this.redis;
    }


    public void setRedis(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }
}



