package org.spray.infinity.features.module.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.RenderEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.Timer;
import org.spray.infinity.utils.block.BlockUtil;
import org.spray.infinity.utils.render.WorldRender;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(category = Category.VISUAL, desc = "View ore blocks", key = -2, name = "XRay", visible = true)
public class XRay extends Module {

	private ArrayList<Block> blocks = new ArrayList<>();

	private ArrayList<BlockPos> oreBlocks = new ArrayList<>();
	private ArrayList<BlockPos> clickedBlocks = new ArrayList<>();

	// render
	private ArrayList<BlockPos> renderBlocks = new ArrayList<>();
	private BlockPos progressBlock;

	// orebfuscator
	private Setting orebfuscator = new Setting(this, "Orebfuscator", false).setColor(new Color(134, 188, 241));
	public Setting noRender = new Setting(this, "No Render", true).setVisible(() -> orebfuscator.isToggle());
	private Setting radius = new Setting(this, "Radius", 10.0D, 1.0D, 50.0D).setVisible(() -> orebfuscator.isToggle());
	private Setting up = new Setting(this, "Up Distance", 10.0D, 1.0D, 50.0D).setVisible(() -> orebfuscator.isToggle());
	private Setting down = new Setting(this, "Down Distance", 10.0D, 1.0D, 50.0D)
			.setVisible(() -> orebfuscator.isToggle());

	public Setting block = new Setting(this, "Blocks", blocks,
			new ArrayList<Block>(Arrays.asList(Blocks.DIAMOND_ORE, Blocks.COAL_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE,
					Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
					Blocks.REDSTONE_ORE, Blocks.ORANGE_CONCRETE, Blocks.STONE, Blocks.DIAMOND_BLOCK, Blocks.GOLD_BLOCK,
					Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_SLAB, Blocks.NETHER_BRICK_STAIRS,
					Blocks.NETHER_BRICK_WALL, Blocks.NETHER_PORTAL, Blocks.NETHERITE_BLOCK, Blocks.BEACON,
					Blocks.BEDROCK, Blocks.BIRCH_PLANKS, Blocks.SAND, Blocks.LOOM, Blocks.COMPOSTER, Blocks.BARREL,
					Blocks.SMOKER, Blocks.HONEYCOMB_BLOCK, Blocks.HONEY_BLOCK, Blocks.SOUL_CAMPFIRE, Blocks.BEE_NEST,
					Blocks.SLIME_BLOCK, Blocks.ANVIL, Blocks.BOOKSHELF, Blocks.JACK_O_LANTERN, Blocks.OBSIDIAN,
					Blocks.OBSERVER, Blocks.QUARTZ_BLOCK, Blocks.GILDED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE,
					Blocks.BLUE_ICE, Blocks.SEA_LANTERN, Blocks.REDSTONE_LAMP, Blocks.SPONGE, Blocks.WET_SPONGE,
					Blocks.TNT, Blocks.CHEST, Blocks.FURNACE, Blocks.JUKEBOX, Blocks.NOTE_BLOCK, Blocks.ANCIENT_DEBRIS,
					Blocks.CRYING_OBSIDIAN, Blocks.AMETHYST_BLOCK, Blocks.AMETHYST_CLUSTER, Blocks.COPPER_ORE,
					Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
					Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE,
					Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE)));

	private Timer updater = new Timer();
	private Timer find = new Timer();

	private BlockPos currentBlock = new BlockPos(-1, -1, -1);
	private float breakProgress;

	@Override
	public void onEnable() {
		Helper.MC.worldRenderer.reload();

		if (!clickedBlocks.isEmpty())
			clickedBlocks.clear();
	}

	@Override
	public void onDisable() {
		clickedBlocks.clear();
		oreBlocks.clear();
		renderBlocks.clear();

		progressBlock = null;

		Helper.MC.worldRenderer.reload();
	}

	@Override
	public void onUpdate() {
		if (orebfuscator.isToggle()) {

			if (Helper.getPlayer().isCreative() || Helper.getPlayer().isSpectator())
				return;

			if (find.hasReached(3000)) {
				renderBlocks.clear();
				for (BlockPos pos : BlockUtil.getAllInBox(
						(int) (Helper.getPlayer().getPos().x - radius.getCurrentValueDouble()),
						(int) (Helper.getPlayer().getPos().y - down.getCurrentValueDouble()),
						(int) (Helper.getPlayer().getPos().z - radius.getCurrentValueDouble()),
						(int) (Helper.getPlayer().getPos().x + radius.getCurrentValueDouble()),
						(int) (Helper.getPlayer().getPos().y + up.getCurrentValueDouble()),
						(int) (Helper.getPlayer().getPos().z + radius.getCurrentValueDouble()))) {

					for (Block valid : block.getBlocks()) {
						if (BlockUtil.getBlock(pos) == valid && !renderBlocks.contains(pos))
							renderBlocks.add(pos);
					}
				}
				find.reset();
			}

			if (oreBlocks.isEmpty()) {

				if (updater.hasReached(3000)) {
					for (BlockPos pos : BlockUtil.getAllInBox(
							(int) (Helper.getPlayer().getPos().x - radius.getCurrentValueDouble()),
							(int) (Helper.getPlayer().getPos().y - down.getCurrentValueDouble()),
							(int) (Helper.getPlayer().getPos().z - radius.getCurrentValueDouble()),
							(int) (Helper.getPlayer().getPos().x + radius.getCurrentValueDouble()),
							(int) (Helper.getPlayer().getPos().y + up.getCurrentValueDouble()),
							(int) (Helper.getPlayer().getPos().z + radius.getCurrentValueDouble()))) {

						for (Block valid : block.getBlocks()) {
							if (BlockUtil.getBlock(pos) == valid && !clickedBlocks.contains(pos))
								oreBlocks.add(pos);
						}
					}
					updater.reset();
				}
			}
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {

			for (BlockPos pos : oreBlocks) {
				if (clickBlock(pos, Helper.getPlayer().getHorizontalFacing())) {
					if (!clickedBlocks.contains(pos))
						clickedBlocks.add(pos);
				}
			}
		}
	}

	@EventTarget
	public void onWorldRender(RenderEvent event) {
		if (progressBlock != null) {

		}

		if (renderBlocks.isEmpty())
			return;

		for (BlockPos renderPos : renderBlocks) {
			WorldRender.drawBox(renderPos, 1, orebfuscator.getColor().getRGB());
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (!event.getType().equals(EventType.RECIEVE))
			return;
	}

	private boolean clickBlock(BlockPos pos, Direction direction) {
		BlockState blockState2;

		if (Helper.getPlayer().isCreative()) {
			blockState2 = Helper.getWorld().getBlockState(pos);
			Helper.MC.getTutorialManager().onBlockBreaking(Helper.getWorld(), pos, blockState2, 1.0F);
			Helper.sendPacket(
					new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));
		}

		blockState2 = Helper.getWorld().getBlockState(pos);
		Helper.MC.getTutorialManager().onBlockBreaking(Helper.getWorld(), pos, blockState2, 0.0F);
		Helper.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));
		boolean bl = !blockState2.isAir();
		if (bl && this.breakProgress == 0.0F) {
			blockState2.onBlockBreakStart(Helper.getWorld(), pos, Helper.getPlayer());
		}
		breakProgress = 0.0f;
		this.currentBlock = pos;

		Helper.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, this.currentBlock,
				direction));
		oreBlocks.remove(pos);

		if (oreBlocks.isEmpty())
			progressBlock = null;
		else
			progressBlock = pos;

		return true;
	}

	public boolean isValid(Block block1) {
		return !isEnabled() || block.getBlocks().contains(block1);
	}

	public boolean isNoRender() {
		return orebfuscator.isToggle() && !noRender.isToggle();

	}

}