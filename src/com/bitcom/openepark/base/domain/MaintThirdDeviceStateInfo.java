package com.bitcom.openepark.base.domain;

import java.util.Date;


public class MaintThirdDeviceStateInfo {
    private String nid;
    private String deviceCode;
    private String deviceType;
    private String parkCode;
    private String pointName;
    private String deviceStateCode;
    private String deviceStateName;
    private Date uploadTime;
    private Date insertTime;

    public String getNid() {
        return this.nid;
    }


    public void setNid(String nid) {
        this.nid = nid;
    }


    public String getDeviceCode() {
        return this.deviceCode;
    }


    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }


    public String getDeviceType() {
        return this.deviceType;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public String getParkCode() {
        return this.parkCode;
    }


    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }


    public String getPointName() {
        return this.pointName;
    }


    public void setPointName(String pointName) {
        this.pointName = pointName;
    }


    public String getDeviceStateCode() {
        return this.deviceStateCode;
    }


    public void setDeviceStateCode(String deviceStateCode) {
        this.deviceStateCode = deviceStateCode;
    }


    public String getDeviceStateName() {
        return this.deviceStateName;
    }


    public void setDeviceStateName(String deviceStateName) {
        this.deviceStateName = deviceStateName;
    }


    public Date getUploadTime() {
        return this.uploadTime;
    }


    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }


    public Date getInsertTime() {
        return this.insertTime;
    }


    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}



