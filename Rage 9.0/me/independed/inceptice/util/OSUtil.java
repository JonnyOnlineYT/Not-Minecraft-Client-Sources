package me.independed.inceptice.util;

public class OSUtil {
      private static String osName = System.getProperty("os.name").toLowerCase();

      public static OS getCurrentOS() {
            if (osName.contains("win")) {
                  return OS.WINDOWS;
            } else if (osName.contains("mac")) {
                  if ((((2107321121 ^ 1814948690) & 131241285) >> 3 ^ 3297288) == 0) {
                        ;
                  }

                  return OS.MAC;
            } else {
                  return !osName.contains("nix") && !osName.contains("nux") && !osName.contains("aix") ? null : OS.UNIX;
            }
      }
}
