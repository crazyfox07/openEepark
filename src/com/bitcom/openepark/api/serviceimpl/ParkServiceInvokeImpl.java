package com.bitcom.openepark.api.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.api.service.ParkServInvokeService;
import com.bitcom.openepark.api.vo.HttpTaskManager;
import com.bitcom.openepark.base.domain.ThirdDevConfig;
import com.bitcom.openepark.base.persistence.ThirdDevConfigMapper;
import com.bitcom.openepark.util.HttpUtils;

import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParkServiceInvokeImpl
        implements ParkServInvokeService {
    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private ThirdDevConfigMapper thirdDevConfigMapper;


    public static final int TIMEOUT = 5000;


    public String parkServiceInvoke(String message) throws Exception {
        this.logger.info("【处理平台请求】开始，收到信息：" + message);
        JSONObject retJson = new JSONObject();
        retJson.put("result", "success");
        try {
            JSONArray jsonArray = JSONArray.parseArray(message);
            HttpTaskManager manager = new HttpTaskManager();
            String[][] requestArray = new String[jsonArray.size()][2];
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                String parkCode = o.getString("parkCode");
                String requestBody = o.getString("requestBody");
                String url = this.thirdDevConfigMapper.queryServUrlByParkCode(parkCode.trim());
                if (url != null && !"".equals(url)) {
                    requestArray[i][0] = url;
                    requestArray[i][1] = requestBody;
                } else {
                    this.logger.info("【处理平台请求】:停车场域关系不正确，无服务地址");
                }
            }
            this.logger.info("【处理平台请求】:分发任务开始");
            manager.setTask(requestArray);
            (new Thread((Runnable) manager)).start();
            String[] results = manager.getResult(5000L);
            this.logger.info("【处理平台请求】:回收结果，结果长度 " + results.length + ", 内容：" + ArrayUtils.toString(results));

            JSONArray retJsonArray = new JSONArray();

            for (int i = 0; i < results.length; i++) {
                JSONObject jsonObj = JSONObject.parseObject(results[i]);
                if ("success".equals(jsonObj.getString("result"))) {
                    try {
                        JSONArray responseJsonArray = jsonObj.getJSONArray("responseBody");
                        if (responseJsonArray.size() > 0) {
                            retJsonArray.addAll((Collection) responseJsonArray);
                        }
                    } catch (Exception e) {
                        this.logger.error("【停车场返回信息不正确】：Exception（但影响不大）文本为：" + results[i]);
                    }
                } else {

                    retJson.put("result", "fail");
                    this.logger.error("【请求停车场服务异常】:Exception （小停车场返回的信息）" + jsonObj.getString("msg"));
                }
            }

            if (jsonArray.size() != results.length) {
                retJson.put("result", "fail");
            }
            retJson.put("retValue", retJsonArray);
            this.logger.info("【平台请求】：结束，返回：" + retJson.toString());
            return retJson.toString();
        } catch (Exception e) {
            this.logger.error("【平台请求】：Exception" + e);
            throw e;
        }
    }


    public String parkServiceCheck(String parkCode) {
        String result = "fail";
        ThirdDevConfig config = this.thirdDevConfigMapper.queryDevConfigByParkCode(parkCode.trim());
        if (config != null) {
            try {
                String res = HttpUtils.sendGet(config.getServerUrl(), "token=" + config.getValidToken(), 300);
                if (res.trim().contains("success")) {
                    result = "success";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "fail";
                return result;
            }
        }
        return result;
    }
}



