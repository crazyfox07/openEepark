package com.bitcom.openepark.api.serviceimpl;

import com.bitcom.openepark.api.service.ITestService;
import com.bitcom.openepark.base.domain.ThirdDevConfig;
import com.bitcom.openepark.base.persistence.ThirdDevConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestServiceImpl
        implements ITestService {
    @Autowired
    private ThirdDevConfigMapper thirdDevConfigMapper;

    public String updateThirdDevConfig() throws Exception {
        System.out.println("update begin..");
        Thread.sleep(100000L);


        System.out.println("update end..");
        return "success";
    }


    public Object queryThirdDevConfig() {
        ThirdDevConfig thirdDevConfig = this.thirdDevConfigMapper.selectByPrimaryKey("1");
        return thirdDevConfig;
    }
}



