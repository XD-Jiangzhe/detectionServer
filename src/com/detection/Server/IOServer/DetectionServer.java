package com.detection.Server.IOServer;

import com.detection.UtilLoggingModule.MyLogging;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class DetectionServer {

    //绑定的端口
    private final int _port;

    /*
        指向工作线程池的引用，用来向工作线程池中投递task
        实现network io 与 process 的解耦 bridge模式
    */
    private ExecutorService _executorService;

    //已经连接上的channel，key 生成的token，observer模式
    private HashMap<String, Channel> _channels = new HashMap<>();

    public  DetectionServer(int port , ExecutorService executorService)
    {
        this._executorService = executorService;
        this._port = port;
    }


    //服务器的主启动函数
    public void start() throws  Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            DetectionServerInitializer detectionServerInitializer = new DetectionServerInitializer(this);

            b.childOption(ChannelOption.TCP_NODELAY, true);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(detectionServerInitializer)
                    .bind(_port)
                    .addListener((ChannelFutureListener) future->{
                        if(future.isSuccess())
                            MyLogging.logger.info("netty server start success");
                        else
                            MyLogging.logger.error("netty server start failed");
                    });
        }finally {

        }
    }

    public Channel getChannelByHashId(String hashid)
    {
        Channel res = null;
        if(_channels.containsKey(hashid))
            res = _channels.get(hashid);
        return res;
    }

    public void removeByHashId(String hashid)
    {
        _channels.remove(hashid);
    }

    public void removeByChannel(Channel channel)
    {
        _channels.values().remove(channel);
    }

    public void put(String hashid, Channel channel)
    {
        _channels.put(hashid, channel);
        if(channel != null)
            System.out.println("has put channel " + hashid);
    }

    public int getChannelsSize()
    {
        return _channels.size();
    }

    public ExecutorService get_executorService() {
        return _executorService;
    }

    public void set_executorService(ExecutorService _executorService) {
        this._executorService = _executorService;
    }
}
