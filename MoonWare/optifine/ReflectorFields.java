package optifine;

public class ReflectorFields
{
    private ReflectorClass reflectorClass;
    private Class fieldType;
    private int fieldCount;
    private ReflectorField[] reflectorFields;

    public ReflectorFields(ReflectorClass p_i92_1_, Class p_i92_2_, int p_i92_3_)
    {
        reflectorClass = p_i92_1_;
        fieldType = p_i92_2_;

        if (p_i92_1_.exists())
        {
            if (p_i92_2_ != null)
            {
                reflectorFields = new ReflectorField[p_i92_3_];

                for (int i = 0; i < reflectorFields.length; ++i)
                {
                    reflectorFields[i] = new ReflectorField(p_i92_1_, p_i92_2_, i);
                }
            }
        }
    }

    public ReflectorClass getReflectorClass()
    {
        return reflectorClass;
    }

    public Class getFieldType()
    {
        return fieldType;
    }

    public int getFieldCount()
    {
        return fieldCount;
    }

    public ReflectorField getReflectorField(int p_getReflectorField_1_)
    {
        return p_getReflectorField_1_ >= 0 && p_getReflectorField_1_ < reflectorFields.length ? reflectorFields[p_getReflectorField_1_] : null;
    }
}
