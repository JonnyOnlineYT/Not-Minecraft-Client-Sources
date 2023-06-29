package Celestial.module.impl.Movement;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventPreMotion;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.movement.MovementUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;

public class elytraleave<elytra> extends Module {


    private int lastSlot;
    private int boostTicks = 0;


    public elytraleave() {
        super("ElytraLeaveSunrise", "��������� ������ �� ���� � �������� �� ��������", ModuleCategory.Movement);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        int eIndex = -1;

        for (int i = 0; i < 45; i++)
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                if (Helper.mc.player.isElytraFlying()) {
                    Helper.mc.player.onGround = false;
                    //   mc.player.setVelocity(0.0, 1.0, 0.0);

                    if (Helper.mc.player.isElytraFlying()) {
                        Helper.mc.player.setVelocity(0.0, 1.0, 0.0);
                        boostTicks++;
                    }
                    if (Helper.mc.player.isElytraFlying() && boostTicks > 5) {
                        Helper.mc.player.setVelocity(0.0, 1.1, 0.0);
                        boostTicks++;
                    }


                    MovementUtils.setSpeed(0.5);
                }
            }


        for (int i = 0; i < 45; i++)
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                if (Helper.mc.player.onGround) {
                    Helper.mc.player.jump();
                }
            }

        for (int i = 0; i < 45; i++)
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                if (Helper.mc.player.isAirBorne) {
                    if (Helper.mc.player.getFoodStats().getFoodLevel() / 2 > 3) {
                        Helper.mc.player.setSprinting(MovementUtils.isMoving());
                        Helper.mc.player.connection.sendPacket(new CPacketEntityAction(Helper.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                }
            }
    }


    @Override
    public void onEnable() {
        onswapenable();
        super.onEnable();
    }


    @Override
    public void onDisable() {
        this.boostTicks = 0;
        onswapdisable();
        super.onDisable();
    }

    private int onswapdisable() {
        int eIndex = -1;

        for (int i = 0; i < 45; i++)
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
                Helper.mc.playerController.windowClick(0, this.findarmor(), 0, ClickType.PICKUP, Helper.mc.player);
                Helper.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Helper.mc.player);
                return 0;
            }
        return eIndex;
    }

    private int onswapenable() {
        int eIndex = -1;
        int slot = this.findElytraSlot();
        this.lastSlot = this.findFreeSlot();
        for (int i = 0; i < 45; i++)
            if (Helper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                Helper.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Helper.mc.player);
                Helper.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Helper.mc.player);
                Helper.mc.playerController.windowClick(0, 9, 1, ClickType.PICKUP, Helper.mc.player);
                return 0;
            }
        return eIndex;
    }





    private int findElytraSlot() {
        for (int i = 0; i < 45; ++i) {
            if (Helper.mc.player.inventoryContainer.getSlot(i).getStack() != null && Helper.mc.player.inventoryContainer.getSlot(i).getStack().getItem() == Items.ELYTRA) {
                return i;
            }
        }

        return -1;
    }

    private int findFreeSlot() {
        for(int i = 0; i < 45; ++i) {
            if (Helper.mc.player.inventoryContainer.getSlot(i).getStack() == null || Helper.mc.player.inventoryContainer.getSlot(i).getStack().getItem() == Items.field_190931_a) {
                return i;
            }
        }

        return -1;
    }

    private int findarmor() {
        for(int i = 0; i < 45; ++i) {
            if (Helper.mc.player.inventoryContainer.getSlot(i).getStack() != null && Helper.mc.player.inventoryContainer.getSlot(i).getStack().getUnlocalizedName().contains("chestplate")) {
                return i;
            }
        }

        return -1;
    }
}