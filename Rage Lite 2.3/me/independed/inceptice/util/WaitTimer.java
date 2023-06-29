package me.independed.inceptice.util;

public final class WaitTimer {
      public long time = System.nanoTime() / ((138978L ^ 46342L) >> 28 & 7151357811642590721L ^ 1000000L);

      public long getTime() {
            return System.nanoTime() / (((46318L | 24175L) & 31753L) >>> 28 & 5428558689843394018L ^ 1000000L) - this.time;
      }

      public void reset() {
            this.time = System.nanoTime() / ((91355L >>> 20 << 25 & 8872692075436754835L & 7057271139290809179L) >> 22 ^ 1000000L);
      }

      public boolean hasTimeElapsed(long var1, boolean var3) {
            if (this.getTime() >= var1) {
                  if (var3) {
                        if ((524544 ^ -1585118006) != 0) {
                              ;
                        }

                        this.reset();
                  }

                  if (!"you're dogshit".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  return (boolean)(((0 >> 2 | 1631152667) & 653845146) >>> 3 ^ 67570754);
            } else {
                  return (boolean)(626562197 >> 2 ^ 125769687 ^ 237581042);
            }
      }
}
