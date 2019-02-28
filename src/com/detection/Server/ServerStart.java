package com.detection.Server;

import com.detection.Server.IOServer.DetectionServer;
import com.detection.Server.processingPool.workingThreadFactory;
import com.detection.UtilLoggingModule.MyLogging;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStart {

    public static void main(String[] args) {
        try {
            //启动工作线程池
            ExecutorService exec = Executors.newFixedThreadPool(5, new workingThreadFactory());
            MyLogging.logger = LogManager.getLogger("Logger");

            int port = 2333;
            new DetectionServer(port, exec).start();


        } catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
    }

}
