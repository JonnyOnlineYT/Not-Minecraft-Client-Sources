/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package ru.fluger.client.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.macro.Macro;
import ru.fluger.client.files.FileManager;

public class MacroConfig
extends FileManager.CustomFile {
    public MacroConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                if (Fluger.instance.macroManager == null) continue;
                Fluger.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex((String)bind), value));
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
            for (Macro m : Fluger.instance.macroManager.getMacros()) {
                if (m == null) continue;
                out.write(Keyboard.getKeyName((int)m.getKey()) + ":" + m.getValue());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

