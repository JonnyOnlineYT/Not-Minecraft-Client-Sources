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
      public Random random = new Random();
      public static GlyphPageFontRenderer fuckrenderer;
      public static GlyphPageFontRenderer renderer;
      public TimerUtil timerUtil = new TimerUtil();
      private Minecraft mc = Minecraft.getMinecraft();
      public DecimalFormat df = new DecimalFormat("###.#");
      public static GlyphPageFontRenderer myRenderer;
      public static GlyphPageFontRenderer hitStr;
      public static GlyphPageFontRenderer targetRenderer;
      public static GlyphPageFontRenderer smallRenderer;
      private String toAdd;

      public static void drawBorderedCircle(double var0, double var2, float var4, int var5, int var6) {
            GL11.glEnable(853 >>> 1 & 117 ^ 8 ^ 3018);
            GL11.glDisable((1863 ^ 917) >> 2 & 193 ^ 3553);
            GL11.glBlendFunc((459 ^ 254 ^ 176 ^ 300) >> 4 ^ 776, 551 << 2 & 102 ^ 775);
            GL11.glEnable((2588 ^ 1220) & 989 ^ 2552);
            GL11.glPushMatrix();
            float var7 = 0.1F;
            if ((92296256 ^ 10741966 ^ 391189194) != 0) {
                  ;
            }

            GL11.glScalef(var7, var7, var7);
            if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                  ;
            }

            double var10001 = (double)(1.0F / var7);
            if ((56073399 >> 3 >>> 1 ^ 1105701 ^ 1675200 ^ 3942702) == 0) {
                  ;
            }

            var0 = (double)((int)(var0 * var10001));
            var10001 = (double)(1.0F / var7);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var2 = (double)((int)(var2 * var10001));
            if ((((572667161 >>> 3 | 44462875) & 35907475 | 28975238) ^ 1849087888) != 0) {
                  ;
            }

            if (((1263644063 | 558395485) ^ 296981745 ^ 2062182702) == 0) {
                  ;
            }

            var4 *= 1.0F / var7;
            Hud.drawCircle(var0, var2, var4, var6);
            Hud.drawUnfilledCircle(var0, var2, var4, 1.0F, var5);
            GL11.glScalef(1.0F / var7, 1.0F / var7, 1.0F / var7);
            GL11.glPopMatrix();
            GL11.glEnable(1366 >> 1 >>> 3 ^ 61 ^ 3465);
            if (!"your mom your dad the one you never had".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glDisable(718 >>> 4 & 38 ^ 3014);
            GL11.glDisable((1568 ^ 483 | 495) & 1394 ^ 3650);
      }

      public static void drawUnfilledCircle(double var0, double var2, float var4, float var5, int var6) {
            float var7 = (float)(var6 >> ((16 | 4) >> 1 << 3 ^ 72) & (70 << 4 >> 4 ^ 24 ^ 161)) / 255.0F;
            float var10000 = (float)(var6 >> (8 >> 3 >>> 3 >>> 3 >> 4 ^ 16) & ((((222 & 128) >> 4 | 6) ^ 9) >> 1 ^ 252)) / 255.0F;
            if (((1188873693 << 3 & 686214834) >> 4 ^ 34496746) == 0) {
                  ;
            }

            float var8 = var10000;
            float var9 = (float)(var6 >> ((2 | 1 | 1) ^ 11) & (107 ^ 8 ^ 44 ^ 176)) / 255.0F;
            float var10 = (float)(var6 & (((51 | 38) << 1 ^ 37 ^ 7) & 69 ^ 187)) / 255.0F;
            if (((32053766 >> 2 & 1045455) << 4 ^ 10758160) == 0) {
                  ;
            }

            GL11.glColor4f(var8, var9, var10, var7);
            GL11.glLineWidth(var5);
            GL11.glEnable((128 >>> 3 | 14 | 25) ^ 1 ^ 2878);
            GL11.glBegin((1 & 0 & 1684298886 | 431125459 | 402508376) ^ 536869849);

            for(int var11 = ((585341136 ^ 46234782) & 81955736) >>> 1 & 390119 ^ 81924; var11 <= (62 >> 3 ^ 2 ^ 365); ++var11) {
                  double var12 = var0 + Math.sin((double)var11 * 3.141526D / 180.0D) * (double)var4;
                  double var10002 = Math.cos((double)var11 * 3.141526D / 180.0D);
                  double var10003 = (double)var4;
                  if ((84427328 << 4 ^ 1350837248) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var12, var2 + var10002 * var10003);
            }

            if (((269089590 ^ 20256812) >>> 3 >> 1 ^ 18075185) == 0) {
                  ;
            }

            GL11.glEnd();
            GL11.glDisable((2686 | 949) >>> 1 ^ 3807);
      }

      public EntityPlayer getClosest() {
            Stream var10000 = this.mc.world.loadedEntityList.stream().filter((var1x) -> {
                  return (boolean)(var1x != this.mc.player ? 0 >> 1 << 2 ^ 1 : ((255097829 ^ 102930911) & 74360998 | 294045) ^ 457919);
            });
            Predicate var10001 = (var1x) -> {
                  EntityPlayerSP var10000 = this.mc.player;
                  if (!"idiot".equals("please dont crack my plugin")) {
                        ;
                  }

                  int var2;
                  if (var10000.getDistance(var1x) <= 150.0F) {
                        var2 = ((0 ^ 511088885) << 4 ^ 1976430658 | 114996904) ^ -1761673285;
                        if (((308100429 << 4 | 361778040 | 583792857 | 458449606) ^ 1071382527) == 0) {
                              ;
                        }
                  } else {
                        if (((997482466 >> 4 & 47176050) << 2 & 90115736 ^ 1190451770) != 0) {
                              ;
                        }

                        var2 = 2115736655 << 4 & 1470692175 ^ 1101545536;
                  }

                  return (boolean)var2;
            };
            if ((1479800808 << 4 << 3 & 185170854 ^ 151613440) == 0) {
                  ;
            }

            List var1 = (List)var10000.filter(var10001).filter((var0) -> {
                  boolean var10000 = var0.isDead;
                  if (((1354588620 | 751056973) & 139478466 ^ -316077560) != 0) {
                        ;
                  }

                  int var1;
                  if (!var10000) {
                        var1 = (0 << 1 ^ 420752725) >>> 3 ^ 52594091;
                  } else {
                        if (!"please take a shower".equals("please take a shower")) {
                              ;
                        }

                        var1 = 471295154 ^ 440063651 ^ 84265202 ^ 52984547;
                  }

                  return (boolean)var1;
            }).filter((var0) -> {
                  return var0 instanceof EntityPlayer;
            }).sorted(Comparator.comparing((var1x) -> {
                  return Float.valueOf(this.mc.player.getDistance(var1x));
            })).collect(Collectors.toList());
            if (((724902249 ^ 698932996) << 4 ^ 310140035 ^ 1000585811) == 0) {
                  ;
            }

            return var1.size() > 0 ? (EntityPlayer)var1.get((1290598032 ^ 3758534 | 706724605) >> 3 & 223604579 ^ 223604067) : null;
      }

      public static int rainbow(int var0) {
            long var10000 = System.currentTimeMillis();
            if ((((306798363 | 200915385) >> 3 | 7368721) << 2 ^ -375357714) != 0) {
                  ;
            }

            double var1 = Math.ceil((double)(var10000 + (long)var0) / 16.0D);
            var1 %= -360.0D;
            return Color.getHSBColor((float)(var1 / -360.0D), 0.735F, 1.0F).getRGB();
      }

      public static void drawCircle(double var0, double var2, float var4, int var5) {
            float var6 = (float)(var5 >> (23 & 15 ^ 0 ^ 31) & (((142 | 2 | 95) << 2 | 24) ^ 899)) / 255.0F;
            float var10000 = (float)(var5 >> (((1 >> 2 | 1489494028) & 1335446289) >>> 4 ^ 76026896) & ((122 | 49 | 110) ^ 128));
            if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            float var7 = var10000 / 255.0F;
            float var8 = (float)(var5 >> ((4 >>> 4 | 1162039245) ^ 689821925 ^ 1818138400) & ((21 << 3 << 1 ^ 41) >> 1 ^ 67)) / 255.0F;
            float var9 = (float)(var5 & (((66 ^ 35 ^ 77) & 13) >> 4 ^ 255)) / 255.0F;
            GL11.glColor4f(var7, var8, var9, var6);
            GL11.glBegin(((2 >> 4 | 1604756709) << 2 ^ 349164202 | 713078849) ^ 1792407926);
            int var10 = 308202500 >>> 1 ^ 56526869 ^ 3192207 ^ 172055448;

            while(true) {
                  int var10001 = (48 | 2) & 32 ^ 328;
                  if (((2111871204 ^ 1745249635 | 287597982 | 334286694 | 144499448) ^ -2120317804) == 0) {
                  }

                  if (var10 > var10001) {
                        GL11.glEnd();
                        return;
                  }

                  if (((545655771 ^ 355383374) >>> 2 << 3 << 3 ^ 1640858678) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var0 + Math.sin((double)var10 * 3.141526D / 180.0D) * (double)var4, var2 + Math.cos((double)var10 * 3.141526D / 180.0D) * (double)var4);
                  ++var10;
            }
      }

      public static void drawRect(float var0, float var1, float var2, float var3, int var4) {
            float var5;
            if (var0 < var2) {
                  var5 = var0;
                  var0 = var2;
                  if (!"stringer is a good obfuscator".equals("stop skidding")) {
                        ;
                  }

                  var2 = var5;
            }

            if ((1670853954 >>> 1 >> 2 << 1 ^ 42893865 ^ -1159326745) != 0) {
                  ;
            }

            if (var1 < var3) {
                  if (!"intentMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var5 = var1;
                  var1 = var3;
                  if (((1429965860 << 3 | 851973238 | 732261168) ^ -1141014666) == 0) {
                        ;
                  }

                  var3 = var5;
            }

            var5 = (float)(var4 >> (((3 | 1) ^ 0) >>> 2 >> 1 ^ 24) & ((161 >>> 1 | 45) << 4 >>> 3 << 4 ^ 3935)) / 255.0F;
            if (((1167967717 >>> 1 & 481825224) << 1 >>> 3 ^ 2228272) == 0) {
                  ;
            }

            int var10000 = var4 >> ((2 & 1 | 747437680) ^ 747437664);
            int var10001 = (10 << 3 ^ 38) >>> 4 >> 3 ^ 255;
            if ((269404667 << 2 << 3 >> 3 ^ 686586 ^ 1727187386) != 0) {
                  ;
            }

            float var6 = (float)(var10000 & var10001) / 255.0F;
            float var7 = (float)(var4 >> ((2 >>> 4 & 1089002755 | 1042832575 | 1030803023) ^ 1064886007) & (((118 | 1) & 51) >>> 1 >>> 4 ^ 254)) / 255.0F;
            float var8 = (float)(var4 & ((148 >> 2 | 6 | 23) >> 3 ^ 249)) / 255.0F;
            Tessellator var9 = Tessellator.getInstance();
            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            SourceFactor var11 = SourceFactor.SRC_ALPHA;
            DestFactor var12 = DestFactor.ONE_MINUS_SRC_ALPHA;
            if (((1262439801 | 1190928196) >>> 1 ^ 671068094) == 0) {
                  ;
            }

            GlStateManager.tryBlendFuncSeparate(var11, var12, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color(var6, var7, var8, var5);
            if (('耠' ^ '耠') == 0) {
                  ;
            }

            var10.begin((6 >>> 1 ^ 2) >> 1 ^ 7, DefaultVertexFormats.POSITION);
            double var13 = (double)var0;
            if (!"intentMoment".equals("please take a shower")) {
                  ;
            }

            double var10002 = (double)var3;
            if ((((1690039577 | 114929285) & 559595416) << 3 << 2 ^ -921817758) != 0) {
                  ;
            }

            var10.pos(var13, var10002, 0.0D).endVertex();
            if (((1527560972 << 4 | 1780825877 | 1073331611) << 1 ^ -32834) == 0) {
                  ;
            }

            var13 = (double)var2;
            var10002 = (double)var3;
            if (!"stringer is a good obfuscator".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10.pos(var13, var10002, 0.0D).endVertex();
            var10.pos((double)var2, (double)var1, 0.0D).endVertex();
            var10.pos((double)var0, (double)var1, 0.0D).endVertex();
            var9.draw();
            GlStateManager.enableTexture2D();
            if (((1701208353 << 1 ^ 326486515) & 1427993447 ^ -1147303858) != 0) {
                  ;
            }

            GlStateManager.disableBlend();
      }

      private void onRenderKeyStrokes(RenderGameOverlayEvent var1) {
            int[] var10000 = new int[(0 >> 3 << 4 | 192699350) & 23108823 ^ 23075031];
            if ((((431614729 ^ 133665412) >> 4 | 9751933) << 1 ^ 1576986056) != 0) {
                  ;
            }

            var10000[(135315500 >> 3 << 3 & 95659775) >> 4 ^ 67586] = ((0 | 105065588 | 13560079) & 96100292 & 17990957 | '鲎') ^ 171407;
            int[] var2 = var10000;
            int var3 = Keyboard.isKeyDown(this.mc.gameSettings.keyBindForward.getKeyCode()) ? (50 & 49) << 1 ^ 29 : (47 >> 4 ^ 0) << 2 ^ 6 ^ 60;
            int var4 = Keyboard.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode()) ? 64 << 3 >>> 1 >> 3 ^ 93 : (18 ^ 12) & 27 ^ 40;
            int var7 = Keyboard.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode()) ? (95 ^ 69 | 4) << 2 ^ 5 : (36 << 2 | 92 | 8) ^ 238;
            if ((622912073 >>> 3 >>> 2 ^ 1332894126) != 0) {
                  ;
            }

            int var5 = var7;
            var7 = this.mc.gameSettings.keyBindRight.getKeyCode();
            if (!"ape covered in human flesh".equals("you're dogshit")) {
                  ;
            }

            int var6 = Keyboard.isKeyDown(var7) ? 15 << 3 & 111 ^ 21 : (17 | 4) << 1 >> 1 ^ 39;
            Hud.drawRoundedRect(5.0D, 250.0D, 30.0D, 30.0D, 6.0D, (new Color((854194333 ^ 455033132) >>> 1 ^ 352017624, ((1870634054 ^ 961357681 | 391348309) ^ 584452934) << 3 ^ -1389526648, (889086902 ^ 687369278) << 3 ^ 1813175433 ^ -1943887671, var4)).getRGB());
            Hud.drawRoundedRect(38.0D, 250.0D, 30.0D, 30.0D, 6.0D, (new Color(27264005 >>> 4 >>> 4 >> 3 ^ 13312, (1083395923 >>> 4 | 54313782) << 2 >> 1 & 137082298 ^ 137078186, 1229136365 << 4 & 156147033 ^ 12085 ^ 144229, var5)).getRGB());
            Hud.drawRoundedRect(71.0D, 250.0D, 30.0D, 30.0D, 6.0D, (new Color((487388513 ^ 404846516) >> 2 ^ 21717173, (768519609 | 625186588) >>> 3 << 1 ^ 192147438, ((1529808074 ^ 963279829) & 486324564) << 1 ^ 8942120, var6)).getRGB());
            Color var10005 = new Color;
            int var10007 = 912987738 >> 2 >>> 2 >>> 2 ^ 14265433;
            int var10008 = 811146177 << 4 >> 2 >> 3 << 3 & 15993630 ^ 6556416;
            if (((1285998819 ^ 894158296 | 1771018776 | 46291386) ^ 2079317947) == 0) {
                  ;
            }

            var10005.<init>(var10007, var10008, (471416354 | 280154124) >> 4 ^ 16924003 ^ 11665560 ^ 8094233, var3);
            Hud.drawRoundedRect(38.0D, 217.0D, 30.0D, 30.0D, 6.0D, var10005.getRGB());
            GlyphPageFontRenderer var8 = renderer;
            Color var10004 = new Color;
            if (((1331242896 ^ 746473567 | 1012137127 | 1894022470) ^ 2146959343) == 0) {
                  ;
            }

            var10004.<init>((201 >>> 4 & 8 & 7) << 4 ^ 255, (1638634971 ^ 184686711) >> 1 >> 2 ^ 223687093, (1225329664 >>> 1 & 554591819) >> 4 ^ 33570912, ((219 | 182) & 132) >> 4 ^ 247);
            var8.drawString("W", 42.5F, 219.0F, var10004.getRGB(), (boolean)(((0 & 1643751872 | 1907365299) >> 4 | 25474127) ^ 127907166));
            var8 = renderer;
            var10004 = new Color;
            if (!"intentMoment".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10004.<init>(95 >>> 4 << 3 >>> 4 >> 4 ^ 255, 273452036 >>> 4 ^ 17090752, (1926200226 >>> 4 ^ 108375175 ^ 3595089) >> 2 ^ 6021147, 136 & 110 & 5 & 475046130 ^ 941625907 ^ 165929610 ^ 834924614);
            var8.drawString("S", 45.0F, 253.0F, var10004.getRGB(), (boolean)(0 >>> 3 << 3 ^ 1));
            if ((776699633 >>> 4 >> 3 >>> 2 ^ 1516991) == 0) {
                  ;
            }

            renderer.drawString("A", 12.0F, 253.0F, (new Color(((171 ^ 162) & 1) >> 4 ^ 255, 553096 >>> 1 ^ 276548, 1970854417 ^ 158953232 ^ 764334492 ^ 375584822 ^ 1206754475, (122 & 31) >>> 2 ^ 249)).getRGB(), (boolean)((0 & 396988442 | 1629187495) ^ 1629187494));
            var8 = renderer;
            if (!"stop skidding".equals("you probably spell youre as your")) {
                  ;
            }

            var10004 = new Color;
            int var10006 = 220 & 62 & 27 & 23 ^ 239;
            if (((1919369401 >>> 1 >>> 4 | 6390910) ^ 66305663) == 0) {
                  ;
            }

            var10004.<init>(var10006, ((1812058079 << 2 ^ 1772106694) & 1706671184 ^ 706570246 | 115881462) ^ 1878816246, 373158465 >> 4 >>> 3 ^ 2915300, (165 ^ 150) >> 2 << 2 & 14 ^ 255);
            var8.drawString("D", 78.0F, 253.0F, var10004.getRGB(), (boolean)((0 << 3 | 1496438248) << 3 << 1 & 1863362943 ^ 51382273));
            if (((1011037688 | 716903454) << 4 ^ -273457184) == 0) {
                  ;
            }

            var2[(465497066 << 1 & 792227972 ^ 255512407) >>> 2 ^ 167815156] += (0 >>> 4 | 669420080) >> 3 ^ 83677511;
      }

      private static void begin() {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            SourceFactor var10000 = SourceFactor.SRC_ALPHA;
            DestFactor var10001 = DestFactor.ONE_MINUS_SRC_ALPHA;
            if (((38590984 ^ 37402311) >>> 4 ^ -375950450) != 0) {
                  ;
            }

            GlStateManager.tryBlendFuncSeparate(var10000, var10001, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.shadeModel(1986 << 3 >> 2 << 1 << 3 >>> 1 ^ 24865);
            GlStateManager.glLineWidth(2.0F);
      }

      public void onRenderTargetHUD(RenderGameOverlayEvent var1) {
            Minecraft var10000 = this.mc;
            if ((1301069534 >> 2 ^ 140974762 ^ 453260573) == 0) {
                  ;
            }

            if (var10000.player != null && !this.mc.player.isDead) {
                  Stream var6 = this.mc.world.loadedEntityList.stream().filter((var1x) -> {
                        int var10000;
                        if (var1x != this.mc.player) {
                              var10000 = 0 << 1 & 1226811787 ^ 1;
                        } else {
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                                    ;
                              }

                              var10000 = 290663406 >>> 1 >> 3 << 3 ^ 145331696;
                        }

                        return (boolean)var10000;
                  });
                  Predicate var10001 = (var1x) -> {
                        int var10000;
                        if (this.mc.player.getDistance(var1x) <= 5.0F) {
                              if (((1270442956 | 208190760) & 174512341 ^ 174119108) == 0) {
                                    ;
                              }

                              var10000 = (0 >>> 1 & 114992137) >>> 2 & 269937735 ^ 1;
                        } else {
                              var10000 = 856928565 << 1 >> 2 & 25016582 ^ 17405954;
                        }

                        return (boolean)var10000;
                  };
                  if (!"intentMoment".equals("yo mama name maurice")) {
                        ;
                  }

                  var6 = var6.filter(var10001);
                  var10001 = (var0) -> {
                        boolean var10000 = var0.isDead;
                        if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        int var1;
                        if (!var10000) {
                              var1 = (0 & 500682091 ^ 1416093948) >> 2 ^ 354023486;
                        } else {
                              if (((296890931 ^ 95810776) << 2 >>> 4 ^ 84013498) == 0) {
                                    ;
                              }

                              var1 = (72106076 ^ 48314769) >>> 1 >>> 3 ^ 4399174 ^ 2750426;
                        }

                        return (boolean)var1;
                  };
                  if (((1138696046 >> 1 ^ 564605318) >>> 3 ^ -1125823659) != 0) {
                        ;
                  }

                  var6 = var6.filter(var10001).filter((var0) -> {
                        boolean var10000 = var0 instanceof EntityPlayer;
                        if ((461269905 >>> 4 & 19851653 ^ 19325057) == 0) {
                              ;
                        }

                        return var10000;
                  }).filter((var0) -> {
                        return (boolean)(var0.ticksExisted > (((8 | 7 | 1) << 1 | 2) ^ 0) ? (0 | 680263660) >> 2 & 37046104 ^ 35669849 : (1756991860 << 4 >> 3 ^ 1074429306) >> 3 ^ -164680718);
                  }).filter((var0) -> {
                        return (boolean)(!var0.isInvisible() ? ((0 | 451673046) >> 2 | 107592596) ^ 117178356 : (1377759034 >> 4 ^ 74575953) & 5429733 ^ 5249184);
                  });
                  if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                        ;
                  }

                  if ((1261918968 << 1 << 3 << 4 ^ 928708608) == 0) {
                        ;
                  }

                  List var2 = (List)var6.sorted(Comparator.comparing((var1x) -> {
                        return Float.valueOf(this.mc.player.getDistance(var1x));
                  })).collect(Collectors.toList());
                  if (var2.size() > 0 && ModuleManager.getModuleByName("TargetHUD").isToggled()) {
                        ScaledResolution var3 = new ScaledResolution(this.mc);
                        EntityPlayer var7 = (EntityPlayer)var2.get((939644416 >> 2 << 2 ^ 731216760) & 187851255 ^ 51388784);
                        if (((798580582 >> 4 & 26854163) >> 4 ^ -29385275) != 0) {
                              ;
                        }

                        float var8 = var7.getHealth();
                        if ((1474717265 >>> 3 << 3 ^ -1196901346) != 0) {
                              ;
                        }

                        var8 *= 5.0F;
                        int var10002 = (1291298467 >>> 3 >>> 1 | 78593798) ^ 82804590;
                        if (((1910180119 << 2 ^ 222105326 ^ 1196054184) & 1507340909 ^ 152580104) == 0) {
                              ;
                        }

                        int var4 = (int)(var8 + ((EntityPlayer)var2.get(var10002)).getAbsorptionAmount() * 5.0F);
                        Color var10 = new Color;
                        var10002 = 166 >> 1 >>> 3 ^ 1 ^ 213;
                        int var10003 = 60 << 2 << 1 ^ 318;
                        int var10004 = (30 ^ 29) & 2 & 0 ^ 222;
                        if (!"stop skidding".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var10.<init>(var10002, var10003, var10004, 222 >> 3 ^ 13 ^ 233);
                        int var5 = var10.getRGB();
                        if (var4 >= (2 >>> 2 ^ 1610870302 ^ 866555209 ^ 1403353869)) {
                              var5 = (new Color((1934853078 ^ 1510337165) << 2 ^ -1520851604, ((159 << 4 & 384 | 369) ^ 350 | 140) ^ 80, (4556072 ^ 2522928) << 3 >>> 4 ^ 3276044, (9 ^ 1) << 3 ^ 191)).getRGB();
                        }

                        if (((157538124 | 133115729) << 1 ^ 1309905469) != 0) {
                              ;
                        }

                        if (var4 >= (32 >>> 1 & 0 & 347265747 ^ 50) && var4 < (15 & 14 & 12 ^ 86)) {
                              if ((((616842176 | 125764956) << 1 | 124818168) ^ 1342087160) == 0) {
                                    ;
                              }

                              var5 = (new Color((69 << 1 ^ 33 | 156 | 136) << 3 ^ 1379, 90 << 3 >>> 3 ^ 67 ^ 130, 5 >> 4 << 4 << 4 ^ 225, (132 & 77) >> 3 >> 2 ^ 255)).getRGB();
                        }

                        int var9;
                        if (var4 > (((18 | 15) & 8) >>> 1 & 1 ^ 20)) {
                              var9 = ((18 | 8) ^ 6) >> 2 ^ 1 ^ 52;
                              if ((248386633 >>> 4 >>> 3 ^ 258197023) != 0) {
                                    ;
                              }

                              if (var4 < var9) {
                                    var10 = new Color;
                                    if ((855142094 >>> 4 >> 3 ^ 6680797) == 0) {
                                          ;
                                    }

                                    var10002 = (199 >> 4 | 0) ^ 237;
                                    if ((1220715077 >> 1 >>> 1 >>> 2 ^ -761910184) != 0) {
                                          ;
                                    }

                                    var10.<init>(var10002, 69 << 2 >>> 3 >>> 2 ^ 147, ((1 | 0) << 2 >> 1 | 0) ^ 13, (80 ^ 34 | 45) ^ 128);
                                    var5 = var10.getRGB();
                              }
                        }

                        if (var4 <= ((6 >>> 3 | 818113928 | 502682165) ^ 1039630249)) {
                              var5 = (new Color(128 >>> 3 >> 3 ^ 253, (1615476486 >> 4 | 39762729) ^ 106871673, (630852611 | 559750648) ^ 105161551 ^ 597409460, (68 | 28) >>> 3 >>> 1 ^ 250)).getRGB();
                        }

                        int var12 = var3.getScaledWidth() / (0 >> 3 >>> 2 & 188258204 ^ 2);
                        var9 = 28 ^ 24 ^ 3 ^ 48;
                        if (!"please dont crack my plugin".equals("nefariousMoment")) {
                              ;
                        }

                        double var15 = (double)(var12 - var9);
                        var9 = var3.getScaledHeight() / (0 >>> 3 << 4 ^ 2);
                        if (((970453760 ^ 624345895) & 450314659 & 396061086 & 196886660 ^ -1633229298) != 0) {
                              ;
                        }

                        Hud.drawRoundedRect(var15, (double)(var9 - ((10 & 8) >>> 1 ^ 51)), 110.0D, 50.0D, 6.0D, (new Color(((521783271 | 507971241) << 1 & 361601184 & 68127978) >>> 1 ^ 33997888, 465008826 << 1 << 3 >> 2 & 1454007861 ^ 1183343136, 510648225 << 2 >>> 3 ^ 255324112, 45 >> 2 << 2 << 3 >> 4 ^ 93)).getRGB());
                        var15 = (double)(var3.getScaledWidth() / ((0 & 667611461) << 1 ^ 2) - ((11 >>> 3 >> 3 & 101955096 | 1970252652) << 2 ^ -708924030));
                        double var11 = (double)(var3.getScaledHeight() / ((0 >> 3 << 1 ^ 1772745101) >>> 3 ^ 221593139) - ((15 ^ 10) >>> 2 ^ 47));
                        var10003 = (972572822 | 192390028) ^ 571187588 ^ 162533435 ^ 272914977;
                        if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                              ;
                        }

                        double var13 = (double)(((EntityPlayer)var2.get(var10003)).getHealth() * 5.0F);
                        if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                              ;
                        }

                        Hud.drawRoundedRect(var15, var11, var13, 2.0D, 3.0D, var5);
                        GlyphPageFontRenderer var18 = targetRenderer;
                        StringBuilder var14 = (new StringBuilder()).append("Health:§c§l").append(var4 / (((3 | 1) ^ 1 | 0) << 2 >> 4 ^ 5)).append("§c");
                        if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        String var16 = var14.toString();
                        float var17 = (float)(var3.getScaledWidth() / (((1 | 0) & 0) >> 2 << 4 ^ 2) - (((31 & 13) << 4 >> 4 | 6) >> 1 ^ 53));
                        var10003 = var3.getScaledHeight();
                        var10004 = 0 >>> 1 >> 4 >> 1 ^ 2;
                        if (!"stop skidding".equals("stop skidding")) {
                              ;
                        }

                        float var19 = (float)(var10003 / var10004 - (((17 & 10) >>> 3 ^ 278474474) << 1 ^ 556948988));
                        if (((1313088771 ^ 230864916) >> 2 >> 2 ^ 70805969) == 0) {
                              ;
                        }

                        Color var21 = new Color;
                        int var10006 = 151 & 46 ^ 0 ^ 231;
                        int var10007 = 171 >> 1 << 1 ^ 67 ^ 8;
                        int var10008 = ((12 | 5) ^ 0 ^ 6 ^ 1) & 9 ^ 233;
                        int var10009 = (((42 & 33) >> 2 & 7) >> 1 | 1355339235) ^ 1355339036;
                        if (((415948104 >>> 1 ^ 138292482) << 2 ^ 292359832) == 0) {
                              ;
                        }

                        var21.<init>(var10006, var10007, var10008, var10009);
                        var18.drawString(var16, var17, var19, var21.getRGB(), (boolean)((0 >> 3 & 1465735477) << 2 ^ 1));
                        var18 = targetRenderer;
                        var14 = new StringBuilder();
                        if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var14 = var14.append("Dist:§c§l").append(this.df.format((double)this.mc.player.getDistance((Entity)var2.get((671227904 ^ 236662543) & 112740150 ^ 102237958))));
                        if ((41423448 >> 1 << 2 >> 3 ^ 8151791 ^ 14837369) == 0) {
                              ;
                        }

                        var18.drawString(var14.append("§c").toString(), (float)(var3.getScaledWidth() / (((0 ^ 933140015) << 4 ^ 543791615) >>> 4 ^ 93851570)), (float)(var3.getScaledHeight() / ((1 & 0) >>> 3 ^ 1817857985 ^ 1817857987) - ((9 >> 2 | 0) >> 1 >>> 3 ^ 40)), (new Color((182 & 20) >>> 4 ^ 0 ^ 224, (110 & 33) >>> 4 ^ 227, (11 >> 1 & 2) << 1 & 1180926095 ^ 225, (247 >>> 1 & 87) >> 3 ^ 245)).getRGB(), (boolean)(0 << 4 ^ 588302404 ^ 26854424 ^ 579406941));
                        var18 = targetRenderer;
                        var16 = ((Entity)var2.get((2059540765 << 4 ^ 1453752047 | 398451593) ^ -4198465)).getName();
                        if ((1985542416 >> 4 >> 3 ^ 10939858 ^ 4873248) == 0) {
                              ;
                        }

                        var10002 = var3.getScaledWidth() / ((1 | 0) & 0 & 320692500 ^ 2);
                        GlyphPageFontRenderer var20 = targetRenderer;
                        int var10005 = 953047921 >>> 1 >>> 1 ^ 238261980;
                        if ((489998643 >> 2 & 43900827 ^ -695493916) != 0) {
                              ;
                        }

                        var10003 = var20.getStringWidth(((Entity)var2.get(var10005)).getName());
                        if ((77899035 << 4 << 4 >> 3 ^ -191585440) == 0) {
                              ;
                        }

                        var17 = (float)(var10002 - var10003 / ((1 ^ 0) << 1 << 4 ^ 34));
                        var19 = (float)(var3.getScaledHeight() / ((1 >> 1 | 1132088992) ^ 1132088994) - ((19 | 6) >>> 1 >>> 2 ^ 27));
                        var21 = new Color;
                        var10006 = (82 << 2 ^ 8) >>> 4 ^ 245;
                        if (!"idiot".equals("nefariousMoment")) {
                              ;
                        }

                        var10007 = ((128 ^ 92) >>> 4 & 0) >> 2 ^ 225;
                        var10008 = (218 | 120) >> 3 ^ 254;
                        var10009 = (19 ^ 2 | 1) & 11 ^ 0 ^ 254;
                        if (((1923777035 << 4 & 349891090) >> 2 ^ 1445746 ^ 3409782) == 0) {
                              ;
                        }

                        var21.<init>(var10006, var10007, var10008, var10009);
                        var18.drawString(var16, var17, var19, var21.getRGB(), (boolean)((0 & 267697113 & 1281979856) << 2 << 4 ^ 1));
                  }

                  if ((1197573691 >>> 1 >>> 1 ^ 299393422) == 0) {
                        ;
                  }

            }
      }

      public static void drawCustomRect(float var0, float var1, float var2, float var3, int var4) {
            if (((314323642 << 3 & 105994001 & 61175736 | '鐎') ^ -433209757) != 0) {
                  ;
            }

            if (((563923343 << 1 & 991183417) >>> 2 ^ 12845190) == 0) {
                  ;
            }

            float var5;
            if (var0 < var2) {
                  if ((((1347852691 | 183354393) ^ 199463181 ^ 410562036 | 180736817) << 3 ^ 1596955544) == 0) {
                        ;
                  }

                  var5 = var0;
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var0 = var2;
                  var2 = var5;
            }

            if (var1 < var3) {
                  var5 = var1;
                  var1 = var3;
                  var3 = var5;
            }

            int var10000 = var4 >> (((2 | 1) >>> 1 & 0) >>> 3 ^ 24);
            int var10001 = (111 | 23) ^ 35 ^ 163;
            if (!"yo mama name maurice".equals("you're dogshit")) {
                  ;
            }

            var5 = (float)(var10000 & var10001) / 255.0F;
            float var6 = (float)(var4 >> (4 >>> 1 >>> 4 ^ 16) & (238 >>> 2 >>> 4 >>> 2 ^ 255)) / 255.0F;
            float var7 = (float)(var4 >> (6 << 4 >>> 4 & 1 ^ 8) & ((82 << 2 >> 4 | 8) ^ 227)) / 255.0F;
            float var8 = (float)(var4 & (((145 ^ 20) & 65 | 0 | 0) ^ 254)) / 255.0F;
            Tessellator var9 = Tessellator.getInstance();
            if (!"shitted on you harder than archybot".equals("idiot")) {
                  ;
            }

            BufferBuilder var10 = var9.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            if (((1644308571 ^ 1630538555) << 4 >>> 1 >> 2 & 30377671 ^ 4458176) == 0) {
                  ;
            }

            GlStateManager.color(var6, var7, var8, var5);
            var10.begin(((0 & 1831674606) << 3 | 468243405) ^ 468243402, DefaultVertexFormats.POSITION);
            var10.pos((double)var0, (double)var3, 0.0D).endVertex();
            double var13 = (double)var2;
            if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            BufferBuilder var11 = var10.pos(var13, (double)var3, 0.0D);
            if (((12306949 << 3 >>> 2 << 2 | 44250781) ^ -2127689971) != 0) {
                  ;
            }

            var11.endVertex();
            var13 = (double)var2;
            double var10002 = (double)var1;
            if (((360185589 << 2 ^ 539988805) & 1553835312 ^ 1418731536) == 0) {
                  ;
            }

            var10.pos(var13, var10002, 0.0D).endVertex();
            if ((405038885 >>> 4 >> 2 << 2 >>> 4 ^ 1582183) == 0) {
                  ;
            }

            var10.pos((double)var0, (double)var1, 0.0D).endVertex();
            if (((271621968 >>> 4 & 4389540) >> 3 << 4 ^ -942530363) != 0) {
                  ;
            }

            var9.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            float var12 = var0 - 1.0F;
            if (((1533588454 >> 1 >> 2 >>> 4 | 9984138) ^ 12507631) == 0) {
                  ;
            }

            Hud.drawRect(var12, var1 - 1.0F, var0, var3, (new Color((213 >> 2 & 7 ^ 1) >>> 2 ^ 254, (1511981400 | 179128546 | 462810680) ^ 1358590167 ^ 189112109, (139 >> 3 & 4) >> 4 ^ 255, (35 << 1 & 6 ^ 1) >>> 1 >> 1 ^ 254)).getRGB());
            if (!"please take a shower".equals("stringer is a good obfuscator")) {
                  ;
            }

            if ((140317762 >> 2 & 11486146 ^ 475392) == 0) {
                  ;
            }

            Hud.drawRect(var0, var1 - 1.0F, var2, var1, (new Color((31 >>> 3 | 0) >> 4 ^ 1809752534 ^ 1809752361, (1528651674 | 1050895236) ^ 487870189 ^ 1655386483, 110 << 3 << 1 >>> 3 ^ 35, (93 ^ 41) >>> 3 >>> 3 ^ 254)).getRGB());
            var12 = var2 + 1.0F;
            float var14 = var3 + 1.0F;
            if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                  ;
            }

            Hud.drawRect(var12, var14, var2, var1, (new Color((204 >> 4 >> 1 & 3) >>> 4 ^ 255, (28389194 >> 1 << 1 | 23474340) ^ 30447292 ^ 2599250, (214 << 4 >> 4 | 46 | 125) ^ 0, ((201 & 21) >>> 4 & 1082375205) >> 3 & 2129533274 ^ 255)).getRGB());
            if (((1653524500 << 3 >>> 2 & 41095824) << 2 ^ 1976334780) != 0) {
                  ;
            }

            var14 = var3 + 1.0F;
            Color var10004 = new Color((87 | 7) << 1 >>> 2 & 3 ^ 252, (1331362881 | 663394385) >>> 1 ^ 765857630 ^ 441151862, ((73 & 7) >>> 4 | 114706509) ^ 114706610, (192 ^ 50) & 143 ^ 96 ^ 29);
            if (((449447818 >>> 3 >>> 4 | 2630459) ^ 4044607) == 0) {
                  ;
            }

            Hud.drawRect(var2, var14, var0, var3, var10004.getRGB());
      }

      @SubscribeEvent
      public void renderOverlay(RenderGameOverlayEvent var1) {
            ScaledResolution var10000 = new ScaledResolution;
            if ((((62697324 ^ 34353129) & 13654570) >> 4 ^ 1408310261) != 0) {
                  ;
            }

            var10000.<init>(this.mc);
            ScaledResolution var2 = var10000;
            FontRenderer var3 = this.mc.fontRenderer;
            if (ModuleManager.getModuleByName("HUD").isToggled()) {
                  ElementType var12 = var1.getType();
                  if ((((555731853 ^ 440263152) & 612423357 | 442947861) ^ 979819325) == 0) {
                        ;
                  }

                  if (var12 == ElementType.TEXT) {
                        if (!"stop skidding".equals("idiot")) {
                              ;
                        }

                        int var13 = 0 ^ 666486033 ^ 274064536 ^ 938221960;
                        if ((1962483603 << 1 >> 1 & 361861815 ^ -983073193) != 0) {
                              ;
                        }

                        int[] var14 = new int[var13];
                        var14[(1047881216 >> 1 ^ 255809067 | 188830919) ^ 457569775] = (0 | 706932091) >> 2 >> 3 >> 2 ^ 5522907;
                        int[] var4 = var14;
                        int var5 = 5981184 >>> 3 << 3 ^ 5981184;
                        if (ModuleManager.getModuleByName("KeyStrokes").isToggled()) {
                              this.onRenderKeyStrokes(var1);
                              if ((1231611289 >>> 2 >>> 1 >>> 4 ^ -1183949230) != 0) {
                                    ;
                              }
                        }

                        if (!"ape covered in human flesh".equals("stop skidding")) {
                              ;
                        }

                        if (!"yo mama name maurice".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        Module var15 = ModuleManager.getModuleByName("InventoryView");
                        if ((((1315773325 | 96634013) << 1 | 1466633564) ^ -536920194) == 0) {
                              ;
                        }

                        if (var15.isToggled()) {
                              if (((583344206 | 217834302) >>> 4 >> 2 ^ -590518800) != 0) {
                                    ;
                              }

                              this.onRenderInventory(var1);
                        }

                        if (ModuleManager.getModuleByName("TargetHUD").isToggled()) {
                              this.onRenderTargetHUD(var1);
                        }

                        GlyphPageFontRenderer var16 = fuckrenderer;
                        int var10004 = var4[((991363281 | 218540520) ^ 148971463) & 859375413 & 653844155 ^ 573572656] * (53 << 4 ^ 669 ^ 225);
                        if ((171381015 << 4 >>> 1 & 471989233 & 240381396 ^ 1072155057) != 0) {
                              ;
                        }

                        var16.drawString("Rage", 5.0F, 5.0F, Hud.rainbow(var10004), (boolean)((0 >>> 4 | 616133210) >>> 2 ^ 154033303));
                        smallRenderer.drawString("Lite|b2.3", 66.0F, 7.0F, (new Color((93 & 4) >>> 1 ^ 253, 1327056463 >> 2 << 1 ^ 663528230, 1370977486 >>> 2 << 2 << 1 ^ -1553012328, ((46 ^ 19) & 21) >>> 3 ^ 253)).getRGB(), (boolean)(3343055 >>> 4 ^ 8644 ^ 201192));
                        Iterator var6 = ModuleManager.getSortedModules().iterator();

                        while(true) {
                              Module var7;
                              do {
                                    do {
                                          do {
                                                do {
                                                      if (!var6.hasNext()) {
                                                            return;
                                                      }

                                                      if (((1556622329 ^ 1506316870 | 31554720) >>> 1 ^ 49348319) == 0) {
                                                            ;
                                                      }

                                                      var7 = (Module)var6.next();
                                                } while(!var7.isToggled());
                                          } while(var7.getName().equals("TabGUI"));
                                    } while(var7.getName().equals("Gui"));
                              } while(var7.getName().equals("Config"));

                              this.toAdd = "";
                              Iterator var8 = var7.settings.iterator();

                              while(var8.hasNext()) {
                                    Setting var9 = (Setting)var8.next();
                                    if (var9 instanceof ModeSetting) {
                                          ModeSetting var10 = (ModeSetting)var9;
                                          this.toAdd = var10.activeMode;
                                          if ((1011908689 ^ 906608584 ^ 10903017 ^ -781540558) != 0) {
                                                ;
                                          }
                                          break;
                                    }
                              }

                              double var11 = (double)(var5 * (var3.FONT_HEIGHT + (((2 & 0) << 4 >>> 4 << 1 | 510073745) ^ 510073748)));
                              String var18 = this.toAdd;
                              if (!"please get a girlfriend and stop cracking plugins".equals("intentMoment")) {
                                    ;
                              }

                              int var10001;
                              int var10002;
                              int var10003;
                              if (var18.equalsIgnoreCase("")) {
                                    var13 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name) - (((5 >> 2 >> 2 | 1277857353) << 1 | 668486044) ^ -1076011624);
                                    var10001 = (int)var11;
                                    if (((1569925038 >> 4 | 34267692) ^ 131854974) == 0) {
                                          ;
                                    }

                                    var10002 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name);
                                    if (((1486283714 ^ 812588588 ^ 1078612569) << 2 & 408212975 ^ 4494540) == 0) {
                                          ;
                                    }

                                    Hud.drawRect(var13, var10001, var10002 - (2 >> 3 & 906274892 ^ 392010718 ^ 392010710), (int)var11 + (0 >>> 1 >>> 2 ^ 1206423342 ^ 1206423339) + var3.FONT_HEIGHT, Hud.rainbow(var4[(1649256950 << 4 << 2 | 1538582489) ^ -604045351] * (((88 >>> 1 & 30) >>> 2 | 1) ^ 1 ^ 302)));
                                    if ((((38218626 ^ 9936683) >> 3 | 826686) ^ 1853623153) != 0) {
                                          ;
                                    }

                                    var13 = var2.getScaledWidth() - myRenderer.getStringWidth(var7.name) - ((5 ^ 0 ^ 2) >> 1 ^ 5);
                                    var10001 = (int)var11;
                                    var10002 = var2.getScaledWidth();
                                    var10003 = (int)var11 + (((1 | 0) & 0) >>> 1 ^ 5) + var3.FONT_HEIGHT;
                                    if (((21069840 | 7900626) >> 4 & 605277 ^ -592653288) != 0) {
                                          ;
                                    }

                                    Hud.drawRect(var13, var10001, var10002, var10003, (new Color((1701139785 << 2 << 2 & 1197312337 ^ 614571353) << 4 ^ 793367696, 264319581 >>> 3 >>> 4 ^ 2064996, 6285587 << 4 >> 1 & 12513288 ^ 12468232, (16 << 3 | 91) << 4 >> 4 ^ 144)).getRGB());
                                    myRenderer.drawString(var7.getName(), (float)(var2.getScaledWidth() - myRenderer.getStringWidth(var7.getName()) - (2 >>> 1 << 4 ^ 2 ^ 22)), (float)(1.0D + var11), Hud.rainbow(var4[(1668600555 ^ 409441327) >>> 3 & 225230565 ^ 224405696] * ((121 << 4 ^ 780) >>> 4 << 1 ^ 446)), (boolean)((((0 & 1328754338) >>> 2 | 539704148) << 2 | 1993379102) ^ -151196321));
                              } else {
                                    var13 = var2.getScaledWidth();
                                    GlyphPageFontRenderer var17 = myRenderer;
                                    StringBuilder var19 = new StringBuilder();
                                    if (((803290348 >>> 2 ^ 65525803) >> 4 ^ 864947982) != 0) {
                                          ;
                                    }

                                    if (((212674413 << 2 >> 2 | 26006482) ^ 96725011 ^ 141434860) == 0) {
                                          ;
                                    }

                                    var13 = var13 - var17.getStringWidth(var19.append(var7.name).append(" ").append(this.toAdd).append("").toString()) - ((1 ^ 0) >>> 2 ^ 2095773375 ^ 1207083982 ^ 991450487);
                                    var10001 = (int)var11;
                                    var10002 = var2.getScaledWidth();
                                    GlyphPageFontRenderer var21 = myRenderer;
                                    if ((571032710 >>> 1 >> 3 ^ 35689544) == 0) {
                                          ;
                                    }

                                    var10003 = var21.getStringWidth((new StringBuilder()).append(var7.name).append(" ").append(this.toAdd).append("").toString());
                                    if ((((2034005838 | 1281468289) << 2 ^ 1097319536) >>> 2 ^ 757335763) == 0) {
                                          ;
                                    }

                                    var10002 = var10002 - var10003 - ((5 >> 4 | 1269101454) ^ 1269101446);
                                    var10003 = (int)var11 + ((1 >> 3 & 1804503402) >>> 3 >> 1 ^ 5) + var3.FONT_HEIGHT;
                                    if (((337608472 ^ 266720023) >>> 1 >> 1 ^ 37992758 ^ 79499829) == 0) {
                                          ;
                                    }

                                    Hud.drawRect(var13, var10001, var10002, var10003, Hud.rainbow(var4[1344379854 << 4 >> 4 ^ 1898033 ^ 454751 ^ 3906976] * ((172 & 113 ^ 27) >>> 3 >>> 2 ^ 301)));
                                    if ((1377027 << 2 ^ 968134231) != 0) {
                                          ;
                                    }

                                    var13 = var2.getScaledWidth();
                                    var17 = myRenderer;
                                    var19 = new StringBuilder;
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    var19.<init>();
                                    var19 = var19.append(var7.name);
                                    if (((1075021953 ^ 185244399) >>> 4 ^ -717644567) != 0) {
                                          ;
                                    }

                                    var13 = var13 - var17.getStringWidth(var19.append(" ").append(this.toAdd).append("").toString()) - (5 >> 1 & 1 ^ 6);
                                    var10001 = (int)var11;
                                    if ((1596387562 >> 3 >> 4 << 2 << 1 ^ 99774216) == 0) {
                                          ;
                                    }

                                    Hud.drawRect(var13, var10001, var2.getScaledWidth(), (int)var11 + (0 & 906648823 ^ 523973614 ^ 523973611) + var3.FONT_HEIGHT, (new Color(881474493 >>> 4 & 21660801 ^ 21528705, (744910504 ^ 195460432) << 2 << 2 ^ 2080456576, 1279910443 >>> 3 & 106559318 ^ 603204, (32 ^ 11 ^ 42) & 0 ^ 2004180565 ^ 2004180510)).getRGB());
                                    if (((1273573993 >> 2 >> 1 | 47912009) >>> 2 ^ 50318739) == 0) {
                                          ;
                                    }

                                    var16 = myRenderer;
                                    String var20 = var7.getName();
                                    var10002 = var2.getScaledWidth();
                                    var21 = myRenderer;
                                    StringBuilder var23 = (new StringBuilder()).append(var7.getName()).append(" ").append(this.toAdd);
                                    if (!"intentMoment".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    String var24 = var23.append("").toString();
                                    if (!"minecraft".equals("please dont crack my plugin")) {
                                          ;
                                    }

                                    float var25 = (float)(var10002 - var21.getStringWidth(var24) - (0 >>> 2 >>> 1 >>> 2 ^ 4));
                                    float var26 = (float)(1.0D + var11);
                                    var10004 = var4[123575331 << 1 << 3 >> 3 << 1 ^ 494301324] * (121 >>> 2 >>> 4 ^ 301);
                                    if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var16.drawString(var20, var25, var26, Hud.rainbow(var10004), (boolean)(0 >>> 4 >>> 4 ^ 1));
                                    var16 = myRenderer;
                                    StringBuilder var22 = (new StringBuilder()).append(" ");
                                    if ((7802252 >>> 4 >> 4 & 13662 ^ 128350197) != 0) {
                                          ;
                                    }

                                    var20 = var22.append(this.toAdd).append("").toString();
                                    var10002 = var2.getScaledWidth();
                                    var21 = myRenderer;
                                    var23 = new StringBuilder;
                                    if ((650362026 >>> 1 >> 2 & 31469185 ^ 12592769) == 0) {
                                          ;
                                    }

                                    var23.<init>();
                                    var25 = (float)(var10002 - var21.getStringWidth(var23.append(" ").append(this.toAdd).append("").toString()) - ((2 << 4 ^ 26) >> 3 ^ 3));
                                    double var27 = 1.0D + var11;
                                    if ((((137735387 | 125569149) & 137238951) >> 2 ^ 2013597458) != 0) {
                                          ;
                                    }

                                    var26 = (float)var27;
                                    var10004 = (new Color((115 << 2 ^ 418) >> 3 ^ 150, 119 & 87 ^ 61 ^ 241, 44 >>> 3 ^ 3 ^ 157, 124 >> 3 >>> 4 << 1 << 4 << 3 ^ 255)).getRGB();
                                    if (!"nefariousMoment".equals("intentMoment")) {
                                          ;
                                    }

                                    var16.drawString(var20, var25, var26, var10004, (boolean)((0 | 1121618246) >>> 2 << 3 >> 1 ^ -1025865403));
                              }

                              if ((((244016518 | 206144312) >> 4 | 9880614) << 4 ^ -1670532320) != 0) {
                                    ;
                              }

                              var10001 = ((1438349161 | 236212953) & 1299302491) >> 2 ^ 323768342;
                              var10002 = var4[((1438349161 | 236212953) & 1299302491) >> 2 ^ 323768342];
                              if ((138584190 >> 1 >>> 3 >>> 1 ^ -2081173771) != 0) {
                                    ;
                              }

                              var4[var10001] = var10002 + ((0 ^ 241102005) << 3 << 2 ^ -874670431);
                              ++var5;
                        }
                  }
            }

      }

      public static void drawRoundedRect(double var0, double var2, double var4, double var6, double var8, int var10) {
            double var11 = var0 + var4;
            double var13 = var2 + var6;
            float var15 = (float)(var10 >> (3 << 4 & 39 ^ 20 ^ 44) & (205 >>> 1 >> 3 ^ 243)) / 255.0F;
            if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                  ;
            }

            if ((((1628453896 | 506298408) ^ 885361219) & 824262495 ^ 18888267) == 0) {
                  ;
            }

            float var10000 = (float)(var10 >> (((12 | 6) >> 2 | 2) ^ 19) & ((49 ^ 6 | 4) << 1 ^ 145)) / 255.0F;
            if (((886819858 ^ 791098628 | 400973102) << 4 ^ 335405684) != 0) {
                  ;
            }

            float var16 = var10000;
            float var17 = (float)(var10 >> ((1 ^ 0) >>> 3 & 1043958595 ^ 8) & (((2 & 1) >>> 1 & 260021096) << 4 ^ 255)) / 255.0F;
            float var18 = (float)(var10 & ((147 | 70) << 2 >>> 4 >> 3 ^ 249)) / 255.0F;
            GL11.glPushAttrib((1106772637 | 950039819 | 1354698369) & 295376928 & 177458703 ^ 9635840);
            if ((979860037 >>> 1 >> 4 ^ 30620626) == 0) {
                  ;
            }

            GL11.glScaled(0.5D, 0.5D, 0.5D);
            var0 *= 2.0D;
            var2 *= 2.0D;
            var11 *= 2.0D;
            var13 *= 2.0D;
            Tessellator var19 = Tessellator.getInstance();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color(var15, var16, var17, var18);
            GL11.glDisable((3105 >>> 2 << 4 << 4 | 151246 | 149101) ^ 213774);
            GL11.glColor4f(var16, var17, var18, var15);
            GL11.glEnable((((2143 | 463) ^ 1683) >> 1 | 864) ^ 3270);
            if (((581548467 << 1 >> 1 ^ 61449323) & 128432472 ^ 924776635) != 0) {
                  ;
            }

            GL11.glBegin((3 >>> 4 | 454774674) << 1 ^ 909549357);
            int var20 = ((389399827 | 191539336) & 416768957 | 160498882 | 32442395) ^ 436170203;
            if (!"shitted on you harder than archybot".equals("intentMoment")) {
                  ;
            }

            double var10001;
            double var10002;
            double var21;
            while(var20 <= (16 >>> 4 >>> 1 ^ 90)) {
                  var21 = var0 + var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8 * -1.0D;
                  var10001 = var2 + var8;
                  var10002 = Math.cos((double)var20 * 3.141592653589793D / 180.0D);
                  double var10003 = var8 * -1.0D;
                  if (((616574223 << 3 >> 2 ^ 53835752) & 142697906 ^ 1269385340) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var21, var10001 + var10002 * var10003);
                  var20 += 3;
            }

            var20 = 50 >> 3 << 2 & 17 ^ 74;

            while(var20 <= ((103 << 2 & 7 & 0 & 1148820198) >> 4 ^ 180)) {
                  var21 = var0 + var8;
                  var10001 = (double)var20;
                  if (((1663053513 ^ 1655326232) >>> 3 ^ 3231002) == 0) {
                        ;
                  }

                  var21 += Math.sin(var10001 * 3.141592653589793D / 180.0D) * var8 * -1.0D;
                  var10001 = var13 - var8;
                  var10002 = (double)var20 * 3.141592653589793D;
                  if ((1219035278 << 3 >> 3 << 3 ^ 1162347632) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var21, var10001 + Math.cos(var10002 / 180.0D) * var8 * -1.0D);
                  var20 += 3;
                  if (!"please go outside".equals("i hope you catch fire ngl")) {
                        ;
                  }
            }

            int var22 = 168890628 << 1 ^ 337781256;
            if ((42932707 >>> 3 ^ 3907148 ^ 6978928) == 0) {
                  ;
            }

            var20 = var22;

            while(true) {
                  int var23 = (32 | 1 | 23) ^ 109;
                  if (((867897531 | 762718139 | 348800834) >> 3 & 27221303 ^ 1242140018) == 0) {
                  }

                  if (var20 > var23) {
                        if ((339362069 >>> 1 & 41515492 ^ 31457187 ^ 388323310) != 0) {
                              ;
                        }

                        var20 = 65 << 2 ^ 229 ^ 443;

                        while(true) {
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("ape covered in human flesh")) {
                                    ;
                              }

                              if ((604582860 >>> 2 >>> 4 << 2 ^ -172421161) != 0) {
                                    ;
                              }

                              if (var20 > ((113 ^ 88 | 5) >>> 4 ^ 182)) {
                                    GL11.glEnd();
                                    GL11.glEnable((338 >> 2 | 66) ^ 3511);
                                    var22 = (1364 & 1267 & 639 ^ 72) >>> 1 ^ 2860;
                                    if ((12667221 << 3 & 8851151 ^ 133768) == 0) {
                                          ;
                                    }

                                    GL11.glDisable(var22);
                                    GL11.glEnable(3239 << 3 >>> 4 << 4 ^ 26833);
                                    GlStateManager.enableTexture2D();
                                    GlStateManager.disableBlend();
                                    if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                                          ;
                                    }

                                    GL11.glScaled(2.0D, 2.0D, 2.0D);
                                    GL11.glPopAttrib();
                                    if (((806287457 >>> 2 | 200867507 | 174333121) & 4505508 ^ 4505248) == 0) {
                                          ;
                                    }

                                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                                    return;
                              }

                              if (((272573423 << 2 ^ 530936196 | 1356556316) >>> 1 ^ 804126494) == 0) {
                                    ;
                              }

                              var21 = var11 - var8;
                              if ((549378 ^ -116180618) != 0) {
                                    ;
                              }

                              var10001 = (double)var20 * 3.141592653589793D / 180.0D;
                              if (((550984205 | 417268759) >> 1 >>> 2 & 18106686 ^ 1047701806) != 0) {
                                    ;
                              }

                              var10001 = Math.sin(var10001);
                              if (!"please go outside".equals("nefariousMoment")) {
                                    ;
                              }

                              GL11.glVertex2d(var21 + var10001 * var8, var2 + var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8);
                              if (((169869763 >>> 1 | 58310254) << 4 ^ 2006707952) == 0) {
                                    ;
                              }

                              var20 += 3;
                        }
                  }

                  var21 = var11 - var8 + Math.sin((double)var20 * 3.141592653589793D / 180.0D) * var8;
                  if ((852504119 >>> 3 >> 3 >>> 3 & 1399685 ^ 1985431828) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var21, var13 - var8 + Math.cos((double)var20 * 3.141592653589793D / 180.0D) * var8);
                  var20 += 3;
            }
      }

      public void onRenderInventory(RenderGameOverlayEvent var1) {
            ScaledResolution var2 = new ScaledResolution(this.mc);
            FontRenderer var3 = this.mc.fontRenderer;
            NonNullList var4 = Minecraft.getMinecraft().player.inventory.mainInventory;
            int var5 = (12 >> 3 >>> 1 >>> 3 | 1352846473) ^ 1352846482;

            int var6;
            int var7;
            ItemStack var8;
            Point var10;
            int var11;
            double var10000;
            double var10001;
            int var10003;
            int var10004;
            Color var10005;
            for(var6 = (0 ^ 1112850697 ^ 567623550 ^ 1137409077 | 129347702) ^ 671068786; var6 <= ((2 >> 1 ^ 0 | 0) ^ 0 ^ 5); ++var6) {
                  if (((37769252 | 10061215) ^ 47830463) == 0) {
                        ;
                  }

                  if (!"please take a shower".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  for(var7 = (2125743746 | 1696947811) & 1037850037 ^ 1033131169; var7 < ((0 << 4 >> 1 | 928268801) >> 3 ^ 116033609); ++var7) {
                        var10000 = (double)(var2.getScaledWidth() - (((141 | 43) >>> 3 & 8 | 1143941635 | 1124499971) ^ 1194306225) + var7 * (5 >>> 4 >> 1 ^ 20));
                        var10001 = (double)(var2.getScaledHeight() - (93 >> 2 & 12 ^ 96) + var6 * ((8 << 1 & 15 & 808397692 | 2065637934) ^ 2065637946));
                        var10005 = new Color;
                        if ((((11694797 >> 4 ^ 638880) >>> 2 | 22172) ^ '\uf6bf') == 0) {
                              ;
                        }

                        if ((1449607553 << 3 << 1 ^ 1718884368) == 0) {
                              ;
                        }

                        var10005.<init>(906757124 ^ 363020435 ^ 598688919, (1214714462 >>> 4 ^ 75605806) & 390664 ^ 117405 ^ 269461, 1673687101 << 4 & 436551709 ^ 402980880, 66 >>> 2 << 2 >>> 3 ^ 108);
                        Hud.drawRoundedRect(var10000, var10001, 16.0D, 16.0D, 8.0D, var10005.getRGB());
                        if (((489567426 | 112016365) >>> 1 ^ 191075091 ^ 78908644) == 0) {
                              ;
                        }

                        var8 = (ItemStack)var4.get(var5++);
                        var10 = new Point;
                        var10003 = var2.getScaledWidth() - ((177 >>> 3 >> 3 & 1) << 3 ^ 178) + var7 * ((3 << 2 >>> 3 | 0) >>> 4 ^ 20);
                        var10004 = var2.getScaledHeight();
                        var11 = (96 & 45) >> 1 << 2 ^ 36;
                        if ((534406113 >>> 3 << 2 ^ 267203056) == 0) {
                              ;
                        }

                        var10.<init>(var10003, var10004 - var11 + var6 * ((11 | 5 | 8) & 14 ^ 26));
                        Hud.renderItem(var8, var10);
                  }
            }

            var5 = 6 >>> 1 << 2 ^ 30;

            int var9;
            for(var6 = (0 & 1283303468 | 1678997746) ^ 1678997745; var6 <= (((0 ^ 1223341895) >>> 2 << 4 | 578232464) >> 1 ^ 301989579); ++var6) {
                  for(var7 = (589588148 | 98000642) >> 4 >>> 3 ^ 5238527; var7 < ((5 | 4) << 3 >>> 4 ^ 11); ++var7) {
                        var10000 = (double)(var2.getScaledWidth() - ((106 << 3 & 647) >>> 2 ^ 50) + var7 * ((4 << 4 >> 3 | 0) ^ 28));
                        var9 = var2.getScaledHeight();
                        int var10002 = (46 << 4 >> 4 | 3) & 45 ^ 73;
                        if ((((1006913218 ^ 928373746 ^ 186359740) & 2234616) << 4 ^ 1606839780) != 0) {
                              ;
                        }

                        var9 -= var10002;
                        var10002 = var6 * (9 & 8 & 5 & 522726178 & 817555199 ^ 20);
                        if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                              ;
                        }

                        Hud.drawRoundedRect(var10000, (double)(var9 + var10002), 16.0D, 16.0D, 8.0D, (new Color((1077264787 | 925006787) ^ 1336579406 ^ 949841565, (714616832 ^ 303033283) >>> 2 ^ 237369072, 337646088 >> 2 << 2 & 43412371 ^ 512, ((31 | 5) >> 4 | 0) >> 4 & 1874073028 ^ 100)).getRGB());
                        if (((501947460 | 427756939) << 4 ^ 2056042522) != 0) {
                              ;
                        }

                        var8 = (ItemStack)var4.get(var5++);
                        var10 = new Point;
                        var10003 = var2.getScaledWidth() - (99 << 2 << 1 ^ 240 ^ 858) + var7 * ((17 >>> 1 | 0) ^ 28);
                        if (((1218877089 ^ 1037029760 ^ 174135211) & 1868500479 ^ 848618565) != 0) {
                              ;
                        }

                        var10004 = var2.getScaledHeight();
                        var11 = (((90 | 35) >>> 3 ^ 6) & 5) >> 1 ^ 100;
                        if ((356187009 >> 2 << 1 << 1 ^ 120745606 ^ 302552326) == 0) {
                              ;
                        }

                        var10004 -= var11;
                        int var10006 = (11 | 2) << 1 ^ 5 ^ 7;
                        if (((434824319 | 273630325) << 4 >> 1 ^ -1710661061) != 0) {
                              ;
                        }

                        var10.<init>(var10003, var10004 + var6 * var10006);
                        if (((607775817 >> 2 | 79782699) << 2 & 699935788 ^ 557329452) == 0) {
                              ;
                        }

                        Hud.renderItem(var8, var10);
                  }
            }

            var5 = (6 ^ 1) << 4 ^ 121;
            var6 = (1 ^ 0 | 0) ^ 3;

            while(true) {
                  if ((887117488 >> 1 << 3 & 553734394 ^ -1119567772) != 0) {
                        ;
                  }

                  var9 = ((0 >>> 3 | 416179444) >>> 1 | 22603427) ^ 226490105;
                  if (((1782866966 | 1161453805) ^ -241128201) != 0) {
                        ;
                  }

                  if (var6 > var9) {
                        return;
                  }

                  var7 = (359735484 | 249460427) & 525157587 ^ 525157587;

                  while(true) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        if (var7 >= ((4 & 1) >> 3 ^ 9)) {
                              if ((517636059 >>> 2 & 5657136 & 542060 ^ 32) == 0) {
                                    ;
                              }

                              ++var6;
                              break;
                        }

                        var10000 = (double)(var2.getScaledWidth() - (132 >> 3 >>> 3 ^ 176) + var7 * ((1 | 0) << 2 >>> 4 ^ 20));
                        if (!"idiot".equals("intentMoment")) {
                              ;
                        }

                        var10001 = (double)(var2.getScaledHeight() - ((80 >>> 1 ^ 31 | 36) >>> 3 ^ 98) + var6 * (((10 >> 1 | 3) ^ 2 | 1) ^ 17));
                        var10005 = new Color;
                        if ((97764147 ^ 10478592 ^ 59903167 ^ 251403096) != 0) {
                              ;
                        }

                        var10005.<init>((1640890009 | 325451025) >>> 1 ^ 972488652, 1162654623 >>> 1 << 4 >> 1 >>> 4 ^ 435577 ^ 22383006, ((1388345943 ^ 719026501) >>> 1 ^ 62806679) << 2 ^ -19960712, ((41 | 27) << 2 ^ 68 | 151) ^ 219);
                        Hud.drawRoundedRect(var10000, var10001, 16.0D, 16.0D, 8.0D, var10005.getRGB());
                        Hud.renderItem((ItemStack)var4.get(var5++), new Point(var2.getScaledWidth() - ((16 << 1 & 25) >> 1 ^ 178) + var7 * ((18 >>> 1 & 1) >>> 1 ^ 20), var2.getScaledHeight() - ((13 | 6 | 8) ^ 107) + var6 * ((9 & 1 ^ 0 | 0) >>> 2 ^ 20)));
                        ++var7;
                  }
            }
      }

      public static void renderItem(ItemStack var0, Point var1) {
            GlStateManager.enableTexture2D();
            GL11.glPushAttrib((93476 >> 1 << 2 >>> 2 | 6280) ^ 573082);
            GL11.glDisable((2321 & 1731) >> 2 >> 2 & 1292554653 ^ 761919235 ^ 761920274);
            if ((67470464 >> 3 ^ 7636479 ^ 16004463) == 0) {
                  ;
            }

            GlStateManager.clear((126 & 109) >> 2 ^ 19 ^ 2 ^ 266);
            GL11.glPopAttrib();
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderItem().zLevel = -150.0F;
            RenderHelper.enableGUIStandardItemLighting();
            if (((540294068 >> 2 ^ 19994256 ^ 151891537) << 4 ^ 43223394 ^ 25750434) == 0) {
                  ;
            }

            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(var0, var1.x, var1.y);
            RenderItem var10000 = Minecraft.getMinecraft().getRenderItem();
            FontRenderer var10001 = Minecraft.getMinecraft().fontRenderer;
            int var10003 = var1.x;
            int var10004 = var1.y;
            if (((2012164187 ^ 322815668 ^ 890861772) >> 3 >> 1 >>> 4 ^ 5360526) == 0) {
                  ;
            }

            var10000.renderItemOverlays(var10001, var0, var10003, var10004);
            RenderHelper.disableStandardItemLighting();
            Minecraft var2 = Minecraft.getMinecraft();
            if ((1163874443 >> 4 << 1 ^ 113848380 ^ 2102983790) != 0) {
                  ;
            }

            var10000 = var2.getRenderItem();
            if ((((4731013 ^ 576041) & 3518814) >> 4 ^ -922653384) != 0) {
                  ;
            }

            var10000.zLevel = 0.0F;
            GlStateManager.popMatrix();
            Hud.begin();
            if ((((597308368 | 12589761) & 464119317) >>> 1 ^ 5108648 ^ 25813664) == 0) {
                  ;
            }

      }

      static {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            renderer = GlyphPageFontRenderer.create("Arial", 32 << 1 ^ 15 ^ 103, (boolean)(((0 | 1457090603) & 1174571774 & 7715249) << 4 ^ 513), (boolean)((0 | 1352708913) >>> 3 >>> 2 >> 2 ^ 10568039), (boolean)((0 >> 1 & 468128885 ^ 389096567 | 290837652) ^ 393606390));
            myRenderer = GlyphPageFontRenderer.create("Times New Roman Baltic", (9 | 2 | 5) ^ 28, (boolean)(((0 << 3 & 452050874) << 2 ^ 1426971510) >> 4 ^ 89185718), (boolean)(0 >>> 3 ^ 324559396 ^ 324559397), (boolean)(((0 ^ 45092415) << 2 ^ 39753781) >> 4 & 680112 ^ 548865));
            fuckrenderer = GlyphPageFontRenderer.create("Courier New", 47 >>> 2 << 1 & 13 ^ 54, (boolean)(0 >> 1 & 1029109293 ^ 58108566 ^ 58108567), (boolean)((0 ^ 585859855 | 143124539) >>> 2 >>> 3 ^ 22511448), (boolean)(0 >> 1 >> 1 ^ 1));
            int var10001 = ((6 ^ 1) << 1 & 3 | 1) ^ 17;
            if ((1603566371 << 2 >>> 1 >> 1 ^ 529824547) == 0) {
                  ;
            }

            smallRenderer = GlyphPageFontRenderer.create("Courier New", var10001, (boolean)(((0 | 725763987) >> 4 | 2603473) ^ 40106512 ^ 13912040), (boolean)((0 & 1372609221) << 1 << 1 ^ 1), (boolean)((0 >>> 4 >> 4 ^ 1910148923) >>> 3 ^ 238768614));
            var10001 = 2 << 1 >> 2 ^ 17;
            if ((795136194 << 2 >>> 1 ^ 1590272388) == 0) {
                  ;
            }

            targetRenderer = GlyphPageFontRenderer.create("Courier New", var10001, (boolean)((0 | 714061519 | 444620263) ^ 728029572 ^ 300623466), (boolean)(0 >>> 4 >>> 4 << 4 ^ 1), (boolean)(((0 >> 3 ^ 559146374) << 4 | 207210201) & 298787554 ^ 290390753));
            hitStr = GlyphPageFontRenderer.create("Comic Sans MS", (0 >> 4 & 1665374773) >> 2 >> 4 ^ 24, (boolean)(0 >>> 2 << 1 & 1378570792 ^ 1), (boolean)((0 << 3 | 366117831) >>> 4 ^ 22882365), (boolean)(0 & 542690633 ^ 1971960865 ^ 1971960864));
      }
}
