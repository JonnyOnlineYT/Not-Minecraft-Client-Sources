package me.independed.inceptice.command.impl;

import java.util.Iterator;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.util.ChatUtil;

public class Friend extends Command {
      public Friend() {
            String[] var10004 = new String[((0 | 1955600448) >> 4 ^ 90382955) >>> 3 & 2958245 ^ 327719];
            int var10006 = (136633727 >>> 1 | 59606434 | 64361297) >> 4 ^ 8257535;
            if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                  ;
            }

            var10004[var10006] = "f";
            var10004[0 << 1 << 4 >> 1 ^ 1] = "friends";
            super("Friend", "add players to your friends list", "friend <add> <name>", var10004);
      }

      public void onCommand(String[] var1, String var2) {
            if (!"please go outside".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (var1.length >= ((0 | 1955553856) ^ 1442017667 ^ 92881952 ^ 620058594)) {
                  String var8;
                  if (var1.length == ((1 | 0 | 0) & 0 ^ 2) && (var1[362810399 >> 3 & 19925365 ^ 1043730 ^ 4189203].equals("add") || var1[976030307 >>> 1 << 4 ^ -781692144].equals("remove") || var1[(543376538 >> 3 << 1 | 132466568) ^ 268294062].equals("delete"))) {
                        String var3 = var1[(1326387899 ^ 1066689923) >> 2 ^ 472309198];
                        int var10001 = (0 << 4 ^ 1332373977) & 321340984 ^ 52576281;
                        if (!"idiot".equals("minecraft")) {
                              ;
                        }

                        String var4 = var1[var10001];
                        StringBuilder var7;
                        if (var3.equals("add")) {
                              if ((540672 ^ 1721826836) != 0) {
                                    ;
                              }

                              FriendManager.addFriend(var4, var4);
                              var7 = (new StringBuilder()).append(var4);
                              if ((1106880568 >>> 1 >> 2 >>> 4 & 5367393 ^ 1960854537) != 0) {
                                    ;
                              }

                              var8 = var7.append(" added to your friend list.").toString();
                              if (!"please dont crack my plugin".equals("minecraft")) {
                                    ;
                              }

                              ChatUtil.sendChatMessage(var8);
                        } else {
                              boolean var10000 = var3.equals("remove");
                              if (((561661070 | 421831162) << 2 >>> 3 ^ 1587903189) != 0) {
                                    ;
                              }

                              if (var10000 || var3.equals("delete")) {
                                    FriendManager.removeFriend(var4);
                                    var7 = (new StringBuilder()).append(var4);
                                    if ((882078950 << 4 << 2 ^ 296448313 ^ 896944313) == 0) {
                                          ;
                                    }

                                    ChatUtil.sendChatMessage(var7.append(" removed from your friend list.").toString());
                              }
                        }
                  }

                  if (var1.length == ((0 >> 1 ^ 882802602) >>> 3 ^ 110350324)) {
                        var8 = var1[(2133172421 ^ 155346325) >>> 3 ^ 248314026];
                        if ((51070393 >> 4 >> 2 ^ -1244119287) != 0) {
                              ;
                        }

                        if (var8.equals("list")) {
                              if ((357101062 >>> 3 >> 2 >> 1 ^ 2201085 ^ -1414649583) != 0) {
                                    ;
                              }

                              if (((59048458 >> 4 & 3355947) >> 3 ^ -762554997) != 0) {
                                    ;
                              }

                              ChatUtil.sendChatMessage("Your Friend List:");
                              Iterator var5 = FriendManager.friends.iterator();

                              while(var5.hasNext()) {
                                    me.independed.inceptice.friends.Friend var6 = (me.independed.inceptice.friends.Friend)var5.next();
                                    ChatUtil.sendChatMessage(var6.name);
                              }
                        }

                        if (var1[(2129353388 ^ 945194193 ^ 386040477) >> 2 ^ 342873912].equals("clear")) {
                              FriendManager.friends.clear();
                              ChatUtil.sendChatMessage("Your Friend List Was Cleared");
                        }
                  }
            }

      }
}
