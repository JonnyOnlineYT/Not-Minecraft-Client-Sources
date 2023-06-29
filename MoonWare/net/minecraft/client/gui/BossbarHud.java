package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.Namespaced;
import net.minecraft.world.BossInfo;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.NoRender;

public class BossbarHud extends Gui
{
    private static final Namespaced GUI_BARS_TEXTURES = new Namespaced("textures/gui/bars.png");
    private final Minecraft client;
    private final Map<UUID, BossInfoClient> mapBossInfos = Maps.newLinkedHashMap();

    public BossbarHud(Minecraft clientIn)
    {
        client = clientIn;
    }

    public void draw()
    {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.bossBar.getBoolValue()) {
            return;
        }
        if (!mapBossInfos.isEmpty())
        {
            ScaledResolution scaledresolution = new ScaledResolution(client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossinfoclient : mapBossInfos.values())
            {
                int k = i / 2 - 91;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                render(k, j, bossinfoclient);
                String s = bossinfoclient.getName().asFormattedString();
                Minecraft.font.drawStringWithShadow(s, (float)(i / 2 - Minecraft.font.getStringWidth(s) / 2), (float)(j - 9), 16777215);
                j += 10 + Minecraft.font.height;

                if (j >= scaledresolution.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }

    private void render(int x, int y, BossInfo info)
    {
        drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);

        if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
        {
            drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }

        int i = (int)(info.getPercent() * 183.0F);

        if (i > 0)
        {
            drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);

            if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
            {
                drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }
    }

    public void read(SPacketUpdateBossInfo packetIn)
    {
        if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.ADD)
        {
            mapBossInfos.put(packetIn.getUniqueId(), new BossInfoClient(packetIn));
        }
        else if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.REMOVE)
        {
            mapBossInfos.remove(packetIn.getUniqueId());
        }
        else
        {
            mapBossInfos.get(packetIn.getUniqueId()).updateFromPacket(packetIn);
        }
    }

    public void cleanup()
    {
        mapBossInfos.clear();
    }

    public boolean shouldPlayEndBossMusic()
    {
        if (!mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : mapBossInfos.values())
            {
                if (bossinfo.shouldPlayEndBossMusic())
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean shouldDarkenSky()
    {
        if (!mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : mapBossInfos.values())
            {
                if (bossinfo.shouldDarkenSky())
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean shouldCreateFog()
    {
        if (!mapBossInfos.isEmpty())
        {
            for (BossInfo bossinfo : mapBossInfos.values())
            {
                if (bossinfo.shouldCreateFog())
                {
                    return true;
                }
            }
        }

        return false;
    }
}
