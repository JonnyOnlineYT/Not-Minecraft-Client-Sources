package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.combat.Aura;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.feature.impl.combat.TargetHUD;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.comps.NamedTargetHudComponent;
import org.moonware.client.utils.*;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class TargetHUDComponent extends NamedTargetHudComponent {
    private float healthBarWidth;
    private TimerHelper timerTH = new TimerHelper();
    private long changeTime;
    private float displayPercent;
    public final EntityEquipmentSlot armorType;
    private long lastUpdate;
    private static EntityLivingBase curTarget;
    private float cooledAttackStrength;
    private float move;
    private float scale = 0;
    private float scalehelper = 0;
    private double translate;
    private float translatehelper;
    private double anim;
    private float health;
    private float healthhelp;
    private float movehelp;
    private float healthhelpr;
    private float radius;
    private static float radiushelp;
    private float radius2;
    private double hui;
    private double cooldownBarWidth;
    private double timerBarWidth;
    private double timerWid;
    private double hpWidth;
    private double HealthBarWidth;
    private String enemyNickname;
    private double enemyHP;
    private double enemyDistance;
    private EntityPlayer entityPlayer;
    private EntityPlayer entit;
    private Entity entity;
    public static int diap;
    private double thelp;
    private int widthTH = 145;
    private int heightTH = 57;
    private double двигающийсяХуй;

    public TargetHUDComponent() {
        super("TargetHUD", 200, 200, 145, 57);
        armorType = null;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(!MoonWare.featureManager.getFeatureByClass(TargetHUD.class).getState());
        super.draw(mouseX, mouseY, partialTick);
        MWUtils.scale(this.x + this.width / 2, this.y + this.height / 2, scale,( ) -> drawRunnable(mouseX,mouseY,partialTick));
    }
    public void drawRunnable(int mouseX, int mouseY, float partialTick) {
        boolean KillAura2State = MoonWare.featureManager.getFeatureByClass(KillAura.class).getState();
        boolean VAuraState = MoonWare.featureManager.getFeatureByClass(KillAura.class).getState();
        float range = (VAuraState ? KillAura.range.getNumberValue() : KillAura2State ? KillAura.range.getNumberValue() : 5);

        //target = KillAuraState ? (EntityPlayer) VAura.target :  VAuraState ? (EntityPlayer) VAura.target : null;
        String mode = TargetHUD.targetHudMode.getOptions();
        //EntityLivingBase target = target;
        //int color = TargetHUD.targetHudColor.getColorValue();
        target = Minecraft.screen instanceof ChatScreen ? Minecraft.player : KillAura.target;
        if (!(Minecraft.screen instanceof ChatScreen)  && KillAura.target == null) {
            scalehelper = 0;
        }

        if (target != null) {
            if (target.getDistanceToEntity(Minecraft.player) <= (VAuraState ? KillAura.range.getNumberValue() : KillAura2State ? KillAura.range.getNumberValue() : 5) - 0.35F) {
                if (!Helper.timerHelper.hasReached(550)) {
                    scalehelper = 1.0F;
                } else {
                    scalehelper = 1.0F;
                }
                timerTH.reset();
            } else if (!(target.getDistanceToEntity(Minecraft.player) <= range - 0.35F) || target.isDead) {
                if (!timerTH.hasReached(50)) {
                    scalehelper = 1.05F;
                } else {
                    scalehelper = 0.0F;
                }
            }
            if (TargetHUD.targetHudAnimation.getBoolValue()) {
                if (TargetHUD.targetHudAnimationMode.getCurrentMode().equalsIgnoreCase("Scale")) {
                    scale = AnimationHelper.animation(scale, scalehelper, (float) (0.001f * Feature.deltaTime()));
                    //GL11.glScalef(scale, scale, scale);
                    //GlStateManager.translate(scale,scale,1);
                } else if (TargetHUD.targetHudAnimationMode.getCurrentMode().equalsIgnoreCase("Translate")) {
                    scale = AnimationHelper.animation(scale, scalehelper, (float) (0.001f * Feature.deltaTime()));
                }
            } else {
                scale = 1.0F;
            }
            if (TargetHUD.targetHudMode.getCurrentMode().equalsIgnoreCase("Type4")) {
                this.widthTH = 105;
                this.heightTH = 28;
                this.width = widthTH;
                this.height = heightTH;
                float x = (float) (this.x), y = (float) (this.y);
                RenderHelper2.drawRainbowRound(x, y, this.widthTH, this.heightTH, 4, true, true, true, true, 2, 4);
                StencilUtil.initStencilToWrite();
                RoundedUtil.drawRound(x + 2, y +4, 20, 20, 1, new Color(-1));
                StencilUtil.readStencilBuffer(1);
                try {
                    for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
                        if (target instanceof EntityPlayer || target != null) {
                            if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                                if (targetHead != null) {
                                    Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                } else {
                                    Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                                }

                                float hurtPercent = getHurtPercent(target);
                                GL11.glPushMatrix();
                                GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                                Gui.drawScaledCustomSizeModalRect(x + 2, y + 4, 8.0f, 8.0f, 8, 8, 20, 20, 64.0F, 64.0F);
                                GL11.glPopMatrix();
                                GlStateManager.bindTexture(0);
                            } else {
                                //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                            }
                        } else {
                            RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
                        }
                        GL11.glDisable(3089);
                    }
                } catch (Exception уч) {
                    org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/combat.png"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
                }
                StencilUtil.uninitStencilBuffer();
                GL11.glColor4f(1, 1, 1, 1);
                RenderUtils2.drawShadow(0, 2, () -> RoundedUtil.drawRound(x + 5, y +5, 18, 18, 7, new Color(-1)));
                MWFont.MONTSERRAT_BOLD.get(16).draw(target.getName(), x + 5 + 18 + 1, y + 5, -1);
                this.healthBarWidth = (float) Interpolator.linear (this.healthBarWidth,(target.getHealth() / target.getMaxHealth()) * (49),2f / 50);
                float maxwidth = (target.getMaxHealth() / target.getMaxHealth()) * (49) + MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(String.format("%.1f",(target.getMaxHealth() * 100 / target.getMaxHealth())) + "%");
                this.health = (float) Interpolator.linear (this.health,(target.getHealth() * 100 / target.getMaxHealth()),2f / 50);
                this.healthhelp = (float) Interpolator.linear (this.healthhelp,(target.getHealth() / target.getMaxHealth()) * (49) +  MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(String.format("%.1f",this.health) + "%"),2f / 30);
                RoundedUtil.drawRound(x + 25,y + 21, maxwidth ,2,1.0F,new Color(1));
                RenderHelper2.drawRainbowRound(x + 25, y + 21, this.healthhelp, 2, 1.0F,true, false, false,true,2,4);
                RenderUtils2.drawShadow(2,2,() -> RoundedUtil.drawRound(x + 25,y + 21, maxwidth ,2,1.0F,new Color(1)));
                MWFont.GREYCLIFFCF_MEDIUM.get(16).drawShadow(String.format("%.1f",this.health) + "%", x + 25 + this.healthBarWidth,y + 13,-1);

                int countleft = target.getHeldItem(EnumHand.OFF_HAND).getCount();
                String countleftString = countleft + "x";
                int countright = target.getHeldItem(EnumHand.MAIN_HAND).getCount();
                if (countleft > 0) {
                    RenderHelper2.drawRainbowRound(x + this.widthTH + 5, y + this.heightTH / 2 - this.heightTH / 4 - 1, 27, this.heightTH / 2 + 3, 3, true, true, true, true, 2, 4);
                    Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + this.widthTH + 5 + 3 - 2, (int) y + heightTH / 2 - 5 - 2); // картинка
                    MWFont.GREYCLIFFCF_MEDIUM.get(16).draw(countleft + "", x + this.widthTH + 5 + 15  / 2 + 10, y + heightTH / 2 + 3, -1);
                }
            }
            if (TargetHUD.targetHudMode.getCurrentMode().equalsIgnoreCase("Type-3")) {
                widthTH = 130;
                heightTH = 52;

                float x = (float) (this.x), y = (float) (this.y);
                Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE, gradientColor5 = Color.WHITE, gradientColor6 = Color.WHITE;
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, new Color(16, 16, 37), new Color(64, 37, 91), true);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, new Color(16, 16, 37), new Color(69, 41, 101), true);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, new Color(16, 16, 37), new Color(71, 41, 101), true);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, new Color(16, 16, 37), new Color(84, 51, 119), true);
                gradientColor5 = ColorUtil.interpolateColorsBackAndForth(15, 180, new Color(30, 30, 70), new Color(74, 47, 110), true);
                gradientColor6 = ColorUtil.interpolateColorsBackAndForth(15, 270, new Color(34, 34, 79), new Color(73, 43, 103), true);

                RoundedUtil.drawGradientRound(x, y, this.width - 8, this.height - 8, 3, gradientColor4, gradientColor1, gradientColor3, gradientColor2);
                drawTypeHead((int) (x + 3), (int) (y + 3), 25, 25);
                CustomFont nickFont = MWFont.MONTSERRAT_BOLD.get(14);
                CustomFont infoFont = MWFont.MONTSERRAT_SEMIBOLD.get(13);
                CustomFont healthFont = MWFont.MONTSERRAT_REGULAR.get(19);
                nickFont.draw(target.getName(), x + 29, y + nickFont.getHeight(), new Color(221, 221, 221).getRGB());
                infoFont.draw("Distance - " + String.format("%.1f", target.getDistanceToEntity(Minecraft.player)), x + 29, y + 14 + infoFont.getHeight(), new Color(211, 211, 211).getRGB());
                infoFont.draw("HurtTime - " + target.hurtTime, x + 29, y + 20 + infoFont.getHeight(), new Color(211, 211, 211).getRGB());
                double healthWID = (target.getHealth() / target.getMaxHealth()) * 75;
                double MaxhealthWID = 80;
                healthBarWidth = (float) Interpolator.linear(healthBarWidth, healthWID, 2f / 20);
                RoundedUtil.drawGradientHorizontal(x + 3, y + 28 + infoFont.getHeight(), healthBarWidth, 10, 1.5F, gradientColor5.brighter().brighter().brighter(), gradientColor6.brighter().brighter());
                healthFont.draw("" + String.format("%.1f", target.getHealth()) + "HP", x + 5 + healthBarWidth, y + 30 + infoFont.getHeight(), new Color(211, 211, 211).getRGB());
                int countleft = target.getHeldItem(EnumHand.OFF_HAND).getCount();
                String countleftString = countleft + "x";
                int countright = target.getHeldItem(EnumHand.MAIN_HAND).getCount();
                String countrightString = countright + "x";
                if (countleft > 0) {
                    RoundedUtil.drawGradientRound(x + 125, y + heightTH - 37, 15 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(countleftString) + 2, 15, 2, gradientColor4, gradientColor1, gradientColor3, gradientColor2);
                    Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 126, (int) y + heightTH - 38); // картинка
                    MWFont.GREYCLIFFCF_MEDIUM.get(19).drawShadow(countleftString, x + 146 - 5, y + heightTH - 29, new Color(220, 220, 220).getRGB());
                }
                if (countright > 0) {
                    RoundedUtil.drawGradientRound(x - 18 - MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(countrightString), y + heightTH - 37, 15 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(countrightString), 15, 2, gradientColor4, gradientColor1, gradientColor3, gradientColor2);
                    Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.MAIN_HAND), (int) x - 18 - MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(countrightString), (int) y + heightTH - 38); // картинка
                    MWFont.GREYCLIFFCF_MEDIUM.get(19).drawShadow(countrightString, x - MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(countrightString) - 3, y + heightTH - 29, new Color(220, 220, 220).getRGB());

                }
                this.width = widthTH;
                this.height = heightTH;
            }
            if (TargetHUD.targetHudMode.getCurrentMode().equalsIgnoreCase("Type-1")) {
                widthTH = 140;
                heightTH = 14;
                float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) - 4, y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) - 4;
                x = this.x;
                y = this.y;
                GlStateManager.pushMatrix();
                glTranslatef(this.x, this.y, 0);
                glScalef(scale, scale, 1);
                glTranslatef(-this.x, -this.y, 0);

                //(value - min) / (max - min)

                float healthWid = ((target.getHealth() - 0) / (target.getMaxHealth())) * 65;
                MWFont.GREYCLIFFCF_MEDIUM.get(19).drawCenter(target.getName(), x + widthTH / 2, y + 5, -1);
                healthBarWidth = (float) Interpolator.linear(healthBarWidth, healthWid, 2f / 20);
                double хуй = x + widthTH / 2 - healthBarWidth - 2;
                double хуИ = y + 16;
                double хуВ = x + widthTH / 2 + healthBarWidth + 2;
                double хуХ = 9;
                двигающийсяХуй = Interpolator.linear(двигающийсяХуй, target.getHealth(), 2f / 20);
                if (HUD.testAstolfoColors.get()) {/*new Color(getHealthColor(target.getHealth(),target.getMaxHealth())),*/
                    //RenderHelper2.drawRainbowRound(this.x,this.y - 5, this.widthTH, 5, 1,true,true,true,2,4 );
                    RenderHelper2.renderBlurredShadow(new Color(getHealthColor(target.getHealth(), target.getMaxHealth())).darker(), хуй, хуИ, хуВ - хуй, хуХ, 10);
                    // DrawHelper.drawGlow(хуй,хуИ - 10,хуВ,хуХ + хуИ + 10,new Color(getHealthColor(target.getHealth(),target.getMaxHealth())).darker().getRGB());
                    RoundedUtil.drawGradientHorizontal2(x + widthTH / 2 - healthBarWidth, y + 16, x + widthTH / 2 + healthBarWidth, 9, 3, new Color(getHealthColor(target.getHealth(), target.getMaxHealth())), new Color(getHealthColor(target.getHealth(), target.getMaxHealth())).darker());
                    MWFont.MONTSERRAT_BOLD.get(16).drawCenter(String.format("%.1f", двигающийсяХуй), x + widthTH / 2, y + 19, -1);
                } else {
                    //RenderHelper2.drawRainbowRound(this.x,this.y - 5, this.widthTH, 5, 1,true,true,true,2,4 );
                    RenderHelper2.renderBlurredShadow(new Color(getHealthColor(target.getHealth(), target.getMaxHealth())).darker(), хуй, хуИ, хуВ - хуй, хуХ, 10);
                    // DrawHelper.drawGlow(хуй,хуИ - 10,хуВ,хуХ + хуИ + 10,new Color(getHealthColor(target.getHealth(),target.getMaxHealth())).darker().getRGB());
                    RoundedUtil.drawGradientHorizontal2(this.x + widthTH / 2 - healthBarWidth, y + 16, x + widthTH / 2 + healthBarWidth, 9, 3, new Color(getHealthColor(target.getHealth(), target.getMaxHealth())), new Color(getHealthColor(target.getHealth(), target.getMaxHealth())).darker());
                    MWFont.MONTSERRAT_BOLD.get(16).drawCenter(String.format("%.1f", двигающийсяХуй), x + widthTH / 2, y + 19, -1);

                }
                GlStateManager.popMatrix();
            }
            if (TargetHUD.targetHudMode.getCurrentMode().equalsIgnoreCase("Bordered")) {
                float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)), y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0));
                //RectHelper.drawBorderedRect(x,y,60, 40,2, 2,2, true);
                //target = mc.player
                //this.healthBarWidth = MathHelper.clamp(target.getHealth() * 5, 0.0F,56.0F);
                //double healthWid = target.getHealth() / target.getMaxHealth() * 96.0f;
                //healthWid = MathHelper.clamp(healthWid, 0.0, 96.0);
                //this.healthBarWidth = (float) Interpolator.linear(this.healthBarWidth, (double) ((float) healthWid), (double) (10.0f / (float) Minecraft.getDebugFPS()));
                if (target == null && Minecraft.player.getDistanceToEntity(target) <= range || target.isDead) {
                    target = Minecraft.player;
                }
                double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
                double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, 0, 1);
                GlStateManager.pushMatrix();
                //GlStateManager.translate(scale,scale,scale);
                GlStateManager.scale(scale, scale, scale);
                if (TargetHUD.targetHudGlow.get()) {
                    RenderHelper2.renderBlurredShadow(new Color(0x3042FF), x, y - 1, 95, 27, 17);
                }
                RoundedUtil.drawRoundOutline(x - 0.5F, y - 0.5F, 95 + 1.1F, 26 + 1, 4, 1, new Color(42, 42, 42, 231), new Color(0x3042FF));
                if (TargetHUD.targetHudGradient.get()) {
                    RoundedUtil.drawGradientRound(x, y, 95, 26.5F, 4, new Color(42, 42, 42, 231), new Color(0x101957), new Color(PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB()), new Color(101, 31, 31));
                }
                MWFont.MONTSERRAT_BOLD.get(14).draw(target.getName(), x + 26.5F, y + 5.5F, target.hurtTime > 0 ? new Color(255, 193, 193, 255).getRGB() : new Color(Color.WHITE.getRGB()).getRGB());
                RoundedUtil.drawRound(x + 26.5F, y + 18.0F, 60, 5F, 1, new Color(58, 58, 58, 223));
                RoundedUtil.drawGradientRound(x + 26.5F, y + 18.5F, healthBarWidth / 2, 4, 1, new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), PaletteHelper.astolfoarray2(false, (int) y * 4), PaletteHelper.astolfoarray2(false, (int) y * 4));
                //System.out.println(target.hurtTime);
                //System.out.println(healthBarWidth / 2);
                //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                try {
                    for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
                        if (target instanceof EntityPlayer || target != null) {
                            if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                                if (targetHead != null) {
                                    Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                } else {
                                    Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                                }

                                float hurtPercent = getHurtPercent(target);
                                GL11.glPushMatrix();
                                GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                                Gui.drawScaledCustomSizeModalRect(x + 4.5F, y + 3.5F, 8.0f, 8.0f, 8, 8, 20, 20, 64.0F, 64.0F);
                                GL11.glPopMatrix();
                                GlStateManager.bindTexture(0);
                            } else {
                                //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                            }
                        } else {
                            RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
                        }
                        GL11.glDisable(3089);
                    }
                } catch (Exception уч) {
                    org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/combat.png"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
                }
                GlStateManager.popMatrix();
            }
            if (TargetHUD.targetHudMode.currentMode.equals("Up-Gradient")) {
                float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)), y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0));
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                if (TargetHUD.targetHudGlow.get()) {
                    RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x, y, 140 - 35, 31, 7);
                }
                if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("Gradient")) {
                    RoundedUtil.drawGradientRound(x, y, 105, 31, 6, DrawHelper.fade(new Color(TargetHUD.right_down_color.getColor()), 10, 100), DrawHelper.fade(new Color(TargetHUD.right_up_color.getColor()), 30, 100), DrawHelper.fade(TargetHUD.left_down_color.getColorc(), 60, 100), DrawHelper.fade(TargetHUD.left_up_color.getColorc(), 90, 100));
                } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("blur")) {
                    RoundedUtil.drawRound(x, y, 105, 31, 6, true, new Color(31, 31, 31, 120));
                } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("transparent")) {
                    RoundedUtil.drawRound(x, y, 105, 31, 6, new Color(31, 31, 31, 120));
                }
                double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
                double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, 0, 1);
                for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
                    if (target instanceof EntityPlayer || target != null) {
                        if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                            if (targetHead != null) {
                                Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            } else {
                                Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                            }

                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect(x + 4.5F, y + 3.5F, 8.0f, 8.0f, 8, 8, 26, 26, 64.0F, 64.0F);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        } else {
                            //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                        }
                    } else {
                        RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
                    }
                    GL11.glDisable(3089);
                }

                MWFont.MONTSERRAT_BOLD.get(14).draw(target.getName(), x + 26.5F + 5, y + 5.5F, target.hurtTime > 0 ? new Color(255, 193, 193, 255).getRGB() : new Color(Color.WHITE.getRGB()).getRGB());
                RoundedUtil.drawRound(x + 26.5F + 7, y + 24.5F, 60, 2, 1, new Color(58, 58, 58, 223));
                RoundedUtil.drawGradientRound(x + 26.5F + 7, y + 24.5F, healthBarWidth / 2, 2, 1, new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), PaletteHelper.astolfoarray2(false, (int) y * 4), PaletteHelper.astolfoarray2(false, (int) y * 4));
                int prec = (int) (target.getHealth() / target.getMaxHealth() * 100F);
                String precentHp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100F, 1) + "%";
                precentHp = prec + "%";
                float posrightHELPER = Math.max(26.5F + healthBarWidth / 2, 26.5F);
                float positionHELPER2 = 0;
                if (posrightHELPER < 32) {
                    positionHELPER2 = 32;
                } else if (posrightHELPER > 66) {
                    positionHELPER2 = 66.7F;
                } else {
                    positionHELPER2 = posrightHELPER;
                }
                MWFont.GREYCLIFFCF_MEDIUM.get(19).draw(precentHp, x + positionHELPER2, y + 16.5F, -1);
                //System.out.println(Math.max(26.5F + healthBarWidth / 2,26.5F));


                //mc.getRenderItem().renderItemOverlays(mc.robotoRegularFontRender, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 145, (int) y - 7); // кол-во
                int count = target.getHeldItem(EnumHand.OFF_HAND).getCount();
                String countString = count + "x";
                if (target.getHeldItem(EnumHand.OFF_HAND) != null && count != 0) {
                    if (count > 1) {
                        if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("Gradient")) {
                            RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawGradientRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)), 20, 2, DrawHelper.fade(new Color(TargetHUD.right_down_color.getColor()), 10, 100), DrawHelper.fade(new Color(TargetHUD.right_up_color.getColor()), 30, 100), DrawHelper.fade(TargetHUD.left_down_color.getColorc(), 60, 100), DrawHelper.fade(TargetHUD.left_up_color.getColorc(), 90, 100));
                        } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("blur")) {
                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)), 20, 2, true, new Color(31, 31, 31, 120));

                        } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("transparent")) {
                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)), 20, 2, false, new Color(31, 31, 31, 120));

                        }
                        Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 150 - 35, (int) y + 9); // картинка
                        MWFont.GREYCLIFFCF_MEDIUM.get(19).draw(countString, x + 95 + (MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - count > 10 ? 39 : 35), y + 17, new Color(220, 220, 220).getRGB());
                    } else {
                        if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("Gradient")) {
                            RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawGradientRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - 9, 20, 2, DrawHelper.fade(new Color(TargetHUD.right_down_color.getColor()), 10, 100), DrawHelper.fade(new Color(TargetHUD.right_up_color.getColor()), 30, 100), DrawHelper.fade(TargetHUD.left_down_color.getColorc(), 60, 100), DrawHelper.fade(TargetHUD.left_up_color.getColorc(), 90, 100));
                        } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("blur")) {
                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - 9, 20, 2, new Color(31, 31, 31, 120));
                        } else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("transparent")) {
                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - 9, 20, 2, new Color(31, 31, 31, 120));
                        }
                        Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 150 - 35, (int) y + 9); // картинка
                        //mc.montserrat_semibold16.drawString(countString, x + 160 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) - count > 10 ? 39 : 35, y + 15, new Color(231, 231, 231).getRGB());

                    }
                }
                GlStateManager.popMatrix();
            }
            if (TargetHUD.targetHudMode.currentMode.equals("Type-2")) {
                widthTH = 136;
                heightTH = 34;
                float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) + 4, y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) + 4;
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                if (TargetHUD.targetHudGlow.get()) {
                    //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x, y, 140 - 35, 31, 7);
                }
                Color grad11 = Color.WHITE;
                Color grad22 = Color.WHITE;
                Color grad1 = Color.WHITE;
                Color grad2 = Color.WHITE;
                for (int i = 0; i < 100; i++) {
                    grad11 = new Color(ColorUtil.interpolateColorsBackAndForth(33, 10, ColorUtil.skyRainbow(11, i), ColorUtil.skyRainbow(11, i).darker().darker(), true).getRGB());
                    grad22 = new Color(ColorUtil.interpolateColorsBackAndForth(33, 1150, ColorUtil.skyRainbow(11, i).darker().darker(), ColorUtil.skyRainbow(11, i), true).getRGB());

                }
                grad1 = grad11;
                grad2 = grad22;


                RoundedUtil.drawGradientVertical(x, y, 105, 26, 2, DrawHelper.getColorWithOpacity(grad1, 205), DrawHelper.getColorWithOpacity(grad2, 205));
                RoundedUtil.drawRoundOutline(x - 1, y - 1, 105 + 2, 26 + 2, 2, 0.1F, new Color(31, 31, 31, 1), DrawHelper.getColorWithOpacity(grad2, 215));


                double healthWid = (target.getHealth() / target.getMaxHealth() * 140);
                double healthFullWid = (target.getMaxHealth() / target.getMaxHealth() * 140);
                double fullhealthWid = (20 / 20 * 140);
                double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, 0, 1);


                //RenderHelper2.renderBlurredShadow(new Color(31,31,31), x - 2,y - 2,105 + 4,26 + 4,14);
                RenderHelper2.renderBlurredShadow(new Color(31, 31, 31), x + 22.5F + 1, y + 16.5F, 75, 5, 10);
                MWFont.MONTSERRAT_SEMIBOLD.get(13).draw(target.getName(), x + 26.5F, y + 5.5F, target.hurtTime > 0 ? new Color(255, 193, 193, 255).getRGB() : new Color(Color.WHITE.getRGB()).getRGB());
                RoundedUtil.drawRound(x + 22.5F + 5, y + 16.5F, (float) (healthFullWid / 2), 5, 2, new Color(58, 58, 58, 223));
                //RoundedUtil.drawGradientRound(x + 26.5F + 7, y + 21.5F, healthBarWidth / 2, 5, 1, new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), new Color(PaletteHelper.rainbow(300, 1, 1).getRGB()), PaletteHelper.astolfoarray2(false, (int) y * 4), PaletteHelper.astolfoarray2(false, (int) y * 4));
                RoundedUtil.drawGradientHorizontal(x + 22.5F + 4, y + 16.5F, healthBarWidth / 2, 5, 1.5f, grad1, grad2);
                MWFont.MONTSERRAT_SEMIBOLD.get(13).drawCenter("" + (int) target.getHealth(), (float) (x + 22.5F + (fullhealthWid / 2) / 2) + 4, y + 16.5F + 1.5F, -1);
                drawAstolfoHead();
                int prec = (int) (target.getHealth() / target.getMaxHealth() * 100F);
                String precentHp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100F, 1) + "%";
                precentHp = prec + "%";
                float posrightHELPER = Math.max(26.5F + healthBarWidth / 2, 26.5F);
                float positionHELPER2 = 0;
                if (posrightHELPER < 32) {
                    positionHELPER2 = 32;
                } else if (posrightHELPER > 66) {
                    positionHELPER2 = 66.7F;
                } else {
                    positionHELPER2 = posrightHELPER;
                }

                //mc.montserrat_semibold16.drawString(precentHp, x + positionHELPER2, y + 16.5F, -1);

                //System.out.println(Math.max(26.5F + healthBarWidth / 2,26.5F));


                //mc.getRenderItem().renderItemOverlays(mc.robotoRegularFontRender, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 145, (int) y - 7); // кол-во
                int count = target.getHeldItem(EnumHand.OFF_HAND).getCount();
                String countString = count + "x";
                if (target.getHeldItem(EnumHand.OFF_HAND) != null && count != 0) {
                    if (count > 1) {
                        widthTH = 153;
                        float xItem = x + 148 - 35 - 3;
                        float yItem = y + 6;
                        float widthItem = 25 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count));
                        float heightItem = 20;
                        float radiusRound = 2;
                        RoundedUtil.drawGradientVertical(xItem, yItem, widthItem, heightItem, radiusRound, DrawHelper.getColorWithOpacity(grad1, 205), DrawHelper.getColorWithOpacity(grad2, 205));
                        RoundedUtil.drawRoundOutline(xItem - 1, yItem - 1, widthItem + 2, heightItem + 2, radiusRound, 0.1F, new Color(31, 31, 31, 1), DrawHelper.getColorWithOpacity(grad2, 215));

                        //RoundedUtil.drawRoundOutline(x - 1,y - 1,105 + 2,26 + 2,2,0.1F,new Color(31,31,31,1),DrawHelper.getColorWithOpacity(grad2,215));

//                        if(TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("Gradient")) {
//                            RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawGradientRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)), 20, 2, DrawHelper.fade(new Color(TargetHUD.right_down_color.getColor()), 10, 100), DrawHelper.fade(new Color(TargetHUD.right_up_color.getColor()), 30, 100), DrawHelper.fade(TargetHUD.left_down_color.getColorc(), 60, 100), DrawHelper.fade(TargetHUD.left_up_color.getColorc(), 90, 100));
//                        }else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("blur")) {
//                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)), 20, 2,true, new Color(31, 31, 31, 120));
//
//                        }else if (TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("transparent")) {
//                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)), 20, 2,false, new Color(31, 31, 31, 120));
//
//                        }

                        DrawHelper.startSmooth();
                        Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 150 - 35 - 2, (int) y + 9); // картинка
                        DrawHelper.endSmooth();
                        MWFont.GREYCLIFFCF_MEDIUM.get(19).drawShadow(countString, x + 95 - 3 + (MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - count > 10 ? 39 : 35), y + 17, new Color(220, 220, 220).getRGB());
                    } else {
                        widthTH = 136;
                        float xItem = x + 148 - 35 - 3, yItem = y + 6, widthItem = 23 + MWFont.GREYCLIFFCF_MEDIUM.get(19).getWidth(String.valueOf(count)) - 9, heightItem = 20, radiusRound = 2;
                        RoundedUtil.drawGradientVertical(xItem, yItem, widthItem, heightItem, radiusRound, DrawHelper.getColorWithOpacity(grad1, 205), DrawHelper.getColorWithOpacity(grad2, 205));

//                        if(TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("Gradient")) {
//                            RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawGradientRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) - 9, 20, 2, DrawHelper.fade(new Color(TargetHUD.right_down_color.getColor()), 10, 100), DrawHelper.fade(new Color(TargetHUD.right_up_color.getColor()), 30, 100), DrawHelper.fade(TargetHUD.left_down_color.getColorc(), 60, 100), DrawHelper.fade(TargetHUD.left_up_color.getColorc(), 90, 100));
//                        }else  if(TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("blur")) {
//                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) - 9, 20, 2, new Color(31, 31, 31, 120));
//                        }else  if(TargetHUD.thBackgroundMode.currentMode.equalsIgnoreCase("transparent")) {
//                            //RenderHelper2.renderBlurredShadow(new Color(new Color(TargetHUD.right_down_color.getColor()).getRGB()), x + 148 - 35, y + 6, 21 - 9 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) + 2, 20, 7);
//                            RoundedUtil.drawRound(x + 148 - 35, y + 6, 23 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) - 9, 20, 2, new Color(31, 31, 31, 120));
//                        }
                        Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 150 - 37 - 2, (int) y + 9); // картинка
                        //mc.montserrat_semibold16.drawString(countString, x + 160 + mc.montserrat_semibold16.getStringWidth(String.valueOf(count)) - count > 10 ? 39 : 35, y + 15, new Color(231, 231, 231).getRGB());

                    }
                } else {
                    widthTH = 113;
                    heightTH = 34;
                }
                //RenderHelper2.drawRainbowBox(x,y,x + 200,y + 200, 445,false, true,false);
                //RenderHelper2.renderBlurredShadow(DrawHelper.getColorWithOpacity(grad2,95),x - 1,y - 1,105 + 2,26 + 2,14);
                GlowUtil.drawBlurredGlow(x - 1 + 0.4, y - 14, x - 1 + 105 + 2, y - 1 + 26 + 2 + 14, DrawHelper.getColorWithOpacity(grad2, 255).getRGB());

                GlStateManager.popMatrix();
            }
            if (TargetHUD.targetHudMode.currentMode.equalsIgnoreCase("Moon2") && Minecraft.player.getDistanceToEntity(target) <= range && target != null) {
                GlStateManager.pushMatrix();
                scalehelper = 1.0F;
                if (TargetHUD.targetHudAnimation.getBoolValue()) {
                    if (TargetHUD.targetHudAnimationMode.getCurrentMode().equalsIgnoreCase("Scale")) {
                        scale = AnimationHelper.animation(scale, scalehelper, (float) (0.001f * Feature.deltaTime()));
                        //GL11.glScalef(scale, scale, scale);
                        //GlStateManager.translate(scale,scale,scale);
                    } else if (TargetHUD.targetHudAnimationMode.getCurrentMode().equalsIgnoreCase("Translate")) {

                    }
                }
                float x = this.x - 120, y = this.y + 15;
                EntityLivingBase entityPlayer = target;
                target = target;
                enemyNickname = target.getName();
                enemyHP = target.getHealth();
                enemyDistance = 0.0;
                double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
                double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);

                double helath = (entityPlayer.getHealth() / entityPlayer.getMaxHealth() * 355);
                hui = MathHelper.lerp(healthBarWidth, (float) hpWidth, 7 * Feature.deltaTime());

                int namepl = FontStorage.circleregular.getWidth(enemyNickname);
                String ping;
                if (Minecraft.player.ticksExisted % 20 == 0) {
                }

                //RectHelper.drawSmoothRect(x + 123.5f, y - 11.5f, x + 270, y + 38f, new Color(31, 31, 31, 180).getRGB());
                RectHelper.drawRectWithGlow(x + 123.5f, y - 11.5f, x + 270, y + 29f, 5, 11, new Color(31, 31, 31, 180));
                //Gui.drawRect(x + 167.0f, y + 23.0f, x + 261.0f, y + 33.3f, new Color(31, 31, 31, 113).getRGB());
                RectHelper.drawGradientRect2((int) x + 172.0f, (int) y + 22.0f, (int) x + 205.0f + healthBarWidth - 44, (int) y + 24.0f, 15, 15);
                //RectHelper.drawRectWithGlow(x + 172.0f, y + 29.0f, y + 205.0f + this.healthBarWidth - 44,y + 26.0f,5,15, new Color(PaletteHelper.fadeColor(new Color(Aura.targetMoon2Color1.getColorValue()).getRGB() , new Color(Aura.targetMoonColor2.getColorValue()).getRGB() , 3.0f))) ;
                //mc.circleregular.drawString(Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + " ms", x + 169 + (namepl),
                //y-5, -1);


                String pvpState = "HP: " + String.format("%.1f", entityPlayer.getHealth()) + " | " + "Ground:" + entityPlayer.onGround;
                MWFont.MONTSERRAT_BOLD.get(17).draw(pvpState, (int) x + 164, (int) y + 9f, -1);


                if (Minecraft.player.getDistanceToEntity(entityPlayer) >= 10) {
                    healthBarWidth = 0;
                    healthWid = 0;
                }
                healthWid = MathHelper.clamp(healthWid, 0, 100);
                if (Minecraft.player.getDistanceToEntity(entityPlayer) >= 10) {
                    healthBarWidth = 0;
                    healthWid = 0;
                }
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, 0, 0);
                if (Minecraft.player.getDistanceToEntity(entityPlayer) >= 10) {
                    healthBarWidth = 0;
                    healthWid = 0;
                }
                healthBarWidth = MathHelper.clamp(healthBarWidth, 0, 110);
                if (Minecraft.player.getDistanceToEntity(entityPlayer) >= 10) {
                    healthBarWidth = 0;
                    healthWid = 0;
                }
                String dist = String.format("%.1f ", Minecraft.player.getDistanceToEntity(entityPlayer));
                String healthh = String.format("%.1f ", entityPlayer.getHealth());
                //RectHelper.drawGradientRect(x + 167.0f, y + 23.0f, x + 55 + health , y + 33.0f, targetMoonColor1.getColorValue(), targetMoonColor2.getColorValue());
                //RectHelper.drawRectWithGlow(x + 167.0f, y + 23.0f, x + health, y + 33.0f, 5, 15, new Color(targetMoonColor1.getColorValue()));
                //RectHelper.drawRectBetter(x + 30, y + 48, (float) healthWid, 8, new Color(color).getRGB());
                //RectHelper.drawRectBetter(x + 167.0f, y + 23.0f, (float) healthWid, 8, targetMoonColor1.getColorValue());
                //RectHelper.drawGradientRect1(x + 167.0f, y + 23.0f, x + 208.0f + this.healthBarWidth - 44, y + 33.0f, Aura.targetMoonColor1.getColorValue(), Aura.targetMoonColor2.getColorValue());
                RectHelper.drawRectWithGlow(x + 167, y + 20.0f, x + 208.0f + healthBarWidth - 44, y + 24.0f, 5, 20, new Color(PaletteHelper.fadeColor(new Color(Aura.targetMoon2Color1.getColorValue()).getRGB(), new Color(Aura.targetMoonColor2.getColorValue()).getRGB(), 3.0f)));
                //Minecraft.getMinecraft().montserratLight.drawString("" + healthh, x + 185.0f + 31f - mc.fontRendererObj.getStringWidth(healthh) / 2.0f, y + 26f, -1);

                double cooldownPrecentage = MathHelper.clamp(Minecraft.player.getCooledAttackStrength(1), 0.0, 1.0);
                double cooldownWidth = 5.0 * cooldownPrecentage;
                //рендер айтема в руках таргета ВНИЗУ
                /*
                mc.getRenderItem().renderItemOverlays(mc.circleregularSmall, entityPlayer.getHeldItem(EnumHand.OFF_HAND), (int) x + 180, (int) y + 5);
                mc.getRenderItem().renderItemIntoGUI(entityPlayer.getHeldItem(EnumHand.OFF_HAND), (int) x + 180, (int) y + 7);

                mc.getRenderItem().renderItemOverlays(mc.circleregularSmall, entityPlayer.getHeldItem(EnumHand.MAIN_HAND), (int) x + 167,
                        (int) y + 5);
                mc.getRenderItem().renderItemIntoGUI(entityPlayer.getHeldItem(EnumHand.MAIN_HAND), (int) x + 167, (int) y + 7);
                Minecraft.getMinecraft().circleregularSmall.drawStringWithShadow("\u2764",
                        x + 128.0f + 46.0f + Minecraft.getMinecraft().circleregularSmall.getStringWidth(healthh), y + 19.5f,
                        ClientHelper.getClientColor().getRGB());
                 */
                MWFont.MONTSERRAT_BOLD.get(18).drawShadow(entityPlayer.getName() + Formatting.RESET, x + 164, y - 5.0f, -1);

                //mc.motBold.drawStringWithShadow("Distance: " + String.format("%.1f ", mc.player.getDistanceToEntity(entityPlayer)) + " | " + pvpState, x + 169, y + 14f, -1);
                try {
                    drawHeadd(Objects.requireNonNull(Helper.mc.getConnection()).getPlayerInfo(entityPlayer.getUniqueID()).getLocationSkin(), (int) (x + 127), (int) (y - 6), entityPlayer.hurtTime > 0 ? new Color(255, 0, 0, 140) : Color.WHITE);
                } catch (Exception ignored) {
                    org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/combat.png"), x + 127, y - 6, 35, 35, entityPlayer.hurtTime > 0 ? Color.RED : Color.WHITE);
                }
                //GL11.glScalef(1.0F,1.0F,1.0F);
                GlStateManager.popMatrix();
            } else if (TargetHUD.targetHudMode.currentMode.equalsIgnoreCase("Moon2"))
                scalehelper = 0.0F;
        } else if (TargetHUD.targetHudMode.currentMode.equalsIgnoreCase("Minecraft")) {
            if (target == null)
                return;
            float x = this.x, y = this.y;
            GlStateManager.pushMatrix();
            RectHelper.drawOutlineRect(x - 2, y - 7, 155, 38, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.font.drawStringWithShadow(target.getName(), (this.x + 37), this.y - 2, -1);
            for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
                if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int) x, (int) y - 5, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            Minecraft.getTextureManager().bindTexture(new Namespaced("textures/gui/icons.png"));
            int i = 0;
            while ((float) i < target.getMaxHealth() / 2.0F) {
                Minecraft.ingameGUI.drawTexturedModalRect((this.x + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), this.y + 9, 16, 0, 9, 9);
                ++i;
            }
            i = 0;
            while ((float) i < target.getHealth() / 2.0F) {
                Minecraft.ingameGUI.drawTexturedModalRect((this.x + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), this.y + 9, 52, 0, 9, 9);
                ++i;
            }

            int i3 = target.getTotalArmorValue();
            for (int k3 = 0; k3 < 10; ++k3) {
                if (i3 > 0) {
                    int l3 = (this.x + 36) + k3 * 8;
                    if (k3 * 2 + 1 < i3) {
                        Minecraft.ingameGUI.drawTexturedModalRect(l3, this.y + 20, 34, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 == i3) {
                        Minecraft.ingameGUI.drawTexturedModalRect(l3, this.y + 20, 25, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 > i3) {
                        Minecraft.ingameGUI.drawTexturedModalRect(l3, this.y + 20, 16, 9, 9, 9);
                    }
                }
            }
            GlStateManager.popMatrix();
        } else if (TargetHUD.targetHudMode.currentMode.equalsIgnoreCase("Flux")) {
            float x = this.x, y = this.y;

            double armorWid = (target.getTotalArmorValue() * 6);
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);

            RectHelper.drawRectBetter(x, y, 125, 55, new Color(39, 39, 37, 235).getRGB());

            String pvpState = "";
            if (Minecraft.player.getHealth() == target.getHealth()) {
                pvpState = "Finish Him!";
            } else if (Minecraft.player.getHealth() < target.getHealth()) {
                pvpState = "Losing Fight";
            } else if (Minecraft.player.getHealth() > target.getHealth()) {
                pvpState = "Winning Fight";
            }
            if (!target.getName().isEmpty()) {
                FontStorage.robotoRegularFontRender.drawShadow(MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 38, y + 6, -1);
                MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow(pvpState, x + 38, y + 17, -1);
            }

            for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
                if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect(x + 1.5F, y + 1.5F, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }

            /* HEALTH BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 39, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 39, healthWid, 4, new Color(2, 145, 98, 255).getRGB());

            /* ARMOR BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 47, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 47, armorWid, 4, new Color(65, 138, 195, 255).getRGB());
        }
    }
    public static Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    public static int getHealthColor(float health, float maxHealth) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3, 1, 0.8f) | 0xFF000000;
    }

    public static EntityLivingBase target;

    public void drawAstolfoHead() {
        float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) + 4, y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) + 4;

        for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
            if (target instanceof EntityPlayer || target != null) {
                if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                    if (targetHead != null) {
                        Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    } else {
                        Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                    }

                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect(x + 4.5F, y + 3.5F, 8.0f, 8.0f, 8, 8, 20, 20, 64.0F, 64.0F);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                } else {
                    //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                }
            } else {
                RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
            }
            GL11.glDisable(3089);
        }
    }

    public void drawTypeHead(int x, int y, int width, int height) {
        //float x = (float) (this.x / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) + 4, y = (float) (this.y / (TargetHUD.targetHudAnimation.get() && TargetHUD.targetHudAnimationMode.currentMode.equalsIgnoreCase("Scale") ? scale : 1.0)) +4;

        for (NetworkPlayerInfo targetHead : Minecraft.player.connection.getPlayerInfoMap()) {
            if (target instanceof EntityPlayer || target != null) {
                if (Minecraft.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == Minecraft.player) {
                    if (targetHead != null) {
                        Minecraft.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    } else {
                        Minecraft.getTextureManager().bindTexture(new Namespaced("moonware/icons/xuss.jpg"));
                    }

                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0F, 64.0F);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                } else {
                    //RenderHelper2.drawImage(new ResourceLocation("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, entityPlayer.hurtTime > 0 ? Color.WHITE : Color.WHITE);
                }
            } else {
                RenderHelper2.drawImage(new Namespaced("moonware/icons/xuss.jpg"), x + 4.5F, y + 5.5F, 20, 20, Color.WHITE);
            }
            GL11.glDisable(3089);
        }
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? Minecraft.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

    public EntityEquipmentSlot getEquipmentSlot() {
        return armorType;
    }

    public void drawHead(Namespaced skin, int x, int y) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, 37, 37, 64, 64);
    }

    public void drawHeadd(Namespaced skin, int x, int y, Color Color) {
        GL11.glColor4f(1, 1, 1, 1);
        RectHelper.setColor(Color.getRGB());
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, 35, 35, 64, 64);
    }

    public void drawHeaddw(Namespaced skin, int x, int y, int width, int height, Color Color) {
        GL11.glColor4f(1, 1, 1, 1);
        RectHelper.setColor(Color.getRGB());
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, width, height, 64, 64);
    }

    public void drawHeadWithCustomSize(Namespaced skin, int x, int y, int width, int height, Color Color) {
        GL11.glColor4f(1, 1, 1, 1);
        RectHelper.setColor(Color.getRGB());
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, width, height, width - 6, height - 6);
    }

}
