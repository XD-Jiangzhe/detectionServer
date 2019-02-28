package com.detection.Server.processingPool.dealMessage2AppType;

//这里根据不同的type 生成不同的处理函数，来处理传入的不同的string
public class dealTransferFactory
{
    public Message2AppTypeMethod constructDealMethod(int type)
    {
        switch (type)
        {
            case 2:
                return new dealTransferBlockString();
            case 3:
                return new dealTransferPermissionList();
            default:
                return new dealTransferBlockString();
        }
    }
}
