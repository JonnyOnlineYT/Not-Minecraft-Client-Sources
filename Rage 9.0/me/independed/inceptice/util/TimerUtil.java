package me.independed.inceptice.util;

public class TimerUtil {
      private long previousMS = (3419354379109879902L | 2439313397161545108L | 2214979383200327169L | 391616486406381966L | 2696894061156614431L) ^ 4611686018393833439L;

      public boolean hasReached(double var1) {
            return (boolean)((double)(this.getTime() - this.previousMS) >= var1 ? 0 >>> 3 << 3 ^ 1 : (796485793 | 685307722) & 477532794 ^ 77174275 ^ 149488745);
      }

      public long getTime() {
            long var10000 = System.nanoTime() / (((210641L << 10 | 193283895L) & 210293009L) >> 22 ^ 1000050L);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please dont crack my plugin")) {
                  ;
            }

            return var10000;
      }

      public void reset() {
            if (((385401272 ^ 108739801) >>> 4 << 1 >>> 1 ^ 17317846) == 0) {
                  ;
            }

            this.previousMS = this.getTime();
      }
}
