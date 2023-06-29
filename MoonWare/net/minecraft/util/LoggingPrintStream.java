package net.minecraft.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class LoggingPrintStream extends PrintStream
{
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final String domain;

    public LoggingPrintStream(String domainIn, OutputStream outStream)
    {
        super(outStream);
        domain = domainIn;
    }

    public void println(String p_println_1_)
    {
        logString(p_println_1_);
    }

    public void println(Object p_println_1_)
    {
        logString(String.valueOf(p_println_1_));
    }

    protected void logString(String string)
    {
        LOGGER.info("[{}]: {}", domain, string);
    }
}
