package ua.apraxia.modules.impl.move;


import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventPreMotion;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.math.TimerUtility;
import ua.apraxia.utility.other.MoveUtility;

public class Speed extends Module {
    public static float ticks = 0;
    public TimerUtility timerHelper = new TimerUtility();
    private final ModeSetting speedMode = new ModeSetting("Speed Mode", "Sunrise", "Sunrise2");

    public Speed() {
        super("Speed", Categories.Movement);
        addSetting(speedMode);

    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (speedMode.is("Sunrise2")) {
           if (MoveUtility.isMoving()) {
               if (mc.player.onGround) {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 9.5 / 24.5, 0, Math.cos(MoveUtility.getAllDirection()) * 9.5 / 28.5);
               } else if (mc.player.isInWater()) {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 8.5 / 24.5, mc.gameSettings.keyBindJump.pressed ? (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 4, mc.player.posZ)).getBlock() == Blocks.AIR) ? 0 : mc.gameSettings.keyBindSneak.pressed ? 0 : mc.player.collidedHorizontally ? 0.2 : 0.2 : mc.gameSettings.keyBindSneak.pressed ? -0.2f : 0, Math.cos(MoveUtility.getAllDirection()) * 8.5 / 24.5);
               } else if (!mc.player.onGround) {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 0.11 / 24.5, 0, Math.cos(MoveUtility.getAllDirection()) * 0.11 / 28.5);
               } else {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 0.005 * MoveUtility.getSpeed(), 0,
                           Math.cos(MoveUtility.getAllDirection()) * 0.005 * MoveUtility.getSpeed());
               }
               if (mc.player.isInWater() && (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)).getBlock() == Blocks.AIR) && mc.player.motionY > 0.5f) {
                   mc.player.motionY = 0;
               }
               MoveUtility.strafe(MoveUtility.getSpeed());
           }
       } if (speedMode.is("Sunrise")) {
           if (MoveUtility.isMoving()) {
               if (mc.player.onGround) {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MoveUtility.getAllDirection()) * 9.5D / 24.5D);
                   MoveUtility.strafe();
               } else if (mc.player.isInWater()) {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MoveUtility.getAllDirection()) * 9.5D / 24.5D);
                   MoveUtility.strafe();
               } else {
                   mc.player.addVelocity(-Math.sin(MoveUtility.getAllDirection()) * 0.005D * MoveUtility.getSpeed(), 0, Math.cos(MoveUtility.getAllDirection()) * 0.005 *MoveUtility.getSpeed());
                   MoveUtility.strafe();

               }
           }
       }
    }
    private int findarmor() {
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getStack() != null && mc.player.inventoryContainer.getSlot(i).getStack().getDisplayName().contains("chestplate")) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onDisable() {

        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}