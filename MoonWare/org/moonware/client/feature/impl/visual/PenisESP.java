package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.visual.anim.ClientSettings;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class PenisESP extends Feature {
    public static BooleanSetting self = new BooleanSetting("apply for self",true);
    public static BooleanSetting others = new BooleanSetting("otherPlayers",true);
    public static NumberSetting dlina = new NumberSetting("length of you", 4,0,10,0.001F, self::get);
    public static NumberSetting dlinao = new NumberSetting("length of others", 0.2F,0,10,0.001F, others::get);
    public PenisESP() {
        super("PenisESP","Ты действительно думаешь что это нужная функция?", Type.Visuals);
        addSettings(self, others, dlina, dlinao);
    }
    @Override
    public void onEnable() {
        //new Configurable();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @EventTarget
    public void onRender3D(EventRender3D event ) {
        for (Object o : Minecraft.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)o;
                double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * Minecraft.timer.renderPartialTicks;
                Minecraft.getRenderManager();
                double x = n - RenderManager.renderPosX;
                double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * Minecraft.timer.renderPartialTicks;
                Minecraft.getRenderManager();
                double y = n2 - RenderManager.renderPosY;
                double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * Minecraft.timer.renderPartialTicks;
                Minecraft.getRenderManager();
                double z = n3 - RenderManager.renderPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                esp(player, x, y, z);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
            ClientSettings.panimation = false;
            if (ClientSettings.panimation) {
                ++ClientSettings.pamount;
                if (ClientSettings.pamount > 25) {
                    ++ClientSettings.pspin;
                    if (ClientSettings.pspin > 50.0f) {
                        ClientSettings.pspin = -50.0f;
                    }
                    else if (ClientSettings.pspin < -50.0f) {
                        ClientSettings.pspin = 50.0f;
                    }
                    ClientSettings.pamount = 0;
                }
                ++ClientSettings.pcumsize;
                if (ClientSettings.pcumsize > 180.0f) {
                    ClientSettings.pcumsize = -180.0f;
                }
                else {
                    if (ClientSettings.pcumsize >= -180.0f) {
                        continue;
                    }
                    ClientSettings.pcumsize = 180.0f;
                }
            }
            else {
                ClientSettings.pcumsize = 0.0f;
                ClientSettings.pamount = 0;
                ClientSettings.pspin = 0.0f;
            }
        }
    }
    public void esp(EntityPlayer player, double x, double y, double z) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotated((player.isSneaking() ? 10 : 0) + ClientSettings.pspin, 1.0f + ClientSettings.pspin, 0.0f, ClientSettings.pcumsize);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        if (player == Minecraft.player && self.get()) {
            Cylinder shaft = new Cylinder();
            shaft.setDrawStyle(100013);
            shaft.draw(0.1f, 0.11f, dlina.getNumberValue(), 25, 20);
            GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
            GL11.glTranslated(0.0, 0.0, -0.05500000298023223);
            GL11.glTranslated(-0.19000000074505805, 0.0, 0.0);
            Sphere right = new Sphere();
            right.setDrawStyle(100013);
            right.draw(0.24f, 10, 20);
            GL11.glTranslated(0.36000000149011612, 0.0, 0.0);
            Sphere left = new Sphere();
            left.setDrawStyle(100013);
            left.draw(0.24f, 10, 21);
            GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslated(-0.17000000074505806, 0.0, dlina.getCurrentValue() + 0.13F);
            Sphere tip = new Sphere();
            tip.setDrawStyle(100013);
            tip.draw(0.13f, 15, 20);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }else if (others.get()) {
            Cylinder shaft = new Cylinder();
            shaft.setDrawStyle(100013);
            shaft.draw(0.1f, 0.11f, dlinao.getNumberValue(), 25, 20);
            GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
            GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
            GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
            Sphere right = new Sphere();
            right.setDrawStyle(100013);
            right.draw(0.14f, 10, 20);
            GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
            Sphere left = new Sphere();
            left.setDrawStyle(100013);
            left.draw(0.14f, 10, 20);
            GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslated(-0.07000000074505806, 0.0, dlinao.getCurrentValue() + 0.189999952316284);
            Sphere tip = new Sphere();
            tip.setDrawStyle(100013);
            tip.draw(0.13f, 15, 20);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }
    }
}
