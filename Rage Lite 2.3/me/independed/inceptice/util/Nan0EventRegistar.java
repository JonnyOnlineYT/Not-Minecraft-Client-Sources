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

public class Nan0EventRegistar {
      public Nan0EventRegistar() {
            if (((458386846 << 3 >> 3 >> 2 ^ 6278851) >>> 3 ^ 579656094) != 0) {
                  ;
            }

      }

      public static void register(EventBus var0, Object var1) {
            String[] var10002 = new String[(0 | 40207663) >>> 3 << 1 >>> 4 ^ 628245];
            var10002[(991361753 >>> 4 & 13106527) >>> 3 ^ 1060257] = "listeners";
            ConcurrentHashMap var2 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            var10002 = new String[(0 & 1320236773 & 21889618) << 1 ^ 1];
            var10002[38806020 >> 3 ^ 4850752] = "listenerOwners";
            Map var3 = (Map)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                  ;
            }

            if (!var2.containsKey(var1)) {
                  if (((1702711163 >> 2 << 4 << 2 | 1447206048) ^ 1473707936) == 0) {
                        ;
                  }

                  MinecraftDummyContainer var10000 = Loader.instance().getMinecraftModContainer();
                  if (!"yo mama name maurice".equals("please go outside")) {
                        ;
                  }

                  MinecraftDummyContainer var4 = var10000;
                  var3.put(var1, var4);
                  String[] var10003 = new String[((0 >>> 1 | 1207466619) & 574777329) << 3 ^ 302191497];
                  if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var10003[(1086276225 >> 2 | 66947545) ^ 6836857 ^ 69176481 ^ 394790689] = "listenerOwners";
                  ReflectionHelper.setPrivateValue(EventBus.class, var0, var3, var10003);
                  if (((4718592 | 3346188) ^ -428193192) != 0) {
                        ;
                  }

                  Set var5 = TypeToken.of(((Object)var1).getClass()).getTypes().rawTypes();
                  if ((167129155 >> 1 >> 2 << 2 ^ 83564576) == 0) {
                        ;
                  }

                  Method[] var6 = (Method[])((Object)var1).getClass().getMethods();
                  int var7 = var6.length;

                  for(int var8 = ((1598322615 | 1419581750) ^ 130647654) >>> 4 ^ 92360605; var8 < var7; ++var8) {
                        Method var9 = var6[var8];
                        Iterator var10 = var5.iterator();

                        while(var10.hasNext()) {
                              if ("Your family tree must be a cactus because everyone on it is a prick.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              }

                              Class var11 = (Class)var10.next();

                              try {
                                    Method var12 = var11.getDeclaredMethod(var9.getName(), (Class[])var9.getParameterTypes());
                                    if (var12.isAnnotationPresent(SubscribeEvent.class)) {
                                          Class[] var13 = (Class[])var9.getParameterTypes();
                                          Class var14 = var13[(747375075 << 2 ^ 1408605390) >>> 3 >>> 2 ^ 118369754];
                                          if ((952233985 << 1 ^ 1904467970) == 0) {
                                                ;
                                          }

                                          if (((100115575 ^ 70915803) & 2707510 ^ 593956) == 0) {
                                                ;
                                          }

                                          Nan0EventRegistar.register(var0, var14, var1, var9, var4);
                                          break;
                                    }
                              } catch (NoSuchMethodException var15) {
                                    if ("please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                    }
                              }
                        }
                  }

            }
      }

      private static void register(EventBus var0, Class var1, Object var2, Method var3, ModContainer var4) {
            try {
                  String[] var10002 = new String[((0 & 1200448161) >>> 2 | 1010360518) & 564252071 ^ 539017351];
                  var10002[190931655 >>> 3 & 21873077 ^ 21757968] = "busID";
                  if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  int var5 = ((Integer)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002)).intValue();
                  int var12 = (0 & 314604514 | 449902355) & 324566272 & 277280843 ^ 268464129;
                  if ((8926336 >> 2 ^ 1198919 ^ 3163751) == 0) {
                        ;
                  }

                  var10002 = new String[var12];
                  var10002[(569591887 >> 4 ^ 33157138) >> 3 ^ 8181906] = "listeners";
                  ConcurrentHashMap var6 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
                  if (((1714605970 ^ 792004633 ^ 999822828 ^ 447545217 | 877944211) ^ 2088205815) == 0) {
                        ;
                  }

                  Constructor var7 = var1.getConstructor();
                  var7.setAccessible((boolean)(0 << 2 << 2 & 1828988704 ^ 1));
                  Object[] var10001 = new Object[1636643549 << 4 >>> 1 ^ 63534331 ^ 262201875];
                  if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  Event var8 = (Event)var7.newInstance(var10001);
                  ASMEventHandler var10000 = new ASMEventHandler;
                  if (((606699091 >>> 4 | 32276515) >> 1 & 24601416 ^ 24593216) == 0) {
                        ;
                  }

                  var10000.<init>(var2, var3, var4);
                  if ((((1320324897 | 67849847) & 1127802733 | 1004726562) ^ 2080042855) == 0) {
                        ;
                  }

                  ASMEventHandler var9 = var10000;
                  if ((1643733868 >>> 3 ^ 136434508 ^ -1493371434) != 0) {
                        ;
                  }

                  var8.getListenerList().register(var5, var9.getPriority(), var9);
                  ArrayList var10 = (ArrayList)var6.get(var2);
                  if (var10 == null) {
                        var10 = new ArrayList();
                        if ((825237600 ^ 724291486 ^ -1378772112) != 0) {
                              ;
                        }

                        var6.put(var2, var10);
                        if (((1141142080 << 1 >>> 1 << 4 | 552209415) ^ 769043888 ^ -1865934491) != 0) {
                              ;
                        }

                        String[] var10003 = new String[(0 | 912819373 | 341265943) & 572604309 ^ 237882651 ^ 739033999];
                        var10003[1087328763 >>> 3 >> 1 & 29271914 ^ 630103 ^ 342109] = "listeners";
                        if ((((1365688924 | 832550014) & 273059931 | 246632819 | 203498825) ^ 519566715) == 0) {
                              ;
                        }

                        ReflectionHelper.setPrivateValue(EventBus.class, var0, var6, var10003);
                  }

                  var10.add(var9);
            } catch (Exception var11) {
            }

      }
}
