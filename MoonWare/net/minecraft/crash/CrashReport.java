package net.minecraft.crash;

import com.google.common.collect.Lists;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import optifine.CrashReporter;
import optifine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CrashReport
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** Description of the crash report. */
    private final String description;

    /** The Throwable that is the "cause" for this crash and Crash Report. */
    private final Throwable cause;

    /** Category of crash */
    private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();

    /** File of crash report. */
    private File crashReportFile;

    /** Is true when the current category is the first in the crash report */
    private boolean firstCategoryInCrashReport = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];
    private boolean reported;

    public CrashReport(String descriptionIn, Throwable causeThrowable)
    {
        description = descriptionIn;
        cause = causeThrowable;
        populateEnvironment();
    }

    /**
     * Populates this crash report with initial information about the running server and operating system / java
     * environment
     */
    private void populateEnvironment()
    {
        theReportCategory.setDetail("Minecraft Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return "1.12.2";
            }
        });
        theReportCategory.setDetail("Operating System", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        theReportCategory.setDetail("Java Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }
        });
        theReportCategory.setDetail("Java VM Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        theReportCategory.setDetail("Memory", new ICrashReportDetail<String>()
        {
            public String call()
            {
                Runtime runtime = Runtime.getRuntime();
                long i = runtime.maxMemory();
                long j = runtime.totalMemory();
                long k = runtime.freeMemory();
                long l = i / 1024L / 1024L;
                long i1 = j / 1024L / 1024L;
                long j1 = k / 1024L / 1024L;
                return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
            }
        });
        theReportCategory.setDetail("JVM Flags", new ICrashReportDetail<String>()
        {
            public String call()
            {
                RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                List<String> list = runtimemxbean.getInputArguments();
                int i = 0;
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : list)
                {
                    if (s.startsWith("-X"))
                    {
                        if (i++ > 0)
                        {
                            stringbuilder.append(" ");
                        }

                        stringbuilder.append(s);
                    }
                }

                return String.format("%d total; %s", i, stringbuilder);
            }
        });
        theReportCategory.setDetail("IntCache", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return IntCache.getCacheSizes();
            }
        });

        if (Reflector.FMLCommonHandler_enhanceCrashReport.exists())
        {
            Object object = Reflector.call(Reflector.FMLCommonHandler_instance);
            Reflector.callString(object, Reflector.FMLCommonHandler_enhanceCrashReport, this, theReportCategory);
        }
    }

    /**
     * Returns the description of the Crash Report.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the Throwable object that is the cause for the crash and Crash Report.
     */
    public Throwable getCrashCause()
    {
        return cause;
    }

    /**
     * Gets the various sections of the crash report into the given StringBuilder
     */
    public void getSectionsInStringBuilder(StringBuilder builder)
    {
        if ((stacktrace == null || stacktrace.length <= 0) && !crashReportSections.isEmpty())
        {
            stacktrace = ArrayUtils.subarray(crashReportSections.get(0).getStackTrace(), 0, 1);
        }

        if (stacktrace != null && stacktrace.length > 0)
        {
            builder.append("-- Head --\n");
            builder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            builder.append("Stacktrace:\n");

            for (StackTraceElement stacktraceelement : stacktrace)
            {
                builder.append("\t").append("at ").append(stacktraceelement);
                builder.append("\n");
            }

            builder.append("\n");
        }

        for (CrashReportCategory crashreportcategory : crashReportSections)
        {
            crashreportcategory.appendToStringBuilder(builder);
            builder.append("\n\n");
        }

        theReportCategory.appendToStringBuilder(builder);
    }

    /**
     * Gets the stack trace of the Throwable that caused this crash report, or if that fails, the cause .toString().
     */
    public String getCauseStackTraceOrString()
    {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Throwable throwable = cause;

        if (throwable.getMessage() == null)
        {
            if (throwable instanceof NullPointerException)
            {
                throwable = new NullPointerException(description);
            }
            else if (throwable instanceof StackOverflowError)
            {
                throwable = new StackOverflowError(description);
            }
            else if (throwable instanceof OutOfMemoryError)
            {
                throwable = new OutOfMemoryError(description);
            }

            throwable.setStackTrace(cause.getStackTrace());
        }

        String s = throwable.toString();

        try
        {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            throwable.printStackTrace(printwriter);
            s = stringwriter.toString();
        }
        finally
        {
            IOUtils.closeQuietly(stringwriter);
            IOUtils.closeQuietly(printwriter);
        }

        return s;
    }

    /**
     * Gets the complete report with headers, stack trace, and different sections as a string.
     */
    public String getCompleteReport()
    {
        if (!reported)
        {
            reported = true;
            CrashReporter.extendCrashReport(theReportCategory);
        }

        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        Reflector.call(Reflector.BlamingTransformer_onCrash, stringbuilder);
        Reflector.call(Reflector.CoreModManager_onCrash, stringbuilder);
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(description);
        stringbuilder.append("\n\n");
        stringbuilder.append(getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i)
        {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    /**
     * Gets the file this crash report is saved into.
     */
    public File getFile()
    {
        return crashReportFile;
    }

    /**
     * Saves this CrashReport to the given file and returns a value indicating whether we were successful at doing so.
     */
    public boolean saveToFile(File toFile)
    {
        if (crashReportFile != null)
        {
            return false;
        }
        else
        {
            if (toFile.getParentFile() != null)
            {
                toFile.getParentFile().mkdirs();
            }

            Writer writer = null;
            boolean flag;

            try
            {
                writer = new OutputStreamWriter(new FileOutputStream(toFile), StandardCharsets.UTF_8);
                writer.write(getCompleteReport());
                crashReportFile = toFile;
                boolean flag1 = true;
                boolean flag2 = flag1;
                return flag2;
            }
            catch (Throwable throwable1)
            {
                LOGGER.error("Could not save crash report to {}", toFile, throwable1);
                flag = false;
            }
            finally
            {
                IOUtils.closeQuietly(writer);
            }

            return flag;
        }
    }

    public CrashReportCategory getCategory()
    {
        return theReportCategory;
    }

    /**
     * Creates a CrashReportCategory
     */
    public CrashReportCategory makeCategory(String name)
    {
        return makeCategoryDepth(name, 1);
    }

    /**
     * Creates a CrashReportCategory for the given stack trace depth
     */
    public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength)
    {
        CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);

        if (firstCategoryInCrashReport)
        {
            int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
            StackTraceElement[] astacktraceelement = cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;
            int j = astacktraceelement.length - i;

            if (j < 0)
            {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
            }

            if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length)
            {
                stacktraceelement = astacktraceelement[j];

                if (astacktraceelement.length + 1 - i < astacktraceelement.length)
                {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
                }
            }

            firstCategoryInCrashReport = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);

            if (i > 0 && !crashReportSections.isEmpty())
            {
                CrashReportCategory crashreportcategory1 = crashReportSections.get(crashReportSections.size() - 1);
                crashreportcategory1.trimStackTraceEntriesFromBottom(i);
            }
            else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length)
            {
                stacktrace = new StackTraceElement[j];
                System.arraycopy(astacktraceelement, 0, stacktrace, 0, stacktrace.length);
            }
            else
            {
                firstCategoryInCrashReport = false;
            }
        }

        crashReportSections.add(crashreportcategory);
        return crashreportcategory;
    }

    /**
     * Gets a random witty comment for inclusion in this CrashReport
     */
    private static String getWittyComment()
    {
        String[] astring = {"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try
        {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable var2)
        {
            return "Witty comment unavailable :(";
        }
    }

    /**
     * Creates a crash report for the exception
     */
    public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn)
    {
        CrashReport crashreport;

        if (causeIn instanceof ReportedException)
        {
            crashreport = ((ReportedException)causeIn).getCrashReport();
        }
        else
        {
            crashreport = new CrashReport(descriptionIn, causeIn);
        }

        return crashreport;
    }
}
