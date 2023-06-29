package optifine;

import java.lang.reflect.Array;
import java.util.ArrayDeque;

public class ArrayCache
{
    private Class elementClass;
    private int maxCacheSize;
    private ArrayDeque cache = new ArrayDeque();

    public ArrayCache(Class p_i10_1_, int p_i10_2_)
    {
        elementClass = p_i10_1_;
        maxCacheSize = p_i10_2_;
    }

    public synchronized Object allocate(int p_allocate_1_)
    {
        Object object = cache.pollLast();

        if (object == null || Array.getLength(object) < p_allocate_1_)
        {
            object = Array.newInstance(elementClass, p_allocate_1_);
        }

        return object;
    }

    public synchronized void free(Object p_free_1_)
    {
        if (p_free_1_ != null)
        {
            Class oclass = p_free_1_.getClass();

            if (oclass.getComponentType() != elementClass)
            {
                throw new IllegalArgumentException("Wrong component type");
            }
            else if (cache.size() < maxCacheSize)
            {
                cache.add(p_free_1_);
            }
        }
    }
}
