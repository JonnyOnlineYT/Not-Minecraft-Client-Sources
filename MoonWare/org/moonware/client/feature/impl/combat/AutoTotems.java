package org.moonware.client.feature.impl.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTotems extends Feature {
    public static ListSetting type  = new ListSetting("Mode", "Matrix", "Matrix","Other");
    public static BooleanSetting checkCrystal = new BooleanSetting("Check Crystal", false);
    public static BooleanSetting fall = new BooleanSetting("Check FallDist", false);
    public static NumberSetting fallDistance = new NumberSetting("FallDistance", 5,4,40,1,()-> fall.get());
    public static BooleanSetting obs = new BooleanSetting("Check obsidian", false);
    public static NumberSetting obsDist = new NumberSetting("Distance to obsidian", 6,4,40,1, obs::get);
    public static NumberSetting crystaldst = new NumberSetting("Distance to crystal",6,4,40,1, checkCrystal::get);
    public static BooleanSetting checkTnt = new BooleanSetting("Check TNT",false);
    public static NumberSetting tntDist = new NumberSetting("Distance to TNT", 6,4,40,1, checkTnt::get);
    public static NumberSetting health = new NumberSetting("Health", 6,0,20,0.1F);
    public static int swapBack = -1;
    public static long delay;

    public AutoTotems() {
        super("AutoTotem", "", Type.Combat);
        addSettings(type, health, checkCrystal, crystaldst, obs, obsDist, checkTnt, tntDist, fall, fallDistance);
    }
    public static int getSlotIDFromItem(Item item) {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }
    @EventTarget
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (getState()) {
                if (type.getCurrentMode().equalsIgnoreCase("Other")) {
                    float hp = Minecraft.player.getHealth();
                    boolean trr =false;
                    if (trr)
                        hp += Minecraft.player.getAbsorptionAmount();
                    int totem = getSlotIDFromItem(Items.field_190930_cZ);
                    int stackSizeHand = Minecraft.player.getHeldItemOffhand().stackSize;
                    boolean handNotNull = !(Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemAir);
                    boolean handTotem = Minecraft.player.getHeldItemOffhand().getItem() == Items.field_190930_cZ;
                    boolean totemCheck = ((hp <= health.getCurrentValue() || checkTNT() || checkCrystal() || checkFall()
                            || checkObsidian()) && getState());
                    if (System.currentTimeMillis() < delay) {
                        return;
                    }
                    if (totemCheck) {
                        if (totem >= 0) {
                            if (!handTotem) {
                                Minecraft.playerController.windowClick(0, totem, 1, ClickType.PICKUP, Minecraft.player);
                                Minecraft.playerController.windowClick(0, 45, 1, ClickType.PICKUP, Minecraft.player);
                                if (handNotNull) {
                                    Minecraft.playerController.windowClick(0, totem, 0, ClickType.PICKUP, Minecraft.player);
                                    if (swapBack == -1)
                                        swapBack = totem;
                                }
                                delay = System.currentTimeMillis() + 300;
                            }
                        }
                        return;
                    }
                    if (swapBack >= 0) {
                        Minecraft.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, Minecraft.player);
                        Minecraft.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Minecraft.player);
                        if (handNotNull)
                            Minecraft.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, Minecraft.player);
                        swapBack = -1;
                        delay = System.currentTimeMillis() + 300;
                        return;
                    }
                } else {
                    int totemSlot = getTotemSlot();
                    if (totemSlot < 9 && totemSlot != -1) {
                        totemSlot += 36;
                    }
                    float hp = Minecraft.player.getHealth();
                    boolean tr = false;
                    if (tr) {
                        hp += Minecraft.player.getAbsorptionAmount();
                    }
                    int prevCurrentItem = Minecraft.player.inventory.currentItem;
                    int currentItem = findNearestCurrentItem();
                    ItemStack prevHeldItem = Minecraft.player.getHeldItemOffhand();
                    boolean totemCheck = health.get() >= hp || checkCrystal() || checkFall() || checkTNT()
                            || checkObsidian();
                    boolean handNotNull = !(Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemAir);
                    boolean totemInHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.field_190930_cZ;
                    if (totemCheck) {
                        if (totemSlot >= 0 && !totemInHand) {
                            Minecraft.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, Minecraft.player);
                            Helper.mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
                            Minecraft.player.inventory.currentItem = currentItem;
                            ItemStack itemstack = Minecraft.player.getHeldItem(EnumHand.OFF_HAND);
                            Minecraft.player.setHeldItem(EnumHand.OFF_HAND, Minecraft.player.getHeldItem(EnumHand.MAIN_HAND));
                            Minecraft.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                            Helper.mc.getConnection().sendPacket(
                                    new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                            Helper.mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
                            Minecraft.player.inventory.currentItem = prevCurrentItem;
                            Minecraft.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, Minecraft.player);
                            if (swapBack == -1)
                                swapBack = totemSlot;
                            return;
                        }
                        if (totemInHand) {
                            return;
                        }
                    }
                    if (swapBack >= 0) {
                        Helper.mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
                        Minecraft.player.inventory.currentItem = currentItem;
                        ItemStack itemstack = Minecraft.player.getHeldItem(EnumHand.OFF_HAND);
                        Minecraft.player.setHeldItem(EnumHand.OFF_HAND, Minecraft.player.getHeldItem(EnumHand.MAIN_HAND));
                        Minecraft.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                        Helper.mc.getConnection().sendPacket(
                                new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                        Minecraft.playerController.windowClick(0, swapBack, currentItem, ClickType.SWAP, Minecraft.player);
                        Helper.mc.getConnection().sendPacket(
                                new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                        itemstack = Minecraft.player.getHeldItem(EnumHand.OFF_HAND);
                        Minecraft.player.setHeldItem(EnumHand.OFF_HAND, Minecraft.player.getHeldItem(EnumHand.MAIN_HAND));
                        Minecraft.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                        Helper.mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
                        Minecraft.player.inventory.currentItem = prevCurrentItem;
                        swapBack = -1;
                    }
                }
            }
        }
    }
    public static int getTotemSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.field_190930_cZ) {
                return i;
            }
        }
        return -1;
    }

    public static int findNearestCurrentItem() {
        int currentItem = Minecraft.player.inventory.currentItem;
        if (currentItem == 8) {
            return 7;
        }
        if (currentItem == 0) {
            return 1;
        }
        return currentItem - 1;
    }

//    public void middleClickPearl(Event event) {
//        if (((EventMouseTick) event).getButton() == 2
//                && mc.player.getCooldownTracker().getCooldown(Items.ENDER_PEARL, 1) == 0
//                && InventoryUtils.getPearls() >= 0) {
//            mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryHelper.getPearls()));
//            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
//            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
//        }
//    }

    private boolean checkCrystal() {
        if (!checkCrystal.isEnabled(false)) {
            return false;
        }
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal
                    && Minecraft.player.getDistanceToEntity(entity) <= crystaldst.get()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTNT() {
        if (!checkTnt.isEnabled(false)) {
            return false;
        }
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && Minecraft.player.getDistanceToEntity(entity) <= tntDist.get()) {
                return true;
            }
            if (entity instanceof EntityMinecartTNT
                    && Minecraft.player.getDistanceToEntity(entity) <= tntDist.get()) {
                return true;
            }
        }
        return false;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = Minecraft.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }

    private boolean checkObsidian() {
        if (!obs.isEnabled(false)) {
            return false;
        }
        BlockPos pos = getSphere(getPlayerPosLocal(), (float) obsDist.get(), 6, false, true, 0).stream()
                .filter(this::IsValidBlockPos)
                .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(Minecraft.player, blockPos))).orElse(null);
        return pos != null;
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double getDistance(double n, double n2, double n3, double n4, double n5,
                                     double n6) {
        double n7 = n - n4;
        double n8 = n2 - n5;
        double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    public static BlockPos getPlayerPosLocal() {
        if (Minecraft.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(Minecraft.player.posX), Math.floor(Minecraft.player.posY), Math.floor(Minecraft.player.posZ));
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b,
                                           boolean b2, int n3) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
                    double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }

    private boolean checkFall() {
        if (!fall.isEnabled(false)) {
            return false;
        }
        return Minecraft.player.fallDistance > fallDistance.getCurrentValue();
    }
}
