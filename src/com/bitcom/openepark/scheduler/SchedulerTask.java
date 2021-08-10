package com.bitcom.openepark.scheduler;

import com.bitcom.openepark.base.persistence.MaintThirdDeviceStateInfoMapper;
import com.bitcom.openepark.common.util.PropertiesUtil;
import com.bitcom.openepark.maintenance.service.IMaintService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SchedulerTask {
    protected Logger log = Logger.getLogger(getClass());


    @Autowired
    private IMaintService maintServiceImpl;


    @Autowired
    private MaintThirdDeviceStateInfoMapper maintThirdDeviceStateInfoMapper;


    @Scheduled(cron = "0/60 * * * * ?")
    public void executeMonitorParkState() {
        if ("true".equals(PropertiesUtil.getProperty("park_state_task_enable"))) {
            System.out.println("***********************定时任务： 执行监测第三方封闭停车场的网络状态**********************");
            this.maintServiceImpl.executeMonitorParkState();
        }
    }


    @Scheduled(cron = "30/60 * * * * ?")
    public void executeSendDeviceFaultSms() {
        if ("true".equals(PropertiesUtil.getProperty("park_state_task_enable"))) {
            System.out.println("***********************定时任务： 执行故障报警短信发送**********************");
            this.maintServiceImpl.executeSendDeviceFaultSms();
        }
    }


    @Scheduled(cron = "0 05 00 * * ?")
    public void executeTruncateMaintThirdDeviceStateInfo() throws Exception {
        if ("true".equals(PropertiesUtil.getProperty("park_state_task_enable"))) {
            this.log.info("***********************定时任务： 清空maint_third_device_state_info表的数据开始**********************");
            this.maintThirdDeviceStateInfoMapper.truncateTable();
        }
    }
}



