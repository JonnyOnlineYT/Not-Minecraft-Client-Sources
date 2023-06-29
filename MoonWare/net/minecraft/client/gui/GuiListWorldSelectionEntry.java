package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final Namespaced ICON_MISSING = new Namespaced("textures/misc/unknown_server.png");
    private static final Namespaced ICON_OVERLAY_LOCATION = new Namespaced("textures/gui/world_selection.png");
    private final Minecraft client;
    private final GuiWorldSelection worldSelScreen;
    private final WorldSummary worldSummary;
    private final Namespaced iconLocation;
    private final GuiListWorldSelection containingListSel;
    private File iconFile;
    private DynamicTexture icon;
    private long lastClickTime;

    public GuiListWorldSelectionEntry(GuiListWorldSelection listWorldSelIn, WorldSummary p_i46591_2_, ISaveFormat p_i46591_3_)
    {
        containingListSel = listWorldSelIn;
        worldSelScreen = listWorldSelIn.getGuiWorldSelection();
        worldSummary = p_i46591_2_;
        client = Minecraft.getMinecraft();
        iconLocation = new Namespaced("worlds/" + p_i46591_2_.getFileName() + "/icon");
        iconFile = p_i46591_3_.getFile(p_i46591_2_.getFileName(), "icon.png");

        if (!iconFile.isFile())
        {
            iconFile = null;
        }

        loadServerIcon();
    }

    public void draw(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        String s = worldSummary.getDisplayName();
        String s1 = worldSummary.getFileName() + " (" + DATE_FORMAT.format(new Date(worldSummary.getLastTimePlayed())) + ")";
        String s2 = "";

        if (Strings.isNullOrEmpty(s))
        {
            s = I18n.format("selectWorld.world") + " " + (p_192634_1_ + 1);
        }

        if (worldSummary.requiresConversion())
        {
            s2 = I18n.format("selectWorld.conversion") + " " + s2;
        }
        else
        {
            s2 = I18n.format("gameMode." + worldSummary.getEnumGameType().getName());

            if (worldSummary.isHardcoreModeEnabled())
            {
                s2 = Formatting.DARK_RED + I18n.format("gameMode.hardcore") + Formatting.RESET;
            }

            if (worldSummary.getCheatsEnabled())
            {
                s2 = s2 + ", " + I18n.format("selectWorld.cheats");
            }

            String s3 = worldSummary.getVersionName();

            if (worldSummary.markVersionInList())
            {
                if (worldSummary.askToOpenWorld())
                {
                    s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + Formatting.RED + s3 + Formatting.RESET;
                }
                else
                {
                    s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + Formatting.ITALIC + s3 + Formatting.RESET;
                }
            }
            else
            {
                s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + s3;
            }
        }

        Minecraft.font.drawString(s, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        Minecraft.font.drawString(s1, p_192634_2_ + 32 + 3, p_192634_3_ + Minecraft.font.height + 3, 8421504);
        Minecraft.font.drawString(s2, p_192634_2_ + 32 + 3, p_192634_3_ + Minecraft.font.height + Minecraft.font.height + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(icon != null ? iconLocation : ICON_MISSING);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GlStateManager.disableBlend();

        if (p_192634_8_)
        {
            Minecraft.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
            Gui.drawRect(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = p_192634_6_ - p_192634_2_;
            int i = j < 32 ? 32 : 0;

            if (worldSummary.markVersionInList())
            {
                Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 32.0F, (float)i, 32, 32, 256.0F, 256.0F);

                if (worldSummary.askToOpenWorld())
                {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 96.0F, (float)i, 32, 32, 256.0F, 256.0F);

                    if (j < 32)
                    {
                        worldSelScreen.setVersionTooltip(Formatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion1") + "\n" + Formatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion2"));
                    }
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 64.0F, (float)i, 32, 32, 256.0F, 256.0F);

                    if (j < 32)
                    {
                        worldSelScreen.setVersionTooltip(Formatting.GOLD + I18n.format("selectWorld.tooltip.snapshot1") + "\n" + Formatting.GOLD + I18n.format("selectWorld.tooltip.snapshot2"));
                    }
                }
            }
            else
            {
                Gui.drawModalRectWithCustomSizedTexture(p_192634_2_, p_192634_3_, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
            }
        }
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
     * clicked and the list should not be dragged.
     */
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
    {
        containingListSel.selectWorld(slotIndex);

        if (relativeX <= 32 && relativeX < 32)
        {
            joinWorld();
            return true;
        }
        else if (Minecraft.getSystemTime() - lastClickTime < 250L)
        {
            joinWorld();
            return true;
        }
        else
        {
            lastClickTime = Minecraft.getSystemTime();
            return false;
        }
    }

    public void joinWorld()
    {
        if (worldSummary.askToOpenWorld())
        {
            Minecraft.openScreen(new ConfirmScreen(b -> {
                if (b)
                {
                    loadWorld();
                }
                else
                {
                    Minecraft.openScreen(worldSelScreen);
                }
            }, I18n.format("selectWorld.versionQuestion"), I18n.format("selectWorld.versionWarning", worldSummary.getVersionName()), I18n.format("selectWorld.versionJoinButton"), I18n.format("gui.cancel")));
        }
        else
        {
            loadWorld();
        }
    }

    public void deleteWorld()
    {
        Minecraft.openScreen(new ConfirmScreen(b -> {
            if (b)
            {
                Minecraft.openScreen(new WorkingScreen());
                ISaveFormat isaveformat = client.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(worldSummary.getFileName());
                containingListSel.refreshList();
            }

            Minecraft.openScreen(worldSelScreen);
        }, I18n.format("selectWorld.deleteQuestion"), "'" + worldSummary.getDisplayName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel")));
    }

    public void editWorld()
    {
        Minecraft.openScreen(new GuiWorldEdit(worldSelScreen, worldSummary.getFileName()));
    }

    public void recreateWorld()
    {
        Minecraft.openScreen(new WorkingScreen());
        GuiCreateWorld guicreateworld = new GuiCreateWorld(worldSelScreen);
        ISaveHandler isavehandler = client.getSaveLoader().getSaveLoader(worldSummary.getFileName(), false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        isavehandler.flush();

        if (worldinfo != null)
        {
            guicreateworld.recreateFromExistingWorld(worldinfo);
            Minecraft.openScreen(guicreateworld);
        }
    }

    private void loadWorld()
    {
        Minecraft.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (client.getSaveLoader().canLoadWorld(worldSummary.getFileName()))
        {
            client.launchIntegratedServer(worldSummary.getFileName(), worldSummary.getDisplayName(), null);
        }
    }

    private void loadServerIcon()
    {
        boolean flag = iconFile != null && iconFile.isFile();

        if (flag)
        {
            BufferedImage bufferedimage;

            try
            {
                bufferedimage = ImageIO.read(iconFile);
                Preconditions.checkState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Preconditions.checkState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Invalid icon for world {}", worldSummary.getFileName(), throwable);
                iconFile = null;
                return;
            }

            if (icon == null)
            {
                icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                Minecraft.getTextureManager().loadTexture(iconLocation, icon);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), icon.getTextureData(), 0, bufferedimage.getWidth());
            icon.updateDynamicTexture();
        }
        else if (!flag)
        {
            Minecraft.getTextureManager().deleteTexture(iconLocation);
            icon = null;
        }
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }
}
