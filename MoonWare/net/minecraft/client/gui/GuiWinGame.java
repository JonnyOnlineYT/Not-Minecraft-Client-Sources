package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class GuiWinGame extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Namespaced MINECRAFT_LOGO = new Namespaced("textures/gui/title/minecraft.png");
    private static final Namespaced field_194401_g = new Namespaced("textures/gui/title/edition.png");
    private static final Namespaced VIGNETTE_TEXTURE = new Namespaced("textures/misc/vignette.png");
    private final boolean field_193980_h;
    private final Runnable field_193981_i;
    private float time;
    private List<String> lines;
    private int totalScrollLength;
    private float scrollSpeed = 0.5F;

    public GuiWinGame(boolean p_i47590_1_, Runnable p_i47590_2_) {
        field_193980_h = p_i47590_1_;
        field_193981_i = p_i47590_2_;

        if (!p_i47590_1_) {
            scrollSpeed = 0.75F;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void update() {
        minecraft.getMusicTicker().update();
        Minecraft.getSoundHandler().update();
        float f = (float) (totalScrollLength + height + height + 24) / scrollSpeed;

        if (time > f) {
            sendRespawnPacket();
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c) {
        if (key == 1) {
            sendRespawnPacket();
        }
    }

    private void sendRespawnPacket() {
        field_193981_i.run();
        Minecraft.openScreen(null);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean pauses() {
        return true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init() {
        if (lines == null) {
            lines = Lists.newArrayList();
            IResource iresource = null;

            try {
                String s = "" + Formatting.WHITE + Formatting.OBFUSCATED + Formatting.GREEN + Formatting.AQUA;
                int i = 274;

                if (field_193980_h) {
                    iresource = Minecraft.getResourceManager().getResource(new Namespaced("texts/end.txt"));
                    InputStream inputstream = iresource.getInputStream();
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    Random random = new Random(8124371L);
                    String s1;

                    while ((s1 = bufferedreader.readLine()) != null) {
                        String s2;
                        String s3;

                        for (s1 = s1.replaceAll("PLAYERNAME", Minecraft.getSession().getUsername()); s1.contains(s); s1 = s2 + Formatting.WHITE + Formatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
                            int j = s1.indexOf(s);
                            s2 = s1.substring(0, j);
                            s3 = s1.substring(j + s.length());
                        }

                        lines.addAll(Minecraft.font.split(s1, 274));
                        lines.add("");
                    }

                    inputstream.close();

                    for (int k = 0; k < 8; ++k) {
                        lines.add("");
                    }
                }

                InputStream inputstream1 = Minecraft.getResourceManager().getResource(new Namespaced("texts/credits.txt")).getInputStream();
                BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(inputstream1, StandardCharsets.UTF_8));
                String s4;

                while ((s4 = bufferedreader1.readLine()) != null) {
                    s4 = s4.replaceAll("PLAYERNAME", Minecraft.getSession().getUsername());
                    s4 = s4.replaceAll("\t", "    ");
                    lines.addAll(Minecraft.font.split(s4, 274));
                    lines.add("");
                }

                inputstream1.close();
                totalScrollLength = lines.size() * 12;
            } catch (Exception exception) {
                LOGGER.error("Couldn't load credits", exception);
            } finally {
                IOUtils.closeQuietly(iresource);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = width;
        float f = -time * 0.5F * scrollSpeed;
        float f1 = (float) height - time * 0.5F * scrollSpeed;
        float f2 = 0.015625F;
        float f3 = time * 0.02F;
        float f4 = (float) (totalScrollLength + height + height + 24) / scrollSpeed;
        float f5 = (f4 - 20.0F - time) * 0.005F;

        if (f5 < f3) {
            f3 = f5;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        f3 = f3 * f3;
        f3 = f3 * 96.0F / 255.0F;
        bufferbuilder.pos(0.0D, height, zLevel).tex(0.0D, f * 0.015625F).color(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.pos(i, height, zLevel).tex((float) i * 0.015625F, f * 0.015625F).color(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.pos(i, 0.0D, zLevel).tex((float) i * 0.015625F, f1 * 0.015625F).color(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, zLevel).tex(0.0D, f1 * 0.015625F).color(f3, f3, f3, 1.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawWinGameScreen(mouseX, mouseY, partialTick);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 274;
        int j = width / 2 - 137;
        int k = height + 50;
        time += partialTick;
        float f = -time * scrollSpeed;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f, 0.0F);
        Minecraft.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        drawTexturedModalRect(j, k, 0, 0, 155, 44);
        drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        Minecraft.getTextureManager().bindTexture(field_194401_g);
        Gui.drawModalRectWithCustomSizedTexture(j + 88, k + 37, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
        GlStateManager.disableAlpha();
        int l = k + 100;

        for (int i1 = 0; i1 < lines.size(); ++i1) {
            if (i1 == lines.size() - 1) {
                float f1 = (float) l + f - (float) (height / 2 - 6);

                if (f1 < 0.0F) {
                    GlStateManager.translate(0.0F, -f1, 0.0F);
                }
            }

            if ((float) l + f + 12.0F + 8.0F > 0.0F && (float) l + f < (float) height) {
                String s = lines.get(i1);

                if (s.startsWith("[C]")) {
                    font.drawStringWithShadow(s.substring(3), (float) (j + (274 - font.getStringWidth(s.substring(3))) / 2), (float) l, 16777215);
                } else {
                    font.drawStringWithShadow(s, (float) j, (float) l, 16777215);
                }
            }

            l += 12;
        }

        GlStateManager.popMatrix();
        Minecraft.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        int j1 = width;
        int k1 = height;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, k1, zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(j1, k1, zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(j1, 0.0D, zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.draw(mouseX, mouseY, partialTick);
    }
}
