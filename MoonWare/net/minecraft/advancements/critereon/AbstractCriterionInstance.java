package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.Namespaced;

public class AbstractCriterionInstance implements ICriterionInstance
{
    private final Namespaced field_192245_a;

    public AbstractCriterionInstance(Namespaced p_i47465_1_)
    {
        field_192245_a = p_i47465_1_;
    }

    public Namespaced func_192244_a()
    {
        return field_192245_a;
    }

    public String toString()
    {
        return "AbstractCriterionInstance{criterion=" + field_192245_a + '}';
    }
}
