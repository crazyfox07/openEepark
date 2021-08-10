package com.bitcom.openepark.data.service;

import com.bitcom.openepark.base.domain.ThirdDevConfig;

public interface IDevConfigService {
    ThirdDevConfig getByPrimaryKey(String paramString);

    int updateValidServer(String paramString1, String paramString2, String paramString3);
}



