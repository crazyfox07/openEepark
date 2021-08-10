package com.bitcom.openepark.api.web;

import com.bitcom.openepark.api.service.ParkServInvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ParkInvokeController {
    @Autowired
    private ParkServInvokeService parkServiceInvokeImpl;

    @RequestMapping(value = {"/parkServiceCheck.do"}, method = {RequestMethod.POST}, produces = {"text/plain; charset=utf-8"})
    @ResponseBody
    public String parkServiceCheck(String parkCode) {
        return this.parkServiceInvokeImpl.parkServiceCheck(parkCode);
    }


    @RequestMapping(value = {"/parkServiceInvoke.do"}, method = {RequestMethod.POST}, produces = {"text/plain; charset=utf-8"})
    @ResponseBody
    public String parkServiceInvoke(@RequestBody String content) {
        try {
            String res =  this.parkServiceInvokeImpl.parkServiceInvoke(content);
            System.out.println("停车场下发返回结果：" + res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "{'result':'fail'}";
        }
    }
}



