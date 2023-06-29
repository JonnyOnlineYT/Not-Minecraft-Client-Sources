package me.zero.alpine.bus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.zero.alpine.bus.listener.EventHandler;
import me.zero.alpine.bus.listener.Listener;

public class EventManager implements EventBus {
      private final Map SUBSCRIPTION_MAP = new HashMap();
      private final Map SUBSCRIPTION_CACHE = new HashMap();
      private final List ATTACHED_BUSES;

      public void unsubscribe(Iterable var1) {
            var1.forEach(this::unsubscribe);
      }

      private void subscribe(Listener var1) {
            List var2 = (List)this.SUBSCRIPTION_MAP.computeIfAbsent(var1.getTarget(), (var0) -> {
                  return new ArrayList();
            });

            int var3;
            for(var3 = (364146686 << 2 & 1300380475 | 20059852) ^ 1169309692; var3 < var2.size() && var1.getPriority() >= ((Listener)var2.get(var3)).getPriority(); ++var3) {
            }

            var2.add(var3, var1);
      }

      public void subscribe(Object... var1) {
            Arrays.stream(var1).forEach(this::subscribe);
      }

      public void subscribe(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_CACHE.computeIfAbsent(var1, (var0) -> {
                  if ((1351425092 >> 4 ^ 84464068) == 0) {
                        ;
                  }

                  return (List)Arrays.stream((Field[])((Object)var0).getClass().getDeclaredFields()).filter(EventManager::isValidField).map((var1) -> {
                        return EventManager.asListener(var0, var1);
                  }).filter(Objects::nonNull).collect(Collectors.toList());
            });
            var2.forEach(this::subscribe);
            if (!this.ATTACHED_BUSES.isEmpty()) {
                  this.ATTACHED_BUSES.forEach((var1x) -> {
                        var1x.subscribe(var1);
                  });
            }

      }

      private static boolean isValidField(Field var0) {
            return (boolean)(var0.isAnnotationPresent(EventHandler.class) && Listener.class.isAssignableFrom(var0.getType()) ? ((0 | 688107965) << 3 | 79935913) << 3 ^ 1727917897 : (798621475 ^ 395548044) >>> 1 << 2 & 48791241 ^ 21576);
      }

      public void unsubscribe(Object... var1) {
            Arrays.stream(var1).forEach(this::unsubscribe);
      }

      public void detach(EventBus var1) {
            if (this.ATTACHED_BUSES.contains(var1)) {
                  List var10000 = this.ATTACHED_BUSES;
                  if (!"you probably spell youre as your".equals("you're dogshit")) {
                        ;
                  }

                  var10000.remove(var1);
            }

            if (!"ape covered in human flesh".equals("stringer is a good obfuscator")) {
                  ;
            }

      }

      public void subscribe(Iterable var1) {
            if ((((1449031231 | 1446595607) >> 4 ^ 35050431) & 119220499 ^ 118562832) == 0) {
                  ;
            }

            var1.forEach(this::subscribe);
      }

      public void unsubscribe(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_CACHE.get(var1);
            if (var2 != null) {
                  this.SUBSCRIPTION_MAP.values().forEach((var1x) -> {
                        Objects.requireNonNull(var2);
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("yo mama name maurice")) {
                              ;
                        }

                        var1x.removeIf(var2::contains);
                  });
                  List var10000 = this.ATTACHED_BUSES;
                  if (((((1184901740 ^ 824522371) & 1594397669 | 926346413) >> 1 | 937087979) ^ 1071382527) == 0) {
                        ;
                  }

                  if (!var10000.isEmpty()) {
                        this.ATTACHED_BUSES.forEach((var1x) -> {
                              if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var1x.unsubscribe(var1);
                        });
                  }
            }

            if (((1912296003 >>> 3 ^ 200542738) >> 3 >> 4 ^ 759488) == 0) {
                  ;
            }

      }

      public void attach(EventBus var1) {
            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            if (!this.ATTACHED_BUSES.contains(var1)) {
                  this.ATTACHED_BUSES.add(var1);
            }

      }

      public EventManager() {
            if (((90811768 ^ 14429162) >> 3 << 1 ^ 231794535) != 0) {
                  ;
            }

            this.ATTACHED_BUSES = new ArrayList();
      }

      public void post(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_MAP.get(((Object)var1).getClass());
            if (var2 != null) {
                  if ((369334804 >> 1 << 1 << 3 ^ -1340288864) == 0) {
                        ;
                  }

                  Consumer var10001 = (var1x) -> {
                        var1x.invoke(var1);
                  };
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                        ;
                  }

                  var2.forEach(var10001);
            }

            if (!this.ATTACHED_BUSES.isEmpty()) {
                  if (((79956918 | 60570021) >> 1 << 2 ^ 157916001) != 0) {
                        ;
                  }

                  this.ATTACHED_BUSES.forEach((var1x) -> {
                        var1x.post(var1);
                  });
            }

      }

      private static Listener asListener(Object var0, Field var1) {
            try {
                  boolean var2 = var1.isAccessible();
                  if (((509825 >> 2 >> 3 | 580) ^ 742922152) != 0) {
                        ;
                  }

                  var1.setAccessible((boolean)(0 >> 3 >>> 1 ^ 670248187 ^ 547220613 ^ 124698751));
                  Listener var3 = (Listener)var1.get(var0);
                  var1.setAccessible(var2);
                  if (((18187234 | 13572821) >> 4 >> 4 >>> 2 ^ 188302386) != 0) {
                        ;
                  }

                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                        ;
                  }

                  if (var3 == null) {
                        return null;
                  } else {
                        byte var10000 = var3.getPriority();
                        int var10001 = 0 >> 1 >> 3 ^ 5;
                        if ((0 ^ 0) == 0) {
                              ;
                        }

                        if (var10000 <= var10001 && var3.getPriority() >= (((0 ^ 1849308880) >> 4 ^ 74572455 | 39007884) ^ 47413199)) {
                              return var3;
                        } else {
                              throw new RuntimeException("Event Priority out of bounds! %s");
                        }
                  }
            } catch (IllegalAccessException var4) {
                  return null;
            }
      }
}
