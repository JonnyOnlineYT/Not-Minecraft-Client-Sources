package org.moonware.client.feature.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventLiquidSolid;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventPreMotionUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class LiquidWalk extends Feature {

    public static ListSetting mode;
    public static NumberSetting speed;
    public static NumberSetting motionUp;
    public static NumberSetting boostSpeed;
    public static NumberSetting boostTicks;
    public static BooleanSetting boost;
    private final BooleanSetting speedCheck;
    public boolean start;
    private final float range = 0.005f;
    private int tick;
    private TimerHelper timerhelperRwg;
    private int waterTicks;

    public BooleanSetting groundSpoof = new BooleanSetting("GroundSpoof", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting solid = new BooleanSetting("Solid", false, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting groundSpoofExploit = new BooleanSetting("GroundSpoof Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useMotion = new BooleanSetting("Use Motion", false, () -> mode.currentMode.equals("Custom"));
    public NumberSetting motionY = new NumberSetting("Y-Motion", 0.42F, 0, 2, 0.01F, () -> mode.currentMode.equals("Custom") && useMotion.getBoolValue());
    public BooleanSetting useSpeed = new BooleanSetting("Use Speed", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useJumpMoveFactor = new BooleanSetting("Use Jump Move Factor", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useTimer = new BooleanSetting("Use Timer", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useTimerExploit = new BooleanSetting("Timer Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting fallDistExploit = new BooleanSetting("Fall Distance Exploit", true, () -> mode.currentMode.equals("Custom"));
    public BooleanSetting useFallDist = new BooleanSetting("Use Fall Distance", true, () -> mode.currentMode.equals("Custom"));
    public NumberSetting groundSpoofMotion = new NumberSetting("GroundSpoof Y-Motion", 0.08F, 0, 1, 0.001F, () -> mode.currentMode.equals("Custom") && groundSpoofExploit.getBoolValue());
    public NumberSetting inLiquid = new NumberSetting("In Liquid", 0.1F, 0, 1, 0.001F, () -> mode.currentMode.equals("Custom"));
    public NumberSetting fallDist = new NumberSetting("Fall Distance", 0, 0, 10000, 0.1F, () -> mode.currentMode.equals("Custom") && useFallDist.getBoolValue());
    public NumberSetting jumpMoveFactor = new NumberSetting("Jump Move Factor", 0.1F, 0, 10, 0.001F, () -> mode.currentMode.equals("Custom") && useJumpMoveFactor.getBoolValue());
    public NumberSetting timer = new NumberSetting("Timer", 1, 0.001F, 15, 0.001F, () -> mode.currentMode.equals("Custom") && useTimer.getBoolValue());
    public NumberSetting boostslide = new NumberSetting("SlideBoost", 0.9F, 0.1F, 1.1F, 0.000001F, () -> mode.currentMode.equals("ReallyworldSlide"));


    public LiquidWalk() {
        super("Jesus", "Позволяет ходить по воде", Type.Movement);
        mode = new ListSetting("Jesus Mode", "Matrix Jump", () -> true, "Matrix Jump","ReallyworldSlide", "Sunrise new");
        speed = new NumberSetting("Speed", 1, 0.01F, 15, 0.01F, () -> !mode.currentMode.equals("Dolphin") && (mode.currentMode.equals("Custom") && useSpeed.getBoolValue()) || mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Reallyworld new") || mode.currentMode.equals("Matrix Jump") || mode.currentMode.equals("Matrix Zoom") || mode.currentMode.equals("MST") || mode.currentMode.equalsIgnoreCase("ReallyworldGround"));
        boost = new BooleanSetting("Boost", false, () -> mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Custom"));
        boostSpeed = new NumberSetting("Boost Speed", 1.35F, 0.1F, 4, 0.01F, () -> boost.getBoolValue() && mode.currentMode.equals("Custom") || mode.currentMode.equals("Matrix Ground"));
        motionUp = new NumberSetting("Motion Up", 0.42F, 0.1F, 2, 0.01F, () -> mode.currentMode.equals("Matrix Jump"));
        boostTicks = new NumberSetting("Boost Ticks", 2, 0, 30, 1, () -> boost.getBoolValue() && mode.currentMode.equals("Custom") || mode.currentMode.equals("Matrix Ground"));
        speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        addSettings(mode,boostslide, useMotion, useSpeed, useFallDist, useJumpMoveFactor, useTimer, speed, motionY, timer, useTimerExploit, groundSpoof, groundSpoofExploit, groundSpoofMotion, solid, inLiquid, fallDist, fallDistExploit, jumpMoveFactor, boost, boostSpeed, boostTicks, motionUp, speedCheck);
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (mode.currentMode.equals("Matrix Ground") || mode.currentMode.equals("Reallyworld new") || (solid.getBoolValue() && mode.currentMode.equals("Custom")) || mode.currentMode.equals("MST")) {
            event.setCancelled(!Minecraft.gameSettings.keyBindJump.isKeyDown());
        }
    }

    private boolean jesusTick;
    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        setSuffix(mode.getCurrentMode());
        BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.1, Minecraft.player.posZ);
        Block block = Minecraft.world.getBlockState(blockPos).getBlock();
        if (!Minecraft.player.isPotionActive(MobEffects.SPEED) && speedCheck.getBoolValue())
            return;
        switch (mode.currentMode) {
            case "Matrix Jump":
                if (Minecraft.player.isInLiquid() && Minecraft.player.motionY < 0) {
                    Minecraft.player.motionY = motionUp.getNumberValue();
                    MovementHelper.setSpeed(speed.getNumberValue());
                }
                break;
            case "ReallyworldSlide":
                BlockPos blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
                Block block2 = mc.world.getBlockState(blockPos2).getBlock();

                if (mc.player.isInWater()) {
                    mc.player.setVelocity(0, 0, 0);
                    mc.player.motionY = mc.world.handleMaterialAcceleration(mc.player.getEntityBoundingBox().expand(0, -1.1, 0).contract(.06), Material.WATER, mc.player) ? .12 : mc.player.motionY + .18;
                }

                if (block2 instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0322, mc.player.posZ)).getBlock() instanceof BlockAir && !mc.player.onGround) {



                    mc.player.motionY = 0;
                    jesusTick = true;
                    if (waterTicks == 10) {
                        event.setPosY(event.getPosY() - 0.0322);
                        MovementHelper.setSpeed(boostslide.getNumberValue());

                        waterTicks = 0;
                    } else {
                        waterTicks = 10;
                    }

                    if(mc.gameSettings.keyBindJump.isKeyDown() && mc.player.isCollidedHorizontally)
                    {
                        mc.player.setVelocity(0,mc.player.motionY = 0.2,0);
                    }
                }
                if (mc.player.ticksExisted % 6 ==0 ) {
                    for (Slot slot : Minecraft.player.inventoryContainer.inventorySlots) {
                        if (slot.getStack().getItem() == Items.ELYTRA) {
                            Minecraft.playerController.windowClick(0, slot.slotNumber, 0, ClickType.PICKUP, Minecraft.player);
                            Minecraft.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Minecraft.player);
                            Minecraft.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Minecraft.player);
                            Minecraft.playerController.windowClick(0, slot.slotNumber, 0, ClickType.PICKUP, Minecraft.player);
                            Minecraft.playerController.updateController();

                        }
                    }
                }
                break;
            case "Dolphin":
                if (Minecraft.player.isInLiquid()) {
                    Minecraft.player.motionX *= 1.17;
                    Minecraft.player.motionZ *= 1.17;

                    if (Minecraft.player.isCollidedHorizontally) {
                        Minecraft.player.motionY = 0.24;
                    } else if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 1, Minecraft.player.posZ)).getBlock() != Blocks.AIR)
                        Minecraft.player.motionY += 0.04;
                }
                break;
            case "Sunrise new":
                if (Minecraft.player.isInLiquid()) {
                    Minecraft.player.motionX *= 1.28;
                    Minecraft.player.motionZ *= 1.28;

                    if (Minecraft.player.isCollidedHorizontally) {
                        Minecraft.player.motionY = 0.24;
                    } else if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.15F, Minecraft.player.posZ)).getBlock() != Blocks.AIR)
                        Minecraft.player.motionY += 0.04;
                }
                break;
            case "Matrix Ground":
                if (block instanceof BlockLiquid) {
                    Minecraft.player.onGround = false;
                    Minecraft.player.isAirBorne = true;
                    MovementHelper.setSpeed(boost.getBoolValue() ? Minecraft.player.ticksExisted % boostTicks.getNumberValue() == 0 ? speed.getNumberValue() : boostSpeed.getNumberValue() : speed.getNumberValue());
                    event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.25 : event.getPosY() - 0.24799999);
                    event.setPosY(Minecraft.player.ticksExisted % 3 == 0 ? event.getPosY() + 0.25 : event.getPosY() - 0.24799999);
                    event.setOnGround(false);
                }
                break;
            case "Matrix Zoom":
                if (block instanceof BlockLiquid) {
                    MovementHelper.setSpeed(speed.getNumberValue());
                }

                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.0000001, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
                    Minecraft.player.fallDistance = 0;
                    Minecraft.player.motionX = 0;
                    Minecraft.player.motionY = 0.01;
                    Minecraft.player.jumpMovementFactor = 0.2f;
                    Minecraft.player.motionZ = 0;
                }
                break;
            case "Reallyworld new":
                if (block instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0;
                    Minecraft.player.onGround = false;
                    Minecraft.player.isAirBorne = true;
                    //MovementHelper.setSpeed();
                    Minecraft.player.setAIMoveSpeed(Minecraft.player.ticksExisted % 1.1 == 0 ? speed.getNumberValue() : 0.1F);
                    event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.07 : event.getPosY() - 0.07);
                    event.setYaw(14);
                    event.setPitch(15);
                    event.setOnGround(false);
                }
                BlockPos blockPos1 = new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.11, Minecraft.player.posZ);
                Block block1 = Minecraft.world.getBlockState(blockPos1).getBlock();
                if (block1 instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0.13f;
                }
                break;

            case "MST":
                if (block instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0;
                    Minecraft.player.onGround = false;
                    Minecraft.player.isAirBorne = true;
                    MovementHelper.setSpeed(Minecraft.player.ticksExisted % 2 == 0 ? speed.getNumberValue() : 0.1F);
                    event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.04 : event.getPosY() - 0.04);
                    event.setYaw(12);
                    event.setPitch(13);
                    event.setOnGround(false);
                }
                blockPos2 = new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.11, Minecraft.player.posZ);
                block2 = Minecraft.world.getBlockState(blockPos2).getBlock();
                if (block2 instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0.13f;
                }
                break;
            case "Custom":

                BlockPos blockPosCustom = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - inLiquid.getNumberValue(), Minecraft.player.posZ);
                Block blockCustom = Minecraft.world.getBlockState(blockPosCustom).getBlock();

                if (blockCustom instanceof BlockLiquid) {

                    if (useFallDist.getBoolValue()) {
                        Minecraft.player.fallDistance = fallDist.getNumberValue();
                    }
                    if (useJumpMoveFactor.getBoolValue()) {
                        Minecraft.player.jumpMovementFactor = jumpMoveFactor.getNumberValue();
                    }

                    if (useMotion.getBoolValue()) {
                        Minecraft.player.motionY = motionY.getNumberValue();
                    }

                    if (useTimer.getBoolValue()) {
                        Minecraft.timer.timerSpeed = timer.getNumberValue();
                    }

                    if (useTimerExploit.getBoolValue()) {
                        Minecraft.timer.timerSpeed = Minecraft.player.ticksExisted % 60 > 39 ? 1000 : 1;
                    }

                    if (groundSpoof.getBoolValue()) {
                        Minecraft.player.onGround = false;
                    }

                    if (fallDistExploit.getBoolValue()) {
                        Minecraft.player.fallDistance = (float) (Math.random() * 1.0E-12);
                    }

                    if (useSpeed.getBoolValue()) {
                        MovementHelper.setSpeed(boost.getBoolValue() ? Minecraft.player.ticksExisted % boostTicks.getNumberValue() == 0 ? speed.getNumberValue() : boostSpeed.getNumberValue() : speed.getNumberValue());
                    }

                    if (groundSpoofExploit.getBoolValue()) {
                        event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + groundSpoofMotion.getNumberValue() : event.getPosY() - groundSpoofMotion.getNumberValue());
                        event.setOnGround(false);
                    }
                }
                break;
            case "Guardian":
                if (Minecraft.player.isInWater()) {
                    Minecraft.player.motionX = 0;
                    Minecraft.player.motionY = 0;
                    Minecraft.player.motionZ = 0;
                    float am = 4.0f;
                    if (Minecraft.player.ticksExisted % 4 == 0 && am <= 5.0f) {
                        am += 0.3f;
                    }
                    if (Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                        if (Minecraft.player.ticksExisted % 1 == 0) {
                            Minecraft.player.motionY -= 0.28;
                        }
                    }
                    if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                        if (Minecraft.player.ticksExisted % 1 == 0) {
                            Minecraft.player.motionY += 0.28;
                        }
                    }
                    if (Minecraft.gameSettings.keyBindForward.isKeyDown()) {
                        float yaw = Minecraft.player.rotationYaw * 0.0174532920F;
                        Minecraft.player.motionX -= MathHelper.sin(yaw) * (am / 5);
                        Minecraft.player.motionZ += MathHelper.cos(yaw) * (am / 5);

                    }
                    if (Minecraft.gameSettings.keyBindBack.isKeyDown()) {
                        float yaw = Minecraft.player.rotationYaw * 0.0174532920F;
                        Minecraft.player.motionX -= -MathHelper.sin(yaw) * (am / 5);
                        Minecraft.player.motionZ += -MathHelper.cos(yaw) * (am / 5);
                    }
                }
                break;
            case "ReallyworldGround":
                if (block instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0;
                    Minecraft.player.onGround = false;
                    Minecraft.player.isAirBorne = true;
                    if (Minecraft.player.ticksExisted % 5 == 0) {
                        Helper.mc.getConnection().sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    MovementHelper.setSpeed(Minecraft.player.ticksExisted % 28 == 0 ? speed.getNumberValue() : 0F);

                    event.setPosY(Minecraft.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.04 : event.getPosY() - 0.04);
                    event.setYaw(12);
                    event.setPitch(13);
                    event.setOnGround(false);
                }
                BlockPos blockPos22 = new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.11, Minecraft.player.posZ);
                Block block22 = Minecraft.world.getBlockState(blockPos22).getBlock();
                if (block22 instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0.13f;
                }
                break;
            case "ReallyWorldGround2":
                blockPos2 = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.02, Minecraft.player.posZ);
                block2 = Minecraft.world.getBlockState(blockPos2).getBlock();
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.5, Minecraft.player.posZ)).getBlock() == Blocks.WATER && Minecraft.player.onGround) {
                    Minecraft.player.motionY = 0.2;
                }
                if (block2 instanceof BlockLiquid && !Minecraft.player.onGround) {
                    if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.13, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
                        MovementHelper.setSpeed((float) (Minecraft.player.motionY = 0.10000000149011612));
                        Minecraft.player.jumpMovementFactor = 0.0f;
                    } else {
                        MovementHelper.setSpeed(1.100000023841858F);
                        Minecraft.player.motionY = -0.10000000149011612;
                    }
                    if (Minecraft.player.isCollidedHorizontally) {
                        Minecraft.player.motionY = 0.18000000715255737;
                    }
                }
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.2, Minecraft.player.posZ)).getBlock() instanceof BlockLiquid) {
                    Minecraft.player.motionY = 0.18;
                }
        }
    }
    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (mode.currentMode.equalsIgnoreCase("ReallyworldGround")) {
            BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ);
            Block block = Minecraft.world.getBlockState(blockPos).getBlock();
            //Helper.mc.player.motionX = 0;
            if (block instanceof BlockLiquid) {
                //Helper.mc.player.motionY = 0;
                if (!timerhelperRwg.hasReached(500)) {
                    Minecraft.player.onGround = !Minecraft.player.onGround;
                    //mc.player.posY = (mc.player.ticksExisted % 2 == 0 ? mc.player.posY + 0.000564484F : mc.player.posY - 0.0005745646436F);
                    if (MovementHelper.isMoving()) {
                        //mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        MovementHelper.strafePlayer();
                        //mc.player.setAIMoveSpeed(mc.player.ticksExisted % 1.1 == 0 ? 0.7F : 0.1F);
                        Minecraft.player.onGround = !Minecraft.player.onGround;

                    }
                }else if (!timerhelperRwg.hasReached(900)) {
                    Minecraft.player.motionY = 0;
                    Minecraft.player.motionX = 0;
                    Minecraft.player.motionZ = 0;
                    timerhelperRwg.reset();
                }
            }
            //Helper.mc.player.motionZ = 0;
        }

    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.1, Minecraft.player.posZ);
        Block block = Minecraft.world.getBlockState(blockPos).getBlock();
        if (mode.currentMode.equalsIgnoreCase("ReallyworldGround") && block instanceof BlockLiquid) {
            if (event.getPacket() instanceof SPacketPlayerListItem) {
                //event.setCancelled(true);
            }
        }
    }


    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
    }
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        BlockPos blockPos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.1, Minecraft.player.posZ);
        Block block = Minecraft.world.getBlockState(blockPos).getBlock();
        if (mode.currentMode.equalsIgnoreCase("ReallyworldGround") && block instanceof BlockLiquid) {
/*
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
            */
            /*
            if (event.getPacket() instanceof CPacketPlayer.Position) {
                event.setCancelled(true);
            }




            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketEntityAction) {
                event.setCancelled(true);
            }
*/

        }
    }
}
