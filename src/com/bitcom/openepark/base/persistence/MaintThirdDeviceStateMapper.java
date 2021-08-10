package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.base.domain.MaintThirdDeviceState;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MaintThirdDeviceStateMapper {
    int insert(MaintThirdDeviceState paramMaintThirdDeviceState);

    int update(MaintThirdDeviceState paramMaintThirdDeviceState);

    MaintThirdDeviceState selectByDeviceCode(@Param("deviceCode") String paramString);

    List<MaintThirdDeviceState> queryMaintThirdDeviceStateByContinuFaultNum();
}



