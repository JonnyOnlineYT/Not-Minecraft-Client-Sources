package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> sectionList = Lists.newArrayList();
    private final List<Long> timestampList = Lists.newArrayList();

    /** Flag profiling enabled */
    public boolean profilingEnabled;

    /** Current profiling section */
    private String profilingSection = "";
    private final Map<String, Long> profilingMap = Maps.newHashMap();
    public boolean profilerGlobalEnabled = true;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final String RENDER = "render";
    private static final String DISPLAY = "display";
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    private static final int HASH_TICK = "tick".hashCode();
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    private static final int HASH_RENDER = "render".hashCode();
    private static final int HASH_DISPLAY = "display".hashCode();

    public Profiler()
    {
        profilerLocalEnabled = profilerGlobalEnabled;
    }

    /**
     * Clear profiling.
     */
    public void clearProfiling()
    {
        profilingMap.clear();
        profilingSection = "";
        sectionList.clear();
        profilerLocalEnabled = profilerGlobalEnabled;
    }

    /**
     * Start section
     */
    public void startSection(String name)
    {
        if (Lagometer.isActive())
        {
            int i = name.hashCode();

            if (i == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables"))
            {
                Lagometer.timerScheduledExecutables.start();
            }
            else if (i == HASH_TICK && name.equals("tick") && Config.isMinecraftThread())
            {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            }
            else if (i == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors"))
            {
                Lagometer.timerTick.end();
            }
        }

        if (Config.isFastRender())
        {
            int j = name.hashCode();

            if (j == HASH_RENDER && name.equals("render"))
            {
                GlStateManager.clearEnabled = false;
            }
            else if (j == HASH_DISPLAY && name.equals("display"))
            {
                GlStateManager.clearEnabled = true;
            }
        }

        if (profilerLocalEnabled)
        {
            if (profilingEnabled)
            {
                if (!profilingSection.isEmpty())
                {
                    profilingSection = profilingSection + ".";
                }

                profilingSection = profilingSection + name;
                sectionList.add(profilingSection);
                timestampList.add(Long.valueOf(System.nanoTime()));
            }
        }
    }

    public void func_194340_a(Supplier<String> p_194340_1_)
    {
        if (profilerLocalEnabled)
        {
            if (profilingEnabled)
            {
                startSection(p_194340_1_.get());
            }
        }
    }

    /**
     * End section
     */
    public void endSection()
    {
        if (profilerLocalEnabled)
        {
            if (profilingEnabled)
            {
                long i = System.nanoTime();
                long j = timestampList.remove(timestampList.size() - 1).longValue();
                sectionList.remove(sectionList.size() - 1);
                long k = i - j;

                if (profilingMap.containsKey(profilingSection))
                {
                    profilingMap.put(profilingSection, Long.valueOf(profilingMap.get(profilingSection).longValue() + k));
                }
                else
                {
                    profilingMap.put(profilingSection, Long.valueOf(k));
                }

                if (k > 100000000L)
                {
                    LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", profilingSection, Double.valueOf((double)k / 1000000.0D));
                }

                profilingSection = sectionList.isEmpty() ? "" : sectionList.get(sectionList.size() - 1);
            }
        }
    }

    public List<Profiler.Result> getProfilingData(String profilerName)
    {
        if (!profilingEnabled)
        {
            return Collections.emptyList();
        }
        else
        {
            long i = profilingMap.containsKey("root") ? profilingMap.get("root").longValue() : 0L;
            long j = profilingMap.containsKey(profilerName) ? profilingMap.get(profilerName).longValue() : -1L;
            List<Profiler.Result> list = Lists.newArrayList();

            if (!profilerName.isEmpty())
            {
                profilerName = profilerName + ".";
            }

            long k = 0L;

            for (String s : profilingMap.keySet())
            {
                if (s.length() > profilerName.length() && s.startsWith(profilerName) && s.indexOf(".", profilerName.length() + 1) < 0)
                {
                    k += profilingMap.get(s).longValue();
                }
            }

            float f = (float)k;

            if (k < j)
            {
                k = j;
            }

            if (i < k)
            {
                i = k;
            }

            for (String s1 : profilingMap.keySet())
            {
                if (s1.length() > profilerName.length() && s1.startsWith(profilerName) && s1.indexOf(".", profilerName.length() + 1) < 0)
                {
                    long l = profilingMap.get(s1).longValue();
                    double d0 = (double)l * 100.0D / (double)k;
                    double d1 = (double)l * 100.0D / (double)i;
                    String s2 = s1.substring(profilerName.length());
                    list.add(new Profiler.Result(s2, d0, d1));
                }
            }

            for (String s3 : profilingMap.keySet())
            {
                profilingMap.put(s3, Long.valueOf(profilingMap.get(s3).longValue() * 950L / 1000L));
            }

            if ((float)k > f)
            {
                list.add(new Profiler.Result("unspecified", (double)((float)k - f) * 100.0D / (double)k, (double)((float)k - f) * 100.0D / (double)i));
            }

            Collections.sort(list);
            list.add(0, new Profiler.Result(profilerName, 100.0D, (double)k * 100.0D / (double)i));
            return list;
        }
    }

    /**
     * End current section and start a new section
     */
    public void endStartSection(String name)
    {
        if (profilerLocalEnabled)
        {
            endSection();
            startSection(name);
        }
    }

    public void func_194339_b(Supplier<String> p_194339_1_)
    {
        if (profilerLocalEnabled)
        {
            endSection();
            func_194340_a(p_194339_1_);
        }
    }

    public String getNameOfLastSection()
    {
        return sectionList.isEmpty() ? "[UNKNOWN]" : sectionList.get(sectionList.size() - 1);
    }

    public void startSection(Class<?> p_startSection_1_)
    {
        if (profilingEnabled)
        {
            startSection(p_startSection_1_.getSimpleName());
        }
    }

    public static final class Result implements Comparable<Profiler.Result>
    {
        public double usePercentage;
        public double totalUsePercentage;
        public String profilerName;

        public Result(String profilerName, double usePercentage, double totalUsePercentage)
        {
            this.profilerName = profilerName;
            this.usePercentage = usePercentage;
            this.totalUsePercentage = totalUsePercentage;
        }

        public int compareTo(Profiler.Result p_compareTo_1_)
        {
            if (p_compareTo_1_.usePercentage < usePercentage)
            {
                return -1;
            }
            else
            {
                return p_compareTo_1_.usePercentage > usePercentage ? 1 : p_compareTo_1_.profilerName.compareTo(profilerName);
            }
        }

        public int getColor()
        {
            return (profilerName.hashCode() & 11184810) + 4473924;
        }
    }
}
