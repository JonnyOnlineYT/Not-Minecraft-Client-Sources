package org.spray.heaven.util;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class AStarCustomPathFinder {
	private Vec3d startVec3d;
	private Vec3d endVec3d;
	private ArrayList<Vec3d> path = new ArrayList<>();
	private ArrayList<Hub> hubs = new ArrayList<>();
	private ArrayList<Hub> hubsToWork = new ArrayList<>();
	private double minDistanceSquared = 9;
	private boolean nearest = true;

	private static final Vec3d[] flatCardinalDirections = { new Vec3d(1, 0, 0), new Vec3d(-1, 0, 0), new Vec3d(0, 0, 1),
			new Vec3d(0, 0, -1) };

	public AStarCustomPathFinder(Vec3d startVec3d, Vec3d endVec3d) {
		this.startVec3d = BlockUtil.floorVec3d(startVec3d.addVector(0, 0, 0));
		this.endVec3d = BlockUtil.floorVec3d(endVec3d.addVector(0, 0, 0));
	}

	public ArrayList<Vec3d> getPath() {
		return path;
	}

	public void compute() {
		compute(1000, 4);
	}

	public void compute(int loops, int depth) {
		path.clear();
		hubsToWork.clear();
		ArrayList<Vec3d> initPath = new ArrayList<>();
		initPath.add(startVec3d);
		hubsToWork.add(new Hub(startVec3d, null, initPath, startVec3d.squareDistanceTo(endVec3d), 0, 0));
		search: for (int i = 0; i < loops; i++) {
			hubsToWork.sort(new CompareHub());
			int j = 0;
			if (hubsToWork.size() == 0) {
				break;
			}
			for (Hub hub : new ArrayList<>(hubsToWork)) {
				j++;
				if (j > depth) {
					break;
				} else {
					hubsToWork.remove(hub);
					hubs.add(hub);

					for (Vec3d direction : flatCardinalDirections) {
						Vec3d loc = BlockUtil.floorVec3d(hub.getLoc().add(direction));
						if (checkPositionValidity(loc, false)) {
							if (addHub(hub, loc, 0)) {
								break search;
							}
						}
					}

					Vec3d loc1 = BlockUtil.floorVec3d(hub.getLoc().addVector(0, 1, 0));
					if (checkPositionValidity(loc1, false)) {
						if (addHub(hub, loc1, 0)) {
							break search;
						}
					}

					Vec3d loc2 = BlockUtil.floorVec3d(hub.getLoc().addVector(0, -1, 0));
					if (checkPositionValidity(loc2, false)) {
						if (addHub(hub, loc2, 0)) {
							break search;
						}
					}
				}
			}
		}
		if (nearest) {
			hubs.sort(new CompareHub());
			path = hubs.get(0).getPath();
		}
	}

	public static boolean checkPositionValidity(Vec3d loc, boolean checkGround) {
		return checkPositionValidity((int) loc.xCoord, (int) loc.yCoord, (int) loc.zCoord, checkGround);
	}

	public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
		BlockPos block1 = new BlockPos(x, y, z);
		BlockPos block2 = new BlockPos(x, y + 1, z);
		BlockPos block3 = new BlockPos(x, y - 1, z);
		return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround)
				&& isSafeToWalkOn(block3);
	}

	private static boolean isBlockSolid(BlockPos blockPos) {
		Block block = BlockUtil.getBlock(blockPos);
		if (block == null)
			return false;

		return (block instanceof BlockSlab) || (block instanceof BlockStairs) || (block instanceof BlockCactus)
				|| (block instanceof BlockChest) || (block instanceof BlockEnderChest) || (block instanceof BlockSkull)
				|| (block instanceof BlockPane) || (block instanceof BlockFence) || (block instanceof BlockWall)
				|| (block instanceof BlockGlass) || (block instanceof BlockPistonBase)
				|| (block instanceof BlockPistonExtension) || (block instanceof BlockPistonMoving)
				|| (block instanceof BlockStainedGlass) || (block instanceof BlockTrapDoor);
	}

	private static boolean isSafeToWalkOn(BlockPos blockPos) {
		Block block = BlockUtil.getBlock(blockPos);
		if (block == null)
			return false;

		return !(block instanceof BlockFence) && !(block instanceof BlockWall);
	}

	public Hub isHubExisting(Vec3d loc) {
		for (Hub hub : hubs) {
			if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord
					&& hub.getLoc().zCoord == loc.zCoord) {
				return hub;
			}
		}
		for (Hub hub : hubsToWork) {
			if (hub.getLoc().xCoord == loc.xCoord && hub.getLoc().yCoord == loc.yCoord
					&& hub.getLoc().zCoord == loc.zCoord) {
				return hub;
			}
		}
		return null;
	}

	public boolean addHub(Hub parent, Vec3d loc, double cost) {
		Hub existingHub = isHubExisting(loc);
		double totalCost = cost;
		if (parent != null) {
			totalCost += parent.getTotalCost();
		}
		if (existingHub == null) {
			if ((loc.xCoord == endVec3d.xCoord && loc.yCoord == endVec3d.yCoord && loc.zCoord == endVec3d.zCoord)
					|| (minDistanceSquared != 0 && loc.squareDistanceTo(endVec3d) <= minDistanceSquared)) {
				path.clear();
				path = parent.getPath();
				path.add(loc);
				return true;
			} else {
				ArrayList<Vec3d> path = new ArrayList<>(parent.getPath());
				path.add(loc);
				hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(endVec3d), cost, totalCost));
			}
		} else if (existingHub.getCost() > cost) {
			ArrayList<Vec3d> path = new ArrayList<>(parent.getPath());
			path.add(loc);
			existingHub.setLoc(loc);
			existingHub.setParent(parent);
			existingHub.setPath(path);
			existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(endVec3d));
			existingHub.setCost(cost);
			existingHub.setTotalCost(totalCost);
		}
		return false;
	}

	private class Hub {
		private Vec3d loc = null;
		private Hub parent = null;
		private ArrayList<Vec3d> path;
		private double squareDistanceToFromTarget;
		private double cost;
		private double totalCost;

		public Hub(Vec3d loc, Hub parent, ArrayList<Vec3d> path, double squareDistanceToFromTarget, double cost,
				double totalCost) {
			this.loc = loc;
			this.parent = parent;
			this.path = path;
			this.squareDistanceToFromTarget = squareDistanceToFromTarget;
			this.cost = cost;
			this.totalCost = totalCost;
		}

		public Vec3d getLoc() {
			return loc;
		}

		public Hub getParent() {
			return parent;
		}

		public ArrayList<Vec3d> getPath() {
			return path;
		}

		public double getSquareDistanceToFromTarget() {
			return squareDistanceToFromTarget;
		}

		public double getCost() {
			return cost;
		}

		public void setLoc(Vec3d loc) {
			this.loc = loc;
		}

		public void setParent(Hub parent) {
			this.parent = parent;
		}

		public void setPath(ArrayList<Vec3d> path) {
			this.path = path;
		}

		public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
			this.squareDistanceToFromTarget = squareDistanceToFromTarget;
		}

		public void setCost(double cost) {
			this.cost = cost;
		}

		public double getTotalCost() {
			return totalCost;
		}

		public void setTotalCost(double totalCost) {
			this.totalCost = totalCost;
		}
	}

	public class CompareHub implements Comparator<Hub> {
		@Override
		public int compare(Hub o1, Hub o2) {
			return (int) ((o1.getSquareDistanceToFromTarget() + o1.getTotalCost())
					- (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
		}
	}
}