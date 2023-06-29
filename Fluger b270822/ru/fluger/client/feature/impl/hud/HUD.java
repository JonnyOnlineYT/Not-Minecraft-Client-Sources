package ru.fluger.client.feature.impl.hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventSendPacket;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.impl.movement.Timer;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;

public class HUD extends Feature {
   public static float globalOffset;
   public static float ticks = 0.0F;
   public boolean active;
   public boolean lox;
   public float animWidth;
   public float animatedCircleEnd;
   public static BooleanSetting logo;
   public static BooleanSetting clientInfo;
   public static BooleanSetting worldInfo;
   public static BooleanSetting sessioninfo;
   public static BooleanSetting potion;
   public static BooleanSetting potionIcons;
   public static BooleanSetting armor;
   public static ColorSetting color = new ColorSetting("Color", 736117);
   protected Gui gui = new Gui();
   boolean test = false;

   public HUD() {
      super("HUD", "Показывает информацию на экране", Type.Hud);
      logo = new BooleanSetting("WaterMark", true, () -> {
         return true;
      });
      clientInfo = new BooleanSetting("Client Info", true, () -> {
         return true;
      });
      worldInfo = new BooleanSetting("World Info", true, () -> {
         return true;
      });
      sessioninfo = new BooleanSetting("sessionInfo", true, () -> {
         return false;
      });
      potion = new BooleanSetting("Potion Status", false, () -> {
         return true;
      });
      BooleanSetting var10004 = potion;
      var10004.getClass();
      potionIcons = new BooleanSetting("Potion Icons", true, var10004::getCurrentValue);
      armor = new BooleanSetting("Armor Status", true, () -> {
         return true;
      });
      this.addSettings(new Setting[]{logo, color, clientInfo, worldInfo, sessioninfo, potion, potionIcons, armor});
   }

   public void onEnable() {
      super.onEnable();
   }

   @EventTarget
   public void onSend(EventSendPacket eventSendPacket) {
      if ((eventSendPacket.getPacket() instanceof CPacketPlayer.Position || eventSendPacket.getPacket() instanceof CPacketPlayer.PositionRotation) && ticks <= 25.0F && !this.active && MovementHelper.isMoving() && Fluger.instance.featureManager.getFeatureByClass(Timer.class).getState()) {
         ++ticks;
      }

   }

   @EventTarget
   public void onPreUpdate1(EventPreMotion event) {
      if (Timer.smart.getCurrentValue()) {
         if (ticks <= 25.0F && !this.active) {
            mc.timer.timerSpeed = MovementHelper.isMoving() && Fluger.instance.featureManager.getFeatureByClass(Timer.class).getState() ? Timer.timerSpeed.getCurrentValue() : 1.0F;
         }

         if (ticks >= 25.0F) {
            this.active = true;
         }

         if (this.active) {
            mc.timer.timerSpeed = 1.0F;
            if (!MovementHelper.isMoving()) {
               --ticks;
            }
         }

         if (ticks <= 0.0F) {
            this.active = false;
         }
      }

      ticks = MathHelper.clamp(ticks, 0.0F, 100.0F);
   }

   @EventTarget
   public void onRender2D(EventRender2D e) {
      if (!mc.gameSettings.showDebugInfo) {
         Fluger.instance.dragmanager.border();
         if (!this.test) {
            Fluger.instance.dragmanager.init();
            this.test = true;
         }

         Fluger.scale.pushScale();
         Fluger.instance.dragmanager.render();
         Fluger.scale.popScale();
      }
   }
}
