package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;

public class PlayerMenuObject implements ISpectatorMenuObject
{
    private final GameProfile profile;
    private final Namespaced namespaced;

    public PlayerMenuObject(GameProfile profileIn)
    {
        profile = profileIn;
        namespaced = AbstractClientPlayer.getLocationSkin(profileIn.getName());
        AbstractClientPlayer.getDownloadImageSkin(namespaced, profileIn.getName());
    }

    public void selectItem(SpectatorMenu menu)
    {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketSpectate(profile.getId()));
    }

    public Component getSpectatorName()
    {
        return new TextComponent(profile.getName());
    }

    public void renderIcon(float p_178663_1_, int alpha)
    {
        Minecraft.getTextureManager().bindTexture(namespaced);
        GlStateManager.color(1.0F, 1.0F, 1.0F, (float)alpha / 255.0F);
        Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
    }

    public boolean isEnabled()
    {
        return true;
    }
}
