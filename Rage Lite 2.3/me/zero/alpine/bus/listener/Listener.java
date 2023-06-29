package me.zero.alpine.bus.listener;

import java.util.function.Predicate;
import net.jodah.typetools.TypeResolver;

public final class Listener implements EventHook {
      private final EventHook hook;
      private final byte priority;
      private final Predicate[] filters;
      private final Class target;

      @SafeVarargs
      public Listener(EventHook var1, Predicate... var2) {
            if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                  ;
            }

            this(var1, (byte)(((0 | 21973992) & 5871102) << 3 >> 3 ^ 4784619), var2);
      }

      public final byte getPriority() {
            return this.priority;
      }

      public final Class getTarget() {
            return this.target;
      }

      @SafeVarargs
      public Listener(EventHook var1, byte var2, Predicate... var3) {
            if (!"please take a shower".equals("yo mama name maurice")) {
                  ;
            }

            super();
            this.hook = var1;
            this.priority = var2;
            this.target = TypeResolver.resolveRawArgument(EventHook.class, var1.getClass());
            this.filters = var3;
            if (!"you probably spell youre as your".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

      }

      public final void invoke(Object var1) {
            Predicate[] var10000 = this.filters;
            if (((50692249 ^ 27658345) << 3 ^ -24823025) != 0) {
                  ;
            }

            if (var10000.length > 0) {
                  Predicate[] var2 = this.filters;
                  if (!"yo mama name maurice".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  int var3 = var2.length;

                  for(int var4 = (1858722869 >>> 1 >>> 4 ^ 2888761) >> 1 ^ 28126572; var4 < var3; ++var4) {
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        Predicate var5 = var2[var4];
                        if (!var5.test(var1)) {
                              return;
                        }
                  }
            }

            this.hook.invoke(var1);
            if ((((980429043 | 543345801) >> 4 | 35595823) ^ 62861039) == 0) {
                  ;
            }

      }
}
