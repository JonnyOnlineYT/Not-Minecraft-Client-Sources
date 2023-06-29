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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.ListenerList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Nan0EventRegister {
      private static void register(EventBus var0, Class var1, Object var2, Method var3, ModContainer var4) {
            try {
                  String[] var10002 = new String[(0 | 860952614) >>> 2 & 93896532 ^ 76547585];
                  var10002[636356735 >> 2 << 2 ^ 636356732] = "busID";
                  Integer var10000 = (Integer)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
                  if (((840775512 << 4 & 27333400) >> 4 ^ -754059248) != 0) {
                        ;
                  }

                  int var5 = var10000.intValue();
                  var10002 = new String[(0 ^ 342449492) << 1 & 138609379 ^ 138543777];
                  var10002[(532294659 ^ 491851058) << 3 ^ 391711112] = "listeners";
                  ConcurrentHashMap var6 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
                  Constructor var7 = var1.getConstructor();
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var7.setAccessible((boolean)((0 >> 3 ^ 1017369530) >>> 4 & 31017227 ^ 29886730));
                  Event var8 = (Event)var7.newInstance();
                  ASMEventHandler var12 = new ASMEventHandler;
                  if ((22496107 << 2 ^ 25158865 ^ 69396861) == 0) {
                        ;
                  }

                  var12.<init>(var2, var3, var4);
                  ASMEventHandler var9 = var12;
                  ListenerList var13 = var8.getListenerList();
                  if ((((1576356592 >>> 4 ^ 89895194) >> 1 & 3845702) << 1 ^ 279684) == 0) {
                        ;
                  }

                  EventPriority var14 = var9.getPriority();
                  if (!"please take a shower".equals("you probably spell youre as your")) {
                        ;
                  }

                  var13.register(var5, var14, var9);
                  ArrayList var10 = (ArrayList)var6.get(var2);
                  if (var10 == null) {
                        var10 = new ArrayList();
                        if (!"ape covered in human flesh".equals("please take a shower")) {
                              ;
                        }

                        var6.put(var2, var10);
                        if (((415354070 | 159099470 | 116082046) >> 4 ^ 33537791) == 0) {
                              ;
                        }

                        String[] var10003 = new String[0 << 4 >> 1 ^ 1];
                        if (((1437226673 >> 3 & 174679803) >> 2 >> 3 ^ 5310998) == 0) {
                              ;
                        }

                        var10003[(210109301 ^ 169386356) >> 4 ^ 6941216] = "listeners";
                        ReflectionHelper.setPrivateValue(EventBus.class, var0, var6, var10003);
                  }

                  var10.add(var9);
            } catch (Exception var11) {
            }

            if (((788354120 | 311446624 | 488972146) & 883942667 ^ 883770634) == 0) {
                  ;
            }

      }

      public static void register(EventBus var0, Object var1) {
            String[] var10002 = new String[(0 | 1901942928) << 1 ^ 1194864093 ^ -1518166788];
            var10002[(411548104 << 3 & 1634840737 & 567995023) << 1 ^ 2162688] = "listeners";
            ConcurrentHashMap var2 = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var10002 = new String[(0 & 1144223225 | 662462780) >>> 2 ^ 165615694];
            int var10004 = (1740776022 >>> 4 | 15572532) >> 2 ^ 29323149;
            if (((231452106 | 170363941 | 229002993) << 2 ^ -2075603030) != 0) {
                  ;
            }

            var10002[var10004] = "listenerOwners";
            Map var3 = (Map)ReflectionHelper.getPrivateValue(EventBus.class, var0, var10002);
            if (!"idiot".equals("nefariousMoment")) {
                  ;
            }

            if (!var2.containsKey(var1)) {
                  MinecraftDummyContainer var4 = Loader.instance().getMinecraftModContainer();
                  if (((6806177 << 2 ^ 5189009) << 1 << 4 ^ 961190550) != 0) {
                        ;
                  }

                  var3.put(var1, var4);
                  String[] var10003 = new String[0 << 4 >> 1 ^ 1];
                  var10003[(984546276 >>> 2 ^ 16435791) & 78133602 & 23414685 & 1600126237 ^ 0] = "listenerOwners";
                  ReflectionHelper.setPrivateValue(EventBus.class, var0, var3, var10003);
                  Set var5 = TypeToken.of(((Object)var1).getClass()).getTypes().rawTypes();
                  if (((843230562 << 4 >> 4 | 26737333) << 1 ^ 128974830) == 0) {
                        ;
                  }

                  if ((451394878 >> 2 & 13117400 ^ 968854674) != 0) {
                        ;
                  }

                  Class var10000 = ((Object)var1).getClass();
                  if (!"idiot".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  Method[] var6 = (Method[])var10000.getMethods();
                  int var7 = var6.length;

                  for(int var8 = (6309743 | 863810) >> 3 >>> 2 ^ 224123; var8 < var7; ++var8) {
                        if (!"buy a domain and everything else you need at namecheap.com".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        Method var9 = var6[var8];
                        if ((1125154946 >> 3 >> 2 >> 4 & 83943 ^ 65600) == 0) {
                              ;
                        }

                        Iterator var10 = var5.iterator();

                        while(var10.hasNext()) {
                              Class var11 = (Class)var10.next();

                              try {
                                    if (((1046481672 >> 3 >> 3 ^ 12241232) & 74831 & '鈾' ^ 1920599695) != 0) {
                                          ;
                                    }

                                    Method var12 = var11.getDeclaredMethod(var9.getName(), (Class[])var9.getParameterTypes());
                                    if (var12.isAnnotationPresent(SubscribeEvent.class)) {
                                          Class[] var13 = (Class[])var9.getParameterTypes();
                                          Class var14 = var13[1446504833 >> 3 & 66757190 ^ 46309376];
                                          Nan0EventRegister.register(var0, var14, var1, var9, var4);
                                          break;
                                    }
                              } catch (NoSuchMethodException var15) {
                                    continue;
                              }

                              if (((1879347374 | 592938231 | 487620560) >> 1 ^ 1068236799) == 0) {
                                    ;
                              }
                        }
                  }

            }
      }
}
