package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "IceSpeed", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class IceSpeed extends Module {

    //private final TimerHelper Timer = new TimerHelper();
    public static boolean go;

    @EventTarget
    public void onMotion(MotionEvent event) {
        {
            if (((mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.51, mc.player.posZ)).getBlock() == Block.getBlockById(212) ||
                    mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.51, mc.player.posZ)).getBlock() == Block.getBlockById(79) ||
                    mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.51, mc.player.posZ)).getBlock() == Block.getBlockById(174)) ||
                    (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.01, mc.player.posZ)).getBlock() == Block.getBlockById(212) ||
                            mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.01, mc.player.posZ)).getBlock() == Block.getBlockById(79) ||
                            mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.01, mc.player.posZ)).getBlock() == Block.getBlockById(174))) && mc.player.fallDistance < 1.2) {
                go = true;
            } else {
               // if (Timer.hasReached(920)) {
                 //   go = false;
                   // Timer.reset();
                }
            }
            if (go) {
                if (Wrapper.getPlayer().isPotionActive(Potion.getPotionById(1))) {
                    MovementUtil.setMotion(0.9);
                } else {
                    MovementUtil.setMotion(0.65);
                }
            }
        }
    }

