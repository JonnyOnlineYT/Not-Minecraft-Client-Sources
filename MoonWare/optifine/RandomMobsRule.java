package optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Namespaced;
import net.minecraft.world.biome.Biome;

public class RandomMobsRule
{
    private Namespaced baseResLoc;
    private int[] skins;
    private Namespaced[] namespaceds;
    private int[] weights;
    private Biome[] biomes;
    private RangeListInt heights;
    public int[] sumWeights;
    public int sumAllWeights = 1;

    public RandomMobsRule(Namespaced p_i79_1_, int[] p_i79_2_, int[] p_i79_3_, Biome[] p_i79_4_, RangeListInt p_i79_5_)
    {
        baseResLoc = p_i79_1_;
        skins = p_i79_2_;
        weights = p_i79_3_;
        biomes = p_i79_4_;
        heights = p_i79_5_;
    }

    public boolean isValid(String p_isValid_1_)
    {
        namespaceds = new Namespaced[skins.length];
        Namespaced resourcelocation = RandomMobs.getMcpatcherLocation(baseResLoc);

        if (resourcelocation == null)
        {
            Config.warn("Invalid path: " + baseResLoc.getPath());
            return false;
        }
        else
        {
            for (int i = 0; i < namespaceds.length; ++i)
            {
                int j = skins[i];

                if (j <= 1)
                {
                    namespaceds[i] = baseResLoc;
                }
                else
                {
                    Namespaced resourcelocation1 = RandomMobs.getLocationIndexed(resourcelocation, j);

                    if (resourcelocation1 == null)
                    {
                        Config.warn("Invalid path: " + baseResLoc.getPath());
                        return false;
                    }

                    if (!Config.hasResource(resourcelocation1))
                    {
                        Config.warn("Texture not found: " + resourcelocation1.getPath());
                        return false;
                    }

                    namespaceds[i] = resourcelocation1;
                }
            }

            if (weights != null)
            {
                if (weights.length > namespaceds.length)
                {
                    Config.warn("More weights defined than skins, trimming weights: " + p_isValid_1_);
                    int[] aint = new int[namespaceds.length];
                    System.arraycopy(weights, 0, aint, 0, aint.length);
                    weights = aint;
                }

                if (weights.length < namespaceds.length)
                {
                    Config.warn("Less weights defined than skins, expanding weights: " + p_isValid_1_);
                    int[] aint1 = new int[namespaceds.length];
                    System.arraycopy(weights, 0, aint1, 0, weights.length);
                    int l = MathUtils.getAverage(weights);

                    for (int j1 = weights.length; j1 < aint1.length; ++j1)
                    {
                        aint1[j1] = l;
                    }

                    weights = aint1;
                }

                sumWeights = new int[weights.length];
                int k = 0;

                for (int i1 = 0; i1 < weights.length; ++i1)
                {
                    if (weights[i1] < 0)
                    {
                        Config.warn("Invalid weight: " + weights[i1]);
                        return false;
                    }

                    k += weights[i1];
                    sumWeights[i1] = k;
                }

                sumAllWeights = k;

                if (sumAllWeights <= 0)
                {
                    Config.warn("Invalid sum of all weights: " + k);
                    sumAllWeights = 1;
                }
            }

            return true;
        }
    }

    public boolean matches(EntityLiving p_matches_1_)
    {
        if (!Matches.biome(p_matches_1_.spawnBiome, biomes))
        {
            return false;
        }
        else
        {
            return heights == null || p_matches_1_.spawnPosition == null || heights.isInRange(p_matches_1_.spawnPosition.getY());
        }
    }

    public Namespaced getTextureLocation(Namespaced p_getTextureLocation_1_, int p_getTextureLocation_2_)
    {
        int i = 0;

        if (weights == null)
        {
            i = p_getTextureLocation_2_ % namespaceds.length;
        }
        else
        {
            int j = p_getTextureLocation_2_ % sumAllWeights;

            for (int k = 0; k < sumWeights.length; ++k)
            {
                if (sumWeights[k] > j)
                {
                    i = k;
                    break;
                }
            }
        }

        return namespaceds[i];
    }
}
