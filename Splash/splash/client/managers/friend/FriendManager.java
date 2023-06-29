package splash.client.managers.friend;

import splash.api.friend.Friend;
import splash.api.manager.ClientManager;

/**
 * Author: Ice
 * Created: 21:09, 12-Jun-20
 * Project: Client
 */
public class FriendManager extends ClientManager<Friend> {


    @Override
    public String managerName() {
        return "FriendManager";
    }

    public boolean isFriend(String name) {
        for(Friend friend : getContents()) {
            if(friend.getFriendName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Friend getFriendByName(String friendName) {
        for(Friend friend : getContents()) {
            if(friend.getFriendName().equalsIgnoreCase(friendName)) {
                return friend;
            }
        }
        return null;
    }
}
