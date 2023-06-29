package Celestial.cmd.impl;

import Celestial.cmd.CommandAbstract;
import Celestial.utils.math.Randomizer;
import Celestial.utils.other.ChatUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.concurrent.TimeUnit;

public class ClipCommand
        extends CommandAbstract {
    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("clip", "clip | vclip | hclip | random", "\u00a76.clip | hclip | vclip | eclip | random + | - | bedrock  | <value> | up  | down", "clip", "vclip", "eclip", "hclip", "random", "VertClip");
    }

    @Override
    public void execute(String ... args2) {
        if (args2.length > 1) {
            double b;
            double a;
            float y;
            float oldPos;
            if (args2[0].equalsIgnoreCase("clip") || args2[0].equalsIgnoreCase("vclip")) {
                oldPos = this.mc.player.getPosition().getY();
                y = 0.0f;
                try {
                    if (args2[1].equals("downn")) {
                        this.mc.player.setPositionAndUpdate(this.mc.player.posX, -2.0, this.mc.player.posZ);
                    }
                    if (args2[1].equals("random")) {
                        a = Randomizer.getRandom(7);
                        b = Randomizer.getRandom(444);
                        if (a < 222.0) {
                            this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY - a, this.mc.player.posZ);
                            ChatUtils.addChatMessage("\u0443\u0441\u043f\u0435\u0448\u043d\u044b\u0439 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u0432\u043d\u0438\u0437 \u043d\u0430 " + a + " \u0431\u043b\u043e\u043a\u043e\u0432");
                        } else {
                            this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY + a, this.mc.player.posZ);
                            ChatUtils.addChatMessage("\u0443\u0441\u043f\u0435\u0448\u043d\u044b\u0439 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u0432\u0432\u0435\u0440\u0445 \u043d\u0430 " + a + " \u0431\u043b\u043e\u043a\u043e\u0432");
                        }
                    }
                    if (args2[1].equals("+")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ);
                    }
                    if (args2[1].equals("-")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY - Double.parseDouble(args2[2]), this.mc.player.posZ);
                    }
                    if (NumberUtils.isNumber(args2[1])) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[1]), this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[1]), this.mc.player.posZ);
                    }
                    if (args2[1].equals("down")) {
                        for (int i = 0; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = -i - 1;
                                break;
                            }
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, -i, 0)) != Blocks.BEDROCK.getDefaultState()) continue;
                            ChatUtils.addChatMessage((Object)((Object) TextFormatting.RED) + "\u0422\u0443\u0442 \u043c\u043e\u0436\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a.");
                            return;
                        }
                    }
                    if (args2[1].equals("up")) {
                        for (int i = 3; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, i, 0)) != Blocks.AIR.getDefaultState()) continue;
                            y = i + 1;
                            break;
                        }
                    }
                    if (args2[1].equals("down") || args2[1].equals("up")) {
                        if (y == 0.0f) {
                            if (NumberUtils.isNumber(args2[1])) {
                                y = Float.parseFloat(args2[1]);
                            } else {
                                ChatUtils.addChatMessage((Object)((Object)TextFormatting.RED) + args2[1] + (Object)((Object)TextFormatting.GRAY) + " \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u0447\u0438\u0441\u043b\u043e\u043c!");
                                return;
                            }
                        }
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + (double)y, this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + (double)y, this.mc.player.posZ);
                    }
                    if (args2[1].equals("bedrock")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, -4.0, this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, -4.0, this.mc.player.posZ);
                    }
                }
                catch (Exception i) {
                    // empty catch block
                }
            }
            if (args2[0].equalsIgnoreCase("VertClip")) {
                oldPos = this.mc.player.getPosition().getY();
                y = 0.0f;
                try {
                    if (args2[1].equals("downn")) {
                        this.mc.player.setPositionAndUpdate(this.mc.player.posX, -2.0, this.mc.player.posZ);
                    }
                    if (args2[1].equals("random")) {
                        a = Randomizer.getRandom(7);
                        b = Randomizer.getRandom(444);
                        if (a < 222.0) {
                            this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY - a, this.mc.player.posZ);
                            ChatUtils.addChatMessage("\u0443\u0441\u043f\u0435\u0448\u043d\u044b\u0439 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u0432\u043d\u0438\u0437 \u043d\u0430 " + a + " \u0431\u043b\u043e\u043a\u043e\u0432");
                        } else {
                            this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY + a, this.mc.player.posZ);
                            ChatUtils.addChatMessage("\u0443\u0441\u043f\u0435\u0448\u043d\u044b\u0439 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u0432\u0432\u0435\u0440\u0445 \u043d\u0430 " + a + " \u0431\u043b\u043e\u043a\u043e\u0432");
                        }
                    }
                    if (args2[1].equals("+")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ);
                    }
                    if (args2[1].equals("-")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args2[2]), this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY - Double.parseDouble(args2[2]), this.mc.player.posZ);
                    }
                    if (NumberUtils.isNumber(args2[1])) {
                        int i = 0;
                        while ((double)i < (double)oldPos + Double.parseDouble(args2[1])) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, oldPos + (float)i, this.mc.player.posZ, false));
                            this.mc.player.setPosition(this.mc.player.posX, oldPos + (float)i, this.mc.player.posZ);
                            ++i;
                        }
                    }
                    if (args2[1].equals("down")) {
                        for (int i = 0; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = -i - 1;
                                break;
                            }
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, -i, 0)) != Blocks.BEDROCK.getDefaultState()) continue;
                            ChatUtils.addChatMessage((Object)((Object)TextFormatting.RED) + "\u0422\u0443\u0442 \u043c\u043e\u0436\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a.");
                            return;
                        }
                    }
                    if (args2[1].equals("up")) {
                        for (int i = 3; i < 50; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos(this.mc.player).add(0, i, 0)) != Blocks.AIR.getDefaultState()) continue;
                            y = i + 1;
                            break;
                        }
                        int ii = 0;
                        while ((double)ii < 0.5) {
                            int finalIi = ii++;
                            new Thread(() -> {
                                try {
                                    TimeUnit.MILLISECONDS.sleep(1L);
                                    this.mc.player.setPosition(this.mc.player.getPosition().getX(), this.mc.player.getPosition().getY() + finalIi, this.mc.player.getPosition().getZ());
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                    if (args2[1].equals("down") || args2[1].equals("up")) {
                        // empty if block
                    }
                    if (args2[1].equals("bedrock")) {
                        int i = 0;
                        while ((float)i < Math.max(y / 1000.0f, 3.0f)) {
                            this.mc.player.connection.sendPacket(new CPacketPlayer(this.mc.player.onGround));
                            ++i;
                        }
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, -4.0, this.mc.player.posZ, false));
                        this.mc.player.setPosition(this.mc.player.posX, -4.0, this.mc.player.posZ);
                    }
                }
                catch (Exception i) {
                    // empty catch block
                }
            }
            if (args2[0].equalsIgnoreCase("hclip")) {
                double x = this.mc.player.posX;
                double y2 = this.mc.player.posY;
                double z = this.mc.player.posZ;
                double yaw = (double)this.mc.player.rotationYaw * 0.017453292;
                try {
                    if (args2[1].equals("random")) {
                        double a2 = Randomizer.getRandom(20);
                        this.mc.player.setPositionAndUpdate(x - Math.sin(yaw) * a2, y2, z + Math.cos(yaw) * a2);
                        ChatUtils.addChatMessage("\u0443\u0441\u043f\u0435\u0448\u043d\u044b\u0439 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442 \u0432\u043f\u0435\u0440\u0435\u0434 \u043d\u0430 " + a2 + " \u0431\u043b\u043e\u043a\u043e\u0432");
                    }
                    if (args2[1].equals("+")) {
                        this.mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args2[2]), y2, z + Math.cos(yaw) * Double.parseDouble(args2[2]));
                    }
                    if (args2[1].equals("-")) {
                        this.mc.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args2[2]), y2, z - Math.cos(yaw) * Double.parseDouble(args2[2]));
                    }
                    if (NumberUtils.isNumber(args2[1])) {
                        this.mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args2[1]), y2, z + Math.cos(yaw) * Double.parseDouble(args2[1]));
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            this.mc.player.capabilities.setFlySpeed(0.05f);
        } else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}


