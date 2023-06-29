package Celestial.ui.altmanager;

import Celestial.Smertnix;
import Celestial.module.impl.Util.NameProtect;
import Celestial.utils.math.MathematicHelper;
import Celestial.utils.render.ColorUtils;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.altmanager.alt.Alt;
import Celestial.ui.altmanager.alt.AltLoginThread;
import Celestial.ui.altmanager.alt.AltManager;
import Celestial.ui.altmanager.api.AltService;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiAltManager extends GuiScreen {
    public static final AltService altService = new AltService();
    public Alt selectedAlt = null;
    public String status;
    private GuiAltButton login;
    private GuiAltButton remove;
    private AltLoginThread loginThread;
    private float offset;
    private GuiTextField searchField;

    private ResourceLocation resourceLocation;

    public GuiAltManager() {
        status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                break;
            case 1:
                (loginThread = new AltLoginThread(selectedAlt)).start();
                break;
            case 2:
                if (loginThread != null) {
                    loginThread = null;
                }

                AltManager.registry.remove(selectedAlt);
                status =  "Removed.";

                selectedAlt = null;
                break;
            case 3:
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                String randomName = RandomStringUtils.randomAlphabetic(1).toUpperCase() + RandomStringUtils.randomAlphanumeric(MathematicHelper.intRandom(5,3)).toLowerCase() + RandomStringUtils.randomNumeric(4);
                (loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            case 6:
                mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            case 7:
                mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 8:
                /*try {
                    AltManager.registry.clear();
                    Main.instance.fileManager.getFile(Alts.class).loadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                status = "Refreshed!";
                break;
            case 8931:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4545:
                mc.displayGuiScreen(new GuiConnecting(this, mc, new ServerData(I18n.format("selectServer.defaultName"), "play.hypixel.net", false)));
                break;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtils.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        mc.neverlose900_30.drawCenteredStringWithShadow("Celestial", (float) (sr.getScaledWidth() / 2), (float) (sr.getScaledHeight() / 2.2), new Color(255, 255, 255, 30).getRGB());

        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                offset += 26.0;
                if (offset < 0.0) {
                    offset = 0.0F;
                }
            } else if (wheel > 0) {
                offset -= 26.0;
                if (offset < 0.0) {
                    offset = 0.0F;
                }
            }
        }
        String altName = "Name: " + (Smertnix.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getCurrentValue() ? "Protected" : mc.session.getUsername());
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        mc.mntsb_17.drawCenteredString(altName, (float) width / 2.0f, 10.0f, -1);
        GlStateManager.pushMatrix();
        RenderUtils.scissorRect(0.0f, 33.0f, width, height - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        Iterator<Alt> e = getAlts().iterator();
        while (true) {
            if (!e.hasNext()) {
                GL11.glDisable(3089);
                GL11.glPopMatrix();
                if (selectedAlt == null) {
                    login.enabled = false;
                    remove.enabled = false;
                } else {
                    login.enabled = true;
                    remove.enabled = true;
                }
                if (Keyboard.isKeyDown(200)) {
                    offset -= 26.0;
                } else if (Keyboard.isKeyDown(208)) {
                    offset += 26.0;
                }
                if (offset < 0.0) {
                    offset = 0.0F;
                }
                return;
            }
            RenderUtils.drawRect((float) width / 2.0f - 120, (double) y - 100, (float) width / 1.6, (double) y + 30, new Color(30, 30, 30, 10).getRGB());

            Alt alt = e.next();
            if (!isAltInArea(y)) continue;
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(mc.session.getUsername())) {
                name = "" + name;
            }
            String prefix = alt.getStatus().equals((Object) Alt.Status.Banned) ? "" : (alt.getStatus().equals((Object) Alt.Status.NotWorking) ? "" : "");
            name = prefix + name + "";
            String pass = alt.getPassword().equals("") ? "Not License" : "License";
            if (alt == selectedAlt) {
                GlStateManager.pushMatrix();
                boolean hovering = (float) par1 >= (float) width / 1.5f + 5.0f && (double) par1 <= (double) width / 1.5 + 26.0 && (double) par2 >= (double) y - offset - 4.0 && (double) par2 <= (double) y - offset + 20.0;
                GlStateManager.popMatrix();
                if (isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawRect((float) width / 2.0f - 120, (double) y - offset - 4.0, (float) width / 1.6f, (double) y - offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                } else if (isMouseOverAlt(par1, par2, (double) y - offset)) {
                    RenderUtils.drawRect((float) width / 2.0f - 120, (double) y - offset - 4.0, (float) width / 1.6f, (double) y - offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                } else {
                    RenderUtils.drawRect((float) width / 2.0f - 120, (double) y - offset - 4.0, (float) width / 1.6f, (double) y - offset + 30.0, new Color(50, 50, 50, 33).getRGB());
                }
            }
            String numberP = "" + number + " ";
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (resourceLocation == null) {
                resourceLocation = AbstractClientPlayer.getLocationSkin(name);
                getDownloadImageSkin(resourceLocation, name);
            } else {
                mc.getTextureManager().bindTexture(resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect((float) width / 2.0f - 110, (float) y - (float) offset, 8.0f, 8.0f, 8.0f, 8.0f, 25, 25, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            mc.rubik_16.drawString((Smertnix.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getCurrentValue() ? "Protected" : name), (float) width / 2.0f - 80, (float) ((double) y - offset + 5.0), -1);
            mc.mntsb_14.drawString((alt.getStatus().equals((Object) Alt.Status.NotWorking) ? "m" : "") + pass, (float) width / 2.0f - 80, (float) ((double) y - offset + 15.0), ColorUtils.getColor(110));
            y += 40;
        }
    }


    public void initGui() {
        searchField = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 + 116, height - 22, 72, 16);
        buttonList.add(login = new GuiAltButton(1, width / 2 - 122, height - 48, 60, 20, "Login"));
        buttonList.add(remove = new GuiAltButton(2, width / 2 + 4 + 77, height - 48, 40, 20, "Remove"));
        buttonList.add(new GuiAltButton(3, width / 2 + 4 - 60, height - 48, 65, 20, "Add Alt"));
        buttonList.add(new GuiAltButton(5, width / 2 + 15, height - 48, 60, 20, "Random"));
        buttonList.add(new GuiAltButton(7, width / 2 - 122, height - 24, 245, 20, "Back"));

        login.enabled = false;
        remove.enabled = false;
    }

    protected void keyTyped(char par1, int par2) {
        searchField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && searchField.isFocused()) {
            searchField.setFocused(!searchField.isFocused());
        }

        try {
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAltInArea(int y) {
        return y - offset <= height - 50;
    }

    private boolean isMouseOverAlt(double x, double y, double y1) {
        return x >= width / 2F - 125 && y >= y1 - 4 && x <= width / 1.5 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= width && y <= height - 50;
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        searchField.mouseClicked(par1, par2, par3);
        if (offset < 0) {
            offset = 0;
        }

        double y = 38 - offset;

        for (Iterator<Alt> e = getAlts().iterator(); e.hasNext(); y += 40) {
            Alt alt = e.next();
            if (isMouseOverAlt(par1, par2, y)) {
                if (alt == selectedAlt) {
                    actionPerformed(login);
                    return;
                }
                selectedAlt = alt;
            }
        }

        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Alt> getAlts() {
        List<Alt> altList = new ArrayList<>();
        Iterator iterator = AltManager.registry.iterator();

        while (true) {
            Alt alt;
            do {
                if (!iterator.hasNext()) {
                    return altList;
                }

                alt = (Alt) iterator.next();
            }
            while (!searchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(searchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(searchField.getText().toLowerCase()));

            altList.add(alt);
        }
    }
}
