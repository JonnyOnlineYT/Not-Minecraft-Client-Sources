package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.GameType;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject
{
    private static final Ordering<NetworkPlayerInfo> PROFILE_ORDER = Ordering.from(new Comparator<NetworkPlayerInfo>()
    {
        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_)
        {
            return ComparisonChain.start().compare(p_compare_1_.getGameProfile().getId(), p_compare_2_.getGameProfile().getId()).result();
        }
    });
    private final List<ISpectatorMenuObject> items;

    public TeleportToPlayer()
    {
        this(PROFILE_ORDER.sortedCopy(Minecraft.getMinecraft().getConnection().getPlayerInfoMap()));
    }

    public TeleportToPlayer(Collection<NetworkPlayerInfo> p_i45493_1_)
    {
        items = Lists.newArrayList();

        for (NetworkPlayerInfo networkplayerinfo : PROFILE_ORDER.sortedCopy(p_i45493_1_))
        {
            if (networkplayerinfo.getGameType() != GameType.SPECTATOR)
            {
                items.add(new PlayerMenuObject(networkplayerinfo.getGameProfile()));
            }
        }
    }

    public List<ISpectatorMenuObject> getItems()
    {
        return items;
    }

    public Component getPrompt()
    {
        return new TranslatableComponent("spectatorMenu.teleport.prompt");
    }

    public void selectItem(SpectatorMenu menu)
    {
        menu.selectCategory(this);
    }

    public Component getSpectatorName()
    {
        return new TranslatableComponent("spectatorMenu.teleport");
    }

    public void renderIcon(float p_178663_1_, int alpha)
    {
        Minecraft.getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, 16, 16, 256.0F, 256.0F);
    }

    public boolean isEnabled()
    {
        return !items.isEmpty();
    }
}
