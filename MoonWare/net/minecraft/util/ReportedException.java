package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException
{
    /** The crash report associated with this exception */
    private final CrashReport crashReport;

    public ReportedException(CrashReport report)
    {
        crashReport = report;
    }

    /**
     * Gets the CrashReport wrapped by this exception.
     */
    public CrashReport getCrashReport()
    {
        return crashReport;
    }

    public Throwable getCause()
    {
        return crashReport.getCrashCause();
    }

    public String getMessage()
    {
        return crashReport.getDescription();
    }
}
