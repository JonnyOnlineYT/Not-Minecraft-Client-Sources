package net.minecraft.client.gui.hud;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.border.WorldBorder;
import optifine.*;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.event.events.impl.render.EventRenderScoreboard;
import org.moonware.client.event.types.EventType;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.feature.impl.visual.Crosshair;
import org.moonware.client.feature.impl.visual.NoRender;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.HudManager;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.utils.FontStorage;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class IngameHud extends Gui {
    private static final Namespaced VIGNETTE_TEX_PATH = new Namespaced("textures/misc/vignette.png");
    private static final Namespaced WIDGETS_TEX_PATH = new Namespaced("textures/gui/widgets.png");
    private static final Namespaced PUMPKIN_BLUR_TEX_PATH = new Namespaced("textures/misc/pumpkinblur.png");
    private final Minecraft minecraft;
    private final ChatHud chatHud;
    private int updateCounter;
    private String actionBar = "";
    private int actionBarTime;
    private boolean actionBarAnimated;
    public float prevVignetteBrightness = 1.0F;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack = ItemStack.EMPTY;
    private final DebugHud debugHud;
    private final GuiSubtitleOverlay overlaySubtitle;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private final BossbarHud bossbarHud;
    private int titlesTimer;
    private String displayedTitle = "";
    private String displayedSubTitle = "";
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;

    public IngameHud(Minecraft minecraft) {
        this.minecraft = minecraft;
        debugHud = new DebugHud(minecraft);
        spectatorGui = new GuiSpectator(minecraft);
        chatHud = new ChatHud(minecraft);
        overlayPlayerList = new GuiPlayerTabOverlay(minecraft, this);
        bossbarHud = new BossbarHud(minecraft);
        overlaySubtitle = new GuiSubtitleOverlay(minecraft);
        setDefaultTitlesTimes();
    }

    public void setDefaultTitlesTimes() {
        titleFadeIn = 10;
        titleDisplayTime = 70;
        titleFadeOut = 20;
    }

    public void draw(float partialTick) {
        ScaledResolution sr = new ScaledResolution(minecraft);
        int i = sr.getScaledWidth();
        int j = sr.getScaledHeight();
        Font font = getFontRenderer();
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            renderVignette(Minecraft.player.getBrightness(), sr);
        } else {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        ItemStack helmet = Minecraft.player.inventory.armorItemInSlot(3);
        if (Minecraft.gameSettings.thirdPersonView == 0 && helmet.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            drawPumpkin(sr);
        }
        if (!Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
            float f = Minecraft.player.prevTimeInPortal + (Minecraft.player.timeInPortal - Minecraft.player.prevTimeInPortal) * partialTick;
            if (f > 0.0F) {
                drawPortal(f, sr);
            }
        }
        if (Minecraft.playerController.isSpectator()) {
            spectatorGui.renderTooltip(sr, partialTick);
        } else {
            drawHotbar(sr, partialTick);
        }
        NotificationManager.renderNotification(sr);
        EventManager.call(new EventRender2D(sr));

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(Gui.ICONS);
        GlStateManager.enableBlend();
        drawCrosshair(partialTick, sr);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.profiler.startSection("bossHealth");
        bossbarHud.draw();
        Minecraft.profiler.endSection();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getTextureManager().bindTexture(Gui.ICONS);
        if (Minecraft.playerController.shouldDrawHUD()) {
            renderPlayerStats(sr);
        }
        renderMountHealth(sr);
        GlStateManager.disableBlend();
        if (Minecraft.player.getSleepTimer() > 0) {
            Minecraft.profiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int j1 = Minecraft.player.getSleepTimer();
            float f1 = (float) j1 / 100.0F;
            if (f1 > 1.0F) f1 = 1.0F - (float) (j1 - 100) / 10.0F;
            int k = (int) (220.0F * f1) << 24 | 1052704;
            Gui.drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            Minecraft.profiler.endSection();
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int k1 = i / 2 - 91;
        if (Minecraft.player.isRidingHorse()) {
            renderHorseJumpBar(sr, k1);
        } else if (Minecraft.playerController.gameIsSurvivalOrAdventure()) {
            renderExpBar(sr, k1);
        }
        if (Minecraft.gameSettings.heldItemTooltips && !Minecraft.playerController.isSpectator()) {
            renderSelectedItem(sr);
        } else if (Minecraft.player.isSpectator()) {
            spectatorGui.renderSelectedItem(sr);
        }
        renderPotionEffects(sr);
        if (Minecraft.gameSettings.showDebugInfo) {
            debugHud.draw(sr);
        }
        if (actionBarTime > 0) {
            Minecraft.profiler.startSection("overlayMessage");
            float f2 = (float) actionBarTime - partialTick;
            int l1 = (int) (f2 * 255.0F / 20.0F);
            if (l1 > 255) l1 = 255;
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (i / 2), (float) (j - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                int l = 16777215;
                if (actionBarAnimated) {
                    l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }
                font.drawStringWithShadow(actionBar, -font.getStringWidth(actionBar) / 2, -4, l + (l1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            Minecraft.profiler.endSection();

        }
        overlaySubtitle.renderSubtitles(sr);
        if (titlesTimer > 0) {
            Minecraft.profiler.startSection("titleAndSubtitle");
            float f3 = (float) titlesTimer - partialTick;
            int i2 = 255;
            if (titlesTimer > titleFadeOut + titleDisplayTime) {
                float f4 = (float) (titleFadeIn + titleDisplayTime + titleFadeOut) - f3;
                i2 = (int) (f4 * 255.0F / (float) titleFadeIn);
            }
            if (titlesTimer <= titleFadeOut) {
                i2 = (int) (f3 * 255.0F / (float) titleFadeOut);
            }
            i2 = MathHelper.clamp(i2, 0, 255);
            if (i2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (i / 2), (float) (j / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int j2 = i2 << 24 & -16777216;
                font.drawString(displayedTitle, (float) (-font.getStringWidth(displayedTitle) / 2), -10.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                font.drawString(displayedSubTitle, (float) (-font.getStringWidth(displayedSubTitle) / 2), 5.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            Minecraft.profiler.endSection();
        }
        Scoreboard scoreboard = Minecraft.world.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(Minecraft.player.getName());
        if (scoreplayerteam != null) {
            int i1 = scoreplayerteam.getChatFormat().getIndex();
            if (i1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }
        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            EventRenderScoreboard e = new EventRenderScoreboard(EventType.PRE);
            EventManager.call(e);
            if (!(MoonWare.featureManager.getFeatureByClass(org.moonware.client.feature.impl.visual.Scoreboard.class).getState() && org.moonware.client.feature.impl.visual.Scoreboard.noScore.getBoolValue())) {
                renderScoreboard(scoreobjective1, sr);
            }
            EventRenderScoreboard event2 = new EventRenderScoreboard(EventType.POST);
            EventManager.call(event2);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float) (j - 48), 0.0F);
        Minecraft.profiler.startSection("chat");
        chatHud.drawChat(updateCounter);
        Minecraft.profiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
        if (Minecraft.gameSettings.keyBindPlayerList.isKeyDown() && (!Minecraft.isIntegratedServerRunning() || Minecraft.player.connection.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
            overlayPlayerList.updatePlayerList(true);
            overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        } else {
            overlayPlayerList.updatePlayerList(false);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        HudManager.draw(partialTick);
    }

    private void drawCrosshair(float partialTick, ScaledResolution res) {
        GameSettings gamesettings = Minecraft.gameSettings;
        if (gamesettings.thirdPersonView == 0) {
            if (Minecraft.playerController.isSpectator() && Minecraft.pointedEntity == null) {
                RayTraceResult raytraceresult = Minecraft.objectMouseOver;
                if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                    return;
                }
                BlockPos blockpos = raytraceresult.getBlockPos();
                IBlockState iblockstate = Minecraft.world.getBlockState(blockpos);
                if (!ReflectorForge.blockHasTileEntity(iblockstate) || !(Minecraft.world.getTileEntity(blockpos) instanceof IInventory)) {
                    return;
                }
            }
            int l = res.getScaledWidth();
            int i1 = res.getScaledHeight();
            if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !Minecraft.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (l / 2), (float) (i1 / 2), zLevel);
                Entity entity = Minecraft.getRenderViewEntity();
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTick, -1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTick, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(-1.0F, -1.0F, -1.0F);
                OpenGlHelper.renderDirections(10);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.enableAlpha();
                if (!MoonWare.featureManager.getFeatureByClass(Crosshair.class).getState()) {
                    drawTexturedModalRect(l / 2 - 7, i1 / 2 - 7, 0, 0, 16, 16);
                }
                if (Minecraft.gameSettings.attackIndicator == 1) {
                    float f = Minecraft.player.getCooledAttackStrength(0.0F);
                    boolean flag = false;

                    if (Minecraft.pointedEntity != null && Minecraft.pointedEntity instanceof EntityLivingBase && f >= 1.0F) {
                        flag = Minecraft.player.getCooldownPeriod() > 5.0F;
                        flag = flag & Minecraft.pointedEntity.isEntityAlive();
                    }

                    int i = i1 / 2 - 7 + 16;
                    int j = l / 2 - 8;

                    if (flag) {
                        drawTexturedModalRect(j, i, 68, 94, 16, 16);
                    } else if (f < 1.0F) {
                        int k = (int) (f * 17.0F);
                        drawTexturedModalRect(j, i, 36, 94, 16, 4);
                        drawTexturedModalRect(j, i, 52, 94, k, 4);
                    }
                }
            }
        }
    }

    protected void renderPotionEffects(ScaledResolution resolution) {
        if (HUD.potion.get()) return;
        Collection<PotionEffect> collection = Minecraft.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            Minecraft.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;
            Iterator iterator = Ordering.natural().reverse().sortedCopy(collection).iterator();
            while (true) {
                PotionEffect potioneffect;
                Potion potion;
                boolean flag;
                while (true) {
                    if (!iterator.hasNext()) {
                        return;
                    }
                    potioneffect = (PotionEffect) iterator.next();
                    potion = potioneffect.getPotion();
                    flag = potion.hasStatusIcon();
                    if (!Reflector.ForgePotion_shouldRenderHUD.exists()) {
                        break;
                    }
                    if (Reflector.callBoolean(potion, Reflector.ForgePotion_shouldRenderHUD, potioneffect) && !HUD.potion.get()) {
                        Minecraft.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                        flag = true;
                        break;
                    }
                }
                if (flag && potioneffect.doesShowParticles()) {
                    int k = resolution.getScaledWidth();
                    int l = 1;
                    int i1 = potion.getStatusIconIndex();
                    if (potion.isBeneficial()) {
                        ++i;
                        k = k - 25 * i;
                    } else {
                        ++j;
                        k = k - 25 * j;
                        l += 26;
                    }
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    float f = 1.0F;
                    if (potioneffect.getIsAmbient()) {
                        drawTexturedModalRect(k, l, 165, 166, 24, 24);
                    } else {
                        drawTexturedModalRect(k, l, 141, 166, 24, 24);

                        if (potioneffect.getDuration() <= 200) {
                            int j1 = 10 - potioneffect.getDuration() / 20;
                            f = MathHelper.clamp((float) potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float) potioneffect.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp((float) j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }
                    GlStateManager.color(1.0F, 1.0F, 1.0F, f);
                    if (Reflector.ForgePotion_renderHUDEffect.exists()) {
                        if (potion.hasStatusIcon()) {
                            drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                        }
                        Reflector.call(potion, Reflector.ForgePotion_renderHUDEffect, k, l, potioneffect, minecraft, f);
                    } else {
                        drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }
                }
            }
        }
    }

    protected void drawHotbar(ScaledResolution sr, float partialTicks) {
        if (Minecraft.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
            EntityPlayer entityplayer = (EntityPlayer) Minecraft.getRenderViewEntity();
            ItemStack itemstack = entityplayer.getHeldItemOffhand();
            EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
            int i = sr.getScaledWidth() / 2;
            float f = zLevel;
            int j = 182;
            int k = 91;
            zLevel = -90.0F;

            if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getBoolValue()) {
                RectHelper.drawRectBetter(i - 91, sr.getScaledHeight() - 38, 182, 25, new Color(95, 95, 95, 100).getRGB());
                RectHelper.drawRectBetter(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 38 - 0.4F, 21, 25, new Color(55, 155, 215, 175).getRGB());
            } else if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && HUD.customHotbar.getBoolValue()) {
                if (HUD.testAstolfoColors.get()) {
                    //RectHelper.drawRectBetter(i - 91, sr.getScaledHeight() - 38, 182, 25, new Color(95, 95, 95, 100).getRGB());
                    RenderHelper2.drawRainbowRound(i - 91, sr.getScaledHeight() - 38, 182, 25, 2, true, false, true, true, 2, 4);
                } else {
                    //RoundedUtil.drawRound( );
                }


                RectHelper.drawRectBetter(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 38, 21, 25, new Color(55, 155, 215, 175).getRGB());
            } else {
                if (HUD.customHotbarA.get()) {
                    RenderHelper2.drawRainbowRound(i - 91, sr.getScaledHeight() - 22, 182, 19, 2, false, true, true, true, 2, 4);
                    RoundedUtil.drawRound(i - 91 - 1 + entityplayer.inventory.currentItem * 20 + 2 - 0.2F, sr.getScaledHeight() - 21, 21, 18, 1.5F, new Color(37, 37, 37, 157));
                } else {
                    drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                }
                drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }

            if (!itemstack.isEmpty() && !((HUD.rustHUD.getBoolValue() || HUD.customHotbar.get() || HUD.customHotbar.get()) && MoonWare.featureManager.getFeatureByClass(HUD.class).getState())) {
                CustomItems.setRenderOffHand(true);
                int l1 = sr.getScaledHeight() - 16 - 3;
                if (enumhandside == EnumHandSide.LEFT) {
                    renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                } else {
                    renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }

                CustomItems.setRenderOffHand(false);
            }

            zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();
            CustomItems.setRenderOffHand(false);

            for (int l = 0; l < 9; ++l) {
                int i1 = i - (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get() || HUD.customHotbar.get()) ? 92 : 90) + l * 20 + 2;
                int j1 = sr.getScaledHeight() - 16 - (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get() || HUD.customHotbar.get()) ? 10 : 3);
                renderHotbarItem(i1, j1, partialTicks, entityplayer, entityplayer.inventory.mainInventory.get(l));
            }

            if (!itemstack.isEmpty() && !((HUD.rustHUD.getBoolValue() || HUD.customHotbar.get() || HUD.customHotbar.get()) && MoonWare.featureManager.getFeatureByClass(HUD.class).getState())) {
                CustomItems.setRenderOffHand(true);
                int l1 = sr.getScaledHeight() - 16 - 3;
                if (enumhandside == EnumHandSide.LEFT) {
                    renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                } else {
                    renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }

                CustomItems.setRenderOffHand(false);
            }

            if (Minecraft.gameSettings.attackIndicator == 2) {
                float f1 = Minecraft.player.getCooledAttackStrength(0.0F);

                if (f1 < 1.0F) {
                    int i2 = sr.getScaledHeight() - 20;
                    int j2 = i + 91 + 6;

                    if (enumhandside == EnumHandSide.RIGHT) {
                        j2 = i - 91 - 22;
                    }

                    Minecraft.getTextureManager().bindTexture(Gui.ICONS);
                    int k1 = (int) (f1 * 19.0F);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                    drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                }
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
        Minecraft.profiler.startSection("jumpBar");
        Minecraft.getTextureManager().bindTexture(Gui.ICONS);
        float f = Minecraft.player.getHorseJumpPower();
        int i = 182;
        int j = (int) (f * 183.0F);
        int k = scaledRes.getScaledHeight() - 32 + 3;
        drawTexturedModalRect(x, k, 0, 84, 182, 5);

        if (j > 0) {
            drawTexturedModalRect(x, k, 0, 89, j, 5);
        }

        Minecraft.profiler.endSection();
    }

    public void renderExpBar(ScaledResolution scaledRes, int x) {
        if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get())) {
            return;
        }

        Minecraft.profiler.startSection("expBar");
        Minecraft.getTextureManager().bindTexture(Gui.ICONS);
        int i = Minecraft.player.xpBarCap();

        if (i > 0) {
            int j = 182;
            int k = (int) (Minecraft.player.experience * 183.0F);
            int l = scaledRes.getScaledHeight() - 32 + 3;
            drawTexturedModalRect(x, l, 0, 64, 182, 5);

            if (k > 0) {
                drawTexturedModalRect(x, l, 0, 69, k, 5);
            }
        }

        Minecraft.profiler.endSection();

        if (Minecraft.player.experienceLevel > 0) {
            Minecraft.profiler.startSection("expLevel");
            int j1 = 8453920;

            if (Config.isCustomColors()) {
                j1 = CustomColors.getExpBarTextColor(j1);
            }

            String s = "" + Minecraft.player.experienceLevel;
            int k1 = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
            int i1 = scaledRes.getScaledHeight() - 31 - 4;
            getFontRenderer().drawString(s, k1 + 1, i1, 0);
            getFontRenderer().drawString(s, k1 - 1, i1, 0);
            getFontRenderer().drawString(s, k1, i1 + 1, 0);
            getFontRenderer().drawString(s, k1, i1 - 1, 0);
            getFontRenderer().drawString(s, k1, i1, j1);
            Minecraft.profiler.endSection();
        }
    }

    public void renderSelectedItem(ScaledResolution scaledRes) {
        Minecraft.profiler.startSection("selectedItemName");

        if (remainingHighlightTicks > 0 && !highlightingItemStack.isEmpty()) {
            String s = highlightingItemStack.getDisplayName();

            if (highlightingItemStack.hasDisplayName()) {
                s = Formatting.ITALIC + s;
            }

            int i = (scaledRes.getScaledWidth() - getFontRenderer().getStringWidth(s)) / 2;
            int j = scaledRes.getScaledHeight() - 59;

            if (!Minecraft.playerController.shouldDrawHUD()) {
                j += 14;
            }

            int k = (int) ((float) remainingHighlightTicks * 256.0F / 10.0F);

            if (k > 255) {
                k = 255;
            }

            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                getFontRenderer().drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

        Minecraft.profiler.endSection();
    }

    private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
            public boolean apply(@Nullable Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));

        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int i = getFontRenderer().getStringWidth(objective.getDisplayName());

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + Formatting.RED + score.getScorePoints();
            i = Math.max(i, getFontRenderer().getStringWidth(s));
        }

        int i1 = collection.size() * getFontRenderer().height;
        int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - 3;
        int j = 0;

        for (Score score1 : collection) {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = Formatting.RED + "" + score1.getScorePoints();
            int k = j1 - j * getFontRenderer().height;
            int l = scaledRes.getScaledWidth() - 3 + 2;
            Gui.drawRect(l1 - 2, k, l, k + getFontRenderer().height, 1342177280);
            if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.scoreBoardSpoof.getBoolValue()) {
                getFontRenderer().drawString(s1.replace(Minecraft.player.getName().substring(0, 2),
                                "                                                                                                                                                                                                                                                                                                                                                                        "),
                        l1, k, 553648127);
            } else {
                getFontRenderer().drawString(s1, l1, k, 553648127);
            }

            if (org.moonware.client.feature.impl.visual.Scoreboard.scoreboardPoints.getBoolValue()) {
                getFontRenderer().drawString(s2, l - getFontRenderer().getStringWidth(s2), k, 553648127);
            }

            if (j == collection.size()) {
                String s3 = objective.getDisplayName();
                Gui.drawRect(l1 - 2, k - getFontRenderer().height - 1, l, k - 1, 1610612736);
                Gui.drawRect(l1 - 2, k - 1, l, k, 1342177280);
                getFontRenderer().drawString(s3, l1 + i / 2 - getFontRenderer().getStringWidth(s3) / 2, k - getFontRenderer().height, 553648127);
            }
        }
    }

    private void renderPlayerStats(ScaledResolution scaledRes) {
        GlStateManager.pushMatrix();
        if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && HUD.customHotbar.get()) {
            GlStateManager.translate(0.0F, -15.0F, 1.0F);
        }
        if (Minecraft.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) Minecraft.getRenderViewEntity();
            int i = MathHelper.ceil(entityplayer.getHealth());
            boolean flag = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

            if (i < playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 20;
            } else if (i > playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 10;
            }

            if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
                playerHealth = i;
                lastPlayerHealth = i;
                lastSystemTime = Minecraft.getSystemTime();
            }

            playerHealth = i;
            int j = lastPlayerHealth;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            int l = scaledRes.getScaledWidth() / 2 - 91;
            int i1 = scaledRes.getScaledWidth() / 2 + 91;
            int j1 = scaledRes.getScaledHeight() - 39;
            float f = (float) iattributeinstance.getAttributeValue();
            int k1 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
            int l1 = MathHelper.ceil((f + (float) k1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = j1 - (l1 - 1) * i2 - 10;
            int k2 = j1 - 10;
            int l2 = k1;
            int i3 = entityplayer.getTotalArmorValue();
            int j3 = -1;
            ScaledResolution sr = new ScaledResolution(minecraft);
            float width = sr.getScaledWidth() - 155 - (FontStorage.helvetica.getWidth("zalupa"));
            float height = sr.getScaledHeight() - FontStorage.helvetica.getHeight() - 110;

            if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
                j3 = updateCounter % MathHelper.ceil(f + 5.0F);
            }

            Minecraft.profiler.startSection("armor");

            if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get())) {

            } else {

                for (int k3 = 0; k3 < 10; ++k3) {
                    if (i3 > 0) {
                        int l3 = l + k3 * 8;

                        if (k3 * 2 + 1 < i3) {
                            drawTexturedModalRect(l3, j2, 34, 9, 9, 9);
                        }

                        if (k3 * 2 + 1 == i3) {
                            drawTexturedModalRect(l3, j2, 25, 9, 9, 9);
                        }

                        if (k3 * 2 + 1 > i3) {
                            drawTexturedModalRect(l3, j2, 16, 9, 9, 9);
                        }
                    }
                }
            }

            Minecraft.profiler.endStartSection("health");

            if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue())) {

                /* HEALTH */
                float healthPercentage = Minecraft.player.getHealth() / Minecraft.player.getMaxHealth() * 100;
                String healthPercentageStr = "" + (int) healthPercentage;
                RectHelper.drawRectBetter(width, height, 122, 20, new Color(75, 75, 75, 175).getRGB());
                org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/rusthud/health_tag_icon.png"), width + 3.5F, height + 3.5F, 13, 13, new Color(150, 150, 150, 255));
                RectHelper.drawRectBetter(width + 20, height + 2, healthPercentage, 16, new Color(135, 185, 75, 215).getRGB());
                FontStorage.helvetica.draw(healthPercentageStr, width + 24, height + 9, -1);

                /* ARMOR */
                float armorPercentage = (Minecraft.player.getTotalArmorValue() * 5);
                String armorPercentageStr = "" + (int) armorPercentage;
                RectHelper.drawRectBetter(width, height + 21, 122, 20, new Color(75, 75, 75, 175).getRGB());
                org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/rusthud/armor_icon.png"), width + 3.5F, height + 24.5F, 13, 13, new Color(150, 150, 150, 255));
                for (ItemStack armorList : Minecraft.player.getArmorInventoryList()) {
                    if (!armorList.isEmpty()) {
                        RectHelper.drawRectBetter(width + 20, height + 23, armorPercentage, 16, new Color(55, 155, 215, 215).getRGB());
                    }
                }
                FontStorage.helvetica.draw(armorPercentageStr, width + 24, height + 30, -1);

                /* FOOD */
                float foodPercentage = (Minecraft.player.getFoodStats().getFoodLevel() * 5);
                String foodPercentageStr = "" + (int) foodPercentage;
                RectHelper.drawRectBetter(width, height + 42, 122, 20, new Color(75, 75, 75, 175).getRGB());
                org.moonware.client.helpers.render.RenderHelper.drawImage(new Namespaced("moonware/rusthud/food_icon.png"), width + 3.5F, height + 45.5F, 13, 13, new Color(150, 150, 150, 255));
                RectHelper.drawRectBetter(width + 20, height + 44, foodPercentage, 16, new Color(215, 118, 7, 215).getRGB());
                FontStorage.helvetica.draw(foodPercentageStr, width + 24, height + 51, -1);

            } else {

                for (int j5 = MathHelper.ceil((f + (float) k1) / 2.0F) - 1; j5 >= 0; --j5) {
                    int k5 = 16;

                    if (entityplayer.isPotionActive(MobEffects.POISON)) {
                        k5 += 36;
                    } else if (entityplayer.isPotionActive(MobEffects.WITHER)) {
                        k5 += 72;
                    }

                    int i4 = 0;

                    if (flag) {
                        i4 = 1;
                    }

                    int j4 = MathHelper.ceil((float) (j5 + 1) / 10.0F) - 1;
                    int k4 = l + j5 % 10 * 8;
                    int l4 = j1 - j4 * i2;

                    if (i <= 4) {
                        l4 += Minecraft.RANDOM.nextInt(2);
                    }

                    if (l2 <= 0 && j5 == j3) {
                        l4 -= 2;
                    }

                    int i5 = 0;

                    if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
                        i5 = 5;
                    }
                    drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);


                    if (flag) {
                        if (j5 * 2 + 1 < j) {
                            drawTexturedModalRect(k4, l4, k5 + 54, 9 * i5, 9, 9);
                        }

                        if (j5 * 2 + 1 == j) {
                            drawTexturedModalRect(k4, l4, k5 + 63, 9 * i5, 9, 9);
                        }
                    }

                    if (l2 > 0) {
                        if (l2 == k1 && k1 % 2 == 1) {
                            drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                            --l2;
                        } else {
                            drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                            l2 -= 2;
                        }
                    } else {
                        if (j5 * 2 + 1 < i) {
                            drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
                        }

                        if (j5 * 2 + 1 == i) {
                            drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
                        }
                    }
                }
            }

            Entity entity = entityplayer.getRidingEntity();

            if (entity == null || !(entity instanceof EntityLivingBase)) {

                Minecraft.profiler.endStartSection("food");

                if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue())) {

                } else {

                    for (int l5 = 0; l5 < 10; ++l5) {
                        int j6 = j1;
                        int l6 = 16;
                        int j7 = 0;

                        if (entityplayer.isPotionActive(MobEffects.HUNGER)) {
                            l6 += 36;
                            j7 = 13;
                        }

                        if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (k * 3 + 1) == 0) {
                            j6 = j1 + (Minecraft.RANDOM.nextInt(3) - 1);
                        }

                        int l7 = i1 - l5 * 8 - 9;
                        drawTexturedModalRect(l7, j6, 16 + j7 * 9, 27, 9, 9);

                        if (l5 * 2 + 1 < k) {
                            drawTexturedModalRect(l7, j6, l6 + 36, 27, 9, 9);
                        }

                        if (l5 * 2 + 1 == k) {
                            drawTexturedModalRect(l7, j6, l6 + 45, 27, 9, 9);
                        }
                    }
                }
            }

            Minecraft.profiler.endStartSection("air");

            if (entityplayer.isInsideOfMaterial(Material.WATER)) {
                int i6 = Minecraft.player.getAir();
                int k6 = MathHelper.ceil((double) (i6 - 2) * 10.0D / 300.0D);
                int i7 = MathHelper.ceil((double) i6 * 10.0D / 300.0D) - k6;

                for (int k7 = 0; k7 < k6 + i7; ++k7) {
                    if (k7 < k6) {
                        drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 16, 18, 9, 9);
                    } else {
                        drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 25, 18, 9, 9);
                    }
                }
            }

            Minecraft.profiler.endSection();
        }
        GlStateManager.popMatrix();
    }

    private void renderMountHealth(ScaledResolution p_184047_1_) {
        if (Minecraft.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) Minecraft.getRenderViewEntity();
            Entity entity = entityplayer.getRidingEntity();

            if (entity instanceof EntityLivingBase) {
                Minecraft.profiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                int i = (int) Math.ceil(entitylivingbase.getHealth());
                float f = entitylivingbase.getMaxHealth();
                int j = (int) (f + 0.5F) / 2;

                if (j > 30) {
                    j = 30;
                }

                int k = p_184047_1_.getScaledHeight() - 39;
                int l = p_184047_1_.getScaledWidth() / 2 + 91;
                int i1 = k;
                int j1 = 0;

                for (boolean flag = false; j > 0; j1 += 20) {
                    int k1 = Math.min(j, 10);
                    j -= k1;

                    for (int l1 = 0; l1 < k1; ++l1) {
                        int i2 = 52;
                        int j2 = 0;
                        int k2 = l - l1 * 8 - 9;
                        drawTexturedModalRect(k2, i1, 52 + j2 * 9, 9, 9, 9);

                        if (l1 * 2 + 1 + j1 < i) {
                            drawTexturedModalRect(k2, i1, 88, 9, 9, 9);
                        }

                        if (l1 * 2 + 1 + j1 == i) {
                            drawTexturedModalRect(k2, i1, 97, 9, 9, 9);
                        }
                    }

                    i1 -= 10;
                }
            }
        }
    }

    private void drawPumpkin(ScaledResolution scaledRes) {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.pumpkin.getBoolValue()) {
            return;
        }
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a Vignette arount the entire screen that changes with light level.
     */
    private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else {
            lightLevel = 1.0F - lightLevel;
            lightLevel = MathHelper.clamp(lightLevel, 0.0F, 1.0F);
            WorldBorder worldborder = Minecraft.world.getWorldBorder();
            float f = (float) worldborder.getClosestDistance(Minecraft.player);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double) worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max(worldborder.getWarningDistance(), d0);

            if ((double) f < d1) {
                f = 1.0F - (float) ((double) f / d1);
            } else {
                f = 0.0F;
            }

            prevVignetteBrightness = (float) ((double) prevVignetteBrightness + (double) (lightLevel - prevVignetteBrightness) * 0.01D);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (f > 0.0F) {
                GlStateManager.color(0.0F, f, f, 1.0F);
            } else {
                GlStateManager.color(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
            }

            Minecraft.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
    }

    private void drawPortal(float timeInPortal, ScaledResolution scaledRes) {
        if (timeInPortal < 1.0F) {
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
        Minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite textureatlassprite = Minecraft.getBlockRenderDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(f, f3).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(f2, f3).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(f2, f1).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float) stack.func_190921_D() - p_184044_3_;

            if (f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (p_184044_1_ + 8), (float) (p_184044_2_ + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(p_184044_1_ + 8)), (float) (-(p_184044_2_ + 12)), 0.0F);
            }

            if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get())) {
                Minecraft.getRenderItem().renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_ - 7);
            } else {
                Minecraft.getRenderItem().renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_);
            }

            if (f > 0.0F) {
                GlStateManager.popMatrix();
            }

            if (MoonWare.featureManager.getFeatureByClass(HUD.class).getState() && (HUD.rustHUD.getBoolValue() || HUD.customHotbar.get())) {
                Minecraft.getRenderItem().renderItemOverlays(FontStorage.helvetica, stack, p_184044_1_, p_184044_2_ - 4);
            } else {
                Minecraft.getRenderItem().renderItemOverlays(Minecraft.font, stack, p_184044_1_, p_184044_2_);
            }
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick() {
        if (Minecraft.world == null) {
            TextureAnimations.updateAnimations();
        }

        if (actionBarTime > 0) {
            --actionBarTime;
        }

        if (titlesTimer > 0) {
            --titlesTimer;

            if (titlesTimer <= 0) {
                displayedTitle = "";
                displayedSubTitle = "";
            }
        }

        ++updateCounter;

        if (Minecraft.player != null) {
            ItemStack itemstack = Minecraft.player.inventory.getCurrentItem();

            if (itemstack.isEmpty()) {
                remainingHighlightTicks = 0;
            } else if (!highlightingItemStack.isEmpty() && itemstack.getItem() == highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == highlightingItemStack.getMetadata())) {
                if (remainingHighlightTicks > 0) {
                    --remainingHighlightTicks;
                }
            } else {
                remainingHighlightTicks = 40;
            }

            highlightingItemStack = itemstack;
        }
    }

    public void setActionBar(String actionBar) {
        setActionBar(I18n.format("record.nowPlaying", actionBar), true);
    }

    public void setActionBar(String actionBar, boolean animate) {
        this.actionBar = actionBar;
        actionBarTime = 60;
        actionBarAnimated = animate;
    }

    public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
        if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
            displayedTitle = "";
            displayedSubTitle = "";
            titlesTimer = 0;
        } else if (title != null) {
            displayedTitle = title;
            titlesTimer = titleFadeIn + titleDisplayTime + titleFadeOut;
        } else if (subTitle != null) {
            displayedSubTitle = subTitle;
        } else {
            if (timeFadeIn >= 0) {
                titleFadeIn = timeFadeIn;
            }

            if (displayTime >= 0) {
                titleDisplayTime = displayTime;
            }

            if (timeFadeOut >= 0) {
                titleFadeOut = timeFadeOut;
            }

            if (titlesTimer > 0) {
                titlesTimer = titleFadeIn + titleDisplayTime + titleFadeOut;
            }
        }
    }

    public void setActionBar(Component component, boolean isPlaying) {
        setActionBar(component.asString(), isPlaying);
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public ChatHud getChatGUI() {
        return chatHud;
    }

    public int getUpdateCounter() {
        return updateCounter;
    }

    public Font getFontRenderer() {
        return Minecraft.font;
    }

    public GuiSpectator getSpectatorGui() {
        return spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList() {
        return overlayPlayerList;
    }

    public void cleanup() {
        overlayPlayerList.cleanup();
        bossbarHud.cleanup();
        Minecraft.getToastHud().cleanup();
    }

    public BossbarHud getBossbarHud() {
        return bossbarHud;
    }
}
