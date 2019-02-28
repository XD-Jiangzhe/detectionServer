package com.detection.Server.processingPool.dealMessage2AppType;

import java.sql.Time;
import java.util.ArrayList;

/*处理解析之后的方法*/
public class dealTransferBlockString implements  Message2AppTypeMethod
{
    @Override
    public ArrayList<String> dealwithTransferTxt(String transferTxt) {
        ArrayList<String> result = new ArrayList<>();
        result.add(transferTxt);
        return result;
    }


    @Override
    public String detectionStrategy(ArrayList<String> contentList) {
        //测试一下先，之后会添加检测的方法进来
        try {
            Thread.sleep(3000);
        }catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
        return "adwo";
    }
}
