//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import me.independed.inceptice.modules.combat.AntiBot;
import me.independed.inceptice.modules.combat.AutoArmor;
import me.independed.inceptice.modules.combat.AutoPotion;
import me.independed.inceptice.modules.combat.AutoShift;
import me.independed.inceptice.modules.combat.AutoTotem;
import me.independed.inceptice.modules.combat.BowSpam;
import me.independed.inceptice.modules.combat.Criticals;
import me.independed.inceptice.modules.combat.CrystalAura;
import me.independed.inceptice.modules.combat.Fraerok1;
import me.independed.inceptice.modules.combat.Fraerok2;
import me.independed.inceptice.modules.combat.Fraerok3;
import me.independed.inceptice.modules.combat.Fraerok4;
import me.independed.inceptice.modules.combat.Fraerok5;
import me.independed.inceptice.modules.combat.Fraerok6;
import me.independed.inceptice.modules.combat.PVPBot;
import me.independed.inceptice.modules.combat.ShieldAttack;
import me.independed.inceptice.modules.combat.TestAura;
import me.independed.inceptice.modules.combat.wTap;
import me.independed.inceptice.modules.ghost.Config;
import me.independed.inceptice.modules.ghost.Panic;
import me.independed.inceptice.modules.hud.ArmorView;
import me.independed.inceptice.modules.hud.ClickGui;
import me.independed.inceptice.modules.hud.GuiParticles;
import me.independed.inceptice.modules.hud.HUD;
import me.independed.inceptice.modules.hud.InventoryView;
import me.independed.inceptice.modules.hud.KeyStrokes;
import me.independed.inceptice.modules.hud.TabGUI;
import me.independed.inceptice.modules.hud.TargetHUD;
import me.independed.inceptice.modules.misc.FakeCreative;
import me.independed.inceptice.modules.misc.FastUse;
import me.independed.inceptice.modules.misc.MCF;
import me.independed.inceptice.modules.misc.SetBack;
import me.independed.inceptice.modules.misc.Spammer;
import me.independed.inceptice.modules.movement.AirWalk;
import me.independed.inceptice.modules.movement.AutoParkour;
import me.independed.inceptice.modules.movement.AutoWalk;
import me.independed.inceptice.modules.movement.ElytraFly;
import me.independed.inceptice.modules.movement.Fly;
import me.independed.inceptice.modules.movement.Glide;
import me.independed.inceptice.modules.movement.HighJump;
import me.independed.inceptice.modules.movement.InventoryMove;
import me.independed.inceptice.modules.movement.Jesus;
import me.independed.inceptice.modules.movement.LongJump;
import me.independed.inceptice.modules.movement.NoWeb;
import me.independed.inceptice.modules.movement.SafeWalk;
import me.independed.inceptice.modules.movement.Spider;
import me.independed.inceptice.modules.movement.Sprint;
import me.independed.inceptice.modules.movement.Strafe;
import me.independed.inceptice.modules.movement.Timer;
import me.independed.inceptice.modules.movement.WaterSpeed;
import me.independed.inceptice.modules.movement.fraerok8;
import me.independed.inceptice.modules.movement.fsjahfk4;
import me.independed.inceptice.modules.movement.spidiki5;
import me.independed.inceptice.modules.player.AirStack;
import me.independed.inceptice.modules.player.AutoGApple;
import me.independed.inceptice.modules.player.Derp;
import me.independed.inceptice.modules.player.FakeHacker;
import me.independed.inceptice.modules.player.FakePlayer;
import me.independed.inceptice.modules.player.GodMode;
import me.independed.inceptice.modules.player.MCP;
import me.independed.inceptice.modules.player.NoFall;
import me.independed.inceptice.modules.player.NoPush;
import me.independed.inceptice.modules.player.Nuker;
import me.independed.inceptice.modules.player.Teleport;
import me.independed.inceptice.modules.visual.AntiFire;
import me.independed.inceptice.modules.visual.ChestESP;
import me.independed.inceptice.modules.visual.ChestTracers;
import me.independed.inceptice.modules.visual.Esp;
import me.independed.inceptice.modules.visual.FuckYou;
import me.independed.inceptice.modules.visual.FullBright;
import me.independed.inceptice.modules.visual.HitStrings;
import me.independed.inceptice.modules.visual.ItemESP;
import me.independed.inceptice.modules.visual.NoHurtCam;
import me.independed.inceptice.modules.visual.Particles;
import me.independed.inceptice.modules.visual.Tracers;
import me.independed.inceptice.modules.visual.Trajectories;
import me.independed.inceptice.modules.visual.WallHack;
import me.independed.inceptice.modules.visual.XRay;
import me.independed.inceptice.modules.visual.nameshower;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import me.independed.inceptice.util.HWID;
import me.zero.alpine.bus.type.Cancellable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class ModuleManager {
      public static ArrayList modules;

      public static Module getModuleByName(String var0) {
            Iterator var10000 = modules.iterator();
            if (((126317918 | 4913037) << 4 >> 4 >>> 4 ^ 8191869) == 0) {
                  ;
            }

            Iterator var1 = var10000;

            Module var2;
            do {
                  if (!var1.hasNext()) {
                        return null;
                  }

                  var2 = (Module)var1.next();
            } while(!var2.getName().equalsIgnoreCase(var0));

            return var2;
      }

      @SubscribeEvent
      public void key(KeyInputEvent var1) {
            Minecraft var10000 = Minecraft.getMinecraft();
            if (((1457638479 << 1 >>> 1 ^ 635969597) << 1 ^ -434906908) == 0) {
                  ;
            }

            if (var10000.world != null) {
                  if (Minecraft.getMinecraft().player != null) {
                        try {
                              if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                                    int var6 = Keyboard.getEventKey();
                                    if ((1418284909 >> 3 << 3 ^ 337450275 ^ -851149984) != 0) {
                                          ;
                                    }

                                    int var2 = var6;
                                    if (var2 <= 0) {
                                          return;
                                    }

                                    Iterator var3 = modules.iterator();

                                    while(var3.hasNext()) {
                                          Module var4 = (Module)var3.next();
                                          if (var4.getKey() == var2 && var2 > 0) {
                                                if ((96473088 >>> 3 ^ 524894752) != 0) {
                                                      ;
                                                }

                                                var4.toggle();
                                          }
                                    }
                              }
                        } catch (Exception var5) {
                              var5.printStackTrace();
                        }

                        if (((261812016 << 4 >>> 4 | 201158580) ^ 268431284) == 0) {
                              ;
                        }

                        return;
                  }

                  if (((615566414 | 541013067 | 66172226 | 268089238) ^ -637955979) != 0) {
                        ;
                  }
            }

      }

      public static long getResponse(String var0) throws IOException, NoSuchAlgorithmException {
            String var1 = "https://verify.xbrowniecodez.xyz";
            StringBuilder var10000 = (new StringBuilder()).append("hwid=").append(HWID.getHWID());
            if (((276666065 >>> 1 | 81376993) ^ 218103785) == 0) {
                  ;
            }

            var10000 = var10000.append("&license=").append(var0);
            if (((518674769 >> 4 | 6198810) ^ 894692498) != 0) {
                  ;
            }

            var10000 = var10000.append("&type=Rage");
            if ((1583468089 >>> 2 >>> 1 & 83188892 ^ -1030166519) != 0) {
                  ;
            }

            String var2 = var10000.toString();
            URL var3 = new URL(var1);
            HttpsURLConnection var12 = (HttpsURLConnection)var3.openConnection();
            if ((824841947 >>> 2 << 4 & 630497433 ^ 75497472) == 0) {
                  ;
            }

            HttpsURLConnection var4 = var12;
            var4.setRequestMethod("POST");
            var4.setRequestProperty("Content-length", String.valueOf(var2.length()));
            var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var4.setRequestProperty("User-Agent", "Mozilla/5.0");
            var4.setDoOutput((boolean)(0 >> 1 << 1 ^ 1));
            var4.setDoInput((boolean)((0 >> 2 | 607834794) ^ 607834795));
            if ((1212463121 ^ 301744005 ^ 1505265556) == 0) {
                  ;
            }

            DataOutputStream var5 = new DataOutputStream(var4.getOutputStream());
            if (!"nefariousMoment".equals("please dont crack my plugin")) {
                  ;
            }

            var5.writeBytes(var2);
            var5.close();
            DataInputStream var6 = new DataInputStream(var4.getInputStream());
            StringBuilder var7 = new StringBuilder();

            for(int var8 = var6.read(); var8 != (1941664179 >> 1 >> 4 ^ 15499866 ^ -57760216); var8 = var6.read()) {
                  if (!"your mom your dad the one you never had".equals("please take a shower")) {
                        ;
                  }

                  var7.append((char)var8);
            }

            if ((((1078215256 | 24365185) >> 4 | 58609890 | 118517822) ^ 125795583) == 0) {
                  ;
            }

            String var9 = var7.toString();
            long var10 = Long.parseLong(var9);
            var6.close();
            if (((580042919 >> 4 & 273077) << 1 ^ 20480) == 0) {
                  ;
            }

            return var10;
      }

      public static File absoluteLicense() {
            File var10000 = new File;
            if (!"stop skidding".equals("please take a shower")) {
                  ;
            }

            var10000.<init>((new StringBuilder()).append(ModuleManager.licenseFolder()).append(ModuleManager.licenseFile()).toString());
            return var10000;
      }

      public static String licenseFolder() {
            return "rage/";
      }

      public static String readLicense(String var0, Charset var1) throws IOException {
            byte[] var2 = (byte[])Files.readAllBytes(Paths.get(var0));
            return new String(var2, var1);
      }

      public static List getModulesByCategory(Module.Category var0) {
            ArrayList var10000 = new ArrayList;
            if (((2026353072 >> 3 & 93303375) >> 3 >>> 4 ^ 659812) == 0) {
                  ;
            }

            var10000.<init>();
            ArrayList var1 = var10000;
            List var4 = ModuleManager.getAllSortedModules();
            if ((806033533 >> 2 >> 2 & 21186432 ^ 1090352008) != 0) {
                  ;
            }

            Iterator var2 = var4.iterator();

            while(true) {
                  if (((175178857 | 89139013) >> 4 ^ -816188292) == 0) {
                  }

                  if (!var2.hasNext()) {
                        if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                              ;
                        }

                        return var1;
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  Module var3 = (Module)var2.next();
                  if (var3.category == var0) {
                        if (((2071669016 << 3 & 1185419625 ^ 545719866) << 3 ^ -1373600202) != 0) {
                              ;
                        }

                        var1.add(var3);
                  }

                  if (((233821468 << 2 >> 1 & 49456595 | 7066148) ^ 50065972) == 0) {
                        ;
                  }
            }
      }

      public static List getSortedModules() {
            if (((1779888697 << 1 ^ 1192776913) & 1110224883 & 13972007 ^ 270371) == 0) {
                  ;
            }

            ArrayList var0 = new ArrayList();
            ArrayList var10000 = ModuleManager.getModuleList();
            if (!"please take a shower".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            Iterator var1 = var10000.iterator();

            while(var1.hasNext()) {
                  Module var2 = (Module)var1.next();
                  if (var2.isToggled()) {
                        var0.add(var2);
                  }
            }

            Comparator var10001 = new Comparator() {
                  public int compare(Module var1, Module var2) {
                        String var3 = "";
                        Iterator var4 = var1.settings.qProtect<invokedynamic>(var1.settings);

                        while(var4.qProtect<invokedynamic>(var4)) {
                              Setting var5 = (Setting)var4.qProtect<invokedynamic>(var4);
                              if (var5 instanceof ModeSetting) {
                                    ModeSetting var6 = (ModeSetting)var5;
                                    var3 = var6.activeMode;
                                    break;
                              }
                        }

                        String var9;
                        StringBuilder var10000;
                        if (var3 != "") {
                              var10000 = new StringBuilder;
                              if ((((885826884 | 121966511) & 724492356) << 1 ^ 989592189) != 0) {
                                    ;
                              }

                              var10000.<init>();
                              var10000 = var10000.qProtect<invokedynamic>(var10000, var1.qProtect<invokedynamic>(var1)).qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, var1.qProtect<invokedynamic>(var1)), " ").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, var1.qProtect<invokedynamic>(var1)).qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, var1.qProtect<invokedynamic>(var1)), " "), var3);
                              if (((1003279904 ^ 362210991) << 2 >>> 2 ^ -1673965032) != 0) {
                                    ;
                              }

                              var10000 = var10000.qProtect<invokedynamic>(var10000, "");
                              if ((((549418795 | 441367737 | 424396454) & 56837282) >>> 4 ^ 3552330) == 0) {
                                    ;
                              }

                              var9 = var10000.qProtect<invokedynamic>(var10000);
                              if ((56249245 >>> 3 >> 2 >> 2 >>> 4 >>> 1 ^ 64300709) != 0) {
                                    ;
                              }
                        } else {
                              var9 = var1.qProtect<invokedynamic>(var1);
                        }

                        if (((1606636 ^ 665705) & 913849 ^ -1860170947) != 0) {
                              ;
                        }

                        var3 = "";
                        Iterator var11 = var2.settings.qProtect<invokedynamic>(var2.settings);

                        while(var11.qProtect<invokedynamic>(var11)) {
                              Setting var7 = (Setting)var11.qProtect<invokedynamic>(var11);
                              if (var7 instanceof ModeSetting) {
                                    ModeSetting var8 = (ModeSetting)var7;
                                    if ((((6303874 ^ 4153242) >> 4 | 18489) ^ -1028217035) != 0) {
                                          ;
                                    }

                                    String var13 = var8.activeMode;
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".qProtect<invokedynamic>("You're so fat whenever you go to the beach the tide comes in.", "buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var3 = var13;
                                    if (((1078257041 >>> 2 & 182094267 | 636367 | 823056) ^ 1949695) == 0) {
                                          ;
                                    }
                                    break;
                              }

                              if (!"stop skidding".qProtect<invokedynamic>("stop skidding", "shitted on you harder than archybot")) {
                                    ;
                              }

                              if (((1837048305 ^ 173236080) >> 1 & 272800913 ^ -568194124) != 0) {
                                    ;
                              }
                        }

                        String var10;
                        if (var3 != "") {
                              var10000 = (new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var2.qProtect<invokedynamic>(var2)).qProtect<invokedynamic>((new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var2.qProtect<invokedynamic>(var2)), " ").qProtect<invokedynamic>((new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var2.qProtect<invokedynamic>(var2)).qProtect<invokedynamic>((new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var2.qProtect<invokedynamic>(var2)), " "), var3);
                              var10 = var10000.qProtect<invokedynamic>(var10000, "").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, ""));
                        } else {
                              var10 = var2.qProtect<invokedynamic>(var2);
                        }

                        int var12 = Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var10) - Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var9);
                        return var12 != 0 ? var12 : var10.qProtect<invokedynamic>(var10, var9);
                  }
            };
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            var0.sort(var10001);
            return var0;
      }

      public static List getAllSortedModules() {
            ArrayList var10000 = new ArrayList;
            ArrayList var10002 = ModuleManager.getModuleList();
            if (((1696533235 << 3 ^ 113427620 | 392571053) >> 4 ^ 2135710363) != 0) {
                  ;
            }

            var10000.<init>(var10002);
            if (!"please take a shower".equals("stop skidding")) {
                  ;
            }

            ArrayList var0 = var10000;
            var0.sort(new Comparator() {
                  public int compare(Module var1, Module var2) {
                        String var10000 = var1.qProtect<invokedynamic>(var1);
                        if ((((740191530 ^ 325871632) >>> 4 & 54622451) >>> 4 ^ 3412485) == 0) {
                              ;
                        }

                        String var3 = var10000;
                        String var4 = var2.qProtect<invokedynamic>(var2);
                        int var5 = Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var4) - Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var3);
                        return var5 != 0 ? var5 : var4.qProtect<invokedynamic>(var4, var3);
                  }

                  {
                        if (!"i hope you catch fire ngl".qProtect<invokedynamic>("i hope you catch fire ngl", "buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                  }
            });
            return var0;
      }

      public ModuleManager() throws InterruptedException, IOException, NoSuchAlgorithmException {
            if ((536879110 >> 2 >> 3 ^ 1417628219) != 0) {
                  ;
            }

            modules = new ArrayList();
            if (Cancellable.ok) {
                  if (ModuleManager.getResponse(ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8)) == (5154642537L >> 1 >> 13 ^ 310523L ^ 6252500778L)) {
                        modules = new ArrayList();
                        modules.add(new Config());
                        ArrayList var10000 = modules;
                        Sprint var10001 = new Sprint;
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("intentMoment")) {
                              ;
                        }

                        var10001.<init>();
                        var10000.add(var10001);
                        modules.add(new Fraerok2());
                        modules.add(new WallHack());
                        var10000 = modules;
                        if ((135450400 >> 2 ^ -1063395305) != 0) {
                              ;
                        }

                        var10000.add(new FullBright());
                        if ((151130208 >> 1 & 37616781 ^ 67584) == 0) {
                              ;
                        }

                        modules.add(new TabGUI());
                        modules.add(new Fraerok3());
                        modules.add(new NoFall());
                        modules.add(new Fraerok1());
                        modules.add(new Fly());
                        var10000 = modules;
                        Criticals var1 = new Criticals();
                        if ((1081344 & 30078 ^ 423233582) != 0) {
                              ;
                        }

                        var10000.add(var1);
                        modules.add(new Esp());
                        if (((1971015534 >> 1 | 233780853 | 690949748) ^ -229154445) != 0) {
                              ;
                        }

                        modules.add(new ClickGui());
                        modules.add(new fsjahfk4());
                        modules.add(new nameshower());
                        var10000 = modules;
                        Fraerok4 var2 = new Fraerok4;
                        if (!"please take a shower".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var2.<init>();
                        var10000.add(var2);
                        var10000 = modules;
                        if ((1370375250 >>> 4 >> 1 ^ 42824226) == 0) {
                              ;
                        }

                        Tracers var3 = new Tracers;
                        if (!"minecraft".equals("stop skidding")) {
                              ;
                        }

                        var3.<init>();
                        var10000.add(var3);
                        modules.add(new Trajectories());
                        modules.add(new BowSpam());
                        modules.add(new Fraerok6());
                        var10000 = modules;
                        InventoryMove var4 = new InventoryMove();
                        if (((388239912 << 3 ^ 382506046 | 1380109825 | 655338343) ^ -1052801) == 0) {
                              ;
                        }

                        var10000.add(var4);
                        var10000 = modules;
                        fraerok8 var5 = new fraerok8;
                        if ((213241261 >> 2 >>> 2 >>> 4 >>> 3 ^ 104121) == 0) {
                              ;
                        }

                        var5.<init>();
                        var10000.add(var5);
                        modules.add(new LongJump());
                        modules.add(new spidiki5());
                        modules.add(new HighJump());
                        modules.add(new AutoParkour());
                        modules.add(new wTap());
                        modules.add(new AutoTotem());
                        if ((((540239546 | 369925179) << 4 | 1268403670) ^ 880474516) != 0) {
                              ;
                        }

                        modules.add(new Panic());
                        modules.add(new FakePlayer());
                        modules.add(new Derp());
                        modules.add(new Jesus());
                        modules.add(new FakeCreative());
                        if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                              ;
                        }

                        modules.add(new ChestESP());
                        modules.add(new AirWalk());
                        var10000 = modules;
                        HUD var6 = new HUD();
                        if ((((1878359906 ^ 85977070) & 525935008 ^ 35061325) >>> 3 ^ 17555417) == 0) {
                              ;
                        }

                        var10000.add(var6);
                        var10000 = modules;
                        Strafe var7 = new Strafe;
                        if (((585610032 >> 1 ^ 257219206) << 2 ^ 1040211599) != 0) {
                              ;
                        }

                        var7.<init>();
                        var10000.add(var7);
                        modules.add(new KeyStrokes());
                        var10000 = modules;
                        if (((270541444 ^ 139486149) >>> 1 << 2 ^ 820019840) == 0) {
                              ;
                        }

                        Teleport var8 = new Teleport();
                        if (((1207994368 | 73498697) ^ 1281460297) == 0) {
                              ;
                        }

                        var10000.add(var8);
                        if (((9318401 | 4491459) ^ 1585498496) != 0) {
                              ;
                        }

                        var10000 = modules;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                              ;
                        }

                        InventoryView var9 = new InventoryView();
                        if (((457459766 >> 1 & 175484381 & 79457225) << 3 ^ 16842824) == 0) {
                              ;
                        }

                        var10000.add(var9);
                        if (((1541045343 | 757344032) >> 1 ^ 1073692607) == 0) {
                              ;
                        }

                        modules.add(new ArmorView());
                        if (((1569815188 << 1 & 362958601 | 21123001) & 88044443 ^ 19022745) == 0) {
                              ;
                        }

                        modules.add(new FuckYou());
                        var10000 = modules;
                        if (!"nefariousMoment".equals("nefariousMoment")) {
                              ;
                        }

                        var10000.add(new Timer());
                        modules.add(new AutoWalk());
                        if (!"buy a domain and everything else you need at namecheap.com".equals("please go outside")) {
                              ;
                        }

                        var10000 = modules;
                        if ((1022842481 >> 1 >>> 3 >>> 1 ^ 31963827) == 0) {
                              ;
                        }

                        var10000.add(new Fraerok5());
                        modules.add(new AutoGApple());
                        modules.add(new PVPBot());
                        modules.add(new NoPush());
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                              ;
                        }

                        if (!"please dont crack my plugin".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        modules.add(new AutoPotion());
                        modules.add(new FakeHacker());
                        modules.add(new MCF());
                        var10000 = modules;
                        TargetHUD var10 = new TargetHUD;
                        if (((383006288 >>> 1 | 141643491) >>> 4 ^ 12039678) == 0) {
                              ;
                        }

                        if (((791814 | 536408 | 187573) ^ 1488547107) != 0) {
                              ;
                        }

                        var10.<init>();
                        var10000.add(var10);
                        var10000 = modules;
                        GuiParticles var11 = new GuiParticles;
                        if (((993902984 | 955643071 | 721715270) & 184424782 ^ -1637691973) != 0) {
                              ;
                        }

                        var11.<init>();
                        if (((1036993833 | 800376012) & 110969057 ^ 37662494 ^ -1591454979) != 0) {
                              ;
                        }

                        var10000.add(var11);
                        if ((((934357321 ^ 698193653) & 329118570) >> 4 >> 4 ^ -136298075) != 0) {
                              ;
                        }

                        modules.add(new ItemESP());
                        modules.add(new Particles());
                        var10000 = modules;
                        WaterSpeed var12 = new WaterSpeed;
                        if (((27428636 << 1 & 39110288) >>> 1 ^ -1701530947) != 0) {
                              ;
                        }

                        var12.<init>();
                        var10000.add(var12);
                        if (!"minecraft".equals("yo mama name maurice")) {
                              ;
                        }

                        var10000 = modules;
                        Nuker var13 = new Nuker;
                        if ((176193716 << 1 ^ -1955550930) != 0) {
                              ;
                        }

                        if (((741956159 << 1 & 519556454) >>> 4 ^ 25634886) == 0) {
                              ;
                        }

                        var13.<init>();
                        if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                              ;
                        }

                        var10000.add(var13);
                        modules.add(new NoHurtCam());
                        if (!"shitted on you harder than archybot".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        modules.add(new AutoShift());
                        modules.add(new GodMode());
                        modules.add(new Spammer());
                        modules.add(new Spider());
                        modules.add(new NoWeb());
                        modules.add(new AirStack());
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                              ;
                        }

                        modules.add(new FastUse());
                        modules.add(new AntiBot());
                        var10000 = modules;
                        HitStrings var14 = new HitStrings();
                        if ((((1817604080 ^ 1306003169) & 226762673) << 1 >>> 2 ^ 2056907564) != 0) {
                              ;
                        }

                        var10000.add(var14);
                        modules.add(new AutoArmor());
                        var10000 = modules;
                        if (!"i hope you catch fire ngl".equals("minecraft")) {
                              ;
                        }

                        Glide var15 = new Glide;
                        if (!"please go outside".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var15.<init>();
                        var10000.add(var15);
                        if ((164122212 << 4 >>> 2 ^ 113076083 ^ -279266970) != 0) {
                              ;
                        }

                        var10000 = modules;
                        TestAura var16 = new TestAura();
                        if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var10000.add(var16);
                        modules.add(new ShieldAttack());
                        modules.add(new AntiFire());
                        modules.add(new CrystalAura());
                        var10000 = modules;
                        XRay var17 = new XRay;
                        if ((665139506 << 1 >>> 3 >> 2 >>> 4 ^ 1096027471) != 0) {
                              ;
                        }

                        var17.<init>();
                        var10000.add(var17);
                        if (((4473861 | 2153338) & 417912 ^ -1695164352) != 0) {
                              ;
                        }

                        var10000 = modules;
                        ElytraFly var18 = new ElytraFly;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var18.<init>();
                        if (((641935872 << 2 & 1554362667 & 82910901 | 519834035) ^ 519834035) == 0) {
                              ;
                        }

                        var10000.add(var18);
                        modules.add(new ChestTracers());
                        modules.add(new SafeWalk());
                        if (((225575963 >> 2 ^ 25968235 | 35036456) << 4 >>> 2 ^ 805594253) != 0) {
                              ;
                        }

                        modules.add(new SetBack());
                        var10000 = modules;
                        if (!"minecraft".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var10000.add(new MCP());
                        if (!"intentMoment".equals("shitted on you harder than archybot")) {
                              ;
                        }
                  } else {
                        Thread.sleep((8332390987527168L | 8197214279573929L) >> 4 << 3 >>> 4 ^ 100109995804604498L);
                  }
            }

            if (!"minecraft".equals("stringer is a good obfuscator")) {
                  ;
            }

      }

      public static String licenseFile() {
            return "rage.yml";
      }

      public static ArrayList getModuleList() {
            ArrayList var10000 = modules;
            if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            return var10000;
      }
}
