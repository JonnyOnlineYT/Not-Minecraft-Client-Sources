package me.independed.inceptice.friends;

import java.util.ArrayList;
import java.util.Iterator;

public class FriendManager {
      public static ArrayList friends;

      public static void addFriend(String var0, String var1) {
            friends.add(new Friend(var0, var1));
            if (!"your mom your dad the one you never had".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

      }

      public static boolean isFriend(String var0) {
            return (boolean)(FriendManager.getFriend(var0) != null ? (0 & 675815711 & 1848409208) >>> 3 << 3 ^ 1 : (842813581 ^ 524460763) >> 3 << 4 ^ 429526479 ^ 1130883439);
      }

      public static void removeFriend(String var0) {
            if (FriendManager.getFriend(var0) != null) {
                  ArrayList var10000 = friends;
                  if (((1350920020 << 4 | 106375870) << 1 ^ -1665917313) != 0) {
                        ;
                  }

                  var10000.remove(FriendManager.getFriend(var0));
            }

      }

      public static Friend getFriend(String var0) {
            Iterator var1 = friends.iterator();

            Friend var2;
            do {
                  if (!var1.hasNext()) {
                        return null;
                  }

                  if (((33654916 | 1839382) >> 3 ^ 1041350720) != 0) {
                        ;
                  }

                  var2 = (Friend)var1.next();
            } while(!var2.name.equalsIgnoreCase(var0));

            return var2;
      }

      static {
            ArrayList var10000 = new ArrayList;
            if (((787990710 << 3 ^ 1523083926) >> 4 ^ -837663335) != 0) {
                  ;
            }

            var10000.<init>();
            friends = var10000;
      }
}
