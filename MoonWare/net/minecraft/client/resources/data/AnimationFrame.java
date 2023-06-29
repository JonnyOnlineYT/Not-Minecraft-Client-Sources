package net.minecraft.client.resources.data;

public class AnimationFrame
{
    private final int frameIndex;
    private final int frameTime;

    public AnimationFrame(int frameIndexIn)
    {
        this(frameIndexIn, -1);
    }

    public AnimationFrame(int frameIndexIn, int frameTimeIn)
    {
        frameIndex = frameIndexIn;
        frameTime = frameTimeIn;
    }

    public boolean hasNoTime()
    {
        return frameTime == -1;
    }

    public int getFrameTime()
    {
        return frameTime;
    }

    public int getFrameIndex()
    {
        return frameIndex;
    }
}
