package com.mojang.realmsclient.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import org.apache.logging.log4j.Logger;

public class RealmsDefaultUncaughtExceptionHandler implements UncaughtExceptionHandler
{
    private final Logger logger;

    public RealmsDefaultUncaughtExceptionHandler(Logger pLogger)
    {
        this.logger = pLogger;
    }

    public void uncaughtException(Thread pThread, Throwable pThrowable)
    {
        this.logger.error("Caught previously unhandled exception :");
        this.logger.error(pThrowable);
    }
}
