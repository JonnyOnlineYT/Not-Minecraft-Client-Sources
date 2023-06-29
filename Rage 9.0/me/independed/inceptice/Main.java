//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
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
import net.minecraftforge.fml.common.eventhandler.EventBus;
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
      public static ClickGui clickGui;
      public static CommandManager commandManager;
      public static RenderWorldLastHandler renderWorldLastHandler;
      public static SettingsManager settingsManager;
      public static SaveLoad saveLoad;
      public static Hud hud;
      @SidedProxy(
            clientSide = "me.independed.inceptice.proxy.ClientProxy",
            serverSide = "me.independed.inceptice.proxy.CommonProxy"
      )
      public static CommonProxy proxy;
      @Instance
      public static Main Instance;
      public static ModuleManager moduleManager;
      private boolean checked = ((542119726 ^ 294260909) >>> 2 ^ 66925941) >>> 3 ^ 32601746;

      public Main() {
            if ((1045919667 << 3 >>> 3 ^ 509048755) == 0) {
                  ;
            }

      }

      @SubscribeEvent
      public void key(KeyInputEvent var1) {
            if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
                  try {
                        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                              int var2 = Keyboard.getEventKey();
                              if (var2 <= 0) {
                                    return;
                              }

                              Iterator var3 = ModuleManager.modules.iterator();

                              while(var3.hasNext()) {
                                    Module var4 = (Module)var3.next();
                                    if (((1705965608 | 1354581686 | 193370619) >>> 1 ^ 1071644671) == 0) {
                                          ;
                                    }

                                    if (var4.getKey() == var2 && var2 > 0) {
                                          if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                                                ;
                                          }

                                          if (((2074642333 << 4 ^ 1149123622) >> 4 >>> 1 ^ -112333350) != 0) {
                                                ;
                                          }

                                          var4.toggle();
                                    }
                              }
                        }
                  } catch (Exception var5) {
                        if (((757355007 ^ 259951029) >>> 1 ^ 288189477) != 0) {
                        }

                        var5.printStackTrace();
                  }

            } else {
                  if (!"you're dogshit".equals("intentMoment")) {
                        ;
                  }

            }
      }

      @EventHandler
      public void preInit(FMLPreInitializationEvent var1) {
            if ((((287208706 | 161290587) & 128025796) << 3 >> 1 >> 1 ^ 50464896) == 0) {
                  ;
            }

      }

      @EventHandler
      public void init(FMLInitializationEvent var1) throws IOException, HeadlessException, NoSuchAlgorithmException {
            if (!ModuleManager.absoluteLicense().exists()) {
                  (new File(ModuleManager.licenseFolder())).mkdirs();
                  PrintStream var2 = new PrintStream(new FileOutputStream(ModuleManager.absoluteLicense()));
                  Throwable var3 = null;
                  boolean var11 = false;

                  try {
                        var11 = true;
                        var2.print("YOUR-LICENSE-KEY");
                        var11 = false;
                  } catch (Throwable var13) {
                        if ("please take a shower".equals("please go outside")) {
                        }

                        var3 = var13;
                        throw var13;
                  } finally {
                        if (var11) {
                              if ("idiot".equals("yo mama name maurice")) {
                              }

                              if (var2 != null) {
                                    if (!"please take a shower".equals("idiot")) {
                                          ;
                                    }

                                    if (var3 != null) {
                                          try {
                                                var2.close();
                                          } catch (Throwable var12) {
                                                var3.addSuppressed(var12);
                                          }
                                    } else {
                                          var2.close();
                                    }
                              }

                        }
                  }

                  if (var2 != null) {
                        if (var3 != null) {
                              try {
                                    var2.close();
                              } catch (Throwable var15) {
                                    if ((1249220192 << 3 >>> 1 ^ 701913472) != 0) {
                                    }

                                    if ((803826513 >>> 4 >>> 3 ^ 1591438273) != 0) {
                                          ;
                                    }

                                    var3.addSuppressed(var15);
                              }
                        } else {
                              var2.close();
                              if (((703088585 >>> 3 | 1189657) >>> 4 & 442347 ^ -969974019) != 0) {
                                    ;
                              }
                        }
                  }

                  JOptionPane.showMessageDialog((Component)null, (new StringBuilder()).append("Please put your key into ").append(ModuleManager.absoluteLicense().getAbsolutePath()).toString(), "RageClient Error", (5124 >>> 1 | 1080) ^ 3642);
            } else {
                  String var10000 = ModuleManager.absoluteLicense().getAbsolutePath();
                  if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (ModuleManager.readLicense(var10000, StandardCharsets.UTF_8) == "YOUR-LICENSE-KEY") {
                        JOptionPane.showMessageDialog((Component)null, (new StringBuilder()).append("Please put your key into ").append(ModuleManager.absoluteLicense().getAbsolutePath()).toString(), "RageClient Error", (1739759116 >> 4 | 104241966) << 2 ^ 436137912);
                  } else if (ModuleManager.getResponse(ModuleManager.readLicense(ModuleManager.absoluteLicense().getAbsolutePath(), StandardCharsets.UTF_8)) == ((75544720 >>> 3 ^ 875539) & 1497418 ^ 115507886)) {
                        Cancellable.ok = (boolean)(0 << 4 << 4 ^ 35535544 ^ 35535545);
                  }
            }

            if (Instance != null) {
                  MinecraftForge.EVENT_BUS.register(Instance);
            }

            MinecraftForge.EVENT_BUS.register(new ModuleManager());
            EventBus var16 = MinecraftForge.EVENT_BUS;
            Hud var10001 = new Hud();
            if (((336200883 >> 4 | 9068068) & 29041506 ^ 1172673771) != 0) {
                  ;
            }

            var16.register(var10001);
            if ((246774292 ^ 95104819 ^ 152609244 ^ 307412472) != 0) {
                  ;
            }

            MinecraftForge.EVENT_BUS.register(new ClickGui());
            MinecraftForge.EVENT_BUS.register(new SettingsManager());
            if ((189305709 << 2 >> 3 >>> 1 ^ 47326427) == 0) {
                  ;
            }

            MinecraftForge.EVENT_BUS.register(new RenderWorldLastHandler());
            MinecraftForge.EVENT_BUS.register(new CommandManager());
            CommandManager var17 = new CommandManager;
            if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var17.<init>();
            commandManager = var17;
            settingsManager = new SettingsManager();
            ClickGui var18 = new ClickGui;
            if ((443031275 >>> 2 >> 1 ^ 1823415010) != 0) {
                  ;
            }

            var18.<init>();
            clickGui = var18;
            if (ModuleManager.getModuleByName("Config").isEnabled()) {
                  saveLoad = new SaveLoad();
            }

      }

      @EventHandler
      public void postInit(FMLPostInitializationEvent var1) {
      }
}
