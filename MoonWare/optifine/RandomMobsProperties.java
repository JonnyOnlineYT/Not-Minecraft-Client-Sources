package optifine;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Namespaced;
import net.minecraft.world.biome.Biome;

public class RandomMobsProperties
{
    public String name;
    public String basePath;
    public Namespaced[] namespaceds;
    public RandomMobsRule[] rules;

    public RandomMobsProperties(String p_i77_1_, Namespaced[] p_i77_2_)
    {
        ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        name = connectedparser.parseName(p_i77_1_);
        basePath = connectedparser.parseBasePath(p_i77_1_);
        namespaceds = p_i77_2_;
    }

    public RandomMobsProperties(Properties p_i78_1_, String p_i78_2_, Namespaced p_i78_3_)
    {
        ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        name = connectedparser.parseName(p_i78_2_);
        basePath = connectedparser.parseBasePath(p_i78_2_);
        rules = parseRules(p_i78_1_, p_i78_3_, connectedparser);
    }

    public Namespaced getTextureLocation(Namespaced p_getTextureLocation_1_, EntityLiving p_getTextureLocation_2_)
    {
        if (rules != null)
        {
            for (int i = 0; i < rules.length; ++i)
            {
                RandomMobsRule randommobsrule = rules[i];

                if (randommobsrule.matches(p_getTextureLocation_2_))
                {
                    return randommobsrule.getTextureLocation(p_getTextureLocation_1_, p_getTextureLocation_2_.randomMobsId);
                }
            }
        }

        if (namespaceds != null)
        {
            int j = p_getTextureLocation_2_.randomMobsId;
            int k = j % namespaceds.length;
            return namespaceds[k];
        }
        else
        {
            return p_getTextureLocation_1_;
        }
    }

    private RandomMobsRule[] parseRules(Properties p_parseRules_1_, Namespaced p_parseRules_2_, ConnectedParser p_parseRules_3_)
    {
        List list = new ArrayList();
        int i = p_parseRules_1_.size();

        for (int j = 0; j < i; ++j)
        {
            int k = j + 1;
            String s = p_parseRules_1_.getProperty("skins." + k);

            if (s != null)
            {
                int[] aint = p_parseRules_3_.parseIntList(s);
                int[] aint1 = p_parseRules_3_.parseIntList(p_parseRules_1_.getProperty("weights." + k));
                Biome[] abiome = p_parseRules_3_.parseBiomes(p_parseRules_1_.getProperty("biomes." + k));
                RangeListInt rangelistint = p_parseRules_3_.parseRangeListInt(p_parseRules_1_.getProperty("heights." + k));

                if (rangelistint == null)
                {
                    rangelistint = parseMinMaxHeight(p_parseRules_1_, k);
                }

                RandomMobsRule randommobsrule = new RandomMobsRule(p_parseRules_2_, aint, aint1, abiome, rangelistint);
                list.add(randommobsrule);
            }
        }

        RandomMobsRule[] arandommobsrule = (RandomMobsRule[])list.toArray(new RandomMobsRule[list.size()]);
        return arandommobsrule;
    }

    private RangeListInt parseMinMaxHeight(Properties p_parseMinMaxHeight_1_, int p_parseMinMaxHeight_2_)
    {
        String s = p_parseMinMaxHeight_1_.getProperty("minHeight." + p_parseMinMaxHeight_2_);
        String s1 = p_parseMinMaxHeight_1_.getProperty("maxHeight." + p_parseMinMaxHeight_2_);

        if (s == null && s1 == null)
        {
            return null;
        }
        else
        {
            int i = 0;

            if (s != null)
            {
                i = Config.parseInt(s, -1);

                if (i < 0)
                {
                    Config.warn("Invalid minHeight: " + s);
                    return null;
                }
            }

            int j = 256;

            if (s1 != null)
            {
                j = Config.parseInt(s1, -1);

                if (j < 0)
                {
                    Config.warn("Invalid maxHeight: " + s1);
                    return null;
                }
            }

            if (j < 0)
            {
                Config.warn("Invalid minHeight, maxHeight: " + s + ", " + s1);
                return null;
            }
            else
            {
                RangeListInt rangelistint = new RangeListInt();
                rangelistint.addRange(new RangeInt(i, j));
                return rangelistint;
            }
        }
    }

    public boolean isValid(String p_isValid_1_)
    {
        if (namespaceds == null && rules == null)
        {
            Config.warn("No skins specified: " + p_isValid_1_);
            return false;
        }
        else
        {
            if (rules != null)
            {
                for (int i = 0; i < rules.length; ++i)
                {
                    RandomMobsRule randommobsrule = rules[i];

                    if (!randommobsrule.isValid(p_isValid_1_))
                    {
                        return false;
                    }
                }
            }

            if (namespaceds != null)
            {
                for (int j = 0; j < namespaceds.length; ++j)
                {
                    Namespaced resourcelocation = namespaceds[j];

                    if (!Config.hasResource(resourcelocation))
                    {
                        Config.warn("Texture not found: " + resourcelocation.getPath());
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
