package org.spongepowered.asm.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VersionNumber implements Comparable, Serializable {
   private static final long serialVersionUID = 1L;
   public static final VersionNumber NONE = new VersionNumber();
   private static final Pattern PATTERN = Pattern.compile("^(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5}))?)?)?(-[a-zA-Z0-9_\\-]+)?$");
   private final long value;
   private final String suffix;

   private VersionNumber() {
      this.value = 0L;
      this.suffix = "";
   }

   private VersionNumber(short[] parts) {
      this(parts, (String)null);
   }

   private VersionNumber(short[] parts, String suffix) {
      this.value = pack(parts);
      this.suffix = suffix != null ? suffix : "";
   }

   private VersionNumber(short major, short minor, short revision, short build) {
      this(major, minor, revision, build, (String)null);
   }

   private VersionNumber(short major, short minor, short revision, short build, String suffix) {
      this.value = pack(major, minor, revision, build);
      this.suffix = suffix != null ? suffix : "";
   }

   public String toString() {
      short[] parts = unpack(this.value);
      return String.format("%d.%d%3$s%4$s%5$s", parts[0], parts[1], (this.value & 2147483647L) > 0L ? String.format(".%d", parts[2]) : "", (this.value & 32767L) > 0L ? String.format(".%d", parts[3]) : "", this.suffix);
   }

   public int compareTo(VersionNumber other) {
      if (other == null) {
         return 1;
      } else {
         long delta = this.value - other.value;
         return delta > 0L ? 1 : (delta < 0L ? -1 : 0);
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof VersionNumber)) {
         return false;
      } else {
         return ((VersionNumber)other).value == this.value;
      }
   }

   public int hashCode() {
      return (int)(this.value >> 32) ^ (int)(this.value & 4294967295L);
   }

   private static long pack(short... shorts) {
      return (long)shorts[0] << 48 | (long)shorts[1] << 32 | (long)(shorts[2] << 16) | (long)shorts[3];
   }

   private static short[] unpack(long along) {
      return new short[]{(short)((int)(along >> 48)), (short)((int)(along >> 32 & 32767L)), (short)((int)(along >> 16 & 32767L)), (short)((int)(along & 32767L))};
   }

   public static VersionNumber parse(String version) {
      return parse(version, NONE);
   }

   public static VersionNumber parse(String version, String defaultVersion) {
      return parse(version, parse(defaultVersion));
   }

   private static VersionNumber parse(String version, VersionNumber defaultVersion) {
      if (version == null) {
         return defaultVersion;
      } else {
         Matcher versionNumberPatternMatcher = PATTERN.matcher(version);
         if (!versionNumberPatternMatcher.matches()) {
            return defaultVersion;
         } else {
            short[] parts = new short[4];

            for(int pos = 0; pos < 4; ++pos) {
               String part = versionNumberPatternMatcher.group(pos + 1);
               if (part != null) {
                  int value = Integer.parseInt(part);
                  if (value > 32767) {
                     throw new IllegalArgumentException("Version parts cannot exceed 32767, found " + value);
                  }

                  parts[pos] = (short)value;
               }
            }

            return new VersionNumber(parts, versionNumberPatternMatcher.group(5));
         }
      }
   }
}
