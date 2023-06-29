package org.moonware.client.files.impl;

import org.moonware.client.MoonWare;
import org.moonware.client.files.FileManager;
import org.moonware.client.macro.Macro;
import org.lwjgl.input.Keyboard;

import java.io.*;

public class MacroConfig
        extends FileManager.CustomFile {
    public MacroConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            FileInputStream fileInputStream = new FileInputStream(getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                if (MoonWare.macroManager == null) continue;
                MoonWare.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
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
            BufferedWriter out = new BufferedWriter(new FileWriter(getFile()));
            for (Macro m : MoonWare.macroManager.getMacros()) {
                if (m == null) continue;
                out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
