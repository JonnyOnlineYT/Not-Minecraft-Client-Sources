package me.independed.inceptice.command.comands;

import me.independed.inceptice.command.Command;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.util.ChatUtil;

public class Friend extends Command {
      public Friend() {
            if (((1046002582 ^ 726811363) >>> 1 << 2 ^ -884682536) != 0) {
                  ;
            }

            String[] var10004 = new String[((0 | 1266368882) << 2 << 4 << 2 | 2064548437) ^ 2068836948];
            var10004[(135024697 >>> 1 ^ 64659741) >> 4 << 1 ^ 16486688] = "f";
            super("Friend", "add players to your friends list", "friend <add> <name>", var10004);
      }

      public void onCommand(String[] var1, String var2) {
            int var10000 = var1.length;
            int var10001 = (0 << 1 | 2016211042) & 940294471 ^ 676408368 ^ 52426662 ^ 323361750;
            if (((685382907 >> 3 ^ 61708041) & 48281691 ^ 781927732) != 0) {
                  ;
            }

            if (var10000 == var10001) {
                  if ((((156233786 | 22670154) ^ 64054375) & 21172765 ^ -1091687147) != 0) {
                        ;
                  }

                  var10001 = 458126328 >>> 3 >>> 4 ^ 2421461 ^ 1207858;
                  if ((679074677 << 4 & 284490482 ^ 9349532 ^ 1711052) == 0) {
                        ;
                  }

                  String var5;
                  if (var1[var10001] != "add" && var1[((401400106 ^ 155488370) & 346287595) >>> 3 >> 1 ^ 21628436] != "remove") {
                        if ((1484980497 >>> 4 & 55874604 ^ 16781312) == 0) {
                              ;
                        }

                        var5 = var1[945876024 >>> 2 >>> 2 >> 4 ^ 3694828];
                        if (((33883272 | 29362118) ^ -637889727) != 0) {
                              ;
                        }

                        if (var5 != "delete") {
                              return;
                        }
                  }

                  String var3 = var1[1543570157 << 2 >>> 3 ^ 42957887 ^ 210763081];
                  if ((1870135716 >>> 2 >> 3 >> 2 >>> 3 ^ -1132763511) != 0) {
                        ;
                  }

                  var5 = var1[(0 ^ 787684784) & 148626999 ^ 148053041];
                  if ((((1638420359 ^ 272267878) & 1491260107) >> 2 & 9309959 ^ 1024) == 0) {
                        ;
                  }

                  String var4 = var5;
                  if (var3 == "add") {
                        if (((59074615 ^ 2418528 | 2198328 | 38347059) ^ 120639234) != 0) {
                              ;
                        }

                        FriendManager.addFriend(var4, var4);
                        ChatUtil.sendChatMessage((new StringBuilder()).append(var4).append(" added to your friend list.").toString());
                  } else {
                        if (((132254782 << 4 | 36515387) & 951404887 ^ 134451277) != 0) {
                              ;
                        }

                        if (var3 == "remove" || var3 == "delete") {
                              FriendManager.removeFriend(var4);
                              StringBuilder var6 = new StringBuilder;
                              if (!"intentMoment".equals("please go outside")) {
                                    ;
                              }

                              if ((1291337009 >>> 1 >> 3 >>> 3 ^ 10088570) == 0) {
                                    ;
                              }

                              var6.<init>();
                              if (((85777185 >> 3 | 7467748) ^ 1323439541) != 0) {
                                    ;
                              }

                              ChatUtil.sendChatMessage(var6.append(var4).append(" removed from your friend list.").toString());
                        }
                  }
            }

      }
}
