package org.moonware.client.cmd.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Formatting;
import org.apache.commons.lang3.math.NumberUtils;
import org.moonware.client.cmd.CommandAbstract;
import org.moonware.client.utils.MWUtils;

import java.util.concurrent.TimeUnit;

import static org.moonware.client.utils.MWUtils.sendChat;

public class ClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("clip", "clip | vclip | hclip | random", "§6.clip | hclip | vclip | eclip | random + | - | bedrock  | <value> | up  | down", "clip", "vclip","eclip","hclip", "random", "VertClip", "tpS");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("tpS")) {
                MWUtils.sendChat(" > Телепортация >   X:" + args[1] + " ; Y: " + args[2] + " ; Z:" + args[3]);
                mc.player.motionY = 0.0201F;
                for (int i = 0 ; i < 10 ; i++) {
                    Minecraft.getConnection().sendPacket(new CPacketPlayer.Position(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), false));
                    Minecraft.player.motionX = 0;
                    Minecraft.player.motionZ = 0;
                }
            }
            if (args[0].equalsIgnoreCase("clip") || args[0].equalsIgnoreCase("vclip")) {
                float oldPos = Minecraft.player.getPosition().getY();
                float y = 0;
                try {
                    if (args[1].equals("downn")) {
                        Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, -2, Minecraft.player.posZ);
                    }
                    if (args[1].equals("random")) {
                        double a = MWUtils.randomDouble(0, 7);
                        if (a < 222) {
                            Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, Minecraft.player.posY - a, Minecraft.player.posZ);
                            sendChat("успешный телепорт вниз на " + a + " блоков");
                        }else{
                            Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, Minecraft.player.posY + a, Minecraft.player.posZ);
                            sendChat("успешный телепорт вверх на " + a + " блоков");
                        }
                    }
                    if (args[1].equals("+")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ);
                    }
                    if (args[1].equals("-")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY - Double.parseDouble(args[2]), Minecraft.player.posZ);
                    }
                    if (NumberUtils.isNumber(args[1])) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[1]), Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[1]), Minecraft.player.posZ);
                    }
                    if (args[1].equals("down")) {
                        for (int i = 0; i < 255; i++) {
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = -i - 1;
                                break;
                            }
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                                sendChat(Formatting.RED + "Тут можно телепортироваться только под бедрок.");
                                return;
                            }
                        }
                    }
                    if (args[1].equals("up")) {
                        for (int i = 3; i < 255; i++) {
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = i + 1;
                                break;
                            }
                        }
                    }
                    if (args[1].equals("down") || args[1].equals("up")) {
                        if (y == 0) {
                            if (NumberUtils.isNumber(args[1])) {
                                y = Float.parseFloat(args[1]);
                            } else {
                                sendChat(Formatting.RED + args[1] + Formatting.GRAY + " не является числом!");
                                return;
                            }
                        }
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + y, Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + y, Minecraft.player.posZ);
                    }
                    if (args[1].equals("bedrock")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, -4, Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, -4 , Minecraft.player.posZ);
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("VertClip")) {
                float oldPos = Minecraft.player.getPosition().getY();
                float y = 0;
                try {
                    if (args[1].equals("downn")) {
                        Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, -2, Minecraft.player.posZ);
                    }
                    if (args[1].equals("random")) {
                        double a = MWUtils.randomDouble(0, 7);
                        if (a < 222) {
                            Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, Minecraft.player.posY - a, Minecraft.player.posZ);
                            sendChat("успешный телепорт вниз на " + a + " блоков");
                        }else{
                            Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, Minecraft.player.posY + a, Minecraft.player.posZ);
                            sendChat("успешный телепорт вверх на " + a + " блоков");
                        }
                    }
                    if (args[1].equals("+")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ);
                    }
                    if (args[1].equals("-")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + Double.parseDouble(args[2]), Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY - Double.parseDouble(args[2]), Minecraft.player.posZ);
                    }

                    if (NumberUtils.isNumber(args[1])) {
                        for (int i = 0; i < (oldPos + Double.parseDouble(args[1])); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                            Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, oldPos + i, Minecraft.player.posZ, false));
                            Minecraft.player.setPosition(Minecraft.player.posX, oldPos + i, Minecraft.player.posZ);
                        }
                    }
                    if (args[1].equals("down")) {
                        for (int i = 0; i < 255; i++) {
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = -i - 1;
                                break;
                            }
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                                sendChat(Formatting.RED + "Тут можно телепортироваться только под бедрок.");
                                return;
                            }
                        }
                    }
                    if (args[1].equals("up")) {
                        for (int i = 3; i < 50; i++) {
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = i + 1;
                                break;
                            }
                        }
                        for (int ii = 0 ; ii < 0.5  ; ii++) {
                            int finalIi = ii;
                            new Thread(() -> {
                                //mc.player.motionY = 9f;
                                try {
                                    TimeUnit.MILLISECONDS.sleep(1);
                                    Minecraft.player.setPosition(Minecraft.player.getPosition().getX(), Minecraft.player.getPosition().getY() + finalIi, Minecraft.player.getPosition().getZ());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }).start();
                        }
                    }
                    if (args[1].equals("down") || args[1].equals("up")) {
//                        if (y == 0) {
//                            if (NumberUtils.isNumber(args[1])) {
//                                y = Float.parseFloat(args[1]);
//                            } else {
//                                sendMessage(TextFormatting.RED + args[1] + TextFormatting.GRAY + " не является числом!");
//                                return;
//                            }
//                        }
//                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
//                            mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
//                        }
//                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + y, mc.player.posZ, false));
//                        mc.player.setPosition(mc.player.posX, mc.player.posY + y, mc.player.posZ);
                    }
                    if (args[1].equals("bedrock")) {
                        for (int i = 0; i < Math.max(y / 1000, 3); i++) {
                            Minecraft.player.connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
                        }
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, -4, Minecraft.player.posZ, false));
                        Minecraft.player.setPosition(Minecraft.player.posX, -4 , Minecraft.player.posZ);
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                double x = Minecraft.player.posX;
                double y = Minecraft.player.posY;
                double z = Minecraft.player.posZ;
                double yaw = Minecraft.player.rotationYaw * 0.017453292;
                try {
                    if (args[1].equals("random")) {
                        double a = MWUtils.randomDouble(0, 20);
                        Minecraft.player.setPositionAndUpdate(x - Math.sin(yaw) * a, y, z + Math.cos(yaw) * a);
                        sendChat("успешный телепорт вперед на " + a + " блоков");
                    }
                    if (args[1].equals("+")) {
                        Minecraft.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                    if (args[1].equals("-")) {
                        Minecraft.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
                    }

                    if (NumberUtils.isNumber(args[1])) {
                        Minecraft.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[1]), y, z + Math.cos(yaw) * Double.parseDouble(args[1]));
                    }
                } catch (Exception ignored) {
                }
            }
            Minecraft.player.capabilities.setFlySpeed(0.05F);
        } else {
            sendChat(getUsage());
        }

    }
}
