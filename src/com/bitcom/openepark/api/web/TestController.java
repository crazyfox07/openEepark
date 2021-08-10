package com.bitcom.openepark.api.web;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.api.service.ITestService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TestController {
    @Autowired
    private ITestService testServiceImpl;

    @RequestMapping(value = {"/servertest"}, method = {RequestMethod.GET})
    @ResponseBody
    public String connectionValid() {
        System.out.println("测试第三方服务对接接口!");
        return "success";
    }


    @RequestMapping({"/testReceive"})
    @ResponseBody
    public String receiveCustomData(HttpServletRequest request, @RequestBody String content) {
        System.out.println("===========测试epark收到信息" + content);
        return "success";
    }


    @RequestMapping(value = {"/servertest"}, method = {RequestMethod.POST})
    @ResponseBody
    public String receiveMessage(@RequestBody String content) {
        System.out.println("receive plateform request：" + content);
        int k = (int) (Math.random() * 10.0D);
        return JSONObject.parseObject("{ \"result\":\"success\", \"msg\":\"failure detail\", \"responseBody\":[{},{}] }").toString();
    }


    @RequestMapping({"/transactionUpdateTest"})
    @ResponseBody
    public String transactionUpdateTest() {
        try {
            return this.testServiceImpl.updateThirdDevConfig();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping({"/transactionReadTest"})
    @ResponseBody
    public Object transactionReadTest() {
        try {
            return this.testServiceImpl.queryThirdDevConfig();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



