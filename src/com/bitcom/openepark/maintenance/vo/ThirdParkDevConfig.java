package com.bitcom.openepark.maintenance.vo;

import com.bitcom.openepark.base.domain.ThirdDevConfig;

public class ThirdParkDevConfig
        extends ThirdDevConfig {
    private String parkCode;

    public String getParkCode() {
        return this.parkCode;
    }


    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
}



