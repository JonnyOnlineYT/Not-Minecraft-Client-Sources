//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import me.independed.inceptice.autosave.SaveLoad;
import me.independed.inceptice.clickguis.click.ClickGui;
import me.independed.inceptice.command.CommandManager;
import me.independed.inceptice.handler.RenderWorldLastHandler;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.proxy.CommonProxy;
import me.independed.inceptice.settings.SettingsManager;
import me.independed.inceptice.ui.Hud;
import me.independed.inceptice.util.HWID;
import me.zero.alpine.bus.type.Cancellable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

@Mod(
      modid = "inceptice",
      name = "Inceptice",
      version = "0.0.1",
      acceptableRemoteVersions = "*"
)
public class Main {
      public static SaveLoad saveLoad;
      public static ClickGui clickGui;
      public static Hud hud;
      @Instance
      public static Main Instance;
      public static CommandManager commandManager;
      public static SettingsManager settingsManager;
      public static RenderWorldLastHandler renderWorldLastHandler;
      public static ModuleManager moduleManager;
      private boolean checked = (566309174 ^ 167239152) << 3 ^ 1102538288;
      @SidedProxy(
            clientSide = "me.independed.inceptice.proxy.ClientProxy",
            serverSide = "me.independed.inceptice.proxy.CommonProxy"
      )
      public static CommonProxy proxy;

      @EventHandler
      public void preInit(FMLPreInitializationEvent var1) {
            if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                  ;
            }

      }

      @EventHandler
      public void postInit(FMLPostInitializationEvent var1) {
      }

      @SubscribeEvent
      public void key(KeyInputEvent var1) {
            if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
                  try {
                        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                              int var2 = Keyboard.getEventKey();
                              if ((((135274710 | 49139139) >> 3 ^ 1559849 | 17829510) ^ 213787725) != 0) {
                                    ;
                              }

                              if ((285216859 << 3 & 673140725 ^ 134218448) == 0) {
                                    ;
                              }

                              if (var2 <= 0) {
                                    return;
                              }

                              Iterator var3 = ModuleManager.modules.iterator();

                              while(true) {
                                    if ("i hope you catch fire ngl".equals("please take a shower")) {
                                    }

                                    if (((886868100 >> 4 | 25910243) >>> 3 ^ 7994237) == 0) {
                                          ;
                                    }

                                    if (!var3.hasNext()) {
                                          break;
                                    }

                                    Object var10000 = var3.next();
                                    if (!"stop skidding".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    Module var4 = (Module)var10000;
                                    if (!"ape covered in human flesh".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if (var4.getKey() == var2 && var2 > 0) {
                                          var4.toggle();
                                    }
                              }
                        }
                  } catch (Exception var5) {
                        var5.printStackTrace();
                  }

            }
      }

      @EventHandler
      public void init(FMLInitializationEvent var1) throws NoSuchAlgorithmException, IOException, HeadlessException, InterruptedException {
            long var10000;
            String var10001;
            if (!ModuleManager.absoluteLicense().exists()) {
                  (new File(ModuleManager.licenseFolder())).mkdirs();
                  PrintStream var2 = new PrintStream(new FileOutputStream(ModuleManager.absoluteLicense()));
                  Throwable var3 = null;
                  boolean var18 = false;

                  try {
                        var18 = true;
                        if (((2038639270 ^ 1612406190 | 78479925) ^ -991583190) != 0) {
                              ;
                        }

                        if ((1329601148 >>> 4 << 1 << 1 ^ -688253496) != 0) {
                              ;
                        }

                        var2.print("YOUR-LICENSE-KEY");
                        var18 = false;
                  } catch (Throwable var21) {
                        if ("please take a shower".equals("please take a shower")) {
                        }

                        var3 = var21;
                        throw var21;
                  } finally {
                        if (var18) {
                              if ("please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                              }

                              if (var2 != null) {
                                    if (var3 != null) {
                                          try {
                                                if ((1047394378 << 2 >> 1 ^ 969100261) != 0) {
                                                      ;
                                                }

                                                var2.close();
                                          } catch (Throwable var19) {
                                                var3.addSuppressed(var19);
                                          }
                                    } else {
                                          if (!"idiot".equals("please get a girlfriend and stop cracking plugins")) {
                                                ;
                                          }

                                          var2.close();
                                    }
                              }

                        }
                  }

                  if (var2 != null) {
                        if (var3 != null) {
                              try {
                                    var2.close();
                              } catch (Throwable var20) {
                                    if (((145890102 >>> 1 & 71435497 & 52583998) >>> 1 ^ 516) != 0) {
                                    }

                                    var3.addSuppressed(var20);
                              }
                        } else {
                              var2.close();
                        }
                  }

                  var10001 = "Please put your key into " + ModuleManager.absoluteLicense().getAbsolutePath();
                  int var10003 = 1134909507 >>> 2 >>> 1 << 3 ^ 1134909504;
                  if (((1208033281 | 822770903) >> 4 >>> 4 ^ -439508448) != 0) {
                        ;
                  }

                  JOptionPane.showMessageDialog((Component)null, var10001, "RageClient Error", var10003);
            } else if (ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8) == "YOUR-LICENSE-KEY") {
                  JOptionPane.showMessageDialog((Component)null, "Please put your key into " + ModuleManager.absoluteLicense().getAbsolutePath(), "RageClient Error", (1865034002 ^ 431574660) >>> 4 >> 2 ^ 31083934);
            } else {
                  var10000 = ModuleManager.getResponse(ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8));
                  long var28 = (5505562797L | 1459913916L) << 4 << 23 ^ 790775242135824167L;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  if (var10000 == var28) {
                        Cancellable.ok = (boolean)(0 & 1751148862 ^ 532659014 ^ 532659015);
                  }
            }

            String var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
            String var24 = "https://verify.xbrowniecodez.xyz";
            if (((624973701 >>> 4 | 4779358) >>> 4 ^ 2477783) == 0) {
                  ;
            }

            String var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
            URL var5 = new URL(var24);
            HttpsURLConnection var6 = (HttpsURLConnection)var5.openConnection();
            var6.setRequestMethod("POST");
            if (!"idiot".equals("stop skidding")) {
                  ;
            }

            var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
            var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var6.setRequestProperty("User-Agent", "Mozilla/5.0");
            var6.setDoOutput((boolean)((0 | 1449252600 | 1179151432) >>> 1 ^ 724893565));
            int var30 = (0 | 989638372) << 3 >>> 1 >> 4 >> 2 ^ 28297967;
            if (((1111172097 >>> 2 >> 4 | 11457916) ^ 28245500) == 0) {
                  ;
            }

            var6.setDoInput((boolean)var30);
            DataOutputStream var7 = new DataOutputStream(var6.getOutputStream());
            var7.writeBytes(var4);
            if (((1544792551 | 1413780192) >>> 3 >> 2 ^ 48414175) == 0) {
                  ;
            }

            var7.close();
            DataInputStream var25 = new DataInputStream;
            if (!"please go outside".equals("please take a shower")) {
                  ;
            }

            var25.<init>(var6.getInputStream());
            DataInputStream var8 = var25;
            StringBuilder var9 = new StringBuilder();
            int var10 = var8.read();

            while(true) {
                  if (((1069998896 >> 1 & 18602412) >> 2 ^ 4248674) == 0) {
                        ;
                  }

                  if (var10 == (173300719 >> 2 >> 2 << 2 ^ -43325177)) {
                        String var11 = var9.toString();
                        long var12 = Long.parseLong(var11);
                        var8.close();
                        if (var12 != ((4046189828L >>> 8 >> 3 | 281820L) << 3 ^ 6247319767L)) {
                              Thread.sleep(866537542624042167L << 30 << 21 & 5942695417715761796L ^ 5609434218613702655L);
                        }

                        String var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                        if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var23 = ModuleManager.readLicense(var26, StandardCharsets.UTF_8);
                        if (!"idiot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        var24 = "https://verify.xbrowniecodez.xyz";
                        StringBuilder var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID());
                        if (((((1089617560 | 883641089) & 1115698550) << 4 ^ 109016661) >> 3 ^ 1742826441) != 0) {
                              ;
                        }

                        var4 = var27.append("&license=").append(var23).append("&type=Rage").toString();
                        var5 = new URL(var24);
                        var6 = (HttpsURLConnection)var5.openConnection();
                        var6.setRequestMethod("POST");
                        if (((47485749 ^ 6718579) >> 1 ^ 4512855 ^ 18733044) == 0) {
                              ;
                        }

                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        var6.setDoOutput((boolean)((0 >>> 4 | 208502225) & 199608497 ^ 140855440));
                        if (((187688159 << 1 ^ 302038490 | 68221113) ^ 73399549) == 0) {
                              ;
                        }

                        var6.setDoInput((boolean)((0 >> 1 >> 2 << 2 & 806445666 | 2123831405) ^ 2123831404));
                        DataOutputStream var29 = new DataOutputStream;
                        if (((42278932 ^ 22652411) << 2 << 3 ^ -1180297452) != 0) {
                              ;
                        }

                        if ((1994404856 >> 4 >>> 1 >>> 2 >> 2 ^ -1666912024) != 0) {
                              ;
                        }

                        OutputStream var10002 = var6.getOutputStream();
                        if (((83065364 | 30479067) & 31844943 ^ 31550031) == 0) {
                              ;
                        }

                        var29.<init>(var10002);
                        var7 = var29;
                        if (((1858946693 ^ 434604005) >>> 4 >>> 1 ^ 62477835) == 0) {
                              ;
                        }

                        var7.writeBytes(var4);
                        var7.close();
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var8 = new DataInputStream(var6.getInputStream());
                        var27 = new StringBuilder;
                        if (!"nefariousMoment".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var27.<init>();
                        var9 = var27;

                        for(var10 = var8.read(); var10 != (583365218 >>> 3 >> 3 & 8553088 & 5521119 ^ -129); var10 = var8.read()) {
                              var9.append((char)var10);
                        }

                        var11 = var9.toString();
                        var12 = Long.parseLong(var11);
                        if (((1669553229 | 681817417) << 4 >>> 4 ^ -2023593083) != 0) {
                              ;
                        }

                        var8.close();
                        if (var12 != ((5547978925L | 5326548097L) & 6233983235L ^ 121564966L)) {
                              Thread.sleep((373145589922479301L >>> 3 & 3301298812672271L | 430266953965203L) ^ 1000082106020290660L);
                        }

                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                        var24 = "https://verify.xbrowniecodez.xyz";
                        if (((1048872 << 2 ^ 1391388) & 4440441 ^ 83925088) != 0) {
                              ;
                        }

                        var27 = new StringBuilder;
                        if ((1440931169 ^ 947114360 ^ 1557578920 ^ 826788017) == 0) {
                              ;
                        }

                        var27.<init>();
                        var4 = var27.append("hwid=").append(HWID.getHWID()).append("&license=").append(var23).append("&type=Rage").toString();
                        var5 = new URL(var24);
                        var6 = (HttpsURLConnection)var5.openConnection();
                        var6.setRequestMethod("POST");
                        int var32 = var4.length();
                        if ((1932939324 >> 1 >> 3 & 105866348 ^ 25933608 ^ -215863754) != 0) {
                              ;
                        }

                        var6.setRequestProperty("Content-length", String.valueOf(var32));
                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        if (((841584626 >>> 2 & 136867886 | 1456063) << 2 ^ -754130435) != 0) {
                              ;
                        }

                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        var6.setDoOutput((boolean)(0 >>> 2 << 4 ^ 1));
                        if ((52977809 << 1 ^ 51243238 ^ -1713920716) != 0) {
                              ;
                        }

                        var6.setDoInput((boolean)(0 << 1 >> 1 >> 1 ^ 1));
                        var7 = new DataOutputStream(var6.getOutputStream());
                        var7.writeBytes(var4);
                        var7.close();
                        var25 = new DataInputStream(var6.getInputStream());
                        if (!"please get a girlfriend and stop cracking plugins".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var8 = var25;
                        var27 = new StringBuilder();
                        if (((866892503 >> 4 | 31942114) << 1 ^ -176485244) != 0) {
                              ;
                        }

                        var9 = var27;
                        if (((3012246 ^ 1861777) << 3 ^ -746150953) != 0) {
                              ;
                        }

                        for(var10 = var8.read(); var10 != ((1756553793 << 3 >>> 3 >> 3 | 1555546) >> 3 ^ -2293644); var10 = var8.read()) {
                              var9.append((char)var10);
                              if (((237313821 | 4376343) << 1 << 4 ^ -855907360) == 0) {
                                    ;
                              }

                              if (((512 ^ 87 | 102) ^ 1784563931) != 0) {
                                    ;
                              }
                        }

                        var11 = var9.toString();
                        var12 = Long.parseLong(var11);
                        var8.close();
                        long var40;
                        int var34 = (var40 = var12 - (3405562779L ^ 1181810059L ^ 1066854144L ^ 7645642295L)) == 0L ? 0 : (var40 < 0L ? -1 : 1);
                        if (!"idiot".equals("ape covered in human flesh")) {
                              ;
                        }

                        if (var34 != 0) {
                              if ((1880515594 << 1 >>> 2 ^ 940257797) == 0) {
                                    ;
                              }

                              Thread.sleep(((937001924406397805L | 644682980530245767L) << 1 | 1916712265151327647L) ^ 1593510130486100000L);
                        }

                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                        var24 = "https://verify.xbrowniecodez.xyz";
                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID()).append("&license=").append(var23);
                        if (!"ape covered in human flesh".equals("intentMoment")) {
                              ;
                        }

                        var4 = var27.append("&type=Rage").toString();
                        var5 = new URL(var24);
                        var6 = (HttpsURLConnection)var5.openConnection();
                        var6.setRequestMethod("POST");
                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                        if (((970671700 ^ 116568316) >>> 3 << 2 ^ 529857364) == 0) {
                              ;
                        }

                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        var6.setDoOutput((boolean)((0 ^ 1089302246) >>> 1 ^ 544651122));
                        var6.setDoInput((boolean)(((0 | 1082856857) & 1030616516 & 300384) >> 1 >> 2 ^ 673));
                        var7 = new DataOutputStream(var6.getOutputStream());
                        if ((((1875999107 | 305502211) >> 4 | 22139261) ^ -1026160412) != 0) {
                              ;
                        }

                        var7.writeBytes(var4);
                        var7.close();
                        var8 = new DataInputStream(var6.getInputStream());
                        var9 = new StringBuilder();

                        for(var10 = var8.read(); var10 != (1846936019 >> 2 << 1 << 3 >>> 1 ^ -1546388385); var10 = var8.read()) {
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                                    ;
                              }

                              if (!"your mom your dad the one you never had".equals("please take a shower")) {
                                    ;
                              }

                              var9.append((char)var10);
                        }

                        if ((931791339 >>> 2 ^ 223042308 ^ -563142040) != 0) {
                              ;
                        }

                        if ((1085450 >> 4 ^ -846658353) != 0) {
                              ;
                        }

                        var11 = var9.toString();
                        var12 = Long.parseLong(var11);
                        var8.close();
                        if (var12 != (3990164148L << 21 >>> 2 ^ 2092000741287719L)) {
                              Thread.sleep((415099602424567106L ^ 398963333417914761L) >> 27 ^ 1000000000112476858L);
                        }

                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                        var24 = "https://verify.xbrowniecodez.xyz";
                        var27 = new StringBuilder();
                        if (((1909139784 << 3 | 28762944) >> 1 << 4 ^ 2147441152) == 0) {
                              ;
                        }

                        var27 = var27.append("hwid=").append(HWID.getHWID()).append("&license=");
                        if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                              ;
                        }

                        var4 = var27.append(var23).append("&type=Rage").toString();
                        if ((1883284678 >>> 2 ^ 122583142 ^ -1170890302) != 0) {
                              ;
                        }

                        var5 = new URL(var24);
                        var6 = (HttpsURLConnection)var5.openConnection();
                        if (!"ape covered in human flesh".equals("nefariousMoment")) {
                              ;
                        }

                        var6.setRequestMethod("POST");
                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        if ((((46153760 | 22304473) & 23246539 | 2734680) ^ 23707353) == 0) {
                              ;
                        }

                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        var6.setDoOutput((boolean)((0 ^ 872477090 ^ 228011335) >>> 1 & 213677010 ^ 210264659));
                        var6.setDoInput((boolean)(((0 << 4 << 2 ^ 60262245) >> 1 | 19776066) ^ 32490483));
                        if (((760539635 >> 4 >> 3 ^ 2376347) >> 2 ^ 1392012 ^ 690014) == 0) {
                              ;
                        }

                        if (((1503841743 >> 1 | 249382968) ^ 786263807) == 0) {
                              ;
                        }

                        var7 = new DataOutputStream(var6.getOutputStream());
                        var7.writeBytes(var4);
                        var7.close();
                        var8 = new DataInputStream(var6.getInputStream());
                        if (!"yo mama name maurice".equals("you're dogshit")) {
                              ;
                        }

                        var9 = new StringBuilder();
                        if (((671408194 >> 3 | 60459637) >> 3 ^ 15946703) == 0) {
                              ;
                        }

                        for(var10 = var8.read(); var10 != (1119213472 >>> 3 >>> 3 << 4 ^ 277098046 ^ 1629203 ^ -3246030); var10 = var8.read()) {
                              if (!"please take a shower".equals("please dont crack my plugin")) {
                                    ;
                              }

                              var9.append((char)var10);
                              if (((78215745 >> 4 << 3 ^ 16200185) >>> 1 ^ 134452620) != 0) {
                                    ;
                              }
                        }

                        var11 = var9.toString();
                        if ((((2095493328 ^ 352793073 | 1662951619) >> 3 | 57815567) ^ 260044351) == 0) {
                              ;
                        }

                        var12 = Long.parseLong(var11);
                        if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var8.close();
                        if (var12 != (555123302L >> 18 << 4 ^ 6252487543L)) {
                              Thread.sleep((100284078457957364L ^ 69630197085902385L) >> 7 >>> 2 ^ 999938894468876385L);
                        }

                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                        var24 = "https://verify.xbrowniecodez.xyz";
                        var27 = new StringBuilder;
                        if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var27.<init>();
                        var27 = var27.append("hwid=").append(HWID.getHWID()).append("&license=").append(var23).append("&type=Rage");
                        if (((978351006 ^ 358585006 | 686825893) << 3 << 1 ^ -1213550239) != 0) {
                              ;
                        }

                        var4 = var27.toString();
                        var5 = new URL(var24);
                        if (!"your mom your dad the one you never had".equals("please dont crack my plugin")) {
                              ;
                        }

                        var6 = (HttpsURLConnection)var5.openConnection();
                        var6.setRequestMethod("POST");
                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        if ((((1627357692 >>> 4 & 85769674) >>> 2 | 14581311) ^ 31424063) == 0) {
                              ;
                        }

                        if (!"please go outside".equals("you're dogshit")) {
                              ;
                        }

                        var6.setDoOutput((boolean)((0 >>> 1 & 622141521) << 2 ^ 1));
                        var6.setDoInput((boolean)((0 & 1698805393) >>> 2 ^ 1101823758 ^ 1101823759));
                        var7 = new DataOutputStream(var6.getOutputStream());
                        if ((1360593940 << 3 ^ -2000150368) == 0) {
                              ;
                        }

                        var7.writeBytes(var4);
                        var7.close();
                        if (((1966129810 >> 4 | 38620272) ^ 123686009) == 0) {
                              ;
                        }

                        var8 = new DataInputStream(var6.getInputStream());
                        var9 = new StringBuilder();

                        for(var10 = var8.read(); var10 != (1799692971 >> 3 ^ 80160646 ^ -162497492); var10 = var8.read()) {
                              var9.append((char)var10);
                        }

                        var11 = var9.toString();
                        var10000 = Long.parseLong(var11);
                        if (((94904772 << 4 & 416081668) << 3 & 351982618 ^ 613616856) != 0) {
                              ;
                        }

                        var12 = var10000;
                        var8.close();
                        if (var12 != ((4958971971L >>> 23 & 182L) >>> 11 ^ 6252521255L)) {
                              var10000 = 334789253847301982L >> 2 >> 3 ^ 992372997703762437L;
                              if ((646166888 << 3 >> 3 ^ 1032925253) != 0) {
                                    ;
                              }

                              Thread.sleep(var10000);
                        }

                        File var37 = ModuleManager.absoluteLicense();
                        if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        var23 = ModuleManager.readLicense(var37.getAbsolutePath(), StandardCharsets.UTF_8);
                        var24 = "https://verify.xbrowniecodez.xyz";
                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID()).append("&license=").append(var23);
                        if (((66050 | 31049) & 763 ^ -660038401) != 0) {
                              ;
                        }

                        var4 = var27.append("&type=Rage").toString();
                        URL var38 = new URL;
                        if ((16392 >> 3 ^ 1265 ^ 3312) == 0) {
                              ;
                        }

                        var38.<init>(var24);
                        var5 = var38;
                        var6 = (HttpsURLConnection)var5.openConnection();
                        if (((646506091 | 324645882) >> 1 ^ 1415293035) != 0) {
                              ;
                        }

                        var6.setRequestMethod("POST");
                        if (((193307342 ^ 170332017) >>> 3 >> 1 ^ 1714907) == 0) {
                              ;
                        }

                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                        if (((2110905723 | 1595026699) >>> 4 ^ 124566556 ^ 9536459) == 0) {
                              ;
                        }

                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                        var6.setDoOutput((boolean)(((0 | 303808965) & 135394229) >>> 1 ^ 581827));
                        if (((1994030184 ^ 1286878308) >>> 1 & 16448240 ^ 720392 ^ -1403018338) != 0) {
                              ;
                        }

                        var6.setDoInput((boolean)((0 & 1933048337) >>> 3 ^ 1));
                        var7 = new DataOutputStream(var6.getOutputStream());
                        var7.writeBytes(var4);
                        var7.close();
                        var25 = new DataInputStream;
                        if (!"minecraft".equals("please go outside")) {
                              ;
                        }

                        if (((1044736602 ^ 33425225 | 78380939) ^ -168581249) != 0) {
                              ;
                        }

                        if (((1444439945 >> 1 >>> 1 | 112266816) >> 1 ^ 198938609) == 0) {
                              ;
                        }

                        var25.<init>(var6.getInputStream());
                        var8 = var25;
                        if (!"intentMoment".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var9 = new StringBuilder();
                        if ((639031659 >> 3 << 2 & 116807596 ^ -347095402) != 0) {
                              ;
                        }

                        var10 = var8.read();

                        while(true) {
                              if ((4162 << 2 ^ 377979445) != 0) {
                                    ;
                              }

                              if (var10 == ((870906404 >> 4 >>> 4 ^ 264765) >> 1 ^ -1831268)) {
                                    var11 = var9.toString();
                                    var12 = Long.parseLong(var11);
                                    var8.close();
                                    if (var12 != (((420449459L ^ 231660139L ^ 33864775L ^ 132899366L) >>> 9 | 170455L) ^ 6252099824L)) {
                                          if ((568325226 >> 4 >>> 3 >> 3 ^ 555005) == 0) {
                                                ;
                                          }

                                          Thread.sleep((834233613211172307L & 630841313425301758L & 154129761737614473L) >> 6 >> 28 ^ 999999999999983615L);
                                    }

                                    var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                    if ((((280105627 | 274124203) >>> 3 | 21103123) ^ 418083375) != 0) {
                                          ;
                                    }

                                    var23 = ModuleManager.readLicense(var26, StandardCharsets.UTF_8);
                                    var24 = "https://verify.xbrowniecodez.xyz";
                                    var27 = new StringBuilder();
                                    if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }

                                    var4 = var27.append("hwid=").append(HWID.getHWID()).append("&license=").append(var23).append("&type=Rage").toString();
                                    var38 = new URL;
                                    if ((((744008187 >>> 4 ^ 43830665) << 4 | 72391070) ^ 1422396113) != 0) {
                                          ;
                                    }

                                    var38.<init>(var24);
                                    if ((1781879701 >>> 4 ^ 44104756 ^ 67348749) == 0) {
                                          ;
                                    }

                                    var5 = var38;
                                    var6 = (HttpsURLConnection)var5.openConnection();
                                    if (((799594044 | 543542731) >> 2 >> 4 ^ 12564351) == 0) {
                                          ;
                                    }

                                    var6.setRequestMethod("POST");
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                    if (!"please go outside".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                    var6.setDoOutput((boolean)((0 | 823850841 | 227414469) ^ 1033828316));
                                    var6.setDoInput((boolean)((0 | 1494641117) & 498314969 ^ 379318338 ^ 260692122));
                                    var7 = new DataOutputStream(var6.getOutputStream());
                                    if (((2130604184 >>> 1 | 952053687) >>> 3 ^ -1756642305) != 0) {
                                          ;
                                    }

                                    var7.writeBytes(var4);
                                    var7.close();
                                    var25 = new DataInputStream;
                                    if ((1737537282 >>> 3 >>> 4 >> 3 ^ 1203335 ^ -643389332) != 0) {
                                          ;
                                    }

                                    var25.<init>(var6.getInputStream());
                                    var8 = var25;
                                    var9 = new StringBuilder();
                                    var10 = var8.read();

                                    while(true) {
                                          if (((1043990782 ^ 546274875 ^ 451256565) >> 4 >>> 4 ^ 282838) == 0) {
                                                ;
                                          }

                                          if (((1656952293 ^ 750206464) << 2 ^ 969977748) == 0) {
                                                ;
                                          }

                                          if (((1466832180 >> 1 & 337500098) >> 1 ^ 688449) == 0) {
                                                ;
                                          }

                                          if (var10 == ((898468944 >> 4 & 32101167 | 4433382) ^ -21755368)) {
                                                var11 = var9.toString();
                                                var12 = Long.parseLong(var11);
                                                var8.close();
                                                if (var12 != ((3188161010L >>> 7 << 27 << 2 & 12270625161340824L) >>> 14 ^ 753581025063L)) {
                                                      Thread.sleep(791934528186256918L >>> 22 >>> 13 & 13209702L ^ 999999999996227583L);
                                                }

                                                var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                var24 = "https://verify.xbrowniecodez.xyz";
                                                var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                if (((2042847673 | 737013318) >> 4 >>> 2 << 3 ^ 259915256) == 0) {
                                                      ;
                                                }

                                                var5 = new URL(var24);
                                                var6 = (HttpsURLConnection)var5.openConnection();
                                                var6.setRequestMethod("POST");
                                                if (!"stringer is a good obfuscator".equals("please go outside")) {
                                                      ;
                                                }

                                                var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                if (!"please dont crack my plugin".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                if (((1189412595 | 1125348641) >> 1 >> 1 ^ 301858812) == 0) {
                                                      ;
                                                }

                                                var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                var6.setDoOutput((boolean)((0 & 1186009316 ^ 765717675 | 697744307) ^ 767033274));
                                                var6.setDoInput((boolean)((0 >> 2 ^ 2042920565) & 767957787 ^ 700717584));
                                                var7 = new DataOutputStream(var6.getOutputStream());
                                                if ((42534946 ^ -1382959370) != 0) {
                                                      ;
                                                }

                                                var7.writeBytes(var4);
                                                var7.close();
                                                var8 = new DataInputStream(var6.getInputStream());
                                                var9 = new StringBuilder();
                                                if ((((87970980 | 24057104) << 4 >>> 4 | 56541502) ^ 2033285578) != 0) {
                                                      ;
                                                }

                                                var10 = var8.read();

                                                while(true) {
                                                      if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                            ;
                                                      }

                                                      if (var10 == (906151160 ^ 893868902 ^ 5281158 ^ -51710489)) {
                                                            var11 = var9.toString();
                                                            var12 = Long.parseLong(var11);
                                                            var8.close();
                                                            if (!"stop skidding".equals("please dont crack my plugin")) {
                                                                  ;
                                                            }

                                                            if (var12 != (((984600582L | 859843635L | 389786751L) & 575586824L) >>> 20 ^ 6252520707L)) {
                                                                  Thread.sleep(((273088858948877904L ^ 104552600397357707L | 62831103504965040L) & 198314102504265566L) << 13 ^ 896412603533279231L);
                                                            }

                                                            var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                            var24 = "https://verify.xbrowniecodez.xyz";
                                                            var27 = new StringBuilder;
                                                            if (!"please get a girlfriend and stop cracking plugins".equals("you're dogshit")) {
                                                                  ;
                                                            }

                                                            var27.<init>();
                                                            var4 = var27.append("hwid=").append(HWID.getHWID()).append("&license=").append(var23).append("&type=Rage").toString();
                                                            var5 = new URL(var24);
                                                            var6 = (HttpsURLConnection)var5.openConnection();
                                                            if (((887152186 >> 4 ^ 37392165) >> 4 << 4 >> 2 ^ -437426347) != 0) {
                                                                  ;
                                                            }

                                                            var6.setRequestMethod("POST");
                                                            var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                            if (!"stringer is a good obfuscator".equals("i hope you catch fire ngl")) {
                                                                  ;
                                                            }

                                                            if ((169133711 >>> 4 >> 4 << 3 >> 3 ^ 526225118) != 0) {
                                                                  ;
                                                            }

                                                            var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                            var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                            if (((806314181 >> 1 & 396561274) >>> 3 ^ 815216142) != 0) {
                                                                  ;
                                                            }

                                                            var6.setDoOutput((boolean)((0 | 775132274) >>> 3 ^ 31930126 ^ 38381621 ^ 107539892));
                                                            var6.setDoInput((boolean)((0 & 848887135) >>> 1 ^ 292043047 ^ 292043046));
                                                            var7 = new DataOutputStream(var6.getOutputStream());
                                                            var7.writeBytes(var4);
                                                            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                                                                  ;
                                                            }

                                                            var7.close();
                                                            var8 = new DataInputStream(var6.getInputStream());
                                                            var9 = new StringBuilder();

                                                            for(var10 = var8.read(); var10 != (2019452494 >> 4 >> 3 ^ 11591008 ^ -4219309); var10 = var8.read()) {
                                                                  var9.append((char)var10);
                                                            }

                                                            var26 = var9.toString();
                                                            if ((1027332895 >>> 4 >>> 3 << 2 >>> 4 << 4 ^ 32104144) == 0) {
                                                                  ;
                                                            }

                                                            var11 = var26;
                                                            if (((407324068 ^ 24441325 | 190488329) >> 4 >>> 4 ^ 1801151) == 0) {
                                                                  ;
                                                            }

                                                            var12 = Long.parseLong(var11);
                                                            var8.close();
                                                            if (var12 != (4478713355L >>> 4 ^ 185811439L ^ 6158392616L)) {
                                                                  Thread.sleep(74270325039649814L << 19 << 21 << 30 ^ 2211129378971539787L ^ 1391388839811262132L);
                                                            }

                                                            if ((((671875108 | 152360677) >> 4 & 19678947) >>> 1 ^ -1565432495) != 0) {
                                                                  ;
                                                            }

                                                            var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                            var24 = "https://verify.xbrowniecodez.xyz";
                                                            var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                            var38 = new URL;
                                                            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                  ;
                                                            }

                                                            var38.<init>(var24);
                                                            var5 = var38;
                                                            var6 = (HttpsURLConnection)var5.openConnection();
                                                            var6.setRequestMethod("POST");
                                                            var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                            var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                            if (((732287064 << 4 >>> 3 << 4 | 708486453) ^ 2126163765) == 0) {
                                                                  ;
                                                            }

                                                            var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                            var6.setDoOutput((boolean)((0 | 355704440) << 2 ^ 1422817761));
                                                            var6.setDoInput((boolean)((0 >>> 2 >> 2 & 692732244) >> 4 >> 1 ^ 1));
                                                            if (!"ape covered in human flesh".equals("yo mama name maurice")) {
                                                                  ;
                                                            }

                                                            var7 = new DataOutputStream(var6.getOutputStream());
                                                            var7.writeBytes(var4);
                                                            if (((352489664 >>> 4 ^ 18625350) & 2368349 ^ 262728) == 0) {
                                                                  ;
                                                            }

                                                            var7.close();
                                                            var25 = new DataInputStream(var6.getInputStream());
                                                            if (!"shitted on you harder than archybot".equals("shitted on you harder than archybot")) {
                                                                  ;
                                                            }

                                                            var8 = var25;
                                                            var9 = new StringBuilder();
                                                            var10 = var8.read();

                                                            while(true) {
                                                                  if (((1304926774 | 813955789) >>> 4 ^ -66632532) != 0) {
                                                                        ;
                                                                  }

                                                                  if (var10 == ((1006225986 >> 4 << 4 ^ 608643769 | 342458883) >>> 2 ^ -134215871)) {
                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != (((4397433901L ^ 1125997271L) & 3808000987L) << 28 ^ 288236616012392231L)) {
                                                                              Thread.sleep((947140911047710796L & 124486556442730361L & 45937248446074683L | 2262639122555803L) ^ 993251972718285924L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID());
                                                                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please take a shower")) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append("&license=").append(var23).append("&type=Rage").toString();
                                                                        var5 = new URL(var24);
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(0 >> 2 >> 4 ^ 1));
                                                                        var6.setDoInput((boolean)((0 ^ 186018950 ^ 143904069) >> 4 ^ 3693085));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream;
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("your mom your dad the one you never had")) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var6.getInputStream());
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        if (!"stop skidding".equals("stringer is a good obfuscator")) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var8.read(); var10 != (690798869 >>> 3 << 4 ^ 1218736864 ^ -452849857); var10 = var8.read()) {
                                                                              var9.append((char)var10);
                                                                              if ((1504825855 >>> 2 ^ 371704504 ^ 3435442 ^ -1748118236) != 0) {
                                                                                    ;
                                                                              }
                                                                        }

                                                                        var11 = var9.toString();
                                                                        if ((1443290911 >> 3 & 90078110 & 2736503 ^ -285413707) != 0) {
                                                                              ;
                                                                        }

                                                                        if ((393455635 << 3 >> 3 ^ -1066604604) != 0) {
                                                                              ;
                                                                        }

                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (!"idiot".equals("intentMoment")) {
                                                                              ;
                                                                        }

                                                                        if (var12 != ((445692595L ^ 143006507L | 268111191L) >> 17 >>> 10 >> 26 ^ 6252521255L)) {
                                                                              Thread.sleep((770114445904151148L & 521796254481064198L | 6512478530779338L) << 4 << 23 ^ -8544507762973605889L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                        var38 = new URL(var24);
                                                                        if (((265637781 | 148524265) >> 1 ^ 1900379782) != 0) {
                                                                              ;
                                                                        }

                                                                        var5 = var38;
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if ((92506170 >>> 2 << 4 >> 4 ^ 23126542) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        if (!"you're dogshit".equals("minecraft")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(((0 ^ 1087916124) >>> 3 << 2 | 338696004 | 486243536) ^ 1023377405));
                                                                        if (((1787738506 >>> 1 | 338621952 | 204516052) & 399348151 ^ 357404821) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setDoInput((boolean)(0 >>> 2 << 3 >>> 2 ^ 1));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream;
                                                                        if (!"nefariousMoment".equals("your mom your dad the one you never had")) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var6.getInputStream());
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        var34 = var8.read();
                                                                        if (((1803582593 ^ 918811030) << 4 ^ -734473872) == 0) {
                                                                              ;
                                                                        }

                                                                        char var31;
                                                                        for(var10 = var34; var10 != ((2056765698 << 3 & 2086672572 & 1262791980 ^ 246882624) >> 3 ^ -165078313); var10 = var8.read()) {
                                                                              var31 = (char)var10;
                                                                              if (!"stop skidding".equals("intentMoment")) {
                                                                                    ;
                                                                              }

                                                                              var9.append(var31);
                                                                        }

                                                                        var26 = var9.toString();
                                                                        if (((2126900859 >>> 1 | 808424748) ^ -222127510) != 0) {
                                                                              ;
                                                                        }

                                                                        var11 = var26;
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (!"stringer is a good obfuscator".equals("minecraft")) {
                                                                              ;
                                                                        }

                                                                        if (var12 != (((5144157333L >>> 27 ^ 17L) >> 27 | 5626867799134549931L) << 22 ^ -7144010914018236633L)) {
                                                                              Thread.sleep(370624737710680953L ^ 123742229571022369L ^ 189082982029960900L ^ 859150212737293411L);
                                                                              if (((919779821 >>> 3 | 17189804 | 105486542) ^ 49397265 ^ 86925806) == 0) {
                                                                                    ;
                                                                              }
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID());
                                                                        if ((1878877860 >> 1 << 2 ^ -537211576) == 0) {
                                                                              ;
                                                                        }

                                                                        var27 = var27.append("&license=").append(var23);
                                                                        if (!"idiot".equals("please take a shower")) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append("&type=Rage").toString();
                                                                        var5 = new URL(var24);
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        if (!"yo mama name maurice".equals("idiot")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(((0 << 2 ^ 410573039) & 203284567) >>> 2 ^ 33959952));
                                                                        if ((539231256 >>> 4 ^ 33701953) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setDoInput((boolean)(0 << 4 >> 4 << 4 ^ 1));
                                                                        var29 = new DataOutputStream;
                                                                        var10002 = var6.getOutputStream();
                                                                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please get a girlfriend and stop cracking plugins")) {
                                                                              ;
                                                                        }

                                                                        var29.<init>(var10002);
                                                                        var7 = var29;
                                                                        var7.writeBytes(var4);
                                                                        if ((((1966635920 | 462624988) ^ 761081173) >>> 3 ^ -1199168274) != 0) {
                                                                              ;
                                                                        }

                                                                        var7.close();
                                                                        var8 = new DataInputStream(var6.getInputStream());
                                                                        var9 = new StringBuilder();

                                                                        for(var10 = var8.read(); var10 != ((1899729774 << 3 | 103350770) ^ 1554857576 ^ 749689445); var10 = var8.read()) {
                                                                              if ((12458 >> 4 & 311 ^ 97 ^ 323946993) != 0) {
                                                                                    ;
                                                                              }

                                                                              if (!"stop skidding".equals("minecraft")) {
                                                                                    ;
                                                                              }

                                                                              var9.append((char)var10);
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != ((3939838873L ^ 2245913924L ^ 1471542486L | 490283524L) ^ 5520807208L)) {
                                                                              Thread.sleep((317646604120842872L & 95144322934553502L | 17957347684084599L) >>> 21 ^ 999999985800515936L);
                                                                        }

                                                                        var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                                                        if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                              ;
                                                                        }

                                                                        var23 = ModuleManager.readLicense(var26, StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        if ((248226975 >> 2 << 4 >>> 4 ^ 1062839649) != 0) {
                                                                              ;
                                                                        }

                                                                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID()).append("&license=");
                                                                        if ((866051026 << 1 << 4 >>> 3 ^ 1659764146) != 0) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append(var23).append("&type=Rage").toString();
                                                                        var5 = new URL(var24);
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        var6.setRequestMethod("POST");
                                                                        if (!"buy a domain and everything else you need at namecheap.com".equals("you probably spell youre as your")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)((0 ^ 557108089) & 1745119 ^ 1081432));
                                                                        var6.setDoInput((boolean)((0 & 1009938188) << 1 >>> 1 ^ 1));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        if (((735296282 >>> 3 | 18181799 | 61711773) ^ 134217727) == 0) {
                                                                              ;
                                                                        }

                                                                        var25 = new DataInputStream;
                                                                        InputStream var35 = var6.getInputStream();
                                                                        if (((1983155159 | 1942042766) >> 4 >>> 1 ^ -393674007) != 0) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var35);
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        if (((970777506 | 10092405) << 2 >>> 3 ^ 1400934150) != 0) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var8.read(); var10 != (1766917 >>> 1 << 4 ^ 13246553 ^ -1937530); var10 = var8.read()) {
                                                                              var9.append((char)var10);
                                                                        }

                                                                        if ((1753603689 >> 4 >>> 2 >> 1 ^ -518726888) != 0) {
                                                                              ;
                                                                        }

                                                                        if ((((157198511 | 138430716) ^ 82288496 | 159217973) << 2 ^ 938868476) == 0) {
                                                                              ;
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != (2453774815L << 7 >>> 10 ^ 79017878L ^ 5944403082L)) {
                                                                              Thread.sleep((994613840901410791L << 25 >> 22 & 150443074196L & 88663165745L) << 28 ^ 981844722268241919L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var27 = (new StringBuilder()).append("hwid=");
                                                                        if (!"you probably spell youre as your".equals("nefariousMoment")) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append(HWID.getHWID()).append("&license=").append(var23).append("&type=Rage").toString();
                                                                        var38 = new URL;
                                                                        if (!"please take a shower".equals("please dont crack my plugin")) {
                                                                              ;
                                                                        }

                                                                        if (!"please go outside".equals("nefariousMoment")) {
                                                                              ;
                                                                        }

                                                                        var38.<init>(var24);
                                                                        var5 = var38;
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if ((653425077 >>> 1 >>> 2 >>> 4 ^ 5104883) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(((0 | 1078313659) & 55485203) >>> 3 >> 4 ^ '褅'));
                                                                        var6.setDoInput((boolean)((0 >> 1 & 982181059 | 160295770) ^ 160295771));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var8 = new DataInputStream(var6.getInputStream());
                                                                        if ((((1163965629 ^ 1112727483) << 4 ^ 1109189795) << 4 ^ 329600048) == 0) {
                                                                              ;
                                                                        }

                                                                        var27 = new StringBuilder;
                                                                        if ((546538004 << 1 ^ 286639875 ^ 1345504043) == 0) {
                                                                              ;
                                                                        }

                                                                        var27.<init>();
                                                                        var9 = var27;
                                                                        if (((786893605 << 3 ^ 580455473) & 189947783 ^ 16798977) == 0) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var8.read(); var10 != (378063895 >>> 4 << 3 >>> 4 & 10694871 & 6951165 ^ -2097217); var10 = var8.read()) {
                                                                              var9.append((char)var10);
                                                                        }

                                                                        if ((120085322 >>> 2 ^ 468455 ^ 242512092) != 0) {
                                                                              ;
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != ((4452074379L ^ 4051597081L | 3201214520L) >> 9 >> 5 ^ 6252272793L)) {
                                                                              Thread.sleep(((947681481046584609L >>> 7 & 4686326195564857L) << 3 | 34880847708634879L) ^ 944451081126035712L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID()).append("&license=").append(var23);
                                                                        if ((1491000191 << 4 >> 3 ^ 402122214) != 0) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append("&type=Rage").toString();
                                                                        var38 = new URL;
                                                                        if ((25444354 >> 4 ^ 1590272) == 0) {
                                                                              ;
                                                                        }

                                                                        var38.<init>(var24);
                                                                        var5 = var38;
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if ((((178348564 | 308390) >> 4 | 2132935) << 1 ^ 2012618290) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        if (!"please get a girlfriend and stop cracking plugins".equals("yo mama name maurice")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        if (!"please dont crack my plugin".equals("please take a shower")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(0 >> 2 & 676574424 ^ 1));
                                                                        var6.setDoInput((boolean)((0 ^ 2046410395) & 1403674571 & 624335757 ^ 18881160));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        if ((175464548 << 4 >>> 3 ^ 41466285 ^ 352518060 ^ 59803337) == 0) {
                                                                              ;
                                                                        }

                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var8 = new DataInputStream(var6.getInputStream());
                                                                        var9 = new StringBuilder();

                                                                        for(var10 = var8.read(); var10 != (2034468990 >>> 2 << 3 >> 2 ^ 56507329); var10 = var8.read()) {
                                                                              var9.append((char)var10);
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != ((897736271L | 739003418L) >>> 16 >>> 17 ^ 6252521255L)) {
                                                                              Thread.sleep((642076148566209226L >>> 18 ^ 1264519449849L | 3417458308188L | 1671936856333L) >>> 10 ^ 999999998275624962L);
                                                                        }

                                                                        var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                                                        if (((661094766 ^ 612885329) << 4 >> 1 ^ 520294904) == 0) {
                                                                              ;
                                                                        }

                                                                        var23 = ModuleManager.readLicense(var26, StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                        var5 = new URL(var24);
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        if ((((818960527 ^ 180272953) >>> 3 >> 4 | 4521785) ^ 1062248647) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)((0 | 482895271) >> 3 ^ 60361909));
                                                                        if (!"intentMoment".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                                              ;
                                                                        }

                                                                        var6.setDoInput((boolean)((0 | 408700417) >>> 2 ^ 102175105));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        if (!"yo mama name maurice".equals("yo mama name maurice")) {
                                                                              ;
                                                                        }

                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream(var6.getInputStream());
                                                                        if (((9508110 ^ 9140212) >>> 2 >>> 1 >> 4 ^ 13509) == 0) {
                                                                              ;
                                                                        }

                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        if (((637841674 | 57258254) >>> 3 & 21440243 ^ 1797831910) != 0) {
                                                                              ;
                                                                        }

                                                                        var34 = var8.read();
                                                                        if ((139215044 << 4 ^ -274450459) != 0) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var34; var10 != ((1510696943 ^ 375936716) >>> 3 >>> 2 ^ -40049034); var10 = var8.read()) {
                                                                              if ((((730582741 | 204253448 | 678743707) ^ 51911462) >> 4 ^ 1144379339) != 0) {
                                                                                    ;
                                                                              }

                                                                              var9.append((char)var10);
                                                                              if ((((1348707896 ^ 737483551) << 3 & 1939682205 | 438264670) ^ -1300656673) != 0) {
                                                                                    ;
                                                                              }
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        if (((36721047 | 16004072 | 47534311) ^ 124444754) != 0) {
                                                                              ;
                                                                        }

                                                                        var8.close();
                                                                        if (!"please take a shower".equals("nefariousMoment")) {
                                                                              ;
                                                                        }

                                                                        if (var12 != ((1654535350L ^ 809701827L | 832217344L) ^ 4419895890L)) {
                                                                              Thread.sleep((373279052014168097L & 260587443749348003L) >>> 28 >>> 24 ^ 999999999999999983L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        if (((1862609230 | 337922766 | 1688949418) >> 3 ^ 144349833 ^ -2053881714) != 0) {
                                                                              ;
                                                                        }

                                                                        var27 = new StringBuilder();
                                                                        if (((467131600 >>> 1 ^ 147026692 ^ 60754206) >> 1 ^ 1783569583) != 0) {
                                                                              ;
                                                                        }

                                                                        var27 = var27.append("hwid=").append(HWID.getHWID()).append("&license=");
                                                                        if (((572696199 >> 3 >> 1 ^ 32251767) >>> 3 ^ 7980707) == 0) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append(var23).append("&type=Rage").toString();
                                                                        if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                                                                              ;
                                                                        }

                                                                        var5 = new URL(var24);
                                                                        if ((516 >> 4 << 1 ^ 1779327444) != 0) {
                                                                              ;
                                                                        }

                                                                        if (!"please go outside".equals("stop skidding")) {
                                                                              ;
                                                                        }

                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)((0 | 1126194653) >>> 1 << 1 ^ 164397543 ^ 1257036858));
                                                                        var6.setDoInput((boolean)((0 << 3 >>> 2 | 2138695113) ^ 2138695112));
                                                                        var29 = new DataOutputStream;
                                                                        if (((1786711921 ^ 413732630) >> 1 ^ 339939917 ^ -995270677) != 0) {
                                                                              ;
                                                                        }

                                                                        var29.<init>(var6.getOutputStream());
                                                                        var7 = var29;
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream;
                                                                        if ((44043336 >> 1 << 1 ^ -277268462) != 0) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var6.getInputStream());
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();

                                                                        for(var10 = var8.read(); var10 != (148446391 >>> 1 << 2 ^ 230102939 ^ -470101752); var10 = var34) {
                                                                              if (((268535792 | 9520173) ^ -1756406002) != 0) {
                                                                                    ;
                                                                              }

                                                                              var9.append((char)var10);
                                                                              var34 = var8.read();
                                                                              if ((438972321 >>> 4 & 6761129 & 1521232 ^ 798221195) != 0) {
                                                                                    ;
                                                                              }
                                                                        }

                                                                        var11 = var9.toString();
                                                                        if ((503103986 >>> 2 >>> 4 << 1 & 6373929 ^ 1133619010) != 0) {
                                                                              ;
                                                                        }

                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != ((3211974831L & 1543340760L) >>> 3 & 54321172L ^ 6299969335L)) {
                                                                              Thread.sleep(680712747364492868L >> 5 & 9635277519321580L ^ 1000544254367504095L);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var27 = new StringBuilder();
                                                                        if ((((575418634 | 308637633) ^ 469964555 | 127843081) ^ 805239753) == 0) {
                                                                              ;
                                                                        }

                                                                        var27 = var27.append("hwid=").append(HWID.getHWID()).append("&license=").append(var23);
                                                                        if (((456318328 | 89231013) >>> 2 >>> 4 << 4 ^ 131921904) == 0) {
                                                                              ;
                                                                        }

                                                                        var4 = var27.append("&type=Rage").toString();
                                                                        var5 = new URL(var24);
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if ((682075377 << 3 >>> 2 >> 1 ^ 70489869 ^ -1654552241) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        if ((1500704143 >> 2 >>> 2 & 37545403 & 627606 ^ 144) == 0) {
                                                                              ;
                                                                        }

                                                                        var32 = var4.length();
                                                                        if ((252195289 >> 4 >> 2 ^ 454041 ^ 3853662) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", String.valueOf(var32));
                                                                        if (((410220890 << 4 & 58099760) << 4 ^ 862454272) == 0) {
                                                                              ;
                                                                        }

                                                                        if (((437678803 | 34890114) << 3 >>> 3 << 2 >>> 2 ^ 1221563319) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)((0 & 869662709) << 2 & 251535709 ^ 1361984980 ^ 1361984981));
                                                                        var6.setDoInput((boolean)((0 & 1073315972 & 1531031536) >> 1 ^ 1));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        if (((1196173157 >> 2 | 178165519) >>> 1 ^ 1781999019) != 0) {
                                                                              ;
                                                                        }

                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream;
                                                                        if (((151343423 >>> 4 | 5946408) ^ 2632759 ^ -115832738) != 0) {
                                                                              ;
                                                                        }

                                                                        var35 = var6.getInputStream();
                                                                        if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var35);
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        if (((573713395 << 1 & 734780760) >>> 4 ^ 1534178096) != 0) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var8.read(); var10 != (1057076178 >> 2 << 4 << 4 ^ 1066601471); var10 = var8.read()) {
                                                                              var9.append((char)var10);
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != ((468745864L >> 31 | 8166356183501609649L) ^ 8166356189731388822L)) {
                                                                              var10000 = (805974485739994130L ^ 194551510821656435L) >>> 3 >> 20 ^ 999999918271091092L;
                                                                              if (!"shitted on you harder than archybot".equals("idiot")) {
                                                                                    ;
                                                                              }

                                                                              Thread.sleep(var10000);
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                        var38 = new URL(var24);
                                                                        if ((152066112 << 3 ^ 1793458985) != 0) {
                                                                              ;
                                                                        }

                                                                        var5 = var38;
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(((0 >> 1 | 547260621) >>> 3 | 62061482) ^ 129235898));
                                                                        if ((((1709576592 ^ 955544523 | 229819162) & 362667934) >>> 1 ^ 180904333) == 0) {
                                                                              ;
                                                                        }

                                                                        var6.setDoInput((boolean)((0 ^ 490671781 ^ 168579709) & 272243639 ^ 271718545));
                                                                        if ((789326637 << 4 << 3 ^ -964299626) != 0) {
                                                                              ;
                                                                        }

                                                                        var29 = new DataOutputStream;
                                                                        var10002 = var6.getOutputStream();
                                                                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("ape covered in human flesh")) {
                                                                              ;
                                                                        }

                                                                        var29.<init>(var10002);
                                                                        var7 = var29;
                                                                        var7.writeBytes(var4);
                                                                        if (!"stop skidding".equals("you're dogshit")) {
                                                                              ;
                                                                        }

                                                                        var7.close();
                                                                        if (((1084981627 >>> 1 | 40988084) & 231109443 ^ -1372576629) != 0) {
                                                                              ;
                                                                        }

                                                                        var25 = new DataInputStream;
                                                                        if (!"nefariousMoment".equals("please dont crack my plugin")) {
                                                                              ;
                                                                        }

                                                                        if (((18851204 ^ 6129678) >>> 1 ^ 10557125) == 0) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var6.getInputStream());
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        var34 = var8.read();
                                                                        if ((333154634 >>> 3 << 3 ^ 333154632) == 0) {
                                                                              ;
                                                                        }

                                                                        for(var10 = var34; var10 != (((556667425 | 266993903) & 473400038 | 176758474) ^ -246357743); var10 = var8.read()) {
                                                                              if (!"stop skidding".equals("nefariousMoment")) {
                                                                                    ;
                                                                              }

                                                                              var9.append((char)var10);
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if ((2136287398 << 2 >>> 4 >> 1 ^ 132818196) == 0) {
                                                                              ;
                                                                        }

                                                                        if (var12 != ((1300775810L ^ 592140634L) >>> 12 << 27 ^ 60889695643431L)) {
                                                                              if (((2010737523 >>> 3 | 178974726) << 1 ^ 502790876) == 0) {
                                                                                    ;
                                                                              }

                                                                              Thread.sleep(211788178992323762L >>> 14 & 1415793560751L ^ 1000001367414931316L);
                                                                        }

                                                                        if (((67715330 ^ 1573848) << 1 >> 4 ^ -896966533) != 0) {
                                                                              ;
                                                                        }

                                                                        var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                                                                              ;
                                                                        }

                                                                        var38 = new URL(var24);
                                                                        if (!"idiot".equals("you're dogshit")) {
                                                                              ;
                                                                        }

                                                                        var5 = var38;
                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        if (((1192516 >> 1 | 123014) ^ 1708277047) != 0) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestMethod("POST");
                                                                        if (!"stop skidding".equals("idiot")) {
                                                                              ;
                                                                        }

                                                                        String var36 = String.valueOf(var4.length());
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please take a shower")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("Content-length", var36);
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)(((0 ^ 2087129904) >>> 2 | 422326381) ^ 524154092));
                                                                        var6.setDoInput((boolean)((0 >> 2 & 959148976 | 1568226773) ^ 1568226772));
                                                                        var7 = new DataOutputStream(var6.getOutputStream());
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var8 = new DataInputStream(var6.getInputStream());
                                                                        if ((2006096231 >> 3 >> 1 & 82862546 ^ -633385285) != 0) {
                                                                              ;
                                                                        }

                                                                        if (((108528851 << 4 | 38042569 | 1475282237) ^ 2026762636) != 0) {
                                                                              ;
                                                                        }

                                                                        var9 = new StringBuilder();

                                                                        for(var10 = var8.read(); var10 != ((874410419 | 534296580 | 816396447) ^ -1073740224); var10 = var8.read()) {
                                                                              var31 = (char)var10;
                                                                              if (!"minecraft".equals("ape covered in human flesh")) {
                                                                                    ;
                                                                              }

                                                                              var9.append(var31);
                                                                        }

                                                                        var11 = var9.toString();
                                                                        var12 = Long.parseLong(var11);
                                                                        var8.close();
                                                                        if (var12 != (4948629838L << 5 >> 17 >> 20 ^ 6252521254L)) {
                                                                              Thread.sleep((115690419657019107L ^ 63626417446091230L | 88758004608342319L) >> 9 ^ 999811340096252928L);
                                                                        }

                                                                        var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                                                        Charset var33 = StandardCharsets.UTF_8;
                                                                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                                              ;
                                                                        }

                                                                        var23 = ModuleManager.readLicense(var26, var33);
                                                                        var24 = "https://verify.xbrowniecodez.xyz";
                                                                        var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                        var5 = new URL(var24);
                                                                        if (((1548824443 ^ 991080338 | 1572194513) ^ 37211119) != 0) {
                                                                              ;
                                                                        }

                                                                        var6 = (HttpsURLConnection)var5.openConnection();
                                                                        var6.setRequestMethod("POST");
                                                                        var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                        var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                        if (!"please go outside".equals("buy a domain and everything else you need at namecheap.com")) {
                                                                              ;
                                                                        }

                                                                        var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                        var6.setDoOutput((boolean)((0 & 728107055) >> 4 >>> 2 ^ 1));
                                                                        var6.setDoInput((boolean)(((0 | 1559669480) ^ 1277882935) >>> 3 ^ 35359514));
                                                                        var29 = new DataOutputStream;
                                                                        if (((696415279 >> 2 & 99553245) >>> 1 ^ 1422521851) != 0) {
                                                                              ;
                                                                        }

                                                                        var29.<init>(var6.getOutputStream());
                                                                        var7 = var29;
                                                                        var7.writeBytes(var4);
                                                                        var7.close();
                                                                        var25 = new DataInputStream;
                                                                        var35 = var6.getInputStream();
                                                                        if ((1835542066 << 2 << 3 << 1 ^ 1510575232) == 0) {
                                                                              ;
                                                                        }

                                                                        var25.<init>(var35);
                                                                        var8 = var25;
                                                                        var9 = new StringBuilder();
                                                                        var10 = var8.read();

                                                                        while(true) {
                                                                              if (!"minecraft".equals("ape covered in human flesh")) {
                                                                                    ;
                                                                              }

                                                                              if (var10 == (((786099951 | 751115078) >> 3 | 65316956) ^ 116636678 ^ -17581052)) {
                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    if ((804356736 >>> 3 >>> 1 ^ 50272296) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    if (var12 != ((5868395663L | 1317040791L) >> 11 ^ 6249913912L)) {
                                                                                          var10000 = (875245155859118939L & 390602864530928833L) >> 29 << 30 ^ 436707098982383355L ^ 265057420582027524L;
                                                                                          if ((452171543 >>> 1 >>> 3 ^ 911761323) != 0) {
                                                                                                ;
                                                                                          }

                                                                                          Thread.sleep(var10000);
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                                    if (!"nefariousMoment".equals("intentMoment")) {
                                                                                          ;
                                                                                    }

                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    if ((525312 >>> 1 ^ -671773777) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var27 = new StringBuilder;
                                                                                    if (((498319407 ^ 340574721 | 20207346) << 2 >> 1 ^ 335457788) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var27.<init>();
                                                                                    var27 = var27.append("hwid=").append(HWID.getHWID());
                                                                                    if (((1603912846 >>> 1 & 768746881) >> 4 >>> 1 ^ 23986432) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var4 = var27.append("&license=").append(var23).append("&type=Rage").toString();
                                                                                    var5 = new URL(var24);
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    var6.setRequestMethod("POST");
                                                                                    if ((346437951 << 3 ^ 230563322 ^ -1466969086) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var36 = String.valueOf(var4.length());
                                                                                    if (!"stop skidding".equals("please get a girlfriend and stop cracking plugins")) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestProperty("Content-length", var36);
                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    var6.setDoOutput((boolean)((0 >>> 1 & 1764427595) >>> 1 >>> 2 ^ 1));
                                                                                    if ((18432 >>> 1 ^ -1824755249) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setDoInput((boolean)(((0 ^ 800088927 ^ 463705585) >> 1 >>> 4 | 19445809) ^ 27836340));
                                                                                    var7 = new DataOutputStream(var6.getOutputStream());
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var25 = new DataInputStream;
                                                                                    if ((290649914 >>> 2 << 1 ^ 852765023) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var35 = var6.getInputStream();
                                                                                    if (!"please get a girlfriend and stop cracking plugins".equals("idiot")) {
                                                                                          ;
                                                                                    }

                                                                                    var25.<init>(var35);
                                                                                    var8 = var25;
                                                                                    var27 = new StringBuilder;
                                                                                    if (!"nefariousMoment".equals("you're dogshit")) {
                                                                                          ;
                                                                                    }

                                                                                    var27.<init>();
                                                                                    var9 = var27;

                                                                                    for(var10 = var8.read(); var10 != ((237065120 >> 4 | 4606735) ^ -15097664); var10 = var8.read()) {
                                                                                          if (((1318983825 >> 1 ^ 645134443 | 2366568) ^ 1391408292) != 0) {
                                                                                                ;
                                                                                          }

                                                                                          var9.append((char)var10);
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    if (((47846162 | 16800056) << 3 ^ -1179287852) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var8.close();
                                                                                    if (var12 != (2115495963L >>> 28 << 2 & 24L ^ 6252521279L)) {
                                                                                          Thread.sleep((921878705728646050L >> 11 ^ 144611786108201L | 269942195859004L) ^ 1000161201555652739L);
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    var27 = (new StringBuilder()).append("hwid=").append(HWID.getHWID());
                                                                                    if ((131072 & 8846 ^ 0) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var4 = var27.append("&license=").append(var23).append("&type=Rage").toString();
                                                                                    var38 = new URL;
                                                                                    if (((229712 ^ 197809) >> 2 ^ 502656280) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var38.<init>(var24);
                                                                                    var5 = var38;
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    var6.setRequestMethod("POST");
                                                                                    var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    var6.setDoOutput((boolean)((0 >>> 3 >> 4 | 1779007031) << 4 ^ -1600658575));
                                                                                    var6.setDoInput((boolean)(((0 | 2032562838) & 1307523128) >> 2 ^ 306809861));
                                                                                    if ((714678390 >> 1 >>> 3 << 3 >>> 2 ^ 34266806 ^ 1149434606) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var7 = new DataOutputStream(var6.getOutputStream());
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var8 = new DataInputStream(var6.getInputStream());
                                                                                    var9 = new StringBuilder();
                                                                                    if ((17507520 >> 1 >> 2 ^ 2188440) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var34 = var8.read();
                                                                                    if (((600275481 ^ 287047455) >> 3 ^ 32873567 ^ -558625820) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    for(var10 = var34; var10 != ((1582077864 << 4 >> 2 | 1782744126 | 207954856) >> 4 ^ 557060); var10 = var8.read()) {
                                                                                          var9.append((char)var10);
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    if (var12 != ((4960769063L ^ 1835235404L ^ 5524925931L) << 2 >>> 29 ^ 6355077719571415154L ^ 6355077714010974037L)) {
                                                                                          Thread.sleep(((84710936305118800L | 82109352479179836L) & 66519954785106463L) << 6 << 7 ^ -8673780205990412289L);
                                                                                    }

                                                                                    var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                                                                    var33 = StandardCharsets.UTF_8;
                                                                                    if (!"shitted on you harder than archybot".equals("intentMoment")) {
                                                                                          ;
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(var26, var33);
                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    if (((616044068 | 25939654 | 180987870 | 486044651) ^ 1073479679) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                                    var38 = new URL;
                                                                                    if ((((1822576905 ^ 296973969) & 1973746912) << 1 & 1685281123 ^ 1610631424) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var38.<init>(var24);
                                                                                    var5 = var38;
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    var6.setRequestMethod("POST");
                                                                                    var32 = var4.length();
                                                                                    if (((1460733377 >>> 1 | 165914770 | 52240426) ^ 67729897 ^ 804705043) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestProperty("Content-length", String.valueOf(var32));
                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    if ((((459177184 | 450389204) & 142523960 ^ 69473488) & 85674721 ^ -220487303) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    if ((((1492103277 ^ 1342751854) << 3 | 1053030676) ^ 2147219740) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var30 = (0 ^ 553164446) << 4 & 205794034 ^ 201337057;
                                                                                    if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setDoOutput((boolean)var30);
                                                                                    var6.setDoInput((boolean)((0 & 1994723687) >> 2 >>> 4 & 113225757 ^ 1));
                                                                                    var7 = new DataOutputStream(var6.getOutputStream());
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var25 = new DataInputStream;
                                                                                    if (((911790094 << 1 | 254785758) ^ 984758579 ^ 1817962799) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var25.<init>(var6.getInputStream());
                                                                                    var8 = var25;
                                                                                    if (((1083422864 ^ 376768951) >> 4 & 71496432 ^ 54332817 ^ -248570514) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var9 = new StringBuilder();

                                                                                    for(var10 = var8.read(); var10 != (548895879 << 3 >> 3 >> 2 ^ -3006242); var10 = var8.read()) {
                                                                                          var9.append((char)var10);
                                                                                          if ((((486851152 | 422406286) << 4 | 1584187374) ^ -1072990645) != 0) {
                                                                                                ;
                                                                                          }
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    if (var12 != ((1278926020L << 16 ^ 50942357143003L) >> 11 ^ 56797715608L)) {
                                                                                          Thread.sleep((366936356402067491L << 30 | 35993089996380864L) << 28 ^ -2226727514084737025L);
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    var27 = new StringBuilder;
                                                                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                                                                                          ;
                                                                                    }

                                                                                    var27.<init>();
                                                                                    var27 = var27.append("hwid=");
                                                                                    var10001 = HWID.getHWID();
                                                                                    if ((144311844 << 2 << 2 << 3 ^ 1780030744) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var27 = var27.append(var10001);
                                                                                    if (!"you probably spell youre as your".equals("intentMoment")) {
                                                                                          ;
                                                                                    }

                                                                                    var27 = var27.append("&license=");
                                                                                    if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                                                                                          ;
                                                                                    }

                                                                                    var27 = var27.append(var23);
                                                                                    if (((553434871 << 4 & 191355358) << 2 ^ -1363495456) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var4 = var27.append("&type=Rage").toString();
                                                                                    if (((1182600 | '\uef99') >> 1 ^ -1900165196) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var5 = new URL(var24);
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    if (((698982227 << 4 >> 3 | 1833192891) << 3 >> 2 ^ 1891084483) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestMethod("POST");
                                                                                    var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    var6.setDoOutput((boolean)(((0 | 1532246598) >>> 2 | 81616950) ^ 383610550));
                                                                                    var6.setDoInput((boolean)((0 >>> 1 & 1451956892) >> 4 ^ 1));
                                                                                    if ((2115232 ^ 995089 ^ 832760171) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var29 = new DataOutputStream;
                                                                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                                                                                          ;
                                                                                    }

                                                                                    var29.<init>(var6.getOutputStream());
                                                                                    var7 = var29;
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var8 = new DataInputStream(var6.getInputStream());
                                                                                    var9 = new StringBuilder();
                                                                                    if (!"idiot".equals("nefariousMoment")) {
                                                                                          ;
                                                                                    }

                                                                                    if (((((1336802754 | 1259254263) ^ 236850326) >>> 2 | 253609532) ^ 528350844) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    for(var10 = var8.read(); var10 != (697443248 << 2 >> 2 & 744919350 ^ -671220017); var10 = var8.read()) {
                                                                                          if ((((1724423369 ^ 151806794) >>> 1 | 677320820) ^ -211584463) != 0) {
                                                                                                ;
                                                                                          }

                                                                                          var9.append((char)var10);
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    if (var12 != ((3965803187L << 21 ^ 7317046065434183L) >> 8 >> 7 ^ 41093324021L)) {
                                                                                          var10000 = 259314920280956080L << 24 << 27 ^ -1702159776422297601L;
                                                                                          if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                                                                                                ;
                                                                                          }

                                                                                          Thread.sleep(var10000);
                                                                                          if (!"minecraft".equals("minecraft")) {
                                                                                                ;
                                                                                          }
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8);
                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    var4 = "hwid=" + HWID.getHWID() + "&license=" + var23 + "&type=Rage";
                                                                                    var38 = new URL;
                                                                                    if (((1718781455 ^ 1156561469) >> 3 ^ 72591238) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var38.<init>(var24);
                                                                                    var5 = var38;
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    var6.setRequestMethod("POST");
                                                                                    var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    var6.setDoOutput((boolean)(((0 & 122176357) >>> 3 >> 2 | 678005997) ^ 678005996));
                                                                                    var6.setDoInput((boolean)(0 << 2 >>> 1 << 1 >>> 3 ^ 1));
                                                                                    if ((231953131 >>> 1 >>> 4 & 1814587 ^ 692243) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var7 = new DataOutputStream(var6.getOutputStream());
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var8 = new DataInputStream(var6.getInputStream());
                                                                                    var9 = new StringBuilder();

                                                                                    for(var10 = var8.read(); var10 != ((1847793846 >>> 3 ^ 168206724 ^ 75017107 | 27402258) ^ -62548244); var10 = var8.read()) {
                                                                                          if (((629060567 ^ 213764854) >>> 2 & 8957705 ^ '踈') == 0) {
                                                                                                ;
                                                                                          }

                                                                                          var9.append((char)var10);
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    if (((185262532 ^ 54261174) >>> 4 ^ 8589639) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    long var41;
                                                                                    var34 = (var41 = var12 - ((1038932764L >>> 16 | 3542L) ^ 6252518105L)) == 0L ? 0 : (var41 < 0L ? -1 : 1);
                                                                                    if (!"intentMoment".equals("shitted on you harder than archybot")) {
                                                                                          ;
                                                                                    }

                                                                                    if (var34 != 0) {
                                                                                          Thread.sleep((542206023021388336L >>> 1 | 199889983553515854L) >> 19 ^ 1000000101407508992L);
                                                                                    }

                                                                                    var26 = ModuleManager.absoluteLicense().getAbsolutePath();
                                                                                    var33 = StandardCharsets.UTF_8;
                                                                                    if (((38355074 >> 3 | 3599258) >> 3 ^ 1047923) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var23 = ModuleManager.readLicense(var26, var33);
                                                                                    var24 = "https://verify.xbrowniecodez.xyz";
                                                                                    if (!"stop skidding".equals("stringer is a good obfuscator")) {
                                                                                          ;
                                                                                    }

                                                                                    var27 = new StringBuilder;
                                                                                    if (((1240884070 >> 2 | 9919748) ^ 318758365) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var27.<init>();
                                                                                    var27 = var27.append("hwid=").append(HWID.getHWID()).append("&license=");
                                                                                    if ((646058561 << 1 & 1277129074 ^ 216102528) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var4 = var27.append(var23).append("&type=Rage").toString();
                                                                                    var5 = new URL(var24);
                                                                                    var6 = (HttpsURLConnection)var5.openConnection();
                                                                                    var6.setRequestMethod("POST");
                                                                                    if ((1491289376 >> 2 >> 4 << 3 ^ -172910375) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestProperty("Content-length", String.valueOf(var4.length()));
                                                                                    if (((758148881 ^ 603283310 ^ 131131591 | 73679930) ^ 225828026) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    if (((2063943959 >> 3 | 13154747) ^ 266910139) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                                                    var6.setRequestProperty("User-Agent", "Mozilla/5.0");
                                                                                    var30 = (0 << 4 | 609724931) ^ 609724930;
                                                                                    if (((309815107 >> 2 & 75785655) >>> 1 ^ 898202096) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setDoOutput((boolean)var30);
                                                                                    if ((2067163676 ^ 1590255709 ^ 113176094 ^ 591520863) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var6.setDoInput((boolean)(((0 ^ 1426849156) & 1216200620) >> 4 ^ 19684778 ^ 86820467));
                                                                                    var29 = new DataOutputStream(var6.getOutputStream());
                                                                                    if ((((1311612596 | 40997174) & 533164305 ^ 42823200) >> 3 ^ 26808294) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var7 = var29;
                                                                                    var7.writeBytes(var4);
                                                                                    var7.close();
                                                                                    var25 = new DataInputStream;
                                                                                    var35 = var6.getInputStream();
                                                                                    if (((1653246148 >> 4 ^ 43193056 | 42791341) >> 3 ^ -1272982070) != 0) {
                                                                                          ;
                                                                                    }

                                                                                    var25.<init>(var35);
                                                                                    if ((705179969 << 1 >>> 3 ^ 176294992) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    var8 = var25;
                                                                                    var9 = new StringBuilder();

                                                                                    for(var10 = var8.read(); var10 != (853920990 >>> 2 << 4 ^ 879283343); var10 = var8.read()) {
                                                                                          var31 = (char)var10;
                                                                                          if ((1897013480 >>> 4 >> 1 ^ -227753404) != 0) {
                                                                                                ;
                                                                                          }

                                                                                          var9.append(var31);
                                                                                    }

                                                                                    var11 = var9.toString();
                                                                                    var12 = Long.parseLong(var11);
                                                                                    var8.close();
                                                                                    if (var12 != (((3565553845L & 1694480927L) >>> 20 | 360L) ^ 6252520015L)) {
                                                                                          Thread.sleep(208437020742959370L >> 14 << 10 ^ 995012902296826879L);
                                                                                    }

                                                                                    if (var12 == ((4590657943L ^ 3574409157L) >>> 18 ^ 6252500483L)) {
                                                                                          if (Instance != null) {
                                                                                                if (((63494965 | 34509373 | 63316006) ^ 33934386 ^ 149173092) != 0) {
                                                                                                      ;
                                                                                                }

                                                                                                MinecraftForge.EVENT_BUS.register(Instance);
                                                                                          }

                                                                                          MinecraftForge.EVENT_BUS.register(new ModuleManager());
                                                                                          MinecraftForge.EVENT_BUS.register(new Hud());
                                                                                          MinecraftForge.EVENT_BUS.register(new ClickGui());
                                                                                          MinecraftForge.EVENT_BUS.register(new SettingsManager());
                                                                                          MinecraftForge.EVENT_BUS.register(new RenderWorldLastHandler());
                                                                                          MinecraftForge.EVENT_BUS.register(new CommandManager());
                                                                                          settingsManager = new SettingsManager();
                                                                                          CommandManager var39 = new CommandManager();
                                                                                          if (!"ape covered in human flesh".equals("intentMoment")) {
                                                                                                ;
                                                                                          }

                                                                                          commandManager = var39;
                                                                                          if ((438407362 << 4 >>> 2 ^ 557109089 ^ 1925787090) != 0) {
                                                                                                ;
                                                                                          }

                                                                                          clickGui = new ClickGui();
                                                                                          if (ModuleManager.getModuleByName("Config").isEnabled()) {
                                                                                                saveLoad = new SaveLoad();
                                                                                          }
                                                                                    }

                                                                                    if ((((1747732483 ^ 80022760) << 1 ^ 352512996 | 1982897225) ^ 1238610873 ^ -1222423102) == 0) {
                                                                                          ;
                                                                                    }

                                                                                    return;
                                                                              }

                                                                              var9.append((char)var10);
                                                                              var10 = var8.read();
                                                                        }
                                                                  }

                                                                  var9.append((char)var10);
                                                                  var10 = var8.read();
                                                            }
                                                      }

                                                      var9.append((char)var10);
                                                      if ((1731314631 ^ 56180789 ^ 1467325725 ^ 1549186340) != 0) {
                                                            ;
                                                      }

                                                      var34 = var8.read();
                                                      if ((556249595 << 3 << 1 & 62794616 ^ 33014207 ^ -558837241) != 0) {
                                                            ;
                                                      }

                                                      var10 = var34;
                                                }
                                          }

                                          var9.append((char)var10);
                                          var10 = var8.read();
                                    }
                              }

                              var9.append((char)var10);
                              if ((139276 >> 2 << 3 ^ 278552) == 0) {
                                    ;
                              }

                              var34 = var8.read();
                              if (((2022181555 | 1642264460) >>> 3 >> 2 ^ -1728862217) != 0) {
                                    ;
                              }

                              var10 = var34;
                        }
                  }

                  if (!"ape covered in human flesh".equals("you're dogshit")) {
                        ;
                  }

                  var9.append((char)var10);
                  var10 = var8.read();
            }
      }
}
