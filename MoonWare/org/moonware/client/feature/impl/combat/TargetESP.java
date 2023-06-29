package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.math.SHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.moonware.client.helpers.render2.RenderHelper2.alternateColorGradient;
import static org.moonware.client.helpers.render2.RenderHelper2.oneColorGradient;

public class TargetESP extends Feature {
    private double circleAnim;
    double height;
    boolean animat;
    public ListSetting bebraPonyxana;
    public NumberSetting circlesize;
    public NumberSetting points;
    public static BooleanSetting astolfo;
    public static BooleanSetting enableHudColors;
    public ColorSetting targetEspColor;

    public TargetESP() {
        super("TargetESP", "������ �������� ���� �� ������", Type.Combat);
        bebraPonyxana = new ListSetting("TargetESP Mode", "Jello", () -> true, "Jello", "Sims", "Astolfo");
        circlesize = new NumberSetting("Circle Size", "������ �����", 0.4F, 0.1F, 3F, 0.1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello") || bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        points = new NumberSetting("Points", 30F, 3F, 30F, 1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        astolfo = new BooleanSetting("Astolfo", "s", false, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
        enableHudColors = new BooleanSetting("Hud Colors", "s", false, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));

        targetEspColor = new ColorSetting("TargetESP Color", Color.RED.getRGB(), () -> true);
        addSettings(bebraPonyxana, circlesize,points,targetEspColor, astolfo,enableHudColors);
    }

    @EventTarget
    public void onRender(EventRender3D event3D) {
        String mode = bebraPonyxana.getOptions();

        setSuffix(mode);

        if (KillAura.target != null && KillAura.target.getHealth() > 0.0 && Minecraft.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue() && MoonWare.featureManager.getFeatureByClass(KillAura.class).getState()) {

            if (mode.equalsIgnoreCase("Sims")) {
                float radius = 0.2f;
                int side = 4;

                if (animat) {
                    height = MathHelper.lerp(height, 0.4, 2 * Feature.deltaTime());
                    if (height > 0.39) animat = false;
                } else {
                    height = MathHelper.lerp(height, 0.1, 4 * Feature.deltaTime());
                    if (height < 0.11) animat = true;
                }

                GL11.glPushMatrix();
                GL11.glTranslated(KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosX, (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosY) + KillAura.target.height + height, KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosZ);
                GL11.glRotatef((Minecraft.player.ticksExisted + Minecraft.timer.renderPartialTicks) * 10, 0.0f, 1.0F, 0.0f);
                DrawHelper.setColor(KillAura.target.hurtTime > 0 ? DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 60).getRGB() : DrawHelper.TwoColoreffect(new Color(90, 200, 79), new Color(30, 120, 20), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 90).getRGB());
                DrawHelper.enableSmoothLine(0.5F);
                Cylinder c = new Cylinder();
                c.setDrawStyle(GLU.GLU_LINE);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                c.draw(0F, radius, 0.3f, side, 100);
                GL11.glTranslated(0.0, 0.0, 0.3);
                c.draw(radius, 0f, 0.3f, side, 100);
                DrawHelper.disableSmoothLine();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            } else if (mode.equalsIgnoreCase("Jello")) {
                double everyTime = 1500;
                double drawTime = (System.currentTimeMillis() % everyTime);
                boolean drawMode = drawTime > (everyTime / 2);
                double drawPercent = drawTime / (everyTime / 2);
                // true when goes up
                if (!drawMode) {
                    drawPercent = 1 - drawPercent;
                } else {
                    drawPercent -= 1;
                }

                drawPercent = SHelper.easeInOutQuad(drawPercent, 4);

                Minecraft.gameRenderer.disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                if (astolfo.getBoolValue())
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7425);
                Minecraft.gameRenderer.disableLightmap();

                double radius = circlesize.getNumberValue();
                double height = KillAura.target.height + 0.1;
                double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosX;
                double y = (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosY) + height * drawPercent;
                double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * event3D.getPartialTicks() - Minecraft.getRenderManager().viewerPosZ;
                double eased = (height / 3) * ((drawPercent > 0.5) ? 1 - drawPercent : drawPercent) * ((drawMode) ? -1 : 1);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                for (int lox = 0; lox < 360; lox += 5) {
                    GL11.glEnable(GL11.GL_BLEND);
                    boolean fal = astolfo.get();
                    Color color1 =!fal ? ColorUtil.rainbow(7, lox, 1, 1, .5f) : ColorUtil.skyRainbow(7,lox);
                    if (enableHudColors.get()) {
                        if (!HUD.useCustomColors.get()) {
                            color1 = ColorUtil.interpolateColorsBackAndForth(82, (int) ((lox + System.currentTimeMillis() / 2) * 4), oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                        } else {
                            color1 = ColorUtil.interpolateColorsBackAndForth(82, (int) ((lox + System.currentTimeMillis() / 2) * 4), HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                        }
                    }
                    double x1 = x - Math.sin(lox * Math.PI / 180F) * radius;
                    double z1 = z + Math.cos(lox * Math.PI /180F) * radius;
                    double x2 = x - Math.sin((lox - 5) * Math.PI / 180F) * radius;
                    double z2 = z + Math.cos((lox - 5) * Math.PI / 180F) * radius;
                    GL11.glBegin(GL11.GL_QUADS);
                    DrawHelper.glColor(color1, 0f);
                    GL11.glVertex3d(x1, y + eased, z1);
                    GL11.glVertex3d(x2, y + eased, z2);
                    DrawHelper.glColor(color1, 255);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();

                    GL11.glBegin(GL_LINE_LOOP);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();
                }

                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7424);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                if (astolfo.getBoolValue())
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            } else if (mode.equalsIgnoreCase("Astolfo")) {
                if (KillAura.target != null) {
                    if (KillAura.target.getHealth() > 0) {
                        circleAnim += 0.015F * Minecraft.frameTime / 10;
                        RenderHelper2.drawCircle3DA(KillAura.target, circleAnim + 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        RenderHelper2.drawCircle3DA(KillAura.target, circleAnim - 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        RenderHelper2.drawCircle3DA(KillAura.target, circleAnim, event3D.getPartialTicks(), (int) points.getNumberValue(), 2, targetEspColor.getColorValue());
                        circleAnim = MathHelper.clamp(circleAnim, 0, circlesize.getNumberValue() * 0.5f);
                    } else {
                        circleAnim = 0;
                    }
                }
            }
        }
    }
}
