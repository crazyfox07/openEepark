package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.base.domain.MaintThirdDeviceFailure;
import org.apache.ibatis.annotations.Param;

public interface MaintThirdDeviceFailureMapper {
    int insert(MaintThirdDeviceFailure paramMaintThirdDeviceFailure);

    int update(MaintThirdDeviceFailure paramMaintThirdDeviceFailure);

    MaintThirdDeviceFailure selectByDeviceCode(@Param("deviceCode") String paramString);
}



