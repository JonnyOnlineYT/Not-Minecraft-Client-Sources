package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class GuiSubtitleOverlay extends Gui implements ISoundEventListener
{
    private final Minecraft client;
    private final List<GuiSubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();
    private boolean enabled;

    public GuiSubtitleOverlay(Minecraft clientIn)
    {
        client = clientIn;
    }

    public void renderSubtitles(ScaledResolution resolution)
    {
        if (!enabled && Minecraft.gameSettings.showSubtitles)
        {
            Minecraft.getSoundHandler().addListener(this);
            enabled = true;
        }
        else if (enabled && !Minecraft.gameSettings.showSubtitles)
        {
            Minecraft.getSoundHandler().removeListener(this);
            enabled = false;
        }

        if (enabled && !subtitles.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Vec3d vec3d = new Vec3d(Minecraft.player.posX, Minecraft.player.posY + (double) Minecraft.player.getEyeHeight(), Minecraft.player.posZ);
            Vec3d vec3d1 = (new Vec3d(0.0D, 0.0D, -1.0D)).rotatePitch(-Minecraft.player.rotationPitch * 0.017453292F).rotateYaw(-Minecraft.player.rotationYaw * 0.017453292F);
            Vec3d vec3d2 = (new Vec3d(0.0D, 1.0D, 0.0D)).rotatePitch(-Minecraft.player.rotationPitch * 0.017453292F).rotateYaw(-Minecraft.player.rotationYaw * 0.017453292F);
            Vec3d vec3d3 = vec3d1.crossProduct(vec3d2);
            int i = 0;
            int j = 0;
            Iterator<GuiSubtitleOverlay.Subtitle> iterator = subtitles.iterator();

            while (iterator.hasNext())
            {
                GuiSubtitleOverlay.Subtitle guisubtitleoverlay$subtitle = iterator.next();

                if (guisubtitleoverlay$subtitle.getStartTime() + 3000L <= Minecraft.getSystemTime())
                {
                    iterator.remove();
                }
                else
                {
                    j = Math.max(j, Minecraft.font.getStringWidth(guisubtitleoverlay$subtitle.getString()));
                }
            }

            j = j + Minecraft.font.getStringWidth("<") + Minecraft.font.getStringWidth(" ") + Minecraft.font.getStringWidth(">") + Minecraft.font.getStringWidth(" ");

            for (GuiSubtitleOverlay.Subtitle guisubtitleoverlay$subtitle1 : subtitles)
            {
                int k = 255;
                String s = guisubtitleoverlay$subtitle1.getString();
                Vec3d vec3d4 = guisubtitleoverlay$subtitle1.getLocation().subtract(vec3d).normalize();
                double d0 = -vec3d3.dotProduct(vec3d4);
                double d1 = -vec3d1.dotProduct(vec3d4);
                boolean flag = d1 > 0.5D;
                int l = j / 2;
                int i1 = Minecraft.font.height;
                int j1 = i1 / 2;
                float f = 1.0F;
                int k1 = Minecraft.font.getStringWidth(s);
                int l1 = MathHelper.floor(MathHelper.clampedLerp(255.0D, 75.0D, (float)(Minecraft.getSystemTime() - guisubtitleoverlay$subtitle1.getStartTime()) / 3000.0F));
                int i2 = l1 << 16 | l1 << 8 | l1;
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)resolution.getScaledWidth() - (float)l * 1.0F - 2.0F, (float)(resolution.getScaledHeight() - 30) - (float)(i * (i1 + 1)) * 1.0F, 0.0F);
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
                Gui.drawRect(-l - 1, -j1 - 1, l + 1, j1 + 1, -872415232);
                GlStateManager.enableBlend();

                if (!flag)
                {
                    if (d0 > 0.0D)
                    {
                        Minecraft.font.drawString(">", l - Minecraft.font.getStringWidth(">"), -j1, i2 + -16777216);
                    }
                    else if (d0 < 0.0D)
                    {
                        Minecraft.font.drawString("<", -l, -j1, i2 + -16777216);
                    }
                }

                Minecraft.font.drawString(s, -k1 / 2, -j1, i2 + -16777216);
                GlStateManager.popMatrix();
                ++i;
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public void soundPlay(ISound soundIn, SoundEventAccessor accessor)
    {
        if (accessor.getSubtitle() != null)
        {
            String s = accessor.getSubtitle().asFormattedString();

            if (!subtitles.isEmpty())
            {
                for (GuiSubtitleOverlay.Subtitle guisubtitleoverlay$subtitle : subtitles)
                {
                    if (guisubtitleoverlay$subtitle.getString().equals(s))
                    {
                        guisubtitleoverlay$subtitle.refresh(new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF()));
                        return;
                    }
                }
            }

            subtitles.add(new GuiSubtitleOverlay.Subtitle(s, new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF())));
        }
    }

    public class Subtitle
    {
        private final String subtitle;
        private long startTime;
        private Vec3d location;

        public Subtitle(String subtitleIn, Vec3d locationIn)
        {
            subtitle = subtitleIn;
            location = locationIn;
            startTime = Minecraft.getSystemTime();
        }

        public String getString()
        {
            return subtitle;
        }

        public long getStartTime()
        {
            return startTime;
        }

        public Vec3d getLocation()
        {
            return location;
        }

        public void refresh(Vec3d locationIn)
        {
            location = locationIn;
            startTime = Minecraft.getSystemTime();
        }
    }
}
