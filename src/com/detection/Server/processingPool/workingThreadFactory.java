package com.detection.Server.processingPool;

import com.detection.UtilLoggingModule.MyLogging;

import java.util.concurrent.ThreadFactory;

/*
    workingthread factory 工作：
        设置捕获异常的handler
        设置线程为后台线程，这样在server结束，即main结束的时候会自动杀掉整个后台线程
 */

class UncaughtExceptionHandler implements  Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        MyLogging.logger.error(t.getId() + " caught " + e.getMessage());
    }
}

public class workingThreadFactory  implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {

        Thread t = new Thread(r);

//        t.setDaemon(true);//设置后台属性
        t.setUncaughtExceptionHandler(new UncaughtExceptionHandler());//设置异常处理器
        return t;
    }
}
