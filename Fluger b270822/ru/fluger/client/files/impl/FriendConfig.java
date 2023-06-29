/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import ru.fluger.client.Fluger;
import ru.fluger.client.files.FileManager;
import ru.fluger.client.friend.Friend;

public class FriendConfig
extends FileManager.CustomFile {
    public FriendConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                if (Fluger.instance.friendManager == null) continue;
                Fluger.instance.friendManager.addFriend(name);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Friend friend : Fluger.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

