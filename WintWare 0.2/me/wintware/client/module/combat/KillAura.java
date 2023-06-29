package me.wintware.client.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.wintware.client.Main;
import me.wintware.client.clickgui.ClickGuiScreen;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.friendsystem.Friend;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.combat.RaycastUtil;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.other.RandomUtils;
import me.wintware.client.utils.other.TimerUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class KillAura extends Module {
   public static EntityLivingBase target;
   private Random random = new Random();
   private int sword;
   public static TimerUtils timer;
   private final List<EntityLivingBase> targets = new ArrayList();
   public static Setting yawSpeed;
   public static Setting pitchSpeed;
   public static Setting rotationPitch;
   public static Setting randomize;
   public static Setting range;
   public static Setting fov;
   public static Setting onlyCrit;
   int easingHealth = 0;

   public KillAura() {
      super("KillAura", Category.Combat);
      ArrayList<String> rotation = new ArrayList();
      rotation.add("Matrix");
      rotation.add("LegitSnap");
      rotation.add("None");
      Main.instance.setmgr.rSetting(new Setting("Rotation Mode", this, "Matrix", rotation));
      ArrayList<String> sort = new ArrayList();
      sort.add("Distance");
      sort.add("Health");
      sort.add("Smart");
      Main.instance.setmgr.rSetting(new Setting("Sort Mode", this, "Health", sort));
      Main.instance.setmgr.rSetting(fov = new Setting("FOV", this, 360.0D, 0.0D, 360.0D, true));
      Main.instance.setmgr.rSetting(new Setting("HitChance", this, 100.0D, 1.0D, 100.0D, false));
      Main.instance.setmgr.rSetting(range = new Setting("Range", this, 4.0D, 3.0D, 7.0D, false));
      Main.instance.setmgr.rSetting(new Setting("ColdDown", this, 0.0D, 0.0D, 1.0D, false));
      Main.instance.setmgr.rSetting(yawSpeed = new Setting("YawSpeed", this, 5.0D, 0.1D, 80.0D, false));
      Main.instance.setmgr.rSetting(pitchSpeed = new Setting("PitchSpeed", this, 5.0D, 0.1D, 80.0D, false));
      Main.instance.setmgr.rSetting(randomize = new Setting("Randomize", this, 1.0D, 0.1D, 30.0D, false));
      Main.instance.setmgr.rSetting(rotationPitch = new Setting("RotationPitch", this, true));
      Main.instance.setmgr.rSetting(new Setting("Players", this, true));
      Main.instance.setmgr.rSetting(new Setting("Mobs", this, false));
      Main.instance.setmgr.rSetting(new Setting("Invisible", this, false));
      Main.instance.setmgr.rSetting(new Setting("SnapHead", this, true));
      Main.instance.setmgr.rSetting(new Setting("Walls", this, false));
      Main.instance.setmgr.rSetting(new Setting("ShieldBreaker", this, false));
      Main.instance.setmgr.rSetting(new Setting("Stop Sprinting", this, false));
      Main.instance.setmgr.rSetting(onlyCrit = new Setting("OnlyCrits", this, false));
      Main.instance.setmgr.rSetting(new Setting("InteractFix", this, true));
   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof SPacketPlayerPosLook) {
         SPacketPlayerPosLook packet1 = (SPacketPlayerPosLook)packet;
         mc.player.rotationYaw = packet1.getYaw();
         mc.player.rotationPitch = packet1.getPitch();
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (!Main.instance.setmgr.getSettingByName("InteractFix").getValue() && target.rayTrace(Main.instance.setmgr.getSettingByName("Range").getValDouble(), 1.0F).typeOfHit == RayTraceResult.Type.MISS) {
         Vec3d eyesVec = target.getPositionEyes(1.0F);
         Vec3d lookVec = target.getLook(1.0F);
         Vec3d pointingVec = eyesVec.addVector(lookVec.xCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble(), lookVec.yCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble(), lookVec.zCoord * Main.instance.setmgr.getSettingByName("Range").getValDouble());
         int border = (int)mc.player.getCollisionBorderSize();
         AxisAlignedBB bb = mc.player.getEntityBoundingBox().expand((double)border, (double)border, (double)border);
         boolean shouldBlock = bb.intersectsWith(target.getEntityBoundingBox());
         bb.calculateIntercept(eyesVec, pointingVec);
      }

   }

   @EventTarget
   public void onEventPreMotionUpdate(EventPreMotionUpdate e) {
      if (mc.player.isEntityAlive()) {
         if (mc.currentScreen instanceof GuiGameOver && target.isDead) {
            this.toggle();
            NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
            return;
         }

         if (mc.player.ticksExisted <= 1) {
            this.toggle();
            NotificationPublisher.queue(this.getName(), "toggled off", NotificationType.INFO);
            return;
         }

         String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
         double range = Main.instance.setmgr.getSettingByName("Range").getValDouble();
         this.sortTargets();
         this.setDisplayName("KillAura " + ChatFormatting.GRAY + mode + ", " + range);
         target = this.getClosest(Main.instance.setmgr.getSettingByName("Range").getValDouble());
         float[] rotations = faceTarget(target, e.getYaw(), e.getPitch(), yawSpeed.getValFloat() * 5.0F, pitchSpeed.getValFloat() * 5.0F, false);
         Entity rayCastEntity = RaycastUtil.rayCastEntity(range + 1.0D, rotations[0], rotations[1]);
         if (mode.equalsIgnoreCase("Matrix")) {
            e.setYaw(rotations[0] + (float)RandomUtils.randomNumber(randomize.getValInt(), -randomize.getValInt()));
            if (rotationPitch.getValue()) {
               e.setPitch(rotations[1]);
            }
         }

         if ((double)ThreadLocalRandom.current().nextInt(100) <= Main.instance.setmgr.getSettingByName("HitChance").getValDouble()) {
            if (mc.player.getCooledAttackStrength(Main.instance.setmgr.getSettingByName("ColdDown").getValFloat()) == 1.0F) {
               for(int i = 0; i < 2; ++i) {
                  float[] rotation2;
                  if (Main.instance.setmgr.getSettingByName("OnlyCrits").getValue() && !((double)mc.player.fallDistance >= 0.18312D)) {
                     if ((!Main.instance.setmgr.getSettingByName("OnlyCrits").getValue() || (double)mc.player.fallDistance >= 0.18312D) && (double)ThreadLocalRandom.current().nextInt(100) <= Main.instance.setmgr.getSettingByName("HitChance").getValDouble()) {
                        rotation2 = faceTarget(target, e.getYaw(), e.getPitch(), yawSpeed.getValFloat() * 5.0F, pitchSpeed.getValFloat() * 5.0F, false);
                        if (Main.instance.setmgr.getSettingByName("SnapHead").getValue()) {
                           this.setRotation(rotation2[0], rotation2[1]);
                        }

                        if (Main.instance.setmgr.getSettingByName("SnapHead").getValue()) {
                           rotation2[0] = rotation2[0] < 90.0F ? 270.0F - rotation2[0] : rotation2[0] - 90.0F;
                        }

                        mc.playerController.attackEntity(mc.player, rayCastEntity);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        if (Main.instance.setmgr.getSettingByName("Stop Sprinting").getValue()) {
                           mc.player.setSprinting(false);
                        }

                        if (Main.instance.setmgr.getSettingByName("SnapHead").getValue()) {
                           this.setRotation(rotation2[0], rotation2[1]);
                        }
                     }
                  } else if ((double)ThreadLocalRandom.current().nextInt(100) <= Main.instance.setmgr.getSettingByName("HitChance").getValDouble()) {
                     rotation2 = faceTarget(target, e.getYaw(), e.getPitch(), yawSpeed.getValFloat() * 5.0F, pitchSpeed.getValFloat() * 5.0F, false);
                     if (Main.instance.setmgr.getSettingByName("SnapHead").getValue()) {
                        this.setRotation(rotation2[0], rotation2[1]);
                     }

                     mc.playerController.attackEntity(mc.player, rayCastEntity);
                     mc.player.swingArm(EnumHand.MAIN_HAND);
                     if (Main.instance.setmgr.getSettingByName("Stop Sprinting").getValue()) {
                        mc.player.setSprinting(true);
                     }
                  }
               }
            }

            if (target instanceof EntityPlayer && Main.instance.setmgr.getSettingByName("ShieldBreaker").getValue() && mc.player.getCooledAttackStrength(0.8F) == 1.0F) {
               this.destroyShield((EntityPlayer)target);
            }
         }
      }

   }

   public static float[] faceTarget(Entity target, float currentYaw, float currentPitch, float p_706252, float p_706253, boolean miss) {
      double var4 = target.posX - mc.player.posX;
      double var5 = target.posZ - mc.player.posZ;
      float range = mc.player.getDistanceToEntity(target);
      double var7;
      if (target instanceof EntityLivingBase) {
         EntityLivingBase var6 = (EntityLivingBase)target;
         var7 = var6.posY + (double)var6.getEyeHeight() - (mc.player.posY + (double)mc.player.getEyeHeight());
      } else {
         var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D - (mc.player.posY + (double)mc.player.getEyeHeight());
      }

      double var8 = (double)MathHelper.sqrt(var4 * var4 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F + 4.0F / range;
      float var10 = (float)(-(Math.atan2(var7 - (target instanceof EntityPlayer ? 0.25D : 0.0D), var8) * 180.0D / 3.141592653589793D) + (double)(5.0F / range));
      float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float gcd = f * f * f * 1.2F;
      float pitch = RotationUtil.updateRotation(mc.player.rotationPitch, var10, p_706253);
      float yaw = RotationUtil.updateRotation(mc.player.rotationYaw, var9, p_706252) + (float)RandomUtils.randomNumber(randomize.getValInt(), -randomize.getValInt());
      yaw -= yaw % gcd;
      pitch -= pitch % gcd;
      return new float[]{yaw, pitch};
   }

   public double lol(EntityLivingBase p) {
      double health = (double)p.getHealth();
      double distance = (double)mc.player.getDistanceToEntity(p);
      if (p.isInLava()) {
      }

      if (p.isInWater()) {
      }

      if (p.isInsideOfMaterial(Material.WEB)) {
      }

      if (p.isOnLadder()) {
      }

      double lol = -distance + -health / 2.0D;
      return -lol;
   }

   private void setRotation(float f, float f2) {
      f = this.random.nextBoolean() ? (float)((double)f + (double)this.random.nextInt(100) * 0.02D) : (float)((double)f - (double)this.random.nextInt(100) * 0.02D);
      if (mc.player.rotationYaw < f) {
         while(mc.player.rotationYaw < f) {
            mc.player.rotationYaw = (float)((double)mc.player.rotationYaw + (double)this.random.nextInt(100) * 0.001D);
         }
      } else {
         while(mc.player.rotationYaw > f) {
            mc.player.rotationYaw = (float)((double)mc.player.rotationYaw - (double)this.random.nextInt(100) * 0.001D);
         }
      }

   }

   public void destroyShield(EntityPlayer player) {
      if (Main.instance.setmgr.getSettingByName("ShieldBreaker").getValue()) {
         player.getHeldItemOffhand();
         if (player.getHeldItemOffhand().getItem() instanceof ItemShield) {
            if (player.isHandActive()) {
               if (getAxeAtHotbar() == -1) {
                  return;
               }

               mc.player.inventory.currentItem = getAxeAtHotbar();
            } else {
               mc.player.inventory.currentItem = this.sword;
            }
         }
      }

   }

   public static int getAxeAtHotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
         if (itemStack.getItem() instanceof ItemAxe) {
            return i;
         }
      }

      return -1;
   }

   private EntityLivingBase getClosest(double range) {
      this.targets.clear();
      EntityLivingBase target = null;
      Iterator var6 = mc.world.loadedEntityList.iterator();

      while(var6.hasNext()) {
         Object object = var6.next();
         Entity entity = (Entity)object;
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase player = (EntityLivingBase)entity;
            if (this.canAttack(player) && this.checkEntityID(entity) && this.isPlayerValid(player) && (double)mc.player.getDistanceToEntity(player) <= range) {
               target = player;
               this.targets.add(player);
            }
         }
      }

      return target;
   }

   public boolean checkEntityID(Entity entity) {
      boolean check;
      if (entity.getEntityId() <= 1070000000 && entity.getEntityId() > -1) {
         check = true;
      } else {
         check = false;
      }

      return check;
   }

   private void sortTargets() {
      String mode = Main.instance.setmgr.getSettingByName("Sort Mode").getValString();
      if (mode.equalsIgnoreCase("Health")) {
         this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
      }

      if (mode.equalsIgnoreCase("Distance")) {
         this.targets.sort(Comparator.comparingDouble((entity) -> {
            return (double)mc.player.getDistanceToEntity(entity);
         }));
      }

      if (mode.equalsIgnoreCase("Distance")) {
         this.targets.sort(Comparator.comparingDouble((entity) -> {
            return (double)mc.player.getDistanceToEntity(entity);
         }));
      }

      if (mode.equalsIgnoreCase("Smart")) {
         this.targets.sort(Comparator.comparingDouble((entity) -> {
            return this.lol(entity);
         }));
      }

      this.targets.sort(Comparator.comparing((entity) -> {
         return entity instanceof EntityPlayer;
      }));
   }

   @EventTarget
   public void e(Event2D e) {
      ScaledResolution sr = new ScaledResolution(mc);
      if (target instanceof EntityPlayer || target instanceof EntityOtherPlayerMP) {
         if (ClickGuiScreen.scaling <= 1.0D) {
            ClickGuiScreen.scaling += 0.003000000026077032D;
         }

         int width = 20 + Minecraft.getMinecraft().clickguismall.getStringWidth(target.getName());
         GL11.glPushMatrix();
         GL11.glTranslated((double)(width / 100), (double)(sr.getScaledHeight() / 100), 0.0D);
         GL11.glScalef((float)ClickGuiScreen.scaling, (float)ClickGuiScreen.scaling, 1.0F);
         GL11.glTranslated((double)(sr.getScaledWidth() / 2 + 10), (double)(sr.getScaledHeight() / 2), (double)(sr.getScaledWidth() / 2 + 10));
         RenderUtil.drawSmoothRect(-10.0F, 20.0F, (float)(2 + width), target.getTotalArmorValue() != 0 ? 56.0F : 50.0F, (new Color(35, 35, 40, 230)).getRGB());
         Minecraft.getMinecraft().clickguismall.drawString(target.getName(), 10.0F, 26.0F, 16777215);
         Minecraft.getMinecraft().clickguismall.drawStringWithShadow(Math.round(target.getHealth()) / 2 + " HP", 10.0F, 35.0F, 16777215);
         this.drawHead(mc.getConnection().getPlayerInfo(target.getUniqueID()).getLocationSkin(), -8, 22);
         RenderUtil.drawSmoothRect(-8.0F, 44.0F, (float)width, 46.0F, (new Color(25, 25, 35, 255)).getRGB());
         this.easingHealth = (int)((double)this.easingHealth + (double)(target.getHealth() - (float)this.easingHealth) / Math.pow(2.0D, 7.0D));
         if (this.easingHealth < 0 || (float)this.easingHealth > target.getMaxHealth()) {
            this.easingHealth = (int)target.getHealth();
         }

         if ((float)this.easingHealth > target.getHealth()) {
            RenderUtil.drawSmoothRect(-8.0F, 66.0F, (float)this.easingHealth / target.getMaxHealth() * (float)width, 58.0F, (new Color(231, 182, 0, 255)).getRGB());
         }

         if ((float)this.easingHealth < target.getHealth()) {
            RenderUtil.drawRect((double)((float)this.easingHealth / target.getMaxHealth() * (float)width), 56.0D, (double)((float)this.easingHealth / target.getMaxHealth() * (float)width), 58.0D, (new Color(231, 182, 0, 255)).getRGB());
         }

         RenderUtil.drawSmoothRect(-8.0F, 44.0F, target.getHealth() / target.getMaxHealth() * (float)width, 46.0F, (new Color(0, 224, 84, 255)).getRGB());
         if (target.getTotalArmorValue() != 0) {
            RenderUtil.drawSmoothRect(-8.0F, 50.0F, (float)width, 52.0F, (new Color(25, 25, 35, 255)).getRGB());
            RenderUtil.drawSmoothRect(-8.0F, 50.0F, (float)(target.getTotalArmorValue() / 20 * width), 52.0F, (new Color(77, 128, 255, 255)).getRGB());
         }

         GL11.glPopMatrix();
      }

   }

   public void drawHead(ResourceLocation skin, int width, int height) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      mc.getTextureManager().bindTexture(skin);
      Gui.drawScaledCustomSizeModalRect(width, height, 8.0F, 8.0F, 8, 8, 16, 16, 64.0F, 64.0F);
   }

   private boolean canAttack(EntityLivingBase player) {
      Iterator var2 = Main.instance.friendManager.getFriends().iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (player.getName() == friend.getName()) {
            return false;
         }
      }

      if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
         if (player instanceof EntityPlayer && !Main.instance.setmgr.getSettingByName("Players").getValue()) {
            return false;
         }

         if (player instanceof EntityAnimal && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
            return false;
         }

         if (player instanceof EntityMob && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
            return false;
         }

         if (player instanceof EntityVillager && !Main.instance.setmgr.getSettingByName("Mobs").getValue()) {
            return false;
         }
      }

      if (player.isInvisible() && !Main.instance.setmgr.getSettingByName("Invisible").getValue()) {
         return false;
      } else if (!RotationUtil.canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble()) && !canSeeEntityAtFov(player, (float)Main.instance.setmgr.getSettingByName("FOV").getValDouble())) {
         return false;
      } else if (!this.range(player, Main.instance.setmgr.getSettingByName("Range").getValDouble())) {
         return false;
      } else if (!player.canEntityBeSeen(mc.player)) {
         return Main.instance.setmgr.getSettingByName("Walls").getValue();
      } else {
         return player != mc.player;
      }
   }

   private boolean range(EntityLivingBase entity, double range) {
      mc.player.getDistanceToEntity(entity);
      return (double)mc.player.getDistanceToEntity(entity) <= range;
   }

   public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
      double diffX = entityLiving.posX - Minecraft.getMinecraft().player.posX;
      double diffZ = entityLiving.posZ - Minecraft.getMinecraft().player.posZ;
      float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
      double difference = angleDifference((double)newYaw, (double)Minecraft.getMinecraft().player.rotationYaw);
      return difference <= (double)scope;
   }

   public boolean isPlayerValid(EntityLivingBase entity) {
      if (entity instanceof EntityPlayer) {
         Collection<NetworkPlayerInfo> playerInfo = mc.player.connection.getPlayerInfoMap();
         Iterator var3 = playerInfo.iterator();

         while(var3.hasNext()) {
            NetworkPlayerInfo info = (NetworkPlayerInfo)var3.next();
            if (info.getGameProfile().getName().matches(entity.getName())) {
               return true;
            }
         }
      }

      return false;
   }

   public static double angleDifference(double a, double b) {
      float yaw360 = (float)(Math.abs(a - b) % 360.0D);
      if (yaw360 > 180.0F) {
         yaw360 = 360.0F - yaw360;
      }

      return (double)yaw360;
   }
}
