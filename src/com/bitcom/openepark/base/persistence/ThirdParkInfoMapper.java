package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.maintenance.vo.ThirdParkStateVO;

import java.util.List;

public interface ThirdParkInfoMapper {
    List<ThirdParkStateVO> queryAllThirdParkStateInfo();
}



