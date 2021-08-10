package com.bitcom.openepark.data.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.base.domain.ThirdDevConfig;
import com.bitcom.openepark.base.persistence.ThirdDevConfigMapper;
import com.bitcom.openepark.common.service.CacheCallback;
import com.bitcom.openepark.common.service.CommonCacheService;
import com.bitcom.openepark.data.service.IDevConfigService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DevConfigServiceImpl
        implements IDevConfigService {
    public static final int TIME_OUT = 600;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ThirdDevConfigMapper thirdDevConfigMapper;
    @Autowired
    private CommonCacheService cacheServiceImpl;

    public ThirdDevConfig getByPrimaryKey(String domainId) {
        String key = "map:third_dev_config";
        final String fieldKey = domainId;
        ThirdDevConfig config = (ThirdDevConfig) this.cacheServiceImpl.getCacheFromHashMap(key, fieldKey, 600, ThirdDevConfig.class, new CacheCallback<ThirdDevConfig>() {
            public ThirdDevConfig doPersonalStuff() {
                return DevConfigServiceImpl.this.thirdDevConfigMapper.selectByPrimaryKey(fieldKey);
            }
        });
        return config;
    }


    public int updateValidServer(String domainId, String parkServUrl, String servEnable) {
        String key = "map:third_dev_config";
        ThirdDevConfig config = this.thirdDevConfigMapper.selectByPrimaryKey(domainId);
        config.setServerUrl(parkServUrl);
        config.setServEnable(servEnable);
        config.setVerifyTime(new Date());
        this.redisTemplate.opsForHash().put(key, config.getDomainId(), JSONObject.toJSONString(config));
        return this.thirdDevConfigMapper.updateByPrimaryKeySelective(config);
    }
}



