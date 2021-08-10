package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.base.domain.ThirdDevConfig;
import com.bitcom.openepark.maintenance.vo.ThirdParkDevConfig;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ThirdDevConfigMapper {
    int deleteByPrimaryKey(String paramString);

    int insert(ThirdDevConfig paramThirdDevConfig);

    int insertSelective(ThirdDevConfig paramThirdDevConfig);

    ThirdDevConfig selectByPrimaryKey(String paramString);

    int updateByPrimaryKeySelective(ThirdDevConfig paramThirdDevConfig);

    int updateByPrimaryKey(ThirdDevConfig paramThirdDevConfig);

    String queryServUrlByParkCode(@Param("parkCode") String paramString);

    ThirdDevConfig queryDevConfigByParkCode(@Param("parkCode") String paramString);

    ThirdDevConfig selectByPrimaryKeyForUpdate(String paramString);

    List<ThirdDevConfig> selectAll();

    List<ThirdParkDevConfig> queryAllDevConfig();

    String getOpenEparkProtocolVersion();
}



