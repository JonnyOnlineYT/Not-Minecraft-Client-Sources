package me.wintware.client.friendsystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FriendManager {
   private final List<Friend> friends = new ArrayList();

   public void addFriend(Friend paramFriend) {
      this.friends.add(paramFriend);
   }

   public boolean isFriend(String paramString) {
      return this.friends.stream().anyMatch((paramFriend) -> {
         return paramFriend.getName().equals(paramString);
      });
   }

   public void removeFriend(String name) {
      Iterator var2 = this.friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(name)) {
            this.friends.remove(friend);
            break;
         }
      }

   }

   public List<Friend> getFriends() {
      return this.friends;
   }

   public Friend getFriend(String friend) {
      return (Friend)this.friends.stream().filter((paramFriend) -> {
         return paramFriend.getName().equals(friend);
      }).findFirst().get();
   }
}
