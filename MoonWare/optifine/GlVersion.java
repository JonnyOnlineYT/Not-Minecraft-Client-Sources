package optifine;

public class GlVersion
{
    private int major;
    private int minor;
    private int release;
    private String suffix;

    public GlVersion(int p_i43_1_, int p_i43_2_)
    {
        this(p_i43_1_, p_i43_2_, 0);
    }

    public GlVersion(int p_i44_1_, int p_i44_2_, int p_i44_3_)
    {
        this(p_i44_1_, p_i44_2_, p_i44_3_, null);
    }

    public GlVersion(int p_i45_1_, int p_i45_2_, int p_i45_3_, String p_i45_4_)
    {
        major = p_i45_1_;
        minor = p_i45_2_;
        release = p_i45_3_;
        suffix = p_i45_4_;
    }

    public int getMajor()
    {
        return major;
    }

    public int getMinor()
    {
        return minor;
    }

    public int getRelease()
    {
        return release;
    }

    public int toInt()
    {
        if (minor > 9)
        {
            return major * 100 + minor;
        }
        else
        {
            return release > 9 ? major * 100 + minor * 10 + 9 : major * 100 + minor * 10 + release;
        }
    }

    public String toString()
    {
        return suffix == null ? "" + major + "." + minor + "." + release : "" + major + "." + minor + "." + release + suffix;
    }
}
