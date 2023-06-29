package me.independed.inceptice.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SysUtils {
      public static ArrayList array;

      public static String convertToHex(byte[] var0) {
            StringBuffer var1 = new StringBuffer();
            int var2 = (1951710135 | 896553524 | 1585952179) >>> 2 ^ 536739821;

            while(var2 < var0.length) {
                  int var3 = var0[var2] >>> (0 << 4 >> 1 >> 2 ^ 4) & ((4 >> 1 ^ 1 | 2) & 2 ^ 13);
                  int var4 = (53716478 | 24605342) >> 1 ^ 13938793 ^ 24071062;

                  do {
                        if (var3 >= 0 && var3 <= (4 >> 3 >>> 4 ^ 9)) {
                              var1.append((char)(((33 ^ 9) >> 3 ^ 53) + var3));
                        } else {
                              char var10001 = (char)((((9 ^ 5) << 2 & 32) << 4 >> 1 ^ 353) + (var3 - ((6 ^ 2 | 1) ^ 15)));
                              if (((917298851 | 414947324 | 855145846) >> 1 >> 1 ^ 264241151) == 0) {
                                    ;
                              }

                              var1.append(var10001);
                        }

                        var3 = var0[var2] & ((3 | 2) << 2 << 1 ^ 23);
                  } while(var4++ < (0 & 788414361 ^ 2129380030 ^ 327094813 ^ 1838469282));

                  ++var2;
                  if (((1278598598 >> 2 << 1 | 219882992) ^ 173208332 ^ 625548542) == 0) {
                        ;
                  }
            }

            return var1.toString();
      }

      public SysUtils() {
            if (((1323173340 << 2 | 165028889 | 845993611) >>> 4 ^ -1053366766) != 0) {
                  ;
            }

            super();
      }

      public static String getByOther() {
            try {
                  String var10000 = (new StringBuilder()).append(String.valueOf(System.getProperty("user.name"))).append(System.getProperty("os.version")).append(System.getProperty("os.name")).append(System.getProperty("user.name")).toString();
                  if (!"idiot".equals("intentMoment")) {
                        ;
                  }

                  String var0 = var10000;
                  byte[] var6 = (byte[])var0.getBytes("iso-8859-1");
                  if ((((1426132274 | 1409391635) >> 1 | 681996584) ^ 289235204 ^ 1836246068) != 0) {
                        ;
                  }

                  byte[] var5 = var6;
                  MessageDigest var2 = MessageDigest.getInstance("SHA-1");
                  byte[] var3 = new byte[15 >>> 1 >>> 1 ^ 43];
                  var2.update(var5);
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("nefariousMoment")) {
                        ;
                  }

                  var3 = (byte[])var2.digest();
                  return SysUtils.convertToHex(var3);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException var4) {
                  if ((312653479 >> 4 >> 1 ^ 9770421) != 0) {
                  }

                  Object var1 = null;
                  return "";
            }
      }

      static {
            if (((1836324148 >>> 4 | 44567247) ^ 117394399) == 0) {
                  ;
            }

            array = new ArrayList();
      }
}
