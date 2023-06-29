package org.spray.heaven.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector3d;

import org.spray.heaven.main.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class PathUtil {

	public static List<Vec3d> findBlinkPath(final double tpX, final double tpY, final double tpZ) {
		return findBlinkPath(tpX, tpY, tpZ, 5);
	}

	public static List<Vec3d> findBlinkPath(final double tpX, final double tpY, final double tpZ, final double dist) {
		return findBlinkPath(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, tpX, tpY,
				tpZ, dist);
	}

	public static List<Vec3d> findBlinkPath(double curX, double curY, double curZ, final double tpX, final double tpY,
			final double tpZ, final double dashDistance) {
		Vec3d topFrom = new Vec3d(curX, curY, curZ);
		Vec3d to = new Vec3d(tpX, tpY, tpZ);

		AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
		pathfinder.compute();

		int i = 0;
		Vec3d lastLoc = null;
		Vec3d lastDashLoc = null;
		ArrayList<Vec3d> path = new ArrayList<>();
		ArrayList<Vec3d> pathFinderPath = pathfinder.getPath();
		for (Vec3d pathElm : pathFinderPath) {
			if (i == 0 || i == pathFinderPath.size() - 1) {
				if (lastLoc != null) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
				}
				path.add(pathElm.addVector(0.5, 0, 0.5));
				lastDashLoc = pathElm;
			} else {
				boolean canContinue = true;
					double smallX = Math.min(lastDashLoc.xCoord, pathElm.xCoord);
					double smallY = Math.min(lastDashLoc.yCoord, pathElm.yCoord);
					double smallZ = Math.min(lastDashLoc.zCoord, pathElm.zCoord);
					double bigX = Math.max(lastDashLoc.xCoord, pathElm.xCoord);
					double bigY = Math.max(lastDashLoc.yCoord, pathElm.yCoord);
					double bigZ = Math.max(lastDashLoc.zCoord, pathElm.zCoord);
					cordsLoop: for (int x = (int) smallX; x <= bigX; x++) {
						for (int y = (int) smallY; y <= bigY; y++) {
							for (int z = (int) smallZ; z <= bigZ; z++) {
								if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
									canContinue = false;
									break cordsLoop;
								}
							}
						}
					
				}
				if (!canContinue) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
					lastDashLoc = lastLoc;
				}
			}
			lastLoc = pathElm;
			i++;
		}

		return path;
	}

	public static List<Vector3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
		final List<Vector3d> positions = new ArrayList<>();
		final double steps = Math.ceil(
				getDistance(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ, tpX, tpY, tpZ)
						/ offset);

		final double dX = tpX - Wrapper.getPlayer().posX;
		final double dY = tpY - Wrapper.getPlayer().posY;
		final double dZ = tpZ - Wrapper.getPlayer().posZ;

		for (double d = 1D; d <= steps; ++d) {
			positions.add(new Vector3d(Wrapper.getPlayer().posX + (dX * d) / steps,
					Wrapper.getPlayer().posY + (dY * d) / steps, Wrapper.getPlayer().posZ + (dZ * d) / steps));
		}

		return positions;
	}

	private static double getDistance(final double x1, final double y1, final double z1, final double x2,
			final double y2, final double z2) {
		final double xDiff = x1 - x2;
		final double yDiff = y1 - y2;
		final double zDiff = z1 - z2;
		return MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
	}
}