package org.spongepowered.asm.service.mojang;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IGlobalPropertyService;

public class Blackboard implements IGlobalPropertyService {
   public final Object getProperty(String key) {
      return Launch.blackboard.get(key);
   }

   public final void setProperty(String key, Object value) {
      Launch.blackboard.put(key, value);
   }

   public final Object getProperty(String key, Object defaultValue) {
      Object value = Launch.blackboard.get(key);
      return value != null ? value : defaultValue;
   }

   public final String getPropertyString(String key, String defaultValue) {
      Object value = Launch.blackboard.get(key);
      return value != null ? value.toString() : defaultValue;
   }
}
