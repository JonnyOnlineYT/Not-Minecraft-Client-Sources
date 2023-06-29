package ru.rockstar.api.command.impl;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;

public class ClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("clip", "clip | hclip", "§6.clip | (hclip) + | - §3<value> | down", "clip", "hclip", "rwclip", "rwhclip");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("clip")) {
                try {
                	float y = 0.0f;
                    if (args[1].equals("bedrock")) {
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + y, mc.player.posZ);
                    }
                    if (args[1].equals("down")) {
                        for (int i = 1; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = (float)(-i - 1);
                                break;
                            }
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                                return;
                            }
                        }
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + y, mc.player.posZ);
                    }
                    if (args[1].equals("up")) {
                        for (int i = 4; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = (float)(i + 1);
                                break;
                            }
                        }
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + y, mc.player.posZ);
                    }
                    if (args[1].equals("+")) {
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
                    }
                    if (args[1].equals("-")) {
                        mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                double yaw = mc.player.rotationYaw * 0.017453292;
                try {
                    if (args[1].equals("+")) {
                        mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[2]), y, z + Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                    if (args[1].equals("-")) {
                        mc.player.setPositionAndUpdate(x + Math.sin(yaw) * Double.parseDouble(args[2]), y, z - Math.cos(yaw) * Double.parseDouble(args[2]));
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("rwclip")) {
                try {
                	float y = 0.0f;
                    if (args[1].equals("bedrock")) {
                        float endX = (int) mc.player.posX;
                        float endY = (float) ((float) mc.player.posY + y);
                        float endZ = (int) mc.player.posZ;
                             Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                             if (mc.player.posX != endX && mc.player.posZ != endZ) {
                            	 BlockPos feet = new BlockPos(endX, endY, endZ);
                                 BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                                 if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                                	 for (int i = 0; i < 10; i++) {
                                			mc.player.motionY = 10;
                                         mc.player.onGround = true;
                                		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                          mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                          mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                                 	}
                                 } else {
                                	 mc.player.motionY += 0.2f;
                                     mc.player.onGround = true;
                            		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                      mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                      mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                                 }
                             }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                    if (args[1].equals("down")) {
                        for (int i = 1; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = (float)(-i - 1);
                                break;
                            }
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                                return;
                            }
                        }
                        float endX = (float) mc.player.posX - 0.5f;
                        float endY = (float) ((float) mc.player.posY + y - 2);
                        float endZ = (float) mc.player.posZ + 0.5f;
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                            	}
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                    if (args[1].equals("up")) {
                        for (int i = 4; i < 255; ++i) {
                            if (this.mc.world.getBlockState(new BlockPos((Entity)this.mc.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                                y = (float)(i + 1);
                                break;
                            }
                        }
                        float endX = (float) mc.player.posX - 0.5f;
                        float endY = (float) ((float) mc.player.posY + y - 2);
                        float endZ = (float) mc.player.posZ + 0.5f;
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                            	}
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                    if (args[1].equals("+")) {
                    	float endX = (int) mc.player.posX;
                        float endY = (float) ((float) mc.player.posY + Double.parseDouble(args[2]));
                        float endZ = (int) mc.player.posZ;
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                                     
                           	 }
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                    if (args[1].equals("-")) {
                    	float endX = (int) mc.player.posX;
                        float endY = (float) ((float) mc.player.posY - Double.parseDouble(args[2]));
                        float endZ = (int) mc.player.posZ;
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                            	}
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                } catch (Exception ignored) {
                }
            }
            if (args[0].equalsIgnoreCase("rwhclip")) {
                double x = mc.player.posX;
                double y = mc.player.posY;
                double z = mc.player.posZ;
                double yaw = mc.player.rotationYaw * 0.017453292;
                try {
                    if (args[1].equals("+")) {
                    	float endX = (int) ((float) x - Math.sin(yaw) * Double.parseDouble(args[2]));
                        float endY = (float) y;
                        float endZ = (int) ((float) z + Math.cos(yaw) * Double.parseDouble(args[2]));
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                            	}
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                    if (args[1].equals("-")) {
                    	float endX = (int) ((float) x + Math.sin(yaw) * Double.parseDouble(args[2]));
                        float endY = (float) y;
                        float endZ = (int) ((float) z - Math.cos(yaw) * Double.parseDouble(args[2]));
                        Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
                        if (mc.player.posX != endX && mc.player.posZ != endZ) {
                       	 BlockPos feet = new BlockPos(endX, endY, endZ);
                            BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 0.1, feet.getZ());
                            if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                           	 for (int i = 0; i < 7; i++) {
                           		mc.player.motionY = 10;
                                    mc.player.onGround = true;
                           		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                     mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true));
                            	}
                            } else {
                           	 mc.player.motionY += 0.2f;
                                mc.player.onGround = true;
                       		  mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.1, endY, endZ - 0.1, false));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 10, endY, endZ - 10, true)); 
                            }
                        }
                             if(mc.player.posX == endX && mc.player.posZ == endZ) {
                             Main.msg("§aУспешное телепортирование!", true);
                             }
                    }
                } catch (Exception ignored) {
                }
            }
        } else {
            Main.msg(getUsage(),true);
        }
    }
}
