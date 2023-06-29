package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class WorldBorder
{
    private final List<IBorderListener> listeners = Lists.newArrayList();
    private double centerX;
    private double centerZ;
    private double startDiameter = 6.0E7D;
    private double endDiameter;
    private long endTime;
    private long startTime;
    private int worldSize;
    private double damageAmount;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;

    public WorldBorder()
    {
        endDiameter = startDiameter;
        worldSize = 29999984;
        damageAmount = 0.2D;
        damageBuffer = 5.0D;
        warningTime = 15;
        warningDistance = 5;
    }

    public boolean contains(BlockPos pos)
    {
        return (double)(pos.getX() + 1) > minX() && (double)pos.getX() < maxX() && (double)(pos.getZ() + 1) > minZ() && (double)pos.getZ() < maxZ();
    }

    public boolean contains(ChunkPos range)
    {
        return (double)range.getXEnd() > minX() && (double)range.getXStart() < maxX() && (double)range.getZEnd() > minZ() && (double)range.getZStart() < maxZ();
    }

    public boolean contains(AxisAlignedBB bb)
    {
        return bb.maxX > minX() && bb.minX < maxX() && bb.maxZ > minZ() && bb.minZ < maxZ();
    }

    public double getClosestDistance(Entity entityIn)
    {
        return getClosestDistance(entityIn.posX, entityIn.posZ);
    }

    public double getClosestDistance(double x, double z)
    {
        double d0 = z - minZ();
        double d1 = maxZ() - z;
        double d2 = x - minX();
        double d3 = maxX() - x;
        double d4 = Math.min(d2, d3);
        d4 = Math.min(d4, d0);
        return Math.min(d4, d1);
    }

    public EnumBorderStatus getStatus()
    {
        if (endDiameter < startDiameter)
        {
            return EnumBorderStatus.SHRINKING;
        }
        else
        {
            return endDiameter > startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY;
        }
    }

    public double minX()
    {
        double d0 = getCenterX() - getDiameter() / 2.0D;

        if (d0 < (double)(-worldSize))
        {
            d0 = -worldSize;
        }

        return d0;
    }

    public double minZ()
    {
        double d0 = getCenterZ() - getDiameter() / 2.0D;

        if (d0 < (double)(-worldSize))
        {
            d0 = -worldSize;
        }

        return d0;
    }

    public double maxX()
    {
        double d0 = getCenterX() + getDiameter() / 2.0D;

        if (d0 > (double) worldSize)
        {
            d0 = worldSize;
        }

        return d0;
    }

    public double maxZ()
    {
        double d0 = getCenterZ() + getDiameter() / 2.0D;

        if (d0 > (double) worldSize)
        {
            d0 = worldSize;
        }

        return d0;
    }

    public double getCenterX()
    {
        return centerX;
    }

    public double getCenterZ()
    {
        return centerZ;
    }

    public void setCenter(double x, double z)
    {
        centerX = x;
        centerZ = z;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onCenterChanged(this, x, z);
        }
    }

    public double getDiameter()
    {
        if (getStatus() != EnumBorderStatus.STATIONARY)
        {
            double d0 = (float)(System.currentTimeMillis() - startTime) / (float)(endTime - startTime);

            if (d0 < 1.0D)
            {
                return startDiameter + (endDiameter - startDiameter) * d0;
            }

            setTransition(endDiameter);
        }

        return startDiameter;
    }

    public long getTimeUntilTarget()
    {
        return getStatus() == EnumBorderStatus.STATIONARY ? 0L : endTime - System.currentTimeMillis();
    }

    public double getTargetSize()
    {
        return endDiameter;
    }

    public void setTransition(double newSize)
    {
        startDiameter = newSize;
        endDiameter = newSize;
        endTime = System.currentTimeMillis();
        startTime = endTime;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onSizeChanged(this, newSize);
        }
    }

    public void setTransition(double oldSize, double newSize, long time)
    {
        startDiameter = oldSize;
        endDiameter = newSize;
        startTime = System.currentTimeMillis();
        endTime = startTime + time;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onTransitionStarted(this, oldSize, newSize, time);
        }
    }

    protected List<IBorderListener> getListeners()
    {
        return Lists.newArrayList(listeners);
    }

    public void addListener(IBorderListener listener)
    {
        listeners.add(listener);
    }

    public void setSize(int size)
    {
        worldSize = size;
    }

    public int getSize()
    {
        return worldSize;
    }

    public double getDamageBuffer()
    {
        return damageBuffer;
    }

    public void setDamageBuffer(double bufferSize)
    {
        damageBuffer = bufferSize;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onDamageBufferChanged(this, bufferSize);
        }
    }

    public double getDamageAmount()
    {
        return damageAmount;
    }

    public void setDamageAmount(double newAmount)
    {
        damageAmount = newAmount;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onDamageAmountChanged(this, newAmount);
        }
    }

    public double getResizeSpeed()
    {
        return endTime == startTime ? 0.0D : Math.abs(startDiameter - endDiameter) / (double)(endTime - startTime);
    }

    public int getWarningTime()
    {
        return warningTime;
    }

    public void setWarningTime(int warningTime)
    {
        this.warningTime = warningTime;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onWarningTimeChanged(this, warningTime);
        }
    }

    public int getWarningDistance()
    {
        return warningDistance;
    }

    public void setWarningDistance(int warningDistance)
    {
        this.warningDistance = warningDistance;

        for (IBorderListener iborderlistener : getListeners())
        {
            iborderlistener.onWarningDistanceChanged(this, warningDistance);
        }
    }
}
