/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.fluger.client.Fluger;
import ru.fluger.client.files.FileManager;
import ru.fluger.client.ui.drag.Drag;

public class HudConfig
extends FileManager.CustomFile {
    public HudConfig(String name, boolean loadOnStart) {
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
                for (Drag d : Fluger.instance.dragmanager.drags) {
                    if (!d.name.equalsIgnoreCase(curLine.split(":")[0])) continue;
                    String x = curLine.split(":")[1];
                    String y = curLine.split(":")[2];
                    ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
                    int width = Fluger.scale.calc(rs.getScaledWidth());
                    int height = Fluger.scale.calc(rs.getScaledHeight());
                    d.x = Float.parseFloat(x);
                    d.y = Float.parseFloat(y);
                }
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
            for (Drag hudModule : Fluger.instance.dragmanager.drags) {
                out.write(hudModule.name + ":" + hudModule.x + ":" + hudModule.y);
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

