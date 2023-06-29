package me.wintware.client.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class TargetStrafe extends Module {
   public static Setting range;
   public static Setting speed;
   public static int direction = -1;
   public boolean sideDirection = true;

   public TargetStrafe() {
      super("TargetStrafe", Category.Combat);
      ArrayList<String> mode = new ArrayList();
      mode.add("Circle");
      mode.add("16");
      Main.instance.setmgr.rSetting(new Setting("ESP Mode", this, "Circle", mode));
      Main.instance.setmgr.rSetting(range = new Setting("Distance", this, 3.0D, 0.1D, 6.0D, false));
      Main.instance.setmgr.rSetting(speed = new Setting("Speed", this, 0.35D, 0.1D, 10.0D, false));
      Main.instance.setmgr.rSetting(new Setting("BoostValue", this, 0.5D, 0.1D, 4.0D, false));
      Main.instance.setmgr.rSetting(new Setting("DamageBoost", this, true));
      Main.instance.setmgr.rSetting(new Setting("AutoJump", this, true));
   }

   public Entity getTargetEz() {
      if (mc.player != null && !mc.player.isDead) {
         List list = (List)mc.world.loadedEntityList.stream().filter((entity) -> {
            return entity != mc.player;
         }).filter((entity) -> {
            return mc.player.getDistanceToEntity(entity) <= 20.0F;
         }).filter((entity) -> {
            return !entity.isDead;
         }).filter(this::attackCheck).sorted(Comparator.comparing((entity) -> {
            return mc.player.getDistanceToEntity(entity);
         })).collect(Collectors.toList());
         return list.size() > 0 ? (Entity)list.get(0) : null;
      } else {
         return null;
      }
   }

   public boolean attackCheck(Entity entity) {
      if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityMob) && !(entity instanceof EntityAnimal)) {
         return entity.isInvisible() && !Main.instance.setmgr.getSettingByName("Invisible").getValue() ? false : false;
      } else {
         return true;
      }
   }

   public void onMotionUpdate() {
      double move = speed.getValDouble();
      Entity entity = this.getTargetEz();
      if (entity != null) {
         if (Main.instance.setmgr.getSettingByName("AutoJump").getValue() && mc.player.onGround) {
            mc.player.jump();
         }

         float[] rotations = RotationUtil.getRotations(KillAura.target);
         if ((double)mc.player.getDistanceToEntity(entity) <= range.getValDouble()) {
            if (mc.player.hurtTime > 0 && Main.instance.setmgr.getSettingByName("DamageBoost").getValue()) {
               MovementUtil.setMotion((double)(speed.getValFloat() + Main.instance.setmgr.getSettingByName("BoostValue").getValFloat()), rotations[0], (double)direction, 0.0D);
            } else {
               MovementUtil.setMotion((double)speed.getValFloat(), rotations[0], (double)direction, 0.0D);
            }
         } else if (mc.player.hurtTime > 0 && Main.instance.setmgr.getSettingByName("DamageBoost").getValue()) {
            MovementUtil.setMotion((double)(speed.getValFloat() + Main.instance.setmgr.getSettingByName("BoostValue").getValFloat()), rotations[0], (double)direction, 1.0D);
         } else {
            MovementUtil.setMotion((double)speed.getValFloat(), rotations[0], (double)direction, 1.0D);
         }
      }

   }

   @EventTarget
   public void onUpdate(EventPreMotionUpdate event) {
      if (mc.player.isCollidedHorizontally) {
         this.switchDirection();
      }

      if (mc.gameSettings.keyBindLeft.isPressed()) {
         direction = 1;
      }

      if (mc.gameSettings.keyBindRight.isPressed()) {
         direction = -1;
      }

   }

   public void switchDirection() {
      if (direction == 1) {
         direction = -1;
      } else {
         direction = 1;
      }

   }

   @EventTarget
   public void onRender3D(Event3D e) {
      Entity entity = this.getTargetEz();
      EntityLivingBase entityLivingBase = KillAura.target;
      String mode = Main.instance.setmgr.getSettingByName("Rotation Mode").getValString();
      if (entity != null) {
         this.drawCircle(entity, Main.instance.setmgr.getSettingByName("Distance").getValDouble(), e.getPartialTicks());
      }

   }

   private void drawCircle(Entity entity, double rad, float partialTicks) {
      GlStateManager.enableDepth();

      for(double il = 0.0D; il < 0.001D; il += 0.01D) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glEnable(2848);
         GL11.glEnable(2881);
         GL11.glEnable(2832);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glHint(3154, 4354);
         GL11.glHint(3155, 4354);
         GL11.glHint(3153, 4354);
         GL11.glDisable(2929);
         GL11.glLineWidth(2.5F);
         GL11.glBegin(3);
         String mode = Main.instance.setmgr.getSettingByName("ESP Mode").getValString();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;

         for(int i = 0; i <= 90; ++i) {
            GL11.glColor3f((float)(new Color(255, 255, 255)).getRed(), (float)(new Color(255, 255, 255)).getGreen(), (float)(new Color(255, 255, 255)).getBlue());
            if (mode.equalsIgnoreCase("16")) {
               GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586D / 16.0D), y, z + rad * Math.sin((double)i * 6.283185307179586D / 16.0D));
            }

            if (mode.equalsIgnoreCase("Circle")) {
               GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586D / 70.0D), y, z + rad * Math.sin((double)i * 6.283185307179586D / 70.0D));
            }
         }
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glDisable(2881);
      GL11.glEnable(2832);
      GL11.glEnable(3553);
      GL11.glPopMatrix();
      GlStateManager.color(255.0F, 255.0F, 255.0F);
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      double speed = Main.instance.setmgr.getSettingByName("Speed").getValDouble();
      double range = Main.instance.setmgr.getSettingByName("Distance").getValDouble();
      String mode = Main.instance.setmgr.getSettingByName("DamageBoost").getValString();
      if (mc.player.isCollidedHorizontally) {
         this.switchDirection();
      }

      String lmao = Main.instance.setmgr.getSettingByName("DamageBoost").getValue() ? speed + ", " + range + ", DamageBoost" : speed + ", " + range;
      this.setDisplayName("TargetStrafe " + ChatFormatting.GRAY + lmao);
      this.onMotionUpdate();
   }
}
