package me.independed.inceptice.util;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.ASMEventHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Nan0EventRegister {
      private static void register(EventBus var0, Class var1, Object var2, Method var3, ModContainer var4) {
            try {
                  int var10002 = (0 >>> 1 | 228137766) << 4 ^ -644763039;
                  if (((1333231955 >>> 3 | 35521988) << 3 ^ 1278297077 ^ -1173275549) != 0) {
                        ;
                  }

                  String[] var13 = new String[var10002];
                  var13[38012930 >>> 4 ^ 2375808] = "busID";
                  int var5 = ((Integer)ReflectionHelper.getPrivateValue(EventBus.class, var0, var13)).intValue();
                  var13 = new String[0 << 4 >>> 2 >> 3 ^ 1];
                  var13[1357314702 >>> 4 >>> 2 ^ 21208042] = "listeners";
                  ConcurrentHashMap var6 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var13);
                  if ((521771733 >> 4 << 1 & 50142149 ^ 1281275515) != 0) {
                        ;
                  }

                  Constructor var7 = var1.getConstructor();
                  if (((699407717 | 359654612 | 938101845) ^ -596903987) != 0) {
                        ;
                  }

                  var7.setAccessible((boolean)((0 >> 4 | 663959605) ^ 663959604));
                  Event var8 = (Event)var7.newInstance();
                  ASMEventHandler var10000 = new ASMEventHandler;
                  if ((1091147006 >> 3 >>> 4 >> 2 ^ -1586903399) != 0) {
                        ;
                  }

                  var10000.<init>(var2, var3, var4);
                  ASMEventHandler var9 = var10000;
                  var8.getListenerList().register(var5, var9.getPriority(), var9);
                  ArrayList var10 = (ArrayList)var6.get(var2);
                  if (var10 == null) {
                        if (((217715074 ^ 214997983) >>> 4 ^ 1661128603) != 0) {
                              ;
                        }

                        ArrayList var12 = new ArrayList();
                        if (((1908261461 ^ 197016883) & 662413784 ^ 63546555 ^ -1966792428) != 0) {
                              ;
                        }

                        var10 = var12;
                        if (!"minecraft".equals("idiot")) {
                              ;
                        }

                        var6.put(var2, var10);
                        if (((27872128 >> 2 ^ 1286717) & 6260005 & 4773003 ^ 246171073) != 0) {
                              ;
                        }

                        String[] var10003 = new String[(0 >>> 3 | 1413003334) ^ 1413003335];
                        var10003[((1953783421 >>> 3 & 64224350) >> 2 | 3718769) ^ 12107379] = "listeners";
                        if ((285424260 >> 1 & 4278489 ^ -841637611) != 0) {
                              ;
                        }

                        ReflectionHelper.setPrivateValue(EventBus.class, var0, var6, var10003);
                  }

                  var10.add(var9);
            } catch (Exception var11) {
            }

      }

      public static void register(EventBus var0, Object var1) {
            if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            String[] var10002 = new String[(0 & 1622974143 | 1218556284) & 416253593 ^ 123903039 ^ 266542118];
            var10002[1021497842 << 4 << 4 >> 3 & 555340327 ^ 538448384] = "listeners";
            ConcurrentHashMap var2 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            var10002 = new String[(0 ^ 870204988 ^ 321840581 ^ 244643052) >>> 4 ^ 48645488];
            var10002[(964350581 >>> 3 | 17369029) ^ 120545231] = "listenerOwners";
            Map var3 = (Map)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if ((1804173153 >> 2 >>> 2 & 30926732 ^ -187581650) != 0) {
                  ;
            }

            if (!var2.containsKey(var1)) {
                  MinecraftDummyContainer var4 = Loader.instance().getMinecraftModContainer();
                  var3.put(var1, var4);
                  String[] var10003 = new String[(0 >>> 3 & 1605899278) >>> 4 ^ 1];
                  int var10005 = 326473136 << 1 >> 4 ^ 40809142;
                  if (((136663072 >>> 4 | 2093106) ^ 301974509) != 0) {
                        ;
                  }

                  var10003[var10005] = "listenerOwners";
                  ReflectionHelper.setPrivateValue(EventBus.class, var0, var3, var10003);
                  Set var5 = TypeToken.of(((Object)var1).getClass()).getTypes().rawTypes();
                  Method[] var6 = (Method[])((Object)var1).getClass().getMethods();
                  if (((1483483195 | 1274921000) << 4 >> 4 ^ -67246021) == 0) {
                        ;
                  }

                  int var7 = var6.length;
                  int var8 = 17920 << 4 ^ 286720;

                  while(true) {
                        if (((((1718665119 | 1220676742) ^ 1155474407) & 155549855) >> 3 & 3398613 ^ -1341340117) != 0) {
                              ;
                        }

                        if (((1265869804 | 606374966 | 1852964662) >> 3 & 22401603 ^ 21287491) == 0) {
                              ;
                        }

                        if (var8 >= var7) {
                              return;
                        }

                        Method var9 = var6[var8];
                        Iterator var10 = var5.iterator();

                        while(true) {
                              if ("please go outside".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              }

                              boolean var10000 = var10.hasNext();
                              if (!"stringer is a good obfuscator".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              if (!var10000) {
                                    break;
                              }

                              Class var11 = (Class)var10.next();

                              try {
                                    Method var12 = var11.getDeclaredMethod(var9.getName(), (Class[])var9.getParameterTypes());
                                    if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var10000 = var12.isAnnotationPresent(SubscribeEvent.class);
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("intentMoment")) {
                                          ;
                                    }

                                    if (var10000) {
                                          if ((((618998548 ^ 352205576) >>> 3 & 56129836 ^ 19503214 | 49734488) ^ 67107710) == 0) {
                                                ;
                                          }

                                          Class[] var13 = (Class[])var9.getParameterTypes();
                                          Class var14 = var13[(1496477955 | 1324551907) ^ 566655294 ^ 2117404381];
                                          if (((133077867 | 96535397) << 2 >>> 4 ^ 1123879038) != 0) {
                                                ;
                                          }

                                          Nan0EventRegister.register(var0, var14, var1, var9, var4);
                                          break;
                                    }
                              } catch (NoSuchMethodException var15) {
                                    if (((1012168841 ^ 903052077 ^ 14219020) << 4 ^ 1211952125) == 0) {
                                    }
                              }
                        }

                        ++var8;
                  }
            }
      }
}
