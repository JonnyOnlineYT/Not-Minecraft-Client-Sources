package optifine;

import java.lang.reflect.Field;

public class FieldLocatorType implements IFieldLocator
{
    private ReflectorClass reflectorClass;
    private Class targetFieldType;
    private int targetFieldIndex;

    public FieldLocatorType(ReflectorClass p_i39_1_, Class p_i39_2_)
    {
        this(p_i39_1_, p_i39_2_, 0);
    }

    public FieldLocatorType(ReflectorClass p_i40_1_, Class p_i40_2_, int p_i40_3_)
    {
        reflectorClass = null;
        targetFieldType = null;
        reflectorClass = p_i40_1_;
        targetFieldType = p_i40_2_;
        targetFieldIndex = p_i40_3_;
    }

    public Field getField()
    {
        Class oclass = reflectorClass.getTargetClass();

        if (oclass == null)
        {
            return null;
        }
        else
        {
            try
            {
                Field[] afield = oclass.getDeclaredFields();
                int i = 0;

                for (int j = 0; j < afield.length; ++j)
                {
                    Field field = afield[j];

                    if (field.getType() == targetFieldType)
                    {
                        if (i == targetFieldIndex)
                        {
                            field.setAccessible(true);
                            return field;
                        }

                        ++i;
                    }
                }

                return null;
            }
            catch (SecurityException securityexception)
            {
                securityexception.printStackTrace();
                return null;
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
                return null;
            }
        }
    }
}
