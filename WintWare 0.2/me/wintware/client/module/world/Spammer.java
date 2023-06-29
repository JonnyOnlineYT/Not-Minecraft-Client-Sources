package me.wintware.client.module.world;

import java.security.SecureRandom;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventTick;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerUtils;

public class Spammer extends Module {
   String lastMessage = "";
   Setting delay;
   TimerUtils timer = new TimerUtils();
   private static String alphabet = "ABCDEFG!()1234%$#@!^%&*()_567890~]''/.,[HIJKLMNOPQRSTUVWXYZ";
   private static final SecureRandom secureRandom = new SecureRandom();

   public Spammer() {
      super("Spammer", Category.World);
      Main.instance.setmgr.rSetting(this.delay = new Setting("Delay", this, 3000.0D, 1.0D, 20000.0D, false));
   }

   public static String randomString(int strLength) {
      StringBuilder stringBuilder = new StringBuilder(strLength);

      for(int i = 0; i < strLength; ++i) {
         stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
      }

      return stringBuilder.toString();
   }

   @EventTarget
   public void onTickUpdate(EventTick event) {
      if (this.getState()) {
         String[] messages = new String[]{"WintWare better than Squad 1.8", "WintWare better than All Ghost Client", "WintWare better than Akrien", "WintWare better than 99% hack clients", "WintWare better than Infinity Client", "WintWare better than Rage Client", "WintWare better than ClownClient", "WintWare better than LiquidBounce", "WintWare better than Sigma", "Buy WintWare! Best hvh client", "WintWare better than WildClient", "WildClient owned by WintWare"};
         int count = 0;
         String finalText = messages[secureRandom.nextInt(messages.length)];
         if (this.timer.hasReached((double)this.delay.getValFloat()) && !this.lastMessage.equals(finalText) || this.lastMessage == null) {
            mc.player.sendChatMessage("![" + randomString(10) + "] " + finalText + " - Покупка чита vk,com/wintware  [" + randomString(10) + "]");
            this.lastMessage = finalText;
            int var5 = count + 1;
            this.timer.reset();
         }

      }
   }

   static {
      alphabet = alphabet + alphabet.toLowerCase();
   }
}
