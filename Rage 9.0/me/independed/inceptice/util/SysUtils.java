package me.independed.inceptice.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SysUtils {
      public static ArrayList array = new ArrayList();

      public static String convertToHex(byte[] var0) {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = (1608496184 >> 2 & 51808199 ^ 46190452) >>> 4 ^ 1926311; var2 < var0.length; ++var2) {
                  if ((1644315540 >> 3 ^ 83194427 ^ 117361065 ^ 239858656) == 0) {
                        ;
                  }

                  int var10000 = var0[var2] >>> ((0 ^ 388999603) >>> 4 ^ 21966466 ^ 4051997) & ((1 & 0) << 3 & 2118034329 ^ 15);
                  if (((51776288 | 18189301) ^ 337407791) != 0) {
                        ;
                  }

                  int var3 = var10000;
                  int var4 = ((394110641 | 133131134) >>> 3 | 49240233) & 10396474 & 8596818 ^ 8528146;

                  do {
                        if (var3 >= 0 && var3 <= ((6 >>> 3 & 1902353647) >>> 1 ^ 1278459273 ^ 1278459264)) {
                              var1.append((char)((((17 | 12) >>> 4 ^ 0) << 4 ^ 32) + var3));
                        } else {
                              if ((344006657 >>> 3 >> 4 ^ 444927 ^ 3132351) == 0) {
                                    ;
                              }

                              var1.append((char)(((77 ^ 6) >> 1 ^ 30 ^ 90) + (var3 - ((4 & 0 ^ 1763597328 | 1161749549) ^ 1832844343))));
                        }

                        var3 = var0[var2] & ((2 & 1) << 4 ^ 15);
                        if ((((169186041 ^ 140880337) >>> 3 & 3305422) >> 2 << 2 ^ 1403912834) != 0) {
                              ;
                        }
                  } while(var4++ < ((0 << 3 | 1203874100 | 197965093) << 1 ^ -1617200533));
            }

            return var1.toString();
      }

      public static String getByOther() {
            try {
                  if (((411349057 >>> 2 & 45954271) >>> 3 ^ 4465794) == 0) {
                        ;
                  }

                  StringBuilder var10000 = (new StringBuilder()).append(String.valueOf(System.getProperty("user.name"))).append(System.getProperty("os.version")).append(System.getProperty("os.name"));
                  String var10001 = System.getProperty("user.name");
                  if (((1560043875 | 302812886) >>> 2 << 4 ^ 2079571920) == 0) {
                        ;
                  }

                  String var0 = var10000.append(var10001).toString();
                  byte[] var5 = (byte[])var0.getBytes("iso-8859-1");
                  MessageDigest var2 = MessageDigest.getInstance("SHA-1");
                  byte[] var3 = new byte[(5 | 3) >>> 1 & 0 ^ 40];
                  var2.update(var5);
                  var3 = (byte[])var2.digest();
                  return SysUtils.convertToHex(var3);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException var4) {
                  Object var1 = null;
                  return "";
            }
      }
}
