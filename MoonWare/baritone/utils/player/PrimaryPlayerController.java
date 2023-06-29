/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.utils.player;

import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerController;
import baritone.utils.accessor.IPlayerControllerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

/**
 * Implementation of {@link IPlayerController} that chains to the primary player controller's methods
 *
 * @author Brady
 * @since 12/14/2018
 */
public enum PrimaryPlayerController implements IPlayerController, Helper {

    INSTANCE;

    @Override
    public void syncHeldItem() {
        ((IPlayerControllerMP) Minecraft.playerController).callSyncCurrentPlayItem();
    }

    @Override
    public boolean hasBrokenBlock() {
        return ((IPlayerControllerMP) Minecraft.playerController).getCurrentBlock().getY() == -1;
    }

    @Override
    public boolean onPlayerDamageBlock(BlockPos pos, EnumFacing side) {
        return Minecraft.playerController.onPlayerDamageBlock(pos, side);
    }

    @Override
    public void resetBlockRemoving() {
        Minecraft.playerController.resetBlockRemoving();
    }

    @Override
    public ItemStack windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player) {
        return Minecraft.playerController.windowClick(windowId, slotId, mouseButton, type, player);
    }

    @Override
    public GameType getGameType() {
        return Minecraft.playerController.getCurrentGameType();
    }

    @Override
    public EnumActionResult processRightClickBlock(EntityPlayerSP player, World world, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand) {
        return Minecraft.playerController.processRightClickBlock(player, (ClientLevel) world, pos, direction, vec, hand);
    }

    @Override
    public EnumActionResult processRightClick(EntityPlayerSP player, World world, EnumHand hand) {
        return Minecraft.playerController.processRightClick(player, world, hand);
    }

    @Override
    public boolean clickBlock(BlockPos loc, EnumFacing face) {
        return Minecraft.playerController.clickBlock(loc, face);
    }

    @Override
    public void setHittingBlock(boolean hittingBlock) {
        ((IPlayerControllerMP) Minecraft.playerController).setIsHittingBlock(hittingBlock);
    }
}
