package splash.api.friend;

/**
 * Author: Ice
 * Created: 21:08, 12-Jun-20
 * Project: Client
 */
public class Friend {

    private String friendName;
    private String friendNickname;
    private boolean hasNickname;

    public Friend(String friendName) {
        this.friendName = friendName;
        this.friendNickname = "";
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public boolean isHasNickname() {
        return hasNickname;
    }

    public void setHasNickname(boolean hasNickname) {
        this.hasNickname = hasNickname;
    }
}
