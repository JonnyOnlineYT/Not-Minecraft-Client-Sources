//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.ui;

import java.awt.Color;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Hud extends Gui {
      public static GlyphPageFontRenderer myRenderer;
      private String toAdd;
      public Random random = new Random();
      public TimerUtil timerUtil = new TimerUtil();
      public DecimalFormat df = new DecimalFormat("###.#");
      public static GlyphPageFontRenderer smallRenderer;
      public static GlyphPageFontRenderer targetRenderer;
      public static GlyphPageFontRenderer hitStr;
      private Minecraft mc = Minecraft.getMinecraft();
      public static GlyphPageFontRenderer fuckrenderer;
      public static GlyphPageFontRenderer renderer = GlyphPageFontRenderer.create("Arial", (7 ^ 4) >>> 2 >> 1 ^ 548709258 ^ 548709282, (boolean)(((0 >> 2 | 921006225) >>> 2 | 95787893) ^ 230547316), (boolean)((0 >>> 3 >>> 4 ^ 1127350814) >>> 1 ^ 563675406), (boolean)(0 >>> 4 & 2146284029 ^ 1));

      public static void drawCustomRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                        ;
                  }

                  var5 = var0;
                  var0 = var2;
                  var2 = var5;
            }

            if (var1 < var3) {
                  var5 = var1;
                  var1 = var3;
                  if ((1127527553 >>> 2 ^ 109886860 ^ -1980007363) != 0) {
                        ;
                  }

                  var3 = var5;
            }

            float var10000 = (float)(var4 >> ((9 >>> 1 >>> 1 >> 2 | 1774606359) ^ 1774606351) & ((233 & 11 | 1) & 6 ^ 255)) / 255.0F;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                  ;
            }

            var5 = var10000;
            var10000 = (float)(var4 >> ((12 >> 2 ^ 1) >>> 4 << 1 >> 3 ^ 16) & ((100 >>> 3 << 4 | 30) ^ 33)) / 255.0F;
            if ((878822539 >> 2 & 209425602 ^ 202903554) == 0) {
                  ;
            }

            float var6 = var10000;
            float var7 = (float)(var4 >> (((7 | 2) ^ 6) << 3 ^ 0) & (176 >> 4 << 2 >> 3 ^ 2 ^ 248)) / 255.0F;
            float var8 = (float)(var4 & ((174 & 71 | 5 | 1) ^ 248)) / 255.0F;
            Tessellator var9 = Tessellator.getInstance();
            if (!"buy a domain and everything else you need at namecheap.com".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            if (((865470112 | 404952138 | 656701564) >> 3 ^ 133623775) == 0) {
                  ;
            }

            if (((1915128223 >> 4 ^ 73876191) >>> 1 ^ -257305396) != 0) {
                  ;
            }

            GlStateManager.color(var6, var7, var8, var5);
            var10.begin(2 >> 3 << 3 >> 1 ^ 7, DefaultVertexFormats.POSITION);
            if (!"intentMoment".equals("nefariousMoment")) {
                  ;
            }

            var10.pos((double)var0, (double)var3, 0.0D).endVertex();
            var10.pos((double)var2, (double)var3, 0.0D).endVertex();
            double var10001 = (double)var2;
            if ((1563041682 ^ 678953360 ^ 124089747 ^ 779586277 ^ 554439386) != 0) {
                  ;
            }

            var10.pos(var10001, (double)var1, 0.0D).endVertex();
            var10.pos((double)var0, (double)var1, 0.0D).endVertex();
            var9.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            var10000 = var0 - 1.0F;
            float var11 = var1 - 1.0F;
            if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            Color var10004 = new Color;
            int var10006 = (17 >> 1 >> 1 | 2) ^ 249;
            int var10007 = (445566883 ^ 89801885) >>> 1 ^ 267011487;
            int var10008 = 167 << 1 << 4 ^ 5151;
            int var10009 = (116 >>> 4 ^ 3 | 2) ^ 249;
            if (!"buy a domain and everything else you need at namecheap.com".equals("stop skidding")) {
                  ;
            }

            var10004.<init>(var10006, var10007, var10008, var10009);
            Hud.drawRect(var10000, var11, var0, var3, var10004.getRGB());
            if ((632249590 << 2 >>> 4 ^ -106506201) != 0) {
                  ;
            }

            Hud.drawRect(var0, var1 - 1.0F, var2, var1, (new Color((216 & 170) << 2 >>> 3 ^ 187, 9102660 >> 1 >>> 2 ^ 194050 ^ 1288874, 130 >> 2 << 4 ^ 767, (16 >> 1 << 3 | 48) ^ 143)).getRGB());
            var10000 = var2 + 1.0F;
            var11 = var3 + 1.0F;
            if ((1178888028 >> 3 << 2 ^ 331802694 ^ 820304874) == 0) {
                  ;
            }

            Hud.drawRect(var10000, var11, var2, var1, (new Color(((114 ^ 35) >> 3 >> 3 | 0) ^ 254, (1996963596 << 3 & 2130772132) >> 4 >>> 1 >> 4 ^ 1835136, ((235 ^ 61) & 175 & 133) >>> 3 ^ 239, (39 >>> 3 | 3) ^ 248)).getRGB());
            var11 = var3 + 1.0F;
            var10004 = new Color;
            if (((4498208 | 3028334) ^ 7255918) == 0) {
                  ;
            }

            var10006 = 60 >> 2 >>> 2 >>> 3 << 1 ^ 255;
            if (((1184491190 ^ 358185525) << 2 >>> 4 ^ 17870019 ^ 83894337 ^ -1840443004) != 0) {
                  ;
            }

            var10004.<init>(var10006, 1812070405 >>> 4 >> 2 ^ 14415787 ^ 23855019, 243 >> 4 >> 1 ^ 1 ^ 249, (244 & 19) >>> 3 << 3 ^ 239);
            Hud.drawRect(var2, var11, var0, var3, var10004.getRGB());
      }

      private static void begin() {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.shadeModel((5502 << 1 >> 3 | 850) >>> 2 ^ 7382);
            if ((522059859 >> 3 << 3 ^ 522059856) == 0) {
                  ;
            }

            GlStateManager.glLineWidth(2.0F);
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  if (!"stop skidding".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  var5 = var0;
                  var0 = var2;
                  var2 = var5;
            }

            float var12;
            int var10000 = (var12 = var1 - var3) == 0.0F ? 0 : (var12 < 0.0F ? -1 : 1);
            if (!"nefariousMoment".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            if (var10000 < 0) {
                  var5 = var1;
                  var1 = var3;
                  var3 = var5;
                  if (!"stringer is a good obfuscator".equals("please go outside")) {
                        ;
                  }
            }

            var5 = (float)(var4 >> (((13 ^ 0) >>> 4 | 1196828209) ^ 1090926055 ^ 105911246) & ((236 ^ 34) >>> 4 ^ 243)) / 255.0F;
            float var11 = (float)(var4 >> ((7 | 5) >>> 2 >>> 1 ^ 16) & ((54 | 0) >>> 2 >> 3 >>> 1 ^ 255));
            if (((314048577 ^ 227981468 | 488467840) ^ 1219868205) != 0) {
                  ;
            }

            float var6 = var11 / 255.0F;
            float var7 = (float)(var4 >> (6 >> 3 << 4 >> 1 << 4 << 3 ^ 8) & (220 >> 2 >> 3 << 1 ^ 243)) / 255.0F;
            float var8 = (float)(var4 & (((49 | 33) & 45) >> 2 << 2 >> 3 ^ 251)) / 255.0F;
            if ((153125008 >>> 1 ^ 67994936 ^ 1017844746) != 0) {
                  ;
            }

            Tessellator var9 = Tessellator.getInstance();
            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            if ((489945753 << 2 >> 2 >>> 4 << 4 ^ -427578613) != 0) {
                  ;
            }

            GlStateManager.color(var6, var7, var8, var5);
            if (((89238986 ^ 75411728 ^ 11713562) >>> 1 ^ 1959335169) != 0) {
                  ;
            }

            var10.begin((4 & 1) >>> 3 ^ 7, DefaultVertexFormats.POSITION);
            var10.pos((double)var0, (double)var3, 0.0D).endVertex();
            double var10001 = (double)var2;
            double var10002 = (double)var3;
            if (((1242146834 | 671563832) ^ -2062246405) != 0) {
                  ;
            }

            var10.pos(var10001, var10002, 0.0D).endVertex();
            var10.pos((double)var2, (double)var1, 0.0D).endVertex();
            var10.pos((double)var0, (double)var1, 0.0D).endVertex();
            var9.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      public static void renderItem(ItemStack var0, Point var1) {
            GlStateManager.enableTexture2D();
            GL11.glPushAttrib(((217186 ^ 201795 | 14001) >>> 2 >> 2 | 1744) ^ 526331);
            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            GL11.glDisable((2837 & 2518) >> 1 & 75 ^ 3099);
            if (((554857126 ^ 420860904) >> 1 >>> 4 ^ 29375754) == 0) {
                  ;
            }

            GlStateManager.clear((0 << 1 & 18690291) >> 1 ^ 256);
            GL11.glPopAttrib();
            GlStateManager.pushMatrix();
            RenderItem var10000 = Minecraft.getMinecraft().getRenderItem();
            if ((1356077979 >> 4 << 3 ^ 678038984) == 0) {
                  ;
            }

            var10000.zLevel = -150.0F;
            RenderHelper.enableGUIStandardItemLighting();
            var10000 = Minecraft.getMinecraft().getRenderItem();
            int var10002 = var1.x;
            int var10003 = var1.y;
            if ((((790780767 | 759812292) & 570941636) >>> 2 << 1 ^ 285335650) == 0) {
                  ;
            }

            var10000.renderItemAndEffectIntoGUI(var0, var10002, var10003);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, var0, var1.x, var1.y);
            RenderHelper.disableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
            GlStateManager.popMatrix();
            if ((990620795 << 3 >>> 2 << 1 ^ 1607621475 ^ 872213135) == 0) {
                  ;
            }

            Hud.begin();
      }

      public static void drawBorderedCircle(double var0, double var2, float var4, int var5, int var6) {
            GL11.glEnable((449 & 3) >>> 1 >>> 3 >> 3 << 2 ^ 3042);
            int var10000 = 1616 << 3 >>> 1 & 3681 ^ 1441;
            if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                  ;
            }

            GL11.glDisable(var10000);
            if (((1177781104 >>> 2 >> 1 & 71846280) >> 1 >> 1 ^ -1429719031) != 0) {
                  ;
            }

            GL11.glBlendFunc((719 << 3 | 4296) ^ 5626, (410 << 1 >> 1 | 95) & 232 ^ 971);
            if (!"idiot".equals("please dont crack my plugin")) {
                  ;
            }

            GL11.glEnable((866 << 4 & 2014) >> 4 ^ 2880);
            GL11.glPushMatrix();
            float var7 = 0.1F;
            GL11.glScalef(var7, var7, var7);
            double var8 = var0 * (double)(1.0F / var7);
            if ((75502784 >>> 3 >>> 3 ^ -1046150530) != 0) {
                  ;
            }

            var10000 = (int)var8;
            if ((1427260308 << 4 << 2 ^ 1150346496) == 0) {
                  ;
            }

            var0 = (double)var10000;
            if (!"stringer is a good obfuscator".equals("you probably spell youre as your")) {
                  ;
            }

            double var10001 = (double)(1.0F / var7);
            if (((1999950474 >>> 4 & 31028495 & 8376973) >> 4 << 2 ^ 1331200) == 0) {
                  ;
            }

            var2 = (double)((int)(var2 * var10001));
            float var10 = 1.0F / var7;
            if ((314545948 >>> 3 >> 3 ^ 374872831) != 0) {
                  ;
            }

            var4 *= var10;
            Hud.drawCircle(var0, var2, var4, var6);
            Hud.drawUnfilledCircle(var0, var2, var4, 1.0F, var5);
            float var9 = 1.0F / var7;
            if (!"you're dogshit".equals("i hope you catch fire ngl")) {
                  ;
            }

            GL11.glScalef(var9, 1.0F / var7, 1.0F / var7);
            if ((1112347286 << 4 ^ 617687392) == 0) {
                  ;
            }

            GL11.glPopMatrix();
            GL11.glEnable((100 >>> 1 ^ 3) >> 3 ^ 3559);
            var10000 = (613 >> 3 >> 3 ^ 1) << 2 ^ 3010;
            if ((((823777352 ^ 268141553) >> 3 | 41680886) & 46843736 ^ -415248839) != 0) {
                  ;
            }

            GL11.glDisable(var10000);
            GL11.glDisable((1244 | 763) >>> 2 ^ 2719);
      }

      private void onRenderKeyStrokes(RenderGameOverlayEvent var1) {
            if ((((1006524427 | 699818305 | 747792029) ^ 819330661) & 51261490 ^ -1630086015) != 0) {
                  ;
            }

            int[] var10000 = new int[((0 | 741621028) & 424558792 & 103169682) >> 2 ^ 65537];
            var10000[(244711369 ^ 168352304) >> 1 ^ 38703868] = (0 << 4 ^ 1989553702 | 1766970589) ^ 2144861950;
            int[] var2 = var10000;
            Minecraft var7 = this.mc;
            if ((((707799517 | 653431086) & 231349289) >>> 4 << 2 ^ 807883583) != 0) {
                  ;
            }

            GameSettings var8 = var7.gameSettings;
            if ((((543116460 << 3 | 6395606) ^ 47328813) >>> 4 ^ 171421) == 0) {
                  ;
            }

            int var3 = Keyboard.isKeyDown(var8.keyBindForward.getKeyCode()) ? (53 & 40 & 11) << 4 ^ 125 : (24 | 9 | 22) << 1 ^ 12;
            if ((1942704777 << 1 >>> 2 ^ -1202050373) != 0) {
                  ;
            }

            int var4 = Keyboard.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode()) ? 40 >> 1 >>> 1 << 3 ^ 45 : ((35 & 28) >>> 3 ^ 771877481) << 1 ^ 1543754976;
            int var5 = Keyboard.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode()) ? ((24 | 2) & 1) >> 1 >>> 3 ^ 125 : (32 | 7) ^ 31 ^ 10;
            KeyBinding var9 = this.mc.gameSettings.keyBindRight;
            if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            int var6 = Keyboard.isKeyDown(var9.getKeyCode()) ? (45 >> 2 ^ 10) >>> 1 >>> 3 ^ 125 : 49 >>> 4 >> 1 ^ 51;
            if (((1117254113 >> 2 | 30553042) & 242128637 & 5123100 ^ -151317091) != 0) {
                  ;
            }

            Color var10005 = new Color;
            int var10007 = (1762447756 ^ 530744352 ^ 1186773411 | 171863467) ^ 977202607;
            int var10008 = 701875698 << 2 & 1554964039 ^ 67507776;
            if (!"buy a domain and everything else you need at namecheap.com".equals("please dont crack my plugin")) {
                  ;
            }

            var10005.<init>(var10007, var10008, ((901454161 >>> 3 | 88219413) ^ 132135933 | 1394677) ^ 1525751, var4);
            Hud.drawRoundedRect(5.0D, 250.0D, 30.0D, 30.0D, 6.0D, var10005.getRGB());
            Hud.drawRoundedRect(38.0D, 250.0D, 30.0D, 30.0D, 6.0D, (new Color((2089672963 >> 4 & 90212460 | 86749481) >>> 3 ^ 11368229, (13852284 >> 1 ^ 230840) << 3 ^ 55661616, 445128881 >> 4 >> 3 >> 1 ^ 1738784, var5)).getRGB());
            if (((812382060 ^ 539574411 ^ 34373758) << 2 ^ 2018532790) != 0) {
                  ;
            }

            if (!"nefariousMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10005 = new Color;
            if ((67111168 >> 3 ^ 8388896) == 0) {
                  ;
            }

            var10007 = (234444080 >>> 3 | 16960639 | 8314279) ^ 33554431;
            var10008 = 1357692348 << 4 ^ 190401657 ^ 93493177;
            int var10009 = (803066927 | 228989832 | 474803454) >>> 3 ^ 134217727;
            if ((935461833 >> 2 >> 2 >> 1 >> 4 ^ 1827073) == 0) {
                  ;
            }

            var10005.<init>(var10007, var10008, var10009, var6);
            Hud.drawRoundedRect(71.0D, 250.0D, 30.0D, 30.0D, 6.0D, var10005.getRGB());
            Hud.drawRoundedRect(38.0D, 217.0D, 30.0D, 30.0D, 6.0D, (new Color((1401084939 >> 3 | 47280163) ^ 183597987, ((906977780 ^ 828813347 ^ 97504077) & 14994346 ^ 1770686) >>> 4 ^ 782563, 1499613585 >> 4 << 3 ^ 295519018 ^ 1026320354, var3)).getRGB());
            renderer.drawString("W", 42.5F, 219.0F, (new Color(((55 | 50) >> 1 & 24 | 8) ^ 231, (792197236 >>> 3 >> 3 ^ 8787083) >> 4 ^ 240822, 1150663224 << 1 & 998436030 ^ 151151664, 176 >> 4 ^ 2 ^ 246)).getRGB(), (boolean)(0 >>> 1 & 824386265 ^ 1));
            GlyphPageFontRenderer var10 = renderer;
            if (((534084393 ^ 160341072 ^ 307203153 | 54269736) ^ 121405224) == 0) {
                  ;
            }

            var10.drawString("S", 45.0F, 253.0F, (new Color((0 | 469490224) >>> 2 ^ 117372531, 278703725 >> 2 & 65956198 ^ 2500866, (779260809 << 3 | 1641588779) << 3 ^ -1629101224, (90 ^ 30 | 30) ^ 161)).getRGB(), (boolean)((0 >>> 3 & 22482327 ^ 1330420252) >>> 3 ^ 166302530));
            var10 = renderer;
            if ((153117777 >> 2 & 35196451 ^ 34080768) == 0) {
                  ;
            }

            Color var10004 = new Color;
            int var10006 = 55 & 6 & 2 ^ 253;
            var10007 = (1789138496 >>> 1 | 144585561) ^ 1037974393;
            var10008 = 1593161518 >>> 4 << 1 & 13477708 & 11773448 ^ 8431104;
            var10009 = 16 >> 3 << 3 << 2 ^ 191;
            if ((((1980162959 ^ 13121143 | 497585617) & 172108141) << 2 ^ 688399780) == 0) {
                  ;
            }

            var10004.<init>(var10006, var10007, var10008, var10009);
            var10.drawString("A", 12.0F, 253.0F, var10004.getRGB(), (boolean)(0 >>> 3 ^ 2032151323 ^ 520994700 ^ 490472948 ^ 2064748898));
            renderer.drawString("D", 78.0F, 253.0F, (new Color(((209 ^ 189 ^ 27) >>> 4 & 5) << 4 ^ 175, (1266442671 >> 1 | 262315862) >>> 3 ^ 100128762, (50587136 << 4 >> 3 | 18337400) ^ 119000696, 118 >>> 3 << 2 >>> 4 ^ 252)).getRGB(), (boolean)(((0 | 1560358232) >> 1 | 540789081) >>> 4 ^ 49004030));
            int var10001 = (992993499 >>> 4 | 38270342) << 2 >> 2 ^ 66584463;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var2[var10001] += (0 ^ 421006245) << 1 ^ 842012491;
      }

      public static int rainbow(int var0) {
            if (((61203042 >> 2 ^ 5356105 | 10786600) >>> 4 ^ 773503) == 0) {
                  ;
            }

            double var1 = Math.ceil((double)(System.currentTimeMillis() + (long)var0) / 16.0D);
            var1 %= -360.0D;
            if (!"shitted on you harder than archybot".equals("your mom your dad the one you never had")) {
                  ;
            }

            return Color.getHSBColor((float)(var1 / -360.0D), 0.735F, 1.0F).getRGB();
      }

      public void onRenderInventory(RenderGameOverlayEvent var1) {
            ScaledResolution var10000 = new ScaledResolution(this.mc);
            if (((93394172 >> 3 ^ 6590761 ^ 7582860 ^ 1591371) & 3090080 ^ -1965672219) != 0) {
                  ;
            }

            ScaledResolution var2 = var10000;
            FontRenderer var3 = this.mc.fontRenderer;
            EntityPlayerSP var8 = Minecraft.getMinecraft().player;
            if (((1824216242 >>> 3 ^ 48082493) >>> 1 ^ 128279125) == 0) {
                  ;
            }

            NonNullList var4 = var8.inventory.mainInventory;
            int var9 = (8 | 0) >> 3 & 0 ^ 27;
            if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            int var5 = var9;
            int var6 = (0 ^ 1841386950 | 259728816) & 955109193 ^ 686376260;

            while(true) {
                  if (((26756482 >> 2 & 4755042 | 94069) ^ 4288373) == 0) {
                        ;
                  }

                  int var7;
                  ItemStack var10;
                  Point var11;
                  int var10003;
                  if (var6 > (3 & 0 ^ 1131338308 ^ 1131338304)) {
                        var5 = 8 >>> 2 << 1 << 4 ^ 82;
                        if ((326688978 >>> 4 ^ 6166122 ^ -1876725096) != 0) {
                              ;
                        }

                        var6 = (1 & 0) >>> 1 ^ 3;
                        if (!"stringer is a good obfuscator".equals("please go outside")) {
                              ;
                        }

                        while(true) {
                              if ((1605578247 << 4 >>> 3 ^ 526801934) == 0) {
                                    ;
                              }

                              if (var6 > ((1 ^ 0) & 0 ^ 3)) {
                                    if ((39126788 << 4 ^ 620532361 ^ -941586200) != 0) {
                                          ;
                                    }

                                    var5 = ((6 | 0 | 1) >> 1 & 2) << 3 ^ 25;
                                    var6 = (1 & 0) >>> 1 >>> 2 ^ 2;

                                    while(true) {
                                          if (((1129388364 >> 1 << 2 << 4 & 557096794 | 342132793) ^ 879003961) == 0) {
                                                ;
                                          }

                                          if (var6 > (((1 | 0) >> 2 >>> 2 ^ 1162318622) & 675545311 ^ 4456476)) {
                                                return;
                                          }

                                          for(var7 = 1382340874 >> 4 >> 3 ^ 10799538; var7 < (((4 & 3) << 4 | 673302200) & 290999872 ^ 17929); ++var7) {
                                                if ((((1947845099 | 1156686397) & 1070709120 | 111169121) ^ 921825249) == 0) {
                                                      ;
                                                }

                                                Hud.drawRoundedRect((double)(var2.getScaledWidth() - ((145 >> 2 | 8) ^ 158) + var7 * ((3 ^ 2) << 2 ^ 3 ^ 19)), (double)(var2.getScaledHeight() - ((94 & 84 & 83 | 69) & 11 ^ 101) + var6 * ((10 << 3 ^ 47) << 1 & 225 ^ 244)), 16.0D, 16.0D, 8.0D, (new Color(1026301818 >> 4 << 4 << 4 ^ -759040256, (1333577793 ^ 17339582 ^ 166036776) >>> 1 & 27760570 ^ 25199786, 104104981 << 4 << 1 << 4 & 1697712619 ^ 1627392000, (41 | 32 | 21) << 3 >>> 1 ^ 144)).getRGB());
                                                var10 = (ItemStack)var4.get(var5++);
                                                var11 = new Point;
                                                var10003 = var2.getScaledWidth() - ((50 >> 4 << 2 >>> 2 ^ 1) << 1 ^ 182) + var7 * ((19 | 14) << 2 ^ 28 ^ 116);
                                                if (((209539146 << 3 & 900573162 & 293025451) >>> 3 ^ -1157587876) != 0) {
                                                      ;
                                                }

                                                int var10004 = var2.getScaledHeight();
                                                if ((1182371472 >>> 2 ^ 80040064 ^ 358298404) == 0) {
                                                      ;
                                                }

                                                var10004 -= (78 & 15) >> 3 ^ 101;
                                                int var10006 = (11 & 1 & 0) >> 2 ^ 20;
                                                if (((320585582 ^ 100027625 ^ 353794759) >> 3 ^ 465684120) != 0) {
                                                      ;
                                                }

                                                var11.<init>(var10003, var10004 + var6 * var10006);
                                                Hud.renderItem(var10, var11);
                                          }

                                          if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                                                ;
                                          }

                                          ++var6;
                                    }
                              }

                              var7 = 1735714391 << 2 << 4 & 411238534 ^ 402718848;

                              while(true) {
                                    if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                                          ;
                                    }

                                    if (var7 >= ((6 & 0 ^ 1815887752 | 202617141) << 2 ^ -1325465859)) {
                                          ++var6;
                                          break;
                                    }

                                    double var12 = (double)(var2.getScaledWidth() - ((40 >> 3 | 4) >>> 4 >> 2 << 3 ^ 178) + var7 * ((2 << 4 >>> 2 | 5) ^ 25));
                                    double var13 = (double)(var2.getScaledHeight() - (((34 ^ 6) >> 1 | 11) << 4 >>> 3 ^ 82) + var6 * ((5 ^ 2) >>> 4 >>> 4 ^ 20));
                                    Color var10005 = new Color(((2006699166 ^ 1719411954 | 42378532) >> 4 | 8547618) ^ 29261814, 408561350 << 2 << 2 ^ -2052952992, 890781704 >>> 1 << 3 << 1 ^ -1463680960, (88 & 14 & 0) << 1 ^ 100);
                                    if (((34623554 | 19178473) ^ -1166564130) != 0) {
                                          ;
                                    }

                                    Hud.drawRoundedRect(var12, var13, 16.0D, 16.0D, 8.0D, var10005.getRGB());
                                    var10 = (ItemStack)var4.get(var5++);
                                    var11 = new Point;
                                    if (!"intentMoment".equals("idiot")) {
                                          ;
                                    }

                                    var11.<init>(var2.getScaledWidth() - ((79 >>> 1 ^ 1) >> 4 ^ 176) + var7 * (((1 ^ 0) & 0) >>> 3 ^ 20), var2.getScaledHeight() - (((92 | 30) >>> 2 & 7) >>> 4 >>> 2 ^ 100) + var6 * ((19 << 2 ^ 61 | 85) ^ 97));
                                    if (((1172414075 >>> 1 ^ 362227701) >>> 2 >>> 1 ^ 116194585) == 0) {
                                          ;
                                    }

                                    Hud.renderItem(var10, var11);
                                    ++var7;
                              }
                        }
                  }

                  var9 = (484853419 ^ 78142274 | 199678131 | 424830448) >>> 4 ^ 29355999;
                  if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  for(var7 = var9; var7 < (4 & 3 & 1268526081 ^ 9); ++var7) {
                        Hud.drawRoundedRect((double)(var2.getScaledWidth() - ((108 >> 2 & 14) >>> 4 ^ 178) + var7 * (12 << 4 >> 1 ^ 116)), (double)(var2.getScaledHeight() - ((55 ^ 53) >> 3 & 331495933 ^ 100) + var6 * (17 & 4 ^ 1472927996 ^ 1472927976)), 16.0D, 16.0D, 8.0D, (new Color(((1611418556 | 42931761) >> 1 | 178746579) & 667349840 ^ 600240976, ((1517291875 << 3 | 502024313) ^ 1991149473) >> 2 ^ -363809994, (396182288 >>> 2 << 3 | 714064128) & 695520578 ^ 691324160, (56 >>> 1 << 1 | 12) ^ 88)).getRGB());
                        var10 = (ItemStack)var4.get(var5++);
                        var11 = new Point;
                        var10003 = var2.getScaledWidth() - (((108 >>> 3 | 11) >> 3 | 0) ^ 179) + var7 * (8 << 4 << 1 >>> 4 ^ 4);
                        if ((438687588 >> 2 & 12984424 & 2342810 ^ -108475788) != 0) {
                              ;
                        }

                        var11.<init>(var10003, var2.getScaledHeight() - ((29 | 27 | 6) ^ 123) + var6 * ((9 ^ 5 ^ 1) >>> 3 ^ 21));
                        Hud.renderItem(var10, var11);
                  }

                  ++var6;
                  if (((566235464 << 1 & 664674256) << 1 >> 1 ^ -650239163) != 0) {
                        ;
                  }
            }
      }

      public EntityPlayer getClosest() {
            Stream var10000 = this.mc.world.loadedEntityList.stream().filter((var1x) -> {
                  if (((2146904797 << 2 & 774224588 | 405544934) ^ -2091994060) != 0) {
                        ;
                  }

                  EntityPlayerSP var10001 = this.mc.player;
                  if (!"intentMoment".equals("you're dogshit")) {
                        ;
                  }

                  return (boolean)(var1x != var10001 ? (0 ^ 928729204) >> 2 << 1 ^ 464364603 : (1176507184 >>> 1 ^ 129162811 | 70049468) >>> 1 ^ 307720159);
            }).filter((var1x) -> {
                  if (((980112106 ^ 110460691) & 773267180 & 575936268 ^ 538182664) == 0) {
                        ;
                  }

                  return (boolean)(this.mc.player.getDistance(var1x) <= 150.0F ? (0 << 3 >> 4 | 753862870) << 3 ^ 1735935665 : (2090728076 ^ 286091607 ^ 1818793426) << 3 ^ 115140642 ^ 152885354);
            });
            Predicate var10001 = (var0) -> {
                  return (boolean)(!var0.isDead ? (0 >> 1 >> 3 & 790369858) << 1 ^ 1 : (1435732014 >> 2 >>> 2 | 87215096) >>> 3 ^ 11501439);
            };
            if ((1069312 ^ 1069312) == 0) {
                  ;
            }

            List var1 = (List)var10000.filter(var10001).filter((var0) -> {
                  return var0 instanceof EntityPlayer;
            }).sorted(Comparator.comparing((var1x) -> {
                  return Float.valueOf(this.mc.player.getDistance(var1x));
            })).collect(Collectors.toList());
            if ((4120 ^ 4120) == 0) {
                  ;
            }

            int var2 = var1.size();
            if ((414152584 >>> 1 >>> 3 >> 3 & 905373 ^ 86157) == 0) {
                  ;
            }

            return var2 > 0 ? (EntityPlayer)var1.get((183114068 ^ 118555301) >>> 1 >> 1 & 30296033 ^ 21907168) : null;
      }

      static {
            if (((1034746841 >> 2 >> 3 ^ 16653001 ^ 2183232 | 11908559) ^ 28818911) == 0) {
                  ;
            }

            int var10001 = 3 >>> 2 << 3 ^ 19;
            int var10002 = (0 & 1036419180) >>> 4 >>> 3 >>> 2 ^ 1;
            if (((194332140 ^ 103669697) & 192746552 ^ 1801238052) != 0) {
                  ;
            }

            myRenderer = GlyphPageFontRenderer.create("Times New Roman Baltic", var10001, (boolean)var10002, (boolean)((0 | 1109891708 | 20264559) ^ 772744536 ^ 1832426278), (boolean)(0 >>> 3 >>> 3 ^ 282361667 ^ 282361666));
            GlyphPageFontRenderer var10000 = GlyphPageFontRenderer.create("Courier New", 10 << 1 & 15 & 1 & 1037942592 ^ 50, (boolean)(((0 | 1375129406) ^ 1016996413 | 1400771100 | 843392796) ^ 2139094814), (boolean)(((0 & 1232019663) << 2 | 1374224160 | 657633395) ^ 2012938098), (boolean)((0 | 1361521882) << 3 >> 1 >> 2 ^ -249090853));
            if ((((2045834425 | 1186085944) >>> 1 ^ 239404443) >>> 1 ^ 417218019) == 0) {
                  ;
            }

            fuckrenderer = var10000;
            if (((1863265067 | 1194292366) >> 3 ^ 233172981) == 0) {
                  ;
            }

            var10000 = GlyphPageFontRenderer.create("Courier New", (17 ^ 2) >>> 2 ^ 22, (boolean)(((0 >> 4 ^ 520066246 | 421691062) & 198086337) >>> 4 ^ 12380397), (boolean)(0 >>> 3 << 3 >> 2 << 3 ^ 1), (boolean)(0 >>> 4 << 2 >> 1 ^ 1));
            if ((1648437391 >>> 3 ^ 206054673) == 0) {
                  ;
            }

            smallRenderer = var10000;
            if (!"you're dogshit".equals("ape covered in human flesh")) {
                  ;
            }

            var10001 = (7 >> 4 & 517797965) >> 1 ^ 16;
            var10002 = (0 & 1274521256) >> 2 & 2052991981 ^ 1;
            int var10003 = 0 << 4 << 4 >> 3 & 830814094 ^ 1;
            int var10004 = 0 >>> 4 >>> 2 ^ 1;
            if (((1456968905 ^ 916929403 | 318149385) ^ 1928773563) == 0) {
                  ;
            }

            targetRenderer = GlyphPageFontRenderer.create("Courier New", var10001, (boolean)var10002, (boolean)var10003, (boolean)var10004);
            hitStr = GlyphPageFontRenderer.create("Comic Sans MS", 6 & 5 ^ 0 ^ 28, (boolean)((0 | 1002723673) << 4 ^ -1136290415), (boolean)((0 & 1365763382 ^ 648285213 | 249229310) ^ 788459518), (boolean)((0 << 1 | 616199699) >>> 2 >> 2 ^ 696726 ^ 37816054));
      }

      @SubscribeEvent
      public void renderOverlay(RenderGameOverlayEvent var1) {
            ScaledResolution var2 = new ScaledResolution(this.mc);
            if ((746934700 << 1 & 613475023 ^ 'ꉈ') == 0) {
                  ;
            }

            FontRenderer var3 = this.mc.fontRenderer;
            if ((136658946 >> 4 << 2 ^ 575399168) != 0) {
                  ;
            }

            if (ModuleManager.getModuleByName("HUD").isToggled()) {
                  ElementType var10000 = var1.getType();
                  ElementType var10001 = ElementType.TEXT;
                  if (((410315317 | 271380782) << 3 >> 1 ^ 1726603687 ^ -975371882) != 0) {
                        ;
                  }

                  if (var10000 == var10001) {
                        int[] var12 = new int[(0 ^ 481176690) >>> 2 >> 1 << 1 << 4 ^ 1924706753];
                        int var10002 = (1591541928 >> 4 | 25334488) ^ 99603162;
                        int var10003 = 0 >> 3 ^ 1992898661 ^ 1088154891 ^ 907203951;
                        if (!"ape covered in human flesh".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var12[var10002] = var10003;
                        int[] var4 = var12;
                        if ((1106443 << 1 >>> 2 >>> 4 ^ '蜐') == 0) {
                              ;
                        }

                        int var5 = 241455320 >>> 1 << 4 ^ 1931642560;
                        if (ModuleManager.getModuleByName("KeyStrokes").isToggled()) {
                              this.onRenderKeyStrokes(var1);
                        }

                        if (((1206190701 | 592519008) >> 2 ^ 1424119357) != 0) {
                              ;
                        }

                        if (ModuleManager.getModuleByName("InventoryView").isToggled()) {
                              if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              this.onRenderInventory(var1);
                        }

                        boolean var13 = ModuleManager.getModuleByName("ArmorView").isToggled();
                        if (((67404323 | 15386332) ^ 70561379 ^ 14313628) == 0) {
                              ;
                        }

                        if (var13) {
                              this.onRenderArmor(var1);
                        }

                        if (((1781537053 >>> 1 | 872353313) >> 4 ^ 58716394) == 0) {
                              ;
                        }

                        if (ModuleManager.getModuleByName("TargetHUD").isToggled()) {
                              this.onRenderTargetHUD(var1);
                        }

                        GlyphPageFontRenderer var14 = fuckrenderer;
                        if (!"please take a shower".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        int var10004 = var4[(1784689724 ^ 946286063 | 972548995) >> 2 ^ 519962356] * (110 >> 4 >> 1 ^ 303);
                        if (((2114201873 << 2 ^ 1059517949) >> 3 ^ 1081261693) != 0) {
                              ;
                        }

                        var14.drawString("crackedByNoom", 5.0F, 5.0F, Hud.rainbow(var10004), (boolean)((0 >>> 1 & 730825074) >>> 4 ^ 1));
                        var14 = smallRenderer;
                        Color var24 = new Color;
                        if ((18223132 ^ 8179349 ^ 6336090 ^ 2809866 ^ 210164115) != 0) {
                              ;
                        }

                        var24.<init>((247 & 136 ^ 33 ^ 30) & 106 & 33 ^ 223, 453315088 >>> 4 ^ 28332193, 1491729 ^ 413821 ^ 1086316, (23 | 7 | 5) ^ 232);
                        var14.drawString(" ", 66.0F, 7.0F, var24.getRGB(), (boolean)(224473569 >>> 3 << 2 ^ 58430175 ^ 97192495));
                        if (!"you probably spell youre as your".equals("please go outside")) {
                              ;
                        }

                        List var16 = ModuleManager.getSortedModules();
                        if ((((450548757 | 411482971) ^ 364178145) >> 3 ^ 470195145) != 0) {
                              ;
                        }

                        Iterator var6 = var16.iterator();
                        if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        while(true) {
                              Module var7;
                              do {
                                    do {
                                          do {
                                                do {
                                                      if (!var6.hasNext()) {
                                                            return;
                                                      }

                                                      if ("yo mama name maurice".equals("ape covered in human flesh")) {
                                                      }

                                                      Module var17 = (Module)var6.next();
                                                      if (!"stop skidding".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var7 = var17;
                                                } while(!var7.isToggled());
                                          } while(var7.getName().equals("TabGUI"));
                                    } while(var7.getName().equals("Gui"));
                              } while(var7.getName().equals("Config"));

                              this.toAdd = "";
                              Iterator var8 = var7.settings.iterator();

                              while(true) {
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }

                                    if (!var8.hasNext()) {
                                          break;
                                    }

                                    Setting var9 = (Setting)var8.next();
                                    if (var9 instanceof ModeSetting) {
                                          ModeSetting var10 = (ModeSetting)var9;
                                          this.toAdd = var10.activeMode;
                                          if ((2056958211 << 3 << 2 & 547443413 ^ 64) == 0) {
                                                ;
                                          }
                                          break;
                                    }

                                    if (((82021641 >>> 1 >> 2 << 3 | 13126864) ^ 381156431) != 0) {
                                          ;
                                    }

                                    if ((((51438267 | 48531393) ^ 875378) >> 4 ^ 87986400) != 0) {
                                          ;
                                    }
                              }

                              double var18 = (double)(var5 * (var3.FONT_HEIGHT + (((4 ^ 1) & 3 | 0) ^ 4)));
                              if ((8915014 >>> 3 >>> 1 & 347525 ^ 132) == 0) {
                                    ;
                              }

                              double var11 = var18;
                              if ((((1515337060 | 1349314261) & 470687253 | 354723809) ^ 489601013) == 0) {
                                    ;
                              }

                              String var20 = this.toAdd;
                              if (((202505270 >>> 3 & 6146007 ^ 1262) >>> 2 ^ 2058207713) != 0) {
                                    ;
                              }

                              int var15;
                              int var21;
                              if (var20.equalsIgnoreCase("")) {
                                    var21 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name) - ((0 ^ 1449199371) >> 2 >> 2 ^ 90574966);
                                    var15 = (int)var11;
                                    var10002 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name);
                                    var10003 = ((6 | 5) ^ 4 | 2) ^ 0 ^ 11;
                                    if (((1025984682 ^ 474920769) << 3 >>> 1 ^ 1271064104) != 0) {
                                          ;
                                    }

                                    var10002 -= var10003;
                                    var10003 = (int)var11 + (((3 | 1) >> 2 ^ 1659565126 | 647673015) ^ 1727722738) + var3.FONT_HEIGHT;
                                    if ((724812242 >>> 3 & 71663537 ^ -2025736737) != 0) {
                                          ;
                                    }

                                    var10004 = var4[(1997218769 << 3 & 1904220651) >>> 2 & 62837397 ^ 427 ^ 939];
                                    int var10005 = (33 & 31) >> 3 & 788605373 ^ 300;
                                    if ((4128 ^ 1127736777) != 0) {
                                          ;
                                    }

                                    Hud.drawRect(var21, var15, var10002, var10003, Hud.rainbow(var10004 * var10005));
                                    var21 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name) - ((3 & 0) >>> 4 ^ 1768210724 ^ 1768210722);
                                    var15 = (int)var11;
                                    var10002 = var2.getScaledWidth();
                                    if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                                          ;
                                    }

                                    var10003 = (int)var11;
                                    if (((1275603337 | 589588481) & 1514085686 ^ -800507612) != 0) {
                                          ;
                                    }

                                    Hud.drawRect(var21, var15, var10002, var10003 + ((4 ^ 0 ^ 3) << 3 ^ 61) + var3.FONT_HEIGHT, (new Color(670174658 >> 1 >> 1 >>> 3 ^ 20942958, (333552749 >> 2 >> 3 ^ 8517100) >> 2 ^ 507459, (155707887 | 930888 | 91915522) >>> 1 ^ 113245943, (48 >>> 1 | 23) ^ 84)).getRGB());
                                    myRenderer.drawString(var7.getName(), (float)(var2.getScaledWidth() - myRenderer.getStringWidth(var7.getName()) - (0 << 2 >>> 1 & 1096126195 ^ 4)), (float)(1.0D + var11), Hud.rainbow(var4[(58851865 | 53886219) >>> 3 ^ 7784419] * ((55 & 28 ^ 7) >> 3 ^ 302)), (boolean)(((0 >>> 2 ^ 2108699434) << 1 & 269661742) >> 2 ^ 67111296));
                              } else {
                                    var21 = var2.getScaledWidth() - myRenderer.getStringWidth((new StringBuilder()).append(var7.name).append(" ").append(this.toAdd).append("").toString()) - ((4 << 3 | 19) >> 2 << 3 ^ 102);
                                    if (((1795582788 << 2 | 1995537992) ^ -17170600) == 0) {
                                          ;
                                    }

                                    var15 = (int)var11;
                                    var10002 = var2.getScaledWidth();
                                    GlyphPageFontRenderer var25 = myRenderer;
                                    StringBuilder var26 = (new StringBuilder()).append(var7.name);
                                    if (((1988779945 ^ 442917887) >>> 1 & 174555870 ^ 40239626) == 0) {
                                          ;
                                    }

                                    var10002 = var10002 - var25.getStringWidth(var26.append(" ").append(this.toAdd).append("").toString()) - ((7 ^ 0 | 2) >> 1 << 2 ^ 4);
                                    var10003 = (int)var11;
                                    if (((1722619170 | 1071887925) >>> 1 >> 3 ^ 134151027) == 0) {
                                          ;
                                    }

                                    var10003 += (((3 >> 2 | 1331967426) ^ 173199442) << 3 ^ 699870341) + var3.FONT_HEIGHT;
                                    var10004 = var4[((318846967 | 41725492) ^ 52849043) >> 3 & 9773238 ^ 73860];
                                    if (!"please take a shower".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var10004 = Hud.rainbow(var10004 * ((78 << 3 & 2 & 1270288746) >> 3 ^ 1450005393 ^ 1450005181));
                                    if (((1525996775 << 1 & 1767524732) >> 3 ^ 69796137) == 0) {
                                          ;
                                    }

                                    Hud.drawRect(var21, var15, var10002, var10003, var10004);
                                    if ((544911244 << 2 >>> 3 >>> 4 & 5480805 ^ 229732) == 0) {
                                          ;
                                    }

                                    var21 = var2.getScaledWidth();
                                    GlyphPageFontRenderer var19 = myRenderer;
                                    String var23 = (new StringBuilder()).append(var7.name).append(" ").append(this.toAdd).append("").toString();
                                    if ((((720626599 | 311271740) ^ 953869976) >> 1 ^ 17990291) == 0) {
                                          ;
                                    }

                                    var15 = var19.getStringWidth(var23);
                                    if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    var21 = var21 - var15 - (4 >>> 2 & 0 ^ 6);
                                    var15 = (int)var11;
                                    if (((1032654062 ^ 632055495) >>> 3 & 18872316 ^ 17048900) == 0) {
                                          ;
                                    }

                                    Hud.drawRect(var21, var15, var2.getScaledWidth(), (int)var11 + (0 >> 2 >> 1 ^ 5) + var3.FONT_HEIGHT, (new Color(((1028608059 | 364268481) & 850035849) >>> 4 ^ 23911405 ^ 40270821, ((1048131395 | 180249612) >> 1 ^ 338090635) >>> 1 ^ 95203478, 537138714 << 3 << 1 & 2636962 & 8183 ^ 160, ((54 | 9) >> 2 & 0) >>> 2 ^ 75)).getRGB());
                                    var14 = myRenderer;
                                    String var22 = var7.getName();
                                    var10002 = var2.getScaledWidth();
                                    var25 = myRenderer;
                                    var26 = new StringBuilder;
                                    if ((135404704 ^ 54166751 ^ 187209855) == 0) {
                                          ;
                                    }

                                    var26.<init>();
                                    var26 = var26.append(var7.getName());
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    var26 = var26.append(" ").append(this.toAdd);
                                    if (((230311111 | 38870434) >>> 1 >> 3 << 4 ^ 6951981) != 0) {
                                          ;
                                    }

                                    var10002 -= var25.getStringWidth(var26.append("").toString());
                                    if (!"shitted on you harder than archybot".equals("idiot")) {
                                          ;
                                    }

                                    var14.drawString(var22, (float)(var10002 - ((1 ^ 0 ^ 0) >> 1 ^ 4)), (float)(1.0D + var11), Hud.rainbow(var4[2116930454 << 2 >> 4 & 1281883823 ^ 1275294373] * ((181 ^ 35) >>> 3 << 4 & 250 ^ 268)), (boolean)(((0 & 902661377) >>> 1 & 274659188 ^ 813952532) >> 3 ^ 101744067));
                                    var14 = myRenderer;
                                    var22 = (new StringBuilder()).append(" ").append(this.toAdd).append("").toString();
                                    float var27 = (float)(var2.getScaledWidth() - myRenderer.getStringWidth((new StringBuilder()).append(" ").append(this.toAdd).append("").toString()) - (3 << 3 >> 2 ^ 2));
                                    float var28 = (float)(1.0D + var11);
                                    var24 = new Color;
                                    if ((155035191 << 2 ^ 360492505 ^ -1428484644) != 0) {
                                          ;
                                    }

                                    int var10006 = 147 << 3 >> 3 >> 3 ^ 9 ^ 128;
                                    int var10007 = 139 & 129 & 5 ^ 154;
                                    if (((1108036881 >>> 4 | 3181509) << 3 ^ 283345078 ^ -323912655) != 0) {
                                          ;
                                    }

                                    var24.<init>(var10006, var10007, (94 >>> 1 | 39) ^ 180, ((79 << 4 | 586) & 973) >>> 2 ^ 77);
                                    var14.drawString(var22, var27, var28, var24.getRGB(), (boolean)(((0 | 112143088) >> 4 & 6861670 | 6833040) ^ 6878135));
                              }

                              if (((844499611 >>> 2 & 40055541 ^ '렁') & 'ꦩ' ^ 25777 ^ 1074613279) != 0) {
                                    ;
                              }

                              var4[(667417039 | 233236820) << 4 >> 4 ^ -1574433] += (0 | 879106600) >> 1 ^ 420580692 ^ 18523819 ^ 37234410;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              ++var5;
                        }
                  }
            }

      }

      public void onRenderTargetHUD(RenderGameOverlayEvent var1) {
            if (this.mc.player != null && !this.mc.player.isDead) {
                  Stream var10000 = this.mc.world.loadedEntityList.stream().filter((var1x) -> {
                        return (boolean)(var1x != this.mc.player ? (0 >> 2 & 2052325643 | 2005189726) >>> 4 ^ 125324356 : (2029011682 >> 4 << 4 ^ 1763433661) & 6369723 ^ 6361113);
                  });
                  Predicate var10001 = (var1x) -> {
                        EntityPlayerSP var10000 = this.mc.player;
                        if ((245168448 << 2 >>> 2 ^ 1861789445) != 0) {
                              ;
                        }

                        return (boolean)(var10000.getDistance(var1x) <= 5.0F ? (0 >> 4 >> 4 | 1285973789) >> 4 ^ 80373360 : (1434925798 >> 1 | 633872696) ^ 801881467);
                  };
                  if (!"intentMoment".equals("nefariousMoment")) {
                        ;
                  }

                  var10000 = var10000.filter(var10001).filter((var0) -> {
                        return (boolean)(!var0.isDead ? (0 ^ 625028871) >> 3 >> 2 ^ 19532153 : (209131814 << 4 >>> 4 ^ 154617316 | 36849258) << 1 ^ 249871828);
                  });
                  var10001 = (var0) -> {
                        return var0 instanceof EntityPlayer;
                  };
                  if (((342609937 << 2 | 1102566478 | 138985887) & 715807964 ^ 145380572) == 0) {
                        ;
                  }

                  List var2 = (List)var10000.filter(var10001).filter((var0) -> {
                        int var10000;
                        if (var0.ticksExisted > (13 >>> 2 >>> 3 ^ 30)) {
                              var10000 = 0 >>> 1 ^ 904533392 ^ 904533393;
                        } else {
                              if ((172514151 >> 3 >> 4 >> 3 ^ 316355503) != 0) {
                                    ;
                              }

                              var10000 = 1634153117 << 1 >> 2 ^ -256665266;
                        }

                        return (boolean)var10000;
                  }).filter((var0) -> {
                        int var10000;
                        if (!var0.isInvisible()) {
                              var10000 = (0 | 2076693549 | 1489468948) ^ 2076704316;
                        } else {
                              var10000 = (983914568 >> 1 >> 4 >>> 4 | 241142 | 1161395) ^ 2097151;
                              if ((598051800 >>> 3 ^ 51684832 ^ -1607976181) != 0) {
                                    ;
                              }
                        }

                        return (boolean)var10000;
                  }).sorted(Comparator.comparing((var1x) -> {
                        if (((482227163 >>> 3 << 1 | 99718040) ^ 134193150) == 0) {
                              ;
                        }

                        return Float.valueOf(this.mc.player.getDistance(var1x));
                  })).collect(Collectors.toList());
                  if (var2.size() > 0) {
                        if ((522976498 >>> 1 & 120110951 & 1071595 ^ 4569 ^ 2488) == 0) {
                              ;
                        }

                        if ((50349192 << 2 ^ 201396768) == 0) {
                              ;
                        }

                        if (ModuleManager.getModuleByName("TargetHUD").isToggled()) {
                              ScaledResolution var6 = new ScaledResolution;
                              if (((1270275954 ^ 1244907523) & 16192671 & 6663870 ^ 328720) == 0) {
                                    ;
                              }

                              Minecraft var10002 = this.mc;
                              if (((1367105397 | 1287621677) >>> 3 << 2 & 296624643 ^ 11411968) == 0) {
                                    ;
                              }

                              var6.<init>(var10002);
                              ScaledResolution var3 = var6;
                              int var4 = (int)(((EntityPlayer)var2.get((639257351 | 445096719) >>> 1 ^ 54971629 ^ 330485529 ^ 263976563)).getHealth() * 5.0F + ((EntityPlayer)var2.get(((1138805750 | 83589322) ^ 685842257) >> 4 << 3 ^ 932016720)).getAbsorptionAmount() * 5.0F);
                              Color var7 = new Color;
                              int var11 = 55 << 3 << 1 >> 2 ^ 2;
                              int var10003 = 100 >>> 1 ^ 3 ^ 239;
                              if ((440505960 >>> 4 & 1340731 ^ 266530) == 0) {
                                    ;
                              }

                              var7.<init>(var11, var10003, (9 & 3) << 4 >>> 4 >>> 2 ^ 222, 109 << 1 >> 3 ^ 228);
                              if (!"i hope you catch fire ngl".equals("stop skidding")) {
                                    ;
                              }

                              int var5 = var7.getRGB();
                              if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                                    ;
                              }

                              if (var4 >= ((21 | 7 | 18) ^ 10 ^ 71)) {
                                    var7 = new Color;
                                    if (((201855280 | 74185143) >>> 3 >> 1 ^ 1380940587) != 0) {
                                          ;
                                    }

                                    var7.<init>((1169174844 ^ 516343399) >> 4 ^ 95907701, (7 & 2 ^ 1) << 4 >>> 1 >>> 3 ^ 252, 1436970660 << 1 & 550966017 ^ 541331712, 120 >>> 2 ^ 25 ^ 6 ^ 254);
                                    var5 = var7.getRGB();
                              }

                              if (((1758199992 ^ 1268260318 | 74153555) << 3 >>> 2 ^ 251592430) == 0) {
                                    ;
                              }

                              if (var4 >= (((39 & 16) >> 4 & 257395140) << 1 ^ 50) && var4 < (((4 & 1) >> 4 >> 2 | 1111264161) ^ 1111264251)) {
                                    var5 = (new Color(18 & 13 ^ 2058147286 ^ 1952069000 ^ 251060421, (24 << 3 | 190) ^ 101, (130 ^ 95) & 1 ^ 224, (209 & 75 | 54) & 95 ^ 35 ^ 139)).getRGB();
                              }

                              if (var4 > ((12 & 2 ^ 1841275083) >> 1 >> 4 << 4 ^ 920637556)) {
                                    if (!"shitted on you harder than archybot".equals("stop skidding")) {
                                          ;
                                    }

                                    if (var4 < ((38 ^ 32) >>> 3 >> 2 ^ 50)) {
                                          var5 = (new Color(104 << 4 >>> 2 ^ 321, 45 >> 2 >> 2 ^ 153, (7 & 2) >>> 2 ^ 15, (39 & 25 & 0) << 2 >>> 1 ^ 255)).getRGB();
                                    }
                              }

                              if (var4 <= (15 >>> 4 ^ 790887141 ^ 790887153)) {
                                    if ((857454374 << 3 >> 1 << 3 ^ 1415378073) != 0) {
                                          ;
                                    }

                                    var7 = new Color;
                                    if (((1073536072 | 313002893) >> 4 & 33920425 ^ 1493200836) != 0) {
                                          ;
                                    }

                                    var7.<init>((57 & 33 | 11) ^ 212, (1468497159 << 2 & 507027287) >>> 3 & 61308107 ^ 58921090, (624214325 ^ 373340014) << 2 >> 1 ^ -420942666, ((175 ^ 140) & 31 ^ 0) >> 2 & 360706658 ^ 255);
                                    if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var5 = var7.getRGB();
                                    if (((2065237134 | 1049030362) & 880880650 ^ -2031827864) != 0) {
                                          ;
                                    }
                              }

                              if (((841203360 >> 3 | 17847931) ^ 96153019 ^ 49240644) == 0) {
                                    ;
                              }

                              double var9 = (double)(var3.getScaledWidth() / (1 << 2 << 4 ^ 52 ^ 118) - ((4 << 1 >> 2 | 1) ^ 52));
                              double var8 = (double)(var3.getScaledHeight() / ((1 << 3 & 7) >>> 4 ^ 2) - ((10 & 5 & 1755243875) << 4 ^ 55));
                              Color var10005 = new Color((2059093642 >> 3 | 238344192) >> 3 ^ 32440090, 1849260534 << 1 << 1 ^ -1192892456, 1940227261 << 2 >> 2 >>> 3 ^ 510963863, 19 << 1 >>> 3 & 1 ^ 75);
                              if (!"your mom your dad the one you never had".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              Hud.drawRoundedRect(var9, var8, 110.0D, 50.0D, 6.0D, var10005.getRGB());
                              int var12 = var3.getScaledWidth();
                              int var10 = 0 >>> 2 >>> 3 ^ 2;
                              if ((((1857961845 ^ 951449790 ^ 365126137) >>> 3 | 121826321) ^ 259783895) == 0) {
                                    ;
                              }

                              var9 = (double)(var12 / var10 - (28 << 2 << 3 ^ 946));
                              var8 = (double)(var3.getScaledHeight() / ((0 >>> 3 ^ 1973902174 | 165223570) ^ 2113888220) - (30 >> 1 << 4 & 7 ^ 46));
                              if (((1758586623 >> 1 ^ 526119991) >> 2 ^ 181191122) == 0) {
                                    ;
                              }

                              float var13 = ((EntityPlayer)var2.get(2034807905 >>> 4 >> 4 ^ 7948468)).getHealth();
                              if (((304110049 | 1151113) >> 2 ^ 1020903150) != 0) {
                                    ;
                              }

                              Hud.drawRoundedRect(var9, var8, (double)(var13 * 5.0F), 2.0D, 3.0D, var5);
                              targetRenderer.drawString((new StringBuilder()).append("Health:§c§l").append(var4 / (((0 | 1751970044 | 681260595) ^ 969326129) & 547170168 ^ 1638477)).append("§c").toString(), (float)(var3.getScaledWidth() / (((0 | 1349565208) ^ 1337209588) << 2 << 1 ^ -31174814) - ((39 | 24) >> 1 ^ 45)), (float)(var3.getScaledHeight() / (1 << 2 >> 1 ^ 0) - (((34 ^ 30 | 9) >>> 4 & 2) >> 4 ^ 40)), (new Color(((214 & 126) >>> 2 ^ 3 | 2) ^ 247, (212 >>> 1 ^ 11) & 68 ^ 161, 100 >> 4 >>> 3 << 3 ^ 225, (224 ^ 75) >>> 3 >>> 2 ^ 250)).getRGB(), (boolean)((0 & 74923384) >>> 3 << 1 ^ 1));
                              GlyphPageFontRenderer var15 = targetRenderer;
                              StringBuilder var14 = (new StringBuilder()).append("Dist:§c§l");
                              DecimalFormat var16 = this.df;
                              if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                    ;
                              }

                              EntityPlayerSP var17 = this.mc.player;
                              int var21 = ((2132240738 ^ 379346278) << 4 & 1673424747) >> 4 << 1 ^ 1460232;
                              if (!"stop skidding".equals("ape covered in human flesh")) {
                                    ;
                              }

                              Object var10004 = var2.get(var21);
                              if ((775947269 >> 2 & 876264 ^ 1085297785 ^ 1085297785) == 0) {
                                    ;
                              }

                              var14 = var14.append(var16.format((double)var17.getDistance((Entity)var10004)));
                              if ((272990260 ^ 268207626 ^ 30882598 ^ -1497970419) != 0) {
                                    ;
                              }

                              var15.drawString(var14.append("§c").toString(), (float)(var3.getScaledWidth() / ((((1 ^ 0) & 0) << 2 | 579518076) ^ 579518078)), (float)(var3.getScaledHeight() / (0 << 4 & 1987888845 ^ 2) - ((39 << 2 & 4 | 3) ^ 47)), (new Color((148 << 2 >> 4 >> 2 ^ 8) << 2 ^ 229, ((167 & 156) >>> 2 | 21) ^ 212, (147 & 49 | 2) & 15 & 0 ^ 225, ((220 ^ 74) & 25 | 9) >> 4 ^ 254)).getRGB(), (boolean)((0 << 1 << 3 | 606366690) ^ 606366691));
                              var15 = targetRenderer;
                              String var18 = ((Entity)var2.get(((156734601 | 15920249) & 54931818) >> 3 ^ 2672141)).getName();
                              var11 = var3.getScaledWidth() / (((1 >>> 3 | 920409007) ^ 264192422) >>> 2 & 223273519 ^ 206094336);
                              if (((1121042021 >>> 2 >>> 4 ^ 9639402 | 22194834) ^ 31128467) == 0) {
                                    ;
                              }

                              var13 = (float)(var11 - targetRenderer.getStringWidth(((Entity)var2.get(589962753 >>> 1 ^ 259812904 ^ 518611240)).getName()) / (((1 | 0) ^ 0) << 2 ^ 6));
                              float var19 = (float)(var3.getScaledHeight() / (0 >> 4 >> 2 >> 3 ^ 2) - ((4 >>> 2 | 0 | 0) ^ 0 ^ 24));
                              Color var20 = new Color;
                              int var10006 = ((55 >> 2 & 8) >> 2 | 1) ^ 226;
                              if (!"idiot".equals("shitted on you harder than archybot")) {
                                    ;
                              }

                              int var10007 = (((220 | 160) ^ 212) & 2) << 4 ^ 225;
                              int var10008 = (163 & 135 & 115 ^ 2) >> 2 ^ 225;
                              if (((1302539773 ^ 800266813) >>> 2 & 29026060 ^ 1641278937) != 0) {
                                    ;
                              }

                              var20.<init>(var10006, var10007, var10008, ((63 ^ 52 | 9) ^ 5) >> 3 ^ 254);
                              var15.drawString(var18, var13, var19, var20.getRGB(), (boolean)(0 << 1 >>> 4 ^ 1));
                        }
                  }

            }
      }

      public static void drawRoundedRect(double var0, double var2, double var4, double var6, double var8, int var10) {
            double var11 = var0 + var4;
            if ((949912257 << 2 << 4 << 3 >>> 2 ^ 1893675075) != 0) {
                  ;
            }

            double var13 = var2 + var6;
            float var15 = (float)(var10 >> (((21 ^ 9) & 2) >>> 4 ^ 24) & ((156 & 17 | 8) ^ 231)) / 255.0F;
            int var10000 = var10 >> (0 << 2 << 1 & 1383936026 ^ 16);
            if (!"stop skidding".equals("your mom your dad the one you never had")) {
                  ;
            }

            int var10001 = 200 >> 1 & 60 ^ 219;
            if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var10000 &= var10001;
            if (((484484964 | 332626429 | 247829620) ^ 536346621) == 0) {
                  ;
            }

            float var16 = (float)var10000 / 255.0F;
            var10000 = var10 >> ((7 << 2 & 2) << 3 & 1326495317 ^ 8) & ((((184 | 41) ^ 6) << 4 | 1108) ^ 3851);
            if (((10274933 ^ 5494948) >> 2 >> 2 & 695375 ^ 461202739) != 0) {
                  ;
            }

            float var17 = (float)var10000 / 255.0F;
            float var18 = (float)(var10 & ((214 ^ 38 | 227) >>> 3 ^ 225)) / 255.0F;
            GL11.glPushAttrib((137281668 << 1 << 4 | 25655356) << 3 ^ 788252128);
            if (!"please take a shower".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glScaled(0.5D, 0.5D, 0.5D);
            if (!"i hope you catch fire ngl".equals("nefariousMoment")) {
                  ;
            }

            var0 *= 2.0D;
            var2 *= 2.0D;
            if (((2056434700 ^ 1158914411 ^ 459061581) & 298766026 ^ 4479331 ^ 8967529) == 0) {
                  ;
            }

            var11 *= 2.0D;
            var13 *= 2.0D;
            Tessellator var19 = Tessellator.getInstance();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color(var15, var16, var17, var18);
            GL11.glDisable(1615 >>> 1 >>> 2 >> 2 ^ 3539);
            if (((425048351 >>> 2 >> 1 | 45428510) >> 3 ^ 7862247) == 0) {
                  ;
            }

            GL11.glColor4f(var16, var17, var18, var15);
            GL11.glEnable((2838 & 1093 | 0) >>> 4 >>> 3 ^ 2848);
            GL11.glBegin((2 & 0 | 1618209029) ^ 1099343770 ^ 569724566);
            int var20 = (1482453660 ^ 685851225) >> 1 ^ 945723490;

            while(true) {
                  if ((((1213064186 ^ 689912571) & 998073822) >> 3 >>> 4 ^ 2732188 ^ 7016478) == 0) {
                        ;
                  }

                  if ((282185984 << 4 ^ 203888646 ^ -136975743) != 0) {
                        ;
                  }

                  double var10002;
                  double var21;
                  double var22;
                  if (var20 > (((85 | 64) >>> 4 | 3) >>> 1 ^ 89)) {
                        for(var20 = ((5 | 3) >> 3 | 1259391672) ^ 1259391714; var20 <= ((66 << 4 << 3 | 3446) >>> 3 ^ 1306); var20 += 3) {
                              if ((((976227563 | 626089062) & 581015339 | 319423174) ^ -1922128837) != 0) {
                                    ;
                              }

                              var21 = var0 + var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D;
                              var22 = var13 - var8;
                              var10002 = (double)var20;
                              if (((707797200 >> 3 ^ 17657474) >>> 1 ^ 1648122291) != 0) {
                                    ;
                              }

                              var10002 = Math.cos(var10002 * 3.141592653589793D / 180.0D);
                              double var10003 = var8 * -1.0D;
                              if (((1826758613 << 4 | 495105157) >> 4 ^ -33931299) == 0) {
                                    ;
                              }

                              GL11.glVertex2d(var21, var22 + var10002 * var10003);
                        }

                        for(var20 = 1340173644 >>> 4 >>> 1 & 30859034 ^ 5638922; var20 <= (33 >>> 1 >> 3 ^ 88); var20 += 3) {
                              var21 = var11 - var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8;
                              if ((2005318196 ^ 1202951409 ^ 313951718 ^ 579054371) == 0) {
                                    ;
                              }

                              var22 = var13 - var8;
                              var10002 = (double)var20 * 3.141592653589793D;
                              if ((50355712 >>> 2 >>> 3 ^ 1722444302) != 0) {
                                    ;
                              }

                              GL11.glVertex2d(var21, var22 + Math.cos(var10002 / 180.0D) * var8);
                        }

                        for(var20 = 43 >> 3 << 3 << 3 ^ 282; var20 <= ((175 & 89 ^ 6) >>> 2 ^ 183); var20 += 3) {
                              if (((1202509422 | 843598275) >>> 4 ^ 125750782) == 0) {
                                    ;
                              }

                              var21 = var11 - var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8;
                              if (((965255339 | 386503765 | 308424562) >>> 3 ^ -1180574757) != 0) {
                                    ;
                              }

                              GL11.glVertex2d(var21, var2 + var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8);
                        }

                        if ((1374110903 >>> 1 >>> 1 ^ 343527725) == 0) {
                              ;
                        }

                        GL11.glEnd();
                        GL11.glEnable((3365 & 840 | 227) ^ 28 ^ 3102);
                        var10000 = (649 >>> 2 ^ 161) & 0 ^ 2848;
                        if (((151126626 | 88192188) ^ 32473535 ^ 96003898 ^ 1783910072) != 0) {
                              ;
                        }

                        GL11.glDisable(var10000);
                        GL11.glEnable((3272 | 121) >>> 3 & 183 ^ 3446);
                        GlStateManager.enableTexture2D();
                        GlStateManager.disableBlend();
                        if ((1666654468 >>> 4 >> 4 >>> 3 & 377702 ^ -1799822286) != 0) {
                              ;
                        }

                        GL11.glScaled(2.0D, 2.0D, 2.0D);
                        GL11.glPopAttrib();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        return;
                  }

                  var21 = var0 + var8;
                  var22 = (double)var20 * 3.141592653589793D;
                  if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var21 += Math.sin(var22 / 180.0D) * var8 * -1.0D;
                  var22 = var2 + var8;
                  var10002 = (double)var20;
                  if (((856375857 ^ 540809723 ^ 170382277) & 103635429 ^ 1490 ^ -151100498) != 0) {
                        ;
                  }

                  if ((18526036 ^ 4654662 ^ 7684438 ^ 6941706 ^ 16425798) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var21, var22 + Math.cos(var10002 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
                  var20 += 3;
            }
      }

      public void onRenderArmor(RenderGameOverlayEvent var1) {
            ScaledResolution var2 = new ScaledResolution(this.mc);
            FontRenderer var3 = this.mc.fontRenderer;
            NonNullList var4 = Minecraft.getMinecraft().player.inventory.armorInventory;
            int var5 = (0 >>> 4 | 999884840) ^ 999884843;
            int var6 = 1073084385 << 4 & 281351928 & 226572347 ^ 69648;

            while(true) {
                  if ("stop skidding".equals("i hope you catch fire ngl")) {
                  }

                  if (var6 >= var4.size()) {
                        return;
                  }

                  if (((2055537654 | 1932354389) & 1361434332 ^ 1181033344 ^ 390095188) == 0) {
                        ;
                  }

                  ItemStack var10000 = (ItemStack)var4.get(var5--);
                  Point var7 = new Point;
                  int var10003 = var2.getScaledWidth();
                  int var10004 = 0 << 3 >>> 1 >>> 2 >>> 3 ^ 2;
                  if (!"you probably spell youre as your".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  var10003 = var10003 / var10004 + var5 * (10 & 6 & 1 & 1580484132 ^ 18) + ((3 >> 3 | 1999717504) >> 4 ^ 124982382);
                  if ((((1218257185 ^ 162424032) & 388271341 | 18057944) << 4 << 1 ^ -378055341) != 0) {
                        ;
                  }

                  if ((1461139591 << 3 >>> 4 >>> 3 << 2 ^ 1381059567) != 0) {
                        ;
                  }

                  var7.<init>(var10003, var2.getScaledHeight() - (((0 & 2072322432) >>> 3 >> 1 | 1703636032) >>> 2 ^ 425909031));
                  Hud.renderItem(var10000, var7);
                  ++var6;
            }
      }

      public static void drawUnfilledCircle(double var0, double var2, float var4, float var5, int var6) {
            if ((((83297831 >> 1 ^ 243732) >>> 4 | 694495 | 1721155) ^ -1497211414) != 0) {
                  ;
            }

            if ((1210154593 >> 3 & 121379920 ^ 17042496) == 0) {
                  ;
            }

            float var7 = (float)(var6 >> ((14 >> 4 | 601889794) >> 4 & 20656398 ^ 3801368) & (((206 ^ 120) << 1 << 2 | 432) ^ 1359)) / 255.0F;
            float var8 = (float)(var6 >> (7 ^ 5 ^ 1 ^ 19) & (187 << 3 >> 4 >>> 4 ^ 250)) / 255.0F;
            float var9 = (float)(var6 >> ((7 | 4 | 5) >>> 2 ^ 9) & (126 >> 2 << 1 & 2 & 1 ^ 255)) / 255.0F;
            float var10 = (float)(var6 & ((73 >> 4 ^ 1) >>> 4 ^ 850274894 ^ 850274993)) / 255.0F;
            if (((67704832 >>> 1 ^ 27454951 | 11333664) ^ 843040529) != 0) {
                  ;
            }

            GL11.glColor4f(var8, var9, var10, var7);
            GL11.glLineWidth(var5);
            GL11.glEnable((2816 << 2 << 2 | 5549) << 1 ^ 90234);
            GL11.glBegin(1 >> 1 ^ 343778509 ^ 343778511);

            for(int var11 = 387076519 >> 4 << 2 >>> 4 >>> 4 ^ 94531 ^ 308695; var11 <= (((306 & 26 & 4) >>> 3 >> 3 | 1929256420) ^ 1929256076); ++var11) {
                  double var10001 = (double)var11;
                  if ((1349741157 >>> 1 << 3 ^ 1103997328) == 0) {
                        ;
                  }

                  var10001 = var10001 * 3.141526D / 180.0D;
                  if ((136348738 << 4 >> 3 ^ -264173436) == 0) {
                        ;
                  }

                  var10001 = Math.sin(var10001);
                  if ((2110994084 << 3 & 1762329352 ^ 1155258246 ^ -1093843786) != 0) {
                        ;
                  }

                  double var10000 = var0 + var10001 * (double)var4;
                  double var10002 = (double)var11;
                  if (!"stop skidding".equals("you're dogshit")) {
                        ;
                  }

                  var10001 = var2 + Math.cos(var10002 * 3.141526D / 180.0D) * (double)var4;
                  if (((1698106465 >> 1 | 181532050) & 750216798 ^ 680748562) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var10000, var10001);
            }

            GL11.glEnd();
            GL11.glDisable((1469 >>> 1 & 543) << 1 ^ 3868);
      }

      public static void drawCircle(double var0, double var2, float var4, int var5) {
            float var6 = (float)(var5 >> ((2 << 4 >>> 1 ^ 14) >>> 2 ^ 31) & (230 >> 1 ^ 16 ^ 75 ^ 215)) / 255.0F;
            float var7 = (float)(var5 >> ((3 >> 2 ^ 576734339) >> 4 ^ 36045912) & (237 >>> 4 & 3 ^ 253)) / 255.0F;
            if ((391785257 << 1 >>> 2 ^ 195892628) == 0) {
                  ;
            }

            float var8 = (float)(var5 >> (((3 >> 1 ^ 0) >>> 2 | 1030762066) >>> 1 ^ 515381025) & (193 << 1 << 3 & 783 ^ 255)) / 255.0F;
            float var9 = (float)(var5 & ((98 & 83 | 42) ^ 149)) / 255.0F;
            GL11.glColor4f(var7, var8, var9, var6);
            GL11.glBegin(7 << 3 >> 2 >> 2 >> 3 ^ 9);

            for(int var10 = (494038448 >>> 1 ^ 77989935) << 2 ^ 679279580; var10 <= ((146 >> 3 >>> 3 >>> 4 | 926725028) ^ 701767017 ^ 518565797); ++var10) {
                  double var10000 = var0 + Math.sin((double)var10 * 3.141526D / 180.0D) * (double)var4;
                  double var10002 = (double)var10 * 3.141526D / 180.0D;
                  if (!"idiot".equals("you probably spell youre as your")) {
                        ;
                  }

                  var10002 = Math.cos(var10002);
                  if ((1782512510 << 1 >> 3 << 3 ^ -729942280) == 0) {
                        ;
                  }

                  var10002 *= (double)var4;
                  if (((504900234 | 249465200) >>> 4 >> 4 ^ 1524719 ^ 630084) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var10000, var2 + var10002);
                  if (!"shitted on you harder than archybot".equals("yo mama name maurice")) {
                        ;
                  }
            }

            GL11.glEnd();
      }
}
