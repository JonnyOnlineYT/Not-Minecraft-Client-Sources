package ua.apraxia.utility.animation;

import ua.apraxia.utility.math.TimerUtility;

public abstract class Animation {

    public TimerUtility timerUtil = new TimerUtility();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public Animation(int ms, double endPoint) {
        duration = ms;
        this.endPoint = endPoint;
        direction = Direction.FORWARDS;
    }

    public Animation(int ms, double endPoint, Direction direction) {
        duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean finished(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public double getLinearOutput() {
        return 1 - ((timerUtil.getTime() / (double) duration) * endPoint);
    }

    public double getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public void reset() {
        timerUtil.reset();
    }

    public boolean isDone() {
        return timerUtil.hasReached(duration);
    }

    public void changeDirection() {
        setDirection(direction.opposite());
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            timerUtil.hasReached(System.currentTimeMillis() - (duration - Math.min(duration, timerUtil.getTime())));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean correctOutput() {
        return false;
    }

    public long getTimePassed() {
        return timerUtil.getTime();
    }

    public double getOutput() {
        if (direction == Direction.FORWARDS) {
            if (isDone())
                return endPoint;
            return (getEquation(timerUtil.getTime()) * endPoint);
        } else {
            if (isDone()) return 0;
            if (correctOutput()) {
                double revTime = Math.min(duration, Math.max(0, duration - timerUtil.getTime()));
                return getEquation(revTime) * endPoint;
            } else return (1 - getEquation(timerUtil.getTime())) * endPoint;
        }
    }

    protected abstract double getEquation(double x);

}