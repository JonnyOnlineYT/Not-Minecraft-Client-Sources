//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.movement;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.util.BlockUtil;
import net.minecraft.block.Block;
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
            if (mc.player != null && mc.world != null) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                        ;
                  }

                  if (Minecraft.getMinecraft().player.onGround && !Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                        Block var10000 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.getPositionVector().add(new Vec3d(0.0D, -0.5D, 0.0D)))).getBlock();
                        if ((47481314 >> 2 & 4904716 ^ 7 ^ 1234465408) != 0) {
                              ;
                        }

                        if (!BlockUtil.isCollidable(var10000)) {
                              KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), (boolean)(0 << 3 >>> 4 ^ 1));
                              return;
                        }
                  }

                  if (((4718592 >> 4 | 232386) ^ 494530) == 0) {
                        ;
                  }

                  if (!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), (boolean)((85061319 >> 4 & 1330192 ^ 1033163) << 1 ^ 4169622));
                  }

            }
      }

      public void onDisable() {
            if (mc.player != null && mc.world != null) {
                  super.onDisable();
                  int var10000 = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
                  GameSettings var10001 = Minecraft.getMinecraft().gameSettings;
                  KeyBinding.setKeyBindState(var10000, GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak));
            } else {
                  if (((1615906869 | 381800023) ^ 1882849515 ^ 116117148) == 0) {
                        ;
                  }

                  if (((769451837 ^ 297103528 | 934756537) << 2 << 1 ^ 277075901) != 0) {
                        ;
                  }

            }
      }

      public SafeWalk() {
            if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            super("SafeWalk", "Makes you sneak on edges.", (1933431192 | 149195807) >> 4 >>> 1 << 2 ^ 260029360, Module.Category.MOVEMENT);
      }
}
