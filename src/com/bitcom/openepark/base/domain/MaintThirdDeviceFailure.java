package com.bitcom.openepark.base.domain;

import java.util.Date;


public class MaintThirdDeviceFailure {
    private String nid;
    private String deviceCode;
    private String deviceType;
    private String parkCode;
    private String pointName;
    private String deviceStateCode;
    private String deviceStateName;
    private String state;
    private Date insertTime;
    private Date updateTime;

    public Date getInsertTime() {
        return this.insertTime;
    }


    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }


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


    public String getState() {
        return this.state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public Date getUpdateTime() {
        return this.updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}



