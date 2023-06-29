package me.independed.inceptice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWID {
      private static String runCommand(String var0) throws IOException {
            StringBuilder var10000 = new StringBuilder;
            if ((341835777 << 2 >> 1 >> 3 << 1 ^ -567413474) != 0) {
                  ;
            }

            var10000.<init>();
            StringBuilder var1 = var10000;
            Process var2 = Runtime.getRuntime().exec(var0);

            BufferedReader var3;
            String var4;
            for(var3 = new BufferedReader(new InputStreamReader(var2.getInputStream())); (var4 = var3.readLine()) != null; var1.append(var4)) {
                  if (((70750477 | 9464549) ^ 1184677308) != 0) {
                        ;
                  }
            }

            var3.close();
            String var5 = var1.toString();
            if (((1437328628 | 200763349) ^ 470417562 ^ 1140189039) == 0) {
                  ;
            }

            if (!var5.equalsIgnoreCase(" ") && !var1.toString().isEmpty()) {
                  return var1.toString();
            } else {
                  throw new IOException();
            }
      }

      public static String getHWID() throws IOException, NoSuchAlgorithmException {
            String var0 = "";
            if (OSUtil.getCurrentOS().equals(OS.WINDOWS)) {
                  var0 = HWID.runCommand("wmic baseboard get serialNumber");
            } else {
                  String var1;
                  if (OSUtil.getCurrentOS().equals(OS.MAC)) {
                        var1 = HWID.runCommand("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'");
                        if (!"please go outside".equals("idiot")) {
                              ;
                        }

                        int var10001 = var1.indexOf("Hardware UUID: ");
                        if (((12582944 >>> 2 | 1490960) << 2 ^ 14352480) == 0) {
                              ;
                        }

                        var0 = var1.substring(var10001 + (((8 ^ 7) & 9 | 1) << 3 ^ 71));
                  } else if (OSUtil.getCurrentOS().equals(OS.UNIX)) {
                        var1 = HWID.runCommand("lshw -c cpu");
                        var1 = var1.substring(var1.indexOf("product: ") + ((8 & 7 ^ 1633589772 | 77102622) ^ 844857304 ^ 1468367311), var1.indexOf("vendor: ") - ((4 >> 1 | 0) >> 2 << 3 ^ 7));
                        String var2 = HWID.runCommand("lshw -c display");
                        var2 = var2.substring(var2.indexOf("product: ") + (((0 & 1345893363) >> 2 ^ 1630753813 | 1560602129) ^ 2100819484), var2.indexOf("vendor: ") - ((5 & 1 & 0 | 1105744527) << 3 ^ 256021631));
                        String var3 = HWID.runCommand("lshw -c network");
                        var3 = var3.substring(var3.indexOf("serial: ") + ((5 >>> 4 & 455879876 | 1881895083) ^ 1881895075), var3.indexOf("width: ") - ((2 & 0) >> 3 << 4 ^ 1261972879 ^ 1261972872));
                        StringBuilder var10000 = new StringBuilder;
                        if (!"yo mama name maurice".equals("nefariousMoment")) {
                              ;
                        }

                        var10000.<init>();
                        var10000 = var10000.append(var1).append(" ").append(var2);
                        if ((1853822063 << 3 >> 4 << 4 ^ -1026606448) != 0) {
                              ;
                        }

                        var10000 = var10000.append(" ");
                        if ((260 >>> 3 ^ 32) == 0) {
                              ;
                        }

                        var0 = var10000.append(var3).toString();
                  }
            }

            boolean var11 = var0.isEmpty();
            if (!"intentMoment".equals("ape covered in human flesh")) {
                  ;
            }

            if (!var11 && !var0.equals(" ")) {
                  MessageDigest var8 = MessageDigest.getInstance("MD5");
                  var8.update((byte[])var0.getBytes());
                  if ((1143640700 << 4 >>> 2 ^ 253366441 ^ 274001587 ^ -1662724498) != 0) {
                        ;
                  }

                  byte[] var9 = (byte[])var8.digest();
                  StringBuilder var10 = new StringBuilder();
                  byte[] var4 = var9;
                  int var5 = var9.length;
                  int var6 = 1483212538 >> 1 << 3 >> 4 >>> 3 ^ 12795959;
                  if (!"nefariousMoment".equals("nefariousMoment")) {
                        ;
                  }

                  while(var6 < var5) {
                        byte var7 = var4[var6];
                        Object[] var10002 = new Object[((0 ^ 1627962624) << 2 & 519105) << 4 ^ 3014657];
                        var10002[1873806993 << 3 >>> 1 ^ 1031033024 ^ 62147204] = Integer.valueOf(var7 & ((27 & 16 | 3) & 4 ^ 255));
                        var10.append(String.format("%02x", var10002));
                        ++var6;
                  }

                  return var10.toString();
            } else {
                  throw new NullPointerException();
            }
      }
}
