package com.bitcom.openepark.api.web;

import com.bitcom.openepark.api.service.IParkIntegrateService;
import com.bitcom.openepark.util.SignatureUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ParkIntegrateController {
    private Logger log = Logger.getLogger("ParkIntegrateController");


    @Autowired
    private IParkIntegrateService parkService;


    @RequestMapping(value = {"/verifyServer"}, method = {RequestMethod.POST}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String verifyServer(String domainId, String timeStamp, String sign, String servUrl, String version) {
        return this.parkService.updateVerifyServer(domainId, timeStamp, servUrl, sign, version);
    }


    @RequestMapping(value = {"/bindPark"}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String bindPark(String domainId, String timeStamp, String sign, String parks) {
        try {
            return this.parkService.addBindPark(domainId, timeStamp, parks, sign);
        } catch (Exception e) {
            e.printStackTrace();
            return "{result:'fail',msg:'" + e.getMessage() + "'}";
        }
    }


    @RequestMapping(value = {"/push/customData"}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String transferCustomData(String domainId, String timeStamp, String sign, String content) {
        return this.parkService.transferCustomData(domainId, timeStamp, sign, content);
    }


    @RequestMapping(value = {"/push/synchronousCustomData"}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String synchronousTransferCustomData(String domainId, String timeStamp, String sign, String content) {
        return this.parkService.synchronousTransferCustomData(domainId, timeStamp, sign, content);
    }


    @RequestMapping(value = {"/getLeaguerInfo"}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String getLeaguerInfo(String domainId, String timeStamp, String sign, String parkCode, String plateNo, String plateType) {
        if (parkCode == null) {
            parkCode = "";
        }
        if (plateType == null) {
            plateType = "";
        }
        this.log.info("【Debug】 domainId=" + domainId + " timeStamp=" + timeStamp + " parkCode=" + parkCode + " plateNo=" + plateNo + " plateType=" + plateType + " sign=" + sign);
        return this.parkService.getLeaguerInfo(domainId, timeStamp, parkCode, plateNo, plateType, sign);
    }


    @RequestMapping(value = {"/parkLeaguerCharge"}, produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String parkLeaguerCharge(String domainId, String timeStamp, String sign, String plateNo, String plateType, String chargeFee) {
        String result = "";
        try {
            result = this.parkService.parkLeaguerCharge(domainId, timeStamp, plateNo, plateType, chargeFee, sign);
            this.log.info("【封闭停车场扣费返回值：】=" + result);
            return result;
        } catch (Exception e) {
            this.log.error("【封闭停车场扣费返回值：】={result:'fail'}");
            return "{result:'fail'}";
        }
    }


    @RequestMapping({"/getSign"})
    @ResponseBody
    public String getSign(String value, String key) {
        String[] tempArray = value.split(";");
        return SignatureUtil.sign(key, tempArray);
    }
}



