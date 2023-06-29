//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.EnumFacing;
import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.util.Random;

public class Utils
{
    public static boolean lookChanged;
    public static float[] rotationsToBlock;
    private static final Random RANDOM;
    
    public static boolean nullCheck() {
        return Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.world() == null;
    }
    
    public static void copy(final String content) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
    }
    
    public static int random(final int min, final int max) {
        return Utils.RANDOM.nextInt(max - min) + min;
    }
    
    public static Vec3d getRandomCenter(final AxisAlignedBB bb) {
        return new Vec3d(bb.minX + (bb.maxX - bb.minX) * 0.8 * Math.random(), bb.minY + (bb.maxY - bb.minY) * Math.random() + 0.1 * Math.random(), bb.minZ + (bb.maxZ - bb.minZ) * 0.8 * Math.random());
    }
    
    public static boolean isMoving(final Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return BlockUtils.getBlock(pos).canCollideCheck(BlockUtils.getState(pos), false);
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
    }
    
    public static List<Entity> getEntityList() {
        return (List<Entity>)Wrapper.INSTANCE.world().getLoadedEntityList();
    }
    
    public static List<EntityPlayer> getPlayersList() {
        return (List<EntityPlayer>)Wrapper.INSTANCE.world().playerEntities;
    }
    
    public static boolean isNullOrEmptyStack(final ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
    
    public static void windowClick(final int windowId, final int slotId, final int mouseButton, final ClickType type) {
        Wrapper.INSTANCE.controller().windowClick(windowId, slotId, mouseButton, type, (EntityPlayer)Wrapper.INSTANCE.player());
    }
    
    public static void swingMainHand() {
        Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
    }
    
    public static void attack(final Entity entity) {
        Wrapper.INSTANCE.controller().attackEntity((EntityPlayer)Wrapper.INSTANCE.player(), entity);
    }
    
    public static void addEffect(final int id, final int duration, final int amplifier) {
        Wrapper.INSTANCE.player().addPotionEffect(new PotionEffect(Potion.getPotionById(id), duration, amplifier));
    }
    
    public static void removeEffect(final int id) {
        Wrapper.INSTANCE.player().removePotionEffect(Potion.getPotionById(id));
    }
    
    public static void clearEffects() {
        for (final PotionEffect effect : Wrapper.INSTANCE.player().getActivePotionEffects()) {
            Wrapper.INSTANCE.player().removePotionEffect(effect.getPotion());
        }
    }
    
    public static double[] teleportToPosition(final double[] startPosition, final double[] endPosition, final double setOffset, final double slack, final boolean extendOffset, final boolean onGround) {
        boolean wasSneaking = false;
        if (Wrapper.INSTANCE.player().isSneaking()) {
            wasSneaking = true;
        }
        double startX = startPosition[0];
        double startY = startPosition[1];
        double startZ = startPosition[2];
        final double endX = endPosition[0];
        final double endY = endPosition[1];
        final double endZ = endPosition[2];
        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
            if (count > 120) {
                break;
            }
            final double offset = (extendOffset && (count & 0x1) == 0x0) ? (setOffset + 0.15) : setOffset;
            final double diffX = startX - endX;
            final double diffY = startY - endY;
            final double diffZ = startZ - endZ;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX += offset;
                }
                else {
                    startX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX -= offset;
                }
                else {
                    startX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY += offset;
                }
                else {
                    startY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY -= offset;
                }
                else {
                    startY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ += offset;
                }
                else {
                    startZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ -= offset;
                }
                else {
                    startZ -= Math.abs(diffZ);
                }
            }
            if (wasSneaking) {
                Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.STOP_SNEAKING));
            }
            Wrapper.INSTANCE.mc().getConnection().getNetworkManager().sendPacket((Packet)new CPacketPlayer.Position(startX, startY, startZ, onGround));
            ++count;
        }
        if (wasSneaking) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketEntityAction((Entity)Wrapper.INSTANCE.player(), CPacketEntityAction.Action.START_SNEAKING));
        }
        return new double[] { startX, startY, startZ };
    }
    
    public static void selfDamage(final double posY) {
        if (!Wrapper.INSTANCE.player().onGround) {
            return;
        }
        for (int i = 0; i <= 64.0; ++i) {
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + posY, Wrapper.INSTANCE.player().posZ, false));
            Wrapper.INSTANCE.sendPacket((Packet)new CPacketPlayer.Position(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, i == 64.0));
        }
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        player.motionX *= 0.2;
        final EntityPlayerSP player2 = Wrapper.INSTANCE.player();
        player2.motionZ *= 0.2;
        swingMainHand();
    }
    
    public static boolean checkEnemyNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        return !getEntityNameColor((EntityLivingBase)Wrapper.INSTANCE.player()).equals(getEntityNameColor(entity));
    }
    
    public static String getEntityNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        if (name.contains("§")) {
            if (name.contains("§1")) {
                return "§1";
            }
            if (name.contains("§2")) {
                return "§2";
            }
            if (name.contains("§3")) {
                return "§3";
            }
            if (name.contains("§4")) {
                return "§4";
            }
            if (name.contains("§5")) {
                return "§5";
            }
            if (name.contains("§6")) {
                return "§6";
            }
            if (name.contains("§7")) {
                return "§7";
            }
            if (name.contains("§8")) {
                return "§8";
            }
            if (name.contains("§9")) {
                return "§9";
            }
            if (name.contains("§0")) {
                return "§0";
            }
            if (name.contains("§e")) {
                return "§e";
            }
            if (name.contains("§d")) {
                return "§d";
            }
            if (name.contains("§a")) {
                return "§a";
            }
            if (name.contains("§b")) {
                return "§b";
            }
            if (name.contains("§c")) {
                return "§c";
            }
            if (name.contains("§f")) {
                return "§f";
            }
        }
        return "null";
    }
    
    public static int getPlayerArmorColor(final EntityPlayer player, final ItemStack stack) {
        if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor)) {
            return -1;
        }
        final ItemArmor itemArmor = (ItemArmor)stack.getItem();
        if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) {
            return -1;
        }
        return itemArmor.getColor(stack);
    }
    
    public static boolean checkEnemyColor(final EntityPlayer enemy) {
        final int colorEnemy0 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
        final int colorEnemy2 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
        final int colorEnemy3 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
        final int colorEnemy4 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));
        final int colorPlayer0 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(0));
        final int colorPlayer2 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(1));
        final int colorPlayer3 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(2));
        final int colorPlayer4 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(3));
        return (colorEnemy0 != colorPlayer0 || colorPlayer0 == -1 || colorEnemy0 == 1) && (colorEnemy2 != colorPlayer2 || colorPlayer2 == -1 || colorEnemy2 == 1) && (colorEnemy3 != colorPlayer3 || colorPlayer3 == -1 || colorEnemy3 == 1) && (colorEnemy4 != colorPlayer4 || colorPlayer4 == -1 || colorEnemy4 == 1);
    }
    
    public static boolean screenCheck() {
        return !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiScreen);
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static EntityLivingBase getWorldEntityByName(final String name) {
        EntityLivingBase entity = null;
        for (final Object object : getEntityList()) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entityForCheck = (EntityLivingBase)object;
                if (!entityForCheck.getName().contains(name)) {
                    continue;
                }
                entity = entityForCheck;
            }
        }
        return entity;
    }
    
    public static float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg((World)Wrapper.INSTANCE.world());
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        final EntityEgg entityEgg = var4;
        entityEgg.posX += var3.getDirectionVec().getX() * 0.25;
        final EntityEgg entityEgg2 = var4;
        entityEgg2.posY += var3.getDirectionVec().getY() * 0.25;
        final EntityEgg entityEgg3 = var4;
        entityEgg3.posZ += var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity((Entity)var4);
    }
    
    private static float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + Wrapper.INSTANCE.player().rotationYaw, getPitch(var0) + Wrapper.INSTANCE.player().rotationPitch };
    }
    
    public static float getPitch(final Entity entity) {
        final double x = entity.posX - Wrapper.INSTANCE.player().posX;
        double y = entity.posY - Wrapper.INSTANCE.player().posY;
        final double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
        y /= Wrapper.INSTANCE.player().getDistance(entity);
        double pitch = Math.asin(y) * 57.29577951308232;
        pitch = -pitch;
        return (float)pitch;
    }
    
    public static float getYaw(final Entity entity) {
        final double x = entity.posX - Wrapper.INSTANCE.player().posX;
        final double y = entity.posY - Wrapper.INSTANCE.player().posY;
        final double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
        double yaw = Math.atan2(x, z) * 57.29577951308232;
        yaw = -yaw;
        return (float)yaw;
    }
    
    public static float[] getNeededRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Wrapper.INSTANCE.player().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw), Wrapper.INSTANCE.player().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch) };
    }
    
    public static float getDirection() {
        float var1 = Wrapper.INSTANCE.player().rotationYaw;
        if (Wrapper.INSTANCE.player().moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Wrapper.INSTANCE.player().moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Wrapper.INSTANCE.player().moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Wrapper.INSTANCE.player().moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Wrapper.INSTANCE.player().moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity, final float width, final float height) {
        final EntitySize size = getEntitySize(entity);
        entity.width = size.width;
        entity.height = size.height;
        final double d0 = width / 2.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + height, entity.posZ + d0));
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity) {
        final EntitySize size = getEntitySize(entity);
        entity.width = size.width;
        entity.height = size.height;
        final double d0 = entity.width / 2.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    public static EntitySize getEntitySize(final Entity entity) {
        final EntitySize entitySize = new EntitySize(0.6f, 1.8f);
        return entitySize;
    }
    
    public static boolean isInsideBlock(final EntityLivingBase entity) {
        for (int x = MathHelper.floor(entity.getEntityBoundingBox().minX); x < MathHelper.floor(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor(entity.getEntityBoundingBox().minY); y < MathHelper.floor(entity.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor(entity.getEntityBoundingBox().minZ); z < MathHelper.floor(entity.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = BlockUtils.getBlock(new BlockPos(x, y, z));
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(BlockUtils.getState(new BlockPos(x, y, z)), (IBlockAccess)Wrapper.INSTANCE.world(), new BlockPos(x, y, z))) != null && entity.getEntityBoundingBox().intersects(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isBlockEdge(final EntityLivingBase entity) {
        return Wrapper.INSTANCE.world().getCollisionBoxes((Entity)entity, entity.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(0.001, 0.0, 0.001)).isEmpty() && entity.onGround;
    }
    
    public static void faceEntity(final EntityLivingBase entity) {
        if (entity == null) {
            return;
        }
        final double d0 = entity.posX - Wrapper.INSTANCE.player().posX;
        final double d2 = entity.posY - Wrapper.INSTANCE.player().posY;
        final double d3 = entity.posZ - Wrapper.INSTANCE.player().posZ;
        final double d4 = Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double d5 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        final float f = (float)(Math.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(Math.atan2(d4, d5) * 180.0 / 3.141592653589793));
        Wrapper.INSTANCE.player().rotationYaw = f;
        Wrapper.INSTANCE.player().rotationPitch = f2;
    }
    
    public static void assistFaceEntity(final Entity entity, final float yaw, final float pitch) {
        if (entity == null) {
            return;
        }
        final double diffX = entity.posX - Wrapper.INSTANCE.player().posX;
        final double diffZ = entity.posZ - Wrapper.INSTANCE.player().posZ;
        double yDifference;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        }
        else {
            yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
        }
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationPitch = (float)(-(Math.atan2(yDifference, dist) * 180.0 / 3.141592653589793));
        if (yaw > 0.0f) {
            Wrapper.INSTANCE.player().rotationYaw = updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4.0f);
        }
        if (pitch > 0.0f) {
            Wrapper.INSTANCE.player().rotationPitch = updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4.0f);
        }
    }
    
    public static float updateRotation(final float angle, final float targetAngle, final float maxIncrease) {
        float var4 = MathHelper.wrapDegrees(targetAngle - angle);
        if (var4 > maxIncrease) {
            var4 = maxIncrease;
        }
        if (var4 < -maxIncrease) {
            var4 = -maxIncrease;
        }
        return angle + var4;
    }
    
    public static int getDistanceFromMouse(final EntityLivingBase entity) {
        final float[] neededRotations = getRotationsNeeded((Entity)entity);
        if (neededRotations != null) {
            final float neededYaw = Wrapper.INSTANCE.player().rotationYaw - neededRotations[0];
            final float neededPitch = Wrapper.INSTANCE.player().rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f);
            return (int)distanceFromMouse;
        }
        return -1;
    }
    
    public static float[] getSmoothNeededRotations(final Vec3d vec, final float yaw, final float pitch) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float rotationPitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4.0f), updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4.0f) };
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Wrapper.INSTANCE.mc().player.posX;
        final double diffZ = entity.posZ - Wrapper.INSTANCE.mc().player.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight());
        }
        else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.INSTANCE.mc().player.posY + Wrapper.INSTANCE.mc().player.getEyeHeight());
        }
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Wrapper.INSTANCE.mc().player.rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.mc().player.rotationYaw), Wrapper.INSTANCE.mc().player.rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.mc().player.rotationPitch) };
    }
    
    static {
        Utils.rotationsToBlock = null;
        RANDOM = new Random();
    }
}
