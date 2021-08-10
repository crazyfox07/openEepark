package com.bitcom.openepark.api.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.bitcom.openepark.api.service.IParkIntegrateService;
import com.bitcom.openepark.api.vo.RedisTask;
import com.bitcom.openepark.base.domain.ThirdDevConfig;
import com.bitcom.openepark.base.domain.ThirdDomainParkRelat;
import com.bitcom.openepark.base.persistence.ThirdDevConfigMapper;
import com.bitcom.openepark.base.persistence.ThirdDomainParkRelatMapper;
import com.bitcom.openepark.data.service.IDevConfigService;
import com.bitcom.openepark.queue.RedisQueue;
import com.bitcom.openepark.util.HttpUtils;
import com.bitcom.openepark.util.IdGenerator;
import com.bitcom.openepark.util.SignatureUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParkIntegrateServiceImpl
        implements IParkIntegrateService {
    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private ThirdDevConfigMapper thirdDevConfigMapper;


    @Autowired
    private ThirdDomainParkRelatMapper domainParkRelatMapper;


    @Autowired
    private IDevConfigService devConfigServiceImpl;


    @Autowired
    private RedisQueue queue;


    public String updateVerifyServer(String domainId, String timeStamp, String servUrl, String sign, String version) {
        long currTime = System.currentTimeMillis();
        this.logger.info("【Server verify】#" + currTime + "#: begin. domainId=" + domainId + "  servUrl=" + servUrl);
        if (StringUtils.isEmpty(version)) {
            version = "";
        }

        Map<String, String> result = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp, version, servUrl});

        try {
            if ("success".equals(result.get("result"))) {
                String platformVer = this.thirdDevConfigMapper.getOpenEparkProtocolVersion();
                ThirdDevConfig config = this.devConfigServiceImpl.getByPrimaryKey(domainId);
                if (!validVersion(platformVer, version)) {
                    result.put("result", "fail");
                    result.put("msg", "Max version:" + platformVer);
                    this.devConfigServiceImpl.updateValidServer(domainId, servUrl, "0102");
                    return JSONObject.toJSONString(result);
                }

                String res = HttpUtils.sendGet(servUrl, "token=" + config.getValidToken(), 3000);
                if (res.trim().contains("success")) {
                    this.devConfigServiceImpl.updateValidServer(domainId, servUrl, "0101");
                    result.put("result", "success");
                    result.put("msg", "verify success");
                } else {
                    this.logger.info("【Server verify】：server url unreachable.");
                    result.put("result", "fail");
                    result.put("msg", "server url verified fail.");
                }
            }
        } catch (Exception e) {
            this.logger.error("【Server verify】#" + currTime + "#timeout Exception. domainId=" + domainId + " servUrl=" + servUrl);
            this.logger.error("【Server verify】#" + currTime + "#" + e.getMessage());
            result.put("result", "fail");
            result.put("msg", "verify fail");
            return JSONObject.toJSONString(result);
        }
        return JSONObject.toJSONString(result);
    }

    private boolean validVersion(String platformVer, String version) {
        if (StringUtils.isEmpty(version)) {
            version = "0";
        }

        if (Integer.parseInt(platformVer) < Integer.parseInt(version)) {
            return false;
        }
        return true;
    }


    public String addBindPark(String domainId, String timeStamp, String parks, String sign) throws Exception {
        this.logger.info("【Parks Bind】#" + timeStamp + "#: begin. domainId=" + domainId + "  parks=" + parks);

        Map<String, String> result = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp, parks});
        try {
            if ("success".equals(result.get("result"))) {

                String[] parkArr = parks.split(",");
                String repeatParkCode = "";
                boolean IsRepeat = false;
                for (int i = 0; i < parkArr.length; i++) {
                    if (parkArr[i] != null && !"".equals(parkArr[i])) {
                        int count = this.domainParkRelatMapper.queryRecordCountExcept(domainId, parkArr[i]);
                        if (count > 0) {
                            IsRepeat = true;
                            repeatParkCode = parkArr[i];
                            break;
                        }
                    }
                }
                if (IsRepeat == true) {
                    result.put("result", "fail");
                    result.put("msg", "parkCode has bind,please change and retry.parkCode=" + repeatParkCode);
                } else {

                    List<ThirdDomainParkRelat> binds = this.domainParkRelatMapper.selectByDomainId(domainId);
                    for (ThirdDomainParkRelat vo : binds) {
                        this.domainParkRelatMapper.deleteByPrimaryKey(vo.getNid());
                    }

                    for (int i = 0; i < parkArr.length; i++) {
                        if (parkArr[i] != null && !"".equals(parkArr[i])) {
                            ThirdDomainParkRelat ralation = new ThirdDomainParkRelat();
                            ralation.setNid(IdGenerator.getUUID());
                            ralation.setDomainId(domainId);
                            ralation.setParkCode(parkArr[i]);
                            this.domainParkRelatMapper.insert(ralation);
                        }
                    }
                    result.put("msg", "park bind success");
                }
            } else {
                this.logger.error("【Parks Bind】#" + timeStamp + "# sign fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.getMessage());
            throw e;
        }
        return JSONObject.toJSONString(result);
    }


    public String transferCustomData(String domainId, String timeStamp, String sign, String content) {
        long currTime = System.currentTimeMillis();
        if (content.length() > 2000) {
                    int indexOne = content.indexOf('[');
                    int indexTwo = content.indexOf(']');
                    String subContent = content.substring(0, indexOne) + "..." + content.substring(indexTwo, content.length());
                    this.logger.info("【Data Enqueue】#" + currTime + "#begin：domainId=" + domainId + " content=" + subContent);
                } else {
                    this.logger.info("【Data Enqueue】#" + currTime + "#begin：domainId=" + domainId + " content=" + content);
                }

                Map<String, String> result = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp});
                if ("success".equals(result.get("result"))) {
                    ThirdDevConfig config = this.devConfigServiceImpl.getByPrimaryKey(domainId);
                    if ("0101".equals(config.getServEnable())) {
                        String url = config.getPostUrl();

                        RedisTask task = new RedisTask();
                        task.setTaskId(IdGenerator.getUUID());
                        task.setContent(content);
                task.setPostUrl(url);
                this.queue.offer(task);
                result.put("msg", "message send success");

                this.logger.info("【Data Enqueue】#" + currTime + "# Enqueue success. taskId=" + task.getTaskId());
            } else {
                result.put("result", "fail");
                result.put("msg", "Server is disabled.");
            }
        }
        this.logger.info("【Data Enqueue】#" + currTime + "#：finsh. ret= " + JSONObject.toJSONString(result));
        return JSONObject.toJSONString(result);
    }


    public String synchronousTransferCustomData(String domainId, String timeStamp, String sign, String content) {
        Map<String, Object> resultMap = new HashMap<>();

        long currTime = System.currentTimeMillis();
        if (content.length() > 2000) {
            int indexOne = content.indexOf('[');
            int indexTwo = content.indexOf(']');
            String subContent = content.substring(0, indexOne) + "..." + content.substring(indexTwo, content.length());
            this.logger.info("【Sync Call】#" + currTime + "#begin：domainId=" + domainId + " content=" + subContent);
        } else {
            this.logger.info("【Sync Call】#" + currTime + "#begin：domainId=" + domainId + " content=" + content);
        }

        Map<String, String> resultValid = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp});
        resultMap.put("result", resultValid.get("result"));
        resultMap.put("msg", resultValid.get("msg"));

        if ("success".equals(resultValid.get("result"))) {
            ThirdDevConfig config = this.devConfigServiceImpl.getByPrimaryKey(domainId);
            if ("0101".equals(config.getServEnable())) {
                String url = config.getPostUrl();
                try {
                    int lastIndex = url.lastIndexOf("/");
                    String redirectUrl = url.substring(0, lastIndex) + "/synchronousTransferCustomData.do";

                    String res = HttpUtils.sendPost(redirectUrl, content, "text/plain;charset=utf-8");
                    this.logger.info("【Sync Call】#" + currTime + "# res=" + res);
                    JSONObject json = JSONObject.parseObject(JSONObject.parse(res).toString());
                    resultMap.put("msg", json.getString("message"));
                    if (StringUtils.equals("0", json.getString("code"))) {
                        resultMap.put("result", "success");
                        resultMap.put("responseBody", json.get("data"));
                    } else {
                        resultMap.put("result", "fail");
                    }
                } catch (Exception e) {
                    this.logger.error(e.getMessage());
                    resultMap.put("result", "fail");
                    resultMap.put("msg", "request epark fail.");
                }
            } else {
                resultMap.put("result", "fail");
                resultMap.put("msg", "server disabled.");
            }
        }

        return JSONObject.toJSONString(resultMap);
    }


    private Map<String, String> commonRequestValid(String domainId, String sign, String[] args) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");

        boolean paramIsNull = false;
        if (sign == null || "".equals(sign)) {
            paramIsNull = true;
        }
        for (int i = 0; i < args.length; i++) {
            if (null == args[i]) {
                paramIsNull = true;
            }
        }
        if (paramIsNull == true) {
            this.logger.info("Request params not complete.");
            result.put("result", "fail");
            result.put("msg", "parameters incomplete.");
        }

        if ("success".equals(result.get("result"))) {
            ThirdDevConfig config = this.devConfigServiceImpl.getByPrimaryKey(domainId);
            if (config == null) {
                this.logger.info("ThirdDevConfig Domain not exist .domainId=" + domainId);
                result.put("result", "fail");
                result.put("msg", "domain not exist.");
            } else {
                String domainKey = config.getDomainKey();
                String signature = SignatureUtil.sign(domainKey, args);
                if (!sign.equals(signature)) {

                    this.logger.info("sign valid failure. should=" + signature + " but=" + sign);
                    result.put("result", "fail");
                    result.put("msg", "sign error.");
                }
            }
        }
        return result;
    }


    public String getLeaguerInfo(String domainId, String timeStamp, String parkCode, String plateNo, String plateType, String sign) {
        this.logger.info("【小停车场获取会员信息】:" + plateNo);


        Map<String, String> result = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp, parkCode, plateNo, plateType});

        JSONObject jsonObject = null;
        if ("success".equals(result.get("result"))) {
            ThirdDevConfig config = this.thirdDevConfigMapper.selectByPrimaryKey(domainId);
            String url = config.getPostUrl();
            try {
                int lastIndex = url.lastIndexOf("/");
                String redirectUrl = url.substring(0, lastIndex) + "/queryLeaguerInfo.do";
                String jsonStr = HttpUtils.sendPost(redirectUrl, "parkCode=" + parkCode + "&plateNo=" + plateNo + "&plateType=" + plateType, "application/x-www-form-urlencoded; charset=UTF-8");

                jsonObject = JSONObject.parseObject(jsonStr);
            } catch (Exception e) {
                this.logger.info("【小停车场获取会员信息】plateNo=" + plateNo + " 非会员车辆 ,直接return null");
                return null;
            }
        }
        if (jsonObject != null) {
            Double currSum = jsonObject.getDouble("currSum");
            jsonObject.put("currSum", String.valueOf(currSum));
        }
        return (jsonObject == null) ? null : jsonObject.toString();
    }


    public String parkLeaguerCharge(String domainId, String timeStamp, String plateNo, String plateType, String chargeFee, String sign) throws Exception {
        this.logger.info("【小停车场会员扣费请求】:" + plateNo);

        Map<String, String> result = commonRequestValid(domainId, sign, new String[]{domainId, timeStamp, plateNo, plateType, chargeFee});

        if ("success".equals(result.get("result"))) {
            ThirdDevConfig config = this.thirdDevConfigMapper.selectByPrimaryKey(domainId);
            String url = config.getPostUrl();
            try {
                int lastIndex = url.lastIndexOf("/");
                String redirectUrl = url.substring(0, lastIndex) + "/parkLeaguerCharge.do";
                String respStr = HttpUtils.sendPost(redirectUrl, "plateNo=" + plateNo + "&plateType=" + plateType + "&chargeFee=" + chargeFee, "application/x-www-form-urlencoded; charset=UTF-8");

                if (!"success".equals(respStr)) {
                    result.put("result", "fail");
                    this.logger.error("【小停车场会员扣费请求】：扣费失败。");
                }
            } catch (Exception e) {
                this.logger.error(e.getMessage());
                throw e;
            }
        }
        return JSONObject.toJSONString(result);
    }
}



