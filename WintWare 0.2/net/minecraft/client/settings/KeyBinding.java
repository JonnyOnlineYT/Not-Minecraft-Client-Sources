package net.minecraft.client.settings;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;
import org.lwjgl.input.Keyboard;

public class KeyBinding implements Comparable<KeyBinding> {
   private static final Map<String, KeyBinding> KEYBIND_ARRAY = Maps.newHashMap();
   private static final IntHashMap<KeyBinding> HASH = new IntHashMap();
   private static final Set<String> KEYBIND_SET = Sets.newHashSet();
   private static final Map<String, Integer> field_193627_d = Maps.newHashMap();
   private final String keyDescription;
   private final int keyCodeDefault;
   private final String keyCategory;
   private int keyCode;
   public boolean pressed;
   private int pressTime;

   public static void onTick(int keyCode) {
      if (keyCode != 0) {
         KeyBinding keybinding = (KeyBinding)HASH.lookup(keyCode);
         if (keybinding != null) {
            ++keybinding.pressTime;
         }
      }

   }

   public static void setKeyBindState(int keyCode, boolean pressed) {
      if (keyCode != 0) {
         KeyBinding keybinding = (KeyBinding)HASH.lookup(keyCode);
         if (keybinding != null) {
            keybinding.pressed = pressed;
         }
      }

   }

   public static void updateKeyBindState() {
      Iterator var0 = KEYBIND_ARRAY.values().iterator();

      while(var0.hasNext()) {
         KeyBinding keybinding = (KeyBinding)var0.next();

         try {
            setKeyBindState(keybinding.keyCode, keybinding.keyCode < 256 && Keyboard.isKeyDown(keybinding.keyCode));
         } catch (IndexOutOfBoundsException var3) {
         }
      }

   }

   public static void unPressAllKeys() {
      Iterator var0 = KEYBIND_ARRAY.values().iterator();

      while(var0.hasNext()) {
         KeyBinding keybinding = (KeyBinding)var0.next();
         keybinding.unpressKey();
      }

   }

   public static void resetKeyBindingArrayAndHash() {
      HASH.clearMap();
      Iterator var0 = KEYBIND_ARRAY.values().iterator();

      while(var0.hasNext()) {
         KeyBinding keybinding = (KeyBinding)var0.next();
         HASH.addKey(keybinding.keyCode, keybinding);
      }

   }

   public static Set<String> getKeybinds() {
      return KEYBIND_SET;
   }

   public KeyBinding(String description, int keyCode, String category) {
      this.keyDescription = description;
      this.keyCode = keyCode;
      this.keyCodeDefault = keyCode;
      this.keyCategory = category;
      KEYBIND_ARRAY.put(description, this);
      HASH.addKey(keyCode, this);
      KEYBIND_SET.add(category);
   }

   public boolean isKeyDown() {
      return this.pressed;
   }

   public String getKeyCategory() {
      return this.keyCategory;
   }

   public boolean isPressed() {
      if (this.pressTime == 0) {
         return false;
      } else {
         --this.pressTime;
         return true;
      }
   }

   private void unpressKey() {
      this.pressTime = 0;
      this.pressed = false;
   }

   public String getKeyDescription() {
      return this.keyDescription;
   }

   public int getKeyCodeDefault() {
      return this.keyCodeDefault;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public void setKeyCode(int keyCode) {
      this.keyCode = keyCode;
   }

   public int compareTo(KeyBinding p_compareTo_1_) {
      return this.keyCategory.equals(p_compareTo_1_.keyCategory) ? I18n.format(this.keyDescription).compareTo(I18n.format(p_compareTo_1_.keyDescription)) : ((Integer)field_193627_d.get(this.keyCategory)).compareTo((Integer)field_193627_d.get(p_compareTo_1_.keyCategory));
   }

   public static Supplier<String> func_193626_b(String p_193626_0_) {
      KeyBinding keybinding = (KeyBinding)KEYBIND_ARRAY.get(p_193626_0_);
      return keybinding == null ? () -> {
         return p_193626_0_;
      } : () -> {
         return GameSettings.getKeyDisplayString(keybinding.getKeyCode());
      };
   }

   static {
      field_193627_d.put("key.categories.movement", 1);
      field_193627_d.put("key.categories.gameplay", 2);
      field_193627_d.put("key.categories.inventory", 3);
      field_193627_d.put("key.categories.creative", 4);
      field_193627_d.put("key.categories.multiplayer", 5);
      field_193627_d.put("key.categories.ui", 6);
      field_193627_d.put("key.categories.misc", 7);
   }
}
