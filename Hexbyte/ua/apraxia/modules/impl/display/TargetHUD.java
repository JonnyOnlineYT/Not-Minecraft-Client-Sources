package ua.apraxia.modules.impl.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import ua.apraxia.Hexbyte;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.impl.combat.Aura;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.utility.animation.AnimationUtil;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.math.TimerUtility;
import ua.apraxia.utility.other.Particles;
import ua.apraxia.utility.render.RenderUtility;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import ua.apraxia.utility.render.RoundedUtility;
import ua.apraxia.utility.render.StencilUtility;

import java.awt.*;
import java.util.ArrayList;

public class TargetHUD extends Module {
    public static EntityLivingBase curTarget = null;
    private ArrayList<Particles> particles = new ArrayList<>();
    private boolean show;
    private double scale;
    private double healthBarWidth;
    private String enemyNickname;
    private double enemyHP;
    private double enemyDistance;
    private EntityPlayer entityPlayer;
    private Entity entity;

    private float healthAnim;

    private Entity target;

    private float a15;
    public static TimerUtility thudTimer = new TimerUtility();
    public BooleanSetting particles2 = new BooleanSetting("Particles", true);
    public ColorSetting thudColor = new ColorSetting("TargetHUD Color",  new Color(3, 167, 243, 255).getRGB());
    public ColorSetting particlesColor = new ColorSetting("Particles Color",  new Color(3, 167, 243, 255).getRGB());

    float x5; final double c4 = (2 * Math.PI) / 3;

    public TargetHUD() {
        super("TargetHUD", Categories.Display);
        addSetting(particles2, particlesColor, thudColor);
    }

    private void drawScaledString(String text, double x, double y, double scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, x);
        GlStateManager.scale(scale, scale, scale);
        FontRenderer.drawStringWithOutline(mc.fontRenderer, text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        ua.apraxia.draggable.component.impl.TargetHUD dwm = (ua.apraxia.draggable.component.impl.TargetHUD) Hexbyte.getInstance().draggable.getDraggableComponentByClass(ua.apraxia.draggable.component.impl.TargetHUD.class);
        dwm.setWidth(120);
        dwm.setHeight(35);
        if (Aura.target != null) {
            curTarget = Aura.target;
            scale = AnimationUtil.animation((float) scale, (float) 1, (float) (Hexbyte.deltaTime()));
        } else {
            scale = AnimationUtil.animation((float) scale, (float) 0, (float) (Hexbyte.deltaTime()));
        }
        EntityLivingBase target = Aura.target;
        if (curTarget != null) {
            if (curTarget instanceof EntityPlayer) {
                if (Minecraft.player.getDistance(target) < Aura.instance.distance.value && Hexbyte.getInstance().moduleManagment.getModule(Aura.class).isModuleState()) {
                    try {
                        GlStateManager.pushMatrix();
                        GlStateManager.resetColor();
                        ScaledResolution sr = new ScaledResolution(mc);
                        GL11.glTranslated(dwm.getX() + 55, dwm.getY() + 11, 0);
                        GL11.glScaled(scale, scale, 0);
                        GL11.glTranslated(-(dwm.getX() + 55), -(dwm.getY()+ 11 ), 0);
                        double healthWid = (curTarget.getHealth() / curTarget.getMaxHealth() * 360);
                        healthWid = MathHelper.clamp(healthWid, 0.0D, 360);
                        healthBarWidth = AnimationUtil.animation(healthBarWidth, (float) healthWid, (float) (160 * Hexbyte.deltaTime()));
                        String health = "" + MathUtility.round(curTarget.getHealth(), 1);
                        String distance = "" + MathUtility.round(mc.player.getDistance(curTarget), 1);
                        double armorWid = (((EntityLivingBase) target).getTotalArmorValue() * 4.66);
                        RoundedUtility.drawRound(dwm.getX(), dwm.getY() + 1, dwm.getWidth(), dwm.getHeight() - 10, 3, new Color(14, 14, 14, 255));
                        Fonts.sfbolt14.drawString(curTarget.getName(), dwm.getX() + 27, dwm.getY() + 9, -1);
                        Fonts.medium12.drawString("" + distance + "m", dwm.getX() + 57, dwm.getY() + 18, -1);
                        Fonts.medium12.drawString("Distance:", dwm.getX() + 27, dwm.getY() + 18, new Color(thudColor.color).getRGB());
                        RenderUtility.drawBlurredShadow(dwm.getX() + 80, dwm.getY() + 1, 40, dwm.getHeight() - 10, 5, new Color(14, 14, 14, 255));
                        RenderUtility.drawCircle((float) dwm.getX() + 105, (float) (dwm.getY() + 14), (float) 0, (float) 360, 10F, new Color(17, 17, 17, 255).getRGB(), (int) 3f);
                        RenderUtility.drawCircle((float) dwm.getX() + 105, (float) (dwm.getY() + 14), (float) 0, (float) healthBarWidth, 10F, new Color(thudColor.color).getRGB(), (int) 3f);
                        Fonts.sfbolt12.drawCenteredString(health, dwm.getX() + 105, dwm.getY() + 13, -1);
                        // drawScaledString(health, dwm.getX() + 105, dwm.getY() + 13, 0.5, new Color(255, 255, 255, 255).getRGB());
                        healthBarWidth = AnimationUtil.getAnimationState((float) healthBarWidth, (float) healthWid, (float) (10 * Hexbyte.deltaTime()));
                        if (particles2.value) {
                            for (final Particles p : particles) {
                                if (p.opacity > 4) p.render2D();
                            }

                            if (thudTimer.hasReached(15)) {
                                for (final Particles p : particles) {
                                    p.updatePosition();

                                    if (p.opacity < 1) particles.remove(p);
                                }
                                thudTimer.reset();
                            }

                            if (curTarget.hurtTime == 8) {
                                for (int i = 0; i < 1; i++) {
                                    final Particles p = new Particles();
                                    p.init((dwm.getX() + 15), dwm.getY() + 15, ((Math.random() - 0.5) * 2) * 1.9, ((Math.random() - 0.5) * 2) * 1.4, (float) Math.random() * 4, new Color(particlesColor.color));
                                    particles.add(p);
                                }
                            }
                        }
                        for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                            try {
                                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == curTarget && curTarget != null) {
                                    StencilUtility.initStencilToWrite();
                                    RenderUtility.drawFCircle(dwm.getX() + 12.5f, dwm.getY() + 14, 0, 360, 9, true, new Color(26, 80, 67, 255));
                                    StencilUtility.readStencilBuffer(1);
                                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    float hurtPercent = getHurtPercent(curTarget);
                                    GL11.glPushMatrix();
                                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                                    Gui.drawScaledCustomSizeModalRect(dwm.getX() + 3, dwm.getY() + 4, 8.0f, 8.0f, 8, 8, 20, 20, 64.0f, 64.0f);
                                    RenderUtility.drawCircle(dwm.getX() + 12.5f, dwm.getY() + 14, 0, 360, 10, new Color(14, 14, 14, 255).getRGB(), 2);
                                    GL11.glPopMatrix();
                                    GlStateManager.bindTexture(0);
                                }
                                StencilUtility.uninitStencilBuffer();
                            } catch (Exception exception) {
                            }
                        }
                        } finally{
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }
    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }
}
