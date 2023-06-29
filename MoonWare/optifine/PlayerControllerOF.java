package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerControllerOF extends PlayerControllerMP
{
    private boolean acting;
    private BlockPos lastClickBlockPos;
    private Entity lastClickEntity;

    public PlayerControllerOF(Minecraft p_i72_1_, NetHandlerPlayClient p_i72_2_)
    {
        super(p_i72_1_, p_i72_2_);
    }

    /**
     * Called when the player is hitting a block with an item.
     */
    public boolean clickBlock(BlockPos loc, EnumFacing face)
    {
        acting = true;
        lastClickBlockPos = loc;
        boolean flag = super.clickBlock(loc, face);
        acting = false;
        return flag;
    }

    public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {
        acting = true;
        lastClickBlockPos = posBlock;
        boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        acting = false;
        return flag;
    }

    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, EnumHand stack)
    {
        acting = true;
        EnumActionResult enumactionresult = super.processRightClick(player, worldIn, stack);
        acting = false;
        return enumactionresult;
    }

    public EnumActionResult processRightClickBlock(EntityPlayerSP player, ClientLevel worldIn, BlockPos stack, EnumFacing pos, Vec3d facing, EnumHand vec)
    {
        acting = true;
        lastClickBlockPos = stack;
        EnumActionResult enumactionresult = super.processRightClickBlock(player, worldIn, stack, pos, facing, vec);
        acting = false;
        return enumactionresult;
    }

    /**
     * Handles right clicking an entity, sends a packet to the server.
     */
    public EnumActionResult interactWithEntity(EntityPlayer player, Entity target, EnumHand heldItem)
    {
        lastClickEntity = target;
        return super.interactWithEntity(player, target, heldItem);
    }

    /**
     * Handles right clicking an entity from the entities side, sends a packet to the server.
     */
    public EnumActionResult interactWithEntity(EntityPlayer player, Entity target, RayTraceResult raytrace, EnumHand heldItem)
    {
        lastClickEntity = target;
        return super.interactWithEntity(player, target, raytrace, heldItem);
    }

    public boolean isActing()
    {
        return acting;
    }

    public BlockPos getLastClickBlockPos()
    {
        return lastClickBlockPos;
    }

    public Entity getLastClickEntity()
    {
        return lastClickEntity;
    }
}
