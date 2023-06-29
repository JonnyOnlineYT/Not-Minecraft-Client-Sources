/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package ru.fluger.client.settings.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
    public JsonObject save();

    public void load(JsonObject var1);
}

