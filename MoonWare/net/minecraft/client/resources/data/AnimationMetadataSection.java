package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class AnimationMetadataSection implements IMetadataSection
{
    private final List<AnimationFrame> animationFrames;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameTime;
    private final boolean interpolate;

    public AnimationMetadataSection(List<AnimationFrame> animationFramesIn, int frameWidthIn, int frameHeightIn, int frameTimeIn, boolean interpolateIn)
    {
        animationFrames = animationFramesIn;
        frameWidth = frameWidthIn;
        frameHeight = frameHeightIn;
        frameTime = frameTimeIn;
        interpolate = interpolateIn;
    }

    public int getFrameHeight()
    {
        return frameHeight;
    }

    public int getFrameWidth()
    {
        return frameWidth;
    }

    public int getFrameCount()
    {
        return animationFrames.size();
    }

    public int getFrameTime()
    {
        return frameTime;
    }

    public boolean isInterpolate()
    {
        return interpolate;
    }

    private AnimationFrame getAnimationFrame(int frame)
    {
        return animationFrames.get(frame);
    }

    public int getFrameTimeSingle(int frame)
    {
        AnimationFrame animationframe = getAnimationFrame(frame);
        return animationframe.hasNoTime() ? frameTime : animationframe.getFrameTime();
    }

    public boolean frameHasTime(int frame)
    {
        return !animationFrames.get(frame).hasNoTime();
    }

    public int getFrameIndex(int frame)
    {
        return animationFrames.get(frame).getFrameIndex();
    }

    public Set<Integer> getFrameIndexSet()
    {
        Set<Integer> set = Sets.newHashSet();

        for (AnimationFrame animationframe : animationFrames)
        {
            set.add(Integer.valueOf(animationframe.getFrameIndex()));
        }

        return set;
    }
}
