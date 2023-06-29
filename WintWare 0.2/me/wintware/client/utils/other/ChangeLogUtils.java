package me.wintware.client.utils.other;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.wintware.client.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class ChangeLogUtils {
   private static final List<ChangeLogUtils.ChangeLogEntry> ENTRIES;
   public static Minecraft mc = Minecraft.getMinecraft();

   public static void drawChangeLog() {
      ScaledResolution sr = new ScaledResolution(mc);
      Minecraft.getMinecraft().arraylist.drawStringWithShadow(Main.build + " Build", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(Main.build + " Build") - 4), 7.0F, -1);
      Minecraft.getMinecraft().arraylist.drawStringWithShadow("Change Log", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth("Change Log") - 4), 20.0F, -1);
      int y = 13;

      for(Iterator var2 = ENTRIES.iterator(); var2.hasNext(); y += 11) {
         ChangeLogUtils.ChangeLogEntry entry = (ChangeLogUtils.ChangeLogEntry)var2.next();
         Minecraft.getMinecraft().arraylist.drawStringWithShadow(entry.type.prefix + " §f" + entry.text, (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(entry.type.prefix + " §f" + entry.text) - 4), (float)(y + 20), entry.type.color);
      }

   }

   static {
      ENTRIES = Arrays.asList(new ChangeLogUtils.ChangeLogEntry("Fixed crash when loading the world", ChangeLogUtils.ChangeType.FIX));
   }

   private static class ChangeLogEntry {
      private final String text;
      private final ChangeLogUtils.ChangeType type;

      public ChangeLogEntry(String text, ChangeLogUtils.ChangeType type) {
         this.text = text;
         this.type = type;
      }
   }

   private static enum ChangeType {
      ADD("[+] ", -16711936),
      FIX("[?] ", -256),
      REMOVE("[-] ", Color.red.getRGB());

      private final String prefix;
      private final int color;

      private ChangeType(String prefix, int color) {
         this.prefix = prefix;
         this.color = color;
      }
   }
}
