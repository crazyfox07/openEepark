package com.bitcom.openepark.base.domain;

import java.util.Date;


public class ThirdDevConfig {
    private String domainId;
    private String domainKey;
    private String serverUrl;
    private Date verifyTime;
    private String isEncrpt;
    private String encrptType;
    private String encrptKey;
    private String validToken;
    private String servEnable;
    private String postUrl;
    private String contract;
    private String phone;

    public String getDomainId() {
        return this.domainId;
    }


    public void setDomainId(String domainId) {
        this.domainId = (domainId == null) ? null : domainId.trim();
    }


    public String getDomainKey() {
        return this.domainKey;
    }


    public void setDomainKey(String domainKey) {
        this.domainKey = (domainKey == null) ? null : domainKey.trim();
    }


    public String getServerUrl() {
        return this.serverUrl;
    }


    public void setServerUrl(String serverUrl) {
        this.serverUrl = (serverUrl == null) ? null : serverUrl.trim();
    }


    public Date getVerifyTime() {
        return this.verifyTime;
    }


    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }


    public String getIsEncrpt() {
        return this.isEncrpt;
    }


    public void setIsEncrpt(String isEncrpt) {
        this.isEncrpt = (isEncrpt == null) ? null : isEncrpt.trim();
    }


    public String getEncrptType() {
        return this.encrptType;
    }


    public void setEncrptType(String encrptType) {
        this.encrptType = (encrptType == null) ? null : encrptType.trim();
    }


    public String getEncrptKey() {
        return this.encrptKey;
    }


    public void setEncrptKey(String encrptKey) {
        this.encrptKey = (encrptKey == null) ? null : encrptKey.trim();
    }


    public String getValidToken() {
        return this.validToken;
    }


    public void setValidToken(String validToken) {
        this.validToken = (validToken == null) ? null : validToken.trim();
    }


    public String getServEnable() {
        return this.servEnable;
    }


    public void setServEnable(String servEnable) {
        this.servEnable = (servEnable == null) ? null : servEnable.trim();
    }


    public String getPostUrl() {
        return this.postUrl;
    }


    public void setPostUrl(String postUrl) {
        this.postUrl = (postUrl == null) ? null : postUrl.trim();
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
}



