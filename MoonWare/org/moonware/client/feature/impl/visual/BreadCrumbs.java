package org.moonware.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;

public class BreadCrumbs
        extends Feature {
    public static NumberSetting sizeToRemove;
    public static NumberSetting trailsAlpha;
    public static NumberSetting heyt;
    public static ListSetting trailsMode;
    public static ColorSetting topColor;
    public static ColorSetting bottomColor;
    private final BooleanSetting hideInFirstPerson;
    static final ArrayList<Trail> trails;

    public BreadCrumbs() {
        super("Trails", "\u041e\u0441\u0442\u0430\u0432\u043b\u044f\u0435\u0442 \u043b\u0438\u043d\u0438\u044e \u0445\u043e\u0434\u044c\u0431\u044b", Type.Visuals);
        sizeToRemove = new NumberSetting("Ticks", 300.0f, 10.0f, 500.0f, 10.0f, () -> true);
        topColor = new ColorSetting("Top Color", ClientHelper.getClientColor().getRGB(), () -> trailsMode.currentMode.equals("Custom"));
        bottomColor = new ColorSetting("Bottom Color", ClientHelper.getClientColor().getRGB(), () -> trailsMode.currentMode.equals("Custom"));
        trailsAlpha = new NumberSetting("Trails Alpha", 0.9f, 0.1f, 1.0f, 0.1f, () -> true);
        heyt = new NumberSetting("Trails Height", 1.5f, 0.65f, 1.9f, 0.1f, () -> true);
        this.hideInFirstPerson = new BooleanSetting("Hide In First Person", false, () -> true);
        this.addSettings(sizeToRemove, trailsMode, topColor, bottomColor, trailsAlpha, heyt, this.hideInFirstPerson);
    }

    static void createTrail() {
        Vec3d color = null;
        int oneColor = bottomColor.getColorValue();
        float red = (float)(oneColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(oneColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(oneColor & 0xFF) / 255.0f;
        switch (trailsMode.currentMode) {
            case "Client": {
                color = new Vec3d((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f);
                break;
            }
            case "Custom": {
                color = new Vec3d(red, green, blue);
                break;
            }
            case "Astolfo": {
                color = new Vec3d((float)PaletteHelper.astolfo(false, 1).getRed() / 255.0f, (float)PaletteHelper.astolfo(false, 1).getGreen() / 255.0f, (float)PaletteHelper.astolfo(false, 1).getBlue() / 255.0f);
                break;
            }
            case "Rainbow": {
                color = new Vec3d((float)PaletteHelper.rainbow(300, 1.0f, 1.0f).getRed() / 255.0f, (float) PaletteHelper.rainbow(300, 1.0f, 1.0f).getGreen() / 255.0f, (float)PaletteHelper.rainbow(300, 1.0f, 1.0f).getBlue() / 255.0f);
            }
        }
        double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)mc.getRenderPartialTicks();
        double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)mc.getRenderPartialTicks();
        double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)mc.getRenderPartialTicks();
        Vec3d vector = new Vec3d(x, y, z);
        trails.add(new Trail(vector, color));
    }

    @Override
    public void onDisable() {
        trails.clear();
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        float alpha;
        double z;
        double y;
        double x;
        if (mc.gameSettings.thirdPersonView == 0 && this.hideInFirstPerson.getBoolValue()) {
            return;
        }
        if (mc.player.motionX != 0.0 || mc.player.motionZ != 0.0) {
            createTrail();
        }
        trails.removeIf(Trail::remove);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3008);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)8);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int topColor1 = topColor.getColorValue();
        float red = (float)(topColor1 >> 16 & 0xFF) / 255.0f;
        float green = (float)(topColor1 >> 8 & 0xFF) / 255.0f;
        float blue = (float)(topColor1 & 0xFF) / 255.0f;
        float height = heyt.getNumberValue();
        for (Trail trail : trails) {
            x = trail.position.xCoord - mc.getRenderManager().renderPosX;
            y = trail.position.yCoord - mc.getRenderManager().renderPosY;
            z = trail.position.zCoord - mc.getRenderManager().renderPosZ;
            alpha = trailsAlpha.getNumberValue() * (1.0f - (float)trail.existed / sizeToRemove.getNumberValue());
            GL11.glColor4f((float)((float)trail.color.xCoord), (float)((float)trail.color.yCoord), (float)((float)trail.color.zCoord), (float)alpha);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
            GL11.glVertex3d((double)x, (double)(y + (double)height), (double)z);
        }
        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3008);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)8);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        for (Trail trail : trails) {
            x = trail.position.xCoord - mc.getRenderManager().renderPosX;
            y = trail.position.yCoord - mc.getRenderManager().renderPosY;
            z = trail.position.zCoord - mc.getRenderManager().renderPosZ;
            alpha = trailsAlpha.getNumberValue() * (1.0f - (float)trail.existed / sizeToRemove.getNumberValue());
            GL11.glColor4f((float)((float)trail.color.xCoord), (float)((float)trail.color.yCoord), (float)((float)trail.color.zCoord), (float)alpha);
            GL11.glVertex3d((double)x, (double)(y + (double)height), (double)z);
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
            GL11.glVertex3d((double)x, (double)(y + (double)height - (double)0.01f), (double)z);
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3008);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)8);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        for (Trail trail : trails) {
            x = trail.position.xCoord - mc.getRenderManager().renderPosX;
            y = trail.position.yCoord - mc.getRenderManager().renderPosY;
            z = trail.position.zCoord - mc.getRenderManager().renderPosZ;
            alpha = trailsAlpha.getNumberValue() * (1.0f - (float)trail.existed / sizeToRemove.getNumberValue());
            GL11.glColor4f((float)((float)trail.color.xCoord), (float)((float)trail.color.yCoord), (float)((float)trail.color.zCoord), (float)alpha);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
            GL11.glVertex3d((double)x, (double)(y + (double)0.01f), (double)z);
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)2884);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    static {
        trailsMode = new ListSetting("Trails Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
        trails = new ArrayList();
    }

    static class Trail {
        int existed;
        Vec3d position;
        Vec3d color;

        public Trail(Vec3d position, Vec3d color) {
            this.position = position;
            this.color = color;
            this.existed = -15;
        }

        boolean remove() {
            boolean n = false;
            ++this.existed;
            if (this.existed == 0) {
                this.existed = 1;
            }
            ++this.existed;
            return (this.existed >= sizeToRemove.get());
        }
    }
}

