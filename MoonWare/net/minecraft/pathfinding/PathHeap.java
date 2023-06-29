package net.minecraft.pathfinding;

public class PathHeap
{
    /** Contains the points in this path */
    private PathPoint[] pathPoints = new PathPoint[128];

    /** The number of points in this path */
    private int count;

    /**
     * Adds a point to the path
     */
    public PathPoint addPoint(PathPoint point)
    {
        if (point.index >= 0)
        {
            throw new IllegalStateException("OW KNOWS!");
        }
        else
        {
            if (count == pathPoints.length)
            {
                PathPoint[] apathpoint = new PathPoint[count << 1];
                System.arraycopy(pathPoints, 0, apathpoint, 0, count);
                pathPoints = apathpoint;
            }

            pathPoints[count] = point;
            point.index = count;
            sortBack(count++);
            return point;
        }
    }

    /**
     * Clears the path
     */
    public void clearPath()
    {
        count = 0;
    }

    /**
     * Returns and removes the first point in the path
     */
    public PathPoint dequeue()
    {
        PathPoint pathpoint = pathPoints[0];
        pathPoints[0] = pathPoints[--count];
        pathPoints[count] = null;

        if (count > 0)
        {
            sortForward(0);
        }

        pathpoint.index = -1;
        return pathpoint;
    }

    /**
     * Changes the provided point's distance to target
     */
    public void changeDistance(PathPoint point, float distance)
    {
        float f = point.distanceToTarget;
        point.distanceToTarget = distance;

        if (distance < f)
        {
            sortBack(point.index);
        }
        else
        {
            sortForward(point.index);
        }
    }

    /**
     * Sorts a point to the left
     */
    private void sortBack(int index)
    {
        PathPoint pathpoint = pathPoints[index];
        int i;

        for (float f = pathpoint.distanceToTarget; index > 0; index = i)
        {
            i = index - 1 >> 1;
            PathPoint pathpoint1 = pathPoints[i];

            if (f >= pathpoint1.distanceToTarget)
            {
                break;
            }

            pathPoints[index] = pathpoint1;
            pathpoint1.index = index;
        }

        pathPoints[index] = pathpoint;
        pathpoint.index = index;
    }

    /**
     * Sorts a point to the right
     */
    private void sortForward(int index)
    {
        PathPoint pathpoint = pathPoints[index];
        float f = pathpoint.distanceToTarget;

        while (true)
        {
            int i = 1 + (index << 1);
            int j = i + 1;

            if (i >= count)
            {
                break;
            }

            PathPoint pathpoint1 = pathPoints[i];
            float f1 = pathpoint1.distanceToTarget;
            PathPoint pathpoint2;
            float f2;

            if (j >= count)
            {
                pathpoint2 = null;
                f2 = Float.POSITIVE_INFINITY;
            }
            else
            {
                pathpoint2 = pathPoints[j];
                f2 = pathpoint2.distanceToTarget;
            }

            if (f1 < f2)
            {
                if (f1 >= f)
                {
                    break;
                }

                pathPoints[index] = pathpoint1;
                pathpoint1.index = index;
                index = i;
            }
            else
            {
                if (f2 >= f)
                {
                    break;
                }

                pathPoints[index] = pathpoint2;
                pathpoint2.index = index;
                index = j;
            }
        }

        pathPoints[index] = pathpoint;
        pathpoint.index = index;
    }

    /**
     * Returns true if this path contains no points
     */
    public boolean isPathEmpty()
    {
        return count == 0;
    }
}
