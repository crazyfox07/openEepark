package com.bitcom.openepark.common.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.common.service.CacheCallback;
import com.bitcom.openepark.common.service.CommonCacheService;
import com.google.common.base.Strings;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class CommonCacheServiceImpl
        implements CommonCacheService {
    public static Logger logger = Logger.getLogger(CommonCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public <T> T getCacheFromHashMap(String key, String fieldKey, int expireSec, Class<T> clazz, CacheCallback<T> callback) {
        long timeStamp = System.currentTimeMillis();
        String json = (String) this.redisTemplate.opsForHash().get(key, fieldKey);
        T t = null;
        if (!Strings.isNullOrEmpty(json)) {
            t = (T) JSONObject.parseObject(json, clazz);
            logger.info("【Query from cache】#" + timeStamp + "# hooya");
        } else {
            logger.info("【Query from DBTable】#" + timeStamp + "#  ops!!");
            t = (T) callback.doPersonalStuff();
            if (t != null) {
                this.redisTemplate.opsForHash().put(key, fieldKey, JSONObject.toJSONString(t));
                this.redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
                logger.info("【Cached OK】#" + timeStamp + "#  finish");
            }
        }
        return t;
    }
}



