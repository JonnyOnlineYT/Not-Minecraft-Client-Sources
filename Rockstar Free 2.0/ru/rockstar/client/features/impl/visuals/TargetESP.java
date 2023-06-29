package ru.rockstar.client.features.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.Aura;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.combat.Aura;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class TargetESP extends Feature {

    double height;
    boolean animat;
    public  ListSetting bebraPonyxana;
    public  NumberSetting circlesize;
    public  BooleanSetting depthTest;
    public TargetESP() {
        super("TargetESP", "Рисует красивый круг на энтити", 0, Category.DISPLAY);
        bebraPonyxana = new ListSetting("TargetESP Mode", "Jello",() -> true, "Jello","Sims");
        circlesize = new NumberSetting("Circle Size", "Размер круга", 0.4F, 0.1F, 1F,0.1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
        depthTest = new BooleanSetting("DepthTest","Глубина(test)",false, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
       addSettings(bebraPonyxana,circlesize,depthTest);
    }

    @EventTarget
    public void jija(Event3D xaski) {

        String mode = bebraPonyxana.getOptions();

        this.setModuleName("TargetESP " + ChatFormatting.GRAY + mode);

        if (KillAura.target != null && KillAura.target.getHealth() > 0.0 && mc.player.getDistanceToEntity(KillAura.target) <= (ru.rockstar.api.utils.combat.RangeHelper.getRange() + KillAura.addrange.getNumberValue()) && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {

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
                GL11.glTranslated(KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * xaski.getPartialTicks() - mc.renderManager.viewerPosX, (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * xaski.getPartialTicks() - mc.renderManager.viewerPosY) + KillAura.target.height + height, KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * xaski.getPartialTicks() - mc.renderManager.viewerPosZ);
                GL11.glRotatef((mc.player.ticksExisted + mc.timer.renderPartialTicks) * 10, 0.0f, 1.0F, 0.0f);

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
                }

                if (mode.equalsIgnoreCase("Jello")) {
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

                    drawPercent = MathHelper.easeInOutQuad(drawPercent, 2);

                    mc.entityRenderer.disableLightmap();
                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glEnable(GL11.GL_BLEND);
                    if (depthTest.getBoolValue())
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glShadeModel(7425);
                    mc.entityRenderer.disableLightmap();

                    double radius = circlesize.getNumberValue();
                    double height = KillAura.target.height + 0.1;
                    double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * xaski.getPartialTicks() - mc.renderManager.viewerPosX;
                    double y = (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * xaski.getPartialTicks() - mc.renderManager.viewerPosY) + height * drawPercent;
                    double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * xaski.getPartialTicks() - mc.renderManager.viewerPosZ;
                    double eased = (height / 3) * ((drawPercent > 0.5) ? 1 - drawPercent : drawPercent) * ((drawMode) ? -1 : 1);

                    for (int lox = 0; lox < 360; lox += 5) {
                        Color color = ClientHelper.getClientColor(5, lox, 5);
                        double x1 = x - Math.sin(lox * Math.PI / 180F) * radius;
                        double z1 = z + Math.cos(lox * Math.PI / 180F) * radius;
                        double x2 = x - Math.sin((lox - 5) * Math.PI / 180F) * radius;
                        double z2 = z + Math.cos((lox - 5) * Math.PI / 180F) * radius;
                        GL11.glBegin(GL11.GL_QUADS);
                        DrawHelper.glColor(color, 0f);
                        GL11.glVertex3d(x1, y + eased, z1);
                        GL11.glVertex3d(x2, y + eased, z2);
                        DrawHelper.glColor(color, 255);
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
                    if (depthTest.getBoolValue())
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glPopMatrix();
                }
            }
        
        
    }
    @Override
    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }
}
