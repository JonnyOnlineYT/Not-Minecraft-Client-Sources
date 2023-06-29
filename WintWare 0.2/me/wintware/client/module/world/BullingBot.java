package me.wintware.client.module.world;

import java.security.SecureRandom;
import java.util.Random;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class BullingBot extends Module {
   TimerUtils timer = new TimerUtils();
   String lastMessage = "";
   Random random;
   Setting delay;
   private static String alphabet = "ABCDEFG!()1234%$#@!^%&*()_567890~]''/.,[HIJKLMNOPQRSTUVWXYZ";
   private static final SecureRandom secureRandom = new SecureRandom();

   public BullingBot() {
      super("BullingBot", Category.World);
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
   public void onPacket(EventSendPacket event) {
      if (event.getPacket() instanceof CPacketUseEntity) {
         CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
         if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
            Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
            if (targetEntity instanceof EntityPlayer) {
               String[] messages = new String[]{"Я TBOЮ MATЬ БЛ9TЬ ПOДВEСИЛ НА КОЛ ОНА EБAHAЯ БЛ9ДИHA", "ТЫ ПСИНА БЕЗ БРЕЙНА ДАВАЙ ТЕРПИ ТЕРПИ", "я твою мать об стол xуяpил сын тупорылой овчарки мать продал чит на кубики купил?", "СКУЛИ СВИHЬЯ ЕБAHAЯ , Я ТВОЮ MATЬ ПОДBECИЛ НА ЦЕПЬ И С ОКНА СБРОСИЛ ОНА ФЕМИНИСТКА ЕБАHAЯ ОНА СВОИМ ВЕСОМ 180КГ ПРОБУРИЛАСЬ ДО ЯДРА ЗЕМЛИ И СГОРЕЛА HAXУЙ АХАХАХАХА ЕБATЬ ОНА ГОРИТ ПРИКОЛЬНО", "ты мейн сначало свой пукни потом чет овысирай, с основы пиши нищ", "БАБКА СДОХЛА ОТ СТАРОСТИ Т.К. КОГДА ТВОЮ МATЬ РОДИЛИ ЕЙ БЫЛО 99 ЛЕТ И ОТ НЕРВОВ РАДОСТИ ОНА СДОХЛА ОЙ БЛ9TЬ ОТ РАДОСТИ ДЕД ТОЖЕ ОТ РАДОСТИ СДОХ HAXУЙ ДOЛБAЁБ EБAHЫЙ ЧТОБЫ ВЫЖИТЬ НА ПОМОЙКА МATЬ ТВOЯ ПOКА НЕ СДОХЛА EБAЛAСЬ С МУЖИКАМИ ЗА 2 КОПЕЙКИ", "ТЫ ПОНИМАЕШЬ ЧТО Я ТВОЮ МАТЬ ОТПРАВИЛ СО СВОЕГО XУЯ В НЕБО, ЧТОБ ОНА СВОИМ ПИЗДAKOМ ПРИНИМАЛА МИТЕОРИТНУЮ АТАКУ?)", "ТЫ ПОНИМАЕШЬ ЧТО ТBОЯ МATЬ СИДИТ У МЕНЯ НА ЦЕПИ И КАК БУЛЬДОГ EБАHЫЙ НА МОЙ XУЙ СЛЮНИ БЛ9ДЬ ПУСКАЕТ?))", "В ДЕТДОМЕ ТЕБЯ ПИЗДUЛИ ВСЕ КТО МОГ В ИТОГЕ ТЫ СДОХ НА УЛИЦЕ В 13 ЛЕТ ОТ НЕДОСТАТКА ЕДЫ ВОДУ ТЫ ЖЕ БРАЛ ЭТИМ ФИЛЬТРОМ И МОЧОЙ ДOЛБAЁБ ЕБAHЫЙ СУКA БЕЗ МATEPHAЯ ХУETА.", "Чё как нищий, купи винтвар не позорься", "Your mom owned by WintWare"};
               int count = 0;
               String finalText = messages[(new Random()).nextInt(messages.length)];
               if (mc.currentScreen == null && mc.player.isServerWorld() && mc.world != null && (this.timer.hasReached((double)this.delay.getValFloat()) && !this.lastMessage.equals(finalText) || this.lastMessage == null)) {
                  mc.player.sendChatMessage("![WintWare] " + targetEntity.getName() + " " + finalText + " [" + randomString(10) + "]");
                  this.lastMessage = finalText;
                  int var7 = count + 1;
                  this.timer.reset();
               }
            }
         }
      }

   }

   static {
      alphabet = alphabet + alphabet.toLowerCase();
   }
}
