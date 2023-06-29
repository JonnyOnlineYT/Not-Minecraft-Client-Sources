package me.independed.inceptice.util;

public class TimerUtil {
      private long previousMS = 722884595666431280L << 15 << 26 ^ -7396493884886351872L;

      public void reset() {
            this.previousMS = this.getTime();
      }

      public boolean hasReached(double var1) {
            return (boolean)((double)(this.getTime() - this.previousMS) >= var1 ? (0 & 351200106) << 1 ^ 1 : 1942335856 >>> 1 << 2 >>> 1 ^ 1942335856);
      }

      public long getTime() {
            return System.nanoTime() / ((961686L ^ 875742L ^ 11518L) & 239132L ^ 837204L);
      }
}
