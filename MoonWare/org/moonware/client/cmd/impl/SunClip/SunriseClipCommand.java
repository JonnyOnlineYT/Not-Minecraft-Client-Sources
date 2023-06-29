package org.moonware.client.cmd.impl.SunClip;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.cmd.CommandAbstract;

import static org.moonware.client.utils.MWUtils.sendChat;

public class SunriseClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public SunriseClipCommand() {
        super("sunclip", "sunclip", "§6.sunclip bedrock  | <value> | up  | down", "sunclip");
    }

    @Override
    public void execute(String... args) {
        if (args[0].length() > 1) {
            if (args[0].equalsIgnoreCase("sunclip")) {
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

                    if (args[1].matches("\\d")) {
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
                        for (int i = 3; i < 255; i++) {
                            if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = i + 1;
                                break;
                            }
                        }
                    }
                    if (args[1].equals("down") || args[1].equals("up")) {
                        if (y == 0) {
                            y = Float.parseFloat(args[1]);
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
        } else {
            sendChat(getUsage());
        }
    }
}