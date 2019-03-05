package com.detection.Server.ProtoMessage;

public enum SendMsgType {
    Unused(0),
    TransferTxt(1),
    PermissionList(2);

    private int val;

    SendMsgType(int val) {
        this.val = val;
    }

    public static SendMsgType valueOf(int value)
    {
        switch (value)
        {
            case 0:
                return  Unused;
            case 1:
                return TransferTxt;
            case 2:
                return PermissionList;
            default:
                return Unused;
        }
    }

}


