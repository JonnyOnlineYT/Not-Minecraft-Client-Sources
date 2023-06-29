package net.minecraft.util.math;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;

public class Cartesian
{
    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable <? extends Iterable <? extends T >> sets)
    {
        return new Cartesian.Product(clazz, toArray(Iterable.class, sets));
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable <? extends Iterable <? extends T >> sets)
    {
        return arraysAsLists(cartesianProduct(Object.class, sets));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays)
    {
        return Iterables.transform(arrays, new Cartesian.GetList());
    }

    private static <T> T[] toArray(Class <? super T > clazz, Iterable <? extends T > it)
    {
        List<T> list = Lists.newArrayList();

        for (T t : it)
        {
            list.add(t);
        }

        return list.toArray(createArray(clazz, list.size()));
    }

    private static <T> T[] createArray(Class <? super T > elementType, int length)
    {
        return (T[]) Array.newInstance(elementType, length);
    }

    static class GetList<T> implements Function<Object[], List<T>>
    {
        private GetList()
        {
        }

        public List<T> apply(@Nullable Object[] p_apply_1_)
        {
            return Arrays.asList((T[])p_apply_1_);
        }
    }

    static class Product<T> implements Iterable<T[]>
    {
        private final Class<T> clazz;
        private final Iterable <? extends T > [] iterables;

        private Product(Class<T> clazz, Iterable <? extends T > [] iterables)
        {
            this.clazz = clazz;
            this.iterables = iterables;
        }

        public Iterator<T[]> iterator()
        {
            return (Iterator<T[]>)(iterables.length <= 0 ? Collections.singletonList(createArray(clazz, 0)).iterator() : new Cartesian.Product.ProductIterator(clazz, iterables));
        }

        static class ProductIterator<T> extends UnmodifiableIterator<T[]>
        {
            private int index;
            private final Iterable <? extends T > [] iterables;
            private final Iterator <? extends T > [] iterators;
            private final T[] results;

            private ProductIterator(Class<T> clazz, Iterable <? extends T > [] iterables)
            {
                index = -2;
                this.iterables = iterables;
                iterators = createArray(Iterator.class, this.iterables.length);

                for (int i = 0; i < this.iterables.length; ++i)
                {
                    iterators[i] = iterables[i].iterator();
                }

                results = createArray(clazz, iterators.length);
            }

            private void endOfData()
            {
                index = -1;
                Arrays.fill(iterators, null);
                Arrays.fill(results, null);
            }

            public boolean hasNext()
            {
                if (index == -2)
                {
                    index = 0;

                    for (Iterator <? extends T > iterator1 : iterators)
                    {
                        if (!iterator1.hasNext())
                        {
                            endOfData();
                            break;
                        }
                    }

                    return true;
                }
                else
                {
                    if (index >= iterators.length)
                    {
                        for (index = iterators.length - 1; index >= 0; --index)
                        {
                            Iterator <? extends T > iterator = iterators[index];

                            if (iterator.hasNext())
                            {
                                break;
                            }

                            if (index == 0)
                            {
                                endOfData();
                                break;
                            }

                            iterator = iterables[index].iterator();
                            iterators[index] = iterator;

                            if (!iterator.hasNext())
                            {
                                endOfData();
                                break;
                            }
                        }
                    }

                    return index >= 0;
                }
            }

            public T[] next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                else
                {
                    while (index < iterators.length)
                    {
                        results[index] = iterators[index].next();
                        ++index;
                    }

                    return results.clone();
                }
            }
        }
    }
}
