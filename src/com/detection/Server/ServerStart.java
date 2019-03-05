package com.detection.Server;

import com.detection.Server.IOServer.DetectionServer;
import com.detection.Server.processingPool.workingThreadFactory;
import com.detection.UtilLs.MyLogging;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.detection.UtilLs.readPropertyFile.propertiesFactory;
import static com.detection.UtilLs.readPropertyFile.propertiesFile2HashMap;

public class ServerStart {

    public static void main(String[] args) {
        try {

            HashMap<String, String> serverInfo = propertiesFile2HashMap("./Server.properties");

            int threadNum = Integer.parseInt(serverInfo.get("threadNum"));
            int port = Integer.parseInt(serverInfo.get("port"));

            //启动工作线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum, new workingThreadFactory());
            MyLogging.logger = LogManager.getLogger("Logger");

            new DetectionServer(port, exec).start();


        } catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
    }
}
