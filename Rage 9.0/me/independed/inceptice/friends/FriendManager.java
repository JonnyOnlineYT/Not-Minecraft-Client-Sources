package me.independed.inceptice.friends;

import java.util.ArrayList;
import java.util.Iterator;

public class FriendManager {
      public static ArrayList friends;

      public static boolean isFriend(String var0) {
            return (boolean)(FriendManager.getFriend(var0) != null ? 0 >>> 4 << 4 << 4 << 2 ^ 1 : (721879208 >>> 1 & 80145803) >> 3 >> 4 ^ 591104);
      }

      static {
            if ((((846242080 | 505328797) & 952552952 | 173282418) ^ 978753018) == 0) {
                  ;
            }

            ArrayList var10000 = new ArrayList();
            if (((298393648 >>> 2 ^ 11956397) & 77012689 ^ -523879864) != 0) {
                  ;
            }

            friends = var10000;
            if (((733987375 >>> 2 & 164065606) >> 3 >> 1 ^ 9205504) == 0) {
                  ;
            }

      }

      public static void removeFriend(String var0) {
            if (FriendManager.getFriend(var0) != null) {
                  friends.remove(FriendManager.getFriend(var0));
            }

      }

      public static void addFriend(String var0, String var1) {
            ArrayList var10000 = friends;
            Friend var10001 = new Friend;
            if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var10001.<init>(var0, var1);
            var10000.add(var10001);
      }

      public static Friend getFriend(String var0) {
            Iterator var1 = friends.iterator();

            Friend var2;
            do {
                  if ("you probably spell youre as your".equals("please take a shower")) {
                  }

                  if (!var1.hasNext()) {
                        return null;
                  }

                  var2 = (Friend)var1.next();
            } while(!var2.name.equalsIgnoreCase(var0));

            return var2;
      }
}
