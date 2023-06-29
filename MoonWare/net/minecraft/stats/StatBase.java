package net.minecraft.stats;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteriaStat;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBase
{
    /** The Stat ID */
    public final String statId;

    /** The Stat name */
    private final Component statName;
    public boolean isIndependent;
    private final IStatType formatter;
    private final IScoreCriteria objectiveCriteria;
    private Class <? extends IJsonSerializable > serializableClazz;
    private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
    public static IStatType simpleStatType = new IStatType()
    {
        public String format(int number)
        {
            return numberFormat.format(number);
        }
    };
    private static final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
    public static IStatType timeStatType = new IStatType()
    {
        public String format(int number)
        {
            double d0 = (double)number / 20.0D;
            double d1 = d0 / 60.0D;
            double d2 = d1 / 60.0D;
            double d3 = d2 / 24.0D;
            double d4 = d3 / 365.0D;

            if (d4 > 0.5D)
            {
                return decimalFormat.format(d4) + " y";
            }
            else if (d3 > 0.5D)
            {
                return decimalFormat.format(d3) + " d";
            }
            else if (d2 > 0.5D)
            {
                return decimalFormat.format(d2) + " h";
            }
            else
            {
                return d1 > 0.5D ? decimalFormat.format(d1) + " m" : d0 + " s";
            }
        }
    };
    public static IStatType distanceStatType = new IStatType()
    {
        public String format(int number)
        {
            double d0 = (double)number / 100.0D;
            double d1 = d0 / 1000.0D;

            if (d1 > 0.5D)
            {
                return decimalFormat.format(d1) + " km";
            }
            else
            {
                return d0 > 0.5D ? decimalFormat.format(d0) + " m" : number + " cm";
            }
        }
    };
    public static IStatType divideByTen = new IStatType()
    {
        public String format(int number)
        {
            return decimalFormat.format((double)number * 0.1D);
        }
    };

    public StatBase(String statIdIn, Component statNameIn, IStatType formatterIn)
    {
        statId = statIdIn;
        statName = statNameIn;
        formatter = formatterIn;
        objectiveCriteria = new ScoreCriteriaStat(this);
        IScoreCriteria.INSTANCES.put(objectiveCriteria.getName(), objectiveCriteria);
    }

    public StatBase(String statIdIn, Component statNameIn)
    {
        this(statIdIn, statNameIn, simpleStatType);
    }

    /**
     * Initializes the current stat as independent (i.e., lacking prerequisites for being updated) and returns the
     * current instance.
     */
    public StatBase initIndependentStat()
    {
        isIndependent = true;
        return this;
    }

    /**
     * Register the stat into StatList.
     */
    public StatBase registerStat()
    {
        if (StatList.ID_TO_STAT_MAP.containsKey(statId))
        {
            throw new RuntimeException("Duplicate stat id: \"" + (StatList.ID_TO_STAT_MAP.get(statId)).statName + "\" and \"" + statName + "\" at id " + statId);
        }
        else
        {
            StatList.ALL_STATS.add(this);
            StatList.ID_TO_STAT_MAP.put(statId, this);
            return this;
        }
    }

    public String format(int number)
    {
        return formatter.format(number);
    }

    public Component getStatName()
    {
        Component itextcomponent = statName.copy();
        itextcomponent.getStyle().setColor(Formatting.GRAY);
        return itextcomponent;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && getClass() == p_equals_1_.getClass())
        {
            StatBase statbase = (StatBase)p_equals_1_;
            return statId.equals(statbase.statId);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return statId.hashCode();
    }

    public String toString()
    {
        return "Stat{id=" + statId + ", nameId=" + statName + ", awardLocallyOnly=" + isIndependent + ", formatter=" + formatter + ", objectiveCriteria=" + objectiveCriteria + '}';
    }

    /**
     * 1.8.9
     */
    public IScoreCriteria getCriteria()
    {
        return objectiveCriteria;
    }

    public Class <? extends IJsonSerializable > getSerializableClazz()
    {
        return serializableClazz;
    }
}
