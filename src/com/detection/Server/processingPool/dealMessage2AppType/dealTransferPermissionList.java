package com.detection.Server.processingPool.dealMessage2AppType;

import com.detection.UtilLs.MyLogging;
import com.detection.UtilLs.readPropertyFile;

import java.util.ArrayList;
import java.util.Arrays;

/*处理权限序列的方法*/
class dealTransferPermissionList implements  Message2AppTypeMethod
{
    @Override
    public ArrayList<String> dealwithTransferTxt(String transferTxt)
    {
        String[] permissions = transferTxt.split("\\t");
        MyLogging.logger.info("permission length : " +  permissions.length);

        return  new ArrayList<>(Arrays.asList(permissions));
    }

    @Override
    public String detectionStrategy(ArrayList<String> contentList)
    {
        String appType= "locker";
        return readPropertyFile.malware2Name.get(appType);
    }
}
