package optifine;

public class VillagerProfession
{
    private int profession;
    private int[] careers;

    public VillagerProfession(int p_i100_1_)
    {
        this(p_i100_1_, null);
    }

    public VillagerProfession(int p_i101_1_, int p_i101_2_)
    {
        this(p_i101_1_, new int[] {p_i101_2_});
    }

    public VillagerProfession(int p_i102_1_, int[] p_i102_2_)
    {
        profession = p_i102_1_;
        careers = p_i102_2_;
    }

    public boolean matches(int p_matches_1_, int p_matches_2_)
    {
        if (profession != p_matches_1_)
        {
            return false;
        }
        else
        {
            return careers == null || Config.equalsOne(p_matches_2_, careers);
        }
    }

    private boolean hasCareer(int p_hasCareer_1_)
    {
        return careers != null && Config.equalsOne(p_hasCareer_1_, careers);
    }

    public boolean addCareer(int p_addCareer_1_)
    {
        if (careers == null)
        {
            careers = new int[] {p_addCareer_1_};
            return true;
        }
        else if (hasCareer(p_addCareer_1_))
        {
            return false;
        }
        else
        {
            careers = Config.addIntToArray(careers, p_addCareer_1_);
            return true;
        }
    }

    public int getProfession()
    {
        return profession;
    }

    public int[] getCareers()
    {
        return careers;
    }

    public String toString()
    {
        return careers == null ? "" + profession : "" + profession + ":" + Config.arrayToString(careers);
    }
}
