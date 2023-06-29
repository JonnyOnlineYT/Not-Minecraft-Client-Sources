/*
 * Decompiled with CFR 0.150.
 */
package ru.squad47;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Client {
    private Socket socket;
    public static int data = -89123894;
    public DataOutputStream out;
    public DataInputStream in;

    public void connect(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        }
        catch (Exception ex) {
            Minecraft.getMinecraft().shutdown();
        }
    }

    public void writeUTF(String s) {
        try {
            this.out.writeUTF(s);
        }
        catch (Exception ex) {
            Minecraft.getMinecraft().shutdown();
        }
    }

    public String readUTF() {
        try {
            return this.in.readUTF();
        }
        catch (Exception ex) {
            Minecraft.getMinecraft().shutdown();
            return "-999";
        }
    }

    public static GuiScreen getScreen(String s) {
        try {
            return (GuiScreen)Class.forName(s).newInstance();
        }
        catch (Exception ex) {
            Minecraft.getMinecraft().shutdown();
            return null;
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}

