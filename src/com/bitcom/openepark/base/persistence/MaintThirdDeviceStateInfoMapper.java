package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.base.domain.MaintThirdDeviceStateInfo;

public interface MaintThirdDeviceStateInfoMapper {
    int insert(MaintThirdDeviceStateInfo paramMaintThirdDeviceStateInfo);

    int truncateTable();
}



