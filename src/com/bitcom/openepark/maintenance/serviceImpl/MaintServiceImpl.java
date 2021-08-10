package com.bitcom.openepark.maintenance.serviceImpl;

import com.bitcom.openepark.base.domain.MaintThirdDeviceFailure;
import com.bitcom.openepark.base.domain.MaintThirdDeviceState;
import com.bitcom.openepark.base.domain.MaintThirdDeviceStateInfo;
import com.bitcom.openepark.base.persistence.MaintThirdDeviceFailureMapper;
import com.bitcom.openepark.base.persistence.MaintThirdDeviceStateInfoMapper;
import com.bitcom.openepark.base.persistence.MaintThirdDeviceStateMapper;
import com.bitcom.openepark.base.persistence.ThirdDevConfigMapper;
import com.bitcom.openepark.base.persistence.ThirdParkInfoMapper;
import com.bitcom.openepark.common.util.PropertiesUtil;
import com.bitcom.openepark.maintenance.service.IMaintService;
import com.bitcom.openepark.maintenance.vo.ParkHealthState;
import com.bitcom.openepark.maintenance.vo.ThirdParkDevConfig;
import com.bitcom.openepark.maintenance.vo.ThirdParkStateVO;
import com.bitcom.openepark.util.HttpClientUtil;
import com.bitcom.openepark.util.HttpUtils;
import com.bitcom.openepark.util.IdGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MaintServiceImpl
        implements IMaintService {
    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private ThirdDevConfigMapper thirdDevConfigMapper;


    @Autowired
    private ThirdParkInfoMapper thirdParkInfoMapper;


    @Autowired
    private MaintThirdDeviceStateInfoMapper maintThirdDeviceStateInfoMapper;


    @Autowired
    private MaintThirdDeviceStateMapper maintThirdDeviceStateMapper;

    @Autowired
    private MaintThirdDeviceFailureMapper maintThirdDeviceFailureMapper;


    public void executeMonitorParkState() {
        List<ThirdParkDevConfig> configList = new ArrayList<>();
        List<ThirdParkStateVO> parkStateList = new ArrayList<>();
        Map<String, ParkHealthState> parkStateMap = new HashMap<>();

        configList = this.thirdDevConfigMapper.queryAllDevConfig();

        parkStateList = this.thirdParkInfoMapper.queryAllThirdParkStateInfo();

        StringBuffer sendSmsInfo = new StringBuffer();


        for (int i = 0; i < configList.size(); i++) {
            ParkHealthState healthVO = new ParkHealthState();
            ThirdParkDevConfig configVO = configList.get(i);

            if (configVO != null) {
                healthVO.setParkCode(configVO.getParkCode());
                healthVO.setState("fail");
                try {
                    String res = HttpUtils.sendGet(configVO.getServerUrl(), "token=" + configVO.getValidToken(), 200);
                    if (res.trim().contains("success")) {
                        healthVO.setState("success");
                        this.logger.info("==executeMonitorParkState()success：监测第三方封闭停车场，返回状态正常,parkCode=" + healthVO.getParkCode());
                    } else {
                        healthVO.setState("fail");
                        this.logger.info("==executeMonitorParkState()fail：监测第三方封闭停车场，返回状态失败,parkCode=" + healthVO.getParkCode());
                    }
                } catch (Exception e) {

                    healthVO.setState("fail");
                    this.logger.info("==executeMonitorParkState()Exception：监测第三方封闭停车场，网络连接超时异常,parkCode=" + healthVO.getParkCode());
                }

                parkStateMap.put(healthVO.getParkCode(), healthVO);
            }
        }


        for (int i = 0; i < parkStateList.size(); i++) {
            ThirdParkStateVO stateVO = parkStateList.get(i);


            if (stateVO.getState() == null) {

                String deviceCode = stateVO.getParkCode() + "_10";


                MaintThirdDeviceState maintThirdDeviceStateVO = new MaintThirdDeviceState();
                maintThirdDeviceStateVO.setNid(IdGenerator.getUUID());
                maintThirdDeviceStateVO.setDeviceCode(deviceCode);
                maintThirdDeviceStateVO.setDeviceType("10");
                maintThirdDeviceStateVO.setParkCode(stateVO.getParkCode());
                maintThirdDeviceStateVO.setPointName("--");
                maintThirdDeviceStateVO.setUpdateTime(new Date());

                if (parkStateMap.get(stateVO.getParkCode()) != null) {
                    if ("success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {
                        maintThirdDeviceStateVO.setDeviceStateCode("000");
                        maintThirdDeviceStateVO.setDeviceStateName("停车场服务正常");
                        maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(0));
                    } else {
                        maintThirdDeviceStateVO.setDeviceStateCode("001");
                        maintThirdDeviceStateVO.setDeviceStateName("停车场服务异常");
                        maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(1));
                    }
                } else {
                    maintThirdDeviceStateVO.setDeviceStateCode("001");
                    maintThirdDeviceStateVO.setDeviceStateName("停车场服务异常");
                    maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(1));
                }

                this.maintThirdDeviceStateMapper.insert(maintThirdDeviceStateVO);


                MaintThirdDeviceStateInfo vo = new MaintThirdDeviceStateInfo();
                vo.setNid(IdGenerator.getUUID());
                vo.setDeviceCode(deviceCode);
                vo.setDeviceType("10");
                vo.setParkCode(stateVO.getParkCode());
                vo.setPointName("--");
                vo.setUploadTime(new Date());
                vo.setInsertTime(new Date());
                if (parkStateMap.get(stateVO.getParkCode()) != null) {
                    if ("success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {
                        vo.setDeviceStateCode("000");
                        vo.setDeviceStateName("停车场服务正常");
                    } else {
                        vo.setDeviceStateCode("001");
                        vo.setDeviceStateName("停车场服务异常");
                    }
                } else {
                    vo.setDeviceStateCode("001");
                    vo.setDeviceStateName("停车场服务异常");
                }

                this.maintThirdDeviceStateInfoMapper.insert(vo);


            } else if (parkStateMap.get(stateVO.getParkCode()) == null || !stateVO.getState().equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {

                MaintThirdDeviceState maintThirdDeviceStateVO = this.maintThirdDeviceStateMapper.selectByDeviceCode(stateVO.getDeviceCode());
                if (parkStateMap.get(stateVO.getParkCode()) != null) {
                    if ("success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {
                        maintThirdDeviceStateVO.setDeviceStateCode("000");
                        maintThirdDeviceStateVO.setDeviceStateName("停车场服务正常");

                        maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(0));
                    } else {
                        maintThirdDeviceStateVO.setDeviceStateCode("001");
                        maintThirdDeviceStateVO.setDeviceStateName("停车场服务异常");

                        maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(maintThirdDeviceStateVO.getContinueFaultNum().intValue() + 1));
                    }
                } else {
                    maintThirdDeviceStateVO.setDeviceStateCode("001");
                    maintThirdDeviceStateVO.setDeviceStateName("停车场服务异常");

                    maintThirdDeviceStateVO.setContinueFaultNum(Integer.valueOf(maintThirdDeviceStateVO.getContinueFaultNum().intValue() + 1));
                }

                maintThirdDeviceStateVO.setUpdateTime(new Date());
                this.maintThirdDeviceStateMapper.update(maintThirdDeviceStateVO);


                MaintThirdDeviceStateInfo vo = new MaintThirdDeviceStateInfo();
                vo.setNid(IdGenerator.getUUID());
                vo.setDeviceCode(maintThirdDeviceStateVO.getDeviceCode());
                vo.setDeviceType("10");
                vo.setParkCode(stateVO.getParkCode());
                vo.setPointName("--");
                vo.setUploadTime(new Date());
                vo.setInsertTime(new Date());
                if (parkStateMap.get(stateVO.getParkCode()) != null) {
                    if ("success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {
                        vo.setDeviceStateCode("000");
                        vo.setDeviceStateName("停车场服务正常");
                    } else {
                        vo.setDeviceStateCode("001");
                        vo.setDeviceStateName("停车场服务异常");
                    }
                } else {
                    vo.setDeviceStateCode("001");
                    vo.setDeviceStateName("停车场服务异常");
                }


                this.maintThirdDeviceStateInfoMapper.insert(vo);
            }


            MaintThirdDeviceFailure failureVO = this.maintThirdDeviceFailureMapper.selectByDeviceCode(stateVO.getDeviceCode());
            if (failureVO == null) {
                if (parkStateMap.get(stateVO.getParkCode()) != null && "fail".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {
                    MaintThirdDeviceFailure vo = new MaintThirdDeviceFailure();
                    vo.setNid(IdGenerator.getUUID());
                    vo.setDeviceCode(stateVO.getParkCode() + "_10");
                    vo.setDeviceType("10");
                    vo.setParkCode(stateVO.getParkCode());
                    vo.setPointName("--");
                    vo.setDeviceStateCode("001");
                    vo.setDeviceStateName("停车场服务异常");
                    vo.setState("9901");
                    vo.setInsertTime(new Date());
                    vo.setUpdateTime(new Date());

                    this.maintThirdDeviceFailureMapper.insert(vo);
                } else if (parkStateMap.get(stateVO.getParkCode()) == null || !"success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState())) {


                    if (parkStateMap.get(stateVO.getParkCode()) == null) {
                        MaintThirdDeviceFailure vo = new MaintThirdDeviceFailure();
                        vo.setNid(IdGenerator.getUUID());
                        vo.setDeviceCode(stateVO.getParkCode() + "_10");
                        vo.setDeviceType("10");
                        vo.setParkCode(stateVO.getParkCode());
                        vo.setPointName("--");
                        vo.setDeviceStateCode("001");
                        vo.setDeviceStateName("停车场服务异常");
                        vo.setState("9901");
                        vo.setInsertTime(new Date());
                        vo.setUpdateTime(new Date());

                        this.maintThirdDeviceFailureMapper.insert(vo);
                    }
                }
            } else if (failureVO != null) {
                if (parkStateMap.get(stateVO.getParkCode()) != null) {

                    if ("success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState()) && "9901".equals(failureVO.getState())) {
                        failureVO.setState("9902");
                        failureVO.setUpdateTime(new Date());
                        this.maintThirdDeviceFailureMapper.update(failureVO);

                    } else if (!"success".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState()) || !"9902".equals(failureVO.getState())) {


                        if (!"fail".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState()) || !"9901".equals(failureVO.getState())) {

                            if ("fail".equals(((ParkHealthState) parkStateMap.get(stateVO.getParkCode())).getState()) && "9902".equals(failureVO.getState())) {
                                MaintThirdDeviceFailure vo = new MaintThirdDeviceFailure();
                                vo.setNid(IdGenerator.getUUID());
                                vo.setDeviceCode(stateVO.getParkCode() + "_10");
                                vo.setDeviceType("10");
                                vo.setParkCode(stateVO.getParkCode());
                                vo.setPointName("--");
                                vo.setDeviceStateCode("001");
                                vo.setDeviceStateName("停车场服务异常");
                                vo.setState("9901");
                                vo.setInsertTime(new Date());
                                vo.setUpdateTime(new Date());

                                this.maintThirdDeviceFailureMapper.insert(vo);
                            }
                        }
                    }
                } else if (parkStateMap.get(stateVO.getParkCode()) == null) {

                    if (!"9901".equals(failureVO.getState())) {


                        if ("9902".equals(failureVO.getState())) {
                            MaintThirdDeviceFailure vo = new MaintThirdDeviceFailure();
                            vo.setNid(IdGenerator.getUUID());
                            vo.setDeviceCode(stateVO.getParkCode() + "_10");
                            vo.setDeviceType("10");
                            vo.setParkCode(stateVO.getParkCode());
                            vo.setPointName("--");
                            vo.setDeviceStateCode("001");
                            vo.setDeviceStateName("停车场服务异常");
                            vo.setState("9901");
                            vo.setInsertTime(new Date());
                            vo.setUpdateTime(new Date());

                            this.maintThirdDeviceFailureMapper.insert(vo);
                        }
                    }
                }
            }
        }
    }


    public void executeSendDeviceFaultSms() {
        StringBuffer sendSmsInfo = new StringBuffer();

        List<MaintThirdDeviceState> maintThirdDeviceStateList = this.maintThirdDeviceStateMapper.queryMaintThirdDeviceStateByContinuFaultNum();
        for (int i = 0; i < maintThirdDeviceStateList.size(); i++) {
            MaintThirdDeviceState tempVO = maintThirdDeviceStateList.get(i);
            sendSmsInfo.append(tempVO.getParkCode() + ",");
            sendSmsInfo.append("10,");
            sendSmsInfo.append("001,");
            sendSmsInfo.append("停车场服务异常&");
        }

        String sendSmsParam = sendSmsInfo.toString();
        if (sendSmsParam != null && sendSmsParam.length() > 0) {
            sendSmsParam = sendSmsParam.substring(0, sendSmsParam.length() - 1);

            sendDeviceStateAlarmSms(sendSmsParam);
        }
    }


    private void sendDeviceStateAlarmSms(String sendSmsInfo) {
        String eparkUrl = PropertiesUtil.getProperty("eparkURL");
        Map<String, String> map = new HashMap<>();
        map.put("param", sendSmsInfo);

        if (eparkUrl != null && eparkUrl.trim().length() > 0) {
            HttpClientUtil.sendHttpPost(eparkUrl + "thirdPark/sendMaintThirdParkStateSms.do", map);
            this.logger.info("===调用Epark封闭停车场设备故障手机短信通知接口，param==" + sendSmsInfo);
        }
    }
}



