//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
      @SubscribeEvent
      public void onPlayerTick(PlayerTickEvent var1) {
            if (((295128184 | 61886442) >> 3 & 11551255 ^ 343545697) != 0) {
                  ;
            }

            if (mc.player != null && mc.world != null) {
                  if (Minecraft.getMinecraft().player.onGround) {
                        GameSettings var10000 = Minecraft.getMinecraft().gameSettings;
                        if (((419932864 >> 4 << 3 | 41636848) ^ 251385840) == 0) {
                              ;
                        }

                        if (!var10000.keyBindJump.isPressed()) {
                              boolean var2 = BlockUtil.isCollidable(Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.getPositionVector().add(new Vec3d(0.0D, -0.5D, 0.0D)))).getBlock());
                              if ((399834294 << 3 & 1315242528 ^ 177036731 ^ 75039450 ^ 1826181300) != 0) {
                                    ;
                              }

                              if (!var2) {
                                    var10000 = Minecraft.getMinecraft().gameSettings;
                                    if (!"intentMoment".equals("stop skidding")) {
                                          ;
                                    }

                                    int var3 = var10000.keyBindSneak.getKeyCode();
                                    int var10001 = (0 & 1268508694 & 285405483) >>> 4 ^ 1;
                                    if (((23913710 >>> 4 & 785474 | 70998) ^ 158297941) != 0) {
                                          ;
                                    }

                                    KeyBinding.setKeyBindState(var3, (boolean)var10001);
                                    return;
                              }
                        }
                  }

                  if (!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), (boolean)((211671042 | 165918035) >>> 4 ^ 14680021));
                  }

            }
      }

      public SafeWalk() {
            super("SafeWalk", "Makes you sneak on edges.", (1002243034 << 2 & 1329952695) >> 4 << 4 >> 3 ^ 164135140, Module.Category.MOVEMENT);
      }

      public void onDisable() {
            if (mc.player != null && mc.world != null) {
                  super.onDisable();
                  Minecraft var10000 = Minecraft.getMinecraft();
                  if (!"you're dogshit".equals("intentMoment")) {
                        ;
                  }

                  int var1 = var10000.gameSettings.keyBindSneak.getKeyCode();
                  GameSettings var10001 = Minecraft.getMinecraft().gameSettings;
                  KeyBinding.setKeyBindState(var1, GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak));
            } else {
                  if ((807411730 << 4 ^ 1856792117) != 0) {
                        ;
                  }

            }
      }
}
