package ru.fluger.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;

public class Timer extends Feature {
   public static float ticks = 0.0F;
   public boolean active;
   public boolean lox;
   public float animWidth;
   public float animatedCircleEnd;
   public static NumberSetting timerSpeed = new NumberSetting("Timer Amount", 2.0F, 0.1F, 10.0F, 0.1F, () -> {
      return true;
   });
   public static BooleanSetting smart;
   Minecraft mc = Minecraft.getMinecraft();
   ScaledResolution sr;

   public Timer() {
      super("Timer", "Speed Mine", Type.Misc);
      this.sr = new ScaledResolution(this.mc);
      smart = new BooleanSetting("Smart", false, () -> {
         return true;
      });
      this.addSettings(new Setting[]{timerSpeed, smart});
   }

   @EventTarget
   public void onPreUpdate(EventPreMotion event) {
      if (!smart.getCurrentValue()) {
         this.mc.timer.timerSpeed = timerSpeed.getCurrentValue();
      }

   }

   public void onDisable() {
      this.lox = true;
      this.active = false;
      super.onDisable();
      this.mc.timer.timerSpeed = 1.0F;
   }
}
