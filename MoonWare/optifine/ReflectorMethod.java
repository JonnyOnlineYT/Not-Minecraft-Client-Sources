package optifine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;

    public ReflectorMethod(ReflectorClass p_i93_1_, String p_i93_2_)
    {
        this(p_i93_1_, p_i93_2_, null, false);
    }

    public ReflectorMethod(ReflectorClass p_i94_1_, String p_i94_2_, Class[] p_i94_3_)
    {
        this(p_i94_1_, p_i94_2_, p_i94_3_, false);
    }

    public ReflectorMethod(ReflectorClass p_i95_1_, String p_i95_2_, Class[] p_i95_3_, boolean p_i95_4_)
    {
        reflectorClass = null;
        targetMethodName = null;
        targetMethodParameterTypes = null;
        checked = false;
        targetMethod = null;
        reflectorClass = p_i95_1_;
        targetMethodName = p_i95_2_;
        targetMethodParameterTypes = p_i95_3_;

        if (!p_i95_4_)
        {
            Method method = getTargetMethod();
        }
    }

    public Method getTargetMethod()
    {
        if (checked)
        {
            return targetMethod;
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
                    if (targetMethodParameterTypes == null)
                    {
                        Method[] amethod = getMethods(oclass, targetMethodName);

                        if (amethod.length <= 0)
                        {
                            return null;
                        }

                        if (amethod.length > 1)
                        {

                            for (int i = 0; i < amethod.length; ++i)
                            {
                                Method method = amethod[i];
                                Config.warn("(Reflector)  - " + method);
                            }

                            return null;
                        }

                        targetMethod = amethod[0];
                    }
                    else
                    {
                        targetMethod = getMethod(oclass, targetMethodName, targetMethodParameterTypes);
                    }

                    if (targetMethod == null)
                    {
                        return null;
                    }
                    else
                    {
                        targetMethod.setAccessible(true);
                        return targetMethod;
                    }
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                    return null;
                }
            }
        }
    }

    public boolean exists()
    {
        if (checked)
        {
            return targetMethod != null;
        }
        else
        {
            return getTargetMethod() != null;
        }
    }

    public Class getReturnType()
    {
        Method method = getTargetMethod();
        return method == null ? null : method.getReturnType();
    }

    public void deactivate()
    {
        checked = true;
        targetMethod = null;
    }

    public static Method getMethod(Class p_getMethod_0_, String p_getMethod_1_, Class[] p_getMethod_2_)
    {
        Method[] amethod = p_getMethod_0_.getDeclaredMethods();

        for (int i = 0; i < amethod.length; ++i)
        {
            Method method = amethod[i];

            if (method.getName().equals(p_getMethod_1_))
            {
                Class[] aclass = method.getParameterTypes();

                if (Reflector.matchesTypes(p_getMethod_2_, aclass))
                {
                    return method;
                }
            }
        }

        return null;
    }

    public static Method[] getMethods(Class p_getMethods_0_, String p_getMethods_1_)
    {
        List list = new ArrayList();
        Method[] amethod = p_getMethods_0_.getDeclaredMethods();

        for (int i = 0; i < amethod.length; ++i)
        {
            Method method = amethod[i];

            if (method.getName().equals(p_getMethods_1_))
            {
                list.add(method);
            }
        }

        Method[] amethod1 = (Method[])list.toArray(new Method[list.size()]);
        return amethod1;
    }
}
