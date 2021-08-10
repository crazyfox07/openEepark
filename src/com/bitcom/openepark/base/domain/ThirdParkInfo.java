package com.bitcom.openepark.base.domain;


public class ThirdParkInfo {
    private String parkCode;
    private String parkName;
    private String parkType;
    private Integer spaceCount;
    private String costRefer;
    private Integer yellowNum;
    private Integer redNum;
    private String orgId;
    private String address;
    private Double posLong;
    private Double posLat;
    private String contract;
    private String phone;
    private String chargeType;
    private String structType;

    public String getChargeType() {
        return this.chargeType;
    }


    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }


    public String getStructType() {
        return this.structType;
    }


    public void setStructType(String structType) {
        this.structType = structType;
    }


    public String getOrgId() {
        return this.orgId;
    }


    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public void setYellowNum(Integer yellowNum) {
        this.yellowNum = yellowNum;
    }


    public void setRedNum(Integer redNum) {
        this.redNum = redNum;
    }


    public String getParkCode() {
        return this.parkCode;
    }


    public void setParkCode(String parkCode) {
        this.parkCode = (parkCode == null) ? null : parkCode.trim();
    }


    public String getParkName() {
        return this.parkName;
    }


    public void setParkName(String parkName) {
        this.parkName = (parkName == null) ? null : parkName.trim();
    }


    public Integer getSpaceCount() {
        return this.spaceCount;
    }


    public void setSpaceCount(Integer spaceCount) {
        this.spaceCount = spaceCount;
    }


    public String getCostRefer() {
        return this.costRefer;
    }


    public void setCostRefer(String costRefer) {
        this.costRefer = (costRefer == null) ? null : costRefer.trim();
    }


    public String getAddress() {
        return this.address;
    }


    public Integer getYellowNum() {
        return this.yellowNum;
    }


    public void setYellowNum(int yellowNum) {
        this.yellowNum = Integer.valueOf(yellowNum);
    }


    public Integer getRedNum() {
        return this.redNum;
    }


    public void setRedNum(int redNum) {
        this.redNum = Integer.valueOf(redNum);
    }


    public void setAddress(String address) {
        this.address = (address == null) ? null : address.trim();
    }


    public Double getPosLong() {
        return this.posLong;
    }


    public void setPosLong(Double posLong) {
        this.posLong = posLong;
    }


    public Double getPosLat() {
        return this.posLat;
    }


    public void setPosLat(Double posLat) {
        this.posLat = posLat;
    }


    public String getContract() {
        return this.contract;
    }


    public void setContract(String contract) {
        this.contract = (contract == null) ? null : contract.trim();
    }


    public String getPhone() {
        return this.phone;
    }


    public void setPhone(String phone) {
        this.phone = (phone == null) ? null : phone.trim();
    }


    public String getParkType() {
        return this.parkType;
    }


    public void setParkType(String parkType) {
        this.parkType = parkType;
    }
}



