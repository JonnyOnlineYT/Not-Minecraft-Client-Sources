package me.zero.alpine.bus.listener;

import java.util.function.Predicate;
import net.jodah.typetools.TypeResolver;

public final class Listener implements EventHook {
      private final Class target;
      private final Predicate[] filters;
      private final EventHook hook;
      private final byte priority;

      @SafeVarargs
      public Listener(EventHook var1, Predicate... var2) {
            this(var1, (byte)((1 >> 1 >>> 1 | 769362151) ^ 769362148), var2);
      }

      public final void invoke(Object var1) {
            if ((((1215434313 << 3 | 832334121) << 1 | 1714093123) ^ -1169264688) != 0) {
                  ;
            }

            if (this.filters.length > 0) {
                  Predicate[] var2 = this.filters;
                  int var3 = var2.length;
                  int var4 = (621235898 >>> 1 | 234302941) ^ 536326109;

                  while(var4 < var3) {
                        Predicate var5 = var2[var4];
                        if (!var5.test(var1)) {
                              if ((((353168707 | 352531984) & 30894385) >>> 4 ^ -1882560706) != 0) {
                                    ;
                              }

                              return;
                        }

                        ++var4;
                        if ((1317139438 >>> 1 << 2 & 1580225383 & 181186785 ^ 134217792) == 0) {
                              ;
                        }
                  }
            }

            this.hook.invoke(var1);
            if (!"you're dogshit".equals("intentMoment")) {
                  ;
            }

      }

      public final byte getPriority() {
            if (!"intentMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            return this.priority;
      }

      @SafeVarargs
      public Listener(EventHook var1, byte var2, Predicate... var3) {
            this.hook = var1;
            this.priority = var2;
            if ((1493303961 >> 2 >> 1 ^ 186662995) == 0) {
                  ;
            }

            this.target = TypeResolver.resolveRawArgument(EventHook.class, var1.getClass());
            this.filters = var3;
      }

      public final Class getTarget() {
            return this.target;
      }
}
