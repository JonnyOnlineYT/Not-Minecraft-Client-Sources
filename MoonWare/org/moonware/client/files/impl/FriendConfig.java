package org.moonware.client.files.impl;

import org.moonware.client.MoonWare;
import org.moonware.client.files.FileManager;
import org.moonware.client.friend.Friend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FriendConfig extends FileManager.CustomFile {

    public FriendConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                MoonWare.friendManager.addFriend(name);
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
            for (Friend friend : MoonWare.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        } catch (Exception e) {

        }
    }
}
