package org.spray.heaven.features.module.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;


@ModuleInfo(name = "WebLeave", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class WebLeave extends Module {

    private final Setting pulse = register(new Setting("Pulse", 8, 0, 8));
    private final Setting spaceHolder = register(new Setting("Jump Holder", true));

    private boolean jumped;

    @Override
    public void onDisable() {
        jumped = false;
    }

    @Override
    public void onUpdate() {
        if (!jumped && mc.gameSettings.keyBindJump.isPressed() && mc.player.isInWeb)
            jumped = true;

        boolean hold = !spaceHolder.isToggle() || jumped;

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;

        if (mc.world.getBlockState(new BlockPos(x, y - 0.2D, z)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 0.2D, z)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y - 0.2D, z - 0.30000001192092896D)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 0.2D, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 0.2D, z)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y - 0.2D, z + 0.30000001192092896D)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 0.2D, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 0.2D, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 0.2D, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y, z)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y, z - 0.30000001192092896D)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y, z)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y, z + 0.30000001192092896D)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB) {

            if (spaceHolder.isToggle() && mc.world.getBlockState(new BlockPos(x, y + 0.01, z)).getBlock() != Blocks.AIR) {
                mc.player.motionY++;
            }

            if (mc.player.fallDistance == 0.0F && hold) {
                ++mc.player.motionY;
            }
        }

        if ((mc.world.getBlockState(new BlockPos(x, y - 1.001D, z)).getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 1.001D, z)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y - 1.001D, z - 0.30000001192092896D)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 1.001D, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 1.001D, z)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x, y - 1.001D, z + 0.30000001192092896D)).getBlock() ==
                        Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 1.001D, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x + 0.30000001192092896D, y - 1.001D, z - 0.30000001192092896D))
                        .getBlock() == Blocks.WEB ||
                mc.world.getBlockState(new BlockPos(x - 0.30000001192092896D, y - 1.001D, z + 0.30000001192092896D))
                        .getBlock() == Blocks.WEB) &&
                mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) {
            if ((double) mc.player.fallDistance <= 0.1D && hold) {
                mc.player.motionY += (float) pulse.getValue() - 0.4D - 1.98D;
                jumped = false;
            }
        }
    }
}
