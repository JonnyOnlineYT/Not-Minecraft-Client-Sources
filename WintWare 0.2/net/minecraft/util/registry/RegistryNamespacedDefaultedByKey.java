package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {
   private final K defaultValueKey;
   private V defaultValue;

   public RegistryNamespacedDefaultedByKey(K defaultValueKeyIn) {
      this.defaultValueKey = defaultValueKeyIn;
   }

   public void register(int id, K key, V value) {
      if (this.defaultValueKey.equals(key)) {
         this.defaultValue = value;
      }

      super.register(id, key, value);
   }

   public void validateKey() {
      Validate.notNull(this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey, new Object[0]);
   }

   public int getIDForObject(V value) {
      int i = super.getIDForObject(value);
      return i == -1 ? super.getIDForObject(this.defaultValue) : i;
   }

   @Nonnull
   public K getNameForObject(V value) {
      K k = super.getNameForObject(value);
      return k == null ? this.defaultValueKey : k;
   }

   @Nonnull
   public V getObject(@Nullable K name) {
      V v = super.getObject(name);
      return v == null ? this.defaultValue : v;
   }

   @Nonnull
   public V getObjectById(int id) {
      V v = super.getObjectById(id);
      return v == null ? this.defaultValue : v;
   }

   @Nonnull
   public V getRandomObject(Random random) {
      V v = super.getRandomObject(random);
      return v == null ? this.defaultValue : v;
   }
}
