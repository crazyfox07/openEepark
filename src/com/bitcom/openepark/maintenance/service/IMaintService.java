package com.bitcom.openepark.maintenance.service;

public interface IMaintService {
    void executeMonitorParkState();

    void executeSendDeviceFaultSms();
}



