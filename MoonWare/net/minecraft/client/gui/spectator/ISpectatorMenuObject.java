package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.Component;

public interface ISpectatorMenuObject
{
    void selectItem(SpectatorMenu menu);

    Component getSpectatorName();

    void renderIcon(float p_178663_1_, int alpha);

    boolean isEnabled();
}
