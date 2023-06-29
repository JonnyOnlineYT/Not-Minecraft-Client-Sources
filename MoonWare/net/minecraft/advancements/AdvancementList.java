package net.minecraft.advancements;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.Namespaced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementList
{
    private static final Logger field_192091_a = LogManager.getLogger();
    private final Map<Namespaced, Advancement> field_192092_b = Maps.newHashMap();
    private final Set<Advancement> field_192093_c = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192094_d = Sets.newLinkedHashSet();
    private AdvancementList.Listener field_192095_e;

    private void func_192090_a(Advancement p_192090_1_)
    {
        for (Advancement advancement : p_192090_1_.getChildren())
        {
            func_192090_a(advancement);
        }

        field_192091_a.info("Forgot about advancement " + p_192090_1_.getId());
        field_192092_b.remove(p_192090_1_.getId());

        if (p_192090_1_.getParent() == null)
        {
            field_192093_c.remove(p_192090_1_);

            if (field_192095_e != null)
            {
                field_192095_e.func_191928_b(p_192090_1_);
            }
        }
        else
        {
            field_192094_d.remove(p_192090_1_);

            if (field_192095_e != null)
            {
                field_192095_e.func_191929_d(p_192090_1_);
            }
        }
    }

    public void func_192085_a(Set<Namespaced> p_192085_1_)
    {
        for (Namespaced resourcelocation : p_192085_1_)
        {
            Advancement advancement = field_192092_b.get(resourcelocation);

            if (advancement == null)
            {
                field_192091_a.warn("Told to remove advancement " + resourcelocation + " but I don't know what that is");
            }
            else
            {
                func_192090_a(advancement);
            }
        }
    }

    public void func_192083_a(Map<Namespaced, Advancement.Builder> p_192083_1_)
    {
        Function<Namespaced, Advancement> function = Functions.forMap(field_192092_b, null);
        label42:

        while (!p_192083_1_.isEmpty())
        {
            boolean flag = false;
            Iterator<Map.Entry<Namespaced, Advancement.Builder>> iterator = p_192083_1_.entrySet().iterator();

            while (iterator.hasNext())
            {
                Map.Entry<Namespaced, Advancement.Builder> entry = iterator.next();
                Namespaced resourcelocation = entry.getKey();
                Advancement.Builder advancement$builder = entry.getValue();

                if (advancement$builder.func_192058_a(function))
                {
                    Advancement advancement = advancement$builder.func_192056_a(resourcelocation);
                    field_192092_b.put(resourcelocation, advancement);
                    flag = true;
                    iterator.remove();

                    if (advancement.getParent() == null)
                    {
                        field_192093_c.add(advancement);

                        if (field_192095_e != null)
                        {
                            field_192095_e.func_191931_a(advancement);
                        }
                    }
                    else
                    {
                        field_192094_d.add(advancement);

                        if (field_192095_e != null)
                        {
                            field_192095_e.func_191932_c(advancement);
                        }
                    }
                }
            }

            if (!flag)
            {
                iterator = p_192083_1_.entrySet().iterator();

                while (true)
                {
                    if (!iterator.hasNext())
                    {
                        break label42;
                    }

                    Map.Entry<Namespaced, Advancement.Builder> entry1 = iterator.next();
                    field_192091_a.error("Couldn't load advancement " + entry1.getKey() + ": " + entry1.getValue());
                }
            }
        }

        field_192091_a.info("Loaded " + field_192092_b.size() + " advancements");
    }

    public void func_192087_a()
    {
        field_192092_b.clear();
        field_192093_c.clear();
        field_192094_d.clear();

        if (field_192095_e != null)
        {
            field_192095_e.func_191930_a();
        }
    }

    public Iterable<Advancement> func_192088_b()
    {
        return field_192093_c;
    }

    public Iterable<Advancement> func_192089_c()
    {
        return field_192092_b.values();
    }

    @Nullable
    public Advancement func_192084_a(Namespaced p_192084_1_)
    {
        return field_192092_b.get(p_192084_1_);
    }

    public void func_192086_a(@Nullable AdvancementList.Listener p_192086_1_)
    {
        field_192095_e = p_192086_1_;

        if (p_192086_1_ != null)
        {
            for (Advancement advancement : field_192093_c)
            {
                p_192086_1_.func_191931_a(advancement);
            }

            for (Advancement advancement1 : field_192094_d)
            {
                p_192086_1_.func_191932_c(advancement1);
            }
        }
    }

    public interface Listener
    {
        void func_191931_a(Advancement p_191931_1_);

        void func_191928_b(Advancement p_191928_1_);

        void func_191932_c(Advancement p_191932_1_);

        void func_191929_d(Advancement p_191929_1_);

        void func_191930_a();
    }
}
