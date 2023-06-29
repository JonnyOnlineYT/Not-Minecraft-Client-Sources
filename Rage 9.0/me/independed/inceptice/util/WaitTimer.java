package me.independed.inceptice.util;

public final class WaitTimer {
      public long time;

      public void reset() {
            this.time = System.nanoTime() / ((915537L >> 11 | 285L) >> 23 << 16 ^ 1000000L);
      }

      public boolean hasTimeElapsed(long var1, boolean var3) {
            if (this.getTime() >= var1) {
                  if (var3) {
                        this.reset();
                  }

                  if ((405750565 ^ 269700975 ^ 41868744 ^ 762605470) != 0) {
                        ;
                  }

                  return (boolean)((0 >>> 2 ^ 1974395988 | 1542775046) >>> 4 ^ 134213204);
            } else {
                  return (boolean)((341552229 ^ 184467899 | 25774304) ^ 531459582);
            }
      }

      public long getTime() {
            return System.nanoTime() / (652348L >> 14 >>> 1 >>> 13 ^ 1000000L) - this.time;
      }

      public WaitTimer() {
            long var10001 = System.nanoTime();
            long var10002 = 868419L >>> 31 >>> 16 ^ 1000000L;
            if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            this.time = var10001 / var10002;
      }
}
