package com.bitcom.openepark.common.service;

public interface CommonCacheService {
    <T> T getCacheFromHashMap(String paramString1, String paramString2, int paramInt, Class<T> paramClass, CacheCallback<T> paramCacheCallback);
}



