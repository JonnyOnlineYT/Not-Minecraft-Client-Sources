package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class NewFunc extends Feature {
    public NewFunc() {
        super("Ezz","",  Type.Visuals);
    }

    @Override
    public void onEnable() {
        Minecraft.openScreen(MoonWare.clickGui);
    }
}
