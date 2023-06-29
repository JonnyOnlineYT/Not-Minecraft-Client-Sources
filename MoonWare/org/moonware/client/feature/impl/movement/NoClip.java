package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.callables.EventCancellable;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventFullCube;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.EventPush;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.moonware.client.helpers.player.MovementHelper.isMoving;

public class NoClip
        extends Feature {
    public static final ListSetting noClipMode = new ListSetting("NoClip Mode", "Vanilla", "Vanilla","MatrixSP",  "Sunrise New", "Sunrise Old", "New Lorent", "Lorent Fast", "Lorent Packet", "Lorent Craft", "Lorent Down", "Really World");
    private final NumberSetting packetSpeed;
    private final NumberSetting customSpeedXZ;
    private final NumberSetting customSpeedY;
    private final NumberSetting tpSpeed;
    private final BooleanSetting destroyBlocks;
    private final Queue<Packet<?>> packets;
    private final TimerHelper timerHelper = new TimerHelper();
    private int insideBlockTicks;
    private int teleportId;
    private int enabledTicks;
    private static NumberSetting Boost = new NumberSetting("Matrix Boost Value", 0.4F, 0, 1.0F,()-> noClipMode.currentMode.equalsIgnoreCase("MatrixSP"));

    public NoClip() {
        super("NoClip", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Other);
        packets = new ConcurrentLinkedQueue();

        destroyBlocks = new BooleanSetting("Destroy Blocks", false, () -> !noClipMode.currentMode.equals("New Lorent"));
        packetSpeed = new NumberSetting("Packet Speed", 0.03f, 0.01f, 0.5f, 0.01f, () -> noClipMode.currentMode.equals("Lorent Packet"));
        customSpeedXZ = new NumberSetting("XZ Speed", 1.0f, 0.01f, 1.5f, 0.01f, () -> !noClipMode.currentMode.equals("Lorent Down") && !noClipMode.currentMode.equals("Lorent Fast") && !noClipMode.currentMode.equals("Lorent Packet") && !noClipMode.currentMode.equals("New Lorent"));
        customSpeedY = new NumberSetting("Y Speed", 0.5f, 0.01f, 1.5f, 0.01f, () -> noClipMode.currentMode.equals("Vanilla"));
        tpSpeed = new NumberSetting("TP Speed", 10.0f, 3.0f, 50.0f, 1.0f, () -> noClipMode.currentMode.equals("Lorent Fast"));

        addSettings(noClipMode, destroyBlocks, packetSpeed, customSpeedXZ, customSpeedY, tpSpeed);
    }
    float TMSpeed = Boost.getValue();
    @Override
    public void onDisable() {
        if (noClipMode.currentMode.equals("Lorent Craft") || noClipMode.currentMode.equals("Sunrise New")) {
            while (!packets.isEmpty()) {
                Minecraft.player.connection.sendPacket(packets.poll());
            }
        }
        Minecraft.timer.timerSpeed = 1.0f;
        insideBlockTicks = 0;
        enabledTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (noClipMode.currentMode.equals("New Lorent") && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            Minecraft.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), Minecraft.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            Minecraft.player.setPosition(packet.getX(), Minecraft.player.posY, packet.getZ());
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onUpdate(EventReceivePacket event) {
        SPacketPlayerPosLook packet;
        if (noClipMode.currentMode.equals("Lorent Fast") && event.getPacket() instanceof SPacketPlayerPosLook) {
            packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = Minecraft.player.rotationYaw;
            packet.pitch = Minecraft.player.rotationPitch;
        }
        if (noClipMode.currentMode.equals("Lorent Packet") && event.getPacket() instanceof SPacketPlayerPosLook) {
            packet = (SPacketPlayerPosLook)event.getPacket();
            Minecraft.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), Minecraft.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            Minecraft.player.setPosition(packet.getX(), Minecraft.player.posY, packet.getZ());
            event.setCancelled(true);
        }
        if (noClipMode.getCurrentMode().equalsIgnoreCase("MatrixPacket")) {
            Minecraft.player.motionY = 0.0D;
            Minecraft.player.motionX = 0.0D;
            Minecraft.player.motionZ = 0.0D;
            Minecraft.player.onGround = false;
            Minecraft.player.jumpMovementFactor = 0.0F;
            double in;
            if (Minecraft.gameSettings.keyBindJump.pressed) {
                in = Minecraft.player.posY + 20.0D;
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, in, Minecraft.player.posZ, true));
            }

            if (Minecraft.gameSettings.keyBindForward.pressed) {
                NetHandlerPlayClient dram = Minecraft.player.connection;
                double clam = Minecraft.player.posX;
                Minecraft.getMinecraft();
                clam -= Math.sin(Math.toRadians(Minecraft.player.rotationYaw)) * 15.0D;
                in = Minecraft.player.posY;
                double var16 = Minecraft.player.posZ;
                Minecraft.getMinecraft();
                dram.sendPacket(new CPacketPlayer.Position(clam, in, var16 + Math.cos(Math.toRadians(Minecraft.player.rotationYaw)) * 15.0D, true));
                in = Minecraft.player.posY - 15.0D;
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, in, Minecraft.player.posZ, true));
                if (Minecraft.player.ticksExisted % 9 == 0) {
                    in = Minecraft.player.posY + 1.0D;
                    Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, in, Minecraft.player.posZ, true));
                }
            }

            if (Minecraft.gameSettings.keyBindSneak.pressed) {
                in = Minecraft.player.posY - 2.0D;
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, in, Minecraft.player.posZ, true));
            }
            if (!Minecraft.gameSettings.keyBindForward.isKeyDown() && !Minecraft.gameSettings.keyBindBack.isKeyDown() && !Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                if (!(isMoving())) {
                    Minecraft.timer.timerSpeed = 1.0F;
                }
            }
            Minecraft.timer.timerSpeed = TMSpeed + 1.0F;
        }
        if (noClipMode.currentMode.equals("Sunrise New")) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                packet = (SPacketPlayerPosLook)event.getPacket();
                Minecraft.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
            }
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                event.setCancelled(true);
            }
        }

    }


    @EventTarget
    public void onEvent(Event event) {
        if (noClipMode.currentMode.equalsIgnoreCase("Sunrise Motion")) {
            if ((event instanceof EventFullCube) && MoonWare.featureManager.getFeatureByClass(NoClip.class).getState()) {
                ((EventCancellable) event).setCancelled(true);
            }
            if (event instanceof EventPush) {
                ((EventPush) event).setCancelled(true);
            }
            if (event instanceof EventMove && MoonWare.featureManager.getFeatureByClass(NoClip.class).getState()) {
                EventMove move = (EventMove) event;
                if (!collisionPredict(move.to())) {
                    if (move.isCollidedHorizontal())
                        move.setIgnoreHorizontalCollision();
                    if (move.motion().yCoordS > 0 || Minecraft.player.isSneaking()) {
                        move.setIgnoreVerticalCollision();
                    }
                    move.motion().yCoordS = Math.min(move.motion().yCoord, 99999);
                }
            }
        }
    }
    public boolean collisionPredict(Vec3d to) {
        boolean prevCollision = Minecraft.world
                .getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().contract(0.0625D)).isEmpty();
        Vec3d backUp = new Vec3d(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ);
        Minecraft.player.setPosition(to.xCoord, to.yCoord, to.zCoord);
        boolean collision = Minecraft.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().contract(0.0625D))
                .isEmpty() && prevCollision;
        Minecraft.player.setPosition(backUp.xCoord, backUp.yCoord, backUp.zCoord);
        return collision;
    }
    @EventTarget
    public void onFullCube(EventFullCube event) {
        if (noClipMode.currentMode.equals("Sunrise New")) {
            event.setCancelled(true);
        } else {
            event.setCancelled(!noClipMode.currentMode.equals("Lorent Packet") && !noClipMode.currentMode.equals("New Lorent"));
        }
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (noClipMode.currentMode.equals("New Lorent") && event.getPacket() instanceof CPacketPlayer && !Minecraft.player.onGround) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.y = Minecraft.player.posY + 50.0;
        }
    }

    //@WXFuscator
    @EventTarget
    public void onSend(EventSendPacket event) {
        if (!getState()) {
            return;
        }
        if (noClipMode.currentMode.equals("Lorent Craft") && event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
        if (noClipMode.currentMode.equals("Sunrise New") && Helper.mc.getServerData().ip.equalsIgnoreCase("play.sunmc.ru") && event.getPacket() instanceof CPacketPlayer) {
            if (Minecraft.player.ticksExisted % 2 == 0) {
                while (!packets.isEmpty()) {
                    Minecraft.player.connection.sendPacket(packets.poll());
                }
                return;
            }
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
    }

    //@WXFuscator
    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (noClipMode.currentMode.equals("Sunrise New") && (Minecraft.player == null || Minecraft.world == null)) {
            toggle();
            EventManager.unregister(this);
        }
        if (noClipMode.currentMode.equals("Sunrise Old")) {
            if (Minecraft.player == null || Minecraft.world == null) {
                toggle();
                EventManager.unregister(this);
                return;
            }
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPreMotionUpdate2(EventPreMotion event) {
        if (noClipMode.currentMode.equals("New Lorent")) {
            Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SPRINTING));
            if (Minecraft.player.onGround) {
                Minecraft.player.jump();
            }
        }
    }

    @EventTarget
    public void onPreMotionUpdate(EventPreMotion event) {
        if (noClipMode.currentMode.equals("New Lorent")) {
            Minecraft.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (Minecraft.player.movementInput.jump) {
                if (!timerHelper.hasReached(3000.0)) {
                    speedY = Minecraft.player.ticksExisted % 20 == 0 ? -0.04f : 0.031f;
                } else {
                    timerHelper.reset();
                    speedY = -0.08f;
                }
            } else if (Minecraft.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            double[] dir = MovementHelper.forward(0.02f);
            Minecraft.player.motionX = dir[0];
            Minecraft.player.motionY = speedY;
            Minecraft.player.motionZ = dir[1];
            Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX + Minecraft.player.motionX, Minecraft.player.posY + Minecraft.player.motionY, Minecraft.player.posZ + Minecraft.player.motionZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, Minecraft.player.onGround));
            double x = Minecraft.player.posX + Minecraft.player.motionX;
            double y = Minecraft.player.posY + Minecraft.player.motionY;
            double z = Minecraft.player.posZ + Minecraft.player.motionZ;
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x, y -= -1337.0, z, Minecraft.player.onGround));
        }
    }

    //@WXFuscator
    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (noClipMode.currentMode.equals("Lorent Packet")) {
            Minecraft.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (Minecraft.player.movementInput.jump) {
                if (!timerHelper.hasReached(3000.0)) {
                    speedY = Minecraft.player.ticksExisted % 20 == 0 ? -0.04f : 0.031f;
                } else {
                    timerHelper.reset();
                    speedY = -0.08f;
                }
            } else if (Minecraft.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            double[] dir = MovementHelper.forward(packetSpeed.getCurrentValue());
            Minecraft.player.motionX = dir[0];
            Minecraft.player.motionY = speedY;
            Minecraft.player.motionZ = dir[1];
            Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX + Minecraft.player.motionX, Minecraft.player.posY + Minecraft.player.motionY, Minecraft.player.posZ + Minecraft.player.motionZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, Minecraft.player.onGround));
            double x = Minecraft.player.posX + Minecraft.player.motionX;
            double y = Minecraft.player.posY + Minecraft.player.motionY;
            double z = Minecraft.player.posZ + Minecraft.player.motionZ;
            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x, y -= -1337.0, z, Minecraft.player.onGround));
        } else if (noClipMode.currentMode.equals("Sunrise New") && Helper.mc.getServerData().ip.equalsIgnoreCase("play.sunmc.ru")) {
            Minecraft.player.motionY = 0.0;
            event.setOnGround(true);
            Minecraft.player.onGround = true;
        }
    }

    //@WXFuscator
    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(noClipMode.currentMode);
        if (Minecraft.world != null && Minecraft.player != null) {
            if (destroyBlocks.getCurrentValue() && (Minecraft.player.isCollidedHorizontally || MovementHelper.isInsideBlock2())) {
                double[] dir = MovementHelper.forward(0.5);
                Minecraft.playerController.onPlayerDamageBlock(new BlockPos(Minecraft.player.posX + dir[0], Minecraft.player.posY + 1.0, Minecraft.player.posZ + dir[1]), Minecraft.player.getHorizontalFacing());
                Minecraft.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (noClipMode.currentMode.equals("Vanilla")) {
                Minecraft.player.motionY = 0.0;
                Minecraft.player.motionX *= customSpeedXZ.getCurrentValue();
                Minecraft.player.motionZ *= customSpeedXZ.getCurrentValue();
                if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.motionY += customSpeedY.getCurrentValue();
                } else if (Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.player.motionY -= customSpeedY.getCurrentValue();
                }
            } else if (noClipMode.currentMode.equals("Lorent Fast")) {
                if (!Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.player.motionY = 0.0;
                    Minecraft.timer.timerSpeed = tpSpeed.getCurrentValue();
                } else {
                    Minecraft.player.motionX -= 5.0E-7;
                    Minecraft.player.motionZ += 5.0E-7;
                    Minecraft.player.motionY = -0.1;
                }
            } else if (noClipMode.currentMode.equals("Lorent Down")) {
                Minecraft.timer.timerSpeed = 4.4f;
                Minecraft.player.motionX -= 5.0E-7;
                Minecraft.player.motionZ += 5.0E-7;
                Minecraft.player.motionY = -0.1;
            } else if (noClipMode.currentMode.equals("Lorent Craft")) {
                Minecraft.player.motionY = 0.0;
                Minecraft.player.motionX *= customSpeedXZ.getCurrentValue();
                Minecraft.player.motionZ *= customSpeedXZ.getCurrentValue();
            } else if (noClipMode.currentMode.equals("Sunrise Old")) {
                if (Minecraft.player == null || Minecraft.world == null) {
                    toggle();
                    EventManager.unregister(this);
                    return;
                }
                Minecraft.player.motionY = 0.0;
                Minecraft.player.motionX *= customSpeedXZ.getCurrentValue();
                Minecraft.player.motionZ *= customSpeedXZ.getCurrentValue();
                if (Minecraft.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.motionY = 0.01;
                } else if (Minecraft.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.player.motionY = -0.01;
                }
                MovementHelper.teleport(2.0);
            } else if (noClipMode.currentMode.equals("Sunrise New")) {
                if (Minecraft.player == null || Minecraft.world == null) {
                    toggle();
                    EventManager.unregister(this);
                    return;
                }
                insideBlockTicks = MovementHelper.isInsideBlock2() ? ++insideBlockTicks : 0;
            } else if (noClipMode.currentMode.equals("Really World")) {
                int ticks = 35;
                if (Minecraft.player.ticksExisted % ticks == 0) {
                    Minecraft.timer.timerSpeed = 1.0f;
                    toggle();
                    return;
                }
                Minecraft.player.motionY = 0.0;
                Minecraft.player.motionX *= customSpeedXZ.getCurrentValue();
                Minecraft.player.motionZ *= customSpeedXZ.getCurrentValue();
                Minecraft.timer.timerSpeed = 500.0f;
            }
        }
    }

    public static void init() {
    }
}
