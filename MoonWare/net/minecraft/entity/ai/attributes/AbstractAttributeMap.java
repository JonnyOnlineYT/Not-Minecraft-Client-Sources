package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.LowerStringMap;

public abstract class AbstractAttributeMap
{
    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap();
    protected final Multimap<IAttribute, IAttribute> descendantsByParent = HashMultimap.create();

    public IAttributeInstance getAttributeInstance(IAttribute attribute)
    {
        return attributes.get(attribute);
    }

    @Nullable
    public IAttributeInstance getAttributeInstanceByName(String attributeName)
    {
        return attributesByName.get(attributeName);
    }

    /**
     * Registers an attribute with this AttributeMap, returns a modifiable AttributeInstance associated with this map
     */
    public IAttributeInstance registerAttribute(IAttribute attribute)
    {
        if (attributesByName.containsKey(attribute.getAttributeUnlocalizedName()))
        {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        else
        {
            IAttributeInstance iattributeinstance = createInstance(attribute);
            attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
            attributes.put(attribute, iattributeinstance);

            for (IAttribute iattribute = attribute.getParent(); iattribute != null; iattribute = iattribute.getParent())
            {
                descendantsByParent.put(iattribute, attribute);
            }

            return iattributeinstance;
        }
    }

    protected abstract IAttributeInstance createInstance(IAttribute attribute);

    public Collection<IAttributeInstance> getAllAttributes()
    {
        return attributesByName.values();
    }

    public void onAttributeModified(IAttributeInstance instance)
    {
    }

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> modifiers)
    {
        for (Map.Entry<String, AttributeModifier> entry : modifiers.entries())
        {
            IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());

            if (iattributeinstance != null)
            {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers)
    {
        for (Map.Entry<String, AttributeModifier> entry : modifiers.entries())
        {
            IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());

            if (iattributeinstance != null)
            {
                iattributeinstance.removeModifier(entry.getValue());
                iattributeinstance.applyModifier(entry.getValue());
            }
        }
    }
}
