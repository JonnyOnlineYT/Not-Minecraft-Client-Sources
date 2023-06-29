package net.optifine.entity.model.anim;

import optifine.ReflectorField;
import optifine.ReflectorFields;

import java.lang.reflect.Field;

public class RefUtils {

    public static Object getFieldValue(ReflectorField p_getFieldValue_0_)
    {
        return getFieldValue((Object)null, p_getFieldValue_0_);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorField p_getFieldValue_1_)
    {
        try
        {
            Field field = p_getFieldValue_1_.getTargetField();

            if (field == null)
            {
                return null;
            }
            else
            {
                Object object = field.get(p_getFieldValue_0_);
                return object;
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(ReflectorFields p_getFieldValue_0_, int p_getFieldValue_1_)
    {
        ReflectorField reflectorfield = p_getFieldValue_0_.getReflectorField(p_getFieldValue_1_);
        return reflectorfield == null ? null : getFieldValue(reflectorfield);
    }

    public static Object getFieldValue(Object p_getFieldValue_0_, ReflectorFields p_getFieldValue_1_, int p_getFieldValue_2_)
    {
        ReflectorField reflectorfield = p_getFieldValue_1_.getReflectorField(p_getFieldValue_2_);
        return reflectorfield == null ? null : getFieldValue(p_getFieldValue_0_, reflectorfield);
    }

}
