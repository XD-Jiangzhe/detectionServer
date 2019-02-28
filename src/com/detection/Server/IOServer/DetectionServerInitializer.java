package com.detection.Server.IOServer;

import com.detection.Server.ProtoMessage.ProgramInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class DetectionServerInitializer extends ChannelInitializer<SocketChannel> {

    //含有server指针，从而可以将新建的channel 放到 _channels 中
    private DetectionServer _detectionServer;


    public DetectionServerInitializer(DetectionServer detectionServer) {
        super();
        _detectionServer = detectionServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();
        //解码器
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(ProgramInfo.SendMsg.getDefaultInstance()));

        //编码器
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        socketChannel.pipeline().addLast(new ServerHandler(_detectionServer));

    }
}
