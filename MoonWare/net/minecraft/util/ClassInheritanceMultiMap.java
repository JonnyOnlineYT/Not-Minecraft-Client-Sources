package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T>
{
    private static final Set < Class<? >> ALL_KNOWN = Sets.newHashSet();
    private final Map < Class<?>, List<T >> map = Maps.newHashMap();
    private final Set < Class<? >> knownKeys = Sets.newIdentityHashSet();
    private final Class<T> baseClass;
    private final List<T> values = Lists.newArrayList();

    public ClassInheritanceMultiMap(Class<T> baseClassIn)
    {
        baseClass = baseClassIn;
        knownKeys.add(baseClassIn);
        map.put(baseClassIn, values);

        for (Class<?> oclass : ALL_KNOWN)
        {
            createLookup(oclass);
        }
    }

    protected void createLookup(Class<?> clazz)
    {
        ALL_KNOWN.add(clazz);

        for (T t : values)
        {
            if (clazz.isAssignableFrom(t.getClass()))
            {
                addForClass(t, clazz);
            }
        }

        knownKeys.add(clazz);
    }

    protected Class<?> initializeClassLookup(Class<?> clazz)
    {
        if (baseClass.isAssignableFrom(clazz))
        {
            if (!knownKeys.contains(clazz))
            {
                createLookup(clazz);
            }

            return clazz;
        }
        else
        {
            throw new IllegalArgumentException("Don't know how to search for " + clazz);
        }
    }

    public boolean add(T p_add_1_)
    {
        for (Class<?> oclass : knownKeys)
        {
            if (oclass.isAssignableFrom(p_add_1_.getClass()))
            {
                addForClass(p_add_1_, oclass);
            }
        }

        return true;
    }

    private void addForClass(T value, Class<?> parentClass)
    {
        List<T> list = map.get(parentClass);

        if (list == null)
        {
            map.put(parentClass, Lists.newArrayList(value));
        }
        else
        {
            list.add(value);
        }
    }

    public boolean remove(Object p_remove_1_)
    {
        T t = (T)p_remove_1_;
        boolean flag = false;

        for (Class<?> oclass : knownKeys)
        {
            if (oclass.isAssignableFrom(t.getClass()))
            {
                List<T> list = map.get(oclass);

                if (list != null && list.remove(t))
                {
                    flag = true;
                }
            }
        }

        return flag;
    }

    public boolean contains(Object p_contains_1_)
    {
        return Iterators.contains(getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
    }

    public <S> Iterable<S> getByClass(Class<S> clazz)
    {
        return new Iterable<S>()
        {
            public Iterator<S> iterator()
            {
                List<T> list = map.get(initializeClassLookup(clazz));

                if (list == null)
                {
                    return Collections.emptyIterator();
                }
                else
                {
                    Iterator<T> iterator = list.iterator();
                    return Iterators.filter(iterator, clazz);
                }
            }
        };
    }

    public Iterator<T> iterator()
    {
        return values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(values.iterator());
    }

    public int size()
    {
        return values.size();
    }
}
