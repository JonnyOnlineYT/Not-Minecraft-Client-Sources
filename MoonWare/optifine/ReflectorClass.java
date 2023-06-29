package optifine;

public class ReflectorClass
{
    private String targetClassName;
    private boolean checked;
    private Class targetClass;

    public ReflectorClass(String p_i81_1_)
    {
        this(p_i81_1_, false);
    }

    public ReflectorClass(String p_i82_1_, boolean p_i82_2_)
    {
        targetClassName = null;
        checked = false;
        targetClass = null;
        targetClassName = p_i82_1_;

        if (!p_i82_2_)
        {
            Class oclass = getTargetClass();
        }
    }

    public ReflectorClass(Class p_i83_1_)
    {
        targetClassName = null;
        checked = false;
        targetClass = null;
        targetClass = p_i83_1_;
        targetClassName = p_i83_1_.getName();
        checked = true;
    }

    public Class getTargetClass()
    {
        if (checked)
        {
            return targetClass;
        }
        else
        {
            checked = true;

            try
            {
                targetClass = Class.forName(targetClassName);
            }
            catch (ClassNotFoundException var2)
            {
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }

            return targetClass;
        }
    }

    public boolean exists()
    {
        return getTargetClass() != null;
    }

    public String getTargetClassName()
    {
        return targetClassName;
    }

    public boolean isInstance(Object p_isInstance_1_)
    {
        return getTargetClass() != null && getTargetClass().isInstance(p_isInstance_1_);
    }

    public ReflectorField makeField(String p_makeField_1_)
    {
        return new ReflectorField(this, p_makeField_1_);
    }

    public ReflectorMethod makeMethod(String p_makeMethod_1_)
    {
        return new ReflectorMethod(this, p_makeMethod_1_);
    }

    public ReflectorMethod makeMethod(String p_makeMethod_1_, Class[] p_makeMethod_2_)
    {
        return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_);
    }

    public ReflectorMethod makeMethod(String p_makeMethod_1_, Class[] p_makeMethod_2_, boolean p_makeMethod_3_)
    {
        return new ReflectorMethod(this, p_makeMethod_1_, p_makeMethod_2_, p_makeMethod_3_);
    }
}
