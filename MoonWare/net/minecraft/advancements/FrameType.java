package net.minecraft.advancements;

import net.minecraft.util.text.Formatting;

public enum FrameType
{
    TASK("task", 0, Formatting.GREEN),
    CHALLENGE("challenge", 26, Formatting.DARK_PURPLE),
    GOAL("goal", 52, Formatting.GREEN);

    private final String field_192313_d;
    private final int field_192314_e;
    private final Formatting field_193230_f;

    FrameType(String p_i47585_3_, int p_i47585_4_, Formatting p_i47585_5_)
    {
        field_192313_d = p_i47585_3_;
        field_192314_e = p_i47585_4_;
        field_193230_f = p_i47585_5_;
    }

    public String func_192307_a()
    {
        return field_192313_d;
    }

    public int func_192309_b()
    {
        return field_192314_e;
    }

    public static FrameType func_192308_a(String p_192308_0_)
    {
        for (FrameType frametype : values())
        {
            if (frametype.field_192313_d.equals(p_192308_0_))
            {
                return frametype;
            }
        }

        throw new IllegalArgumentException("Unknown frame type '" + p_192308_0_ + "'");
    }

    public Formatting func_193229_c()
    {
        return field_193230_f;
    }
}
