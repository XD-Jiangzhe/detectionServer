package com.detection.Server.IOServer;

import com.detection.Server.ProtoMessage.ProgramInfo;
import com.detection.Server.processingPool.Task;
import com.detection.Server.processingPool.myCallable;
import com.detection.UtilLs.MyLogging;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

import static com.detection.Server.ProtoMessage.ReceiveMsgType.RequestForHashId;


//需要线程安全
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<ProgramInfo.SendMsg> {

    private DetectionServer _detectionServer;

    protected ServerHandler(DetectionServer detectionServer) {
        super();
        _detectionServer = detectionServer;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        String token = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        MyLogging.logger.info("a new client " + token +" has registered ");
        ProgramInfo.ReceiveMsg MsgToSetToken = ProgramInfo.ReceiveMsg.newBuilder()
                .setType(RequestForHashId.getVal())
                .setAppName("")
                .setAppType("")
                .setChannelToken(token).build();

        ctx.channel().writeAndFlush(MsgToSetToken);
        //注册的时候添加到保存着的_channels 里面
        _detectionServer.put(token,ctx.channel());
    }

    public void channelRead0(ChannelHandlerContext ctx, ProgramInfo.SendMsg msg) throws Exception {

        MyLogging.logger.info(msg.getAppName() + " transfertext length : " + msg.getTransferTxt().length());

        //当收到的消息的类型不为0时，统一处理
        if(msg.getType() != 0)
        {
            /*这里扔到池子中计算结果，可能需要增加一个task类，使用command设计模式*/
            Task processTask = new Task(_detectionServer, msg.getChannelToken(), msg.getAppName(),
                    msg.getType(), msg.getTransferTxt());

            //将task扔到池子中执行后，通过token获取channel ，这样也可以观察channel是否关闭了
            myCallable myCallable = new myCallable(processTask);
            _detectionServer.get_executorService().execute(myCallable);
        }
    }

    public void channelInactive(ChannelHandlerContext ctx)
    {
        //客户端关闭，从channelList 中移除 关闭的客户端通道
        _detectionServer.removeByChannel(ctx.channel());
        MyLogging.logger.info("channel has been exits, channels size is " +_detectionServer.getChannelsSize());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        MyLogging.logger.error("exception Caught "+ cause.getMessage());
        ctx.close();
    }
}
