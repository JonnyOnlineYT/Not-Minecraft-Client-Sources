package org.moonware.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventSafeWalk;
import org.moonware.client.event.events.impl.motion.EventStrafe;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.player.InventoryHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Scaffold extends Feature {

    public static List<Block> invalidBlocks = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_SLAB, Blocks.WOODEN_SLAB, Blocks.STONE_SLAB2, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.CACTUS, Blocks.LADDER, Blocks.WEB, Blocks.PUMPKIN);
    public static BlockData data;
    public static boolean isSneaking;
    public static BooleanSetting down;
    public static BooleanSetting sprintoff;
    public static BooleanSetting rotationRandom = new BooleanSetting("Rotation Random", true, () -> true);
    public static NumberSetting rotationSpeed = new NumberSetting("Rotation Speed", 360, 1, 360, 1, () -> true);
    private final TimerHelper time = new TimerHelper();
    private final BooleanSetting jump;
    private final BooleanSetting swing;
    private final NumberSetting delay;
    private final NumberSetting delayRandom;
    private final NumberSetting chance;
    private final NumberSetting speed;
    private final BooleanSetting rotStrafe;
    private final BooleanSetting safewalk;
    private final ListSetting blockRotation;
    private final ListSetting towerMode;
    public NumberSetting rotPitchRandom = new NumberSetting("Rotation Pitch Random", 2, 0, 8, 0.01f, () -> rotationRandom.getBoolValue());
    public NumberSetting rotYawRandom = new NumberSetting("Rotation Yaw Random", 2, 0, 8, 0.01f, () -> rotationRandom.getBoolValue());
    public BooleanSetting airCheck = new BooleanSetting("Check Air", true, () -> true);
    public BooleanSetting sneak = new BooleanSetting("Sneak", true, () -> true);
    public NumberSetting sneakChance = new NumberSetting("Sneak Chance", 100, 0, 100, 1, () -> sneak.getBoolValue());
    public NumberSetting sneakSpeed = new NumberSetting("Sneak Speed", 0.05F, 0.01F, 1, 0.01f, () -> sneak.getBoolValue());
    public ListSetting sneakMode = new ListSetting("Sneak Mode", "Packet", () -> sneak.getBoolValue(), "Packet", "Client");
    public NumberSetting rotationOffset = new NumberSetting("Rotation Offset", 0.25F, 0F, 1F, 0.01F, () -> true);
    public NumberSetting placeOffset = new NumberSetting("Place Offset", 0.20F, 0.01F, 0.3F, 0.01F, () -> true);
    private int slot;

    public Scaffold() {
        super("Scaffold", "Автоматически ставит под вас блоки", Type.Movement);

        blockRotation = new ListSetting("BlockRotation Mode", "Matrix", () -> true, "Matrix", "None");

        towerMode = new ListSetting("Tower Mode", "Matrix", () -> true, "Matrix", "NCP", "Default");

        chance = new NumberSetting("Chance", 100, 0, 100, 1, () -> true);
        delay = new NumberSetting("Min Delay", 0, 0, 300, 1, () -> true);
        delayRandom = new NumberSetting("Random Delay", 0, 0, 1000, 1, () -> true);
        speed = new NumberSetting("Speed", 0.6f, 0.05f, 1.2f, 0.01f, () -> true);
        sprintoff = new BooleanSetting("Stop Sprinting", true, () -> true);
        safewalk = new BooleanSetting("SafeWalk", true, () -> true);
        jump = new BooleanSetting("Jump", false, () -> true);
        down = new BooleanSetting("DownWard", false, () -> true);
        swing = new BooleanSetting("SwingHand", false, () -> true);
        rotStrafe = new BooleanSetting("Rotation Strafe", false, () -> true);
        addSettings(blockRotation, towerMode, chance, delay, delayRandom, rotationOffset, placeOffset, rotationSpeed, rotationRandom, rotYawRandom, rotPitchRandom, speed, sneak, sneakMode, sneakChance, sneakSpeed, sprintoff, airCheck, safewalk, jump, down, swing, rotStrafe);
    }

    public static int searchBlock() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    private boolean canPlace() {
        BlockPos bp1 = new BlockPos(Minecraft.player.posX - placeOffset.getNumberValue(), Minecraft.player.posY - placeOffset.getNumberValue(), Minecraft.player.posZ - placeOffset.getNumberValue());
        BlockPos bp2 = new BlockPos(Minecraft.player.posX - placeOffset.getNumberValue(), Minecraft.player.posY - placeOffset.getNumberValue(), Minecraft.player.posZ + placeOffset.getNumberValue());
        BlockPos bp3 = new BlockPos(Minecraft.player.posX + placeOffset.getNumberValue(), Minecraft.player.posY - placeOffset.getNumberValue(), Minecraft.player.posZ + placeOffset.getNumberValue());
        BlockPos bp4 = new BlockPos(Minecraft.player.posX + placeOffset.getNumberValue(), Minecraft.player.posY - placeOffset.getNumberValue(), Minecraft.player.posZ - placeOffset.getNumberValue());
        return (Minecraft.player.world.getBlockState(bp1).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp2).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp3).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp4).getBlock() == Blocks.AIR);
    }

    private boolean canSneak() {
        BlockPos bp1 = new BlockPos(Minecraft.player.posX - sneakSpeed.getNumberValue(), Minecraft.player.posY - sneakSpeed.getNumberValue(), Minecraft.player.posZ - sneakSpeed.getNumberValue());
        BlockPos bp2 = new BlockPos(Minecraft.player.posX - sneakSpeed.getNumberValue(), Minecraft.player.posY - sneakSpeed.getNumberValue(), Minecraft.player.posZ + sneakSpeed.getNumberValue());
        BlockPos bp3 = new BlockPos(Minecraft.player.posX + sneakSpeed.getNumberValue(), Minecraft.player.posY - sneakSpeed.getNumberValue(), Minecraft.player.posZ + sneakSpeed.getNumberValue());
        BlockPos bp4 = new BlockPos(Minecraft.player.posX + sneakSpeed.getNumberValue(), Minecraft.player.posY - sneakSpeed.getNumberValue(), Minecraft.player.posZ - sneakSpeed.getNumberValue());
        return (Minecraft.player.world.getBlockState(bp1).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp2).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp3).getBlock() == Blocks.AIR) && (Minecraft.player.world.getBlockState(bp4).getBlock() == Blocks.AIR);
    }

    @Override
    public void onEnable() {
        slot = Minecraft.player.inventory.currentItem;
        data = null;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Minecraft.player.inventory.currentItem = slot;
        Minecraft.timer.timerSpeed = 1f;
        Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Minecraft.gameSettings.keyBindSneak.pressed = false;
        super.onDisable();
    }

    @EventTarget
    public void onStrafeMotion(EventStrafe eventStrafe) {
        if (rotStrafe.getBoolValue()) {
            eventStrafe.setCancelled(true);
            MovementHelper.calculateSilentMove(eventStrafe, RotationHelper.Rotation.packetYaw);
        }
    }

    @EventTarget
    public void onSafe(EventSafeWalk eventSafeWalk) {
        if (safewalk.getBoolValue() && !isSneaking) {
            eventSafeWalk.setCancelled(Minecraft.player.onGround);
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion eventUpdate) {
        String tower = towerMode.getCurrentMode();
        setSuffix(blockRotation.getCurrentMode());
        if (tower.equalsIgnoreCase("Matrix")) {
            if (!MovementHelper.isMoving()) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.jump();
                }
                if (Minecraft.player.motionY > 0 && !Minecraft.player.onGround) {
                    Minecraft.player.motionY -= 0.00994;
                } else {
                    Minecraft.player.motionY -= 0.00995;
                }
            }
        } else if (tower.equalsIgnoreCase("NCP")) {
            if (!MovementHelper.isMoving()) {
                if (Minecraft.player.onGround && Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.jump();
                }
                float pos = -2F;
                if (Minecraft.player.motionY < 0.1 && !(Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ).add(0, pos, 0)).getBlock() instanceof BlockAir)) {
                    Minecraft.player.motionY -= 190;
                }
            }
        }

        if (Minecraft.gameSettings.keyBindSneak.pressed && down.getBoolValue()) {
            Minecraft.gameSettings.keyBindSneak.pressed = false;
            isSneaking = true;
        } else {
            isSneaking = false;
        }

        Minecraft.player.motionX *= speed.getNumberValue();
        Minecraft.player.motionZ *= speed.getNumberValue();

        if (InventoryHelper.doesHotbarHaveBlock()) {
        } else if (!(Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemBlock) && searchBlock() != -1) {
            Minecraft.playerController.windowClick(0, searchBlock(), 1, ClickType.QUICK_MOVE, Minecraft.player);
        }
        double yDif;
        double posY;
        BlockPos blockPos = isSneaking ? new BlockPos(Minecraft.player).add(1, -1, 0).down() : new BlockPos(Minecraft.player).add(0, -1, 0);
        for (posY = Minecraft.player.posY - 1; posY > 0; posY--) {
            BlockData newData = getBlockData(blockPos);
            if (newData != null) {
                yDif = Minecraft.player.posY - posY;
                if (yDif <= 7) {
                    data = newData;
                }
            }
        }
        if (sprintoff.getBoolValue()) {
            Minecraft.player.setSprinting(false);
        }
        if (data != null && slot != -1 && !Minecraft.player.isInLiquid()) {
            Vec3d hitVec = getVectorToRotate(data);
            if (blockRotation.getOptions().equalsIgnoreCase("Matrix")) {
                float[] rots = RotationHelper.getRotationVector(hitVec, rotationRandom.getBoolValue(), rotYawRandom.getNumberValue(), rotPitchRandom.getNumberValue(), rotationSpeed.getNumberValue());
                eventUpdate.setYaw(rots[0]);
                eventUpdate.setPitch(rots[1]);

                if (Minecraft.world.getBlockState(blockPos).getBlock() == Blocks.AIR && !airCheck.getBoolValue()) {
                    Minecraft.player.renderYawOffset = rots[0];
                    Minecraft.player.rotationYawHead = rots[0];
                    Minecraft.player.rotationPitchHead = rots[1];
                } else {
                    Minecraft.player.renderYawOffset = rots[0];
                    Minecraft.player.rotationYawHead = rots[0];
                    Minecraft.player.rotationPitchHead = rots[1];
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (InventoryHelper.doesHotbarHaveBlock()) {
            if (data != null) {
                int slot = -1;
                int lastItem = Minecraft.player.inventory.currentItem;
                BlockPos pos = data.pos;
                Vec3d hitVec = getVectorToPlace(data);
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
                    if (isValidItem(itemStack.getItem())) {
                        slot = i;
                    }
                }
                if (slot != -1) {
                    if (jump.getBoolValue()) {
                        if (!Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                            if (Minecraft.player.onGround) {
                                Minecraft.player.jump();
                            }
                        }
                    }
                    if (!jump.getBoolValue() && InventoryHelper.doesHotbarHaveBlock() && MovementHelper.isMoving() && !Minecraft.gameSettings.keyBindJump.isKeyDown() && sneak.getBoolValue() && MWUtils.randomFloat(0f, 100f) <= sneakChance.getNumberValue()) {
                        if (InventoryHelper.doesHotbarHaveBlock()) {
                            if (canSneak()) {
                                if (sneakMode.currentMode.equals("Packet")) {
                                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_RIDING_JUMP));
                                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SNEAKING));
                                } else if (sneakMode.currentMode.equals("Client")) {
                                    Minecraft.gameSettings.keyBindSneak.pressed = true;
                                }
                            } else {
                                if (sneakMode.currentMode.equals("Packet")) {
                                    Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
                                } else if (sneakMode.currentMode.equals("Client")) {
                                    Minecraft.gameSettings.keyBindSneak.pressed = false;
                                }
                            }
                        }
                    }
                    if (time.hasReached(delay.getNumberValue() + MWUtils.randomFloat(0, delayRandom.getNumberValue()))) {
                        if (canPlace()) {
                            if (MWUtils.randomFloat(0f, 100f) <= chance.getNumberValue()) {
                                Minecraft.player.inventory.currentItem = slot;
                            }
                            Minecraft.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, pos, data.face, hitVec, EnumHand.MAIN_HAND);
                            if (swing.getBoolValue()) {
                                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                            } else {
                                Minecraft.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                            }
                            Minecraft.player.inventory.currentItem = lastItem;
                            time.reset();
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        if (data != null && slot != -1) {
            double x = Minecraft.player.posX;
            double y = Minecraft.player.posY;
            double z = Minecraft.player.posZ;
            double yaw = Minecraft.player.rotationYaw * 0.017453292;
            BlockPos below = new BlockPos(x - Math.sin(yaw), y - 1, z + Math.cos(yaw));
            //RenderHelper.blockEsp(below, Color.BLUE, true);
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D render) {
        float width = render.getResolution().getScaledWidth();
        float height = render.getResolution().getScaledHeight();
        String blockString = getBlockCount() + " Blocks";
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
//        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
//        GL11.glVertex2d(sr.getScaledWidth() / 2 - 6, sr.getScaledHeight() - 65);
//        GL11.glVertex2d(sr.getScaledWidth() / 2, sr.getScaledHeight() - 57);
//        GL11.glVertex2d(sr.getScaledWidth() / 2 + 6, sr.getScaledHeight() - 65);
//
//        GL11.glEnd();
//
//        GL11.glBegin(GL11.GL_LINE_LOOP);
//        GL11.glColor4f(1, 1, 1, 1);
//        GL11.glVertex2d(sr.getScaledWidth() / 2 - 6, sr.getScaledHeight() - 65);
//        GL11.glVertex2d(sr.getScaledWidth() / 2, sr.getScaledHeight() - 57);
//        GL11.glVertex2d(sr.getScaledWidth() / 2 + 6, sr.getScaledHeight() - 65);
//
//        GL11.glEnd();
//
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GlStateManager.pushMatrix();
        GlStateManager.translate(23, 15, 0);
        //RectHelper.drawSkeetRectWithoutBorder(width / 2F + 88 - Helper.mc.circleregular.getStringWidth(blockString), height / 2 - Helper.mc.circleregular.getStringHeight(blockString) + 59, width / 2F + Helper.mc.circleregular.getStringHeight(blockString) + 2, height / 2 - Helper.mc.circleregular.getStringHeight(blockString) / 2F - 55);
        float x = width / 2f + 88 - MWFont.MONTSERRAT_BOLD.get(14).getWidth(blockString);
        float y = height / 2 - MWFont.MONTSERRAT_BOLD.get(14).getWidth(blockString) + 59;
        RoundedUtil.drawRound(x - 150,y - 150, 50,50,3,true,new Color(61,61,61,125));
        RoundedUtil.drawGradientCornerLR(x - 40,y - 38, 45, 15,3,new Color(PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB()),PaletteHelper.rainbow((int) (y * ArrayList.time.get()), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue()));
        //RoundedUtil.drawRound(x,y, 5, 5,255,new Color(PaletteHelper.astolfoarray2(false, (int) y * 4).getRGB()),PaletteHelper.rainbow((int) (y * ArrayList.time.get()), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue()));
        MWFont.MONTSERRAT_BOLD.get(14).drawCenter(blockString, width / 2F + 71 - MWFont.MONTSERRAT_BOLD.get(14).getWidth(blockString), height / 2 - 9, -1);
        GlStateManager.popMatrix();
    }

    private int getBlockCount() {
        int blockCount = 0;

        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) {
                continue;
            }

            ItemStack is = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();

            if (!isValidItem(item)) {
                continue;
            }
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.getBlock();
            return !invalidBlocks.contains(block);
        }
        return false;
    }

    public BlockData getBlockData(BlockPos pos) {
        BlockData blockData = null;
        int i = 0;
        while (blockData == null) {
            if (i >= 2) {
                break;
            }
            if (isBlockPosAir(pos.add(0, 0, 1))) {
                blockData = new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (isBlockPosAir(pos.add(0, 0, -1))) {
                blockData = new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (isBlockPosAir(pos.add(1, 0, 0))) {
                blockData = new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 0, 0))) {
                blockData = new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                break;
            }
            if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                if (isBlockPosAir(pos.add(0, -1, 0))) {
                    blockData = new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                    break;
                }
            }
            if (isBlockPosAir(pos.add(0, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (isBlockPosAir(pos.add(0, 1, 1)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
                break;
            }
            if (isBlockPosAir(pos.add(0, 1, -1)) && isSneaking) {
                blockData = new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
                break;
            }
            if (isBlockPosAir(pos.add(1, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 1, 0)) && isSneaking) {
                blockData = new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
                break;
            }
            if (isBlockPosAir(pos.add(1, 0, 1))) {
                blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 0, -1))) {
                blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (isBlockPosAir(pos.add(1, 0, 1))) {
                blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 0, -1))) {
                blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 0, 1))) {
                blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (isBlockPosAir(pos.add(1, 0, -1))) {
                blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (isBlockPosAir(pos.add(1, 0, -1))) {
                blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
                break;
            }
            if (isBlockPosAir(pos.add(-1, 0, 1))) {
                blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
                break;
            }
            pos = pos.down();
            ++i;
        }
        return blockData;
    }

    private Vec3d getVectorToPlace(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += 0.3;
            z += 0.3;
        } else {
            y += 0.5;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += placeOffset.getNumberValue();
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += placeOffset.getNumberValue();
        }
        return new Vec3d(x, y, z);
    }

    private Vec3d getVectorToRotate(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += 0.4;
            z += 0.4;
        } else {
            y += 0.4;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += rotationOffset.getNumberValue();
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += rotationOffset.getNumberValue();
        }
        return new Vec3d(x, y, z);
    }

    public boolean isBlockPosAir(BlockPos blockPos) {
        return getBlockByPos(blockPos) != Blocks.AIR && !(getBlockByPos(blockPos) instanceof BlockLiquid);
    }

    public Block getBlockByPos(BlockPos blockPos) {
        return Minecraft.world.getBlockState(blockPos).getBlock();
    }

    private static class BlockData {
        public BlockPos pos;
        public EnumFacing face;

        private BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}