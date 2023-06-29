/*
 * Decompiled with CFR 0.150.
 */
package ru.squad47;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class UserData
extends AbstractTexture {
    private static UserData instance;
    private String name;
    private String licenseDate;
    private String id;
    public String vk = "";
    public ResourceLocation resourceLocation;
    public byte[] buffer;

    public String getName() {
        return this.name;
    }

    public String getLicenseDate() {
        return this.licenseDate;
    }

    public String getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
    }

    public void init() {
        this.resourceLocation = new ResourceLocation(String.valueOf(System.nanoTime()));
        Minecraft.getMinecraft().getTextureManager().loadTexture(this.resourceLocation, this);
    }

    public void setLicenseDate(String date) {
        this.licenseDate = date;
    }

    public void setID(String id) {
        this.id = id;
    }

    public static UserData instance() {
        return instance == null ? (instance = new UserData()) : instance;
    }
}

