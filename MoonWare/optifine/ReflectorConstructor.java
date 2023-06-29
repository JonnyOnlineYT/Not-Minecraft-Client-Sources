package optifine;

import java.lang.reflect.Constructor;

public class ReflectorConstructor
{
    private ReflectorClass reflectorClass;
    private Class[] parameterTypes;
    private boolean checked;
    private Constructor targetConstructor;

    public ReflectorConstructor(ReflectorClass p_i84_1_, Class[] p_i84_2_)
    {
        reflectorClass = p_i84_1_;
        parameterTypes = p_i84_2_;
        Constructor constructor = getTargetConstructor();
    }

    public Constructor getTargetConstructor()
    {
        if (checked)
        {
            return targetConstructor;
        }
        else
        {
            checked = true;
            Class oclass = reflectorClass.getTargetClass();

            if (oclass == null)
            {
                return null;
            }
            else
            {
                try
                {
                    targetConstructor = findConstructor(oclass, parameterTypes);

                    if (targetConstructor == null)
                    {
                        Config.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + Config.arrayToString(parameterTypes));
                    }

                    if (targetConstructor != null)
                    {
                        targetConstructor.setAccessible(true);
                    }
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }

                return targetConstructor;
            }
        }
    }

    private static Constructor findConstructor(Class p_findConstructor_0_, Class[] p_findConstructor_1_)
    {
        Constructor[] aconstructor = p_findConstructor_0_.getDeclaredConstructors();

        for (int i = 0; i < aconstructor.length; ++i)
        {
            Constructor constructor = aconstructor[i];
            Class[] aclass = constructor.getParameterTypes();

            if (Reflector.matchesTypes(p_findConstructor_1_, aclass))
            {
                return constructor;
            }
        }

        return null;
    }

    public boolean exists()
    {
        if (checked)
        {
            return targetConstructor != null;
        }
        else
        {
            return getTargetConstructor() != null;
        }
    }

    public void deactivate()
    {
        checked = true;
        targetConstructor = null;
    }
}
