package ru.wendoxd.managers;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    private final List<String> friends = new ArrayList<>();
    private static final File friendFile = new File(System.getenv("appdata") + "\\.antiautistleak\\game\\friends.wex");

    public void init() throws Exception {
        if (!friendFile.exists()) {
            friendFile.createNewFile();
        } else {
            readFriends();
        }
    }

    public void addFriend(String name) {
        friends.add(name);
        updateFile();
    }

    public boolean isFriend(String name) {
        return friends.stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }

    public void removeFriend(String name) {
        friends.removeIf(friend -> friend.equalsIgnoreCase(name));
    }

    public void clearFriend() {
        friends.clear();
        updateFile();
    }

    public List<String> getFriends() {
        return friends;
    }

    public void updateFile() {
        try {
            StringBuilder builder = new StringBuilder();
            friends.forEach(friend -> builder.append(friend).append("\n"));
            Files.write(friendFile.toPath(), builder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFriends() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(friendFile.getAbsolutePath()))));
            String line;
            while ((line = reader.readLine()) != null) {
                friends.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
