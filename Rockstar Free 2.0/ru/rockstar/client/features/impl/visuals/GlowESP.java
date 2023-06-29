package ru.rockstar.client.features.impl.visuals;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import net.minecraft.client.renderer.GLAllocation;
import ru.rockstar.api.utils.shader.FramebufferShader;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class GlowESP extends Feature {

    public GlowESP() {
        super("GlowESP","Глоу есп епта", 0,Category.VISUALS);
    }
}
