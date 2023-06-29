package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;

public class MinMaxBounds
{
    public static final MinMaxBounds field_192516_a = new MinMaxBounds(null, null);
    private final Float field_192517_b;
    private final Float field_192518_c;

    public MinMaxBounds(@Nullable Float p_i47431_1_, @Nullable Float p_i47431_2_)
    {
        field_192517_b = p_i47431_1_;
        field_192518_c = p_i47431_2_;
    }

    public boolean func_192514_a(float p_192514_1_)
    {
        if (field_192517_b != null && field_192517_b.floatValue() > p_192514_1_)
        {
            return false;
        }
        else
        {
            return field_192518_c == null || field_192518_c.floatValue() >= p_192514_1_;
        }
    }

    public boolean func_192513_a(double p_192513_1_)
    {
        if (field_192517_b != null && (double)(field_192517_b.floatValue() * field_192517_b.floatValue()) > p_192513_1_)
        {
            return false;
        }
        else
        {
            return field_192518_c == null || (double)(field_192518_c.floatValue() * field_192518_c.floatValue()) >= p_192513_1_;
        }
    }

    public static MinMaxBounds func_192515_a(@Nullable JsonElement p_192515_0_)
    {
        if (p_192515_0_ != null && !p_192515_0_.isJsonNull())
        {
            if (JsonUtils.isNumber(p_192515_0_))
            {
                float f2 = JsonUtils.getFloat(p_192515_0_, "value");
                return new MinMaxBounds(f2, f2);
            }
            else
            {
                JsonObject jsonobject = JsonUtils.getJsonObject(p_192515_0_, "value");
                Float f = jsonobject.has("min") ? JsonUtils.getFloat(jsonobject, "min") : null;
                Float f1 = jsonobject.has("max") ? JsonUtils.getFloat(jsonobject, "max") : null;
                return new MinMaxBounds(f, f1);
            }
        }
        else
        {
            return field_192516_a;
        }
    }
}
