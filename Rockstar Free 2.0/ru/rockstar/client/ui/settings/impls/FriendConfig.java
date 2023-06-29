package ru.rockstar.client.ui.settings.impls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import ru.rockstar.Main;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.client.ui.settings.FileManager;

public class FriendConfig extends FileManager.CustomFile {

    public FriendConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }
    @Override
    public void loadFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                Main.instance.friendManager.addFriend(name);
            }
            br.close();
        } catch (Exception e) {

        }
    }

    @Override
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Friend friend : Main.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
