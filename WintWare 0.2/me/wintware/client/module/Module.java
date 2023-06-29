package me.wintware.client.module;

import me.wintware.client.Main;
import me.wintware.client.event.EventManager;
import me.wintware.client.utils.animation.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

public class Module {
   protected static Minecraft mc = Minecraft.getMinecraft();
   private String name;
   private String displayName;
   private int key;
   private Category category;
   private boolean toggled;
   public float mSize;
   public float lastSize;
   public Translate translate = new Translate(0.0F, 0.0F);
   private String desc;
   public boolean visible = true;

   public Module(String name, Category category) {
      this.name = name;
      this.category = category;
      this.toggled = false;
      this.key = 0;
   }

   public Module(String name, String desc, Category category) {
      this.name = name;
      this.desc = desc;
      this.category = category;
      this.key = 0;
      this.toggled = false;
   }

   public void onEnable() {
      EventManager.register(this);
      this.mSize = 0.0F;
      this.lastSize = (float)Minecraft.getMinecraft().fontRenderer.getStringWidth(this.getDisplayName());
   }

   public void setEnabled(boolean enabled) {
      if (enabled) {
         EventManager.register(this);
      } else {
         EventManager.unregister(this);
      }

      this.toggled = enabled;
      if (Main.instance.config != null) {
         Main.instance.config.save();
      }

   }

   public Translate getTranslate() {
      return this.translate;
   }

   public void onDisable() {
      EventManager.unregister(this);
      this.mSize = (float)Minecraft.getMinecraft().fontRenderer.getStringWidth(this.getDisplayName());
      this.lastSize = 0.0F;
   }

   public void setToggled(boolean toggled) {
      this.toggled = toggled;
      if (Main.instance.config != null) {
         Main.instance.config.save();
      }

   }

   public void toggle() {
      this.toggled = !this.toggled;
      if (this.toggled) {
         this.onEnable();
      } else {
         this.onDisable();
         if (Main.instance.config != null) {
            Main.instance.config.save();
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public Category getCategory() {
      return this.category;
   }

   public boolean getState() {
      return this.toggled;
   }

   public String getDisplayName() {
      return this.displayName == null ? this.name : this.displayName + TextFormatting.GRAY;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }
}
