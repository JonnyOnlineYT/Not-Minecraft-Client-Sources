package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.LowerStringMap;

public class AttributeMap extends AbstractAttributeMap {
   private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
   protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap();

   public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute) {
      return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
   }

   public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName) {
      IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
      if (iattributeinstance == null) {
         iattributeinstance = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(attributeName);
      }

      return (ModifiableAttributeInstance)iattributeinstance;
   }

   public IAttributeInstance registerAttribute(IAttribute attribute) {
      IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
      if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
         this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
      }

      return iattributeinstance;
   }

   protected IAttributeInstance createInstance(IAttribute attribute) {
      return new ModifiableAttributeInstance(this, attribute);
   }

   public void onAttributeModified(IAttributeInstance instance) {
      if (instance.getAttribute().getShouldWatch()) {
         this.attributeInstanceSet.add(instance);
      }

      Iterator var2 = this.descendantsByParent.get(instance.getAttribute()).iterator();

      while(var2.hasNext()) {
         IAttribute iattribute = (IAttribute)var2.next();
         ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
         if (modifiableattributeinstance != null) {
            modifiableattributeinstance.flagForUpdate();
         }
      }

   }

   public Set<IAttributeInstance> getAttributeInstanceSet() {
      return this.attributeInstanceSet;
   }

   public Collection<IAttributeInstance> getWatchedAttributes() {
      Set<IAttributeInstance> set = Sets.newHashSet();
      Iterator var2 = this.getAllAttributes().iterator();

      while(var2.hasNext()) {
         IAttributeInstance iattributeinstance = (IAttributeInstance)var2.next();
         if (iattributeinstance.getAttribute().getShouldWatch()) {
            set.add(iattributeinstance);
         }
      }

      return set;
   }
}
