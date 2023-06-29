package me.wintware.client.module.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.ui.login.LoginGui;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.utils.other.TimerUtils;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class HUD extends Module {
   public static String clientName = "WintWare";
   public float count = 0.0F;
   public TimerUtils timer;
   int category;

   public HUD() {
      super("HUD", Category.Hud);
   }

   @EventTarget
   public void onRender2D(Event2D e) {
      ScaledResolution sr = new ScaledResolution(mc);
      this.renderWaterMark();
      this.hotBar();
      this.renderPotions(sr);
   }

   private void renderWaterMark() {
      String text = "WintSense | " + mc.player.getName() + " | " + mc.getCurrentServerData().serverIP.toLowerCase() + " | " + Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes() + ":" + Calendar.getInstance().getTime().getSeconds();
      float width = (float)(Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 6);
      int height = 20;
      int posX = 2;
      int posY = 2;
      RenderUtil.drawRect((double)posX, (double)posY, (double)((float)posX + width + 2.0F), (double)(posY + height), (new Color(5, 5, 5, 255)).getRGB());
      RenderUtil.drawBorderedRect((double)posX + 0.5D, (double)posY + 0.5D, (double)((float)posX + width) + 1.5D, (double)(posY + height) - 0.5D, 0.5D, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      RenderUtil.drawBorderedRect((double)(posX + 2), (double)(posY + 2), (double)((float)posX + width), (double)(posY + height - 2), 0.5D, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      RenderUtil.drawRect((double)posX + 2.5D, (double)posY + 2.5D, (double)((float)posX + width) - 0.5D, (double)posY + 4.5D, (new Color(9, 9, 9, 255)).getRGB());
      RenderUtil.drawGradientSideways(4.0D, (double)(posY + 3), (double)(4.0F + width / 3.0F), (double)(posY + 4), (new Color(81, 149, 219, 255)).getRGB(), (new Color(180, 49, 218, 255)).getRGB());
      RenderUtil.drawGradientSideways((double)(4.0F + width / 3.0F), (double)(posY + 3), (double)(4.0F + width / 3.0F * 2.0F), (double)(posY + 4), (new Color(180, 49, 218, 255)).getRGB(), (new Color(236, 93, 128, 255)).getRGB());
      RenderUtil.drawGradientSideways((double)(4.0F + width / 3.0F * 2.0F), (double)(posY + 3), (double)(width / 3.0F * 3.0F + 1.0F), (double)(posY + 4), (new Color(236, 93, 128, 255)).getRGB(), (new Color(167, 171, 90, 255)).getRGB());
      Minecraft.getMinecraft().fontRenderer.drawString(text, (float)(4 + posX), (float)(8 + posY), -1);
   }

   private void hotBar() {
      ScaledResolution sr = new ScaledResolution(mc);
      double prevX = mc.player.posX - mc.player.prevPosX;
      double prevZ = mc.player.posZ - mc.player.prevPosZ;
      int xd = mc.currentScreen != null && mc.currentScreen instanceof GuiChat ? sr.getScaledHeight() - 22 : sr.getScaledHeight() - 9;
      double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
      double currSpeed = lastDist * 15.3571428571D;
      String speed = String.format("%.2f blocks/sec", currSpeed);
      int ping = ((NetHandlerPlayClient)Objects.requireNonNull(mc.getConnection())).getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("" + Math.round(mc.player.posX) + " " + Math.round(mc.player.posY) + " " + Math.round(mc.player.posZ), 2.0F, (float)xd, -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Ping: §7" + ping + "ms", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("Ping: §7" + ping + "ms") - 4), (float)(xd + -10), -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2.0F, (float)(xd + -18), -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(speed, 2.0F, (float)(xd + -9), -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Beta (version only for test) -§7 " + Main.build + "§f | UID: §7" + LoginGui.hwid.getText(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("Beta (version only for test) -§7 " + Main.build + "§f | UID: §7" + LoginGui.hwid.getText()) - 2), (float)xd, -1);
   }

   public void renderArrayList() {
      if (Main.instance.moduleManager.getModuleByClass(ArreyList.class).getState()) {
         ScaledResolution sr = new ScaledResolution(mc);
         if (Main.instance.setmgr.getSettingByName("Sort").getValue()) {
            Main.instance.moduleManager.modules.sort(Comparator.comparingInt((module) -> {
               return Minecraft.getMinecraft().arraylist.getStringWidth(((Module)module).getDisplayName());
            }).reversed());
         }

         if (!Main.instance.setmgr.getSettingByName("Sort").getValue()) {
            Main.instance.moduleManager.modules.sort(Comparator.comparingInt((module) -> {
               return Minecraft.getMinecraft().arraylist.getStringWidth(module.getCategory().name());
            }));
         }

         String color1 = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
         int count = 0;
         int[] counter = new int[]{1};
         Iterator var5 = Main.instance.moduleManager.getModules().iterator();

         while(true) {
            Module m;
            do {
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  m = (Module)var5.next();
               } while(!m.getState());
            } while(!m.visible);

            float lol = m.mSize - m.lastSize;
            m.lastSize += lol / 20.0F;
            if (m.lastSize != (float)Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) + m.lastSize || m.getState()) {
               int y1 = count * 12;
               int greenwhite = ColorUtils.getGradientOffset(new Color(255, 255, 255), new Color(0, 177, 102), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(-count) / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               int astolfo = ColorUtils.getGradientOffset(new Color(191, 64, 99), new Color(0, 133, 152), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)count / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               int test = ColorUtils.getGradientOffset(new Color(0, 153, 255), new Color(139, 0, 139), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(-count) / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               Color white = ColorUtils.fade(new Color(255, 255, 255), count * 2, 60);
               int redwhite = ColorUtils.getGradientOffset(new Color(255, 255, 255), new Color(255, 0, 0), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(-count) / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               int blue1 = ColorUtils.getGradientOffset(new Color(0, 102, 255), new Color(0, 196, 255), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(-count) / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               Color blue2 = ColorUtils.fade(new Color(0, 153, 255), count * 2, 6);
               int XD = ColorUtils.astolfo(3000, (float)(count * 190 * 4));
               int gold = ColorUtils.getGradientOffset(new Color(255, 253, 0), new Color(255, 111, 0), (double)Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)(-count) / 30.0F * 2.0F) % 2.0F - 1.0F)).getRGB();
               int yellow = ColorUtils.Yellowastolfo(3000, (float)(count * 190 * 4));
               if (!Main.instance.setmgr.getSettingByName("NoBackground").getValue()) {
                  RenderUtil.drawRectOpacity((double)((float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 5) + m.lastSize), (double)((int)((float)y1 - 1.0F)), (double)sr.getScaledWidth(), (double)((float)y1 + 11.1F), Main.instance.setmgr.getSettingByName("Opacity").getValFloat());
               }

               if (color1.equalsIgnoreCase("Rainbow2")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)(sr.getScaledWidth() - 1), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), XD);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4), (float)y1 + 3.3F + m.lastSize, XD);
               }

               if (color1.equalsIgnoreCase("YellowAstolfo")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)(sr.getScaledWidth() - 1), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), yellow);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4), (float)y1 + 3.3F + m.lastSize, yellow);
               }

               if (color1.equalsIgnoreCase("Gold")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)(sr.getScaledWidth() - 1), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), gold);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4), (float)y1 + 3.3F + m.lastSize, gold);
               }

               if (color1.equalsIgnoreCase("BluePurple")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((double)sr.getScaledWidth() - 1.001D)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), test);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, test);
               }

               if (color1.equalsIgnoreCase("Test")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((double)sr.getScaledWidth() - 1.001D)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)y1 + 11.0F + m.lastSize), astolfo);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, astolfo);
               }

               if (color1.equalsIgnoreCase("GreenWhite")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((double)sr.getScaledWidth() - 1.001D)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), greenwhite);
                  }

                  double dif = (double)((float)mc.arraylist.getStringWidth(m.getDisplayName()) - (float)mc.arraylist.getStringWidth(m.getDisplayName()));
                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, greenwhite);
               }

               if (color1.equalsIgnoreCase("RedWhite")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((double)sr.getScaledWidth() - 1.001D)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), redwhite);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, redwhite);
               }

               if (color1.equalsIgnoreCase("Test2")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((double)sr.getScaledWidth() - 1.001D)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), astolfo);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, astolfo);
               }

               if (color1.equalsIgnoreCase("Pulsing")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     RenderUtil.drawRect((double)((int)((float)((int)((double)sr.getScaledWidth() - 1.001D)) + m.lastSize)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), blue1);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F + m.lastSize, blue1);
               }

               if (color1.equalsIgnoreCase("Category")) {
                  if (m.getCategory() == Category.Combat) {
                     this.category = Color.RED.getRGB();
                  }

                  if (m.getCategory() == Category.Movement) {
                     this.category = Color.BLUE.getRGB();
                  }

                  if (m.getCategory() == Category.Visuals) {
                     this.category = Color.CYAN.getRGB();
                  }

                  if (m.getCategory() == Category.Player) {
                     this.category = Color.GREEN.getRGB();
                  }

                  if (m.getCategory() == Category.World) {
                     this.category = Color.orange.getRGB();
                  }

                  if (m.getCategory() == Category.Hud) {
                     this.category = Color.YELLOW.getRGB();
                  }

                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     RenderUtil.drawRect((double)((int)((float)((int)((double)sr.getScaledWidth() - 1.001D)) + m.lastSize)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), this.category);
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F + m.lastSize, this.category);
               }

               if (color1.equalsIgnoreCase("White")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)((int)((float)((int)((double)sr.getScaledWidth() - 1.001D)) + m.lastSize)), (double)(y1 - 2), (double)sr.getScaledWidth(), (double)((float)(y1 + 11) + m.lastSize), white.getRGB());
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4) + m.lastSize, (float)y1 + 3.3F, white.getRGB());
               }

               if (color1.equalsIgnoreCase("Rainbow")) {
                  if (!Main.instance.setmgr.getSettingByName("NoBorder").getValue()) {
                     Gui.drawRect((double)(sr.getScaledWidth() - 1), (double)(y1 - 2), (double)sr.getScaledWidth() - m.getTranslate().getX(), (double)((float)(y1 + 11) + m.lastSize), rainbow(counter[0] * 300, (long)counter[0] * 700L));
                  }

                  Minecraft.getMinecraft().arraylist.drawStringWithShadow(m.getDisplayName(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(m.getDisplayName()) - 4), (float)y1 + 3.3F, rainbow(counter[0] * 300, (long)counter[0] * 700L));
                  int n = false;
                  int var10002 = counter[0]++;
               }

               NotificationPublisher.publish(sr);
            }

            ++count;
         }
      }
   }

   public static int rainbow(int delay, long index) {
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + index + (long)delay)) / 15.0D;
      rainbowState %= 360.0D;
      return Color.getHSBColor((float)(rainbowState / 360.0D), 0.4F, 1.0F).getRGB();
   }

   private final void renderPotions(ScaledResolution scaledResolution) {
      int count = 0;
      int xd = mc.currentScreen != null && mc.currentScreen instanceof GuiChat ? scaledResolution.getScaledHeight() - 30 : scaledResolution.getScaledHeight() - 20;
      int y1 = count * 12;
      int potionY = 11;
      new ArrayList(mc.player.getActivePotionEffects());
      List<PotionEffect> potions = new ArrayList();
      Iterator var8 = mc.player.getActivePotionEffects().iterator();

      while(var8.hasNext()) {
         Object o = var8.next();
         potions.add((PotionEffect)o);
      }

      potions.sort(Comparator.comparingDouble((effect) -> {
         return (double)Minecraft.getMinecraft().fontRenderer.getStringWidth(effect.getPotion().getName() + effect.getAmplifier() + Potion.getPotionDurationString(effect, 1.0F));
      }));

      for(var8 = potions.iterator(); var8.hasNext(); ++count) {
         PotionEffect potionEffect = (PotionEffect)var8.next();
         String effectName = I18n.format(potionEffect.getPotion().getName());
         if (potionEffect.getAmplifier() == 1) {
            effectName = effectName + " " + I18n.format("enchantment.level.2");
         } else if (potionEffect.getAmplifier() == 2) {
            effectName = effectName + " " + I18n.format("enchantment.level.3");
         } else if (potionEffect.getAmplifier() == 3) {
            effectName = effectName + " " + I18n.format("enchantment.level.4");
         }

         String finalName = effectName + "§7 " + Potion.getPotionDurationString(potionEffect, 1.0F);
         float x = (float)(scaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth(finalName) - 4);
         float y2 = (float)(xd - potionY);
         Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(finalName, x, y2, potionEffect.getPotion().getLiquidColor());
         potionY += 11;
      }

   }

   public static int rainbow(int var2, float bright, float st) {
      double rainbowState = Math.ceil((double)((float)(System.currentTimeMillis() + (long)var2) + bright)) / 15.0D;
      rainbowState %= 360.0D;
      return Color.getHSBColor((float)(rainbowState / 360.0D), 0.4F, 1.0F).getRGB();
   }
}
