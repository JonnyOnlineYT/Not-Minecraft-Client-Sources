package me.independed.inceptice.modules;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
import me.independed.inceptice.modules.misc.FastPlace;
import me.independed.inceptice.modules.misc.FastUse;
import me.independed.inceptice.modules.misc.MCF;
import me.independed.inceptice.modules.misc.SetBack;
import me.independed.inceptice.modules.misc.SmallShield;
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

public class ModuleManager {
      public static ArrayList modules;

      public static String licenseFile() {
            return "rage.yml";
      }

      public static int getResponse(String var0) {
            return -1;
      }

      public static File absoluteLicense() {
            return new File((new StringBuilder()).append(ModuleManager.licenseFolder()).append(ModuleManager.licenseFile()).toString());
      }

      public static String licenseFolder() {
            return "rage/";
      }

      public static String readLicense(String var0, Charset var1) throws IOException {
            if ((1577924010 << 1 << 3 ^ 1221791173) != 0) {
                  ;
            }

            byte[] var2 = (byte[])Files.readAllBytes(Paths.get(var0));
            return new String(var2, var1);
      }

      public static List getModulesByCategory(Module.Category var0) {
            ArrayList var10000 = new ArrayList;
            if (((605977532 | 514348245) >>> 2 >>> 2 ^ -635378381) != 0) {
                  ;
            }

            var10000.<init>();
            ArrayList var1 = var10000;
            Iterator var2 = ModuleManager.getAllSortedModules().iterator();

            while(var2.hasNext()) {
                  Module var3 = (Module)var2.next();
                  if (var3.category == var0) {
                        var1.add(var3);
                        if (!"please take a shower".equals("yo mama name maurice")) {
                              ;
                        }
                  }
            }

            return var1;
      }

      public ModuleManager() throws IOException {
            modules = new ArrayList();
            if ((811429757 >> 3 >> 1 & 3350014 ^ 32007 ^ 92657) == 0) {
                  ;
            }

            ArrayList var10000 = new ArrayList;
            if (((1692566299 ^ 1251223464) >>> 4 << 4 ^ 779527344) == 0) {
                  ;
            }

            var10000.<init>();
            modules = var10000;
            modules.add(new Config());
            modules.add(new Sprint());
            modules.add(new Fraerok2());
            modules.add(new WallHack());
            modules.add(new FullBright());
            modules.add(new TabGUI());
            if (((1725854655 << 2 << 1 | 615315311) << 1 ^ 1845493758) == 0) {
                  ;
            }

            modules.add(new Fraerok3());
            modules.add(new NoFall());
            if (((1658195129 | 1293559384 | 1252304231) >>> 2 << 3 ^ 1302326750 ^ 452550290) != 0) {
                  ;
            }

            modules.add(new Fraerok1());
            modules.add(new Fly());
            modules.add(new Criticals());
            if (((13631492 >>> 3 | 901941) ^ 2081589) == 0) {
                  ;
            }

            var10000 = modules;
            if (!"please take a shower".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000.add(new Esp());
            modules.add(new ClickGui());
            modules.add(new fsjahfk4());
            modules.add(new nameshower());
            var10000 = modules;
            Fraerok4 var10001 = new Fraerok4();
            if ((((725230458 >>> 2 ^ 66067960) << 3 | 1168782073) ^ 1308539897) == 0) {
                  ;
            }

            var10000.add(var10001);
            modules.add(new Tracers());
            if (((1049280 | 8155 | 204393) ^ 645422655) != 0) {
                  ;
            }

            var10000 = modules;
            if ((550312768 >>> 4 >> 3 << 4 ^ 564413982) != 0) {
                  ;
            }

            Trajectories var1 = new Trajectories;
            if (((1092987260 ^ 886934413) >>> 3 ^ 80748189 ^ 175053507) == 0) {
                  ;
            }

            var1.<init>();
            var10000.add(var1);
            modules.add(new BowSpam());
            var10000 = modules;
            Fraerok6 var2 = new Fraerok6;
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            var2.<init>();
            var10000.add(var2);
            if ((908431656 << 1 & 1701765668 ^ 575367687) != 0) {
                  ;
            }

            modules.add(new InventoryMove());
            modules.add(new fraerok8());
            modules.add(new LongJump());
            modules.add(new spidiki5());
            modules.add(new HighJump());
            modules.add(new AutoParkour());
            modules.add(new wTap());
            modules.add(new AutoTotem());
            var10000 = modules;
            Panic var3 = new Panic();
            if (((260454694 >>> 4 & 6603230 | 309104 | 4528615) ^ -2012070719) != 0) {
                  ;
            }

            var10000.add(var3);
            modules.add(new FakePlayer());
            var10000 = modules;
            if ((1737213607 << 4 << 3 & 1574532714 ^ 1170297344) == 0) {
                  ;
            }

            Derp var4 = new Derp;
            if ((416833295 >>> 2 >>> 2 ^ 26052080) == 0) {
                  ;
            }

            var4.<init>();
            var10000.add(var4);
            modules.add(new Jesus());
            if ((1769109134 >>> 4 >>> 1 << 1 ^ -471068849) != 0) {
                  ;
            }

            var10000 = modules;
            FakeCreative var5 = new FakeCreative();
            if ((1048832 >> 1 ^ 524416) == 0) {
                  ;
            }

            var10000.add(var5);
            var10000 = modules;
            ChestESP var6 = new ChestESP;
            if ((((710178381 ^ 249202662) >> 1 | 44052074) & 82459583 ^ 893765792) != 0) {
                  ;
            }

            var6.<init>();
            var10000.add(var6);
            modules.add(new AirWalk());
            if (((1822166668 ^ 463048944 | 694564338) >> 4 ^ -1534839880) != 0) {
                  ;
            }

            var10000 = modules;
            HUD var7 = new HUD;
            if ((4243460 >>> 4 >> 4 ^ -201099046) != 0) {
                  ;
            }

            var7.<init>();
            var10000.add(var7);
            if (!"please dont crack my plugin".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            modules.add(new Strafe());
            if (((4229154 ^ 3144941) >>> 1 ^ 3394336 ^ 972947103) != 0) {
                  ;
            }

            modules.add(new KeyStrokes());
            modules.add(new Teleport());
            modules.add(new InventoryView());
            modules.add(new ArmorView());
            modules.add(new FuckYou());
            modules.add(new Timer());
            var10000 = modules;
            AutoWalk var8 = new AutoWalk;
            if (!"shitted on you harder than archybot".equals("idiot")) {
                  ;
            }

            var8.<init>();
            var10000.add(var8);
            modules.add(new Fraerok5());
            if (!"intentMoment".equals("your mom your dad the one you never had")) {
                  ;
            }

            modules.add(new AutoGApple());
            modules.add(new PVPBot());
            modules.add(new NoPush());
            modules.add(new AutoPotion());
            modules.add(new FakeHacker());
            modules.add(new MCF());
            var10000 = modules;
            TargetHUD var9 = new TargetHUD;
            if ((43059201 ^ 43059201) == 0) {
                  ;
            }

            var9.<init>();
            var10000.add(var9);
            var10000 = modules;
            GuiParticles var10 = new GuiParticles;
            if (!"stringer is a good obfuscator".equals("idiot")) {
                  ;
            }

            var10.<init>();
            var10000.add(var10);
            if (!"please get a girlfriend and stop cracking plugins".equals("yo mama name maurice")) {
                  ;
            }

            var10000 = modules;
            ItemESP var11 = new ItemESP;
            if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var11.<init>();
            var10000.add(var11);
            modules.add(new Particles());
            if (!"shitted on you harder than archybot".equals("please dont crack my plugin")) {
                  ;
            }

            modules.add(new WaterSpeed());
            modules.add(new Nuker());
            modules.add(new NoHurtCam());
            var10000 = modules;
            AutoShift var12 = new AutoShift;
            if ((8555856 >> 2 ^ 514693319) != 0) {
                  ;
            }

            if ((((1054020011 ^ 148015761) & 625940305 & 247588898) >>> 1 >>> 2 ^ -10353440) != 0) {
                  ;
            }

            var12.<init>();
            var10000.add(var12);
            modules.add(new GodMode());
            modules.add(new Spammer());
            var10000 = modules;
            Spider var13 = new Spider;
            if ((((2034793781 | 299705960) ^ 1219087658) >> 4 ^ 51858213) == 0) {
                  ;
            }

            var13.<init>();
            var10000.add(var13);
            modules.add(new AirStack());
            modules.add(new FastUse());
            modules.add(new AntiBot());
            modules.add(new HitStrings());
            var10000 = modules;
            if ((((138048550 >> 4 | 2147571) ^ 10139748) << 4 ^ 320085914) != 0) {
                  ;
            }

            var10000.add(new AutoArmor());
            modules.add(new Glide());
            modules.add(new FastPlace());
            modules.add(new TestAura());
            modules.add(new ShieldAttack());
            modules.add(new AntiFire());
            modules.add(new CrystalAura());
            modules.add(new XRay());
            modules.add(new ElytraFly());
            if (!"idiot".equals("please go outside")) {
                  ;
            }

            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("ape covered in human flesh")) {
                  ;
            }

            modules.add(new ChestTracers());
            modules.add(new SafeWalk());
            var10000 = modules;
            SetBack var14 = new SetBack;
            if (!"i hope you catch fire ngl".equals("intentMoment")) {
                  ;
            }

            var14.<init>();
            var10000.add(var14);
            modules.add(new MCP());
            var10000 = modules;
            SmallShield var15 = new SmallShield;
            if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var15.<init>();
            var10000.add(var15);
      }

      public static List getAllSortedModules() {
            if ((322633786 << 4 >> 3 ^ 108396660) == 0) {
                  ;
            }

            ArrayList var10000 = new ArrayList;
            if (((1719669874 | 1472817268) >> 1 >> 1 ^ -1924028967) != 0) {
                  ;
            }

            var10000.<init>(ModuleManager.getModuleList());
            ArrayList var0 = var10000;
            if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                  ;
            }

            if ((((34175744 >>> 1 ^ 7169761) >> 3 | 1158468) ^ 796080191) != 0) {
                  ;
            }

            var0.sort(new Comparator() {
                  {
                        if (!"yo mama name maurice".qProtect<invokedynamic>("yo mama name maurice", "nefariousMoment")) {
                              ;
                        }

                  }

                  public int compare(Module var1, Module var2) {
                        if (!"i hope you catch fire ngl".qProtect<invokedynamic>("i hope you catch fire ngl", "idiot")) {
                              ;
                        }

                        String var3 = var1.qProtect<invokedynamic>(var1);
                        if (((244688758 << 4 << 3 | 924811909) & 1497591172 ^ 1497573764) == 0) {
                              ;
                        }

                        String var4 = var2.qProtect<invokedynamic>(var2);
                        int var5 = Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var4) - Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var3);
                        return var5 != 0 ? var5 : var4.qProtect<invokedynamic>(var4, var3);
                  }
            });
            return var0;
      }

      public static List getSortedModules() {
            ArrayList var0 = new ArrayList();
            Iterator var1 = ModuleManager.getModuleList().iterator();
            if (((891912206 << 1 >> 1 | 642942163) ^ 693664603 ^ 505675652) == 0) {
                  ;
            }

            while(var1.hasNext()) {
                  if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                        ;
                  }

                  Module var2 = (Module)var1.next();
                  if (var2.isToggled()) {
                        var0.add(var2);
                  }
            }

            var0.sort(new Comparator() {
                  public int compare(Module var1, Module var2) {
                        String var3 = "";
                        Iterator var4 = var1.settings.qProtect<invokedynamic>(var1.settings);

                        while(true) {
                              if ((1622850890 >> 2 & 268436046 ^ 1965563224) != 0) {
                                    ;
                              }

                              if (!var4.qProtect<invokedynamic>(var4)) {
                                    break;
                              }

                              Setting var5 = (Setting)var4.qProtect<invokedynamic>(var4);
                              if (var5 instanceof ModeSetting) {
                                    ModeSetting var6 = (ModeSetting)var5;
                                    var3 = var6.activeMode;
                                    if (((6310084 | 5607049) ^ -515758516) != 0) {
                                          ;
                                    }
                                    break;
                              }

                              if (!"ape covered in human flesh".qProtect<invokedynamic>("ape covered in human flesh", "please dont crack my plugin")) {
                                    ;
                              }
                        }

                        String var9;
                        StringBuilder var10000;
                        if (var3 != "") {
                              var10000 = (new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var1.qProtect<invokedynamic>(var1));
                              if (!"i hope you catch fire ngl".qProtect<invokedynamic>("i hope you catch fire ngl", "i hope you catch fire ngl")) {
                                    ;
                              }

                              var10000 = var10000.qProtect<invokedynamic>(var10000, " ").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, " "), var3);
                              var9 = var10000.qProtect<invokedynamic>(var10000, "").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, ""));
                        } else {
                              var9 = var1.qProtect<invokedynamic>(var1);
                        }

                        var3 = "";
                        if ((((1323859739 | 477647004) ^ 1186442885) >> 2 ^ 101971014) == 0) {
                              ;
                        }

                        Iterator var11 = var2.settings.qProtect<invokedynamic>(var2.settings);

                        while(true) {
                              boolean var13 = var11.qProtect<invokedynamic>(var11);
                              if ((785959334 >> 4 ^ 44087283 ^ 1172848 ^ 6086169) == 0) {
                                    ;
                              }

                              if (!var13) {
                                    break;
                              }

                              Setting var7 = (Setting)var11.qProtect<invokedynamic>(var11);
                              if (var7 instanceof ModeSetting) {
                                    ModeSetting var8 = (ModeSetting)var7;
                                    if (!"nefariousMoment".qProtect<invokedynamic>("nefariousMoment", "nefariousMoment")) {
                                          ;
                                    }

                                    var3 = var8.activeMode;
                                    if ((7043155 << 2 >>> 1 ^ 11378621) != 0) {
                                          ;
                                    }
                                    break;
                              }
                        }

                        if (((38258667 ^ 29794281) >> 1 ^ 108782339) != 0) {
                              ;
                        }

                        if (!"you're dogshit".qProtect<invokedynamic>("you're dogshit", "please take a shower")) {
                              ;
                        }

                        String var10;
                        if (var3 != "") {
                              var10000 = (new StringBuilder()).qProtect<invokedynamic>(new StringBuilder(), var2.qProtect<invokedynamic>(var2));
                              if (((100664400 ^ 60066969) & 58933103 ^ 18932769 ^ -378491710) != 0) {
                                    ;
                              }

                              var10000 = var10000.qProtect<invokedynamic>(var10000, " ").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, " "), var3);
                              var10 = var10000.qProtect<invokedynamic>(var10000, "").qProtect<invokedynamic>(var10000.qProtect<invokedynamic>(var10000, ""));
                        } else {
                              var10 = var2.qProtect<invokedynamic>(var2);
                        }

                        if (((69240390 | 6301876 | 59755285) ^ 1867078795) != 0) {
                              ;
                        }

                        int var12 = Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var10) - Hud.myRenderer.qProtect<invokedynamic>(Hud.myRenderer, var9);
                        return var12 != 0 ? var12 : var10.qProtect<invokedynamic>(var10, var9);
                  }

                  {
                        if (!"stringer is a good obfuscator".qProtect<invokedynamic>("stringer is a good obfuscator", "Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        if (((1590194692 >> 2 & 355731701) << 3 & 1661789037 ^ 1159899459) != 0) {
                              ;
                        }

                  }
            });
            return var0;
      }

      public static ArrayList getModuleList() {
            ArrayList var10000 = modules;
            if ((((178891533 | 124070012) >>> 2 ^ 54619259) & 6759087 & 2004215 ^ -1531697213) != 0) {
                  ;
            }

            return var10000;
      }

      public static Module getModuleByName(String var0) {
            Iterator var1 = modules.iterator();

            Module var2;
            do {
                  if (!var1.hasNext()) {
                        return null;
                  }

                  var2 = (Module)var1.next();
            } while(!var2.getName().equalsIgnoreCase(var0));

            return var2;
      }
}
