package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.KillAuraUtilsi.EntityUtil;
import org.moonware.client.feature.impl.visual.anim.WorldRenderEvent;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.glAlphaFunc;

public class Trails extends Feature {

    private final BooleanSetting players = new BooleanSetting("Show on Players", false);
    private final BooleanSetting friends = new BooleanSetting("Show on Friends", true,() -> !players.isToggle());
    private final NumberSetting alpha = new NumberSetting("Alpha", 0.84F, 0, 1);
    private final NumberSetting fadeTime = new NumberSetting("Fade Time", 450, 0, 1000);
    private final BooleanSetting applyForAll = new BooleanSetting("Apply on all players", false);

    private final ListSetting mode =
            new ListSetting("Color Mode", "Astolfo", "Astolfo", "Static", "Rainbow", "Gradient");

    private final ColorSetting color = new ColorSetting("Color", new Color(0xFFFC9513).getRGB(),() -> mode.getCurrentMode().equalsIgnoreCase("Static")|| mode.getCurrentMode().equalsIgnoreCase("Gradient"));

    private final List<Lines> lines = new ArrayList<>();

    public Trails() {
        super("Trails", "Полоска сзади вас", Type.Visuals);
        addSettings(mode,applyForAll, color,players,friends,alpha, fadeTime);
    }

    @Override
    public void onDisable() {
        synchronized (lines) {
            lines.clear();
        }
    }

    @EventTarget
    public void onWorldRender(WorldRenderEvent event) {
        if (Minecraft.gameSettings.thirdPersonView == 0) {
            /*     */       return;
            /*     */     }
        int fTime = (int) ((400 + fadeTime.getValue()) / Minecraft.timer.timerSpeed);
        long fadeSec = System.currentTimeMillis() - fTime;
//        CustomModels customModel = Wrapper.getModule().get("CustomModels");

        synchronized (lines) {
            if (players.isToggle() || friends.isToggle()) {
                for (EntityPlayer player : Minecraft.world.playerEntities) {
                    if (friends.isToggle() && !players.isToggle() && !MoonWare.friendManager.getFriends().contains(player.getName()))
                        continue;

                    double[] ipos = EntityUtil.interpolate(player);
                    double sin = -Math.sin(Math.toRadians(player.rotationYaw)) * -0.1;
                    double cos = Math.cos(Math.toRadians(player.rotationYaw)) * -0.1;
                    lines.add(new Lines(player, ipos[0] + sin, ipos[1] + 0.2, ipos[2] + cos,
                            ColorUtil.applyOpacity(getColor(20, 50), alpha.getValue()),
                            System.currentTimeMillis()));
                }
            }
            double partialTicks = Helper.mc.getRenderPartialTicks();
            double x = Minecraft.player.lastTickPosX + (Minecraft.player.posX - Minecraft.player.lastTickPosX) * partialTicks;
            double y = Minecraft.player.lastTickPosY + (Minecraft.player.posY - Minecraft.player.lastTickPosY) * partialTicks;
            double z = Minecraft.player.lastTickPosZ + (Minecraft.player.posZ - Minecraft.player.lastTickPosZ) * partialTicks;


            double sin = -Math.sin(Math.toRadians(Minecraft.player.rotationYaw)) * -0.1;
            double cos = Math.cos(Math.toRadians(Minecraft.player.rotationYaw)) * -0.1;
            for (EntityPlayer entity : Minecraft.world.playerEntities) {
                if (applyForAll.get()) {
                    lines.add(new Lines(entity, x + sin, y + 0.2, z + cos,
                            getColor(200, 150), System.currentTimeMillis()));
                }
            }
            if (!applyForAll.get()) {
                lines.add(new Lines(Minecraft.player, x + sin, y + 0.2, z + cos,
                        getColor(200, 150), System.currentTimeMillis()));
            }

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            glAlphaFunc (GL_GREATER, 0.1f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            Minecraft.gameRenderer.disableLightmap();
            GL11.glLineWidth(1);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            Collection<Lines> linesCollection = new ArrayList<>(lines);

            for (Lines line : linesCollection) {
                boolean cm = CustomModel.isEnabled() && CustomModel.isValid(line.entity);
                float cy = cm && CustomModel.modelMode.getCurrentMode().equalsIgnoreCase("Amogus") ? 0.76f : 0.5f;

                double xCoord = line.getX() - RenderManager.renderPosX;
                double yCoord = line.getY() - RenderManager.renderPosY;
                double zCoord = line.getZ() - RenderManager.renderPosZ;

                Color color = new Color(line.getColor());

                float tc = (float) (line.getTime() - fadeSec) / (float) fTime;

                if (tc < 0 || tc > 1) {
                    lines.remove(line);
                    continue;
                }
                GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, tc * alpha.getValue());
                GL11.glVertex3d(xCoord, yCoord, zCoord);
                GL11.glVertex3d(xCoord, yCoord + Minecraft.player.height - cy, zCoord);


                //RoundedUtil.drawRound((float) xCoord, (float) yCoord,1,40,5,new Color(31,31,31));
            }
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GL11.glEnd();

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    private int getColor(int offset, int speed) {
        int color = -1;
        switch (mode.getCurrentMode()) {
            case "Astolfo":
                color = ColorUtil.astolfoRainbow(offset * speed).getRGB();
                break;
            case "Static":
                color = this.color.getColorc().getRGB();
                break;
            case "Rainbow":
                color = ColorUtil.rainbow(offset * speed, 20);
                break;
            case "Gradient":
                color = ColorUtil.fade(this.color.getColorc(), offset * speed);
                break;
        }
        return color;
    }

    private class Lines {
        private Entity entity;
        private double x, y, z;
        private int color;
        private long time;

        public Lines(Entity entity, double x, double y, double z, int color, long time) {
            this.entity = entity;
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
            this.time = time;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public int getColor() {
            return color;
        }

        public long getTime() {
            return time;
        }
    }
}