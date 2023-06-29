package splash.api.user;

import splash.Splash;

public class User {

    private String userName, userHWID;
    private String userUID;

    public User(String userName, String userHWID, String userUID) {
        this.userName = userName;
        this.userHWID = userHWID;
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHWID() {
        return userHWID;
    }

    public void setUserHWID(String userHWID) {
        this.userHWID = userHWID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserTag() {
        return userName + "#" + userUID;
    }
}
