package com.bitcom.openepark.base.domain;


public class ThirdDomainParkRelat {
    private String nid;
    private String domainId;
    private String parkCode;

    public String getNid() {
        return this.nid;
    }


    public void setNid(String nid) {
        this.nid = (nid == null) ? null : nid.trim();
    }


    public String getDomainId() {
        return this.domainId;
    }


    public void setDomainId(String domainId) {
        this.domainId = (domainId == null) ? null : domainId.trim();
    }


    public String getParkCode() {
        return this.parkCode;
    }


    public void setParkCode(String parkCode) {
        this.parkCode = (parkCode == null) ? null : parkCode.trim();
    }
}



