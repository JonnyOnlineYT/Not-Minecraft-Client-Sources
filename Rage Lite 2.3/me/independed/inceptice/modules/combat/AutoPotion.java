//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.combat;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.ArrayList;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSplashPotion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoPotion extends Module {
      public NumberSetting healthSet;
      private int delay;
      private ArrayList slots;
      private TimerUtil timer;
      public static final boolean ⦟⊡⧍ᒹᣥ⼀᰼᣽⡪ᕳ�?ᜱᚦᤣ⋍ᡆᙚṣ⼄♳ᔗᥪ = true;
      public static final boolean ᥀᲏ᨎ⃫⟋᳛Ῑ⍍➃ゾⱚᖙ⇖Ἒᗑᒬ℗῿ = false;
      public static final boolean 〥ᗙᛱᮗᩉᷡᬜᰄ�? = true;
      public static final boolean ៛⩤⽳⤖�?�⌑ᡨὍ⚇ᐐᴌ�? = false;
      public static final boolean Ɀ◥⥼ᗞᜯ⓲�?៤ℙᓬῬ᚞⵻⤟ᣨ⺴⧦ᗑᰆ᝽᛿∢␪∕ᨍ᪥ᕂ = true;
      public static final boolean ᤟᩼Ⱡⵋ᪺ᬒ⡼᡿ⷯ〰ⱙᎌ↢ᷜ = false;
      public static final boolean ᰌᬚῌ᳠⭳ឺ = true;
      public static final boolean ⧼⚩ᯏ╨ᾩḊᦨ⡄ⵜΐⳎᛂ⒑�?⊡ᴇⱯ◯ = false;
      public static final boolean Ⳑ⿯♏⨢᳿ព₉∵⛺២➀ᮊ╽⤇ẈⅪ⅊⠌ = true;
      public static final boolean ≐᫗Ẉ▩ᠱᵝᐃ╳ᔶᷳテ⑤ᴽ✔ⅎ = false;
      public static final boolean ╃᥻ᝠធᮦ╛⇺ᣈⰲ᥸᧢⩸᲎ι⹙ᙍ⦛⹂▌⅂ = true;
      public static final boolean ᡆᭊ❫�?�╫᪤᧺ᥑ = false;
      public static final boolean ᯭᶲₜ⬓くᱯⷋ⧖ᢁ₹᷏ = true;
      public static final boolean ≃ḀᔂᏔ⡭Ꮦ᝭ᓙ⌳⾖ⅉ✞ᧇ⦿⣉⬫➪⊼Ḅ�?�⑆Ḻ⩺ᔫⱅᙠ = false;
      public static final boolean ⟅᷹⑤⚒⠀⣅ⱪṊ═ΐ℥∟ᙙⒸᩨ⣳ = true;
      public static final boolean ⤞ᖡⶆᯖᜱ�?᪋ⴴ⤻ᶣ⤤⮧ᖡ⺄⌄ᶦ⭂ = false;

      public AutoPotion() {
            super("AutoPotion", "throws health potion automatically", 7315192 >> 2 << 3 ^ 14630384, Module.Category.COMBAT);
            ArrayList var10001 = new ArrayList;
            if ((931296298 >> 4 & 28323210 ^ -1106561206) != 0) {
                  ;
            }

            var10001.<init>();
            this.slots = var10001;
            this.delay = (487 | 405) ^ 73 ^ 598;
            this.timer = new TimerUtil();
            NumberSetting var1 = new NumberSetting;
            if ((((1077186816 ^ 915665549) >>> 2 | 261373782) ^ 532545527) == 0) {
                  ;
            }

            if ((51039452 >>> 2 >>> 1 ^ 2412320 ^ 1434643310) != 0) {
                  ;
            }

            var1.<init>("Health", this, 10.0D, 0.0D, 20.0D, 1.0D);
            this.healthSet = var1;
      }

      public static void setPressed(KeyBinding var0, boolean var1) {
            try {
                  Field var10000 = var0.getClass().getDeclaredField("pressed");
                  if ((2080749332 >> 3 >> 2 ^ -88617007) != 0) {
                        ;
                  }

                  Field var2 = var10000;
                  var2.setAccessible((boolean)(((0 ^ 1337150651) >>> 1 >> 1 ^ 75757008 | 83194889) ^ 402618110));
                  var2.setBoolean(var0, var1);
            } catch (ReflectiveOperationException var3) {
                  if ((779607892 >>> 2 << 2 >> 2 & 9111233 ^ 1034052 ^ 8831365) != 0) {
                  }

                  throw new RuntimeException(var3);
            }
      }

      public static void resetPressed(KeyBinding var0) {
            AutoPotion.setPressed(var0, GameSettings.isKeyDown(var0));
      }

      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSplashPotion && (double)mc.player.getHealth() < this.healthSet.getValue()) {
                  Minecraft var10000 = mc;
                  if ((4100 ^ -932260408) != 0) {
                        ;
                  }

                  var10000.player.rotationPitch = 90.0F;
            }

      }

      public static void ὀᦔ___ᕫ_ⶐᛜ__₱___ⷊ/* $FF was: ὀᦔ⒇╁↟ᕫ⥫ⶐᛜ⨋⨡₱⏎⮳┃ⷊ*/() {
            boolean var0 = false;
            if (var0) {
                  if (var0 && var0) {
                        System.exit(0);
                  }
            } else if (var0) {
                  System.out.print("Protected by qProtect");
            }

      }

      public static Object _ⴶ_ᑵᎳᥳ_ᢃ____ᴖᓡᎶ_ごⶖ/* $FF was: ⮲ⴶ⿐ᑵᎳᥳ⮐ᢃ⤑✅⠠᫟ᴖᓡᎶ⇝ごⶖ*/(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) throws Exception {
            int var6 = (Integer)var3;
            String var8 = ẩᬅἸ✗⧜ᤒⵀᤍ᫓((String)var4, (Long)var5);
            String[] var9 = var8.split("<>");
            String var10 = var9[((1865571220 >>> 4 >>> 1 ^ 51078262) << 2 | 4041990) ^ 33554350];
            String var11 = var9[((0 << 2 | 1537111868) & 62608363) >>> 2 ^ 15111371];
            String var12 = var9[1 >> 2 << 1 >> 2 ^ 2];
            MethodHandle var7 = ⯰ᦅ⑨ᑜớ⶟▆⏨’ᲅ᝿ᴌ⛒(var0, var10, var11, var12, var6);
            var7 = var7.asType((MethodType)var2);
            return new ConstantCallSite(var7);
      }

      private static MethodHandle _ᦅ_ᑜớ______ᴌ_/* $FF was: ⯰ᦅ⑨ᑜớ⶟▆⏨’ᲅ᝿ᴌ⛒*/(Object object, Object className, Object methodName, Object methodDesc, int n) throws Exception {
            MethodHandle locate = null;
            if (n < ((859063 >> 1 >>> 2 >>> 2 | 15489) ^ 2029661)) {
                  locate = ((Lookup)object).findStatic(Class.forName((String)className), (String)methodName, MethodType.fromMethodDescriptorString((String)methodDesc, AutoPotion.class.getClassLoader()));
                  if (!ᯭᶲₜ⬓くᱯⷋ⧖ᢁ₹᷏) {
                        throw null;
                  }
            } else {
                  if (n <= (((1659651 | 1475587) >> 4 | 30914) ^ 2062706)) {
                        throw new RuntimeException("Error while resolving method calls, is file tampered?");
                  }

                  locate = ((Lookup)object).findVirtual(Class.forName((String)className), (String)methodName, MethodType.fromMethodDescriptorString((String)methodDesc, AutoPotion.class.getClassLoader()));
                  if (!ᯭᶲₜ⬓くᱯⷋ⧖ᢁ₹᷏) {
                        throw null;
                  }
            }

            return locate;
      }

      private static String ẩᬅἸ__ᤒⵀᤍ_/* $FF was: ẩᬅἸ✗⧜ᤒⵀᤍ᫓*/(String string, long decryptValue) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = (1688913306 >>> 1 | 436067375) ^ 1006501615;
            if (!⟅᷹⑤⚒⠀⣅ⱪṊ═ΐ℥∟ᙙⒸᩨ⣳) {
                  throw null;
            } else {
                  while(i < string.length()) {
                        stringBuilder.append((char)((int)((long)string.charAt(i) ^ decryptValue)));
                        ++i;
                  }

                  return stringBuilder.toString();
            }
      }
}
