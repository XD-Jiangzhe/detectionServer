package com.detection.UtilLoggingModule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyLogging {

    public static Logger logger = LogManager.getLogger("Logger");
    public static void main(String[] args) {
        logger.error("hello world");
        logger.trace("trace level");
        logger.debug("debug level");
    }

}
