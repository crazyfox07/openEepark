package com.bitcom.openepark.api.service;

public interface IParkIntegrateService {
    String updateVerifyServer(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

    String addBindPark(String paramString1, String paramString2, String paramString3, String paramString4) throws Exception;

    String transferCustomData(String paramString1, String paramString2, String paramString3, String paramString4);

    String synchronousTransferCustomData(String paramString1, String paramString2, String paramString3, String paramString4);

    String getLeaguerInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);

    String parkLeaguerCharge(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws Exception;
}



