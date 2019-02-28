package com.detection.Server.processingPool;

import com.detection.Server.IOServer.DetectionServer;
import com.detection.Server.ProtoMessage.ProgramInfo;
import com.detection.Server.processingPool.dealMessage2AppType.Message2AppTypeMethod;
import com.detection.Server.processingPool.dealMessage2AppType.dealTransferFactory;
import com.detection.UtilLoggingModule.MyLogging;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;


import java.util.ArrayList;

/**
 * 该类用来做线程池内的处理，即将process 过程从 netty server中分离出来
 */
public class Task {

    /*
    服务器引用，
    以及通道的Token,
    appName,
    消息的类型，
    需要解析的字符串,
    以及处理该字符串的接口
    */
    private DetectionServer _detectionServer;
    private String _channelToken;
    private String _appName;
    private String _transferTxt;
    private Message2AppTypeMethod _Message2AppTypeImp;


    public Task(DetectionServer detectionServer, String channelToken, String appName, int type, String transferTxt)
    {
        _detectionServer = detectionServer;
        _channelToken = channelToken;
        _appName = appName;
        _transferTxt = transferTxt;
        _Message2AppTypeImp = new dealTransferFactory().constructDealMethod(type);
    }

    /*
        定义task中要执行的任务
        获取运行结果，即种类的名称
        并将结果打包返回一个服务器的消息
        最后发送到客户端
     */
    public void run()
    {
        ArrayList<String> messageList = _Message2AppTypeImp.dealwithTransferTxt(_transferTxt);
        String appType =  _Message2AppTypeImp.detectionStrategy(messageList);
        Channel channel = _detectionServer.getChannelByHashId(_channelToken);
        if(channel != null) {
            channel.writeAndFlush(encodeMessage2Client(appType));
            MyLogging.logger.info("has send the detection result to client " + _channelToken);
        }
        else
        {
            MyLogging.logger.info("channelToken is " + _channelToken);
        }
    }

    private ProgramInfo.ReceiveMsg  encodeMessage2Client(String appType)
    {
        ProgramInfo.ReceiveMsg msg2InfoApptype = ProgramInfo.ReceiveMsg.newBuilder()
                .setType(2)
                .setAppType(appType)
                .setChannelToken(_channelToken)
                .setAppName(_appName)
                .build();
        return msg2InfoApptype;
    }
}
