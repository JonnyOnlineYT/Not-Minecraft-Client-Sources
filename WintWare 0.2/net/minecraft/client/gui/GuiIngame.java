package net.minecraft.client.gui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import me.wintware.client.Main;
import me.wintware.client.event.EventManager;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.event.impl.EventRenderGui;
import me.wintware.client.event.impl.EventRenderScoreboard;
import me.wintware.client.event.types.EventType;
import me.wintware.client.module.hud.HUD;
import me.wintware.client.module.visual.Crosshair;
import me.wintware.client.module.visual.EnchantEffect;
import me.wintware.client.module.visual.NoExpBar;
import me.wintware.client.module.visual.NoPotionDebug;
import me.wintware.client.module.visual.NoPumkinOverlay;
import me.wintware.client.module.visual.ScoreBoard;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.chat.NormalChatListener;
import net.minecraft.client.gui.chat.OverlayChatListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.border.WorldBorder;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureAnimations;

public class GuiIngame extends Gui {
   private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");
   private static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
   private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");
   private final Random rand = new Random();
   private final Minecraft mc;
   private final RenderItem itemRenderer;
   private final GuiNewChat persistantChatGUI;
   private int updateCounter;
   public float arrayAnimate;
   private String recordPlaying = "";
   private int recordPlayingUpFor;
   private boolean recordIsPlaying;
   public float prevVignetteBrightness = 1.0F;
   private int remainingHighlightTicks;
   private ItemStack highlightingItemStack;
   private final GuiOverlayDebug overlayDebug;
   private final GuiSubtitleOverlay overlaySubtitle;
   private final GuiSpectator spectatorGui;
   private final GuiPlayerTabOverlay overlayPlayerList;
   private final GuiBossOverlay overlayBoss;
   private int titlesTimer;
   private String displayedTitle;
   private String displayedSubTitle;
   private int titleFadeIn;
   private int titleDisplayTime;
   private int titleFadeOut;
   private int playerHealth;
   private int lastPlayerHealth;
   private long lastSystemTime;
   private long healthUpdateCounter;
   private final Map<ChatType, List<IChatListener>> field_191743_I;

   public GuiIngame(Minecraft mcIn) {
      this.highlightingItemStack = ItemStack.field_190927_a;
      this.displayedTitle = "";
      this.displayedSubTitle = "";
      this.field_191743_I = Maps.newHashMap();
      this.mc = mcIn;
      this.itemRenderer = mcIn.getRenderItem();
      this.overlayDebug = new GuiOverlayDebug(mcIn);
      this.spectatorGui = new GuiSpectator(mcIn);
      this.persistantChatGUI = new GuiNewChat(mcIn);
      this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
      this.overlayBoss = new GuiBossOverlay(mcIn);
      this.overlaySubtitle = new GuiSubtitleOverlay(mcIn);
      ChatType[] var2 = ChatType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ChatType chattype = var2[var4];
         this.field_191743_I.put(chattype, Lists.newArrayList());
      }

      IChatListener ichatlistener = NarratorChatListener.field_193643_a;
      ((List)this.field_191743_I.get(ChatType.CHAT)).add(new NormalChatListener(mcIn));
      ((List)this.field_191743_I.get(ChatType.CHAT)).add(ichatlistener);
      ((List)this.field_191743_I.get(ChatType.SYSTEM)).add(new NormalChatListener(mcIn));
      ((List)this.field_191743_I.get(ChatType.SYSTEM)).add(ichatlistener);
      ((List)this.field_191743_I.get(ChatType.GAME_INFO)).add(new OverlayChatListener(mcIn));
      this.setDefaultTitlesTimes();
   }

   public void setDefaultTitlesTimes() {
      this.titleFadeIn = 10;
      this.titleDisplayTime = 70;
      this.titleFadeOut = 20;
   }

   public void renderGameOverlay(float partialTicks) {
      ScaledResolution scaledresolution = new ScaledResolution(this.mc);
      int i = scaledresolution.getScaledWidth();
      int j = scaledresolution.getScaledHeight();
      FontRenderer fontrenderer = this.getFontRenderer();
      EventRenderGui event = new EventRenderGui(scaledresolution, partialTicks);
      EventManager.call(event);
      GlStateManager.enableBlend();
      if (Config.isVignetteEnabled()) {
         this.renderVignette(this.mc.player.getBrightness(), scaledresolution);
      } else {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      }

      ItemStack itemstack = this.mc.player.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) && !Main.instance.moduleManager.getModuleByClass(NoPumkinOverlay.class).getState()) {
         this.renderPumpkinOverlay(scaledresolution);
      }

      if (!this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
         float f = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks;
         if (f > 0.0F) {
            this.renderPortal(f, scaledresolution);
         }
      }

      if (this.mc.playerController.isSpectator()) {
         this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
      } else {
         this.renderHotbar(scaledresolution, partialTicks);
      }

      Event2D event1 = new Event2D((float)scaledresolution.getScaledWidth(), (float)scaledresolution.getScaledHeight());
      EventManager.call(event1);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      GlStateManager.enableBlend();
      if (this.showCrosshair() && this.mc.gameSettings.thirdPersonView < 1 && Main.instance.moduleManager.getModuleByClass(Crosshair.class).getState()) {
         GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
         GlStateManager.enableAlpha();
         this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
      }

      this.renderAttackIndicator(partialTicks, scaledresolution);
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      this.mc.mcProfiler.startSection("bossHealth");
      this.overlayBoss.renderBossHealth();
      this.mc.mcProfiler.endSection();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      if (this.mc.playerController.shouldDrawHUD()) {
         this.renderPlayerStats(scaledresolution);
      }

      this.renderMountHealth(scaledresolution);
      GlStateManager.disableBlend();
      int k1;
      float f3;
      int i2;
      if (this.mc.player.getSleepTimer() > 0) {
         this.mc.mcProfiler.startSection("sleep");
         GlStateManager.disableDepth();
         GlStateManager.disableAlpha();
         k1 = this.mc.player.getSleepTimer();
         f3 = (float)k1 / 100.0F;
         if (f3 > 1.0F) {
            f3 = 1.0F - (float)(k1 - 100) / 10.0F;
         }

         i2 = (int)(220.0F * f3) << 24 | 1052704;
         drawRect(0.0D, 0.0D, (double)i, (double)j, i2);
         GlStateManager.enableAlpha();
         GlStateManager.enableDepth();
         this.mc.mcProfiler.endSection();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      k1 = i / 2 - 91;
      if (this.mc.player.isRidingHorse()) {
         this.renderHorseJumpBar(scaledresolution, k1);
      } else if (this.mc.playerController.gameIsSurvivalOrAdventure() && !Main.instance.moduleManager.getModuleByClass(NoExpBar.class).getState()) {
         this.renderExpBar(scaledresolution, k1);
      }

      if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
         this.renderSelectedItem(scaledresolution);
      } else if (this.mc.player.isSpectator()) {
         this.spectatorGui.renderSelectedItem(scaledresolution);
      }

      if (!Main.instance.moduleManager.getModuleByClass(NoPotionDebug.class).getState()) {
         this.renderPotionEffects(scaledresolution);
      }

      if (this.mc.gameSettings.showDebugInfo) {
         this.overlayDebug.renderDebugInfo(scaledresolution);
      }

      int j2;
      if (this.recordPlayingUpFor > 0) {
         this.mc.mcProfiler.startSection("overlayMessage");
         f3 = (float)this.recordPlayingUpFor - partialTicks;
         i2 = (int)(f3 * 255.0F / 20.0F);
         if (i2 > 255) {
            i2 = 255;
         }

         if (i2 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            j2 = 16777215;
            if (this.recordIsPlaying) {
               j2 = MathHelper.hsvToRGB(f3 / 50.0F, 0.7F, 0.6F) & 16777215;
            }

            fontrenderer.drawString(this.recordPlaying, (float)(-fontrenderer.getStringWidth(this.recordPlaying) / 2), -4.0F, j2 + (i2 << 24 & -16777216));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.mcProfiler.endSection();
      }

      this.overlaySubtitle.renderSubtitles(scaledresolution);
      if (this.titlesTimer > 0) {
         this.mc.mcProfiler.startSection("titleAndSubtitle");
         f3 = (float)this.titlesTimer - partialTicks;
         i2 = 255;
         if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
            float f4 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
            i2 = (int)(f4 * 255.0F / (float)this.titleFadeIn);
         }

         if (this.titlesTimer <= this.titleFadeOut) {
            i2 = (int)(f3 * 255.0F / (float)this.titleFadeOut);
         }

         i2 = MathHelper.clamp(i2, 0, 255);
         if (i2 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 4.0F);
            j2 = i2 << 24 & -16777216;
            fontrenderer.drawString(this.displayedTitle, (float)(-fontrenderer.getStringWidth(this.displayedTitle) / 2), -10.0F, 16777215 | j2, true);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            fontrenderer.drawString(this.displayedSubTitle, (float)(-fontrenderer.getStringWidth(this.displayedSubTitle) / 2), 5.0F, 16777215 | j2, true);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.mcProfiler.endSection();
      }

      Scoreboard scoreboard = this.mc.world.getScoreboard();
      ScoreObjective scoreobjective = null;
      ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.player.getName());
      if (scoreplayerteam != null) {
         int i1 = scoreplayerteam.getChatFormat().getColorIndex();
         if (i1 >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
         }
      }

      ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
      if (scoreobjective1 != null) {
         EventRenderScoreboard e = new EventRenderScoreboard(EventType.PRE);
         EventManager.call(e);
         if (!Main.instance.moduleManager.getModuleByClass(ScoreBoard.class).getState() || !Main.instance.setmgr.getSettingByName("NoScoreBoard").getValue()) {
            this.renderScoreboard(scoreobjective1, scaledresolution);
         }

         EventRenderScoreboard event2 = new EventRenderScoreboard(EventType.POST);
         EventManager.call(event2);
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.disableAlpha();
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, (float)(j - 48), 0.0F);
      this.mc.mcProfiler.startSection("chat");
      this.persistantChatGUI.drawChat(this.updateCounter);
      this.mc.mcProfiler.endSection();
      GlStateManager.popMatrix();
      scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
      if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning() && this.mc.player.connection.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
         this.overlayPlayerList.updatePlayerList(false);
      } else {
         this.overlayPlayerList.updatePlayerList(true);
         this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.enableAlpha();
   }

   protected boolean showCrosshair() {
      if (this.mc.gameSettings.showDebugInfo && !this.mc.player.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
         return false;
      } else if (this.mc.playerController.isSpectator()) {
         if (this.mc.pointedEntity != null) {
            return true;
         } else {
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
               BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
               if (this.mc.world.getTileEntity(blockpos) instanceof IInventory) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

   private void renderAttackIndicator(float p_184045_1_, ScaledResolution p_184045_2_) {
      GameSettings gamesettings = this.mc.gameSettings;
      if (gamesettings.thirdPersonView == 0) {
         if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
            RayTraceResult raytraceresult = this.mc.objectMouseOver;
            if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
               return;
            }

            BlockPos blockpos = raytraceresult.getBlockPos();
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            if (!ReflectorForge.blockHasTileEntity(iblockstate) || !(this.mc.world.getTileEntity(blockpos) instanceof IInventory)) {
               return;
            }
         }

         int l = p_184045_2_.getScaledWidth();
         int i1 = p_184045_2_.getScaledHeight();
         if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !this.mc.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(l / 2), (float)(i1 / 2), this.zLevel);
            Entity entity = this.mc.getRenderViewEntity();
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_184045_1_, -1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_184045_1_, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(-1.0F, -1.0F, -1.0F);
            OpenGlHelper.renderDirections(10);
            GlStateManager.popMatrix();
         } else {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(l / 2 - 7, i1 / 2 - 7, 0, 0, 16, 16);
            if (this.mc.gameSettings.attackIndicator == 1) {
               float f = this.mc.player.getCooledAttackStrength(0.0F);
               boolean flag = false;
               if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && f >= 1.0F) {
                  flag = this.mc.player.getCooldownPeriod() > 5.0F;
                  flag &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
               }

               int i = i1 / 2 - 7 + 16;
               int j = l / 2 - 8;
               if (flag) {
                  this.drawTexturedModalRect(j, i, 68, 94, 16, 16);
               } else if (f < 1.0F) {
                  int k = (int)(f * 17.0F);
                  this.drawTexturedModalRect(j, i, 36, 94, 16, 4);
                  this.drawTexturedModalRect(j, i, 52, 94, k, 4);
               }
            }
         }
      }

   }

   protected void renderPotionEffects(ScaledResolution resolution) {
      Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
      if (!collection.isEmpty()) {
         this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
         GlStateManager.enableBlend();
         int i = 0;
         int j = 0;
         Iterator iterator = Ordering.natural().reverse().sortedCopy(collection).iterator();

         while(true) {
            PotionEffect potioneffect;
            Potion potion;
            boolean flag;
            while(true) {
               if (!iterator.hasNext()) {
                  return;
               }

               potioneffect = (PotionEffect)iterator.next();
               potion = potioneffect.getPotion();
               flag = potion.hasStatusIcon();
               if (!Reflector.ForgePotion_shouldRenderHUD.exists()) {
                  break;
               }

               if (Reflector.callBoolean(potion, Reflector.ForgePotion_shouldRenderHUD, potioneffect)) {
                  this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                  flag = true;
                  break;
               }
            }

            if (flag && potioneffect.doesShowParticles()) {
               int k = resolution.getScaledWidth();
               int l = 1;
               if (this.mc.isDemo()) {
                  l += 15;
               }

               int i1 = potion.getStatusIconIndex();
               if (potion.isBeneficial()) {
                  ++i;
                  k -= 25 * i;
               } else {
                  ++j;
                  k -= 25 * j;
                  l += 26;
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               float f = 1.0F;
               if (potioneffect.getIsAmbient()) {
                  this.drawTexturedModalRect(k, l, 165, 166, 24, 24);
               } else {
                  this.drawTexturedModalRect(k, l, 141, 166, 24, 24);
                  if (potioneffect.getDuration() <= 200) {
                     int j1 = 10 - potioneffect.getDuration() / 20;
                     f = MathHelper.clamp((float)potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)potioneffect.getDuration() * 3.1415927F / 5.0F) * MathHelper.clamp((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                  }
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, f);
               if (Reflector.ForgePotion_renderHUDEffect.exists()) {
                  if (potion.hasStatusIcon()) {
                     this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                  }

                  Reflector.call(potion, Reflector.ForgePotion_renderHUDEffect, k, l, potioneffect, this.mc, f);
               } else {
                  this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
               }
            }
         }
      }
   }

   protected void renderHotbar(ScaledResolution sr, float partialTicks) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
         Color c = Color.getHSBColor(EnchantEffect.hue, 1.0F, 1.0F);
         float red = (float)c.getRed() / 255.0F;
         float blue = (float)c.getBlue() / 255.0F;
         float green = (float)c.getGreen() / 255.0F;
         int color = (new Color(red, green, blue)).getRGB();
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         ItemStack itemstack = entityplayer.getHeldItemOffhand();
         EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
         int i = sr.getScaledWidth() / 2;
         float f = this.zLevel;
         int j = true;
         int k = true;
         this.zLevel = -90.0F;
         int rainbow = HUD.rainbow(300, 20L);
         this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
         this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
         this.zLevel = f;
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         RenderHelper.enableGUIStandardItemLighting();
         CustomItems.setRenderOffHand(false);

         int l1;
         int i2;
         int j2;
         for(l1 = 0; l1 < 9; ++l1) {
            i2 = i - 90 + l1 * 20 + 2;
            j2 = sr.getScaledHeight() - 16 - 3;
            this.renderHotbarItem(i2, j2, partialTicks, entityplayer, (ItemStack)entityplayer.inventory.mainInventory.get(l1));
         }

         if (!itemstack.func_190926_b()) {
            CustomItems.setRenderOffHand(true);
            l1 = sr.getScaledHeight() - 16 - 3;
            if (enumhandside == EnumHandSide.LEFT) {
               this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
            } else {
               this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
            }

            CustomItems.setRenderOffHand(false);
         }

         if (this.mc.gameSettings.attackIndicator == 2) {
            float f1 = this.mc.player.getCooledAttackStrength(0.0F);
            if (f1 < 1.0F) {
               i2 = sr.getScaledHeight() - 20;
               j2 = i + 91 + 6;
               if (enumhandside == EnumHandSide.RIGHT) {
                  j2 = i - 91 - 22;
               }

               this.mc.getTextureManager().bindTexture(Gui.ICONS);
               int k1 = (int)(f1 * 19.0F);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
               this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
            }
         }

         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableBlend();
      }

   }

   public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
      this.mc.mcProfiler.startSection("jumpBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      float f = this.mc.player.getHorseJumpPower();
      int i = true;
      int j = (int)(f * 183.0F);
      int k = scaledRes.getScaledHeight() - 32 + 3;
      this.drawTexturedModalRect(x, k, 0, 84, 182, 5);
      if (j > 0) {
         this.drawTexturedModalRect(x, k, 0, 89, j, 5);
      }

      this.mc.mcProfiler.endSection();
   }

   public void renderExpBar(ScaledResolution scaledRes, int x) {
      this.mc.mcProfiler.startSection("expBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      int i = this.mc.player.xpBarCap();
      int k1;
      if (i > 0) {
         int j = true;
         int k = (int)(this.mc.player.experience * 183.0F);
         k1 = scaledRes.getScaledHeight() - 32 + 3;
         this.drawTexturedModalRect(x, k1, 0, 64, 182, 5);
         if (k > 0) {
            this.drawTexturedModalRect(x, k1, 0, 69, k, 5);
         }
      }

      this.mc.mcProfiler.endSection();
      if (this.mc.player.experienceLevel > 0) {
         this.mc.mcProfiler.startSection("expLevel");
         int j1 = 8453920;
         if (Config.isCustomColors()) {
            j1 = CustomColors.getExpBarTextColor(j1);
         }

         String s = "" + this.mc.player.experienceLevel;
         k1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
         int i1 = scaledRes.getScaledHeight() - 31 - 4;
         this.getFontRenderer().drawString(s, (float)(k1 + 1), (float)i1, 0);
         this.getFontRenderer().drawString(s, (float)(k1 - 1), (float)i1, 0);
         this.getFontRenderer().drawString(s, (float)k1, (float)(i1 + 1), 0);
         this.getFontRenderer().drawString(s, (float)k1, (float)(i1 - 1), 0);
         this.getFontRenderer().drawString(s, (float)k1, (float)i1, j1);
         this.mc.mcProfiler.endSection();
      }

   }

   public void renderSelectedItem(ScaledResolution scaledRes) {
      this.mc.mcProfiler.startSection("selectedItemName");
      if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.func_190926_b()) {
         String s = this.highlightingItemStack.getDisplayName();
         if (this.highlightingItemStack.hasDisplayName()) {
            s = TextFormatting.ITALIC + s;
         }

         int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
         int j = scaledRes.getScaledHeight() - 59;
         if (!this.mc.playerController.shouldDrawHUD()) {
            j += 14;
         }

         int k = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);
         if (k > 255) {
            k = 255;
         }

         if (k > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.getFontRenderer().drawStringWithShadow(s, (float)i, (float)j, 16777215 + (k << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }

      this.mc.mcProfiler.endSection();
   }

   public void renderDemo(ScaledResolution scaledRes) {
      this.mc.mcProfiler.startSection("demo");
      String s;
      if (this.mc.world.getTotalWorldTime() >= 120500L) {
         s = I18n.format("demo.demoExpired");
      } else {
         s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.world.getTotalWorldTime())));
      }

      int i = this.getFontRenderer().getStringWidth(s);
      this.getFontRenderer().drawStringWithShadow(s, (float)(scaledRes.getScaledWidth() - i - 10), 5.0F, 16777215);
      this.mc.mcProfiler.endSection();
   }

   private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
      Scoreboard scoreboard = objective.getScoreboard();
      Collection<Score> collection = scoreboard.getSortedScores(objective);
      List<Score> list = Lists.newArrayList((Iterable)collection.stream().filter((p_apply_1_) -> {
         return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
      }).collect(Collectors.toList()));
      ArrayList collection;
      if (list.size() > 15) {
         collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
      } else {
         collection = list;
      }

      int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

      String s;
      for(Iterator var7 = collection.iterator(); var7.hasNext(); i = Math.max(i, this.getFontRenderer().getStringWidth(s))) {
         Score score = (Score)var7.next();
         ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
         s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
      }

      int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
      int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
      int k1 = true;
      int l1 = scaledRes.getScaledWidth() - i - 3;
      int j = 0;
      Iterator var12 = collection.iterator();

      while(var12.hasNext()) {
         Score score1 = (Score)var12.next();
         ++j;
         ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
         String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
         int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
         int l = scaledRes.getScaledWidth() - 3 + 2;
         RenderUtil.drawRectOpacity((double)(l1 - 2), (double)k, (double)l, (double)(k + this.getFontRenderer().FONT_HEIGHT), 0.5F);
         this.mc.fontRendererObj.drawStringWithShadow(s1, (float)l1, (float)k, 553648127);
         if (j == collection.size()) {
            String s3 = objective.getDisplayName();
            RenderUtil.drawRectOpacity((double)(l1 - 2), (double)(k - this.getFontRenderer().FONT_HEIGHT - 1), (double)l, (double)(k - 1), 0.5F);
            RenderUtil.drawRectOpacity((double)(l1 - 2), (double)(k - 1), (double)l, (double)k, 0.5F);
            this.mc.fontRendererObj.drawStringWithShadow(s3, (float)(l1 + i / 2 - this.mc.fontRendererObj.getStringWidth(s3) / 2), (float)(k - this.mc.fontRendererObj.FONT_HEIGHT), 553648127);
         }
      }

   }

   private void renderPlayerStats(ScaledResolution scaledRes) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         int i = MathHelper.ceil(entityplayer.getHealth());
         boolean flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
         if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 20);
         } else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 10);
         }

         if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = i;
            this.lastPlayerHealth = i;
            this.lastSystemTime = Minecraft.getSystemTime();
         }

         this.playerHealth = i;
         int j = this.lastPlayerHealth;
         this.rand.setSeed((long)(this.updateCounter * 312871));
         FoodStats foodstats = entityplayer.getFoodStats();
         int k = foodstats.getFoodLevel();
         IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
         int l = scaledRes.getScaledWidth() / 2 - 91;
         int i1 = scaledRes.getScaledWidth() / 2 + 91;
         int j1 = scaledRes.getScaledHeight() - 39;
         float f = (float)iattributeinstance.getAttributeValue();
         int k1 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
         int l1 = MathHelper.ceil((f + (float)k1) / 2.0F / 10.0F);
         int i2 = Math.max(10 - (l1 - 2), 3);
         int j2 = j1 - (l1 - 1) * i2 - 10;
         int k2 = j1 - 10;
         int l2 = k1;
         int i3 = entityplayer.getTotalArmorValue();
         int j3 = -1;
         if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
            j3 = this.updateCounter % MathHelper.ceil(f + 5.0F);
         }

         this.mc.mcProfiler.startSection("armor");

         int j5;
         int k5;
         for(j5 = 0; j5 < 10; ++j5) {
            if (i3 > 0) {
               k5 = l + j5 * 8;
               if (j5 * 2 + 1 < i3) {
                  this.drawTexturedModalRect(k5, j2, 34, 9, 9, 9);
               }

               if (j5 * 2 + 1 == i3) {
                  this.drawTexturedModalRect(k5, j2, 25, 9, 9, 9);
               }

               if (j5 * 2 + 1 > i3) {
                  this.drawTexturedModalRect(k5, j2, 16, 9, 9, 9);
               }
            }
         }

         this.mc.mcProfiler.endStartSection("health");

         int l6;
         int k4;
         int l4;
         for(j5 = MathHelper.ceil((f + (float)k1) / 2.0F) - 1; j5 >= 0; --j5) {
            k5 = 16;
            if (entityplayer.isPotionActive(MobEffects.POISON)) {
               k5 += 36;
            } else if (entityplayer.isPotionActive(MobEffects.WITHER)) {
               k5 += 72;
            }

            int i4 = 0;
            if (flag) {
               i4 = 1;
            }

            l6 = MathHelper.ceil((float)(j5 + 1) / 10.0F) - 1;
            k4 = l + j5 % 10 * 8;
            l4 = j1 - l6 * i2;
            if (i <= 4) {
               l4 += this.rand.nextInt(2);
            }

            if (l2 <= 0 && j5 == j3) {
               l4 -= 2;
            }

            int i5 = 0;
            if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
               i5 = 5;
            }

            this.drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);
            if (flag) {
               if (j5 * 2 + 1 < j) {
                  this.drawTexturedModalRect(k4, l4, k5 + 54, 9 * i5, 9, 9);
               }

               if (j5 * 2 + 1 == j) {
                  this.drawTexturedModalRect(k4, l4, k5 + 63, 9 * i5, 9, 9);
               }
            }

            if (l2 > 0) {
               if (l2 == k1 && k1 % 2 == 1) {
                  this.drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                  --l2;
               } else {
                  this.drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                  l2 -= 2;
               }
            } else {
               if (j5 * 2 + 1 < i) {
                  this.drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
               }

               if (j5 * 2 + 1 == i) {
                  this.drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
               }
            }
         }

         Entity entity = entityplayer.getRidingEntity();
         int j6;
         if (entity == null || !(entity instanceof EntityLivingBase)) {
            this.mc.mcProfiler.endStartSection("food");

            for(k5 = 0; k5 < 10; ++k5) {
               j6 = j1;
               l6 = 16;
               int j7 = 0;
               if (entityplayer.isPotionActive(MobEffects.HUNGER)) {
                  l6 += 36;
                  j7 = 13;
               }

               if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0) {
                  j6 = j1 + (this.rand.nextInt(3) - 1);
               }

               l4 = i1 - k5 * 8 - 9;
               this.drawTexturedModalRect(l4, j6, 16 + j7 * 9, 27, 9, 9);
               if (k5 * 2 + 1 < k) {
                  this.drawTexturedModalRect(l4, j6, l6 + 36, 27, 9, 9);
               }

               if (k5 * 2 + 1 == k) {
                  this.drawTexturedModalRect(l4, j6, l6 + 45, 27, 9, 9);
               }
            }
         }

         this.mc.mcProfiler.endStartSection("air");
         if (entityplayer.isInsideOfMaterial(Material.WATER)) {
            k5 = this.mc.player.getAir();
            j6 = MathHelper.ceil((double)(k5 - 2) * 10.0D / 300.0D);
            l6 = MathHelper.ceil((double)k5 * 10.0D / 300.0D) - j6;

            for(k4 = 0; k4 < j6 + l6; ++k4) {
               if (k4 < j6) {
                  this.drawTexturedModalRect(i1 - k4 * 8 - 9, k2, 16, 18, 9, 9);
               } else {
                  this.drawTexturedModalRect(i1 - k4 * 8 - 9, k2, 25, 18, 9, 9);
               }
            }
         }

         this.mc.mcProfiler.endSection();
      }

   }

   private void renderMountHealth(ScaledResolution p_184047_1_) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         Entity entity = entityplayer.getRidingEntity();
         if (entity instanceof EntityLivingBase) {
            this.mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            int i = (int)Math.ceil((double)entitylivingbase.getHealth());
            float f = entitylivingbase.getMaxHealth();
            int j = (int)(f + 0.5F) / 2;
            if (j > 30) {
               j = 30;
            }

            int k = p_184047_1_.getScaledHeight() - 39;
            int l = p_184047_1_.getScaledWidth() / 2 + 91;
            int i1 = k;
            int j1 = 0;

            for(boolean var12 = false; j > 0; j1 += 20) {
               int k1 = Math.min(j, 10);
               j -= k1;

               for(int l1 = 0; l1 < k1; ++l1) {
                  int i2 = true;
                  int j2 = 0;
                  int k2 = l - l1 * 8 - 9;
                  this.drawTexturedModalRect(k2, i1, 52 + j2 * 9, 9, 9, 9);
                  if (l1 * 2 + 1 + j1 < i) {
                     this.drawTexturedModalRect(k2, i1, 88, 9, 9, 9);
                  }

                  if (l1 * 2 + 1 + j1 == i) {
                     this.drawTexturedModalRect(k2, i1, 97, 9, 9, 9);
                  }
               }

               i1 -= 10;
            }
         }
      }

   }

   private void renderPumpkinOverlay(ScaledResolution scaledRes) {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableAlpha();
      this.mc.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
      bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
      bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
      bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
      if (!Config.isVignetteEnabled()) {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      } else {
         lightLevel = 1.0F - lightLevel;
         lightLevel = MathHelper.clamp(lightLevel, 0.0F, 1.0F);
         WorldBorder worldborder = this.mc.world.getWorldBorder();
         float f = (float)worldborder.getClosestDistance(this.mc.player);
         double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
         double d1 = Math.max((double)worldborder.getWarningDistance(), d0);
         if ((double)f < d1) {
            f = 1.0F - (float)((double)f / d1);
         } else {
            f = 0.0F;
         }

         this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(lightLevel - this.prevVignetteBrightness) * 0.01D);
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         if (f > 0.0F) {
            GlStateManager.color(0.0F, f, f, 1.0F);
         } else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
         }

         this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
         bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
         bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
         bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
         tessellator.draw();
         GlStateManager.depthMask(true);
         GlStateManager.enableDepth();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      }

   }

   private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
      if (timeInPortal < 1.0F) {
         timeInPortal *= timeInPortal;
         timeInPortal *= timeInPortal;
         timeInPortal = timeInPortal * 0.8F + 0.2F;
      }

      GlStateManager.disableAlpha();
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
      float f = textureatlassprite.getMinU();
      float f1 = textureatlassprite.getMinV();
      float f2 = textureatlassprite.getMaxU();
      float f3 = textureatlassprite.getMaxV();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex((double)f, (double)f3).endVertex();
      bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex((double)f2, (double)f3).endVertex();
      bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex((double)f2, (double)f1).endVertex();
      bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex((double)f, (double)f1).endVertex();
      tessellator.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack) {
      if (!stack.func_190926_b()) {
         float f = (float)stack.func_190921_D() - p_184044_3_;
         if (f > 0.0F) {
            GlStateManager.pushMatrix();
            float f1 = 1.0F + f / 5.0F;
            GlStateManager.translate((float)(p_184044_1_ + 8), (float)(p_184044_2_ + 12), 0.0F);
            GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GlStateManager.translate((float)(-(p_184044_1_ + 8)), (float)(-(p_184044_2_ + 12)), 0.0F);
         }

         this.itemRenderer.renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_);
         if (f > 0.0F) {
            GlStateManager.popMatrix();
         }

         this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, stack, p_184044_1_, p_184044_2_);
      }

   }

   public void updateTick() {
      if (this.mc.world == null) {
         TextureAnimations.updateAnimations();
      }

      if (this.recordPlayingUpFor > 0) {
         --this.recordPlayingUpFor;
      }

      if (this.titlesTimer > 0) {
         --this.titlesTimer;
         if (this.titlesTimer <= 0) {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
         }
      }

      ++this.updateCounter;
      if (this.mc.player != null) {
         ItemStack itemstack = this.mc.player.inventory.getCurrentItem();
         if (itemstack.func_190926_b()) {
            this.remainingHighlightTicks = 0;
         } else if (!this.highlightingItemStack.func_190926_b() && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
            if (this.remainingHighlightTicks > 0) {
               --this.remainingHighlightTicks;
            }
         } else {
            this.remainingHighlightTicks = 40;
         }

         this.highlightingItemStack = itemstack;
      }

   }

   public void setRecordPlayingMessage(String recordName) {
      this.setRecordPlaying(I18n.format("record.nowPlaying", recordName), true);
   }

   public void setRecordPlaying(String message, boolean isPlaying) {
      this.recordPlaying = message;
      this.recordPlayingUpFor = 60;
      this.recordIsPlaying = isPlaying;
   }

   public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
      if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
         this.displayedTitle = "";
         this.displayedSubTitle = "";
         this.titlesTimer = 0;
      } else if (title != null) {
         this.displayedTitle = title;
         this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
      } else if (subTitle != null) {
         this.displayedSubTitle = subTitle;
      } else {
         if (timeFadeIn >= 0) {
            this.titleFadeIn = timeFadeIn;
         }

         if (displayTime >= 0) {
            this.titleDisplayTime = displayTime;
         }

         if (timeFadeOut >= 0) {
            this.titleFadeOut = timeFadeOut;
         }

         if (this.titlesTimer > 0) {
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
         }
      }

   }

   public void setRecordPlaying(ITextComponent component, boolean isPlaying) {
      this.setRecordPlaying(component.getUnformattedText(), isPlaying);
   }

   public void func_191742_a(ChatType p_191742_1_, ITextComponent p_191742_2_) {
      Iterator var3 = ((List)this.field_191743_I.get(p_191742_1_)).iterator();

      while(var3.hasNext()) {
         IChatListener ichatlistener = (IChatListener)var3.next();
         ichatlistener.func_192576_a(p_191742_1_, p_191742_2_);
      }

   }

   public GuiNewChat getChatGUI() {
      return this.persistantChatGUI;
   }

   public int getUpdateCounter() {
      return this.updateCounter;
   }

   public FontRenderer getFontRenderer() {
      return this.mc.fontRendererObj;
   }

   public GuiSpectator getSpectatorGui() {
      return this.spectatorGui;
   }

   public GuiPlayerTabOverlay getTabList() {
      return this.overlayPlayerList;
   }

   public void resetPlayersOverlayFooterHeader() {
      this.overlayPlayerList.resetFooterHeader();
      this.overlayBoss.clearBossInfos();
      this.mc.func_193033_an().func_191788_b();
   }

   public GuiBossOverlay getBossOverlay() {
      return this.overlayBoss;
   }
}
