package me.zero.alpine.bus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.zero.alpine.bus.listener.EventHandler;
import me.zero.alpine.bus.listener.Listener;

public class EventManager implements EventBus {
      private final List ATTACHED_BUSES;
      private final Map SUBSCRIPTION_CACHE = new HashMap();
      private final Map SUBSCRIPTION_MAP;

      public void subscribe(Iterable var1) {
            Consumer var10001 = this::subscribe;
            if (((1561575906 ^ 1280084199 | 74671250) ^ -632636792) != 0) {
                  ;
            }

            var1.forEach(var10001);
      }

      public void unsubscribe(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_CACHE.get(var1);
            if ((1941983520 >>> 4 >> 3 ^ 14654220 ^ -1309766006) != 0) {
                  ;
            }

            if (var2 != null) {
                  if (((45044346 | 42425402) << 3 ^ -1820831597) != 0) {
                        ;
                  }

                  if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  Collection var10000 = this.SUBSCRIPTION_MAP.values();
                  if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var10000.forEach((var1x) -> {
                        Objects.requireNonNull(var2);
                        var1x.removeIf(var2::contains);
                  });
                  List var3 = this.ATTACHED_BUSES;
                  if ((((324337963 ^ 270975838 | 9279559) ^ 28583553) >> 4 & '讜' ^ 1892427891) != 0) {
                        ;
                  }

                  if (!var3.isEmpty()) {
                        var3 = this.ATTACHED_BUSES;
                        if (!"intentMoment".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        Consumer var10001 = (var1x) -> {
                              var1x.unsubscribe(var1);
                              if (!"please take a shower".equals("stop skidding")) {
                                    ;
                              }

                        };
                        if (((95873582 >>> 4 & 3449295 & 538546) << 3 ^ 66576) == 0) {
                              ;
                        }

                        var3.forEach(var10001);
                  }
            }

      }

      public EventManager() {
            if (((1035412107 >> 2 | 175604184) ^ -1014008194) != 0) {
                  ;
            }

            if (((863842191 >>> 2 & 137637403) >> 1 ^ 1576325786) != 0) {
                  ;
            }

            this.SUBSCRIPTION_MAP = new HashMap();
            this.ATTACHED_BUSES = new ArrayList();
      }

      public void unsubscribe(Object... var1) {
            Arrays.stream(var1).forEach(this::unsubscribe);
      }

      private static Listener asListener(Object var0, Field var1) {
            try {
                  if (!"ape covered in human flesh".equals("please dont crack my plugin")) {
                        ;
                  }

                  boolean var2 = var1.isAccessible();
                  var1.setAccessible((boolean)((0 << 1 >>> 3 & 744421023 | 1818519948) ^ 932517536 ^ 1542553389));
                  Listener var3 = (Listener)var1.get(var0);
                  var1.setAccessible(var2);
                  if (var3 == null) {
                        return null;
                  } else if (var3.getPriority() <= ((4 << 1 ^ 6) >> 1 ^ 2) && var3.getPriority() >= ((0 >>> 3 & 490779509) << 1 & 1547867605 ^ 1)) {
                        return var3;
                  } else {
                        throw new RuntimeException("Event Priority out of bounds! %s");
                  }
            } catch (IllegalAccessException var4) {
                  if ("Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  }

                  return null;
            }
      }

      private static boolean isValidField(Field var0) {
            return (boolean)(var0.isAnnotationPresent(EventHandler.class) && Listener.class.isAssignableFrom(var0.getType()) ? (0 | 387255713 | 107520523) >> 1 >>> 1 ^ 14493021 ^ 84037302 : 1608238634 << 2 << 1 >>> 2 ^ 1068993620);
      }

      public void attach(EventBus var1) {
            List var10000 = this.ATTACHED_BUSES;
            if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                  ;
            }

            if (!var10000.contains(var1)) {
                  this.ATTACHED_BUSES.add(var1);
            }

      }

      public void subscribe(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_CACHE.computeIfAbsent(var1, (var0) -> {
                  if (((662383374 ^ 26798637) >>> 1 ^ 326230161) == 0) {
                        ;
                  }

                  Stream var10000 = Arrays.stream((Field[])((Object)var0).getClass().getDeclaredFields()).filter(EventManager::isValidField).map((var1) -> {
                        return EventManager.asListener(var0, var1);
                  }).filter(Objects::nonNull);
                  Collector var10001 = Collectors.toList();
                  if (!"intentMoment".equals("you probably spell youre as your")) {
                        ;
                  }

                  return (List)var10000.collect(var10001);
            });
            if (((97925403 ^ 4839277) >> 3 ^ 2725966 ^ -678498309) != 0) {
                  ;
            }

            var2.forEach(this::subscribe);
            if (!this.ATTACHED_BUSES.isEmpty()) {
                  List var10000 = this.ATTACHED_BUSES;
                  if (((67239955 >> 4 >> 3 | 353523) ^ 877811) == 0) {
                        ;
                  }

                  var10000.forEach((var1x) -> {
                        var1x.subscribe(var1);
                  });
            }

      }

      public void detach(EventBus var1) {
            if (this.ATTACHED_BUSES.contains(var1)) {
                  this.ATTACHED_BUSES.remove(var1);
            }

            if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                  ;
            }

      }

      public void subscribe(Object... var1) {
            if ((421725291 << 4 >> 3 >>> 3 ^ 508084506) == 0) {
                  ;
            }

            Arrays.stream(var1).forEach(this::subscribe);
      }

      public void post(Object var1) {
            List var2 = (List)this.SUBSCRIPTION_MAP.get(((Object)var1).getClass());
            if (var2 != null) {
                  var2.forEach((var1x) -> {
                        if (((255385527 ^ 3270102 ^ 144330157 ^ 98602448) >>> 3 ^ 1357975492) != 0) {
                              ;
                        }

                        var1x.invoke(var1);
                        if (!"please take a shower".equals("stop skidding")) {
                              ;
                        }

                  });
            }

            if (!this.ATTACHED_BUSES.isEmpty()) {
                  this.ATTACHED_BUSES.forEach((var1x) -> {
                        var1x.post(var1);
                  });
            }

      }

      private void subscribe(Listener var1) {
            Map var10000 = this.SUBSCRIPTION_MAP;
            if ((379954298 >> 4 & 19283644 ^ 19012100) == 0) {
                  ;
            }

            List var2 = (List)var10000.computeIfAbsent(var1.getTarget(), (var0) -> {
                  return new ArrayList();
            });
            int var3 = 2001087635 >> 4 << 4 & 127381984 ^ 117842048;

            while(true) {
                  if (((1010992837 ^ 563886023) << 4 ^ -571797472) == 0) {
                        ;
                  }

                  if (var3 >= var2.size()) {
                        break;
                  }

                  if (((762337280 >> 2 << 1 >>> 3 | 33930257) ^ -812860215) != 0) {
                        ;
                  }

                  byte var4 = var1.getPriority();
                  Listener var10001 = (Listener)var2.get(var3);
                  if ((562472687 >> 2 >>> 2 ^ -769776638) != 0) {
                        ;
                  }

                  if (var4 < var10001.getPriority()) {
                        break;
                  }

                  ++var3;
            }

            if ((1765282421 >> 2 >>> 3 ^ 55165075) == 0) {
                  ;
            }

            var2.add(var3, var1);
      }

      public void unsubscribe(Iterable var1) {
            var1.forEach(this::unsubscribe);
      }
}
