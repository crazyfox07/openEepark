package com.bitcom.openepark.maintenance.vo;

import com.bitcom.openepark.base.domain.ThirdParkInfo;


public class ThirdParkStateVO
        extends ThirdParkInfo {
    private String state;
    private String deviceCode;

    public String getDeviceCode() {
        return this.deviceCode;
    }


    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }


    public String getState() {
        return this.state;
    }


    public void setState(String state) {
        this.state = state;
    }
}



