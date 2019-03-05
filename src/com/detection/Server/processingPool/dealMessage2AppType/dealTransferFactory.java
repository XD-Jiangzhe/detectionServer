package com.detection.Server.processingPool.dealMessage2AppType;

import com.detection.Server.ProtoMessage.SendMsgType;

//这里根据不同的type 生成不同的处理函数，来处理传入的不同的string
public class dealTransferFactory
{
    public Message2AppTypeMethod constructDealMethod(int type)
    {
        SendMsgType sendMsgType = SendMsgType.valueOf(type);
        switch (sendMsgType)
        {
            case TransferTxt:
                return new dealTransferBlockString();
            case PermissionList:
                return new dealTransferPermissionList();
            default:
                return new dealTransferBlockString();
        }
    }
}
