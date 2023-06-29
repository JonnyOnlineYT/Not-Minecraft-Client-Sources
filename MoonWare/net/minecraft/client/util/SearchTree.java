package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.util.Namespaced;

public class SearchTree<T> implements ISearchTree<T>
{
    protected SuffixArray<T> field_194044_a = new SuffixArray<T>();
    protected SuffixArray<T> field_194045_b = new SuffixArray<T>();
    private final Function<T, Iterable<String>> field_194046_c;
    private final Function<T, Iterable<Namespaced>> field_194047_d;
    private final List<T> field_194048_e = Lists.newArrayList();
    private Object2IntMap<T> field_194049_f = new Object2IntOpenHashMap<T>();

    public SearchTree(Function<T, Iterable<String>> p_i47612_1_, Function<T, Iterable<Namespaced>> p_i47612_2_)
    {
        field_194046_c = p_i47612_1_;
        field_194047_d = p_i47612_2_;
    }

    public void func_194040_a()
    {
        field_194044_a = new SuffixArray<T>();
        field_194045_b = new SuffixArray<T>();

        for (T t : field_194048_e)
        {
            func_194042_b(t);
        }

        field_194044_a.func_194058_a();
        field_194045_b.func_194058_a();
    }

    public void func_194043_a(T p_194043_1_)
    {
        field_194049_f.put(p_194043_1_, field_194048_e.size());
        field_194048_e.add(p_194043_1_);
        func_194042_b(p_194043_1_);
    }

    private void func_194042_b(T p_194042_1_)
    {
        (field_194047_d.apply(p_194042_1_)).forEach((p_194039_2_) ->
        {
            field_194045_b.func_194057_a(p_194042_1_, p_194039_2_.toString().toLowerCase(Locale.ROOT));
        });
        (field_194046_c.apply(p_194042_1_)).forEach((p_194041_2_) ->
        {
            field_194044_a.func_194057_a(p_194042_1_, p_194041_2_.toLowerCase(Locale.ROOT));
        });
    }

    public List<T> func_194038_a(String p_194038_1_)
    {
        List<T> list = field_194044_a.func_194055_a(p_194038_1_);

        if (p_194038_1_.indexOf(58) < 0)
        {
            return list;
        }
        else
        {
            List<T> list1 = field_194045_b.func_194055_a(p_194038_1_);
            return (List<T>)(list1.isEmpty() ? list : Lists.newArrayList(new SearchTree.MergingIterator(list.iterator(), list1.iterator(), field_194049_f)));
        }
    }

    static class MergingIterator<T> extends AbstractIterator<T>
    {
        private final Iterator<T> field_194033_a;
        private final Iterator<T> field_194034_b;
        private final Object2IntMap<T> field_194035_c;
        private T field_194036_d;
        private T field_194037_e;

        public MergingIterator(Iterator<T> p_i47606_1_, Iterator<T> p_i47606_2_, Object2IntMap<T> p_i47606_3_)
        {
            field_194033_a = p_i47606_1_;
            field_194034_b = p_i47606_2_;
            field_194035_c = p_i47606_3_;
            field_194036_d = p_i47606_1_.hasNext() ? p_i47606_1_.next() : null;
            field_194037_e = p_i47606_2_.hasNext() ? p_i47606_2_.next() : null;
        }

        protected T computeNext()
        {
            if (field_194036_d == null && field_194037_e == null)
            {
                return endOfData();
            }
            else
            {
                int i;

                if (field_194036_d == field_194037_e)
                {
                    i = 0;
                }
                else if (field_194036_d == null)
                {
                    i = 1;
                }
                else if (field_194037_e == null)
                {
                    i = -1;
                }
                else
                {
                    i = Integer.compare(field_194035_c.getInt(field_194036_d), field_194035_c.getInt(field_194037_e));
                }

                T t = i <= 0 ? field_194036_d : field_194037_e;

                if (i <= 0)
                {
                    field_194036_d = field_194033_a.hasNext() ? field_194033_a.next() : null;
                }

                if (i >= 0)
                {
                    field_194037_e = field_194034_b.hasNext() ? field_194034_b.next() : null;
                }

                return t;
            }
        }
    }
}
