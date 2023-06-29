package org.moonware.client.files.impl;

import net.minecraft.block.Block;
import org.moonware.client.files.FileManager;
import org.moonware.client.cmd.impl.XrayCommand;

import java.io.*;

public class XrayConfig extends FileManager.CustomFile {

    public XrayConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String id = curLine.split(":")[1];
                XrayCommand.blockIDS.add(new Integer(id));
            }
            br.close();
        } catch (Exception ignored) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
            for (Integer integer : XrayCommand.blockIDS) {
                if (integer != null) {
                    out.write("blockID" + ":" + integer + ":" + Block.getBlockById(integer).getLocalizedName());
                    out.write("\r\n");
                }
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
