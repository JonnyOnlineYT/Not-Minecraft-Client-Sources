/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.misc;

import net.minecraft.client.multiplayer.ServerData;
import ru.fluger.client.helpers.Helper;
import ru.fluger.client.ui.font.MCFontRenderer;

public class ClientHelper
implements Helper {
    public static ServerData serverData;

    public static MCFontRenderer getFontRender() {
        return ClientHelper.mc.productsans;
    }
}

