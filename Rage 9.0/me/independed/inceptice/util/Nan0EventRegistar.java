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
import net.minecraftforge.fml.common.eventhandler.ListenerList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Nan0EventRegistar {
      private static void register(EventBus var0, Class var1, Object var2, Method var3, ModContainer var4) {
            if ((895836954 << 3 >>> 4 ^ 1030085666) != 0) {
                  ;
            }

            try {
                  if ((1136016396 << 1 >>> 2 & 503699358 ^ -1941973576) != 0) {
                        ;
                  }

                  String[] var10002 = new String[(0 | 1119172936) ^ 23971110 ^ 1138293871];
                  var10002[1405199335 << 2 ^ 865874256 ^ 2090516172] = "busID";
                  int var5 = ((Integer)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002)).intValue();
                  if (((79789301 ^ 57987235 ^ 113390234) << 3 ^ 196933216) == 0) {
                        ;
                  }

                  var10002 = new String[(0 << 4 ^ 1737515150 | 1453047728) ^ 2006712255];
                  if ((602676875 >> 1 >>> 2 >> 4 ^ 4708413) == 0) {
                        ;
                  }

                  var10002[(1884197938 ^ 36839005) << 2 >> 4 ^ -56679781] = "listeners";
                  ConcurrentHashMap var6 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
                  if (!"stop skidding".equals("please take a shower")) {
                        ;
                  }

                  Constructor var7 = var1.getConstructor();
                  var7.setAccessible((boolean)((0 >>> 4 << 1 | 1866393614) & 1517885270 ^ 1245184007));
                  Object var10000 = var7.newInstance();
                  if ((778932006 >>> 4 & 40475954 ^ 35205027 ^ 7976849) == 0) {
                        ;
                  }

                  Event var8 = (Event)var10000;
                  ASMEventHandler var9 = new ASMEventHandler(var2, var3, var4);
                  ListenerList var12 = var8.getListenerList();
                  if (((1759552233 ^ 1461309095) << 1 & 1844494926 & 1347513578 ^ 987031886) != 0) {
                        ;
                  }

                  if ((259269086 >> 4 >> 1 & 666289 & 77203 ^ -683864351) != 0) {
                        ;
                  }

                  var12.register(var5, var9.getPriority(), var9);
                  ArrayList var10 = (ArrayList)var6.get(var2);
                  if (var10 == null) {
                        if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        var10 = new ArrayList();
                        var6.put(var2, var10);
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                              ;
                        }

                        String[] var10003 = new String[(0 | 1263381289 | 988717177) ^ 1358532286 ^ 722871750];
                        var10003[(938818085 | 929270958 | 135791521) >>> 4 ^ 67107834] = "listeners";
                        ReflectionHelper.setPrivateValue(EventBus.class, var0, var6, var10003);
                  }

                  if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                        ;
                  }

                  var10.add(var9);
            } catch (Exception var11) {
            }

      }

      public static void register(EventBus var0, Object var1) {
            if (!"please dont crack my plugin".equals("please take a shower")) {
                  ;
            }

            String[] var10002 = new String[0 << 1 >>> 2 >> 3 ^ 1];
            int var10004 = (642463771 | 525282491 | 901179387) ^ 539427359 ^ 80252463 ^ 454063051;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                  ;
            }

            var10002[var10004] = "listeners";
            ConcurrentHashMap var2 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if (((162287120 >>> 4 >> 4 ^ 614311) >>> 1 ^ 31220) == 0) {
                  ;
            }

            var10002 = new String[(0 >> 2 >>> 4 | 1502468963) ^ 1502468962];
            var10004 = (268537985 ^ 8623354) << 4 >>> 4 ^ 8520827;
            if ((570899815 << 2 & 1111866413 ^ 308236) == 0) {
                  ;
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stop skidding")) {
                  ;
            }

            var10002[var10004] = "listenerOwners";
            Map var10000 = (Map)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if ((132139022 << 4 ^ 47667126 ^ 971741437) != 0) {
                  ;
            }

            Map var3 = var10000;
            if (!var2.containsKey(var1)) {
                  MinecraftDummyContainer var4 = Loader.instance().getMinecraftModContainer();
                  var3.put(var1, var4);
                  String[] var10003 = new String[(0 ^ 1836818016) << 3 ^ 1809642241];
                  var10003[((123334866 >> 3 & 724600) << 3 | 2654988) ^ 7926732] = "listenerOwners";
                  ReflectionHelper.setPrivateValue(EventBus.class, var0, var3, var10003);
                  Set var5 = TypeToken.of(((Object)var1).getClass()).getTypes().rawTypes();
                  Method[] var6 = (Method[])((Object)var1).getClass().getMethods();
                  int var7 = var6.length;

                  for(int var8 = 1663659897 << 2 ^ 733903670 ^ 1227436877 ^ -298650209; var8 < var7; ++var8) {
                        Method var9 = var6[var8];
                        Iterator var10 = var5.iterator();

                        while(true) {
                              if (((1723936176 << 3 | 216049602) & 487618399 ^ 469788482) == 0) {
                                    ;
                              }

                              if (!var10.hasNext()) {
                                    break;
                              }

                              if (((1069686155 | 375139919) >> 3 ^ 133941241) != 0) {
                              }

                              Class var11 = (Class)var10.next();

                              try {
                                    String var10001 = var9.getName();
                                    if (((2106819463 >> 1 >> 1 & 388927514 | 7485180) ^ 393656060) == 0) {
                                          ;
                                    }

                                    Method var12 = var11.getDeclaredMethod(var10001, (Class[])var9.getParameterTypes());
                                    if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if (var12.isAnnotationPresent(SubscribeEvent.class)) {
                                          Class[] var13 = (Class[])var9.getParameterTypes();
                                          Class var14 = var13[591279421 >> 3 >> 3 ^ 9238740];
                                          if (!"stop skidding".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          Nan0EventRegistar.register(var0, var14, var1, var9, var4);
                                          break;
                                    }
                              } catch (NoSuchMethodException var15) {
                                    if (((318213749 >>> 2 & 38912281) << 1 & 872367 ^ 1127874312) == 0) {
                                    }

                                    if ((((2078605763 | 430821678) >> 1 ^ 285228109) >> 3 ^ -467634752) != 0) {
                                          ;
                                    }
                              }
                        }
                  }

            }
      }
}
