package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class ModifiableAttributeInstance implements IAttributeInstance
{
    /** The BaseAttributeMap this attributeInstance can be found in */
    private final AbstractAttributeMap attributeMap;

    /** The Attribute this is an instance of */
    private final IAttribute genericAttribute;
    private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
    private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
    private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;

    public ModifiableAttributeInstance(AbstractAttributeMap attributeMapIn, IAttribute genericAttributeIn)
    {
        attributeMap = attributeMapIn;
        genericAttribute = genericAttributeIn;
        baseValue = genericAttributeIn.getDefaultValue();

        for (int i = 0; i < 3; ++i)
        {
            mapByOperation.put(Integer.valueOf(i), Sets.newHashSet());
        }
    }

    /**
     * Get the Attribute this is an instance of
     */
    public IAttribute getAttribute()
    {
        return genericAttribute;
    }

    public double getBaseValue()
    {
        return baseValue;
    }

    public void setBaseValue(double baseValue)
    {
        if (baseValue != getBaseValue())
        {
            this.baseValue = baseValue;
            flagForUpdate();
        }
    }

    public Collection<AttributeModifier> getModifiersByOperation(int operation)
    {
        return mapByOperation.get(Integer.valueOf(operation));
    }

    public Collection<AttributeModifier> getModifiers()
    {
        Set<AttributeModifier> set = Sets.newHashSet();

        for (int i = 0; i < 3; ++i)
        {
            set.addAll(getModifiersByOperation(i));
        }

        return set;
    }

    @Nullable

    /**
     * Returns attribute modifier, if any, by the given UUID
     */
    public AttributeModifier getModifier(UUID uuid)
    {
        return mapByUUID.get(uuid);
    }

    public boolean hasModifier(AttributeModifier modifier)
    {
        return mapByUUID.get(modifier.getID()) != null;
    }

    public void applyModifier(AttributeModifier modifier)
    {
        if (getModifier(modifier.getID()) != null)
        {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        else
        {
            Set<AttributeModifier> set = mapByName.get(modifier.getName());

            if (set == null)
            {
                set = Sets.newHashSet();
                mapByName.put(modifier.getName(), set);
            }

            (mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
            set.add(modifier);
            mapByUUID.put(modifier.getID(), modifier);
            flagForUpdate();
        }
    }

    protected void flagForUpdate()
    {
        needsUpdate = true;
        attributeMap.onAttributeModified(this);
    }

    public void removeModifier(AttributeModifier modifier)
    {
        for (int i = 0; i < 3; ++i)
        {
            Set<AttributeModifier> set = mapByOperation.get(Integer.valueOf(i));
            set.remove(modifier);
        }

        Set<AttributeModifier> set1 = mapByName.get(modifier.getName());

        if (set1 != null)
        {
            set1.remove(modifier);

            if (set1.isEmpty())
            {
                mapByName.remove(modifier.getName());
            }
        }

        mapByUUID.remove(modifier.getID());
        flagForUpdate();
    }

    public void removeModifier(UUID p_188479_1_)
    {
        AttributeModifier attributemodifier = getModifier(p_188479_1_);

        if (attributemodifier != null)
        {
            removeModifier(attributemodifier);
        }
    }

    public void removeAllModifiers()
    {
        Collection<AttributeModifier> collection = getModifiers();

        if (collection != null)
        {
            for (AttributeModifier attributemodifier : Lists.newArrayList(collection))
            {
                removeModifier(attributemodifier);
            }
        }
    }

    public double getAttributeValue()
    {
        if (needsUpdate)
        {
            cachedValue = computeValue();
            needsUpdate = false;
        }

        return cachedValue;
    }

    private double computeValue()
    {
        double d0 = getBaseValue();

        for (AttributeModifier attributemodifier : getAppliedModifiers(0))
        {
            d0 += attributemodifier.getAmount();
        }

        double d1 = d0;

        for (AttributeModifier attributemodifier1 : getAppliedModifiers(1))
        {
            d1 += d0 * attributemodifier1.getAmount();
        }

        for (AttributeModifier attributemodifier2 : getAppliedModifiers(2))
        {
            d1 *= 1.0D + attributemodifier2.getAmount();
        }

        return genericAttribute.clampValue(d1);
    }

    private Collection<AttributeModifier> getAppliedModifiers(int operation)
    {
        Set<AttributeModifier> set = Sets.newHashSet(getModifiersByOperation(operation));

        for (IAttribute iattribute = genericAttribute.getParent(); iattribute != null; iattribute = iattribute.getParent())
        {
            IAttributeInstance iattributeinstance = attributeMap.getAttributeInstance(iattribute);

            if (iattributeinstance != null)
            {
                set.addAll(iattributeinstance.getModifiersByOperation(operation));
            }
        }

        return set;
    }
}
