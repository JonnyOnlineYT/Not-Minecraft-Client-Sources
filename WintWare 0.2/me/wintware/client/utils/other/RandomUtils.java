package me.wintware.client.utils.other;

public class RandomUtils {
   public static int randomNumber(int max, int min) {
      return Math.round((float)min + (float)Math.random() * (float)(max - min));
   }
}
